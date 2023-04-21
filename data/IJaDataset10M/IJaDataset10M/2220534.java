package model.ipmonitor;

import model.AbstractModelListener;

public interface IPMonitorExceptionListener extends AbstractModelListener {

    public void ipMonitorIPNotFound();

    public void ipMonitorIO();
}
