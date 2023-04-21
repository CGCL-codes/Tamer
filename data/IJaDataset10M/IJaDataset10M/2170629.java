package org.demo.ioc.service;

import org.demo.ioc.facade.CPU;

public class AMDCPU implements CPU {

    public void execInstruction() {
        System.out.println("Executing AMD CPU inscructions.");
    }

    public int getSpeed() {
        return 2400;
    }

    public void outResult() {
        System.out.println("AMD CPU results");
    }

    public String getManufacture() {
        return "AMD Inc";
    }

    public String getName() {
        return "AMD 2400+ CPU";
    }

    public double getPrice() {
        return 2000.00;
    }
}
