package be.ac.fundp.infonet.econf.producer;

import java.io.*;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import javax.xml.transform.TransformerException;
import be.ac.fundp.infonet.econf.util.*;
import be.ac.fundp.infonet.econf.Context;
import be.ac.fundp.infonet.econf.ConfigurationException;
import be.ac.fundp.infonet.econf.audio.AudioSession;
import be.ac.fundp.infonet.econf.audio.AudioManager;

/**
 * This class parses the content of the XML file containing the production config file.
 * @author St�phane Nicoll
 * @version 0.2
 */
public class ConfigParser {

    /**
     * Logging object.
     */
    private static Logger logger = Logger.getLogger(ConfigParser.class.getName());

    private static final String XPATH_ROOT = "/sessions";

    private static final String XPATH_META = XPATH_ROOT + "/meta";

    private static final String XPATH_SESSION = XPATH_ROOT + "/session";

    private static final String SESSION_AUDIO = "audio";

    private static final String SESSION_ARCHIVE = "archive";

    private static final String SESSION_INTEGRATOR = "integrator";

    private static ConfigParser instance = new ConfigParser();

    /**
     * Returns the <code>ConfigParser</code> singleton.
     */
    public static ConfigParser getInstance() {
        return instance;
    }

    private ArrayList sessions;

    private ConfigParser() {
    }

    /**
     * Parse the sessions to produce.
     * @exception ConfigurationException    if the config is invalid
     * @exception IllegalStateException     if the state of an item is invalid
     */
    public void parseSessions() throws ConfigurationException, IllegalStateException {
        sessions = new ArrayList();
        XMLResource cfg = Context.getInstance().getSessions();
        setMetaInformation(cfg);
        List sess = null;
        try {
            sess = cfg.getElements(XPATH_SESSION);
        } catch (TransformerException te) {
            logger.error("Failed to get the sessions", te);
            throw new ConfigurationException("Couldn't parse the session " + te.getMessage());
        }
        Iterator i = sess.iterator();
        while (i.hasNext()) {
            Element session = (Element) i.next();
            if ("offline".equals(session.getAttribute("type"))) {
                sessions.add(parseOfflineSession(session));
            } else if ("streaming".equals(session.getAttribute("type"))) {
                sessions.add(parseStreamingSession(session));
            } else throw new IllegalStateException("session type [" + session.getAttribute("type") + "] is not supported");
        }
    }

    /**
     * Returns an <code>ArrayList</code> of <code>SessionConfig</code>.
     * @exception IllegalStateException   if the sessions have not been parsed yet
     */
    public ArrayList getSessions() throws IllegalStateException {
        if (sessions == null) throw new IllegalStateException("parse the sessions before calling getSessions()"); else return sessions;
    }

    /**
     * Parse an offline session.
     * @param session    the root <code>Element</code> containing the session
     * @exception ConfigurationException    if the config is invalid
     * @exception IllegalStateException     if the state of an item is invalid
     */
    private SessionConfig parseOfflineSession(Element session) throws ConfigurationException, IllegalStateException {
        String sessionName = session.getAttribute("name");
        if (sessionName == null) throw new ConfigurationException("Please set a session name");
        Element audio = getChild(session, "audio");
        AudioSession as = getAudioSession(audio);
        Element integrator = getChild(session, "integrator");
        String integratorId = integrator.getAttribute("id");
        if (integratorId == null) throw new ConfigurationException("Please set an integrator id for session [" + sessionName + "]");
        Element archive = getChild(session, "archive");
        String archiveType = archive.getAttribute("type");
        if ("ftp".equals(archiveType)) {
            String ftpURL = archive.getAttribute("url");
            if (ftpURL == null) throw new ConfigurationException("Please set an URL for FTP session [" + sessionName + "]");
            URL url = null;
            try {
                url = new URL(ftpURL);
            } catch (MalformedURLException me) {
                throw new ConfigurationException("FTP URL for session [" + sessionName + "] is invalid " + me.getMessage());
            }
            String login = archive.getAttribute("login");
            if (login == null) throw new ConfigurationException("Please set the login for FTP session [" + sessionName + "]");
            String password = archive.getAttribute("password");
            if (password == null) throw new ConfigurationException("Please set the password for FTP session [" + sessionName + "]");
            FTPSessionConfig fsc = new FTPSessionConfig(sessionName, as, RawProducer.OFFLINE, integratorId, url, login, password);
            return fsc;
        } else if ("local".equals(archiveType)) {
            String path = archive.getAttribute("path");
            if (path == null) throw new ConfigurationException("Please set a directory path for session [" + sessionName + "]");
            LocalSessionConfig lsc = new LocalSessionConfig(sessionName, as, RawProducer.OFFLINE, integratorId, path);
            return lsc;
        } else throw new ConfigurationException("Archive type [" + archiveType + "] is not supported");
    }

