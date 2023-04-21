package net.sourceforge.transumanza.writer.common;

import java.util.Map;
import net.sourceforge.transumanza.data.DataObject;

public class DataObjectToMapConverter implements ObjectToMapConverter {

    public Map convert(Object obj) {
        return ((DataObject) obj).getDataObjectAsMap();
    }
}
