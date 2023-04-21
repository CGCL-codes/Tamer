package org.nex.ts.smp;

import java.io.*;
import java.util.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.nex.ts.TopicSpacesException;
import org.nex.ts.smp.api.ISubjectProxy;
import org.nex.ts.smp.api.ISubjectMap;

/**
 * @author park
 *
 */
public class ImportPullParser extends Thread {

    private BufferedInputStream bis;

    private HashMap attributes;

    private SubjectMapProvider smp;

    private ISubjectProxy theProxy;

    private ISubjectMap workingMap;

    /**
	 * 
	 */
    public ImportPullParser(SubjectMapProvider s) {
        smp = s;
    }

    public void importXML(String filePath) throws TopicSpacesException {
        File inFile = new File(filePath);
        try {
            FileInputStream fis = new FileInputStream(inFile);
            bis = new BufferedInputStream(fis);
        } catch (Exception e) {
            throw new TopicSpacesException(e);
        }
        this.start();
    }

    public void run() {
        try {
            XmlPullParserFactory ppfactory = XmlPullParserFactory.newInstance();
            ppfactory.setNamespaceAware(false);
            XmlPullParser xpp = ppfactory.newPullParser();
            BufferedReader in = new BufferedReader(new InputStreamReader(bis));
            xpp.setInput(in);
            attributes = new HashMap();
            String temp = null;
            String text = null;
            String key = null;
            String proxyLocator = null;
            String ownerProxyLocator = null;
            String longDate = null;
            String creatorLocator = null, creatorComment = null, creatorId = null;
            boolean isSet = false;
            int eventType = xpp.getEventType();
            boolean isStop = false;
            while (!(isStop || eventType == XmlPullParser.END_DOCUMENT)) {
                temp = xpp.getName();
                getAttributes(xpp);
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    smp.logDebug("TopicSpacesMapPullParser Start document");
                } else if (eventType == XmlPullParser.END_DOCUMENT) {
                    smp.logDebug("TopicSpacesMapPullParser End document");
                } else if (eventType == XmlPullParser.START_TAG) {
                    smp.logDebug("TopicSpacesMapPullParser Start tag " + temp);
                } else if (eventType == XmlPullParser.END_TAG) {
                    smp.logDebug("TopicSpacesMapPullParser End tag " + temp + " // " + text + " | " + theProxy);
                } else if (eventType == XmlPullParser.TEXT) {
                    text = xpp.getText().trim();
                } else if (eventType == XmlPullParser.CDSECT) {
                    text = xpp.getText().trim();
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
        }
    }

    void getAttributes(XmlPullParser p) {
        attributes.clear();
        int count = p.getAttributeCount();
        if (count > 0) {
            String name = null;
            for (int i = 0; i < count; i++) {
                name = p.getAttributeName(i);
                attributes.put(name, p.getAttributeValue(i));
            }
        }
    }
}
