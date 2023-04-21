package openfarmmanager.beans.memory;

import openfarmmanager.beans.IBean;

public class InterpreterMemoryMonitorBean extends MonitorBeans implements IBean {

    private static final long serialVersionUID = 57803880840457603L;

    private int registeredInterpreters;

    public int getRegisteredInterpreters() {
        return registeredInterpreters;
    }

    public void setRegisteredInterpreters(int registeredInterpreters) {
        this.registeredInterpreters = registeredInterpreters;
    }
}
