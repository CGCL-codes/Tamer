package org.tigr.Facade.Bean.baseCallVersion;

public class BaseCallVersionSerializerFactory {

    private BaseCallVersionSerializer serializerInstance;

    private static BaseCallVersionSerializerFactory instance;

    private BaseCallVersionSerializerFactory() {
        serializerInstance = TDBBaseCallVersionSerializer.getInstance();
    }

    public static BaseCallVersionSerializerFactory getInstance() {
        if (instance == null) {
            instance = new BaseCallVersionSerializerFactory();
        }
        return instance;
    }

    /**
     * @return Returns the serializerInstance.
     */
    public BaseCallVersionSerializer getSerializerInstance() {
        return serializerInstance;
    }

    /**
     * @param serializerInstance The serializerInstance to set.
     */
    public void setSerializerInstance(BaseCallVersionSerializer serializerInstance) {
        this.serializerInstance = serializerInstance;
    }
}
