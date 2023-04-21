package org.knott.kadavr.metadata;

import java.io.IOException;

/**
 * Класс описывающий элемент типа float.
 * @author Sergey
 */
public class FloatItem extends ConstValueItem {

    /**
     * Эта константа описывает тэг объектов данного типа.
     * Эта константа имеет значение {@value}.
     */
    public static final int TAG = ConstPool.TAG_FLOAT;

    float value;

    /**
     * {@inheritDoc}
     */
    @Override
    void link(ConstPool pool) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTag() {
        return TAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void read(ClassFileReader dis) throws IOException {
        value = dis.readFloat();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValueString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
