package org.ofbiz.birt.report.context;

import java.net.URL;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.exception.ViewerException;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.utility.BirtUtility;
import org.eclipse.birt.report.utility.DataUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;
import org.ofbiz.base.location.FlexibleLocation;

public class BirtViewerAttributeBean extends ViewerAttributeBean {

    public static final String module = BirtViewerAttributeBean.class.getName();

    /**
     * Module Options
     */
    private Map moduleOptions = null;

    /**
     * Request Type
     */
    private String requestType;

    private Boolean reportRtl;

    public BirtViewerAttributeBean(HttpServletRequest arg0) {
        super(arg0);
    }

    /**
     * Init the bean.
     * 
     * @param request
     * @throws Exception
     */
    @Override
    protected void __init(HttpServletRequest request) throws Exception {
        if (ParameterAccessor.isGetImageOperator(request) && (IBirtConstants.SERVLET_PATH_FRAMESET.equalsIgnoreCase(request.getServletPath()) || IBirtConstants.SERVLET_PATH_OUTPUT.equalsIgnoreCase(request.getServletPath()) || IBirtConstants.SERVLET_PATH_RUN.equalsIgnoreCase(request.getServletPath()) || IBirtConstants.SERVLET_PATH_PREVIEW.equalsIgnoreCase(request.getServletPath()))) {
            return;
        }
        this.category = "BIRT";
        this.masterPageContent = ParameterAccessor.isMasterPageContent(request);
        this.isDesigner = ParameterAccessor.isDesigner();
        if (!ParameterAccessor.isBookmarkReportlet(request)) {
            this.bookmark = ParameterAccessor.getBookmark(request);
        } else {
            this.bookmark = null;
        }
        this.isToc = ParameterAccessor.isToc(request);
        this.reportPage = ParameterAccessor.getPage(request);
        this.reportPageRange = ParameterAccessor.getPageRange(request);
        this.action = ParameterAccessor.getAction(request);
        if (IBirtConstants.SERVLET_PATH_FRAMESET.equalsIgnoreCase(request.getServletPath()) || IBirtConstants.SERVLET_PATH_OUTPUT.equalsIgnoreCase(request.getServletPath()) || IBirtConstants.SERVLET_PATH_DOWNLOAD.equalsIgnoreCase(request.getServletPath()) || IBirtConstants.SERVLET_PATH_EXTRACT.equalsIgnoreCase(request.getServletPath())) {
            this.reportDocumentName = ParameterAccessor.getReportDocument(request, null, true);
        } else {
            this.reportDocumentName = ParameterAccessor.getReportDocument(request, null, false);
        }
        String reportParam = DataUtil.trimString(ParameterAccessor.getParameter(request, ParameterAccessor.PARAM_REPORT));
        if (reportParam.startsWith("component://")) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                loader = BirtViewerAttributeBean.class.getClassLoader();
            }
            URL reportFileUrl = null;
            reportFileUrl = FlexibleLocation.resolveLocation(reportParam, loader);
            if (reportFileUrl == null) {
                throw new IllegalArgumentException("Could not resolve location to URL: " + reportParam);
            }
            this.reportDesignName = reportFileUrl.getPath();
        } else {
            this.reportDesignName = ParameterAccessor.getReport(request, null);
        }
        this.emitterId = ParameterAccessor.getEmitterId(request);
        this.format = ParameterAccessor.getFormat(request);
        if (IBirtConstants.ACTION_PRINT.equalsIgnoreCase(action)) {
            if (ParameterAccessor.isSupportedPrintOnServer) {
                this.format = IBirtConstants.POSTSCRIPT_RENDER_FORMAT;
                this.emitterId = null;
            } else {
                this.action = null;
            }
        }
        BirtResources.setLocale(ParameterAccessor.getLocale(request));
        this.requestType = request.getHeader(ParameterAccessor.HEADER_REQUEST_TYPE);
        processReport(request);
        this.reportTitle = ParameterAccessor.getTitle(request);
        this.isShowTitle = ParameterAccessor.isShowTitle(request);
        this.isShowToolbar = ParameterAccessor.isShowToolbar(request);
        this.isShowNavigationbar = ParameterAccessor.isShowNavigationbar(request);
        this.moduleOptions = BirtUtility.getModuleOptions(request);
        this.reportDesignHandle = getDesignHandle(request);
        if (this.reportDesignHandle == null) throw new ViewerException(ResourceConstants.GENERAL_EXCEPTION_NO_REPORT_DESIGN);
        this.reportRtl = null;
        __initParameters(request);
    }
}
