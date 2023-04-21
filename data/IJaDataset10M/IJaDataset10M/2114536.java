package com.volantis.mcs.runtime.configuration;

/**
 *
 */
public class DataSourceConfiguration implements AnonymousDataSource {

    /**
     *  Volantis copyright mark.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004. ";

    /**
     * The reference to the named data source.
     */
    private String ref;

    /**
    * This class is the configuration bean for the data-source element.
    */
    public DataSourceConfiguration() {
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String string) {
        ref = string;
    }
}
