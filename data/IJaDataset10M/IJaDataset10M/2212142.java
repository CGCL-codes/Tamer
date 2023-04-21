package com.objectwave.tools;

import com.objectwave.utility.*;
import javax.swing.tree.*;

/**
 *
 * @version 2.3
 * @author Dave Hoag
 */
public class SearchResults extends javax.swing.JFrame implements javax.swing.event.TreeSelectionListener, java.awt.event.MouseListener {

    private javax.swing.JPanel ivjJFrameContentPane = null;

    private javax.swing.JScrollPane ivjJScrollPane1 = null;

    private javax.swing.JSplitPane ivjJSplitPane1 = null;

    private javax.swing.JTextArea ivjJTextArea1 = null;

    private javax.swing.JTree ivjJTree1 = null;

    private TreeModel ivjTreeModel = null;

    private DocSearchIF ivjDocSearch = null;

    public void setDocSearch(DocSearchIF newValue) {
        if (ivjDocSearch != newValue) {
            try {
                DocSearchIF oldValue = getDocSearch();
                ivjDocSearch = newValue;
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
    }

    public DocSearchIF getDocSearch() {
        return ivjDocSearch;
    }

    public SearchResults() {
        super();
        initialize();
    }

    /**
 * SearchResults constructor comment.
 * @param title java.lang.String
 */
    public SearchResults(String title) {
        super(title);
    }

    private String connEtoC1() {
        String connEtoC1Result = null;
        try {
            connEtoC1Result = this.jTree1_TreeSelectionEvents();
            connEtoM2(connEtoC1Result);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        return connEtoC1Result;
    }

    private void connEtoC2(java.awt.event.MouseEvent arg1) {
        try {
            this.jTree1_MouseClicked(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private void connEtoM1(TreeModel value) {
        try {
            getJTree1().setModel(getTreeModel());
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private void connEtoM2(String result) {
        try {
            getJTextArea1().setText(result);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private javax.swing.JPanel getJFrameContentPane() {
        if (ivjJFrameContentPane == null) {
            try {
                ivjJFrameContentPane = new javax.swing.JPanel();
                ivjJFrameContentPane.setName("JFrameContentPane");
                ivjJFrameContentPane.setLayout(new java.awt.BorderLayout());
                getJFrameContentPane().add(getJSplitPane1(), "Center");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
        return ivjJFrameContentPane;
    }

    private javax.swing.JScrollPane getJScrollPane1() {
        if (ivjJScrollPane1 == null) {
            try {
                ivjJScrollPane1 = new javax.swing.JScrollPane();
                ivjJScrollPane1.setName("JScrollPane1");
                getJScrollPane1().setViewportView(getJTree1());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
        return ivjJScrollPane1;
    }

    private javax.swing.JSplitPane getJSplitPane1() {
        if (ivjJSplitPane1 == null) {
            try {
                ivjJSplitPane1 = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT);
                ivjJSplitPane1.setName("JSplitPane1");
                ivjJSplitPane1.setDividerLocation(155);
                getJSplitPane1().add(getJScrollPane1(), "left");
                getJSplitPane1().add(getJTextArea1(), "right");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
        return ivjJSplitPane1;
    }

    private javax.swing.JTextArea getJTextArea1() {
        if (ivjJTextArea1 == null) {
            try {
                ivjJTextArea1 = new javax.swing.JTextArea();
                ivjJTextArea1.setName("JTextArea1");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
        return ivjJTextArea1;
    }

    private javax.swing.JTree getJTree1() {
        if (ivjJTree1 == null) {
            try {
                ivjJTree1 = new javax.swing.JTree();
                ivjJTree1.setName("JTree1");
                ivjJTree1.setBounds(0, 0, 258, 151);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
        return ivjJTree1;
    }

    public TreeModel getTreeModel() {
        return ivjTreeModel;
    }

    /**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
    private void handleException(Throwable exception) {
    }

    private void initConnections() {
        getJTree1().addTreeSelectionListener(this);
        getJTree1().addMouseListener(this);
    }

    private void initialize() {
        setName("SearchResults");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(426, 240);
        setTitle("Search Results");
        setContentPane(getJFrameContentPane());
        initConnections();
    }

    /**
 * @fixme Needs to address the double click action!
 */
    public void jTree1_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() > 1) {
            Object[] path = getJTree1().getSelectionPath().getPath();
            for (int i = path.length - 1; i > -1; --i) {
                DisplayPair pair = (DisplayPair) path[i];
                try {
                    getDocSearch().openEditor(pair.getSecond());
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }
        return;
    }

    /**
 * Comment
 */
    public String jTree1_TreeSelectionEvents() {
        DisplayPair dp = (DisplayPair) getJTree1().getLastSelectedPathComponent();
        Object obj = dp.getSecond();
        if (obj instanceof com.objectwave.sourceModel.ClassElement) {
            return ((com.objectwave.sourceModel.ClassElement) obj).getFullTextNoChild();
        }
        return obj.toString();
    }

    /**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
    public static void main(java.lang.String[] args) {
        try {
            SearchResults aSearchResults;
            aSearchResults = new SearchResults();
            try {
                Class aCloserClass = Class.forName("com.ibm.uvm.abt.edit.WindowCloser");
                Class parmTypes[] = { java.awt.Window.class };
                Object parms[] = { aSearchResults };
                java.lang.reflect.Constructor aCtor = aCloserClass.getConstructor(parmTypes);
                aCtor.newInstance(parms);
            } catch (java.lang.Throwable exc) {
            }
            ;
            aSearchResults.setVisible(true);
        } catch (Throwable exception) {
            System.err.println("Exception occurred in main() of javax.swing.JFrame");
            exception.printStackTrace(System.out);
        }
    }

    public void mouseClicked(java.awt.event.MouseEvent e) {
        if ((e.getSource() == getJTree1())) {
            connEtoC2(e);
        }
    }

    public void mouseEntered(java.awt.event.MouseEvent e) {
    }

    public void mouseExited(java.awt.event.MouseEvent e) {
    }

    public void mousePressed(java.awt.event.MouseEvent e) {
    }

    public void mouseReleased(java.awt.event.MouseEvent e) {
    }

    public void setTreeModel(TreeModel newValue) {
        if (ivjTreeModel != newValue) {
            try {
                javax.swing.tree.TreeModel oldValue = getTreeModel();
                ivjTreeModel = newValue;
                connEtoM1(ivjTreeModel);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
    }

    public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
        if ((e.getSource() == getJTree1())) {
            connEtoC1();
        }
    }
}
