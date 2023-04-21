package fr.esrf.tangoatk.core;

public interface IDeviceApplication extends Runnable {

    public void setModel(IDevice model);

    public IDevice getModel();
}
