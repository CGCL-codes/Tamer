package org.hswgt.teachingbox.core.rl.env;

import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.hswgt.teachingbox.core.rl.datastructures.ActionSet;
import org.hswgt.teachingbox.core.rl.thrift.ThriftEnvironment;
import org.hswgt.teachingbox.core.rl.tools.ThriftUtils;
import cern.jet.random.Uniform;

public class ThriftMountainCarEnv implements ThriftEnvironment.Iface {

    private static final long serialVersionUID = -8626501922991232562L;

    static Logger log4j = Logger.getLogger("MountainCarEnv");

    /**
	 * The minimum horizontal position.
	 */
    public static final double MIN_POS = -1.2;

    /**
	 * The maximum horizontal position.
	 */
    public static final double MAX_POS = 0.5;

    /**
	 * The minimum velocity.
	 */
    public static final double MIN_VEL = -0.07;

    /**
	 * The maximum velocity.
	 */
    public static final double MAX_VEL = 0.07;

    /**
	 * The goal position.
	 */
    public static final double GOAL_POS = 0.5;

    /**
	 * coast
	 */
    public static final Action COAST = new Action(new double[] { 0 });

    /**
	 * full throttle forward 
	 */
    public static final Action FORWARD = new Action(new double[] { +1 });

    /**
	 * full throttle backward 
	 */
    public static final Action BACKWARD = new Action(new double[] { -1 });

    /**
	 * The set of all possible actions
	 */
    public static final ActionSet ACTION_SET = new ActionSet();

    /**
	 * The number of actions.
	 */
    public static final int ACTIONS;

    /**
	 * initialize the ActionSet
	 */
    static {
        ACTION_SET.add(BACKWARD);
        ACTION_SET.add(COAST);
        ACTION_SET.add(FORWARD);
        ACTIONS = ACTION_SET.size();
    }

    /**
	 * the internal state.
	 * This is a two-dimensional State and represents the actual state of the MountainCar.
	 */
    protected State s = new State(2);

    /**
	 * Start a new server.
	 * @param port 
	 * @throws TTransportException
	 */
    public ThriftMountainCarEnv(int port) throws TTransportException {
        TServerSocket serverTransport = new TServerSocket(port);
        ThriftEnvironment.Processor processor = new ThriftEnvironment.Processor(this);
        Factory protFactory = new TBinaryProtocol.Factory(true, true);
        TServer server = new TThreadPoolServer(processor, serverTransport, protFactory);
        System.out.println("Starting server on port " + port + " ...");
        server.serve();
    }

    public double doAction(final List<Double> a) throws TException {
        double p = s.get(0);
        double v = s.get(1);
        double u = a.get(0);
        u = Math.min(u, +1);
        u = Math.max(u, -1);
        v = v + 0.001 * u - 0.0025 * Math.cos(3 * p);
        p = p + v;
        if (p < MIN_POS) {
            p = MIN_POS;
            v = 0;
        }
        if (v < MIN_VEL) {
            v = MIN_VEL;
        }
        if (v > MAX_VEL) {
            v = MAX_VEL;
        }
        s.set(0, p);
        s.set(1, v);
        if (s.get(0) >= GOAL_POS) return 0;
        return -1;
    }

    public List<Double> getState() throws TException {
        return ThriftUtils.Convert.StateToList(s);
    }

    public void init(final List<Double> s) throws TException {
        this.s = ThriftUtils.Convert.ListToState(s);
    }

    public void initRandom() throws TException {
        s.set(0, Uniform.staticNextDoubleFromTo(MIN_POS, MAX_POS));
        s.set(1, Uniform.staticNextDoubleFromTo(MIN_VEL, MAX_VEL));
    }

    public boolean isTerminalState() throws TException {
        if (s.get(0) >= GOAL_POS) {
            log4j.debug("TerminalState reached");
            return true;
        }
        return false;
    }

    public static void main(String args[]) throws IOException, TTransportException {
        new ThriftMountainCarEnv(7911);
    }
}
