package net.sourceforge.cvsgrab.web;

import net.sourceforge.cvsgrab.CVSGrab;
import net.sourceforge.cvsgrab.InvalidVersionException;
import net.sourceforge.cvsgrab.MarkerNotFoundException;
import net.sourceforge.cvsgrab.RemoteFile;
import net.sourceforge.cvsgrab.WebBrowser;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.w3c.dom.Document;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Support for Chora 2.0 interfaces to a cvs repository
 *
 * @author lclaude
 * @cvsgrab.created 30 mars 2004
 */
public class Chora2_0Interface extends ViewCvsInterface {

    private String _browsePath = "cvs.php";

    public Chora2_0Interface(CVSGrab grabber) {
        super(grabber);
        setFilesXpath("//TR[TD/A/IMG/@alt = 'File']");
        setFileNameXpath("TD[1]/A/@href");
        setFileVersionXpath("TD[2]/B/A");
        setDirectoriesXpath("//TR[TD/A/IMG/@alt = 'Directory']");
        setDirectoryXpath("TD[1]/A/@href");
        setCheckoutPath("co.php");
        setWebInterfaceType("cvs.php/");
    }

    protected String getBrowsePath() {
        return _browsePath;
    }

    protected void setBrowsePath(String browsePath) {
        _browsePath = browsePath;
    }

    /** 
     * {@inheritDoc}
     */
    public void detect(Document htmlPage) throws MarkerNotFoundException, InvalidVersionException {
        String rootUrl = WebBrowser.removeFinalSlash(getGrabber().getRootUrl());
        if (!rootUrl.endsWith(getBrowsePath())) {
            throw new MarkerNotFoundException("Root url should end with " + getBrowsePath());
        }
        JXPathContext context = JXPathContext.newContext(htmlPage);
        String type = (String) context.getValue("//IMG[contains(@src,'chora.gif')]");
        if (type == null) {
            throw new MarkerNotFoundException("Expected marker 'chora.gif', found none");
        }
        setType("Chora 2.x");
        if (getGrabber().getVersionTag() != null) {
            throw new InvalidVersionException("Chora 2.0 doesn't support version tags");
        }
    }

    /** 
     * {@inheritDoc}
     */
    public String getDirectoryUrl(String rootUrl, String directoryName) {
        try {
            String url = WebBrowser.forceFinalSlash(rootUrl);
            url += WebBrowser.forceFinalSlash(quote(directoryName));
            url = WebBrowser.addQueryParam(url, getQueryParams());
            return url;
        } catch (URIException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Cannot create URI");
        }
    }

    /** 
     * {@inheritDoc}
     */
    public String[] getDirectories(Document htmlPage) {
        JXPathContext context = JXPathContext.newContext(htmlPage);
        List directories = new ArrayList();
        Iterator i = context.iteratePointers(getDirectoriesXpath());
        while (i.hasNext()) {
            Pointer pointer = (Pointer) i.next();
            JXPathContext nodeContext = context.getRelativeContext(pointer);
            String dir = (String) nodeContext.getValue(getDirectoryXpath());
            dir = WebBrowser.removeFinalSlash(dir);
            dir = dir.substring(dir.lastIndexOf('/') + 1);
            directories.add(dir);
        }
        return (String[]) directories.toArray(new String[directories.size()]);
    }

    /** 
     * {@inheritDoc}
     */
    public RemoteFile[] getFiles(Document htmlPage) {
        JXPathContext context = JXPathContext.newContext(htmlPage);
        List files = new ArrayList();
        Iterator i = context.iteratePointers(getFilesXpath());
        while (i.hasNext()) {
            Pointer pointer = (Pointer) i.next();
            JXPathContext nodeContext = context.getRelativeContext(pointer);
            String fileName = (String) nodeContext.getValue(getFileNameXpath());
            fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
            String version = (String) nodeContext.getValue(getFileVersionXpath());
            RemoteFile file = new RemoteFile(fileName, version);
            files.add(file);
        }
        return (RemoteFile[]) files.toArray(new RemoteFile[files.size()]);
    }

    /** 
     * {@inheritDoc}
     */
    public String getDownloadUrl(RemoteFile file) {
        try {
            String url = WebBrowser.forceFinalSlash(file.getDirectory().getRemoteRepository().getRootUrl());
            if (getBrowsePath().length() > 0) {
                url = url.substring(0, url.length() - getBrowsePath().length() - 1);
            }
            url = WebBrowser.forceFinalSlash(url + getCheckoutPath());
            String dir = file.getDirectory().getDirectoryPath();
            url += WebBrowser.forceFinalSlash(quote(dir));
            url += quote(file.getName());
            url = WebBrowser.addQueryParam(url, "r", file.getVersion());
            url = WebBrowser.addQueryParam(url, "p", "1");
            url = WebBrowser.addQueryParam(url, getQueryParams());
            return url;
        } catch (URIException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Cannot create URI");
        }
    }

    /**
     * Python-style of URIUtil.encodePath
     * 
     * @param original The string to quote
     * @return the quoted string
     */
    protected String quote(String original) throws URIException {
        return URIUtil.encodePath(original, "ISO-8859-1");
    }

    /** 
     * {@inheritDoc}
     */
    protected String getVersionMarker() {
        return null;
    }

    protected void adjustFile(RemoteFile file, JXPathContext nodeContext) {
    }

    public boolean presetMatch(String rootUrl, String packagePath) {
        if (rootUrl.indexOf("cvs.php.net") > 0) {
            setType("Chora 2.0 on php.net");
            _browsePath = "";
            return true;
        }
        return false;
    }

    public Properties guessWebProperties(String url) {
        if (url.startsWith("http://cvs.php.net/")) {
            Properties properties = new Properties();
            properties.put(CVSGrab.ROOT_URL_OPTION, "http://cvs.php.net/");
            properties.put(CVSGrab.PACKAGE_PATH_OPTION, url.substring("http://cvs.php.net/".length()));
            _browsePath = "";
            return properties;
        }
        return super.guessWebProperties(url);
    }
}