    /**
     * Parse a streaming session.
     * @param session    the root <code>Element</code> containing the session
     * @exception IllegalStateException     <strong>always</strong> thrown since this feature is not supported
     */
    private SessionConfig parseStreamingSession(Element session) throws IllegalStateException {
        throw new IllegalStateException("Streaming sessions are not currently supported");
    }

    /**
     * Gets the audio session represented by the specified element.
     */
    private AudioSession getAudioSession(Element audio) throws ConfigurationException, IllegalStateException {
        String format = audio.getAttribute("format");
        if (format == null) throw new ConfigurationException("audio format not set");
        String framerate = audio.getAttribute("framerate");
        if (framerate == null) throw new ConfigurationException("audio frame rate not set");
        String sampleSize = audio.getAttribute("samplesize");
        if (sampleSize == null) throw new ConfigurationException("audio sample size not set");
        String channel = audio.getAttribute("channel");
        if (channel == null) throw new ConfigurationException("audio channel not set");
        int formatType = AudioManager.getFormat(format);
        if (formatType == -1) throw new ConfigurationException("The specified audio format [" + format + "] is invalid");
        AudioSession as = new AudioSession(formatType, framerate, sampleSize, channel);
        logger.info("Created a new audio session: " + format + " - " + framerate + " " + sampleSize + " " + channel);
        return as;
    }

    /**
     * Returns the child element with the specified name
     * @param root       the root element (father)
     * @param childName  the name of the child element to get
     * @return a child element of <code>root</code> which has the name <code>childName</code>
     * @exception ConfigurationException  if the child is not found
     */
    private Element getChild(Element root, String childName) throws ConfigurationException {
        NodeList nl = root.getElementsByTagName(childName);
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) return (Element) n;
        }
        throw new ConfigurationException("Child [" + childName + "] of element [" + root.getTagName() + "] not found");
    }

    /**
     * Sets the META information element.
     * @param cfg   the XML file containing the sessions information.
     * @exception ConfigurationException   if config is invalid?
     */
    private void setMetaInformation(XMLResource cfg) throws ConfigurationException {
        Context ctx = Context.getInstance();
        Element meta = null;
        try {
            meta = cfg.getElement(XPATH_META);
        } catch (TransformerException te) {
            throw new ConfigurationException("Coudln't get META information with XPATH [" + XPATH_META + "]");
        }
        String author = meta.getAttribute("author");
        if (author == null) ctx.setProperty(SMILSynchronizer.META_AUTHOR, ""); else ctx.setProperty(SMILSynchronizer.META_AUTHOR, author);
        String copyright = meta.getAttribute("copyright");
        if (copyright == null) ctx.setProperty(SMILSynchronizer.META_COPYRIGHT, ""); else ctx.setProperty(SMILSynchronizer.META_COPYRIGHT, copyright);
        String title = meta.getAttribute("title");
        if (title == null) ctx.setProperty(SMILSynchronizer.META_TITLE, ""); else ctx.setProperty(SMILSynchronizer.META_TITLE, title);
    }
}
