package com.cosylab.vdct;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Insert the type's description here.
 * Creation date: (13.5.2001 20:29:29)
 * @author 
 */
public class VisualDCTDocument extends javax.swing.JDialog {

    private JPanel buttonPanel = null;

    private javax.swing.JEditorPane ivjEditorPane = null;

    private javax.swing.JPanel ivjJDialogContentPane = null;

    /**
 * VisualDCTDocument constructor comment.
 */
    public VisualDCTDocument() {
        super();
        initialize();
    }

    /**
 * VisualDCTDocument constructor comment.
 * @param owner java.awt.Frame
 */
    public VisualDCTDocument(java.awt.Frame owner, URL document) {
        super(owner);
        initialize();
        if (document == null) {
            setTitle("VisualDCT - [Invalid URL]");
            Console.getInstance().println("Failed to load document.");
        }
        setTitle("VisualDCT - " + document.getFile());
        try {
            getEditorPane().setPage(document);
        } catch (Exception e) {
            Console.getInstance().println("Failed to load: " + document.toString());
            e.printStackTrace();
        }
    }

    private javax.swing.JEditorPane getEditorPane() {
        if (ivjEditorPane == null) {
            try {
                ivjEditorPane = new javax.swing.JEditorPane();
                ivjEditorPane.setName("EditorPane");
                ivjEditorPane.setEditable(false);
                ivjEditorPane.setMargin(new java.awt.Insets(10, 10, 10, 10));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjEditorPane;
    }

    private javax.swing.JPanel getJDialogContentPane() {
        if (ivjJDialogContentPane == null) {
            try {
                ivjJDialogContentPane = new javax.swing.JPanel();
                ivjJDialogContentPane.setName("JDialogContentPane");
                ivjJDialogContentPane.setLayout(new java.awt.BorderLayout());
                getJDialogContentPane().add(getEditorPane(), "Center");
                getJDialogContentPane().add(getButtonPanel(), "South");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJDialogContentPane;
    }

    /**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
    private void handleException(java.lang.Throwable exception) {
        Console.getInstance().println("--------- UNCAUGHT EXCEPTION ---------");
        Console.getInstance().println(exception);
        exception.printStackTrace(System.out);
    }

    private void initialize() {
        try {
            setName("VisualDCTDocument");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(681, 622);
            setContentPane(getJDialogContentPane());
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            JButton closeButton = new JButton();
            closeButton.setText("Close");
            closeButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    setVisible(false);
                }
            });
            buttonPanel.add(closeButton);
        }
        return buttonPanel;
    }

    /**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
    public static void main(java.lang.String[] args) {
        try {
            VisualDCTDocument aVisualDCTDocument;
            aVisualDCTDocument = new VisualDCTDocument();
            aVisualDCTDocument.setModal(true);
            aVisualDCTDocument.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }

                ;
            });
            aVisualDCTDocument.setVisible(true);
            java.awt.Insets insets = aVisualDCTDocument.getInsets();
            aVisualDCTDocument.setSize(aVisualDCTDocument.getWidth() + insets.left + insets.right, aVisualDCTDocument.getHeight() + insets.top + insets.bottom);
            aVisualDCTDocument.setVisible(true);
        } catch (Throwable exception) {
            System.err.println("Exception occurred in main() of javax.swing.JDialog");
            exception.printStackTrace(System.out);
        }
    }
}
