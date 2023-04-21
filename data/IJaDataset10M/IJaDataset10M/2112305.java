package net.sf.jasperreports.jsf.renderkit;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import net.sf.jasperreports.jsf.ReportPhaseListener;
import net.sf.jasperreports.jsf.component.UIReport;

/**
 * The Class LinkRenderer.
 * 
 * @author A. Alonso Dominguez
 */
public class LinkRenderer extends AbstractReportRenderer {

    /** The Constant RENDERER_TYPE. */
    public static final String RENDERER_TYPE = "net.sf.jasperreports.Link";

    /** The logger. */
    private static final Logger logger = Logger.getLogger(LinkRenderer.class.getPackage().getName(), "net.sf.jasperreports.jsf.LogMessages");

    @Override
    @SuppressWarnings("unused")
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        final ViewHandler viewHandler = context.getApplication().getViewHandler();
        final UIReport report = (UIReport) component;
        final String reportURI = viewHandler.getResourceURL(context, buildReportURI(context, component));
        final ResponseWriter writer = context.getResponseWriter();
        logger.log(Level.FINE, "JRJSF_0001", component.getClientId(context));
        writer.startElement("a", component);
        renderIdAttribute(context, component);
        writer.writeURIAttribute("href", reportURI, null);
        renderAttributes(writer, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        writer.endElement("a");
        registerComponent(context, component);
    }

    @Override
    protected void renderAttributes(final ResponseWriter writer, final UIComponent report) throws IOException {
        super.renderAttributes(writer, report);
        final String target = (String) report.getAttributes().get("target");
        if (target != null) {
            writer.writeAttribute("target", target, null);
        }
    }
}
