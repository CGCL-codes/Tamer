package br.net.woodstock.rockframework.text.impl;

import java.text.FieldPosition;

public final class DontCareFieldPosition extends FieldPosition {

    private static DontCareFieldPosition instance = new DontCareFieldPosition();

    private DontCareFieldPosition() {
        super(0);
    }

    public static FieldPosition getInstance() {
        return DontCareFieldPosition.instance;
    }
}
