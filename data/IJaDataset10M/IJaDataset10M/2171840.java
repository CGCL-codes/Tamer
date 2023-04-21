package com.hydra.eu.device.xbee.service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import com.rapplogic.xbee.api.XBeeResponse;

public abstract class Service {

    private String name;

    public CyclicBarrier barrier = new CyclicBarrier(2);

    private String serviceData;

    private List<CommandAndValue> commandAndValue = new LinkedList<CommandAndValue>();

    private String superClass;

    private int numOfOut = 0;

    public String getServiceName() {
        return this.name;
    }

    public void setServiceName(String name) {
        this.name = name;
    }

    public List<CommandAndValue> getCommandAndValue() {
        return this.commandAndValue;
    }

    public void setCommandAndValue(LinkedList<CommandAndValue> list) {
        this.commandAndValue = list;
    }

    public void setCommandAndValue(String[] commands, int[][] values) {
        if (values == null) {
            for (String cmd : commands) {
                this.commandAndValue.add(new CommandAndValue(cmd, null));
            }
        } else if (commands.length != values.length) return; else {
            for (int i = 0; i < commands.length; i++) this.commandAndValue.add(new CommandAndValue(commands[i], values[i]));
        }
    }

    public abstract int getID();

    public void startWait() {
        try {
            this.barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public void barrierReset() {
        this.barrier.reset();
    }

    public String getServiceData() {
        return this.serviceData;
    }

    public void setServiceData(String data) {
        this.serviceData = data;
        this.startWait();
    }

    public abstract void processData(XBeeResponse response);

    public String getSuperClass() {
        return this.superClass;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }

    public void setNumOfOut(int num) {
        this.numOfOut = num;
    }

    public int getNumOfOut() {
        return this.numOfOut;
    }
}
