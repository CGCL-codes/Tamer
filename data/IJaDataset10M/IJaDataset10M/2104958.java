package ru.ksu.niimm.cll.mocassin.frontend.server;

import static java.lang.String.format;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import ru.ksu.niimm.cll.mocassin.util.StringUtil;
import ru.ksu.niimm.cll.mocassin.util.inject.log.InjectLogger;
import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class PdfDownloadServlet extends HttpServlet {

    private static final String ARXIVID_ENTRY = "/arxivid/";

    private static int ARXIVID_ENTRY_LENGTH = ARXIVID_ENTRY.length();

    @InjectLogger
    private Logger logger;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestURI = req.getRequestURI();
        logger.info("The requested URI: {}", requestURI);
        String parameter = requestURI.substring(requestURI.lastIndexOf(ARXIVID_ENTRY) + ARXIVID_ENTRY_LENGTH);
        int signIndex = parameter.indexOf(StringUtil.ARXIVID_SEGMENTID_DELIMITER);
        String arxivId = signIndex != -1 ? parameter.substring(0, signIndex) : parameter;
        String segmentId = signIndex != -1 ? parameter.substring(signIndex + 1) : null;
        if (arxivId == null) {
            logger.error("The request with an empty arxiv id parameter");
            return;
        }
        String filePath = segmentId == null ? format("/opt/mocassin/aux-pdf/%s" + StringUtil.arxivid2filename(arxivId, "pdf")) : "/opt/mocassin/pdf/" + StringUtil.segmentid2filename(arxivId, Integer.parseInt(segmentId), "pdf");
        if (!new File(filePath).exists()) {
            filePath = format("/opt/mocassin/aux-pdf/%s", StringUtil.arxivid2filename(arxivId, "pdf"));
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            IOUtils.copy(fileInputStream, byteArrayOutputStream);
            resp.setContentType("application/pdf");
            resp.setHeader("Content-disposition", String.format("attachment; filename=%s", StringUtil.arxivid2filename(arxivId, "pdf")));
            ServletOutputStream outputStream = resp.getOutputStream();
            outputStream.write(byteArrayOutputStream.toByteArray());
            outputStream.close();
        } catch (FileNotFoundException e) {
            logger.error("Error while downloading: PDF file= '{}' not found", filePath);
        } catch (IOException e) {
            logger.error("Error while downloading the PDF file", e);
        }
    }
}
