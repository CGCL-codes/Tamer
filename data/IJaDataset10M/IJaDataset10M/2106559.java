package org.spnt.servlet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import org.spantus.logger.Logger;
import org.spnt.dto.CorporaEntry;
import org.spnt.servlet.service.SpntServletConfigService;
import org.spnt.servlet.service.SpntEchoRepository;
import org.spnt.servlet.service.impl.SpntStorageServiceFileImpl;
import edu.mit.csail.sls.wami.audio.WamiResampleAudioInputStream;
import edu.mit.csail.sls.wami.util.ContentType;

public class SpntRecordServlet extends HttpServlet {

    private static Logger LOG = Logger.getLogger(SpntRecordServlet.class);

    /**
	 * 
	 */
    private static final long serialVersionUID = -5886959979493020618L;

    private SpntEchoRepository storageService;

    private SpntServletConfigService configService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        LOG.debug("[doGet] request: {0}", getConfigService().reconstructRequestURLandParams(request.getRequestURL().toString(), request.getParameterMap()));
        try {
            out.println("<html><head><title>MyServlet</title></head><body>");
            out.write("<ul>");
            for (CorporaEntry entry : getStorageService().findAll().getCorporaEntry()) {
                out.write("<li>[doGet] entry {0}" + entry.getFileName() + "</li>");
            }
            out.write("</ul>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    /**
	 * 
	 */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("[doPost] request: {0}", getConfigService().reconstructRequestURLandParams(request.getRequestURL().toString(), request.getParameterMap()));
        LOG.debug("[doPost] Handling portal recognize() post on session: " + request.getSession().getId());
        AudioFormat audioFormat = getAudioFormatFromParams(request, "recordAudioFormat", "recordSampleRate", "recordIsLittleEndian");
        LOG.debug("[doPost]  audioFormat={0}", audioFormat);
        AudioInputStream audioIn = new AudioInputStream(new BufferedInputStream(request.getInputStream()), audioFormat, AudioSystem.NOT_SPECIFIED);
        AudioFormat requiredFormat = getRecognizerRequiredAudioFormat();
        if (audioFormat.getEncoding() != requiredFormat.getEncoding() || audioFormat.getSampleRate() != requiredFormat.getSampleRate() || audioFormat.getSampleSizeInBits() != requiredFormat.getSampleSizeInBits() || audioFormat.getChannels() != requiredFormat.getChannels() || audioFormat.getFrameSize() != requiredFormat.getFrameSize() || audioFormat.getFrameRate() != requiredFormat.getFrameRate() || audioFormat.isBigEndian() != requiredFormat.isBigEndian()) {
            LOG.debug("[doPost] Resampling");
            audioIn = new WamiResampleAudioInputStream(getRecognizerRequiredAudioFormat(), audioIn);
        }
        getStorageService().store(audioIn);
    }

    /**
	 * 
	 * @return
	 */
    private AudioFormat getRecognizerRequiredAudioFormat() {
        return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000, 16, 1, 2, 8000, false);
    }

    /**
	 * 
	 * @param request
	 * @param formatParam
	 * @param sampleRateParam
	 * @param isLittleEndianParam
	 * @return
	 */
    private AudioFormat getAudioFormatFromParams(HttpServletRequest request, String formatParam, String sampleRateParam, String isLittleEndianParam) {
        LOG.debug("Record Content-Type {0} ", request.getContentType());
        ContentType contentType = ContentType.parse(request.getContentType());
        String contentMajor = contentType.getMajor();
        String contentMinor = contentType.getMinor();
        if ("AUDIO".equals(contentMajor)) {
            if (contentMinor.equals("L16")) {
                int rate = contentType.getIntParameter("RATE", 8000);
                int channels = contentType.getIntParameter("CHANNELS", 1);
                boolean big = contentType.getBooleanParameter("BIG", true);
                return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, channels, 2, rate, big);
            }
        }
        String audioFormatStr = request.getParameter(formatParam);
        int sampleRate = Integer.parseInt(request.getParameter(sampleRateParam));
        boolean isLittleEndian = Boolean.parseBoolean(request.getParameter(isLittleEndianParam));
        if ("MULAW".equals(audioFormatStr)) {
            return new AudioFormat(AudioFormat.Encoding.ULAW, sampleRate, 8, 1, 2, 8000, !isLittleEndian);
        } else if ("LIN16".equals(audioFormatStr)) {
            return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, 16, 1, 2, sampleRate, !isLittleEndian);
        }
        throw new UnsupportedOperationException("Unsupported audio format: '" + audioFormatStr + "'");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("[service]");
        super.service(req, resp);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    /**
	 * 
	 * @return
	 */
    public SpntServletConfigService getConfigService() {
        if (configService == null) {
            configService = new SpntServletConfigService();
        }
        return configService;
    }

    public SpntEchoRepository getStorageService() {
        if (storageService == null) {
            storageService = new SpntStorageServiceFileImpl();
        }
        return storageService;
    }
}
