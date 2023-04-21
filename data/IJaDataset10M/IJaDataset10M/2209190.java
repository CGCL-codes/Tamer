package com.worldware.ichabod.webui;

import com.worldware.ichabod.node.*;
import java.net.*;
import java.util.*;
import java.io.*;
import javax.servlet.http.*;
import com.worldware.web.Authorization;
import com.worldware.misc.*;

/** Base class for the web user interface for a test file (including mail messages) */
class WebUIFile extends WebUI {

    static String getCopyrightString() {
        return "Copyright (c) 1998, 1999 Thomas Hill, All rights Reserved";
    }

    /** may be null */
    String m_filename;

    File m_file;

    /** Used in error messages. As in "Delete of "+m_type+" succeeded."
	 */
    String m_type = "file";

    /** Used in error messages. As in "Return to " + m_parentType + " page."
	 */
    String m_parentType = "inbox";

    /** The user interface for a file may, or may not show the form for deleting messages.
	 * This field (set in constructor) controls whether this page will show the delete form.
	 * (For mail messages, the delete is already on the previous list of messages screen,
	 * and it's redundant and awkward to have it on this page, too).
	 */
    private boolean m_showDeleteForm;

    /** true if the object in question is private, like a user's mail message
	 * false if the object is public, such as a message in a list archive
	 */
    private boolean m_hidden;

    /** Common constructor
	 */
    private WebUIFile(DataNode dn, boolean showDeleteForm, String objectName, String parentType, boolean hidden) {
        super(dn);
        m_showDeleteForm = showDeleteForm;
        m_type = objectName;
        m_parentType = parentType;
        m_hidden = hidden;
    }

    /** Constructor, takes a filename */
    WebUIFile(DataNode dn, String filename, boolean showDeleteFrom, String objectType, String parentType, boolean hidden) {
        this(dn, new File(filename), showDeleteFrom, objectType, parentType, hidden);
        m_filename = filename;
    }

    /** Constructor, takes a File */
    WebUIFile(DataNode dn, File fileID, boolean showDeleteForm, String objectType, String parentType, boolean hidden) {
        this(dn, showDeleteForm, objectType, parentType, hidden);
        m_file = fileID;
    }

    String postInterface(WebInfo wi) throws IOException {
        String parentURL = wi.getParentURL();
        String curURL = wi.getCurrentURL();
        Authorization auth = wi.getAuth();
        DataNode dn = m_node;
        StringBuffer sb = new StringBuffer(512);
        sb.append(pageTitle("Message for Account <a href=\"" + parentURL + "\">" + dn.getName() + "</a>\r\n"));
        sb.append(getNavigationBar(wi));
        if (m_filename != null) sb.append(header(1, "Message: " + m_filename));
        String passwordMsg = postPassword(m_node, wi, parentURL);
        if (passwordMsg != null) {
            sb.append(passwordMsg);
            return sb.toString();
        }
        boolean rc = m_file.delete();
        if (rc) {
            sb.append("Delete of " + m_type + " " + m_filename + "' succeded.<P>");
        } else {
            sb.append("Delete of " + m_type + " '" + m_filename + "' Failed<P>");
        }
        sb.append("<a href=\"" + parentURL + "\">Reload account info page</a>");
        return sb.toString();
    }

    String getTitle() {
        return pageTitle("Message for " + m_node.getName());
    }

    String showParentLink(String parentURL) {
        return "Return to the page for <a href=" + parentURL + ">" + m_node.getName() + "</a><P>";
    }

    String getDescription() {
        return "";
    }

    /** Gets the HTML data that makes up the user interface for this object 
	  * @param WebInfo wi an object the contains the vaious parameters need to process the request. Similar to an HttpServletRequest
	  */
    String getInterfaceData(WebInfo wi) {
        if (m_hidden && !wi.getAuth().isValid()) return "Error";
        StringBuffer sb = new StringBuffer(256);
        sb.append(getTitle());
        if (wi.getAuth().isValid() && wi.getAuth().isAdmin()) {
            sb.append(getNavigationBar(wi));
            sb.append(showParentLink(wi.getParentURL()));
            if (wi.getAuth().isAdmin() && (m_filename != null)) sb.append("File: " + m_filename + "<BR>\r\n");
        } else {
            sb.append("<DIV align = center>[ <a href=" + wi.getParentURL() + ">Back to " + m_parentType + "</a> ]</DIV><P>");
        }
        if (m_showDeleteForm && wi.getAuth().isValid()) {
            sb.append("<form>\r\n");
            sb.append("<TABLE WIDTH = 100% BORDER = 2 CELLPADDING = 2 CELLSPACING = 2>\r\n");
            sb.append("<TR>\r\n");
            sb.append("<TD VALIGN = CENTER ALIGN = CENTER><B>Password -></B></TD>\r\n");
            sb.append("<TD VALIGN = CENTER ALIGN = CENTER><input type=\"password\" name=\"" + confirmPassword + "\" value=\"\"></TD>\r\n");
            sb.append("<TD VALIGN = CENTER ALIGN = RIGHT><input type=\"submit\" name=\"" + deleteButtonName + "\" value=\"Delete This Message\"></TD>\r\n");
            sb.append("</TR>\r\n");
            sb.append("</TABLE>\r\n");
            sb.append("</form>\r\n");
        }
        displayData(sb, wi);
        return sb.toString();
    }

    /** Displays the file/mail message */
    void displayData(StringBuffer sb, WebInfo wi) {
        Authorization auth = wi.getAuth();
        sb.append("<pre>\r\n");
        File f;
        if (m_file != null) f = m_file; else f = new File(m_filename);
        try {
            FileInputStream fis = new FileInputStream(f);
            DataInputStream dis = new DataInputStream(fis);
            String line;
            while ((line = dis.readLine()) != null) {
                sb.append(line + "\r\n");
            }
            fis.close();
        } catch (IOException ioe) {
            sb.append("Filename: " + m_filename + "\r\n");
            sb.append(ioe.toString());
        }
        sb.append("</pre>\r\n");
    }

    /** Gets the value to use for the HTML &lt;TITLE&gt; tag for this page 
	  * @see pageTitle
	  */
    public String getTitleTagValue() {
        if (m_filename != null) return "File " + m_filename; else return "File " + m_file;
    }
}
