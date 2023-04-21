package net.sf.webwarp.reports.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.webwarp.reports.ReportData;
import net.sf.webwarp.reports.ReportRenderer;
import net.sf.webwarp.reports.SelectedParameterValue;
import net.sf.webwarp.util.types.MimeType;
import org.apache.commons.lang.Validate;

/**
 * Multi renderer that can contain multiple renderer instances.
 * @author mos
 */
public class AMultiRendererWrapper implements ReportRenderer {

    private Map<MimeType, ReportRenderer> renderers = new HashMap<MimeType, ReportRenderer>();

    public MimeType forceOutputFormat(Map<?, ?> context, ReportData data, MimeType outputFormat, List<SelectedParameterValue> selectedParameters) {
        Validate.isTrue(renderers.containsKey(outputFormat), "The is no renderer registered for the mimetype: " + outputFormat);
        return renderers.get(outputFormat).forceOutputFormat(context, data, outputFormat, selectedParameters);
    }

    /**
	 * Constructor
	 * @param rendererClasses The renderer classes to be instantiated when rendering reports.
	 */
    public AMultiRendererWrapper(Class<ReportRenderer>[] rendererClasses) {
        for (Class<ReportRenderer> rendererClass : rendererClasses) {
            try {
                ReportRenderer renderer = rendererClass.newInstance();
                for (MimeType mimeType : renderer.getOutputFormats()) {
                    Validate.isTrue(!renderers.containsKey(mimeType), "You may not register the mimetype: " + mimeType + " more than once!");
                    renderers.put(mimeType, renderer);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Collects all distinct mime types from the containing renderers.
     * @return The mime types supported by the renderers.
     */
    public MimeType[] getOutputFormats() {
        return renderers.keySet().toArray(new MimeType[0]);
    }

    /**
     * Renders the report. The first renderer found for the given mime type is used for rendering the report.
     * @see net.sf.webwarp.reports.ReportRenderer#renderReport(Map<?, ?>,ReportData,MimeType,List<SelectedParameterValue>)
     */
    public byte[] renderReport(Map<?, ?> context, ReportData data, MimeType mimeType, List<SelectedParameterValue> selectedParameters) {
        Validate.isTrue(renderers.containsKey(mimeType), "The is no renderer registered for the mimetype: " + mimeType);
        return renderers.get(mimeType).renderReport(context, data, mimeType, selectedParameters);
    }
}
