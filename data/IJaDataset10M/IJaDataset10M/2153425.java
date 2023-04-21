package org.zkoss.zkex.zul;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import org.zkoss.lang.Objects;
import org.zkoss.util.Locales;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.util.logging.Log;
import org.zkoss.xml.HTMLs;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.ext.render.DynamicMedia;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.impl.Utils;

/**
 * The JasperReport component. It is used to generate a Jasper report into an
 * inline frame.
 * 
 * <p>
 * Note: this component is serializable only if the data source ({@link #getDatasource})
 * is serializable.
 * 
 * @author gracelin
 * @since 3.0.2
 */
public class Jasperreport extends Iframe implements org.zkoss.zkex.zul.api.Jasperreport {

    private static final Log log = Log.lookup(Jasperreport.class);

    private static final long serialVersionUID = 20080117L;

    private static final String TASK_PDF = "pdf";

    private static final String TASK_XML = "xml";

    private static final String TASK_HTML = "html";

    private static final String TASK_RTF = "rtf";

    private static final String TASK_XLS = "xls";

    private static final String TASK_JXL = "jxl";

    private static final String TASK_CSV = "csv";

    private static final String TASK_ODT = "odt";

    private static final String IMAGE_DIR = "img/";

    private String _src;

    private Map _parameters;

    private JRDataSource _datasource;

    private int _medver;

    private String _type = TASK_PDF;

    private Locale _locale;

    /** The result of the image map is cached since HTML results will
	 * cause other requests to them.
	 */
    private transient Map _imageMap;

    /** The result is cached since the browser might send the request
	 * multiple times (such as parent's invalidated or setVParent at client).
	 */
    private transient Media _media;

    public Jasperreport() {
        setHeight("100%");
        setWidth("100%");
    }

    public Jasperreport(String src) {
        setSrc(src);
    }

    /**
	 * Returns the source (jasper file).
	 * 
	 * @return src The compiled file (jasper file).
	 */
    public String getSrc() {
        return _src;
    }

    /**
	 * Sets the source (jasper file).
	 * <p>
	 * If src is changed, the whole component is invalidate.
	 * 
	 * @param src
	 *            The compiled file (jasper file). If null or empty, nothing is
	 *            included.
	 */
    public void setSrc(String src) {
        if (src != null && src.length() == 0) src = null;
        if (!Objects.equals(_src, src)) {
            _src = src;
            invalidate();
        }
    }

    public void setContent(Media media) {
        throw new UnsupportedOperationException("readonly");
    }

    protected String getEncodedSrc() {
        if (_src == null) {
            final Desktop dt = getDesktop();
            return dt != null ? dt.getExecution().encodeURL("~./img/spacer.gif") : "";
        } else {
            StringTokenizer st = new StringTokenizer(_src, ".");
            return Utils.getDynamicMediaURI(this, _medver++, st.nextToken(), _type.equals("jxl") ? "xls" : _type);
        }
    }

    /**
	 * Returns the JasperReports Parameters.
	 * <p>Default: null.
	 */
    public Map getParameters() {
        return _parameters;
    }

    /**
	 * Sets the JasperReports Parameters.
	 * 
	 * @param parameters
	 *            use to fill the report
	 */
    public void setParameters(Map parameters) {
        if (!Objects.equals(_parameters, parameters)) {
            _parameters = parameters;
            invalidate();
        }
    }

    /**
	 * Returns the JasperReports DataSource.
	 * <p>Default: null.
	 */
    public JRDataSource getDatasource() {
        return _datasource;
    }

    /**
	 * Sets the JasperReports DataSource.
	 * 
	 * @param dataSource
	 *            use to fill the report
	 */
    public void setDatasource(JRDataSource dataSource) {
        if (!Objects.equals(_datasource, dataSource)) {
            _datasource = dataSource;
            invalidate();
        }
    }

    /**
	 * Returns the output file type.
	 * <p>Default: pdf.
	 * @since 3.0.3
	 */
    public String getType() {
        return _type;
    }

