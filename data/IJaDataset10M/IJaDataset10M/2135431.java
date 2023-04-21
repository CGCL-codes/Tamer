package com.google.code.spotshout.lang;

import java.io.IOException;
import ksn.io.KSNSerializableInterface;
import ksn.io.ObjectInputStream;
import ksn.io.ObjectOutputStream;

/**
 * This class represent a Char value allowing to be serializable.
 */
public class SerialChar implements KSNSerializableInterface {

    char value;

    public SerialChar() {
        value = '0';
    }

    public SerialChar(char v) {
        value = v;
    }

    public char getValue() {
        return value;
    }

    public void writeObjectOnSensor(ObjectOutputStream stream) throws IOException {
        stream.writeChar(value);
    }

    public void readObjectOnSensor(ObjectInputStream stream) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        value = stream.readChar();
    }
}
