package com.google.code.jtracert.instrument.impl;

import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.instrument.ByteCodeTransformException;
import com.google.code.jtracert.instrument.JTracertByteCodeTransformer;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public abstract class BaseJTracertByteCodeTransformer implements JTracertByteCodeTransformer {

    private InstrumentationProperties instrumentationProperties;

    /**
     *
     */
    protected BaseJTracertByteCodeTransformer() {
        super();
    }

    /**
     * @param instrumentationProperties
     */
    public BaseJTracertByteCodeTransformer(InstrumentationProperties instrumentationProperties) {
        this();
        setInstrumentationProperties(instrumentationProperties);
    }

    /**
     * @return
     */
    public InstrumentationProperties getInstrumentationProperties() {
        return instrumentationProperties;
    }

    /**
     * @param instrumentationProperties
     */
    public void setInstrumentationProperties(InstrumentationProperties instrumentationProperties) {
        this.instrumentationProperties = instrumentationProperties;
    }

    /**
     * @param originalBytes
     * @param offset
     * @param length
     * @return
     * @throws ByteCodeTransformException
     */
    public abstract byte[] transform(byte[] originalBytes, int offset, int length, boolean instrumentClass) throws ByteCodeTransformException;
}
