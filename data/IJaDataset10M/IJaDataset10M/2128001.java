package org.susan.java.design.factory.abs;

public class N1Schema2 implements N1AbstractFactory {

    public Object createProduct(int type) {
        Object retObj = null;
        if (type == 1) {
            retObj = new OAMDCPU(939);
        } else if (type == 2) {
            retObj = new OMSIMainboard(939);
        }
        return retObj;
    }
}
