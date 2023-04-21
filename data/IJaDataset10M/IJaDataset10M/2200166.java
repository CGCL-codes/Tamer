package com.google.code.spotshout.lang;

import java.io.IOException;
import ksn.io.KSNSerializableInterface;
import ksn.io.ObjectInputStream;
import ksn.io.ObjectOutputStream;

/**
 * This class represent a Long value allowing to be serializable.
 */
public class SerialLong implements KSNSerializableInterface {

    long value;

    public SerialLong() {
        value = 0;
    }

    public SerialLong(long v) {
        value = v;
    }

    public long getValue() {
        return value;
    }

    public void writeObjectOnSensor(ObjectOutputStream stream) throws IOException {
        stream.writeLong(value);
    }

    public void readObjectOnSensor(ObjectInputStream stream) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        value = stream.readLong();
    }
}
