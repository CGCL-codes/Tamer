package ru.adv.http;

import ru.adv.util.Stream;
import ru.adv.util.Strings;
import ru.adv.logger.TLogger;
import java.io.*;
import java.util.StringTokenizer;

/**
 * Header of part "multipart/form-data".</br>
 * @see Multipart
 * @version $Revision: 1.7 $
 */
public class MultipartHeader {

    static final byte[] END_HEADER = { '\r', '\n', '\r', '\n' };

    static final byte[] START_HEADER = "content-".getBytes();

    static final String HEADER_DELIM = "\r\n";

    static final int MAX_LENGTH_HEADER = 1024;

    private String disposition;

    private String content_type;

    private int content_length = -1;

    private String name;

    private String filename;

    private String _enc;

    private TLogger logger = new TLogger(MultipartHeader.class);

    public MultipartHeader(String enc) {
        _enc = enc;
    }

    private synchronized void reset() {
        disposition = null;
        content_type = null;
        name = null;
        filename = null;
    }

    /**
	 * get disposition value, must be "form-data"
	 */
    public String getDisposition() {
        return disposition;
    }

    /**
	 * get part name
	 */
    public String getName() {
        return name;
    }

    /**
	 * get content type.
	 * <br/>Может быть выставлен браузером для указания типа upload файла,
	 */
    public String getContentType() {
        return content_type;
    }

    public int getContentLength() {
        return content_length;
    }

    /**
	 * is it upload file ?
	 */
    public boolean isFile() {
        return filename != null;
    }

    /**
	 * get upload file name
	 */
    public String getFileName() {
        return filename;
    }

    /**
	 * Read MultipartHeader header from stream.
	 * There are multitimes useable for same instance.
	 * Читает из потока строку до символов "\r\n\r\n", затем разбирает строку
	 * как это описано в http://www.ietf.org/rfc/rfc1867.txt
	 * @throws BadQueryException When data in stream not present right MultipartHeader
	 * @throws java.io.IOException
	 */
    public synchronized void parse(BufferedInputStream in) throws BadQueryException, java.io.IOException {
        this.reset();
        if (!Stream.isNext(in, START_HEADER, true)) {
            throw new BadQueryException("Multipart header has wrong start char sequence");
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream(MAX_LENGTH_HEADER);
        Stream.readTo(in, out, END_HEADER);
        String header = new String(out.toByteArray(), _enc);
        StringTokenizer lines = new StringTokenizer(header, HEADER_DELIM);
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken();
            int index = line.indexOf(':');
            String linekey = line.substring(0, index).trim();
            String linevalue = line.substring(index + 1).trim();
            if (linekey.equalsIgnoreCase("CONTENT-DISPOSITION")) {
                Disposition d = parseContentDisposition(linevalue, _enc);
                disposition = d.getDisposition();
                name = d.getName();
                filename = d.getFilename();
            } else if (linekey.equalsIgnoreCase("CONTENT-TYPE")) {
                logger.debug("linevalue=" + linevalue);
                content_type = linevalue;
            } else if (linekey.equalsIgnoreCase("CONTENT-LENGTH")) {
                logger.debug("linevalue=" + linevalue);
                try {
                    content_length = Integer.parseInt(linevalue.trim());
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    private static final String FIRST_DELIM = "; name=\"";

    private static final String SECOND_DELIM = "\"";

    private static final String THIRD_DELIM = "; filename=\"";

    public static Disposition parseContentDisposition(String linevalue, String encoding) throws BadQueryException {
        Disposition result = new Disposition();
        try {
            String s = linevalue;
            int startpos = 0;
            int pos = s.indexOf(FIRST_DELIM);
            if (pos >= 0 && pos < s.length() - FIRST_DELIM.length()) {
                result.setDisposition(s.substring(startpos, pos));
                startpos = pos + FIRST_DELIM.length() - 1;
                pos = s.indexOf(SECOND_DELIM, startpos);
                if (pos >= 0 && pos < s.length() - SECOND_DELIM.length()) {
                    startpos = pos + SECOND_DELIM.length();
                    pos = s.indexOf(SECOND_DELIM, startpos);
                    if (pos >= 0) {
                        result.setName(s.substring(startpos, pos));
                        startpos = pos + SECOND_DELIM.length();
                        pos = s.indexOf(THIRD_DELIM, startpos);
                        if (pos > 0) {
                            startpos = pos + THIRD_DELIM.length();
                            pos = s.length() - 1;
                            result.setFilename(Strings.convertEntities(s.substring(startpos, pos), encoding));
                        }
                    }
                }
            }
        } catch (Exception e) {
            TLogger.logStackTrace(MultipartHeader.class, "", e);
            throw new BadQueryException("Invalid Content-Disposition header: " + linevalue + " Exception: " + e);
        }
        if (result.getDisposition() == null || result.getName() == null) {
            throw new BadQueryException("Invalid Content-Disposition header: " + linevalue);
        }
        return result;
    }
}
