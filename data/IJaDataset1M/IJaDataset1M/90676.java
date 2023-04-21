package net.sourceforge.cvsgrab.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import net.sourceforge.cvsgrab.CVSGrab;
import net.sourceforge.cvsgrab.CvsWebInterface;
import net.sourceforge.cvsgrab.InvalidVersionException;
import net.sourceforge.cvsgrab.MarkerNotFoundException;
import net.sourceforge.cvsgrab.RemoteFile;
import net.sourceforge.cvsgrab.WebBrowser;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.w3c.dom.Document;

/**
 * Support for ViewCvs-like interfaces to a cvs repository
 *
 * @author <a href="mailto:ludovicc@users.sourceforge.net">Ludovic Claude</a>
 * @version $Revision: 1.18 $ $Date: 2006/07/20 02:08:59 $
 * @cvsgrab.created on 11 oct. 2003
 */
public abstract class ViewCvsInterface extends CvsWebInterface {

    private String _type;

    private String _filesXpath = "//TR[TD/A/IMG/@alt = '(file)']";

    private String _fileNameXpath = "TD[1]/A/@name";

    private String _fileVersionXpath = "TD[A/IMG/@alt != '(graph)'][2]/A/B";

    private String _directoriesXpath = "//TR[TD/A/IMG/@alt = '(dir)'][TD/A/@name != 'Attic']";

    private String _directoryXpath = "TD[1]/A/@name";

    private String _checkoutPath = "*checkout*/";

    private String _webInterfaceType = "viewcvs";

    private String _tagParam = "only_with_tag";

    private String _cvsrootParam = "cvsroot";

    /**
     * Constructor for ViewCvsInterface
     *
     */
    public ViewCvsInterface(CVSGrab grabber) {
        super(grabber);
    }

    /**
     * Initialize the web interface
     */
    public void init() throws Exception {
        _type = getId();
    }

