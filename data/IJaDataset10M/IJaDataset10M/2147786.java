package org.project.trunks.factory;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class DataObjectFactory extends DefaultObjectFactory {

    protected static DataObjectFactory _factory = null;

    public DataObjectFactory() {
        super(new String("data_object"));
    }

    public DataObjectFactory(String propertiesFileName) {
        super(propertiesFileName);
    }

    public static DefaultObjectFactory getInstance() {
        if (_factory == null) _factory = new DataObjectFactory();
        return _factory;
    }

    public Object getResource(String name) throws Exception {
        if (name == null) throw new Exception("DataObjectFactory.getResource - Name is required");
        try {
            String className = propertiesFile.getString(name.toUpperCase());
            if (className == null) throw new Exception(className + " not found");
            Object[] tObjects = new Object[] {};
            Class[] tClasses = new Class[] {};
            return Class.forName(className).getConstructor(tClasses).newInstance(tObjects);
        } catch (Exception e) {
            log.error("DataObjectFactory.getResource EXCEPTION : " + e.getMessage());
            throw e;
        }
    }
}
