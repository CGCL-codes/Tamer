package com.xucia.jsponic.datasource;

import java.util.List;
import com.xucia.jsponic.data.GlobalData;

/**
 * This class provides an implementation of NewObjectPersister that will allow a data source to start with an 
 * empty new object, and then add properties to it to initialize it. The getSource and getObjectId will be called prior (and afterwards) to
 * the initialize and record functions are called
 * @author Kris Zyp
 */
public abstract class StartAsEmptyPersister implements NewObjectPersister {

    boolean recordedFunction = false;

    public void initializeAsFunction(String functionBody) throws Exception {
        recordProperty(GlobalData.FUNCTION_METHOD_FIELD, functionBody);
        recordedFunction = true;
    }

    public void finished() throws Exception {
    }

    public void initializeAsList(List values) throws Exception {
        WritableDataSource source = getSource();
        String objectId = getObjectId();
        for (Object value : values) source.recordListAdd(objectId, value);
    }

    public void recordProperty(String name, Object value) throws Exception {
        WritableDataSource source = getSource();
        String objectId = getObjectId();
        if (!recordedFunction || !GlobalData.FUNCTION_METHOD_FIELD.equals(name)) source.recordPropertyAddition(objectId, name, value);
    }
}