    /**
	 * Sets the output file type. 
	 * Allowed type: pdf, xml, html, rtf, xls, jxl, csv and odt.
	 * <dl>
	 * <dt>pdf</dt>
	 * <dd>The PDF format.</dd>
	 * <dt>rtf</dt>
	 * <dd>The RTF format (Rich Text Format).</dd>
	 * <dt>xls</dt>
	 * <dd>The Microsoft Excel format generated with <a href="http://poi.apache.org">Apache POI</a>.</dd>
	 * <dt>jxl</dt>
	 * <dd>The Microsoft Excel format generated with <a href="http://jexcelapi.sourceforge.net/">JExcelApi</a>.</dd>
	 * <dt>csv</dt>
	 * <dd>The CSV format (Comma-Separated Values)</dd>
	 * <dt>odt</dt>
	 * <dd>The OpenOffice Writer format</dd>
	 * </dl>
	 * 
	 * @param type type of output file. If type is null, assign it a default value "pdf".
	 * @since 3.0.3
	 */
    public void setType(String type) {
        if (type == null) type = "pdf";
        if (!Objects.equals(_type, type)) {
            _type = type;
            invalidate();
        }
    }

    /**
	 * Returns the output file locale.
	 *
	 * <p>Default: null (means the ZK default, {@link Locales#getCurrent})).</p>
	 *
	 * <table border="1">
	 * <tr>
	 * <td>{@link #getLocale}</td>
	 * <td>{@link #getParameters} with a value<br/>
	 * associated with JRParameter.REPORT_LOCALE</td>
	 * <td>What is used</td>
	 * </tr>
	 * <tr>
	 * <td>X</td><td>ignored</td><td>X</td>
	 * </tr>
	 * <tr>
	 * <td>null</td><td>Y</td><td>Y</td>
	 * </tr>
	 * <tr>
	 * <td>null</td><td>null</td><td>{@link Locales#getCurrent}</td>
	 * </tr>
	 * </table>
	 * 
	 * @since 3.0.4
	 */
    public Locale getLocale() {
        return _locale;
    }

    /**
	 * Sets the output file locale.
	 *
	 * @param locale the locale. If null, the ZK default is used
	 * ({@link Locales#getCurrent}).
	 * @see #getLocale
	 * @since 3.0.4
	 */
    public void setLocale(Locale locale) {
        if (!Objects.equals(_locale, locale)) {
            _locale = locale;
            invalidate();
        }
    }

    public void invalidate() {
        _media = null;
        super.invalidate();
    }

    protected Object newExtraCtrl() {
        return new ExtraCtrl();
    }

    /**
	 * A utility class to implement {@link #getExtraCtrl}. It is used only by
	 * component developers.
	 */
    protected class ExtraCtrl extends Iframe.ExtraCtrl {

        public Media getMedia(String pathInfo) {
            int indexOfImg = pathInfo.lastIndexOf(IMAGE_DIR);
            if (indexOfImg >= 0) {
                String imageName = pathInfo.substring(indexOfImg + IMAGE_DIR.length());
                if (imageName.indexOf(".") < 0) {
                    return getImage(imageName);
                }
            }
            return doReport();
        }
    }

