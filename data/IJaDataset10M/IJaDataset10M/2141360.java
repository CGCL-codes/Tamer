package com.akivasoftware.comp.model;

import java.util.Vector;
import java.util.Iterator;
import com.akivasoftware.comp.session.*;

/**
 *  Startup - Composite class encapsulating the initialization logic
 *  @author  J.Varner, Sept 18th, 2001
 */
public class Startup extends InitializationComposite {

    /**
     * Construct new Startup object
     */
    public Startup() {
        this.init();
    }

    /**
     * Abstract initialization method implemented by all subclasses
     */
    public void doOperation() {
        Iterator iter = null;
        try {
            iter = m_vecChildren.iterator();
            while (iter.hasNext()) {
                ((InitializationComponent) iter.next()).doOperation();
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    /**
     * Initialization method
     */
    public void init() {
        m_vecChildren = new Vector();
    }

    /**
    * A <code>PartTreeCreator</code> calls this method after constructing this
    * <code>Part</code> and setting its initial attributes.
    *
    * @return this <code>Part</code>
    */
    public InitializationComponent endSetup() throws Exception {
        IOrb m_orb = ADFOrb.getInstance();
        m_orb.register("_LAUNCHER", this);
        return (this);
    }
}
