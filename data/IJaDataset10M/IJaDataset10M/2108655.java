package com.worldware.ichabod.webui;

import com.worldware.ichabod.node.*;
import java.net.*;
import java.util.*;
import java.io.*;
import javax.servlet.http.*;
import com.worldware.web.*;

/** Base class for the web user interfaces for the TargetLocalBase class */
public class WebUITargetLocalBase extends WebUI {

    static String getCopyrightString() {
        return "Copyright (c) 1998, Thomas Hill, All rights Reserved";
    }

    public WebUITargetLocalBase(TargetLocalBase dn) {
        super(dn);
    }

    String postInterface(WebInfo wi) throws IOException {
        String parentURL = wi.getParentURL();
        String curURL = wi.getCurrentURL();
        Authorization auth = wi.getAuth();
        TargetLocalBase dn = (TargetLocalBase) m_node;
        StringBuffer sb = new StringBuffer(128);
        sb.append(pageTitle("Name: " + dn.getName()));
        sb.append(getNavigationBar(wi));
        String NewPassword = null;
        String NewPassword2 = null;
        NewPassword = wi.getParameter(newPasswordText);
        if (NewPassword == null) {
            sb.append("Error in form or URL. (missing new password field).  No changes made");
            return sb.toString();
        }
        NewPassword = NewPassword.trim();
        NewPassword2 = wi.getParameter(newPasswordText2);
        if (NewPassword2 == null) {
            sb.append("Error in form or URL.  (missing new password2 field). No changes made");
            return sb.toString();
        }
        NewPassword2 = NewPassword2.trim();
        {
            if (!NewPassword.equals(NewPassword2)) {
                sb.append("The new passwords do not match. Enter the new password in the <var>New Password</var> field, and then enter it again in the <var>Confirm Password</var> field.<P>");
                return sb.toString();
            }
            if (NewPassword.length() != 0) {
                dn.setPassword(NewPassword);
                sb.append("Password changed, effective immediately<P>You may have to log in again before accessing the user again, since the password you logged in on is no longer valid.<P>You might have to restart your browser to enter the new password.<P> ");
            }
        }
        String newName = wi.getParameter(newNameText);
        if ((newName == null) || (newName.trim().length() == 0)) {
            sb.append("Please enter a new name for the account<P>");
            return sb.toString();
        }
        newName = newName.trim();
        String oldName = dn.getName();
        try {
            dn.setNameSave(newName);
        } catch (DataNodeException iae) {
            sb.append("The new name is not acceptable: " + iae.getMessage());
            return sb.toString();
        }
        sb.append("Name changed to '" + dn.getName() + "'<P>");
        newName = dn.getName();
        curURL = WebTools.replaceLastURLComponent(curURL, newName);
        sb.append("<a href=\"" + curURL + "\">Reload user info page</a>. Don't just use the Browser's back button. If you changed the name the previous URL is no longer valid.");
        return sb.toString();
    }

    /** Gets the HTML data that makes up the user interface for this object */
    String getInterfaceData(WebInfo wi) {
        if (wi.getAuth().isValid()) return getAdminData(wi); else {
            return pageTitle("Name: " + m_node.getName());
        }
    }

    String getDescription() {
        return "This is a generic object";
    }

    /** Default implementation, displays info for any DataNode that does not
	  * have its own getData class
	  */
    String getAdminData(WebInfo wi) {
        TargetLocalBase dn = (TargetLocalBase) m_node;
        StringBuffer sb = new StringBuffer(128);
        sb.append(pageTitle("Account '" + dn.getName() + "' on host <a href=\"" + wi.getParentURL() + "\">" + dn.getParent().getName() + "</a>"));
        sb.append(getNavigationBar(wi));
        sb.append(getDescription() + "<br>");
        sb.append(getExtraInfo() + "<P>");
        sb.append(getAdminForm(wi));
        return sb.toString();
    }

