package com.apelon.apps.dts.treebrowser.logon.beans;

import java.util.Vector;
import java.util.TreeMap;
import com.apelon.apps.dts.treebrowser.logon.application.builders.DriverFetcherImpl;
import com.apelon.apps.dts.treebrowser.logon.application.writers.DriverWriter;

/**
 * Stores driver information HTML for front-end display.
 * <p>
 *
 * @author    All source code copyright (c) 2003 Apelon, Inc.  All rights reserved.
 *
 * @version   Apelon Logon Widget 1.0
 */
public class DriverDisplayBean {

    public String driverHtml;

    public String driverTypesHtml;

    public String getDriverHtml() {
        return this.driverHtml;
    }

    public String getDriverTypesHtml() {
        return this.driverTypesHtml;
    }

    public void setDriverHtml() {
        DriverFetcherImpl fetcher = new DriverFetcherImpl();
        Vector drivers = fetcher.fetchDrivers();
        DriverWriter writer = new DriverWriter(drivers);
        driverHtml = writer.writeDriversHtml();
        driverTypesHtml = writer.writeDriverTypesHtml();
    }
}
