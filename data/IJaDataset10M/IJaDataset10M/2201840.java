package com.astromine.mp3;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.astromine.base.Log;
import com.astromine.base.StringHelper;

/**
 * Play List Parser
 * @author Stephen Fox
 *
 */
public class PLSTranslator extends AbstractTranslator {

    public PLSTranslator() {
        super();
    }

    public PLSTranslator(byte[] data) {
        super(data);
    }

    public PLSTranslator(InputStream stream) {
        super(stream);
    }

    public PLSTranslator(String data) {
        super(data);
    }

    public PLSTranslator(URL url) {
        super(url);
    }

    @Override
    public List<String> parseStreams() {
        List<String> list = new ArrayList<String>();
        boolean isValid = false;
        String[] lines = StringHelper.parseDelimited(getContent(), "\r\n");
        for (int i = 0; (lines != null && i < lines.length); i++) {
            String line = lines[i];
            line = line.trim();
            if (line != null && line.contains("=") && line.toLowerCase().contains("file")) {
                int start = line.indexOf("=") + 1;
                String value = line.substring(start).trim();
                try {
                    URI uri = new URI(value);
                    URL url = new URL(value);
                    if (isExisting(list, value)) {
                        ;
                    } else if (uri.getHost() != null) {
                        list.add(value);
                        isValid = true;
                    } else if (url.getProtocol() != null) {
                        list.add(value);
                        isValid = true;
                    }
                } catch (URISyntaxException e1) {
                    Log.writeToStdout(Log.WARNING, "PLSTranslator", "getChannels", "Invalid stream URI " + value);
                } catch (MalformedURLException e) {
                    Log.writeToStdout(Log.WARNING, "PLSTranslator", "getChannels", "Malformed stream URL " + value);
                }
            } else if (line.startsWith("[playlist]")) {
                isValid = true;
            }
        }
        if (!isValid) {
            list = null;
            Log.writeToStdout(Log.AUDIT, "PLSTranslator", "getChannels", "Invalid playlist");
        }
        return list;
    }
}
