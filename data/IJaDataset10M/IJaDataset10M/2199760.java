package net.adrianromero.sql;

import java.awt.CardLayout;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import net.adrianromero.data.loader.BaseSentence;
import net.adrianromero.basic.BasicException;
import net.adrianromero.data.loader.DataField;
import net.adrianromero.data.loader.DataResultSet;
import net.adrianromero.tpv.forms.JPanelView;
import net.adrianromero.tpv.panels.*;

public class JPanelSQLResult extends javax.swing.JPanel {

    private static final Icon ICO_NULL = new javax.swing.ImageIcon(JPanelView.class.getResource("/net/adrianromero/images/null.png"));

    private static final Icon ICO_TRUE = new javax.swing.ImageIcon(JPanelView.class.getResource("/net/adrianromero/images/true.png"));

    private static final Icon ICO_FALSE = new javax.swing.ImageIcon(JPanelView.class.getResource("/net/adrianromero/images/false.png"));

    private static final Icon ICO_BYTEA = new javax.swing.ImageIcon(JPanelView.class.getResource("/net/adrianromero/images/bytea.png"));

    /** Creates new form JPanelSQLResult */
    public JPanelSQLResult() {
        initComponents();
        m_jTableResult.setDefaultRenderer(java.lang.String.class, new StringRenderer());
        m_jTableResult.setDefaultRenderer(java.lang.Double.class, new NumberRenderer());
        m_jTableResult.setDefaultRenderer(java.lang.Integer.class, new NumberRenderer());
        m_jTableResult.setDefaultRenderer(java.util.Date.class, new DateRenderer());
        m_jTableResult.setDefaultRenderer(java.lang.Boolean.class, new BooleanRenderer());
        m_jTableResult.setDefaultRenderer(byte[].class, new ByteaRenderer());
    }

    public boolean executeSentence(BaseSentence sent) {
        return executeSentence(sent, null);
    }

    public boolean executeSentence(BaseSentence sent, Object params) {
        CardLayout cl = (CardLayout) (getLayout());
        try {
            DataResultSet rs = sent.openExec(params);
            if (rs.updateCount() < 0) {
                cl.show(this, "resultset");
                DataField[] df = rs.getDataField();
                SQLTableModel sqlresult = new SQLTableModel(df);
                while (rs.next()) {
                    sqlresult.addRow(rs);
                }
                rs.close();
                sent.closeExec();
                m_jTableResult.setModel(sqlresult);
            } else {
                cl.show(this, "updatecount");
                m_txtResulltText.setText("Update count: " + Integer.toString(rs.updateCount()));
                m_txtResulltText.setCaretPosition(0);
            }
            return true;
        } catch (BasicException e) {
            cl.show(this, "updatecount");
            StringWriter w = new StringWriter();
            e.printStackTrace(new PrintWriter(w));
            m_txtResulltText.setText(w.toString());
            m_txtResulltText.setCaretPosition(0);
            return false;
        }
    }

    static class StringRenderer extends DefaultTableCellRenderer.UIResource {

        public StringRenderer() {
            super();
        }

        public void setValue(Object value) {
            if (value == null) {
                setIcon(ICO_NULL);
                setText(null);
            } else {
                setIcon(null);
                setText(value.toString());
            }
        }
    }

    static class NumberRenderer extends DefaultTableCellRenderer.UIResource {

        NumberFormat formatter = null;

        public NumberRenderer() {
            super();
            setHorizontalAlignment(JLabel.RIGHT);
        }

        public void setValue(Object value) {
            if (formatter == null) {
                formatter = NumberFormat.getInstance();
            }
            if (value == null) {
                setIcon(ICO_NULL);
                setText(null);
            } else {
                setIcon(null);
                setText(formatter.format(value));
            }
        }
    }

    static class DateRenderer extends DefaultTableCellRenderer.UIResource {

        DateFormat formatter;

        public DateRenderer() {
            super();
        }

        public void setValue(Object value) {
            if (formatter == null) {
                formatter = DateFormat.getDateInstance();
            }
            if (value == null) {
                setIcon(ICO_NULL);
                setText(null);
            } else {
                setIcon(null);
                setText(formatter.format(value));
            }
        }
    }

    static class IconRenderer extends DefaultTableCellRenderer.UIResource {

        public IconRenderer() {
            super();
            setHorizontalAlignment(JLabel.CENTER);
        }

        public void setValue(Object value) {
            setIcon((value instanceof Icon) ? (Icon) value : null);
        }
    }

    static class ByteaRenderer extends DefaultTableCellRenderer.UIResource {

        public ByteaRenderer() {
            super();
        }

        public void setValue(Object value) {
            if (value == null) {
                setIcon(ICO_NULL);
            } else {
                setIcon(ICO_BYTEA);
            }
        }
    }

    static class BooleanRenderer extends DefaultTableCellRenderer.UIResource {

        public BooleanRenderer() {
            super();
        }

        public void setValue(Object value) {
            if (value == null) {
                setIcon(ICO_NULL);
            } else {
                setIcon(((Boolean) value).booleanValue() ? ICO_TRUE : ICO_FALSE);
            }
        }
    }

    private void initComponents() {
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jTableResult = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        m_txtResulltText = new javax.swing.JTextArea();
        setLayout(new java.awt.CardLayout());
        add(jPanel3, "card4");
        jPanel1.setLayout(new java.awt.BorderLayout());
        jToolBar1.setFloatable(false);
        jButton1.setText("jButton1");
        jToolBar1.add(jButton1);
        jButton2.setText("jButton2");
        jToolBar1.add(jButton2);
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jToolBar1.add(jSeparator1);
        jButton3.setText("jButton3");
        jToolBar1.add(jButton3);
        jButton4.setText("jButton4");
        jToolBar1.add(jButton4);
        jPanel1.add(jToolBar1, java.awt.BorderLayout.NORTH);
        jScrollPane1.setViewportView(m_jTableResult);
        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        add(jPanel1, "resultset");
        jPanel2.setLayout(new java.awt.BorderLayout());
        m_txtResulltText.setEditable(false);
        jScrollPane2.setViewportView(m_txtResulltText);
        jPanel2.add(jScrollPane2, java.awt.BorderLayout.CENTER);
        add(jPanel2, "updatecount");
    }

    javax.swing.JButton jButton1;

    javax.swing.JButton jButton2;

    javax.swing.JButton jButton3;

    javax.swing.JButton jButton4;

    javax.swing.JPanel jPanel1;

    javax.swing.JPanel jPanel2;

    javax.swing.JPanel jPanel3;

    javax.swing.JScrollPane jScrollPane1;

    javax.swing.JScrollPane jScrollPane2;

    javax.swing.JSeparator jSeparator1;

    javax.swing.JToolBar jToolBar1;

    javax.swing.JTable m_jTableResult;

    javax.swing.JTextArea m_txtResulltText;
}
