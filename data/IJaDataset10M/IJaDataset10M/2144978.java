package com.akivasoftware.comp.model;

import com.akivasoftware.comp.session.*;
import java.util.Iterator;
import java.util.Vector;
import java.util.EventListener;
import java.util.EventObject;

/**
 *  ParameterFittingHandlerComponent - Encapsulates the logic behind parameter fitting of a dynamic model.
 *  @author J.Varner, Oct 16,2001
 */
public class ParameterFittingHandlerComposite extends InitializationComposite {

    private Vector m_vecRegister;

    /** Constructor - no arg */
    public ParameterFittingHandlerComposite() {
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
     * register() - Registration method for notification of state updates
     */
    public void register(EventListener obj) {
        m_vecRegister.addElement(obj);
    }

    /**
     * fireParameterChangedEvent() - Logic for notifying listeners that parameters have changed
     */
    void fireStateChangedEvent() {
        try {
            ParameterChangedEvent evt = new ParameterChangedEvent(this);
            int loop = m_vecRegister.size();
            for (int q = 0; q < loop; q++) {
                ((ParameterChangedListener) m_vecRegister.elementAt(q)).updateParameters(evt);
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
        m_vecRegister = new Vector();
    }

    /**
    * A <code>PartTreeCreator</code> calls this method after constructing this
    * <code>Part</code> and setting its initial attributes.
    *
    * @return this <code>Part</code>
    */
    public InitializationComponent endSetup() throws Exception {
        IOrb m_orb = ADFOrb.getInstance();
        m_orb.register("_PARAMETER_FITTING", this);
        return (this);
    }
}
