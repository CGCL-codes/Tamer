package com.od.jtimeseries.ui.config;

import com.od.jtimeseries.identifiable.Identifiable;

/**
 * Created by IntelliJ IDEA.
 * User: Nick Ebbutt
 * Date: 04/04/11
 * Time: 07:02
 */
public interface ExportableConfigHolder extends Identifiable {

    public ExportableConfig getExportableConfig();

    String getDefaultFileName();
}
