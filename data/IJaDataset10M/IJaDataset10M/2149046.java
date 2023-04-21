package org.nightlabs.editor2d.iofilter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class ManifestReader {

    /**
	 * LOG4J logger used by this class
	 */
    private static final Logger logger = Logger.getLogger(ManifestReader.class.getName());

    protected XmlPullParser xpp;

    protected InputStreamReader reader;

    protected boolean debug = false;

    protected String charset;

    public ManifestReader(InputStream in, String charset) {
        super();
        try {
            this.charset = charset;
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            xpp = factory.newPullParser();
            reader = new InputStreamReader(in, charset);
            xpp.setInput(reader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static final String APPLICATION = ManifestWriter.APPLICATION;

    protected static final String TEXT = ManifestWriter.DEFAULT_CONTENT_MEDIA_TYPE;

    protected static final String IMAGE = ManifestWriter.DEFAULT_IMAGE_TYPE;

    public void read() {
        nameSpace = xpp.getNamespace();
        if (debug) logger.debug("nameSpace = " + nameSpace);
        int attributeCount = -1;
        int eventType;
        try {
            eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                } else if (eventType == XmlPullParser.END_DOCUMENT) {
                } else if (eventType == XmlPullParser.START_TAG) {
                    attributeCount = xpp.getAttributeCount();
                    for (int i = 0; i < attributeCount; i++) {
                        String attrName = xpp.getAttributeName(i);
                        String value = xpp.getAttributeValue(i);
                        if (!foundContentType) findContentType(value);
                        if (attrName.equals(ManifestWriter.VERSION)) {
                            version = value;
                            logger.debug("version = " + version);
                        }
                        checkImageEntry(value, i);
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String nameSpace;

    /**
	 * 
	 * @return the NameSpace of the Manifest
	 * if no NameSpace is available it returns null
	 */
    public String getNameSpace() {
        return nameSpace;
    }

    protected String contentType;

    /**
	 * 
	 * @return the contentType of the Manifest
	 */
    public String getContentType() {
        return contentType;
    }

    protected Map<String, String[]> imageInfo = new HashMap<String, String[]>();

    /**
	 * @return a List which contains a String[] for each ImageEntry
	 * String[0] = imageType (e.g. jpg)
	 * String[1] = relative Image Path (e.g. images/myImage.jpg.123456-789)
	 */
    public Map<String, String[]> getEntryName2ImageInfo() {
        return imageInfo;
    }

    protected boolean foundContentType = false;

    protected void findContentType(String s) {
        StringBuffer sb = new StringBuffer(s);
        int index = sb.indexOf(APPLICATION);
        ;
        if (index != -1) {
            contentType = sb.substring(index);
            foundContentType = true;
            if (debug) logger.debug("contentType = " + contentType);
        }
    }

    private String version;

    public String getVersion() {
        return version;
    }

    protected void checkImageEntry(String attrName, int attributeIndex) {
        if (attrName.indexOf(IMAGE) != -1) {
            String[] imageInfo = new String[3];
            String imageType = removeDirectory(attrName);
            String imagePath = xpp.getAttributeValue(attributeIndex + 1);
            String imageKey = removeDirectory(imagePath);
            String imageName = "";
            imageInfo[0] = imageType;
            imageInfo[1] = imageName;
            imageInfo[2] = imageKey;
            getEntryName2ImageInfo().put(imagePath, imageInfo);
            if (debug) {
                logger.debug("Image " + getEntryName2ImageInfo().size());
                logger.debug("imageType = " + imageType);
                logger.debug("imageName = " + imageName);
                logger.debug("imageKey = " + imageKey);
            }
        }
    }

    protected String removeDirectory(String s) {
        int index = s.lastIndexOf("/");
        if (index != -1) {
            return s.substring(index + 1);
        }
        return null;
    }
}
