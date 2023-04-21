package org.loon.framework.javase.game.core.store;

public interface RecordFilter {

    boolean matches(byte[] candidate);
}
