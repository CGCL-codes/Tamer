package GoogleDocs;

import Core.AlertFrame;
import Core.Files;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 *
 * @author H3R3T1C
 */
public class exportDoc extends javax.swing.JFrame {

    /** Creates new form exportDoc */
    private exportDoc window;

    DocFeed feed;

    public exportDoc(DocFeed feed, int[] a, DefaultListModel model) {
        this.feed = feed;
        initComponents();
        window = this;
        jList6.setModel(model);
        jLabel58.setText("Documents Format (" + a[0] + ")");
        jLabel59.setText("Presentation Format (" + a[1] + ")");
        jLabel60.setText("Spreadsheet  Format (" + a[2] + ")");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jScrollPane17 = new javax.swing.JScrollPane();
        jList6 = new javax.swing.JList();
        jButton45 = new javax.swing.JButton();
        jButton44 = new javax.swing.JButton();
        jLabel60 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox();
        jComboBox8 = new javax.swing.JComboBox();
        jComboBox7 = new javax.swing.JComboBox();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Export Documens");
        setResizable(false);
        jScrollPane17.setViewportView(jList6);
        jButton45.setText("Export");
        jButton45.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton45ActionPerformed(evt);
            }
        });
        jButton44.setText("Cancel");
        jButton44.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton44ActionPerformed(evt);
            }
        });
        jLabel60.setText("Spreadsheet  Format (0)");
        jLabel59.setText("Presentation Format (0)");
        jLabel58.setText("Documents Format (0)");
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Microsoft Word", "HTML", "PDF", "OpenDocument", "Rich Text (RTF)", "Plain Text" }));
        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Microsoft Excel", "Open Office Spreadsheet", "PDF" }));
        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PowerPoint", "PDF", "Plain Text" }));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(jButton45, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 136, Short.MAX_VALUE).addComponent(jButton44, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel60, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel59, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel58, javax.swing.GroupLayout.Alignment.TRAILING)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jComboBox5, 0, 179, Short.MAX_VALUE).addComponent(jComboBox8, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jComboBox7, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel58).addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel59).addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel60).addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton45).addComponent(jButton44)).addContainerGap()));
        pack();
    }

    private void jButton45ActionPerformed(java.awt.event.ActionEvent evt) {
        new Thread(new Runnable() {

            public void run() {
                window.hide();
                List<gDoc> docs = new ArrayList();
                int[] types = new int[3];
                jButton45.setEnabled(false);
                jButton45.setText("Exporting");
                for (int i = 0; i < jList6.getModel().getSize(); i++) {
                    gDoc doc = (gDoc) jList6.getModel().getElementAt(i);
                    docs.add(doc);
                }
                types[0] = jComboBox5.getSelectedIndex();
                types[1] = jComboBox7.getSelectedIndex();
                types[2] = jComboBox8.getSelectedIndex();
                feed.exportDocs(docs, Files.saveFileDir(), types);
                new AlertFrame("Google Docs", "All documents have finished exporting!", "Number of exported Docs: " + jList6.getModel().getSize());
                window.dispose();
            }
        }).start();
    }

    private void jButton44ActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private javax.swing.JButton jButton44;

    private javax.swing.JButton jButton45;

    private javax.swing.JComboBox jComboBox5;

    private javax.swing.JComboBox jComboBox7;

    private javax.swing.JComboBox jComboBox8;

    private javax.swing.JLabel jLabel58;

    private javax.swing.JLabel jLabel59;

    private javax.swing.JLabel jLabel60;

    private javax.swing.JList jList6;

    private javax.swing.JScrollPane jScrollPane17;
}
