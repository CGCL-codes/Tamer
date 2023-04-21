package org.susan.java.design.factory.abs;

public class N0Schema2 implements N0AbstractFactory {

    public OCPUApi createCPUApi() {
        return new OAMDCPU(939);
    }

    public OMainboardApi createMainboardApi() {
        return new OMSIMainboard(939);
    }
}
