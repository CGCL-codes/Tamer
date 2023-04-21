package org.adapit.wctoolkit.models.xml;

import java.util.ArrayList;
import java.util.Iterator;
import org.adapit.wctoolkit.models.config.ApplicationConfiguration;

@SuppressWarnings({ "unchecked" })
public abstract class AbstractExporterManager {

    protected ArrayList exporters = new ArrayList();

    public AbstractExporterManager() {
    }

    public void initialize() {
        ApplicationConfiguration.getInstance().getUsedExportInterceptors().clear();
        Iterator<ExportInterceptor> it = exporters.iterator();
        while (it.hasNext()) {
            ExportInterceptor ei = it.next();
            ApplicationConfiguration.getInstance().getUsedExportInterceptors().put(ei.getElementClass(), ei);
        }
    }

    public abstract String getExporterName();

    public abstract String getXmiVersion();

    public ArrayList getExporters() {
        return exporters;
    }

    public void setExporters(ArrayList exporters) {
        this.exporters = exporters;
    }
}
