package net.xmlmiddleware.xmldbms.v1.gui;

/**
 * Insert the type's description here.
 * Creation date: (17/04/2002 09:33:13)
 * @author: Adam Flinton
 */
public class Test extends javax.swing.JFrame {

    IvjEventHandler ivjEventHandler = new IvjEventHandler();

    private javax.swing.JButton ivjJButton1 = null;

    private javax.swing.JButton ivjJButton2 = null;

    private javax.swing.JPanel ivjJFrameContentPane = null;

    private javax.swing.JPanel ivjJPanel1 = null;

    private javax.swing.JPanel ivjJPanel2 = null;

    private net.xmlmiddleware.gui.utils.WindowPositioner ivjWindowPositioner1 = null;

    private javax.swing.JLabel ivjJLabel1 = null;

    private javax.swing.JLabel ivjJLabel2 = null;

    private javax.swing.JLabel ivjJLabel3 = null;

    private javax.swing.JPanel ivjJPanel3 = null;

    private javax.swing.JPanel ivjJPanel4 = null;

    private javax.swing.JPanel ivjJPanel5 = null;

    class IvjEventHandler implements java.awt.event.WindowListener {

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
            if (e.getSource() == Test.this) connEtoM1(e);
        }

        ;
    }

    ;

    private javax.swing.JComboBox ivjJComboBox1 = null;

    /**
 * Test constructor comment.
 */
    public Test() {
        super();
        initialize();
    }

    /**
 * Test constructor comment.
 * @param title java.lang.String
 */
    public Test(String title) {
        super(title);
    }

    private void connEtoM1(java.awt.event.WindowEvent arg1) {
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
                ivjJButton1.setText("JButton1");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJButton1;
    }

    private javax.swing.JButton getJButton2() {
        if (ivjJButton2 == null) {
            try {
                ivjJButton2 = new javax.swing.JButton();
                ivjJButton2.setName("JButton2");
                ivjJButton2.setText("JButton2");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJButton2;
    }

    private javax.swing.JComboBox getJComboBox1() {
        if (ivjJComboBox1 == null) {
            try {
                ivjJComboBox1 = new javax.swing.JComboBox();
                ivjJComboBox1.setName("JComboBox1");
                ivjJComboBox1.setBounds(10, 32, 164, 23);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJComboBox1;
    }

    private javax.swing.JPanel getJFrameContentPane() {
        if (ivjJFrameContentPane == null) {
            try {
                ivjJFrameContentPane = new javax.swing.JPanel();
                ivjJFrameContentPane.setName("JFrameContentPane");
                ivjJFrameContentPane.setLayout(new java.awt.BorderLayout());
                getJFrameContentPane().add(getJPanel1(), "Center");
                getJFrameContentPane().add(getJPanel2(), "South");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJFrameContentPane;
    }

    private javax.swing.JLabel getJLabel1() {
        if (ivjJLabel1 == null) {
            try {
                ivjJLabel1 = new javax.swing.JLabel();
                ivjJLabel1.setName("JLabel1");
                ivjJLabel1.setText("Source");
                ivjJLabel1.setBounds(12, 12, 45, 14);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJLabel1;
    }

    private javax.swing.JLabel getJLabel2() {
        if (ivjJLabel2 == null) {
            try {
                ivjJLabel2 = new javax.swing.JLabel();
                ivjJLabel2.setName("JLabel2");
                ivjJLabel2.setText("Intermediate Steps");
                ivjJLabel2.setBounds(7, 8, 116, 14);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJLabel2;
    }

    private javax.swing.JLabel getJLabel3() {
        if (ivjJLabel3 == null) {
            try {
                ivjJLabel3 = new javax.swing.JLabel();
                ivjJLabel3.setName("JLabel3");
                ivjJLabel3.setText("Sink");
                ivjJLabel3.setBounds(15, 14, 45, 14);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJLabel3;
    }

    private javax.swing.JPanel getJPanel1() {
        if (ivjJPanel1 == null) {
            try {
                ivjJPanel1 = new javax.swing.JPanel();
                ivjJPanel1.setName("JPanel1");
                ivjJPanel1.setLayout(null);
                getJPanel1().add(getJPanel3(), getJPanel3().getName());
                getJPanel1().add(getJPanel4(), getJPanel4().getName());
                getJPanel1().add(getJPanel5(), getJPanel5().getName());
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
                ivjJPanel2.setLayout(new java.awt.FlowLayout());
                getJPanel2().add(getJButton1(), getJButton1().getName());
                getJPanel2().add(getJButton2(), getJButton2().getName());
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
                ivjJPanel3.setBorder(new javax.swing.border.EtchedBorder());
                ivjJPanel3.setLayout(null);
                ivjJPanel3.setBounds(6, 5, 585, 113);
                getJPanel3().add(getJLabel1(), getJLabel1().getName());
                getJPanel3().add(getJComboBox1(), getJComboBox1().getName());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJPanel3;
    }

    private javax.swing.JPanel getJPanel4() {
        if (ivjJPanel4 == null) {
            try {
                ivjJPanel4 = new javax.swing.JPanel();
                ivjJPanel4.setName("JPanel4");
                ivjJPanel4.setBorder(new javax.swing.border.EtchedBorder());
                ivjJPanel4.setLayout(null);
                ivjJPanel4.setBounds(6, 120, 581, 180);
                getJPanel4().add(getJLabel2(), getJLabel2().getName());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJPanel4;
    }

    private javax.swing.JPanel getJPanel5() {
        if (ivjJPanel5 == null) {
            try {
                ivjJPanel5 = new javax.swing.JPanel();
                ivjJPanel5.setName("JPanel5");
                ivjJPanel5.setBorder(new javax.swing.border.EtchedBorder());
                ivjJPanel5.setLayout(null);
                ivjJPanel5.setBounds(6, 301, 579, 128);
                getJPanel5().add(getJLabel3(), getJLabel3().getName());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJPanel5;
    }

    private net.xmlmiddleware.gui.utils.WindowPositioner getWindowPositioner1() {
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
        this.addWindowListener(ivjEventHandler);
    }

    private void initialize() {
        try {
            setName("Test");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(593, 469);
            setTitle("XML-DBMS Version 1 Test Suite");
            setContentPane(getJFrameContentPane());
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
            Test aTest;
            aTest = new Test();
            aTest.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }

                ;
            });
            aTest.show();
            java.awt.Insets insets = aTest.getInsets();
            aTest.setSize(aTest.getWidth() + insets.left + insets.right, aTest.getHeight() + insets.top + insets.bottom);
            aTest.setVisible(true);
        } catch (Throwable exception) {
            System.err.println("Exception occurred in main() of javax.swing.JFrame");
            exception.printStackTrace(System.out);
        }
    }
}
