package net.adrianromero.tpv.admin;

import java.awt.Component;
import java.util.UUID;
import net.adrianromero.basic.BasicException;
import net.adrianromero.data.user.DirtyManager;
import net.adrianromero.data.user.EditorRecord;
import net.adrianromero.format.Formats;
import net.adrianromero.tpv.forms.AppLocal;

public class RolesView extends javax.swing.JPanel implements EditorRecord {

    private Object m_oId;

    /** Creates new form RolesEditor */
    public RolesView(DirtyManager dirty) {
        initComponents();
        m_jName.getDocument().addDocumentListener(dirty);
        m_jText.getDocument().addDocumentListener(dirty);
        writeValueEOF();
    }

    public void writeValueEOF() {
        m_oId = null;
        m_jName.setText(null);
        m_jText.setText(null);
        m_jName.setEnabled(false);
        m_jText.setEnabled(false);
    }

    public void writeValueInsert() {
        m_oId = null;
        m_jName.setText(null);
        m_jText.setText(null);
        m_jName.setEnabled(true);
        m_jText.setEnabled(true);
    }

    public void writeValueDelete(Object value) {
        Object[] role = (Object[]) value;
        m_oId = role[0];
        m_jName.setText(Formats.STRING.formatValue(role[1]));
        try {
            m_jText.setText(role[2] == null ? null : new String((byte[]) role[2], "UTF-8"));
        } catch (java.io.UnsupportedEncodingException eu) {
            m_jText.setText(null);
        }
        m_jText.setCaretPosition(0);
        m_jName.setEnabled(false);
        m_jText.setEnabled(false);
    }

    public void writeValueEdit(Object value) {
        Object[] role = (Object[]) value;
        m_oId = role[0];
        m_jName.setText(Formats.STRING.formatValue(role[1]));
        try {
            m_jText.setText(role[2] == null ? null : new String((byte[]) role[2], "UTF-8"));
        } catch (java.io.UnsupportedEncodingException eu) {
            m_jText.setText(null);
        }
        m_jText.setCaretPosition(0);
        m_jName.setEnabled(true);
        m_jText.setEnabled(true);
    }

    public Object createValue() throws BasicException {
        Object[] role = new Object[3];
        role[0] = m_oId == null ? UUID.randomUUID().toString() : m_oId;
        role[1] = m_jName.getText();
        try {
            role[2] = m_jText.getText().getBytes("UTF-8");
        } catch (java.io.UnsupportedEncodingException eu) {
            role[2] = null;
        }
        return role;
    }

    public Component getComponent() {
        return this;
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        m_jName = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jText = new javax.swing.JTextArea();
        setLayout(new java.awt.BorderLayout());
        jPanel1.setLayout(null);
        jPanel1.setPreferredSize(new java.awt.Dimension(150, 100));
        jLabel2.setText(AppLocal.getIntString("Label.Name"));
        jPanel1.add(jLabel2);
        jLabel2.setBounds(20, 20, 80, 15);
        jPanel1.add(m_jName);
        m_jName.setBounds(100, 20, 260, 19);
        add(jPanel1, java.awt.BorderLayout.NORTH);
        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        m_jText.setFont(new java.awt.Font("DialogInput", 0, 13));
        jScrollPane1.setViewportView(m_jText);
        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        add(jPanel2, java.awt.BorderLayout.CENTER);
    }

    private javax.swing.JLabel jLabel2;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextField m_jName;

    private javax.swing.JTextArea m_jText;
}