    /**
	 * Use the Parameters & Data sourse to produce report. If parameters are
	 * null, we will use an empty Map. If data source is null, use
	 * JREmptyDataSource.
	 * 
	 * @return A AMedia contains report's byte stream.
	 */
    private Media doReport() {
        if (_media != null) return _media;
        InputStream is = null;
        try {
            final Execution exec = Executions.getCurrent();
            is = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI(_src, false));
            if (is == null) {
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream(_src);
                if (is == null) {
                    File fl = new File(_src);
                    if (!fl.exists()) throw new RuntimeException("resource for " + _src + " not found.");
                    is = new FileInputStream(fl);
                }
            }
            final Map params;
            Map exportPara = null;
            if (_parameters == null) params = new HashMap(); else {
                params = _parameters;
                exportPara = (Map) params.remove("exportParameter");
            }
            if (_locale != null) params.put(JRParameter.REPORT_LOCALE, _locale); else if (!params.containsKey(JRParameter.REPORT_LOCALE)) params.put(JRParameter.REPORT_LOCALE, Locales.getCurrent());
            JasperPrint jasperPrint = JasperFillManager.fillReport(is, params, _datasource != null ? _datasource : new JREmptyDataSource());
            if (TASK_PDF.equals(_type)) {
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                JRExporter exporter = new JRPdfExporter();
                if (exportPara != null) exporter.setParameters(exportPara);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, arrayOutputStream);
                exporter.exportReport();
                arrayOutputStream.close();
                _imageMap = (Map) exporter.getParameter(JRHtmlExporterParameter.IMAGES_MAP);
                return _media = new AMedia("report.pdf", "pdf", "application/pdf", arrayOutputStream.toByteArray());
            } else if (TASK_XML.equals(_type)) {
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                JRExporter exporter = new JRXmlExporter();
                if (exportPara != null) exporter.setParameters(exportPara);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, arrayOutputStream);
                exporter.exportReport();
                arrayOutputStream.close();
                _imageMap = (Map) exporter.getParameter(JRHtmlExporterParameter.IMAGES_MAP);
                return _media = new AMedia("report.xml", "xml", "text/xml", arrayOutputStream.toByteArray());
            } else if (TASK_HTML.equals(_type)) {
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                JRExporter exporter = new JRHtmlExporter();
                if (exportPara != null) exporter.setParameters(exportPara);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, arrayOutputStream);
                exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, new HashMap());
                exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, IMAGE_DIR);
                exporter.exportReport();
                arrayOutputStream.close();
                _imageMap = (Map) exporter.getParameter(JRHtmlExporterParameter.IMAGES_MAP);
                return _media = new AMedia("report.html", "html", "text/html", arrayOutputStream.toByteArray());
            } else if (TASK_RTF.equals(_type)) {
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                JRExporter exporter = new JRRtfExporter();
                if (exportPara != null) exporter.setParameters(exportPara);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, arrayOutputStream);
                exporter.exportReport();
                arrayOutputStream.close();
                _imageMap = (Map) exporter.getParameter(JRHtmlExporterParameter.IMAGES_MAP);
                return _media = new AMedia("report.rtf", "rtf", "application/rtf", arrayOutputStream.toByteArray());
            } else if (TASK_XLS.equals(_type)) {
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                JRExporter exporter = new JRXlsExporter();
                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
                if (exportPara != null) exporter.setParameters(exportPara);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, arrayOutputStream);
                exporter.exportReport();
                arrayOutputStream.close();
                _imageMap = (Map) exporter.getParameter(JRHtmlExporterParameter.IMAGES_MAP);
                return _media = new AMedia("report.xls", "xls", "application/vnd.ms-excel", arrayOutputStream.toByteArray());
            } else if (TASK_JXL.equals(_type)) {
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                JRExporter exporter = new JExcelApiExporter();
                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
                if (exportPara != null) exporter.setParameters(exportPara);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, arrayOutputStream);
                exporter.exportReport();
                arrayOutputStream.close();
                _imageMap = (Map) exporter.getParameter(JRHtmlExporterParameter.IMAGES_MAP);
                return _media = new AMedia("report.xls", "xls", "application/vnd.ms-excel", arrayOutputStream.toByteArray());
            } else if (TASK_CSV.equals(_type)) {
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                JRExporter exporter = new JRCsvExporter();
                if (exportPara != null) exporter.setParameters(exportPara);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, arrayOutputStream);
                exporter.exportReport();
                arrayOutputStream.close();
                _imageMap = (Map) exporter.getParameter(JRHtmlExporterParameter.IMAGES_MAP);
                return _media = new AMedia("report.csv", "csv", "text/csv", arrayOutputStream.toByteArray());
            } else if (TASK_ODT.equals(_type)) {
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                JRExporter exporter = new JROdtExporter();
                if (exportPara != null) exporter.setParameters(exportPara);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, arrayOutputStream);
                exporter.exportReport();
                arrayOutputStream.close();
                _imageMap = (Map) exporter.getParameter(JRHtmlExporterParameter.IMAGES_MAP);
                return _media = new AMedia("report.odt", "odt", "application/vnd.oasis.opendocument.text", arrayOutputStream.toByteArray());
            } else {
                throw new UiException("Type: " + _type + " is not supported in JasperReports.");
            }
        } catch (Exception ex) {
            throw UiException.Aide.wrap(ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.warningBriefly("Ignored: unable to close", e);
                }
            }
        }
    }

    /**
	 * When output file type is HTML, return image in AMedia
	 * 
	 * @param imageName the request image file name
	 * @return A AMdia contains a image.
	 */
    private AMedia getImage(String imageName) {
        if (_imageMap == null) {
            log.warning("The image map not ready, " + imageName);
            return null;
        }
        byte[] imageBytes = (byte[]) _imageMap.get(imageName);
        return new AMedia(imageName, "", "image/gif", imageBytes);
    }
}
