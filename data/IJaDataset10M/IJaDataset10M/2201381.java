package net.sf.jasperreports.jsf.export;

import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.util.Util;

/**
 * A factory for creating Exporter objects.
 */
public final class ExporterFactory {

    /** The Constant SERVICES_RESOURCE. */
    private static final String SERVICES_RESOURCE = "META-INF/services/" + Exporter.class.getName();

    /** The exporter cache map. */
    private static final Map<String, Class<Exporter>> exporterCacheMap = Util.loadServiceMap(SERVICES_RESOURCE);

    /**
     * Gets the single instance of Exporter.
     * 
     * @param report the report
     * @param context the context
     * 
     * @return single instance of Exporter
     * 
     * @throws ExporterException the exporter exception
     */
    public static Exporter getExporter(final FacesContext context, final UIReport report) throws ExporterException {
        if (!(report instanceof UIComponent)) {
            throw new IllegalArgumentException();
        }
        final Class<Exporter> exporterClass = exporterCacheMap.get(report.getFormat());
        if (exporterClass == null) {
            throw new ExporterNotFoundException(report.getFormat());
        }
        Exporter result;
        try {
            result = exporterClass.newInstance();
        } catch (final Exception e) {
            throw new ExporterException(e);
        }
        result.setComponent((UIComponent) report);
        return result;
    }

    /**
     * Instantiates a new exporter factory.
     */
    private ExporterFactory() {
    }
}
