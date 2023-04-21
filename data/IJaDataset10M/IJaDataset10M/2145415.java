package com.duniptech.engine.core.simulation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import com.duniptech.engine.core.modeling.Message;
import com.duniptech.engine.core.modeling.api.IAtomic;
import com.duniptech.engine.core.modeling.api.ICoupled;
import com.duniptech.engine.core.modeling.api.ICoupling;
import com.duniptech.engine.core.modeling.api.IDevs;
import com.duniptech.engine.core.modeling.api.IMessage;
import com.duniptech.engine.core.simulation.api.ICoordinator;
import com.duniptech.engine.core.simulation.api.ISimulator;

public class Coordinator extends Simulator implements ICoordinator {

    private boolean debug = Boolean.FALSE;

    /** Simulators stored in the coupled model. */
    protected HashMap<IAtomic, ISimulator> children;

    public Coordinator(ICoupled digraph) {
        this(digraph, false);
    }

    public Coordinator(ICoupled digraph, boolean debug) {
        super(digraph);
        digraph.setCoordinator(this);
        if (debug) {
            this.debug = debug;
            System.out.println("Creating coordinator for: " + digraph.getName());
        }
        children = new HashMap<IAtomic, ISimulator>();
        Collection<IAtomic> subModels = digraph.getComponents();
        for (IAtomic subModel : subModels) {
            if (subModel instanceof ICoupled) {
                ISimulator child = new Coordinator((ICoupled) subModel, debug);
                child.setParent(this);
                children.put(subModel, child);
            } else if (subModel instanceof IAtomic) {
                ISimulator child = new Simulator(subModel, debug);
                child.setParent(this);
                children.put(subModel, child);
            }
        }
        initialize(0);
    }

    public void initialize(double t) {
        Iterator<ISimulator> itr = children.values().iterator();
        while (itr.hasNext()) {
            ISimulator child = itr.next();
            child.initialize(t);
        }
        tL = t;
        tN = t + ta(t);
    }

    public double ta(double t) {
        double tn = IDevs.INFINITY;
        Iterator<ISimulator> itr = children.values().iterator();
        while (itr.hasNext()) {
            ISimulator child = itr.next();
            if (child.getTN() < tn) {
                tn = child.getTN();
            }
        }
        return tn - t;
    }

    public void deltfcn(double t) {
        propagateInput();
        Iterator<ISimulator> itr = children.values().iterator();
        while (itr.hasNext()) {
            ISimulator child = itr.next();
            child.deltfcn(t);
            if (debug) {
                child.getModel().logState();
            }
        }
        tL = t;
        tN = tL + ta(t);
        input = new Message();
    }

    public void lambda(double t) {
        if (t != tN) output = new Message();
        Iterator<ISimulator> itr = children.values().iterator();
        while (itr.hasNext()) {
            ISimulator child = itr.next();
            child.lambda(t);
        }
        propagateOutput();
    }

    public void propagateInput() {
        ICoupled digraph = (ICoupled) model;
        Iterator<ICoupling> itrEIC = null;
        itrEIC = digraph.getEICs().iterator();
        while (itrEIC.hasNext()) {
            ICoupling c = itrEIC.next();
            String portFrom = c.getNamePortFrom();
            String portTo = c.getNamePortTo();
            IAtomic componentTo = c.getComponentTo();
            IMessage in = children.get(componentTo).getInput();
            IMessage out = input;
            if (!out.getValuesOnPort(portFrom).isEmpty()) in.receive(portFrom, out, portTo);
        }
    }

    public void propagateOutput() {
        ICoupled digraph = (ICoupled) model;
        Iterator<ICoupling> itrIC = null;
        itrIC = digraph.getICs().iterator();
        while (itrIC.hasNext()) {
            ICoupling c = itrIC.next();
            String portFrom = c.getNamePortFrom();
            String portTo = c.getNamePortTo();
            IAtomic componentFrom = c.getComponentFrom();
            IAtomic componentTo = c.getComponentTo();
            IMessage in = children.get(componentTo).getInput();
            IMessage out = children.get(componentFrom).getOutput();
            if (!out.getValuesOnPort(portFrom).isEmpty()) {
                in.receive(portFrom, out, portTo);
            }
        }
        Iterator<ICoupling> itrEOC = null;
        itrEOC = digraph.getEOCs().iterator();
        while (itrEOC.hasNext()) {
            ICoupling c = itrEOC.next();
            String portFrom = c.getNamePortFrom();
            String portTo = c.getNamePortTo();
            IAtomic componentFrom = c.getComponentFrom();
            IMessage in = output;
            IMessage out = children.get(componentFrom).getOutput();
            if (!out.getValuesOnPort(portFrom).isEmpty()) in.receive(portFrom, out, portTo);
        }
    }

    public void injectInput(double e, IMessage msg) {
        double t = tL + e;
        input = msg;
        deltfcn(t);
    }

    public void simulate(long numIterations) {
        double t = tN;
        long counter;
        for (counter = 1; counter < numIterations && t < IDevs.INFINITY; counter++) {
            lambda(t);
            deltfcn(t);
            t = tN;
        }
        System.out.println(counter + " iterations.");
    }

    public void simulate(double interval) {
        double t = tN;
        double tF = t + interval;
        long counter = 0;
        while (t < IDevs.INFINITY && t < tF) {
            lambda(t);
            deltfcn(t);
            t = tN;
            counter++;
        }
        System.out.println(counter + " iterations.");
    }
}
