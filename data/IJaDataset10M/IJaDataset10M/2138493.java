package mipt.data;

import java.util.List;

/**
 * Superclass for DataFactory singleton. Has implementations for all methods except one.
 */
public abstract class AbstractDataFactory implements DataFactory {

    protected static DataFactory instance;

    /**
 * 
 */
    public AbstractDataFactory() {
        instance = this;
    }

    /**
 * 
 */
    public Data createData(String dataType) {
        return createData(dataType, null);
    }

    /**
 * Returns field names of creating Datas  
 */
    public abstract List getFieldNamesList(String dataType);

    /**
 * 
 * @return mipt.data.DataFactory
 */
    public static DataFactory getInstance() {
        return instance;
    }
}
