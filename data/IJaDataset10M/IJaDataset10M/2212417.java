package net.wgen.op;

import net.wgen.op.system.OpModule;

/**
 * @author paulf
 * @version $Id: ExampleModule.java 8 2007-01-17 15:37:22Z paulfeuer $
 */
public class ExampleModule extends OpModule {

    public static final String NAME = "EXAMPLE";

    public static final String DATA_SOURCE = "java:EXAMPLEDS";

    public static final Long APPLICATION_ID = new Long(1);

    public ExampleModule() {
        super(NAME);
    }

    /**
     * The amount of the url that can be claimed by this Application. Returning a 0 means that this
     * application makes no claim on the url. The highest scoring claim wins.
     *
     * @param url
     *
     * @return the amount of the url that can be claimed by this url
     */
    public int getUrlMatchLength(String url) {
        return 0;
    }

    /**
     * The amount of the class name that can be claimed by this Application. Returning a 0 means
     * that this application makes no claim on the class. The highest scoring claim wins.
     *
     * @param clazz
     *
     * @return the amount of the url that can be claimed by this url
     */
    public int getClassMatchLength(Class clazz) {
        return 0;
    }

    /**
     * The applicationSid of the application.
     *
     * @return the applicationSid of the application
     */
    public Long getModuleId() {
        return APPLICATION_ID;
    }

    /**
     * The datasource name in the oracle-xa-ds configuration file.
     *
     * @return the datasource name in the oracle-xa-ds configuration file.
     */
    public String getDataSourceName() {
        return DATA_SOURCE;
    }
}
