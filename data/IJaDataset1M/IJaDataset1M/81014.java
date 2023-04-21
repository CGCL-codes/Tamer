package org.nex.ts.server.ws.model;

import java.io.*;
import java.util.*;
import org.nex.ts.server.ws.api.IDeliciousListener;
import org.nex.ts.server.tago.api.ITagWorkerListener;
import org.nex.ts.server.tago.api.ITagomizerTag;
import org.nex.ts.server.tago.api.ITagomizerUser;
import org.nex.ts.server.tago.model.TagomizerModel;
import org.nex.ts.server.tago.model.UpdateWorkerThread;
import org.nex.ts.TopicSpacesException;
import org.nex.ts.smp.SubjectMapProvider;
import org.nex.ts.server.common.model.Ticket;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.openrdf.model.Graph;
import org.apache.log4j.Logger;

/**
 * @author Jack Park
 * <p>
 * A Delicious user must be logged in before using these
 * methods.
 * </p>
 * <p>
 * Delicious wants us to call
 * <code>http://del.icio.us/api/posts/update</code> with a
 * response such as
 * <update time="2005-11-29T20:31:52Z" />
 * to see if any data has changed.
 * </p>
 * <p>
 * {@link DeliciousPullParser} is called after a Delicious
 * call to
 * http://del.icio.us/api/posts/all?
 * </p>
 * <p>
 * Calling <code>all</code> gets everything. Might want to call
 * instead: http://del.icio.us/api/posts/dates?
 * and compare that list of dates to dates already in the database.
 * </p>
 */
public class DeliciousPullParser extends Thread implements ITagWorkerListener {

    protected final Logger log = Logger.getLogger(DeliciousPullParser.class);

    private SubjectMapProvider smp;

    private TagomizerModel application;

    private IDeliciousListener exceptionHandler;

    private String parseString;

    private String username;

    private ITagomizerUser user;

    private Ticket credentials;

    private Graph graph;

    private UpdateWorkerThread workerThread;

    public DeliciousPullParser(String xmlString, TagomizerModel app, ITagomizerUser u, IDeliciousListener listener, Ticket credentials, SubjectMapProvider s) throws TopicSpacesException {
        this.parseString = xmlString;
        this.application = app;
        this.exceptionHandler = listener;
        this.user = u;
        this.credentials = credentials;
        this.setPriority(2);
        this.smp = s;
        application.blockTagManager(this);
        this.graph = smp.getMapDatabase().getGraph();
        this.workerThread = new UpdateWorkerThread(application);
        this.start();
    }

    public void tagWorkerDone() throws TopicSpacesException {
        smp.logDebug("DeliciousPullParser.tagWorkerDone");
    }

    public void run() {
        long start = System.currentTimeMillis();
        log.debug("DeliciousPullParser.run- " + start);
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            StringReader sr = new StringReader(parseString);
            BufferedReader in = new BufferedReader(sr);
            xpp.setInput(in);
            String temp = null;
            String text = null;
            String href = null;
            String description = null;
            String tag = null;
            String time = null;
            String extended = null;
            int counter = 0;
            String update = null;
            HashMap attributes = null;
            boolean isList = false;
            int eventType = xpp.getEventType();
            boolean isStop = false;
            while (!(isStop || eventType == XmlPullParser.END_DOCUMENT)) {
                Thread.yield();
                temp = xpp.getName();
                attributes = getAttributes(xpp);
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    System.out.println("Start document");
                } else if (eventType == XmlPullParser.END_DOCUMENT) {
                    log.debug("DeliciousPullParser End document");
                } else if (eventType == XmlPullParser.START_TAG) {
                    System.out.println("Start tag " + temp);
                    if (temp.equalsIgnoreCase("posts")) {
                        username = getProperty(attributes, "user");
                        log.debug("DeliciousPullParser.run username " + username);
                        update = getProperty(attributes, "update");
                    } else if (temp.equalsIgnoreCase("post")) {
                        href = getProperty(attributes, "href");
                        description = getProperty(attributes, "description");
                        tag = getProperty(attributes, "tag");
                        smp.logDebug("DeliciousPullParser.run post 1 " + tag);
                        extended = getProperty(attributes, "extended");
                        time = getProperty(attributes, "time");
                        smp.logDebug("DeliciousPullParser.run post 2 " + time);
                        long x = getDate(time);
                        smp.logDebug("DeliciousPullParser.run post 3 " + x);
                        List nameStrings = new ArrayList();
                        StringTokenizer tok = new StringTokenizer(tag, " ");
                        while (tok.hasMoreTokens()) nameStrings.add(tok.nextToken());
                        smp.logDebug("DeliciousPullParser.run post 4 " + nameStrings);
                        exceptionHandler.say(href);
                        Thread.yield();
                        application.updateBookmark(workerThread, href, nameStrings, ITagomizerTag.STRING_TYPE, description, extended, x, credentials);
                        counter++;
                        if (counter >= 10) {
                            System.gc();
                            counter = 0;
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    System.out.println("End tag " + temp + " // " + text);
                    if (temp.equalsIgnoreCase("posts")) {
                    } else if (temp.equalsIgnoreCase("post")) {
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    text = xpp.getText().trim();
                } else if (eventType == XmlPullParser.CDSECT) {
                    text = xpp.getText().trim();
                }
                eventType = xpp.next();
            }
            application.releaseTagManager();
            smp.getMapDatabase().releaseMergeThread();
        } catch (Exception x) {
            exceptionHandler.handleDeliciousException(new TopicSpacesException(x));
        }
        log.debug("DeliciousPullParser.run+ " + start + " took " + (System.currentTimeMillis() - start));
    }

    /**
	 * 2006-05-18T13:48:07Z
	 * @param time
	 * @return
	 */
    long getDate(String time) {
        long result = 0;
        String w = time;
        String y, m, d;
        int where = w.indexOf('-');
        y = w.substring(0, where);
        w = w.substring(where + 1);
        where = w.indexOf('-');
        m = w.substring(0, where);
        w = w.substring(where + 1);
        where = w.indexOf('T');
        d = w.substring(0, where);
        Calendar c = new GregorianCalendar(Integer.parseInt(y), Integer.parseInt(m), Integer.parseInt(d));
        result = c.getTimeInMillis();
        return result;
    }

    String getProperty(HashMap prop, String key) {
        String result = (String) prop.get(key);
        if (result == null) return "";
        return result;
    }

    HashMap getAttributes(XmlPullParser p) {
        HashMap result = new HashMap();
        int count = p.getAttributeCount();
        if (count > 0) {
            result = new HashMap();
            String name = null;
            for (int i = 0; i < count; i++) {
                name = p.getAttributeName(i);
                result.put(name, p.getAttributeValue(i));
            }
        }
        return result;
    }
}
