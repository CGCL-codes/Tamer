package com.cross.hibernate;

import com.cross.core.AbstractReader;

public class PojoReader extends AbstractReader {

    @Override
    protected boolean nextRow() {
        return false;
    }

    @Override
    public void close() {
    }
}
