package application.agents.agents2d;

import java.awt.Color;
import java.io.Serializable;
import sim.engine.SimState;
import sim.util.Proxiable;
import application.agents.AbstractAgent;
import application.agents.IComponentManipulator;
import domain.structure_new.components.plant.RametNetwork;

/**
 *
 * @author Uwe Grueters,	email: uwe.grueters@bot2.bio.uni-giessen.de
 * @author Roland Dahlem,	email: roland.dahlem@mni.fh-giessen.de
 */
public class RametNetworkAgent extends AbstractAgent implements Proxiable, IComponentManipulator {

    /**
	 * 
	 */
    private static final long serialVersionUID = 78433097982030626L;

    /**
	 * Constructor
	 * @param rametNetwork
	 */
    public RametNetworkAgent(RametNetwork rametNetwork) {
        super(rametNetwork);
    }

    @Override
    public void step(SimState state) {
        super.step(state);
    }

    @Override
    public Color getColor() {
        return new Color(0.0f, 0.0f, 1.0f);
    }

    public RametNetwork getRametNetwork() {
        return (RametNetwork) this.getMyComponent();
    }

    @Override
    protected double getScaleX() {
        return getRametNetwork().getXDimension();
    }

    @Override
    protected double getScaleY() {
        return getRametNetwork().getYDimension();
    }

    @Override
    public Object propertiesProxy() {
        return new MyProxy();
    }

    /**
	 * This inner class is part of the Proxiable interface
	 * and is responsible for inspection of certain ClonalPlant properties.
	 *
	 * @author Uwe Grueters,	email: uwe.grueters@bot2.bio.uni-giessen.de
	 */
    public class MyProxy extends AbstractAgent.MySuperProxy implements Serializable {

        private static final long serialVersionUID = -3497803478857798062L;

        public int getCOUNT_RAMETNETWORKS() {
            return RametNetwork.getCOUNT_RAMETNETWORKS();
        }

        public int getMAX_ID() {
            return RametNetwork.getMAX_ID();
        }

        public int getMaxRametID() {
            return getRametNetwork().getMaxRametID();
        }

        public int getCountRamets() {
            return getRametNetwork().getCountRamets();
        }

        @Override
        public double getAge_d() {
            return getRametNetwork().getAge_d();
        }

        public int getMyID() {
            return getRametNetwork().getMyID();
        }

        public String getName() {
            return getRametNetwork().getDynamicName().toString();
        }

        public String getCGOElementSerchTree() {
            return getRametNetwork().getCgoElementSearchTree().toString();
        }
    }
}
