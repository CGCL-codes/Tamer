package org.qfirst.batavia.mime;

import java.io.*;
import org.apache.log4j.*;
import java.util.*;
import org.qfirst.vfs.*;

class MimeGuesser {

    private Logger logger = Logger.getLogger(getClass());

    private Map extensionToMimeType = new Hashtable();

    private List mimeTypeList = new ArrayList();

    private boolean loaded = false;

    private static final MimeGuesser instance = new MimeGuesser();

    private MimeGuesser() {
    }

    public static MimeGuesser getInstance() {
        return instance;
    }

    private void ensureLoaded() {
        if (!loaded) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/resources/mime.types"), "Latin1"));
                while (true) {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    line = line.trim();
                    if (line.startsWith("#") || line.indexOf('\t') == -1) {
                        continue;
                    }
                    String type = line.substring(0, line.indexOf('\t'));
                    String extStr = line.substring(line.lastIndexOf('\t') + 1);
                    String exts[] = extStr.split(" ");
                    for (int i = 0; i < exts.length; i++) {
                        extensionToMimeType.put(exts[i], type);
                    }
                    mimeTypeList.add(type);
                }
                loaded = true;
            } catch (Exception ex) {
                logger.fatal("Could not load mime types", ex);
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException ioe) {
                        logger.debug(ioe, ioe);
                    }
                }
            }
        }
    }

    /**
	* Returns the value of mimeTypeList.
	* @return the mimeTypeList value.
	*/
    public List getMimeTypeList() {
        ensureLoaded();
        return mimeTypeList;
    }

    public String guessMimeTypeOf(AbstractFile file) {
        ensureLoaded();
        String name = file.getName();
        if (name.lastIndexOf('.') != -1) {
            String ext = name.substring(name.lastIndexOf('.') + 1);
            return (String) extensionToMimeType.get(ext);
        }
        return null;
    }
}