    String getAdminForm(WebInfo wi) {
        String parentURL = wi.getParentURL();
        String curURL = wi.getCurrentURL();
        StringBuffer sb = new StringBuffer(512);
        sb.append(header(1, "Settings"));
        sb.append("To rename this account, enter the new name in the <var>New Name</var> box, and press the <var>Change</var> button. ");
        sb.append("To change the password, enter the new password in the <var>New Password</var> and <var>Confirm password</var> boxes, and then press the <var>Change</var> button. ");
        sb.append("<form>");
        sb.append("<TABLE WIDTH = 100% BORDER = 2 CELLPADDING = 2 CELLSPACING = 2>");
        sb.append("<TR>");
        sb.append("<TD VALIGN = CENTER ALIGN = CENTER WIDTH = 50%><B>New Name --&gt; </TD>\r\n");
        if (wi.getAuth().isAdmin()) sb.append("<TD VALIGN = CENTER ALIGN = CENTER WIDTH = 50%><input type=\"text\" name=\"" + newNameText + "\" value=\"" + m_node.getName() + "\"></TD>\r\n"); else sb.append("<TD VALIGN = CENTER ALIGN = CENTER WIDTH = 50%><input type=\"hidden\" name=\"" + newNameText + "\" value = \"" + m_node.getName() + "\">" + m_node.getName() + "</TD>\r\n");
        sb.append("</TR>");
        sb.append("<TR>\r\n");
        sb.append("<TD VALIGN = CENTER ALIGN = CENTER WIDTH = 50%><B>New Password --&gt; </TD>\r\n");
        sb.append("<TD VALIGN = CENTER ALIGN = CENTER WIDTH = 50%><input type=\"password\" name=\"" + newPasswordText + "\"></TD>\r\n");
        sb.append("</TR>\r\n");
        sb.append("<TR>\r\n");
        sb.append("<TD VALIGN = CENTER ALIGN = CENTER WIDTH = 50%><B>Confirm Password --&gt; </TD>\r\n");
        sb.append("<TD VALIGN = CENTER ALIGN = CENTER WIDTH = 50%><input type=\"password\" name=\"" + newPasswordText2 + "\"></TD>\r\n");
        sb.append("</TR>\r\n");
        sb.append("<TR>");
        sb.append("<TD VALIGN = CENTER  ALIGN = CENTER WIDTH=100%><input type=\"reset\" name=\"Reset\" value=\"Reset\"></TD>\r\n");
        sb.append("<TD VALIGN = CENTER ALIGN = CENTER WIDTH = 50%><input type=\"submit\" name=\"" + renameButtonName + "\" value=\"Change\"></TD>");
        sb.append("</TR>");
        sb.append("</TABLE>");
        sb.append("</form>");
        sb.append(header(1, "Deleting"));
        sb.append("To delete this account, enter the administrative password into the edit box below, then press the <var>Delete This Account</var> button. ");
        sb.append("<form action=" + Trellis.appendURL(curURL, "delete") + ">\r\n");
        sb.append("<TABLE WIDTH = 100% BORDER = 2 CELLPADDING = 2 CELLSPACING = 2>\r\n");
        sb.append("<TR>\r\n");
        sb.append("<TD VALIGN = CENTER ALIGN = CENTER WIDTH = 50%><B>Administrative Password -></B></TD>\r\n");
        sb.append("<TD VALIGN = CENTER ALIGN = CENTER WIDTH = 50%><input type=\"password\" name=\"" + confirmPassword + "\" value=\"\"></TD>\r\n");
        sb.append("</TR>\r\n");
        sb.append("<TR>\r\n");
        sb.append("<TD VALIGN = CENTER  colspan = 2 ALIGN = CENTER WIDTH=100%><input type=\"submit\" name=\"" + deleteButtonName + "\" value=\"Delete This Account\"></TD>\r\n");
        sb.append("</TR>\r\n");
        sb.append("</TABLE>\r\n");
        sb.append("</form>\r\n");
        return sb.toString();
    }

    /** For subsclasses to override */
    String getExtraInfo() {
        return "";
    }
}