    /**
     * {@inheritDoc}
     * @param htmlPage The web page
     * @throws MarkerNotFoundException if the version marker for the web interface was not found
     * @throws InvalidVersionException if the version detected is incompatible with the version supported by this web interface.
     */
    public void detect(Document htmlPage) throws MarkerNotFoundException, InvalidVersionException {
        JXPathContext context = JXPathContext.newContext(htmlPage);
        Iterator viewCvsTexts = context.iterate("//META[@name = 'generator']/@content[starts-with(.,'ViewCVS')] | //A[@href]/text()[starts-with(.,'ViewCVS')]");
        _type = null;
        String viewCvsVersion = null;
        while (viewCvsTexts.hasNext()) {
            viewCvsVersion = (String) viewCvsTexts.next();
            if (viewCvsVersion.startsWith(getVersionMarker())) {
                _type = viewCvsVersion;
                break;
            }
        }
        if (_type == null) {
            throw new MarkerNotFoundException("Expected marker " + getVersionMarker() + ", found " + viewCvsVersion);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getId() {
        String className = getClass().getName();
        className = className.substring(className.lastIndexOf('.') + 1);
        className = className.substring(0, className.indexOf("Interface"));
        return className;
    }

    /**
     * {@inheritDoc}
     */
    public String getType() {
        return _type;
    }

    /**
     * @return the base url to use when trying to auto-detect this type of web interface
     */
    public String getBaseUrl() {
        String url = WebBrowser.forceFinalSlash(getGrabber().getRootUrl());
        url += getGrabber().getPackagePath();
        if (getProjectRoot() != null) {
            url = WebBrowser.addQueryParam(url, _cvsrootParam, getProjectRoot());
        }
        url = WebBrowser.addQueryParam(url, getGrabber().getQueryParams());
        return url;
    }

    /**
     * @param rootUrl
     * @param directoryName
     * @return the url to use to access the contents of the repository
     */
    public String getDirectoryUrl(String rootUrl, String directoryName) {
        try {
            String tag = getVersionTag();
            String url = WebBrowser.forceFinalSlash(rootUrl);
            url += WebBrowser.forceFinalSlash(quote(directoryName));
            if (getProjectRoot() != null) {
                url = WebBrowser.addQueryParam(url, _cvsrootParam, getProjectRoot());
            }
            url = WebBrowser.addQueryParam(url, _tagParam, tag);
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
    public RemoteFile[] getFiles(Document htmlPage) {
        JXPathContext context = JXPathContext.newContext(htmlPage);
        List files = new ArrayList();
        Iterator i = context.iteratePointers(getFilesXpath());
        while (i.hasNext()) {
            Pointer pointer = (Pointer) i.next();
            JXPathContext nodeContext = context.getRelativeContext(pointer);
            String fileName = (String) nodeContext.getValue(getFileNameXpath());
            String version = (String) nodeContext.getValue(getFileVersionXpath());
            RemoteFile file = new RemoteFile(fileName, version);
            adjustFile(file, nodeContext);
            files.add(file);
        }
        return (RemoteFile[]) files.toArray(new RemoteFile[files.size()]);
    }

    /**
     * {@inheritDoc}
     */
    public String[] getDirectories(Document htmlPage) {
        JXPathContext context = JXPathContext.newContext(htmlPage);
        context.registerNamespace("HTML", "http://www.w3.org/1999/xhtml");
        context.registerNamespace("", "http://www.w3.org/1999/xhtml");
        List directories = new ArrayList();
        Iterator i = context.iteratePointers(getDirectoriesXpath());
        while (i.hasNext()) {
            Pointer pointer = (Pointer) i.next();
            JXPathContext nodeContext = context.getRelativeContext(pointer);
            try {
                String dir = (String) nodeContext.getValue(getDirectoryXpath());
                directories.add(dir);
            } catch (RuntimeException e) {
                CVSGrab.getLog().error("Cannot localise directory name in document location " + nodeContext.getPointer("."), e);
            }
        }
        return (String[]) directories.toArray(new String[directories.size()]);
    }

    public String getDownloadUrl(RemoteFile file) {
        try {
            String url = WebBrowser.forceFinalSlash(file.getDirectory().getRemoteRepository().getRootUrl());
            String dir = file.getDirectory().getDirectoryPath();
            url += getCheckoutPath();
            url += WebBrowser.forceFinalSlash(quote(dir));
            if (file.isInAttic()) {
                url += "Attic/";
            }
            url += quote(file.getName());
            if (getProjectRoot() != null) {
                url = WebBrowser.addQueryParam(url, _cvsrootParam, getProjectRoot());
            }
            url = WebBrowser.addQueryParam(url, "rev", file.getVersion());
            url = WebBrowser.addQueryParam(url, getQueryParams());
            return url;
        } catch (URIException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Cannot create URI");
        }
    }

    public Properties guessWebProperties(String url) {
        Properties properties = new Properties();
        int keywordPosition = url.toLowerCase().indexOf(_webInterfaceType);
        if (keywordPosition > 0) {
            int rootUrlPosition = url.indexOf('/', keywordPosition) + 1;
            int cgiFolderPos = url.indexOf("cgi/", rootUrlPosition);
            if (cgiFolderPos > 0) {
                rootUrlPosition = cgiFolderPos + 4;
            }
            int nextSlashPos = url.indexOf('/', rootUrlPosition) + 1;
            int magicScriptPos = url.indexOf(".cgi", rootUrlPosition);
            if (magicScriptPos < 0) {
                magicScriptPos = url.indexOf(".py", rootUrlPosition);
            }
            if (magicScriptPos > 0 && magicScriptPos < nextSlashPos) {
                rootUrlPosition = nextSlashPos;
            }
            String guessedRootUrl = url.substring(0, rootUrlPosition);
            String guessedPackagePath = url.substring(rootUrlPosition);
            String versionTag = null;
            String cvsroot = null;
            String query = null;
            int queryPos = guessedPackagePath.indexOf('?');
            if (queryPos >= 0) {
                query = guessedPackagePath.substring(queryPos + 1);
                guessedPackagePath = guessedPackagePath.substring(0, queryPos);
                Properties queryItems = WebBrowser.getQueryParams(query);
                versionTag = (String) queryItems.remove(_tagParam);
                cvsroot = (String) queryItems.remove(_cvsrootParam);
                query = WebBrowser.toQueryParams(queryItems);
            }
            properties.put(CVSGrab.ROOT_URL_OPTION, guessedRootUrl);
            properties.put(CVSGrab.PACKAGE_PATH_OPTION, guessedPackagePath);
            if (versionTag != null && versionTag.trim().length() > 0) {
                properties.put(CVSGrab.TAG_OPTION, versionTag);
            }
            if (cvsroot != null && cvsroot.trim().length() > 0) {
                properties.put(CVSGrab.PROJECT_ROOT_OPTION, cvsroot);
            }
            if (query != null && query.trim().length() > 0) {
                properties.put(CVSGrab.QUERY_PARAMS_OPTION, query);
            }
        }
        return properties;
    }

    public String getFilesXpath() {
        return _filesXpath;
    }

    public String getFileNameXpath() {
        return _fileNameXpath;
    }

    public String getFileVersionXpath() {
        return _fileVersionXpath;
    }

    public String getDirectoriesXpath() {
        return _directoriesXpath;
    }

    public String getDirectoryXpath() {
        return _directoryXpath;
    }

    protected String getCheckoutPath() {
        return _checkoutPath;
    }

    protected void setCheckoutPath(String checkoutPath) {
        _checkoutPath = checkoutPath;
    }

    public void setDirectoryXpath(String directoryXpath) {
        _directoryXpath = directoryXpath;
    }

    public void setDirectoriesXpath(String directoriesXpath) {
        _directoriesXpath = directoriesXpath;
    }

    public void setFileVersionXpath(String fileVersionXpath) {
        _fileVersionXpath = fileVersionXpath;
    }

    public void setFileNameXpath(String fileNameXpath) {
        _fileNameXpath = fileNameXpath;
    }

    public void setFilesXpath(String filesXpath) {
        _filesXpath = filesXpath;
    }

    public String getTagParam() {
        return _tagParam;
    }

    public void setTagParam(String param) {
        _tagParam = param;
    }

    public String getWebInterfaceType() {
        return _webInterfaceType;
    }

    protected void setWebInterfaceType(String webInterfaceType) {
        this._webInterfaceType = webInterfaceType;
    }

    public String getCvsrootParam() {
        return _cvsrootParam;
    }

    public void setCvsrootParam(String cvsrootParam) {
        _cvsrootParam = cvsrootParam;
    }

    /**
     * @param type
     */
    protected void setType(String type) {
        _type = type;
    }

    protected abstract String getVersionMarker();

    protected void adjustFile(RemoteFile file, JXPathContext nodeContext) {
        String fileName = file.getName();
        if (fileName.startsWith("Attic/")) {
            file.setName(fileName.substring(6));
            file.setInAttic(true);
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

    protected String getProjectRoot() {
        return getGrabber().getProjectRoot();
    }
}
