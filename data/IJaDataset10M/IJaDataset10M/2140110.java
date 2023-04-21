package com.softserve.mproject.utils.common;

import com.softserve.mproject.utils.csv.ExportToCSV;
import com.softserve.mproject.utils.xls.ExportToXLS;
import com.softserve.mproject.utils.xml.ExportToXML;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory for export utils
 * @author serrega
 */
public class ExportFactory {

    private static ExportFactory instance;

    private Map<String, ExportManager> exporters = new HashMap<String, ExportManager>();

    private ExportFactory() {
        exporters.put("xml", new ExportToXML());
        exporters.put("xls", new ExportToXLS());
        exporters.put("xlsx", new ExportToXLS());
        exporters.put("csv", new ExportToCSV());
        exporters.put("txt", new ExportToCSV());
    }

    public static ExportFactory getInstance() {
        if (instance == null) {
            instance = new ExportFactory();
        }
        return instance;
    }

    public ExportManager getExportManager(String s) {
        return exporters.get(s);
    }
}
