package org.zoolib;

public abstract class ZStreamIOFactory {

    public ZStreamIOFactory() {
    }

    public abstract ZStreamIO makeStreamIO();
}
