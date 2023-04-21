package com.xingdongjin.jdocviewer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.net.URL;
import java.util.Vector;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import com.xingdongjin.jdocviewer.events.IndexTocPanelEvent;

public class DocContentPanel extends JPanel {

    private JScrollPane docContentScrollPane;

    private JEditorPane docContentEditorPane;

    /**
	 * the vector containing the visiting history in format of
	 * URLOrHTMLString
	 */
    protected Vector visitHistory;

    private int historyListSize = 50;

    protected int historyPos;

    protected DocContentPanel() {
        init();
    }

    private void init() {
        visitHistory = new Vector(historyListSize);
        historyPos = 0;
        HTMLEditorKit htmlEditorkit = new HTMLEditorKit();
        docContentEditorPane = new JEditorPane();
        docContentEditorPane.setEditorKit(htmlEditorkit);
        docContentEditorPane.setBackground(Color.WHITE);
        docContentEditorPane.setEditable(false);
        docContentEditorPane.addHyperlinkListener(new MyHyperlinkListener());
        docContentEditorPane.setContentType("text/html");
        docContentScrollPane = new JScrollPane();
        docContentScrollPane.getViewport().setView(docContentEditorPane);
        docContentScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setLayout(new BorderLayout());
        this.add(docContentScrollPane, BorderLayout.CENTER);
    }

    /**
	 * Load the JavaDoc page by URL
	 * @param path URL path
	 * @param isAddedToHistory indicates if this visit should be added to the
	 * history. Visit by traverse the history is not added to the history list
	 * again. 
	 */
    public void loadDocContentsByURL(URL path, boolean isAddedToHistory) {
        try {
            synchronized (docContentEditorPane) {
                docContentEditorPane.setPage(path);
            }
            URLOrHTMLString page = new URLOrHTMLString();
            page.setType(IndexTocPanelEvent.CONTENTS_BY_URL);
            page.setUrl(path);
            System.out.println(path);
            if (isAddedToHistory) {
                addVisitHistory(page);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Load the page by HTML string to list the choices 
	 * @param htmlString HTML in string format.
	 * @param isAddedToHistory indicates if this visit should be added to the
	 * history. Visit by traverse the history is not added to the history list
	 * again. 
	 */
    public void loadDocContentsByHTML(String htmlString, boolean isAddedToHistory) {
        synchronized (docContentEditorPane) {
            docContentEditorPane.setText(htmlString);
        }
        URLOrHTMLString page = new URLOrHTMLString();
        page.setType(IndexTocPanelEvent.CONTENTS_BY_HTML);
        page.setHtmlString(htmlString);
        if (isAddedToHistory) addVisitHistory(page);
    }

    private void addVisitHistory(URLOrHTMLString page) {
        if (visitHistory.size() == historyPos) {
            visitHistory.add(page);
            historyPos++;
        } else {
            visitHistory.insertElementAt(page, historyPos + 1);
            historyPos++;
            visitHistory.subList(historyPos + 1, visitHistory.size() - 1).clear();
        }
    }

    public void visitPreviousPageInHistory() {
        if (historyPos > 0) {
            historyPos--;
            URLOrHTMLString page = (URLOrHTMLString) visitHistory.get(historyPos);
            visitPageInHistory(page);
        }
    }

    public void visitNextPageInHistory() {
        if (historyPos < visitHistory.size() - 1) {
            historyPos++;
            URLOrHTMLString page = (URLOrHTMLString) visitHistory.get(historyPos);
            visitPageInHistory(page);
        }
    }

    private void visitPageInHistory(URLOrHTMLString page) {
        if (page.getType() == IndexTocPanelEvent.CONTENTS_BY_HTML) {
            loadDocContentsByHTML(page.getHtmlString(), false);
            System.out.println(page.getHtmlString());
        } else if (page.getType() == IndexTocPanelEvent.CONTENTS_BY_URL) {
            loadDocContentsByURL(page.getUrl(), false);
            System.out.println(page.getUrl());
        }
    }

    class MyHyperlinkListener implements HyperlinkListener {

        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                URL targetPath = e.getURL();
                loadDocContentsByURL(targetPath, true);
            }
        }
    }

    class URLOrHTMLString {

        URL url = null;

        String htmlString = null;

        /**
		 * 1 means URL and 2 means HTMLString
		 */
        int type = 0;

        public String getHtmlString() {
            return htmlString;
        }

        public void setHtmlString(String htmlString) {
            this.htmlString = htmlString;
        }

        public int getType() {
            return type;
        }

        /**
		 * Set the type by which the page is visited. 1 means by URL
		 * and 2 means HTML String.
		 * @param type type of visits
		 */
        public void setType(int type) {
            this.type = type;
        }

        public URL getUrl() {
            return url;
        }

        public void setUrl(URL url) {
            this.url = url;
        }
    }
}
