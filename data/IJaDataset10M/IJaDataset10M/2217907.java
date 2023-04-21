package ch.squix.nataware.punching.connector;

import java.util.Timer;
import java.util.TimerTask;
import org.apache.mina.statemachine.StateMachine;
import org.apache.mina.statemachine.StateMachineFactory;
import org.apache.mina.statemachine.StateMachineProxyBuilder;
import org.apache.mina.statemachine.annotation.State;
import org.apache.mina.statemachine.annotation.Transition;
import org.apache.mina.statemachine.annotation.Transitions;
import org.apache.mina.statemachine.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PunchingProtocolConnectorHandler {

    private Logger logger = LoggerFactory.getLogger(PunchingProtocolConnectorHandler.class);

    @State
    public static final String CLOSED = "CLOSED";

    @State(CLOSED)
    public static final String INIT_SENT = "INIT_SENT";

    @State(CLOSED)
    public static final String ACTIVE_PUNCHING_STARTED = "ACTIVE_PUNCHING_STARTED";

    @State(CLOSED)
    public static final String ROLESWITCH_SENT = "ROLE_SWITCH_SENT";

    @State(CLOSED)
    public static final String PASSIVE_PUNCHING_STARTED = "PASSIVE_PUNCHING_STARTED";

    @State(CLOSED)
    public static final String ESTABLISHED = "ESTABLISHED";

    private Timer timeoutTimer = new Timer();

    private IPunchingProtocolConnector connector;

    private StateMachine stateMachine;

    private boolean isClosed = false;

    private boolean isEstablished = false;

    public PunchingProtocolConnectorHandler() {
        stateMachine = StateMachineFactory.getInstance(Transition.class).create(PunchingProtocolConnectorHandler.CLOSED, this);
        connector = new StateMachineProxyBuilder().create(IPunchingProtocolConnector.class, stateMachine);
    }

    public IPunchingProtocolConnector getProtocolConnector() {
        return connector;
    }

    public StateMachine getStateMachine() {
        return stateMachine;
    }

    @Transition(on = "connect", in = CLOSED, next = INIT_SENT)
    public void sendInit() {
        logger.info("sending init...");
    }

    @Transition(on = "initAck", in = INIT_SENT, next = ACTIVE_PUNCHING_STARTED)
    public void startActivePunching(String punchingAddress) {
        logger.info("starting active punching...");
    }

    @Transition(on = "timeout", in = ACTIVE_PUNCHING_STARTED, next = ROLESWITCH_SENT)
    public void sendRoleSwitch() {
        logger.info("starting passive punching");
    }

    @Transition(on = "roleSwitchAck", in = ROLESWITCH_SENT, next = PASSIVE_PUNCHING_STARTED)
    public void startPassivePunching() {
        logger.info("starting active punching");
    }

    @Transitions({ @Transition(on = "establish", in = PASSIVE_PUNCHING_STARTED, next = ESTABLISHED), @Transition(on = "establish", in = ACTIVE_PUNCHING_STARTED, next = ESTABLISHED) })
    public void established() {
        logger.info("connection successfully established");
        isEstablished = true;
    }

    @Transition(on = "timeout", in = PASSIVE_PUNCHING_STARTED, next = CLOSED)
    public void stop() {
        logger.info("all activities stopped");
        isClosed = true;
    }

    /**
	 * Starts the timeout timer, which will fire a
	 * timeout event, in case it didn't get reset before
	 * @param delay
	 */
    private void startTimeoutTimer(long delay) {
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                logger.info("Timeout occured");
                connector.timeout();
            }
        };
        timeoutTimer.schedule(task, delay);
    }

    @Transition(on = "*", in = CLOSED, weight = 100)
    public void error(Event event) {
        System.out.println("Cannot '" + event.getId() + "' at this time");
    }

    public boolean isClosed() {
        return isClosed;
    }

    public boolean isEstablished() {
        return isEstablished;
    }
}
