package newgen.presentation.cataloguing;

import newgen.presentation.NewGenMain;

/**
 *
 * @author  root
 */
public class SingleRecordView extends javax.swing.JDialog {

    /** Creates new form SingleRecordView */
    public SingleRecordView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setSize(700, 500);
        setLocation(NewGenMain.getAppletInstance().getLocation(600, 400));
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        bnClose = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        epTextFormatFull = new javax.swing.JEditorPane();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CatalogueRecordView"));
        setModal(true);
        bnClose.setMnemonic('e');
        bnClose.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
        bnClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnCloseActionPerformed(evt);
            }
        });
        jPanel1.add(bnClose);
        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);
        jPanel2.setLayout(new java.awt.BorderLayout());
        epTextFormatFull.setEditable(false);
        epTextFormatFull.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {

            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                epTextFormatFullHyperlinkUpdate(evt);
            }
        });
        jScrollPane3.setViewportView(epTextFormatFull);
        jPanel2.add(jScrollPane3, java.awt.BorderLayout.CENTER);
        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);
        pack();
    }

    private void epTextFormatFullHyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
        if (evt.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED) new ViewHoldingsEditPane(evt.getURL());
    }

    private void bnCloseActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new SingleRecordView(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }

    private javax.swing.JButton bnClose;

    private javax.swing.JEditorPane epTextFormatFull;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane3;

    private String[] catalogueRecordDetails = null;

    public String[] getCatalogueRecordDetails() {
        return catalogueRecordDetails;
    }

    public void setCatalogueRecordDetails(String[] catalogueRecordDetails) {
        this.catalogueRecordDetails = catalogueRecordDetails;
        epTextFormatFull.setText("");
        epTextFormatFull.setContentType("text/html");
        ISBDView isview = ISBDView.getInstance();
        isview.setCatRecIdAndOwnLibId(catalogueRecordDetails[0], catalogueRecordDetails[1]);
        epTextFormatFull.setText(isview.getFullRecordView(catalogueRecordDetails[2]));
        epTextFormatFull.setCaretPosition(0);
    }
}
