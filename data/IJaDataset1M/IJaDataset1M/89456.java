package net.xmlmiddleware.gui.utils;

/**
 * Insert the type's description here.
 * Creation date: (08/11/01 15:04:02)
 * @author: Adam Flinton
 */
public class MessageDialog extends javax.swing.JDialog {

    private javax.swing.JPanel ivjJDialogContentPane = null;

    private javax.swing.JTextArea ivjJTextArea1 = null;

    IvjEventHandler ivjEventHandler = new IvjEventHandler();

    private javax.swing.JButton ivjJButton1 = null;

    private javax.swing.JPanel ivjJPanel1 = null;

    private javax.swing.JPanel ivjJPanel2 = null;

    private WindowPositioner ivjWindowPositioner1 = null;

    class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.WindowListener {

        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == MessageDialog.this.getJButton1()) connEtoM1(e);
        }

        ;

        public void windowActivated(java.awt.event.WindowEvent e) {
        }

        ;

        public void windowClosed(java.awt.event.WindowEvent e) {
        }

        ;

        public void windowClosing(java.awt.event.WindowEvent e) {
        }

        ;

        public void windowDeactivated(java.awt.event.WindowEvent e) {
        }

        ;

        public void windowDeiconified(java.awt.event.WindowEvent e) {
        }

        ;

        public void windowIconified(java.awt.event.WindowEvent e) {
        }

        ;

        public void windowOpened(java.awt.event.WindowEvent e) {
            if (e.getSource() == MessageDialog.this) connEtoM2(e);
        }

        ;
    }

    ;

    /**
 * TypeNotFound constructor comment.
 */
    public MessageDialog() {
        super();
        initialize();
    }

    /**
 * TypeNotFound constructor comment.
 * @param owner java.awt.Dialog
 */
    public MessageDialog(java.awt.Dialog owner) {
        super(owner);
        initialize();
    }

    /**
 * TypeNotFound constructor comment.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 */
    public MessageDialog(java.awt.Dialog owner, String title) {
        super(owner, title);
        initialize();
    }

    /**
 * TypeNotFound constructor comment.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 * @param modal boolean
 */
    public MessageDialog(java.awt.Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
 * TypeNotFound constructor comment.
 * @param owner java.awt.Dialog
 * @param modal boolean
 */
    public MessageDialog(java.awt.Dialog owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
 * TypeNotFound constructor comment.
 * @param owner java.awt.Frame
 */
    public MessageDialog(java.awt.Frame owner) {
        super(owner);
        initialize();
    }

    /**
 * TypeNotFound constructor comment.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
    public MessageDialog(java.awt.Frame owner, String title) {
        super(owner, title);
        initialize();
    }

    /**
 * TypeNotFound constructor comment.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 * @param modal boolean
 */
    public MessageDialog(java.awt.Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
 * TypeNotFound constructor comment.
 * @param owner java.awt.Frame
 * @param modal boolean
 */
    public MessageDialog(java.awt.Frame owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    private void connEtoM1(java.awt.event.ActionEvent arg1) {
        try {
            this.dispose();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private void connEtoM2(java.awt.event.WindowEvent arg1) {
        try {
            net.xmlmiddleware.gui.utils.WindowPositioner.positionWindowOnScreen(this);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private javax.swing.JButton getJButton1() {
        if (ivjJButton1 == null) {
            try {
                ivjJButton1 = new javax.swing.JButton();
                ivjJButton1.setName("JButton1");
                ivjJButton1.setText("OK");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJButton1;
    }

    private javax.swing.JPanel getJDialogContentPane() {
        if (ivjJDialogContentPane == null) {
            try {
                ivjJDialogContentPane = new javax.swing.JPanel();
                ivjJDialogContentPane.setName("JDialogContentPane");
                ivjJDialogContentPane.setLayout(new java.awt.BorderLayout());
                getJDialogContentPane().add(getJPanel1(), "Center");
                getJDialogContentPane().add(getJPanel2(), "South");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJDialogContentPane;
    }

    private javax.swing.JPanel getJPanel1() {
        if (ivjJPanel1 == null) {
            try {
                ivjJPanel1 = new javax.swing.JPanel();
                ivjJPanel1.setName("JPanel1");
                ivjJPanel1.setLayout(new java.awt.BorderLayout());
                getJPanel1().add(getJTextArea1(), "Center");
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
                ivjJPanel2.setPreferredSize(new java.awt.Dimension(426, 24));
                ivjJPanel2.setLayout(new java.awt.BorderLayout());
                getJPanel2().add(getJButton1(), "Center");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJPanel2;
    }

    private javax.swing.JTextArea getJTextArea1() {
        if (ivjJTextArea1 == null) {
            try {
                ivjJTextArea1 = new javax.swing.JTextArea();
                ivjJTextArea1.setName("JTextArea1");
                ivjJTextArea1.setLineWrap(true);
                ivjJTextArea1.setWrapStyleWord(true);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJTextArea1;
    }

    private WindowPositioner getWindowPositioner1() {
        if (ivjWindowPositioner1 == null) {
            try {
                ivjWindowPositioner1 = new net.xmlmiddleware.gui.utils.WindowPositioner();
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjWindowPositioner1;
    }

    /**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
    private void handleException(java.lang.Throwable exception) {
    }

    private void initConnections() throws java.lang.Exception {
        getJButton1().addActionListener(ivjEventHandler);
        this.addWindowListener(ivjEventHandler);
    }

    private void initialize() {
        try {
            setName("TypeNotFound");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(426, 240);
            setTitle("");
            setContentPane(getJDialogContentPane());
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
            MessageDialog aMessageDialog;
            aMessageDialog = new MessageDialog();
            aMessageDialog.setModal(true);
            aMessageDialog.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }

                ;
            });
            aMessageDialog.show();
            java.awt.Insets insets = aMessageDialog.getInsets();
            aMessageDialog.setSize(aMessageDialog.getWidth() + insets.left + insets.right, aMessageDialog.getHeight() + insets.top + insets.bottom);
            aMessageDialog.setVisible(true);
        } catch (Throwable exception) {
            System.err.println("Exception occurred in main() of javax.swing.JDialog");
            exception.printStackTrace(System.out);
        }
    }

    /**
 * Insert the method's description here.
 * Creation date: (08/11/01 15:14:17)
 * @param Text java.lang.String
 */
    public void setContentText(String Text) {
        getJTextArea1().setText(Text);
    }
}
