package com.google.code.spotshout.lang;

import java.io.IOException;
import ksn.io.KSNSerializableInterface;
import ksn.io.ObjectInputStream;
import ksn.io.ObjectOutputStream;

/**
 * This class represent a Float value allowing to be serializable.
 */
public class SerialFloat implements KSNSerializableInterface {

    float value;

    public SerialFloat() {
        value = 0;
    }

    public SerialFloat(float v) {
        value = v;
    }

    public float getValue() {
        return value;
    }

    public void writeObjectOnSensor(ObjectOutputStream stream) throws IOException {
        stream.writeFloat(value);
    }

    public void readObjectOnSensor(ObjectInputStream stream) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        value = stream.readFloat();
    }
}
