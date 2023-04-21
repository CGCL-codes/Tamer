package com.apelon.beans.apelxmltree;

import java.awt.*;
import java.io.StringReader;
import javax.swing.*;
import com.apelon.beans.apeltree.*;
import com.apelon.common.log4j.Categories;
import org.w3c.dom.*;
import com.apelon.common.xml.*;
import org.w3c.dom.Document;
import com.apelon.beans.apelpanel.*;
import javax.swing.tree.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;

public class XMLTree extends ApelPanel {

    class XMLTreeSelectionListener implements javax.swing.event.TreeSelectionListener {

        /**
 * XMLTreeSelectionListener constructor comment.
 */
        public XMLTreeSelectionListener() {
            super();
            getApelTree().getJTree().addTreeSelectionListener(this);
        }

        /**
    * Called whenever the value of the selection changes.
    * @param e the event that characterizes the change.
    */
        public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
            ApelTreeNode node = (ApelTreeNode) getApelTree().getJTree().getLastSelectedPathComponent();
            if (node == null) return;
            XMLTreeEvent evt = new XMLTreeEvent(node);
            fireHandleXMLTreeEvent(evt);
        }
    }

    /**
 * XMLTree constructor comment.
 */
    public XMLTree() {
        super();
        initialize();
    }

    private static void getBuilderData() {
    }

    /**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
    private void handleException(java.lang.Throwable exception) {
        Categories.uiView().error("Caught Exception", exception);
    }

    private void initialize() {
        try {
            setName("XMLTree");
            setLayout(new java.awt.BorderLayout());
            setSize(307, 401);
            add(getApelTree(), "Center");
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * Insert the method's description here.
 * Creation date: (6/5/2001 1:30:26 PM)
 * @param args java.lang.String[]
 */
    public static void main(String[] args) {
        try {
            String xml = "<Test><This is a Test</Test>";
            if (!xml.equals("")) {
                final XMLTree xtree = new XMLTree(xml, null);
                JFrame f = new JFrame();
                f.setSize(300, 300);
                f.getContentPane().add("Center", xtree);
                f.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        Categories.uiView().info(xtree.getXML(false));
                        System.exit(0);
                    }

                    ;
                });
                f.show();
            }
        } catch (Exception e) {
            Categories.uiView().error("Caught Exception", e);
        }
    }

    protected transient com.apelon.beans.apelxmltree.XMLTreeListener aXMLTreeListener = null;

    private XMLTreeMgr fTreeMgr;

    private XMLTreeSelectionListener fTreeSelectionListener;

    private ApelTree ivjApelTree = null;

    /**
 *
 * @param newListener com.apelon.beans.apelxmltree.XMLTreeListener
 */
    public void addXMLTreeListener(com.apelon.beans.apelxmltree.XMLTreeListener newListener) {
        aXMLTreeListener = com.apelon.beans.apelxmltree.XMLTreeEventMulticaster.add(aXMLTreeListener, newListener);
        return;
    }

    /**
 * Method to support listener events.
 * @param event com.apelon.beans.apelxmltree.XMLTreeEvent
 */
    protected void fireHandleXMLTreeEvent(com.apelon.beans.apelxmltree.XMLTreeEvent event) {
        if (aXMLTreeListener == null) {
            return;
        }
        ;
        aXMLTreeListener.handleXMLTreeEvent(event);
    }

    private com.apelon.beans.apeltree.ApelTree getApelTree() {
        if (ivjApelTree == null) {
            try {
                ivjApelTree = new com.apelon.beans.apeltree.ApelTree();
                ivjApelTree.setName("ApelTree");
                ivjApelTree.setShowRootsCombo(false);
                ivjApelTree.setRootVisible(true);
                int selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION;
                ivjApelTree.getJTree().getSelectionModel().setSelectionMode(selectionMode);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjApelTree;
    }

    /**
 * Insert the method's description here.
 * Creation date: (7/12/2001 4:42:58 PM)
 * @param boolean set to true if formatted output is desired.
 * @return java.lang.String
 * method returns xml formatted with indents if true is passed in,
 * else xml returned as single inline string;
 */
    public String getXML(boolean formatted) {
        return fTreeMgr.getXML(formatted);
    }

    /**
 *
 * @param newListener com.apelon.beans.apelxmltree.XMLTreeListener
 */
    public void removeXMLTreeListener(com.apelon.beans.apelxmltree.XMLTreeListener newListener) {
        aXMLTreeListener = com.apelon.beans.apelxmltree.XMLTreeEventMulticaster.remove(aXMLTreeListener, newListener);
        return;
    }

    /**
 * Accessor to get the tree manager
 */
    public XMLTreeMgr getTreeManager() {
        return fTreeMgr;
    }

    /**
 * Insert the method's description here.
 * Creation date: (6/5/2001 2:49:33 PM)
 * @param XMLTreeMgr
 * Passing in the XMLTree manager creates the gui xml tree and adds
 * a listener that fires events encapsulating the selected apeltreenode
 * every time a node is selected in the tree.
 */
    public void setTreeManager(XMLTreeMgr mgr) {
        getApelTree().setTreeManager(mgr);
        ((XMLTreeMgr) mgr).setTree(getApelTree());
        ((XMLTreeMgr) mgr).setXMLTree(this);
        fTreeMgr = mgr;
        fTreeSelectionListener = new XMLTreeSelectionListener();
    }

    /**
 * XMLTree constructor comment.
 */
    public XMLTree(String xml, String dtdFileName) throws Exception {
        super();
        initialize();
        setXMLString(xml, dtdFileName);
    }

    /**
 * Insert the method's description here.
 * Creation date: (7/18/2001 3:23:58 PM)
 */
    public void setXMLString(String xml, String dtdFileName) throws Exception {
        Document d = null;
        if (dtdFileName != null) {
            d = XML.parse(XML.getDTD(dtdFileName), xml);
        } else {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource ip = new InputSource(new StringReader(xml));
            d = builder.parse(ip);
        }
        XMLTreeMgr mgr = new XMLTreeMgr(d);
        setTreeManager(mgr);
    }

    /**
 * Insert the method's description here.
 * Creation date: (7/20/2001 2:34:26 PM)
 */
    public void updateTree(ApelTreeNode node) {
        fTreeMgr.updateTree(node);
    }

    /**
 * Insert the method's description here.
 * Creation date: (8/9/2001 9:37:18 AM)
 */
    public ApelTreeNode getTreeRoot() {
        return fTreeMgr.getTreeRoot();
    }
}
