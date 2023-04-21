package org.lindenb.lang;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import org.lindenb.swing.table.AbstractGenericTableModel;
import org.lindenb.util.Compilation;

/**
 * Panel used to display the content of an exception
 * @author pierre
 *
 */
public class ThrowablePane extends JPanel {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /** the error to be displayed */
    private Throwable throwable;

    /** the error to be displayed */
    private StackTraceElement[] stackTrace;

    /** a table to print the stack trace */
    private AbstractGenericTableModel<StackTraceElement> tableModel;

    /**
     * Constructor with a throwable
     * @param throwable
     */
    public ThrowablePane(Throwable throwable, String message) {
        super(new BorderLayout());
        if (message == null) message = throwable.getLocalizedMessage();
        if (message == null) message = throwable.getMessage();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(new Dimension(screen.width / 2, screen.height / 2));
        setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        this.throwable = throwable;
        this.stackTrace = throwable.getStackTrace();
        this.tableModel = new AbstractGenericTableModel<StackTraceElement>() {

            private static final long serialVersionUID = 1L;

            public int getColumnCount() {
                return 4;
            }

            @Override
            public String getColumnName(int columnIndex) {
                switch(columnIndex) {
                    case 0:
                        return "Class";
                    case 1:
                        return "Method";
                    case 2:
                        return "File";
                    case 3:
                        return "Line";
                }
                return null;
            }

            public int getRowCount() {
                return ThrowablePane.this.stackTrace.length;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch(columnIndex) {
                    case 0:
                        return String.class;
                    case 1:
                        return String.class;
                    case 2:
                        return String.class;
                    case 3:
                        return Integer.class;
                }
                return null;
            }

            @Override
            public StackTraceElement elementAt(int rowIndex) {
                return ThrowablePane.this.stackTrace[rowIndex];
            }

            @Override
            public Object getValueOf(StackTraceElement e, int columnIndex) {
                switch(columnIndex) {
                    case 0:
                        return e.getClassName();
                    case 1:
                        return e.getMethodName();
                    case 2:
                        return e.getFileName();
                    case 3:
                        return e.getLineNumber();
                }
                return null;
            }
        };
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.SCROLL_TAB_LAYOUT);
        this.add(tabbedPane, BorderLayout.CENTER);
        JTextField tf = new JTextField(throwable.getClass() + " : " + message);
        tf.setToolTipText(tf.getText());
        tf.setEditable(false);
        tf.setCaretPosition(0);
        tf.setForeground(Color.RED);
        tf.setFont(new Font("Dialog", Font.PLAIN, 24));
        tabbedPane.addTab("Message", tf);
        JPanel pane = new JPanel(new GridLayout(0, 1));
        tabbedPane.addTab("Trace", pane);
        StringWriter statckTrace = new StringWriter();
        throwable.printStackTrace(new PrintWriter(statckTrace));
        JTextArea msg = new JTextArea(throwable.getClass().getName() + "\n" + Compilation.getLabel() + "\n" + statckTrace.toString());
        msg.setEditable(false);
        msg.setBackground(Color.WHITE);
        msg.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(msg);
        scroll.setBorder(BorderFactory.createTitledBorder("An exception of type " + throwable.getClass().getName() + " has occured"));
        scroll.setPreferredSize(new Dimension(200, 200));
        pane.add(scroll);
        JTable table = new JTable(this.tableModel);
        DefaultTableCellRenderer render = new DefaultTableCellRenderer() {

            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Object pack = table.getModel().getValueAt(row, 0);
                if (!isSelected && pack != null && !(pack.toString().startsWith("java") || pack.toString().startsWith("sun"))) {
                    setBackground(Color.GREEN);
                } else if (isSelected) {
                    setBackground(Color.RED);
                } else {
                    setBackground(Color.WHITE);
                }
                return c;
            }
        };
        render.setOpaque(true);
        for (int i = 0; i < table.getColumnModel().getColumnCount(); ++i) {
            table.getColumnModel().getColumn(i).setCellRenderer(render);
        }
        table.setBackground(Color.WHITE);
        scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Stack Trace"));
        pane.add(scroll);
        for (StackTraceElement ste : this.throwable.getStackTrace()) {
            String classname = ste.getClassName();
            if (classname == null) continue;
            classname.replace('.', '/');
            classname = "/" + classname + ".java";
            try {
                InputStream in = getClass().getResourceAsStream(classname);
                if (in == null) continue;
                StringBuilder javaFile = new StringBuilder();
                BufferedReader bin = new BufferedReader(new InputStreamReader(in));
                String line;
                int nLine = 0;
                while ((line = bin.readLine()) != null) {
                    javaFile.append(++nLine).append('\t').append(line).append("\n");
                }
                JTextArea area = new JTextArea(javaFile.toString());
                JPanel srcPane = new JPanel(new BorderLayout());
                srcPane.setBorder(BorderFactory.createTitledBorder("Source"));
                srcPane.add(new JScrollPane(area));
                tabbedPane.addTab("Source", srcPane);
            } catch (Throwable e) {
                break;
            }
            break;
        }
        setBackground(Color.RED);
        pane.setOpaque(true);
        pane.setBackground(Color.RED);
    }

    /** display an alert via OptionPane.showMessageDialog */
    public static void show(Component owner, String message, Throwable t) {
        try {
            JOptionPane.showMessageDialog(owner, new ThrowablePane(t, message), "Error", JOptionPane.ERROR_MESSAGE, null);
        } catch (HeadlessException e) {
            t.printStackTrace(System.err);
        }
    }

    /** display an alert via OptionPane.showMessageDialog */
    public static void show(Component owner, Throwable t) {
        show(owner, null, t);
    }
}
