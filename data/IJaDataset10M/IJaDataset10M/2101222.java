package com.worldware.ichabod.webui;

import java.net.*;
import java.util.*;
import java.io.*;
import com.worldware.web.*;
import com.worldware.ichabod.node.*;

/** Displays statistics for WebUIHost */
class WebUITree extends WebUI {

    static String getCopyrightString() {
        return "Copyright (c) 1998, Thomas Hill, All rights Reserved";
    }

    private MenuHTML m_helpMenu = new MenuHTML();

    /** Constructor */
    WebUITree(DataNode dn) {
        super(dn, false);
        m_specialCases.put(helpURL, new WebUIHelp(dn, this));
        m_helpMenu.addElement(new MenuItemHTML("BACK TO TREE", MenuItemHTML.PARENTURL, true));
    }

    String getAdminData(WebInfo wi) throws HTTPException {
        StringBuffer sb = new StringBuffer(128);
        sb.append(pageTitle("Tree View"));
        sb.append(getNavigationBar(wi));
        sb.append("<a href=\"" + wi.getParentURL() + "\">Back to parent page</a>");
        sb.append(m_node.getWebUIObject().getChildListRecursive(wi.getParentURL(), true));
        return sb.toString();
    }

    /** Gets admin help information for the current page. */
    String getUserHelp(WebInfo wi) {
        StringBuffer s = new StringBuffer(1024);
        s.append(pageTitle("Help for Tree View Page"));
        s.append(m_helpMenu.formatMenu(wi));
        s.append("<P>Teh tree view page is simply a quick way of getting to any object in");
        s.append("the program's user interface.<P>");
        s.append("Frequently fastest way to get to an accounts page, is to click on 'tree view' ");
        s.append("in the menu at the top of any page, and then click on the account name.");
        return s.toString();
    }
}
