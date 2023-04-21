package com.sun.j2ee.blueprints.waf.controller.ejb;

public abstract class EJBCommandSupport implements java.io.Serializable, EJBCommand {

    protected StateMachine machine = null;

    public void init(StateMachine machine) {
        this.machine = machine;
    }

    /**
   * Empty method which can be overriden by subclasses
   */
    public void doStart() {
    }

    /**
   * Empty method which can be overriden by subclasses
   */
    public void doEnd() {
    }
}
