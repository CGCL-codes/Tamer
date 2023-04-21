package com.generalynx.ecos.utils;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;

public class StreamUtils {

    private static final Log logger = LogFactory.getLog(StreamUtils.class);

    public static void closeInputStream(InputStream stream) throws IOException {
        if (stream != null) {
            logger.debug("Closing input stream: " + stream);
            stream.close();
        }
    }

    public static void closeOutputStream(OutputStream stream) throws IOException {
        if (stream != null) {
            logger.debug("Closing output stream: " + stream);
            stream.close();
        }
    }

    public static void closeReader(Reader reader) throws IOException {
        if (reader != null) {
            logger.debug("Closing reader: " + reader);
            reader.close();
        }
    }
}
