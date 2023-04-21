package com.clanwts.nbdf.boot;

import java.io.Serializable;

public interface Peer extends Serializable {

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();
}
