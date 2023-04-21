package com.hazelcast.nio;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

public interface DataSerializable extends Serializable {

    void writeData(DataOutput out) throws IOException;

    void readData(DataInput in) throws IOException;
}
