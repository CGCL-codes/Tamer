package org.opentides.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.opentides.bean.DynamicReport;
import org.opentides.bean.ReportDefinition;
import org.opentides.service.ReportService;
import org.opentides.util.StringUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.opentides.bean.BaseReportValidator;

/**
 * @author allantan
 * 
 */
public class GenerateReportController extends AbstractController {

    private static Logger _log = Logger.getLogger(GenerateReportController.class);

    private String paramView = "/core/report/prompt-parameter";

    private ReportService service;

    private String reportWebPath = "/jasper";

    private Map<String, BaseReportValidator> reportValidators;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView view = validateReport(request, response);
        if (view == null) {
            return generateReport(request, response);
        } else return view;
    }

    @SuppressWarnings("unchecked")
    public ModelAndView validateReport(HttpServletRequest request, HttpServletResponse response) {
        InputStream jasperStream = null;
        InputStream jrXmlStream = null;
        try {
            jasperStream = getReportStream(request.getParameter(DynamicReport.REPORT_FILE), "jasper");
            jrXmlStream = getReportStream(request.getParameter(DynamicReport.REPORT_FILE), "jrxml");
            if (jasperStream == null || jrXmlStream == null) {
                Map<String, String> model = new HashMap<String, String>();
                model.put("title", "error.report");
                model.put("message", "error.report-file-not-found");
                return new ModelAndView("user-message", model);
            }
            List<ReportDefinition> missingParameters = service.getMissingParameters(request.getParameterMap(), jrXmlStream);
            if (!missingParameters.isEmpty()) {
                Map<String, Object> model = new HashMap<String, Object>();
                model.put("missingParameters", missingParameters);
                model.put("requestParameters", request.getParameterMap());
                return new ModelAndView(paramView, model);
            }
            return null;
        } finally {
            try {
                if (jasperStream != null) jasperStream.close();
                if (jrXmlStream != null) jrXmlStream.close();
            } catch (Exception ex) {
            }
        }
    }

    @SuppressWarnings("unchecked")
    public ModelAndView generateReport(HttpServletRequest request, HttpServletResponse response) {
        InputStream jasperStream = null;
        InputStream jrXmlStream = null;
        try {
            jasperStream = getReportStream(request.getParameter(DynamicReport.REPORT_FILE), "jasper");
            jrXmlStream = getReportStream(request.getParameter(DynamicReport.REPORT_FILE), "jrxml");
            Map<String, String[]> parameters = request.getParameterMap();
            String[] validatorParam = parameters.get("validator");
            if (validatorParam != null) {
                String key = validatorParam[0];
                BaseReportValidator validator = reportValidators.get(key);
                if (validator.supports(DynamicReport.class)) {
                    List<String> errorMessages = validator.validate(request);
                    if (errorMessages != null) {
                        Map<String, Object> model = new HashMap<String, Object>();
                        Map<String, String[]> requestParameters = transformRequest(request);
                        List<ReportDefinition> missingParameters = service.getMissingParameters(requestParameters, jrXmlStream);
                        if (!missingParameters.isEmpty()) {
                            model.put("missingParameters", missingParameters);
                        }
                        model.put("requestParameters", requestParameters);
                        model.put("errorMessages", errorMessages);
                        return new ModelAndView(paramView, model);
                    }
                } else {
                    throw new IllegalArgumentException("Validator does not support command class [" + DynamicReport.class.getName() + "]");
                }
            }
            Map<String, Object> reportParam = service.getParameterValues(parameters, jrXmlStream);
            String reportFormat = request.getParameter(DynamicReport.REPORT_FORMAT);
            this.setHeader(response);
            if (DynamicReport.FORMAT_IMAGE.equals(reportFormat)) {
                byte[] byteArray = service.generateImage(jasperStream, reportParam);
                if (byteArray != null) {
                    response.setContentLength(byteArray.length);
                    response.setContentType("image/png");
                    ServletOutputStream out = response.getOutputStream();
                    out.write(byteArray, 0, byteArray.length);
                    out.flush();
                    out.close();
                }
            } else if (DynamicReport.FORMAT_HTML.equals(reportFormat)) {
                String html = service.generateHtml(jasperStream, reportParam);
                response.setContentLength(html.length());
                response.setContentType("text/html");
                ServletOutputStream out = response.getOutputStream();
                out.write(html.getBytes(), 0, html.length());
                out.flush();
                out.close();
            } else if (DynamicReport.FORMAT_PDF.equals(reportFormat)) {
                byte[] byteArray = service.generatePdf(jasperStream, reportParam);
                if (byteArray != null) {
                    response.setContentLength(byteArray.length);
                    response.setContentType("application/pdf");
                    if (!StringUtil.isEmpty(request.getParameter("reportName"))) {
                        response.setHeader("Content-Disposition", "attachment; filename=\"" + request.getParameter("reportName") + ".pdf\"");
                    }
                    ServletOutputStream out = response.getOutputStream();
                    out.write(byteArray, 0, byteArray.length);
                    out.flush();
                    out.close();
                }
            } else if (DynamicReport.FORMAT_EXCEL.equals(reportFormat)) {
            }
        } catch (Exception e) {
            _log.error(e, e);
            Map<String, String> model = new HashMap<String, String>();
            model.put("title", "error.report");
            model.put("message", "error.report-generation-failed");
            return new ModelAndView("user-message", model);
        } finally {
            try {
                if (jasperStream != null) jasperStream.close();
                if (jrXmlStream != null) jrXmlStream.close();
            } catch (Exception ex) {
            }
        }
        return null;
    }

    /**
	 * Helper close to retrieve the report file (jasper, jrxml)
	 * from upload path or within the web application.
	 * @param reportFile
	 * @param extType
	 * @return
	 */
    private InputStream getReportStream(String reportFile, String extType) {
        InputStream stream = null;
        DynamicReport report = service.findByReportFile(reportFile);
        if (report != null) {
            String uploadPath = report.getReportPath() + "/" + reportFile + "." + extType;
            try {
                stream = new FileInputStream(uploadPath);
            } catch (FileNotFoundException e) {
                _log.error("Report file not found. [" + uploadPath + "]", e);
            }
        } else {
            String webPath = this.reportWebPath + "/" + reportFile + "." + extType;
            stream = this.getServletContext().getResourceAsStream(webPath);
        }
        return stream;
    }

    private Map<String, String[]> transformRequest(HttpServletRequest request) {
        String[] requestKeys = request.getParameterValues("requestParameters");
        Map<String, String[]> parameters = request.getParameterMap();
        Map<String, String[]> newRequest = new HashMap<String, String[]>();
        for (String key : requestKeys) {
            newRequest.put(key, parameters.get(key));
        }
        return newRequest;
    }

    /**
	 * Disables page caching of report.
	 * @param response
	 */
    private void setHeader(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        response.setHeader("encoding", "utf-8");
    }

    public void setReportWebPath(String reportWebPath) {
        this.reportWebPath = reportWebPath;
    }

    public void setService(ReportService reportService) {
        this.service = reportService;
    }

    public void setReportValidators(Map<String, BaseReportValidator> reportValidators) {
        this.reportValidators = reportValidators;
    }
}
