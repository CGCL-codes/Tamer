package monocaffe;

import org.jdesktop.application.Action;

/**
 *
 * @author  Alejandro Ayuso
 */
public class ErrorsDialog extends javax.swing.JDialog {

    /** Creates new form ErrorsDialog */
    public ErrorsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    private void initComponents() {
        ok_button = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        label_title = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        label_dialog = new javax.swing.JTextArea();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(monocaffe.MonocaffeApp.class).getContext().getResourceMap(ErrorsDialog.class);
        setTitle(resourceMap.getString("Form.title"));
        setName("Form");
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(monocaffe.MonocaffeApp.class).getContext().getActionMap(ErrorsDialog.class, this);
        ok_button.setAction(actionMap.get("onOkButtonClicked"));
        ok_button.setName("ok_button");
        jLabel1.setName("jLabel1");
        label_title.setFont(resourceMap.getFont("label_title.font"));
        label_title.setText(resourceMap.getString("label_title.text"));
        label_title.setName("label_title");
        jLabel2.setIcon(resourceMap.getIcon("jLabel2.icon"));
        jLabel2.setText(resourceMap.getString("jLabel2.text"));
        jLabel2.setName("jLabel2");
        jScrollPane1.setName("jScrollPane1");
        label_dialog.setColumns(20);
        label_dialog.setEditable(false);
        label_dialog.setLineWrap(true);
        label_dialog.setRows(5);
        label_dialog.setBorder(null);
        label_dialog.setName("label_dialog");
        jScrollPane1.setViewportView(label_dialog);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addGroup(layout.createSequentialGroup().addComponent(jLabel2).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(label_title, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(ok_button, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jLabel2).addComponent(jLabel1).addGroup(layout.createSequentialGroup().addComponent(label_title).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(ok_button).addContainerGap()));
        pack();
    }

    @Action
    public void onOkButtonClicked() {
        this.setVisible(false);
        this.dispose();
    }

    public void setLabels(String title, String message) {
        this.label_title.setText(title);
        this.label_dialog.setText(message);
    }

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextArea label_dialog;

    private javax.swing.JLabel label_title;

    private javax.swing.JButton ok_button;
}
