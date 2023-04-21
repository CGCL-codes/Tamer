package com.apelon.beans.apelxmltree;

import com.apelon.beans.apelapp.ApelApp;
import com.apelon.beans.apeldlg.ApelDlg;
import com.apelon.common.log4j.Categories;
import java.awt.Component;

public class XMLEditorPanel extends com.apelon.beans.apelpanel.ApelPanel {

    private javax.swing.JPanel ivjJPanel1 = null;

    private javax.swing.JPanel ivjJPanel2 = null;

    private XMLTreeViewer ivjXMLTreeViewer = null;

    private javax.swing.JButton ivjbtnCancel = null;

    private javax.swing.JButton ivjbtnOK = null;

    IvjEventHandler ivjEventHandler = new IvjEventHandler();

    private javax.swing.JPanel ivjJPanel3 = null;

    private int maxTextLength = 0;

    class IvjEventHandler implements java.awt.event.ActionListener {

        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == XMLEditorPanel.this.getbtnOK()) connEtoC1(e);
            if (e.getSource() == XMLEditorPanel.this.getbtnCancel()) connEtoC2(e);
        }

        ;
    }

    ;

    /**
 * XMLEditorPanel constructor comment.
 */
    public XMLEditorPanel() {
        super();
        initialize();
    }

    /**
 * XMLEditorPanel constructor comment.
 * @param layout java.awt.LayoutManager
 */
    public XMLEditorPanel(java.awt.LayoutManager layout) {
        super(layout);
    }

    /**
 * XMLEditorPanel constructor comment.
 * @param layout java.awt.LayoutManager
 * @param isDoubleBuffered boolean
 */
    public XMLEditorPanel(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    /**
 * Insert the method's description here.
 * Creation date: (8/8/2001 12:57:19 PM)
 * @param XML java.lang.String
 */
    public XMLEditorPanel(String xml) throws Exception {
        super();
        initialize();
        getXMLTreeViewer().setXML(xml);
    }

    /**
 * XMLEditorPanel constructor comment.
 * @param isDoubleBuffered boolean
 */
    public XMLEditorPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    /**
 * Comment
 */
    public void btnCancel_ActionPerformed() {
        panelComplete(CANCEL_CLICKED);
    }

    /**
 * Called when OK button is pressed.  re-parses XML graph and completes panel on successful parse.
 */
    public void btnOK_ActionPerformed() {
        String errorMessage = null;
        if (!getXMLTreeViewer().validateXML()) {
            errorMessage = "XML Editor Error: Cannot save malformed XML.";
            ApelApp.showErrorMsg(errorMessage, ApelApp.fAppJFrame);
        } else if (getMaxTextLength() > 0 && getXML(false).length() > getMaxTextLength()) {
            errorMessage = "XML text cannot exceed " + getMaxTextLength() + " characters.";
        }
        if (errorMessage != null) {
            ApelApp.showErrorMsg(errorMessage, ApelApp.fAppJFrame);
            return;
        }
        panelComplete(OK_CLICKED);
    }

    private void connEtoC1(java.awt.event.ActionEvent arg1) {
        try {
            this.btnOK_ActionPerformed();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private void connEtoC2(java.awt.event.ActionEvent arg1) {
        try {
            this.btnCancel_ActionPerformed();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private javax.swing.JButton getbtnCancel() {
        if (ivjbtnCancel == null) {
            try {
                ivjbtnCancel = new javax.swing.JButton();
                ivjbtnCancel.setName("btnCancel");
                ivjbtnCancel.setText("Cancel");
                ivjbtnCancel.setActionCommand("Cancel");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjbtnCancel;
    }

    private javax.swing.JButton getbtnOK() {
        if (ivjbtnOK == null) {
            try {
                ivjbtnOK = new javax.swing.JButton();
                ivjbtnOK.setName("btnOK");
                ivjbtnOK.setText("OK");
                ivjbtnOK.setMaximumSize(new java.awt.Dimension(73, 25));
                ivjbtnOK.setActionCommand("OK");
                ivjbtnOK.setPreferredSize(new java.awt.Dimension(73, 25));
                ivjbtnOK.setMinimumSize(new java.awt.Dimension(73, 25));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjbtnOK;
    }

    private static void getBuilderData() {
    }

    private javax.swing.JPanel getJPanel1() {
        if (ivjJPanel1 == null) {
            try {
                ivjJPanel1 = new javax.swing.JPanel();
                ivjJPanel1.setName("JPanel1");
                ivjJPanel1.setLayout(new java.awt.BorderLayout());
                getJPanel1().add(getXMLTreeViewer(), "Center");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJPanel1;
    }

    private javax.swing.JPanel getJPanel2() {
        if (ivjJPanel2 == null) {
            try {
                ivjJPanel2 = new javax.swing.JPanel();
                ivjJPanel2.setName("JPanel2");
                ivjJPanel2.setLayout(new java.awt.GridLayout());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJPanel2;
    }

    private javax.swing.JPanel getJPanel3() {
        if (ivjJPanel3 == null) {
            try {
                ivjJPanel3 = new javax.swing.JPanel();
                ivjJPanel3.setName("JPanel3");
                ivjJPanel3.setPreferredSize(new java.awt.Dimension(150, 30));
                ivjJPanel3.setLayout(new java.awt.FlowLayout());
                ivjJPanel3.setMinimumSize(new java.awt.Dimension(150, 30));
                getJPanel3().add(getbtnOK(), getbtnOK().getName());
                getJPanel3().add(getbtnCancel(), getbtnCancel().getName());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJPanel3;
    }

    /**
 * Insert the method's description here.
 * Creation date: (8/8/2001 12:56:26 PM)
 * @return java.lang.String
 * @param formatted boolean
 */
    public String getXML(boolean formatted) {
        return getXMLTreeViewer().getXML(formatted);
    }

    private XMLTreeViewer getXMLTreeViewer() {
        if (ivjXMLTreeViewer == null) {
            try {
                ivjXMLTreeViewer = new com.apelon.beans.apelxmltree.XMLTreeViewer();
                ivjXMLTreeViewer.setName("XMLTreeViewer");
                ivjXMLTreeViewer.setPreferredSize(new java.awt.Dimension(619, 500));
                ivjXMLTreeViewer.setMinimumSize(new java.awt.Dimension(619, 500));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjXMLTreeViewer;
    }

    /**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
    private void handleException(java.lang.Throwable exception) {
        Categories.uiView().error("Caught Exception", exception);
    }

    private void initConnections() throws java.lang.Exception {
        getbtnOK().addActionListener(ivjEventHandler);
        getbtnCancel().addActionListener(ivjEventHandler);
    }

    private void initialize() {
        try {
            setName("XMLEditorPanel");
            setLayout(new java.awt.GridBagLayout());
            setSize(452, 566);
            java.awt.GridBagConstraints constraintsJPanel1 = new java.awt.GridBagConstraints();
            constraintsJPanel1.gridx = 0;
            constraintsJPanel1.gridy = 0;
            constraintsJPanel1.fill = java.awt.GridBagConstraints.BOTH;
            constraintsJPanel1.weightx = 1.0;
            constraintsJPanel1.weighty = 1.0;
            constraintsJPanel1.insets = new java.awt.Insets(4, 4, 4, 4);
            add(getJPanel1(), constraintsJPanel1);
            java.awt.GridBagConstraints constraintsJPanel2 = new java.awt.GridBagConstraints();
            constraintsJPanel2.gridx = 0;
            constraintsJPanel2.gridy = 2;
            constraintsJPanel2.anchor = java.awt.GridBagConstraints.EAST;
            constraintsJPanel2.weightx = 1.0;
            constraintsJPanel2.weighty = 1.0;
            constraintsJPanel2.insets = new java.awt.Insets(4, 4, 4, 4);
            add(getJPanel2(), constraintsJPanel2);
            java.awt.GridBagConstraints constraintsJPanel3 = new java.awt.GridBagConstraints();
            constraintsJPanel3.gridx = 0;
            constraintsJPanel3.gridy = 1;
            constraintsJPanel3.fill = java.awt.GridBagConstraints.BOTH;
            constraintsJPanel3.weightx = 1.0;
            constraintsJPanel3.weighty = 1.0;
            constraintsJPanel3.insets = new java.awt.Insets(4, 4, 4, 4);
            add(getJPanel3(), constraintsJPanel3);
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
    public static void main(java.lang.String[] args) {
        try {
            javax.swing.JFrame frame = new javax.swing.JFrame();
            XMLEditorPanel aXMLEditorPanel;
            aXMLEditorPanel = new XMLEditorPanel();
            frame.setContentPane(aXMLEditorPanel);
            frame.setSize(aXMLEditorPanel.getSize());
            frame.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }

                ;
            });
            frame.show();
            java.awt.Insets insets = frame.getInsets();
            frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
            frame.setVisible(true);
        } catch (Throwable exception) {
            Categories.uiView().error("Caught Exception", exception);
        }
    }

    /**
   * xmlEditDialog opens a dialog for creating/editing xml.  It returns a string that contains the edited xml.
   * null is returned on failure.
   * @param xmlText Initial Text
   * @param itemName Goes in the title of the dialog
   * @param parent parent component
   * @return a string that contains the edited xml. null is returned on failure
   */
    public static String xmlEditDialog(String xmlText, String itemName, Component parent) {
        return xmlEditDialog(xmlText, itemName, parent, 0);
    }

    /**
   * xmlEditDialog opens a dialog for creating/editing xml.  It returns a string that contains the edited xml.
   * null is returned on failure.
   * @param xmlText Initial Text
   * @param itemName Goes in the title of the dialog
   * @param parent parent component
   * @param maxChars max length for the xml text
   * @return a string that contains the edited xml. null is returned on failure
   */
    public static String xmlEditDialog(String xmlText, String itemName, Component parent, int maxChars) {
        String value = null;
        try {
            XMLEditorPanel xmlep = new XMLEditorPanel(xmlText);
            xmlep.setMaxTextLength(maxChars);
            ApelDlg dlg = new ApelDlg(xmlep, parent, ApelApp.fAppJFrame);
            dlg.setTitle("Enter Value for " + itemName);
            dlg.centerInWindow(null);
            dlg.setResizable(true);
            if (dlg.showApelDlg() == xmlep.OK_CLICKED) {
                value = xmlep.getXML(false);
            }
        } catch (Exception except) {
            XMLTreeViewer.handleXmlError(except);
        }
        return value;
    }

    /**
   * Sets the maximum length of the text. Setting 0 will indicate unlimited text length
   * @param maxChars
   */
    public void setMaxTextLength(int maxChars) {
        maxTextLength = maxChars;
    }

    /**
   * Gets the maximum length for the xml string created in the editor.
   * If there is no limit, it will retrun 0;
   * @return int max length or 0 if there is no limit
   */
    public int getMaxTextLength() {
        return maxTextLength;
    }
}
