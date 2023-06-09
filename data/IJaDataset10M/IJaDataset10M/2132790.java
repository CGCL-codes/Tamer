package org.efs.openreports.dispatcher;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.efs.openreports.ORStatics;
import org.efs.openreports.engine.output.ReportEngineOutput;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.DirectoryProvider;
import org.efs.openreports.util.ORUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Basic servlet for file delivery. Parses image or file name from RequestURI
 * and tries to load from the image, image temp, or report generation directory. 
 * 
 * @author Erik Swenson
 */
public class FileDispatcher extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static Logger log = Logger.getLogger(FileDispatcher.class.getName());

    private DirectoryProvider directoryProvider;

    private String imageDirectory;

    private String imageTempDirectory;

    private String reportGenerationDirectory;

    public void init(ServletConfig servletConfig) throws ServletException {
        ApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletConfig.getServletContext());
        directoryProvider = (DirectoryProvider) appContext.getBean("directoryProvider", DirectoryProvider.class);
        imageDirectory = directoryProvider.getReportImageDirectory();
        imageTempDirectory = directoryProvider.getTempDirectory();
        reportGenerationDirectory = directoryProvider.getReportGenerationDirectory();
        super.init(servletConfig);
        log.info("Started...");
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = "";
        try {
            fileName = StringUtils.substringAfterLast(request.getRequestURI(), "/");
            File file = new File(imageDirectory + fileName);
            if (!file.exists()) {
                file = new File(imageTempDirectory + fileName);
            }
            if (!file.exists()) {
                fileName = request.getParameter("fileName");
                ReportUser user = (ReportUser) request.getSession().getAttribute(ORStatics.REPORT_USER);
                if (user == null || fileName.indexOf(user.getName()) < 0) {
                    String message = "Not Authorized...";
                    response.getOutputStream().write(message.getBytes());
                    return;
                }
                file = new File(reportGenerationDirectory + fileName);
            }
            String contentType = ORUtil.getContentType(fileName);
            response.setContentType(contentType);
            if (contentType != ReportEngineOutput.CONTENT_TYPE_HTML) {
                response.setHeader("Content-disposition", "inline; filename=" + StringUtils.deleteWhitespace(fileName));
            }
            byte[] content = FileUtils.readFileToByteArray(file);
            response.setContentLength(content.length);
            ServletOutputStream ouputStream = response.getOutputStream();
            ouputStream.write(content, 0, content.length);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            log.warn(e);
            String message = "Error Loading File...";
            response.getOutputStream().write(message.getBytes());
        }
    }

    public void setDirectoryProvider(DirectoryProvider directoryProvider) {
        this.directoryProvider = directoryProvider;
    }
}
