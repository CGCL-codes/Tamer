package br.com.jmemory.bean;

import java.io.Serializable;

public class MemoryBean implements Serializable {

    private static final long serialVersionUID = -4616941854052540831L;

    protected long objectIdentification;

    public long objectIdentification() {
        return objectIdentification;
    }

    protected boolean sinchronized;

    public boolean isSincronized() {
        return sinchronized;
    }
}
