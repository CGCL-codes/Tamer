package com.dynamide.util.trace;

import java.util.Collection;
import java.util.Iterator;
import com.dynamide.Session;
import com.dynamide.util.CompositeArray;
import com.dynamide.util.IComposite;
import com.dynamide.util.ICompositeWalker;
import com.dynamide.util.StringTools;
import com.dynamide.util.Tools;

public class ExpansionLog {

    private IComposite m_expansionLogRoot = new CompositeArray("root", null);

    public IComposite getExpansionLogRoot() {
        return m_expansionLogRoot;
    }

    private IComposite m_expansionLogCurrent = m_expansionLogRoot;

    public String dump() {
        try {
            return m_expansionLogRoot.dumpHTML();
        } catch (Exception e) {
            return Tools.errorToString(e, true);
        }
    }

    public String toString() {
        return dump();
    }

    public static String formatEscapePre(String body) {
        return "<pre>" + StringTools.escape(body) + "</pre>";
    }

    private int m_expansionLogSequence = 0;

    public IComposite enterExpansion(Session session, String name, String detailName, String type, String body, String location) {
        CompositeArray expansionLogNode = new CompositeArray(name + '[' + (m_expansionLogSequence++) + ']', m_expansionLogCurrent);
        try {
            m_expansionLogCurrent.addChild(expansionLogNode);
            expansionLogNode.addAttribute("SESSIONID", session.getSessionID());
            expansionLogNode.addAttribute("THREADID", session.getThreadID());
            LogDetailItem item = new LogDetailItem(detailName, type, body, location, LogDetailItem.EC_START);
            expansionLogNode.addAttribute(detailName, item);
            m_expansionLogCurrent = expansionLogNode;
        } catch (Exception e) {
            System.out.println("COULD NOT addChild() in Session.enterExpansion().");
        }
        return expansionLogNode;
    }

    public IComposite logExpansionError(IComposite current, String detailName, String type, String body, String location) {
        LogDetailItem item = new LogDetailItem(detailName, type, body, location, LogDetailItem.EC_ERROR);
        current.addAttribute(detailName, item);
        current.addAttribute("ERROR", "true");
        IComposite parent = current.getParent();
        if (parent != null) {
            m_expansionLogCurrent = parent;
        }
        return current;
    }

    public void logExpansion(IComposite current, String detailName, String type, String body, String location) {
        LogDetailItem item = new LogDetailItem(detailName, type, body, location, LogDetailItem.EC_INFO);
        current.addAttribute(detailName, item);
    }

    public IComposite leaveExpansion(IComposite current, String detailName, String type, String body, String location) {
        LogDetailItem item = new LogDetailItem(detailName, type, body, location, LogDetailItem.EC_DONE);
        current.addAttribute(detailName, item);
        IComposite parent = current.getParent();
        if (parent != null) {
            m_expansionLogCurrent = parent;
        }
        return current;
    }

    public String printExpansionLog(IComposite current) {
        return "<pre><!--ExpansionLog-->" + current.toString() + "</pre>";
    }

    public String dumpLinks(String baseURL) {
        StringBuffer out = new StringBuffer();
        walk(getExpansionLogRoot(), out, baseURL);
        return out.toString();
    }

    public class LinkWalker implements ICompositeWalker {

        public LinkWalker(String baseURL) {
            this.baseURL = baseURL;
        }

        private String baseURL = "";

        private StringBuffer m_buffer = new StringBuffer();

        public String toString() {
            return m_buffer.toString();
        }

        public boolean onNode(com.dynamide.util.IComposite node) {
            int depth = node.getDepth();
            Object sHasError = node.get("ERROR");
            boolean hasError = false;
            if (sHasError != null && sHasError.toString().length() > 0) {
                hasError = true;
            }
            if (hasError) {
                m_buffer.append("<table border='1' cellpadding='0' cellspacing='0'><tr><td bgcolor='#FEFFAA'>");
            }
            m_buffer.append(StringTools.fill("&nbsp;", depth));
            m_buffer.append("<a href='" + baseURL + "&compositeID=" + node.getName() + "#detail'>");
            m_buffer.append(node.dumpPath());
            m_buffer.append("</a>&nbsp;&nbsp;&nbsp;&nbsp;");
            m_buffer.append(node.get("THREADID"));
            m_buffer.append("<br/>\r\n");
            for (Iterator it = node.getAttributes().iterator(); it.hasNext(); ) {
                CompositeArray.Entry entry = (CompositeArray.Entry) it.next();
                Object obj = entry.value;
                if (obj instanceof LogDetailItem) {
                    LogDetailItem item = (LogDetailItem) obj;
                    m_buffer.append(StringTools.fill("&nbsp;", depth + 4));
                    m_buffer.append(item.formatHtmlStatus());
                    m_buffer.append("&nbsp;");
                    m_buffer.append(item.name);
                    m_buffer.append("<br/>\r\n");
                }
            }
            if (hasError) {
                m_buffer.append("</td></tr></table>");
            }
            return true;
        }
    }

    public void walk(IComposite node, StringBuffer out, String baseURL) {
        Object sHasError = node.get("ERROR");
        boolean hasError = false;
        if (sHasError != null && sHasError.toString().length() > 0) {
            hasError = true;
        }
        int depth = node.getDepth();
        int padwidth = depth * 10;
        out.append("<table border='0' cellpadding='0' cellspacing='0'><tr><td width='" + padwidth + "'>&nbsp;</td><td>");
        if (hasError) {
            out.append("<table border='1' cellpadding='4' cellspacing='0'><tr><td bgcolor='#FEFFAA'>");
        }
        out.append("<a href='" + baseURL + "&compositeID=" + node.getName() + "#detail'>");
        out.append(node.getName());
        out.append("</a>&nbsp;&nbsp;&nbsp;&nbsp;");
        out.append("<small>(").append(node.get("THREADID")).append(")</small>");
        out.append("<br />");
        for (Iterator it = node.getAttributes().iterator(); it.hasNext(); ) {
            CompositeArray.Entry entry = (CompositeArray.Entry) it.next();
            Object obj = entry.value;
            if (obj instanceof LogDetailItem) {
                LogDetailItem item = (LogDetailItem) obj;
                out.append(StringTools.fill("&nbsp;", depth + 4));
                out.append(item.formatHtmlStatus());
                out.append("&nbsp;");
                out.append(item.name);
                out.append("<br/>\r\n");
            }
        }
        Collection children = node.getChildren();
        if (null != children) {
            IComposite child;
            for (Iterator it = children.iterator(); it.hasNext(); ) {
                child = (IComposite) it.next();
                walk(child, out, baseURL);
            }
        }
        if (hasError) {
            out.append("</td></tr></table>");
        } else {
        }
        out.append("</td></tr></table>");
    }
}
