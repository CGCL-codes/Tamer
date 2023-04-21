package org.grailrtls.aggregator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.grailrtls.sensor.SensorAggregatorInterface;
import org.grailrtls.sensor.SensorIoAdapter;
import org.grailrtls.sensor.SensorIoHandler;
import org.grailrtls.sensor.ThreadedSensorIoHandler;
import org.grailrtls.sensor.listeners.ConnectionListener;
import org.grailrtls.sensor.protocol.codecs.AggregatorSensorProtocolCodecFactory;
import org.grailrtls.sensor.protocol.messages.HandshakeMessage;
import org.grailrtls.sensor.protocol.messages.SampleMessage;
import org.grailrtls.solver.SolverIoAdapter;
import org.grailrtls.solver.SolverIoHandler;
import org.grailrtls.solver.protocol.codec.AggregatorSolverProtocolCodecFactory;
import org.grailrtls.solver.protocol.messages.SubscriptionMessage;
import org.grailrtls.solver.rules.SubscriptionRequestRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Aggregator implements SensorIoAdapter, SolverIoAdapter {

    public static final int SENSOR_LISTEN_PORT = 7007;

    public static final int SOLVER_LISTEN_PORT = 7008;

    private int samplesReceived = 0;

    private int samplesSent = 0;

    private float meanReceiveLatency = 0;

    private float meanSendLatency = 0;

    private long lastReportTime = System.currentTimeMillis();

    protected final ConcurrentHashMap<IoSession, CachingFilteringSolverInterface> solvers = new ConcurrentHashMap<IoSession, CachingFilteringSolverInterface>();

    protected final ConcurrentHashMap<IoSession, SensorInterface> sensors = new ConcurrentHashMap<IoSession, SensorInterface>();

    protected final ExecutorService handlerPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    protected long sensorTimeout = 5 * 60 * 1000;

    protected AggregatorConfiguration configuration;

    private static final Logger log = LoggerFactory.getLogger(Aggregator.class);

    public static void main(String[] args) {
        int sensorPort = SENSOR_LISTEN_PORT;
        int solverPort = SOLVER_LISTEN_PORT;
        String nextAggHost = null;
        int nextAggPort = -1;
        if (args.length > 0) {
            sensorPort = Integer.parseInt(args[0]);
            if (args.length > 1) {
                solverPort = Integer.parseInt(args[1]);
            }
            if (args.length == 4) {
                nextAggHost = args[2];
                nextAggPort = Integer.valueOf(args[3]);
            }
        }
        AggregatorConfiguration config = new AggregatorConfiguration();
        config.setSensorListenPort(sensorPort);
        config.setSolverListenPort(solverPort);
        config.setNextAggregatorHost(nextAggHost);
        config.setNextAggregatorPort(nextAggPort);
        Aggregator agg = new Aggregator();
        agg.setServerConfig(config);
        agg.init();
    }

    public static void printUsageInfo() {
        System.out.println("Accepts 4 optional parameters: <Sensor Port> <Solver Port> <Next Aggregator Host> <Next Aggregator Port>");
    }

    public void setServerConfig(AggregatorConfiguration serverConfig) {
        this.configuration = serverConfig;
    }

    public void init() {
        SolverIoHandler solverIoHandler = new SolverIoHandler(this);
        SensorIoHandler sensorIoHandler = new ThreadedSensorIoHandler(this);
        NioSocketAcceptor sensorAcceptor = new NioSocketAcceptor();
        sensorAcceptor.getFilterChain().addLast("sensor codec", new ProtocolCodecFilter(new AggregatorSensorProtocolCodecFactory(true)));
        NioSocketAcceptor solverAcceptor = new NioSocketAcceptor();
        solverAcceptor.getFilterChain().addLast("solver codec", new ProtocolCodecFilter(new AggregatorSolverProtocolCodecFactory(true)));
        try {
            sensorAcceptor.setHandler(sensorIoHandler);
            sensorAcceptor.bind(new InetSocketAddress(this.configuration.getSensorListenPort()));
        } catch (IOException e) {
            log.error("Unable to bind to port {}.", this.configuration.getSensorListenPort());
            System.exit(1);
        }
        try {
            solverAcceptor.setHandler(solverIoHandler);
            solverAcceptor.bind(new InetSocketAddress(this.configuration.getSolverListenPort()));
        } catch (IOException ioe) {
            log.error("Unable to bind to port {}.", this.configuration.getSolverListenPort());
            System.exit(1);
        }
        log.info("GRAIL Aggregator is listening for sensors on on port {}.", this.configuration.getSensorListenPort());
        log.info("GRAIL Aggregator is listening for solvers on on port {}.", this.configuration.getSolverListenPort());
    }

    public void handshakeMessageReceived(IoSession session, HandshakeMessage handshakeMessage) {
        SensorInterface sensor = this.sensors.get(session);
        if (sensor == null) {
            log.error("Unable to retrieve sensor for {}", session);
            return;
        }
        log.debug("Received handshake message from sensor {}.", session);
        sensor.setReceivedHandshake(handshakeMessage);
        this.checkHandshakeMessages(sensor);
    }

    private boolean checkHandshakeMessages(SensorInterface sensor) {
        if (sensor.getSentHandshake() != null && sensor.getReceivedHandshake() != null) {
            if (!sensor.getSentHandshake().equals(sensor.getReceivedHandshake())) {
                StringBuffer sb = new StringBuffer();
                sb.append("Handshake mis-match, closing connection.\nLocal:\n\t").append(sensor.getSentHandshake()).append("\nRemote:\n\t").append(sensor.getReceivedHandshake());
                IoSession session = sensor.getSession();
                if (session == null) {
                    log.error("Sensor session was null for {}.", sensor);
                    return false;
                }
                this.sensors.remove(session);
                session.close(true);
                return false;
            }
        }
        return true;
    }

    public void sampleMessageReceived(final IoSession session, final SampleMessage sampleMessage) {
        this.handlerPool.execute(new Runnable() {

            @Override
            public void run() {
                Aggregator.this.handleSampleMessage(session, sampleMessage);
            }
        });
    }

    protected void handleSampleMessage(IoSession session, SampleMessage sampleMessage) {
        log.debug("Received {} from {}", sampleMessage, session.getRemoteAddress());
        org.grailrtls.solver.protocol.messages.SampleMessage solverSample = new org.grailrtls.solver.protocol.messages.SampleMessage();
        solverSample.setPhysicalLayer(sampleMessage.getPhysicalLayer());
        solverSample.setReceiverId(sampleMessage.getReceiverId());
        solverSample.setDeviceId(sampleMessage.getDeviceId());
        solverSample.setRssi(sampleMessage.getRssi());
        solverSample.setSensedData(sampleMessage.getSensorData());
        solverSample.setReceiverTimeStamp(sampleMessage.getReceivedTimestamp());
        this.sendSample(solverSample);
    }

    public void sensorConnected(IoSession session) {
        SensorInterface sensor = new SensorInterface();
        sensor.setSession(session);
        this.sensors.put(session, sensor);
        log.info("{} connected.", sensor);
        HandshakeMessage handshakeMsg = HandshakeMessage.getDefaultMessage();
        session.write(handshakeMsg);
    }

    public void sensorDisconnected(IoSession session) {
        SensorInterface sensor = this.sensors.get(session);
        if (sensor == null) {
            log.error("Unable to retrieve disconnecting sensor for {}.", session);
        }
        log.info("{} disconnected.", sensor);
        this.sensors.remove(session);
    }

    public void handshakeReceived(IoSession session, org.grailrtls.solver.protocol.messages.HandshakeMessage handshakeMessage) {
        log.info("Received {} from {}.", handshakeMessage, session);
    }

    public void connectionOpened(IoSession session) {
        CachingFilteringSolverInterface solver = new CachingFilteringSolverInterface();
        solver.setSession(session);
        this.solvers.put(session, solver);
        org.grailrtls.solver.protocol.messages.HandshakeMessage handshake = org.grailrtls.solver.protocol.messages.HandshakeMessage.getDefaultMessage();
        session.write(handshake);
        log.debug("Sent {} to {}.", handshake, solver);
    }

    public void connectionClosed(IoSession session) {
        this.solvers.remove(session);
    }

    public void subscriptionRequestReceived(IoSession session, SubscriptionMessage subscriptionRequestMessage) {
        CachingFilteringSolverInterface solver = this.solvers.get(session);
        subscriptionRequestMessage.setMessageType(SubscriptionMessage.RESPONSE_MESSAGE_ID);
        session.write(subscriptionRequestMessage);
        log.info("(Solver {}) Responded to subscription request with {}.", solver, subscriptionRequestMessage);
        solver.setSentSubscriptionResponse(true);
        if (subscriptionRequestMessage.getRules() != null) {
            for (SubscriptionRequestRule rule : subscriptionRequestMessage.getRules()) {
                solver.addEffectiveRule(rule);
                log.info("Added {} to {}.", rule, solver);
            }
        }
    }

    public void sendSample(org.grailrtls.solver.protocol.messages.SampleMessage solverSample) {
        for (CachingFilteringSolverInterface solver : this.solvers.values()) {
            solver.sendSample(solverSample);
        }
    }

    public void handshakeSent(IoSession session, org.grailrtls.solver.protocol.messages.HandshakeMessage handshakeMessage) {
    }

    public void sampleMessageSent(IoSession session, SampleMessage sampleMessage) {
    }

    public void subscriptionResponseSent(IoSession session, SubscriptionMessage subscriptionRequestMessage) {
    }

    public void sessionIdle(IoSession session, IdleStatus idleStatus) {
    }

    public void handshakeMessageSent(IoSession session, HandshakeMessage handshakeMessage) {
        SensorInterface sensor = this.sensors.get(session);
        if (sensor == null) {
            log.error("No sensor available for {}.", session);
            return;
        }
        log.info("Sent {} to {}.", handshakeMessage, sensor);
        this.checkHandshakeMessages(sensor);
    }

    public void sampleMessageSent(IoSession session, org.grailrtls.solver.protocol.messages.SampleMessage sampleMessage) {
    }

    public void sampleMessageReceived(IoSession session, org.grailrtls.solver.protocol.messages.SampleMessage sampleMessage) {
    }

    public void subscriptionRequestSent(IoSession session, SubscriptionMessage subsriptionMessage) {
    }

    public void subscriptionResponseReceived(IoSession session, SubscriptionMessage subscriptionMessage) {
    }

    public void exceptionCaught(IoSession session, Throwable exception) {
    }
}
