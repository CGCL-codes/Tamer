package org.forzaframework.util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.util.JRProperties;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import ar.com.fdvs.dj.core.layout.LayoutManager;
import ar.com.fdvs.dj.core.registration.ColumnRegistrationManager;
import ar.com.fdvs.dj.core.registration.DJGroupRegistrationManager;
import ar.com.fdvs.dj.core.DJException;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.CoreException;
import ar.com.fdvs.dj.core.DJJRDesignHelper;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.DynamicJasperDesign;
import ar.com.fdvs.dj.domain.ColumnProperty;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.Subreport;
import ar.com.fdvs.dj.domain.entities.Parameter;
import ar.com.fdvs.dj.util.DJCompilerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author cesarreyes
 *         Date: 07-dic-2008
 *         Time: 19:07:17
 */
public class ReportUtils {

    private static final Log log = LogFactory.getLog(DynamicJasperHelper.class);

    public static final String DEFAULT_XML_ENCODING = "UTF-8";

    private static final String DJ_RESOURCE_BUNDLE = "dj-messages";

    private static final void registerEntities(DynamicJasperDesign jd, DynamicReport dr) {
        new ColumnRegistrationManager(jd, dr).registerEntities(dr.getColumns());
        new DJGroupRegistrationManager(jd, dr).registerEntities(dr.getColumnsGroups());
        registerOtherFields(jd, dr.getFields());
        Locale locale = dr.getReportLocale() == null ? Locale.getDefault() : dr.getReportLocale();
        if (log.isDebugEnabled()) {
            log.debug("Requested Locale = " + dr.getReportLocale() + ", Locale to use: " + locale);
        }
        ResourceBundle messages = null;
        if (dr.getResourceBundle() != null) {
            try {
                messages = ResourceBundle.getBundle(dr.getResourceBundle(), locale);
            } catch (MissingResourceException e) {
                log.warn(e.getMessage() + ", usign defaut (dj-messages)");
            }
        }
        if (messages == null) {
            try {
                messages = ResourceBundle.getBundle(DJ_RESOURCE_BUNDLE, locale);
            } catch (MissingResourceException e) {
                log.warn(e.getMessage() + ", usign defaut (dj-messages)");
                try {
                    messages = ResourceBundle.getBundle(DJ_RESOURCE_BUNDLE, Locale.ENGLISH);
                } catch (MissingResourceException e2) {
                    log.error("Default messajes not found: " + DJ_RESOURCE_BUNDLE + ", " + e2.getMessage(), e2);
                    throw new DJException("Default messajes file not found: " + DJ_RESOURCE_BUNDLE + "en.properties", e2);
                }
            }
        }
        jd.getParametersWithValues().put(JRDesignParameter.REPORT_RESOURCE_BUNDLE, messages);
        jd.getParametersWithValues().put(JRDesignParameter.REPORT_LOCALE, locale);
    }

    /**
     * Compiles and fills the reports design.
     *
     * @param dr            the DynamicReport
     * @param layoutManager the object in charge of doing the layout
     * @param ds            The datasource
     * @param _parameters   Map with parameters that the report may need
     * @return
     * @throws JRException
     */
    public static JasperPrint generateJasperPrint(DynamicReport dr, LayoutManager layoutManager, JRDataSource ds, Map _parameters) throws JRException {
        JasperPrint jp = null;
        if (_parameters == null) _parameters = new HashMap();
        visitSubreports(dr, _parameters);
        compileOrLoadSubreports(dr, _parameters);
        DynamicJasperDesign jd = generateJasperDesign(dr);
        Map params = new HashMap();
        if (!_parameters.isEmpty()) {
            registerParams(jd, _parameters);
            params.putAll(_parameters);
        }
        registerEntities(jd, dr);
        layoutManager.applyLayout(jd, dr);
        JRProperties.setProperty(JRProperties.COMPILER_CLASS, DJCompilerFactory.getCompilerClassName());
        JasperReport jr = JasperCompileManager.compileReport(jd);
        jp = JasperFillManager.fillReport(jr, _parameters, ds);
        return jp;
    }

    /**
     * Performs any needed operation on subreports after they are built like ensuring proper subreport with
     * if "fitToParentPrintableArea" flag is set to true
     *
     * @param dr
     * @param _parameters
     * @throws JRException
     */
    protected static void visitSubreports(DynamicReport dr, Map _parameters) throws JRException {
        for (Iterator iterator = dr.getColumnsGroups().iterator(); iterator.hasNext(); ) {
            DJGroup group = (DJGroup) iterator.next();
            for (Iterator iterator2 = group.getHeaderSubreports().iterator(); iterator2.hasNext(); ) {
                Subreport subreport = (Subreport) iterator2.next();
                if (subreport.getDynamicReport() != null) {
                    visitSubreport(dr, subreport, _parameters);
                    visitSubreports(subreport.getDynamicReport(), _parameters);
                }
            }
            for (Iterator iterator2 = group.getFooterSubreports().iterator(); iterator2.hasNext(); ) {
                Subreport subreport = (Subreport) iterator2.next();
                if (subreport.getDynamicReport() != null) {
                    visitSubreport(dr, subreport, _parameters);
                    visitSubreports(subreport.getDynamicReport(), _parameters);
                }
            }
        }
    }

    protected static void visitSubreport(DynamicReport parentDr, Subreport subreport, Map _parameters) {
        DynamicReport childDr = subreport.getDynamicReport();
        if (subreport.isFitToParentPrintableArea()) {
            childDr.getOptions().setPage(parentDr.getOptions().getPage());
            childDr.getOptions().setLeftMargin(parentDr.getOptions().getLeftMargin());
            childDr.getOptions().setRightMargin(parentDr.getOptions().getRightMargin());
        }
    }

