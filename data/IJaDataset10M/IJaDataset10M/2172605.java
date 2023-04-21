package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.AnalysisFileDescriptor;
import hci.gnomex.utility.HibernateGuestSession;
import hci.gnomex.utility.PropertyDictionaryHelper;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class ShowAnalysisDownloadForm extends GNomExCommand implements Serializable {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ShowAnalysisDownloadForm.class);

    public String SUCCESS_JSP = "/getHTML.jsp";

    private Integer idAnalysis;

    private String serverName;

    private String baseURL;

    private boolean createdSecurityAdvisor = false;

    private SecurityAdvisor secAdvisor = null;

    public void validate() {
    }

    public void loadCommand(HttpServletRequest request, HttpSession session) {
        if (request.getParameter("idAnalysis") != null) {
            idAnalysis = new Integer(request.getParameter("idAnalysis"));
        } else {
            this.addInvalidField("idAnalysis", "idAnalysis is required");
        }
        serverName = request.getServerName();
        baseURL = (request.isSecure() ? "https://" : "http://") + serverName + request.getContextPath();
    }

    public Command execute() throws RollBackCommandException {
        Session sess = null;
        try {
            sess = HibernateGuestSession.currentGuestSession(getUsername());
            secAdvisor = this.getSecAdvisor();
            if (secAdvisor == null) {
                secAdvisor = SecurityAdvisor.create(sess, this.getUsername());
                createdSecurityAdvisor = true;
            }
            Analysis analysis = (Analysis) sess.get(Analysis.class, idAnalysis);
            if (analysis == null) {
                this.addInvalidField("no analysis", "Analysis not found");
            }
            if (this.isValid()) {
                if (secAdvisor.canRead(analysis)) {
                    String baseDir = PropertyDictionaryHelper.getInstance(sess).getAnalysisReadDirectory(serverName);
                    Document doc = formatDownloadHTML(analysis, secAdvisor, baseDir, baseURL);
                    XMLOutputter out = new org.jdom.output.XMLOutputter();
                    out.setOmitEncoding(true);
                    this.xmlResult = out.outputString(doc);
                    this.xmlResult = this.xmlResult.replaceAll("&amp;", "&");
                    this.xmlResult = this.xmlResult.replaceAll("�", "&micro");
                } else {
                    this.addInvalidField("Insufficient permissions", "Insufficient permission to show analysis download form.");
                }
            }
            if (isValid()) {
                setResponsePage(this.SUCCESS_JSP);
            } else {
                setResponsePage(this.ERROR_JSP);
            }
        } catch (UnknownPermissionException e) {
            log.error("An exception has occurred in ShowAnalysisDownloadForm ", e);
            e.printStackTrace();
            throw new RollBackCommandException(e.getMessage());
        } catch (NamingException e) {
            log.error("An exception has occurred in ShowAnalysisDownloadForm ", e);
            e.printStackTrace();
            throw new RollBackCommandException(e.getMessage());
        } catch (SQLException e) {
            log.error("An exception has occurred in ShowAnalysisDownloadForm ", e);
            e.printStackTrace();
            throw new RollBackCommandException(e.getMessage());
        } catch (Exception e) {
            log.error("An exception has occurred in ShowAnalysisDownloadForm ", e);
            e.printStackTrace();
            throw new RollBackCommandException(e.getMessage());
        } finally {
            try {
                if (sess != null) {
                    HibernateGuestSession.closeGuestSession();
                }
            } catch (Exception e) {
            }
        }
        return this;
    }

    /***
   * Format an HTML page showing download links for each of the files of this analysis
   * 
   */
    public static Document formatDownloadHTML(Analysis analysis, SecurityAdvisor secAdvisor, String baseDir, String baseURL) throws UnknownPermissionException {
        Element root = new Element("HTML");
        Document doc = new Document(root);
        Element head = new Element("HEAD");
        root.addContent(head);
        Element link = new Element("link");
        link.setAttribute("rel", "stylesheet");
        link.setAttribute("type", "text/css");
        link.setAttribute("href", Constants.REQUEST_FORM_CSS);
        head.addContent(link);
        Element title = new Element("TITLE");
        title.addContent("Download Files for Analysis - " + analysis.getNumber());
        head.addContent(title);
        Element body = new Element("BODY");
        root.addContent(body);
        Element outerDiv = new Element("DIV");
        outerDiv.setAttribute("id", "container");
        body.addContent(outerDiv);
        Element maindiv = new Element("DIV");
        maindiv.setAttribute("id", "containerForm");
        outerDiv.addContent(maindiv);
        Element img = new Element("img");
        img.setAttribute("src", "images/navbar.png");
        maindiv.addContent(img);
        Element hintBox = new Element("h3");
        hintBox.setAttribute("class", "downloadHint");
        hintBox.addContent("Note to Internet Explorer users: your browser is unable to download files over 4 gigabytes (IE 6 limit is 2 gigabytes). To download large files, switch to another browser like Firefox or Opera.");
        maindiv.addContent(hintBox);
        if (!secAdvisor.canRead(analysis)) {
            throw new UnknownPermissionException("Insufficient permissions to show download form for analysis " + analysis.getNumber());
        }
        Element h = new Element("h1");
        h.setAttribute("class", "downloadHeader");
        h.addContent("Download Analysis Files for " + analysis.getNumber());
        maindiv.addContent(h);
        Map analysisMap = new TreeMap();
        Map directoryMap = new TreeMap();
        Map fileMap = new HashMap();
        List analysisNumbers = new ArrayList<String>();
        GetExpandedAnalysisFileList.getFileNamesToDownload(baseDir, analysis.getKey(), analysisNumbers, analysisMap, directoryMap, false);
        addMainDownloadTable(baseURL, maindiv, "unfiled", analysisMap, directoryMap, analysis.getNumber(), analysis.getIdAnalysis());
        Element br = new Element("br");
        maindiv.addContent(br);
        Set folders = GetAnalysisDownloadList.getAnalysisDownloadFolders(baseDir, analysis.getNumber(), analysis.getCreateYear());
        for (Iterator i = folders.iterator(); i.hasNext(); ) {
            String folder = (String) i.next();
            analysisMap = new TreeMap();
            directoryMap = new TreeMap();
            fileMap = new HashMap();
            analysisNumbers = new ArrayList<String>();
            boolean flattenSubDirs = true;
            GetExpandedAnalysisFileList.getFileNamesToDownload(baseDir, analysis.getKey(folder), analysisNumbers, analysisMap, directoryMap, flattenSubDirs);
            addDownloadTable(baseURL, maindiv, folder, analysisMap, directoryMap, analysis.getNumber(), analysis.getIdAnalysis());
            if (i.hasNext()) {
                br = new Element("br");
                maindiv.addContent(br);
            }
        }
        br = new Element("br");
        maindiv.addContent(br);
        return doc;
    }

    private static void addDownloadTable(String baseURL, Element maindiv, String folder, Map analysisMap, Map directoryMap, String analysisNumber, Integer idAnalysis) {
        Element tableNode = new Element("table");
        maindiv.addContent(tableNode);
        Element caption = new Element("caption");
        caption.setAttribute("class", "narrow");
        caption.addContent(folder);
        tableNode.addContent(caption);
        AnalysisFileDescriptor analysisFd = null;
        List directoryKeys = (List) analysisMap.get(analysisNumber);
        if (directoryKeys != null) {
            for (Iterator i1 = directoryKeys.iterator(); i1.hasNext(); ) {
                String directoryKey = (String) i1.next();
                String dirTokens[] = directoryKey.split("-");
                List theFiles = (List) directoryMap.get(directoryKey);
                for (Iterator i2 = theFiles.iterator(); i2.hasNext(); ) {
                    AnalysisFileDescriptor fd = (AnalysisFileDescriptor) i2.next();
                    fd.setQualifiedFilePath(dirTokens[0]);
                    recurseAddFileRow(baseURL, tableNode, fd, idAnalysis);
                }
            }
        }
    }

    private static void addMainDownloadTable(String baseURL, Element maindiv, String folder, Map analysisMap, Map directoryMap, String analysisNumber, Integer idAnalysis) {
        Element tableNode = new Element("table");
        maindiv.addContent(tableNode);
        Element caption = new Element("caption");
        caption.setAttribute("class", "narrow");
        caption.addContent(folder);
        tableNode.addContent(caption);
        List directoryKeys = (List) analysisMap.get(analysisNumber);
        if (directoryKeys != null) {
            for (Iterator i1 = directoryKeys.iterator(); i1.hasNext(); ) {
                String directoryKey = (String) i1.next();
                String dirTokens[] = directoryKey.split("-");
                List theFiles = (List) directoryMap.get(directoryKey);
                boolean firstFileInDir = true;
                for (Iterator i2 = theFiles.iterator(); i2.hasNext(); ) {
                    AnalysisFileDescriptor fd = (AnalysisFileDescriptor) i2.next();
                    fd.setQualifiedFilePath(dirTokens[0]);
                    if (fd.getType() != null && fd.getType() != "dir") {
                        String dirParm = fd.getQualifiedFilePath() != null && !fd.getQualifiedFilePath().equals("") ? "&dir=" + fd.getQualifiedFilePath() : "";
                        Element downloadLink = new Element("A");
                        downloadLink.setAttribute("href", baseURL + "/" + Constants.DOWNLOAD_ANALYSIS_SINGLE_FILE_SERVLET + "?idAnalysis=" + idAnalysis + "&fileName=" + fd.getDisplayName() + dirParm);
                        downloadLink.addContent(fd.getDisplayName());
                        tableNode.addContent(makeRow(downloadLink, "", fd.getFileSizeText()));
                    }
                }
            }
        }
    }

    private static void recurseAddFileRow(String baseURL, Element tableNode, AnalysisFileDescriptor fd, Integer idAnalysis) {
        if (fd.getChildren() != null && fd.getChildren().size() > 0) {
            for (Iterator i = fd.getChildren().iterator(); i.hasNext(); ) {
                AnalysisFileDescriptor childFd = (AnalysisFileDescriptor) i.next();
                recurseAddFileRow(baseURL, tableNode, childFd, idAnalysis);
            }
        } else {
            String dirParm = fd.getQualifiedFilePath() != null && !fd.getQualifiedFilePath().equals("") ? "&dir=" + fd.getQualifiedFilePath() : "";
            Element downloadLink = new Element("A");
            downloadLink.setAttribute("href", baseURL + "/" + Constants.DOWNLOAD_ANALYSIS_SINGLE_FILE_SERVLET + "?idAnalysis=" + idAnalysis + "&fileName=" + fd.getDisplayName() + dirParm);
            downloadLink.addContent(fd.getDisplayName());
            tableNode.addContent(makeRow(downloadLink, "", fd.getFileSizeText()));
        }
    }

    private static Element makeHeaderRow() {
        Element row = new Element("TR");
        Element cell = new Element("TH");
        cell.addContent("File");
        row.addContent(cell);
        cell = new Element("TH");
        cell.addContent("Comments");
        row.addContent(cell);
        cell = new Element("TH");
        cell.addContent("Size");
        row.addContent(cell);
        return row;
    }

    private static Element makeRow(Element link, String comment, String fileSize) {
        Element row = new Element("TR");
        Element cell = new Element("TD");
        cell.setAttribute("class", "noborder");
        cell.addContent(link);
        row.addContent(cell);
        cell = new Element("TD");
        cell.setAttribute("class", "noborder");
        cell.addContent(comment == null || comment.equals("") ? "&nbsp;" : comment);
        row.addContent(cell);
        cell = new Element("TD");
        cell.setAttribute("class", "noborderSmall");
        cell.addContent(fileSize);
        row.addContent(cell);
        return row;
    }

    /**
   *  The callback method called after the loadCommand, and execute methods,
   *  this method allows you to manipulate the HttpServletResponse object prior
   *  to forwarding to the result JSP (add a cookie, etc.)
   *
   *@param  request  The HttpServletResponse for the command
   *@return          The processed response
   */
    public HttpServletResponse setResponseState(HttpServletResponse response) {
        return response;
    }

    /**
   *  The callback method called after the loadCommand and execute methods
   *  allowing you to do any post-execute processing of the HttpSession. Should
   *  be used to add/remove session data resulting from the execution of this
   *  command
   *
   *@param  session  The HttpSession
   *@return          The processed HttpSession
   */
    public HttpSession setSessionState(HttpSession session) {
        if (createdSecurityAdvisor) {
            session.setAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY, secAdvisor);
        }
        return session;
    }
}
