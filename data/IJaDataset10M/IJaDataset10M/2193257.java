package com.wavechain.interrogatorIO;

import org.singularityoss.devicemgr.DeviceManager;
import com.wavechain.system.ReaderComponent;

public interface IInterrogatorIO extends Runnable {

    public void setReaderComponent(ReaderComponent readerComponent);

    public ReaderComponent getReaderComponent();

    public void setDeviceManager(DeviceManager deviceManager);

    public void on();

    public void off() throws InterruptedException;
}
