package jmxm.formater;

import javax.management.openmbean.CompositeData;

public class DataSetCommittedMemoryToString extends LongMemoryToString {

    public Object format(Object object) throws Exception {
        CompositeData data = (CompositeData) object;
        return super.format(data.get("committed"));
    }

    public static Object newInstance() {
        return new DataSetUsedMemoryToString();
    }
}
