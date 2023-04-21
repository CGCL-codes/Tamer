package org.grailrtls.sim;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.mina.proxy.utils.ByteUtilities;
import org.grailrtls.sensor.SensorAggregatorInterface;
import org.grailrtls.sensor.listeners.ConnectionListener;
import org.grailrtls.solver.SolverAggregatorInterface;
import org.grailrtls.solver.listeners.SampleListener;
import org.grailrtls.solver.protocol.messages.SampleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysoutSolver implements ConnectionListener, org.grailrtls.solver.listeners.ConnectionListener, SampleListener {

    private static final Logger log = LoggerFactory.getLogger(SysoutSolver.class);

    protected final ConcurrentLinkedQueue<SolverAggregatorInterface> inputAggregators = new ConcurrentLinkedQueue<SolverAggregatorInterface>();

    protected final ConcurrentLinkedQueue<SensorAggregatorInterface> outputAggregators = new ConcurrentLinkedQueue<SensorAggregatorInterface>();

    protected final ExecutorService workers = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public void connectionEnded(SensorAggregatorInterface aggregator) {
        aggregator.removeConnectionListener(this);
        this.outputAggregators.remove(aggregator);
        log.info("Connection to {} ended. No longer forwarding samples.", aggregator);
    }

    @Override
    public void connectionEstablished(SensorAggregatorInterface aggregator) {
        log.info("Connecting to {} to send samples.", aggregator);
    }

    @Override
    public void connectionInterrupted(SensorAggregatorInterface aggregator) {
        this.outputAggregators.remove(aggregator);
        log.warn("Connection to {} was interrupted. Samples will not be sent until the connection resumes.");
    }

    @Override
    public void readyForSamples(SensorAggregatorInterface aggregator) {
        this.outputAggregators.add(aggregator);
        log.info("Ready to send samples to {}", aggregator);
    }

    @Override
    public void connectionEnded(SolverAggregatorInterface aggregator) {
        aggregator.removeConnectionListener(this);
        aggregator.removeSampleListener(this);
        this.inputAggregators.remove(aggregator);
        log.info("Connection to {} ended.  No longer receiving samples.", aggregator);
    }

    @Override
    public void connectionEstablished(SolverAggregatorInterface aggregator) {
        this.inputAggregators.add(aggregator);
        log.info("Connecting to {} to receive samples.", aggregator);
    }

    @Override
    public void connectionInterrupted(SolverAggregatorInterface aggregator) {
        log.info("Connection to {} was interrupted.  Samples will not be sent until the connection resumes.", aggregator);
    }

    @Override
    public void sampleReceived(final SolverAggregatorInterface aggregator, final SampleMessage sample) {
        this.workers.execute(new Runnable() {

            @Override
            public void run() {
                SysoutSolver.this.processSample(aggregator, sample);
            }
        });
    }

    protected void processSample(final SolverAggregatorInterface aggregator, final SampleMessage sample) {
        System.out.println();
        System.out.println("Dev ID: " + ByteUtilities.asHex(sample.getDeviceId()).substring(20, 32));
        System.out.println("Recv ID: " + ByteUtilities.asHex(sample.getReceiverId()).substring(20, 32));
        System.out.println("PHY Type: " + sample.getPhysicalLayer());
        System.out.println("Recv TS: " + sample.getReceiverTimeStamp());
        System.out.println("RSSI: " + sample.getRssi());
        System.out.println("Sensed Data asHex?: " + ByteUtilities.asHex(sample.getSensedData()));
    }

    public void addOutput(final String hostname, final Integer port) {
        SensorAggregatorInterface newAgg = new SensorAggregatorInterface();
        newAgg.setHost(hostname);
        newAgg.setPort(port.intValue());
        newAgg.addConnectionListener(this);
        newAgg.setStayConnected(true);
        if (newAgg.doConnectionSetup()) {
            log.info("Connection for outgoing samples succeeded to {}", newAgg);
        } else {
            log.error("Failed connection for outgoing samples to {}", newAgg);
        }
    }

    public void addInput(final String hostname, final Integer port) {
        SolverAggregatorInterface newAgg = new SolverAggregatorInterface();
        newAgg.setHost(hostname);
        newAgg.setPort(port.intValue());
        newAgg.addConnectionListener(this);
        newAgg.addSampleListener(this);
        newAgg.setStayConnected(true);
        if (newAgg.doConnectionSetup()) {
            log.info("Connection for incoming samples succeeded to {}", newAgg);
        } else {
            log.warn("Connection for incoming samples failed to {}", newAgg);
        }
    }

    public void removeInput(final SolverAggregatorInterface aggregator) {
        this.inputAggregators.remove(aggregator);
        aggregator.doConnectionTearDown();
    }

    public void removeOutput(final SensorAggregatorInterface aggregator) {
        this.outputAggregators.remove(aggregator);
        aggregator.doConnectionTearDown();
    }

    public static void main(String[] args) {
        List<String> inputHosts = new ArrayList<String>();
        List<Integer> inputPorts = new ArrayList<Integer>();
        List<String> outputHosts = new ArrayList<String>();
        List<Integer> outputPorts = new ArrayList<Integer>();
        for (int i = 0; i < args.length; ++i) {
            if ("-i".equals(args[i]) || "--input".equals(args[i])) {
                String host = args[++i];
                Integer port = Integer.valueOf(args[++i]);
                inputHosts.add(host);
                inputPorts.add(port);
            } else {
                log.warn("Unknown option {}. Skipping...", args[i]);
            }
        }
        SysoutSolver solver = new SysoutSolver();
        for (int i = 0; i < inputHosts.size(); ++i) {
            solver.addInput(inputHosts.get(i), inputPorts.get(i));
        }
    }
}
