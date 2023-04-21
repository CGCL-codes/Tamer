package org.ocera.orte.types;

public class SendStatus {

    public static final int NEED_DATA = 0x01;

    public static final int CQL = 0x02;

    private int value;

    public SendStatus(int a) {
        value = a;
    }

    public boolean needData() {
        if (this.value == NEED_DATA) return true;
        return false;
    }

    public boolean critQueueLevel() {
        if (this.value == CQL) return true;
        return false;
    }
}