    protected static void compileOrLoadSubreports(DynamicReport dr, Map _parameters) throws JRException {
        for (Iterator iterator = dr.getColumnsGroups().iterator(); iterator.hasNext(); ) {
            DJGroup group = (DJGroup) iterator.next();
            for (Iterator iterator2 = group.getHeaderSubreports().iterator(); iterator2.hasNext(); ) {
                Subreport subreport = (Subreport) iterator2.next();
                if (subreport.getDynamicReport() != null) {
                    compileOrLoadSubreports(subreport.getDynamicReport(), _parameters);
                    JasperReport jp = generateJasperReport(subreport.getDynamicReport(), subreport.getLayoutManager(), _parameters);
                    _parameters.put(jp.toString(), jp);
                    subreport.setReport(jp);
                }
            }
            for (Iterator iterator2 = group.getFooterSubreports().iterator(); iterator2.hasNext(); ) {
                Subreport subreport = (Subreport) iterator2.next();
                if (subreport.getDynamicReport() != null) {
                    compileOrLoadSubreports(subreport.getDynamicReport(), _parameters);
                    JasperReport jp = generateJasperReport(subreport.getDynamicReport(), subreport.getLayoutManager(), _parameters);
                    _parameters.put(jp.toString(), jp);
                    subreport.setReport(jp);
                }
            }
        }
    }

    /**
     * For every String key, it registers the object as a parameter to make it available
     * in the report.
     * @param jd
     * @param _parameters
     */
    public static void registerParams(DynamicJasperDesign jd, Map _parameters) {
        for (Iterator iterator = _parameters.keySet().iterator(); iterator.hasNext(); ) {
            Object key = iterator.next();
            if (key instanceof String) {
                try {
                    if (jd.getParametersMap().get(key) != null) {
                        log.warn("Parameter \"" + key + "\" already registered, skipping this one.");
                        continue;
                    }
                    JRDesignParameter parameter = new JRDesignParameter();
                    Object value = _parameters.get(key);
                    if (value == null) continue;
                    Class clazz = value.getClass().getComponentType();
                    if (clazz == null) clazz = value.getClass();
                    parameter.setValueClass(clazz);
                    parameter.setName((String) key);
                    jd.addParameter(parameter);
                } catch (JRException e) {
                }
            }
        }
    }

    /**
     * Compiles the report and applies the layout. <b>generatedParams</b> MUST NOT BE NULL
     * All the key objects from the generatedParams map that are String, will be registered as parameters of the report.
     * @param dr
     * @param layoutManager
     * @param generatedParams
     * @return
     * @throws JRException
     */
    public static final JasperReport generateJasperReport(DynamicReport dr, LayoutManager layoutManager, Map generatedParams) throws JRException {
        log.info("generating JasperReport");
        JasperReport jr = null;
        if (generatedParams == null) generatedParams = new HashMap();
        visitSubreports(dr, generatedParams);
        compileOrLoadSubreports(dr, generatedParams);
        DynamicJasperDesign jd = generateJasperDesign(dr);
        registerEntities(jd, dr);
        registerParams(jd, generatedParams);
        layoutManager.applyLayout(jd, dr);
        JRProperties.setProperty(JRProperties.COMPILER_CLASS, "ar.com.fdvs.dj.util.DJJRJdtCompiler");
        jr = JasperCompileManager.compileReport(jd);
        generatedParams.putAll(jd.getParametersWithValues());
        return jr;
    }

    protected static DynamicJasperDesign generateJasperDesign(DynamicReport dr) throws CoreException {
        DynamicJasperDesign jd = null;
        try {
            if (dr.getTemplateFileName() != null) {
                log.info("loading template file: " + dr.getTemplateFileName());
                log.info("Attemping to find the file directly in the file system...");
                File file = new File(dr.getTemplateFileName());
                if (file.exists()) {
                    JasperDesign jdesign = JRXmlLoader.load(file);
                    jd = DJJRDesignHelper.downCast(jdesign, dr);
                } else {
                    log.info("Not found: Attemping to find the file in the classpath...");
                    URL url = DynamicJasperHelper.class.getClassLoader().getResource(dr.getTemplateFileName());
                    JasperDesign jdesign = JRXmlLoader.load(url.openStream());
                    jd = DJJRDesignHelper.downCast(jdesign, dr);
                }
                JasperDesignHelper.populateReportOptionsFromDesign(jd, dr);
            } else {
                jd = DJJRDesignHelper.getNewDesign(dr);
            }
            registerParameters(jd, dr);
        } catch (JRException e) {
            throw new CoreException(e.getMessage(), e);
        } catch (IOException e) {
            throw new CoreException(e.getMessage(), e);
        }
        return jd;
    }

    protected static void registerParameters(DynamicJasperDesign jd, DynamicReport dr) {
        for (Iterator iterator = dr.getParameters().iterator(); iterator.hasNext(); ) {
            Parameter param = (Parameter) iterator.next();
            JRDesignParameter jrparam = new JRDesignParameter();
            jrparam.setName(param.getName());
            jrparam.setValueClassName(param.getClassName());
            try {
                jd.addParameter(jrparam);
            } catch (JRException e) {
                throw new CoreException(e.getMessage(), e);
            }
        }
    }

    private static void registerOtherFields(DynamicJasperDesign jd, List fields) {
        for (Iterator iter = fields.iterator(); iter.hasNext(); ) {
            ColumnProperty element = (ColumnProperty) iter.next();
            JRDesignField field = new JRDesignField();
            field.setValueClassName(element.getValueClassName());
            field.setName(element.getProperty());
            try {
                jd.addField(field);
            } catch (JRException e) {
                log.warn(e.getMessage(), e);
            }
        }
    }
}
