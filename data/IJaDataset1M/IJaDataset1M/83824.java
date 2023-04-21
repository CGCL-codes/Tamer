package net.adrianromero.data.loader;

import net.adrianromero.basic.BasicException;

public class SerializerWriteInteger implements SerializerWrite {

    public static final SerializerWrite INSTANCE = new SerializerWriteInteger();

    /** Creates a new instance of SerializerWriteInteger */
    private SerializerWriteInteger() {
    }

    public void writeValues(DataWrite dp, Object obj) throws BasicException {
        Datas.INT.setValue(dp, 1, obj);
    }
}
