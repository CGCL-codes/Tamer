package zen;

import java.awt.Color;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * 
 * @author  eduardo-costa
 */
public class LabelsFrame extends javax.swing.JFrame {

    private static final String FOLDER = "c:\\wamp\\www\\zen\\includes\\languages";

    private static final Pattern DEFINE = Pattern.compile("\\s*define\\s*\\(\\s*'([^']+)'\\s*,\\s*'([^']*)'\\s*\\)\\s*;.*");

    private Map<String, Label> labels = new HashMap<String, Label>();

    private Map<File, Map<Integer, Label>> fileLabels = new HashMap<File, Map<Integer, Label>>();

    private Map<File, File> dirtyFiles = new HashMap<File, File>();

    /** Creates new form LabelsFrame */
    public LabelsFrame() {
        initComponents();
    }

    private void count() {
        int trad = 0;
        int ntrad = 0;
        int miss = 0;
        int total = 0;
        for (Label l : labels.values()) {
            String o = l.getOriginal();
            String t = l.getTranslated();
            total++;
            if ((t == null) || "".equals(t)) {
                if (!"".equals(o)) {
                    miss++;
                }
            } else if (o.equals(t)) {
                ntrad++;
            } else {
                trad++;
            }
        }
        lblStats.setText(String.format("Total de linhas: %d - Traduzidas: %d - Faltam: %d - Novas: %d", total, trad, ntrad, miss));
    }

    private void save() throws IOException {
        for (Map.Entry<File, File> e : dirtyFiles.entrySet()) {
            BufferedReader in = null;
            BufferedWriter out = null;
            try {
                in = new BufferedReader(new FileReader(e.getKey()));
                out = new BufferedWriter(new FileWriter(e.getValue()));
                String line;
                int count = 0;
                Map<Integer, Label> fl = fileLabels.get(e.getKey());
                while ((line = in.readLine()) != null) {
                    count++;
                    Label l = fl.get(count);
                    if (l == null) {
                        out.write(line);
                    } else {
                        out.write(String.format("define('%s', '%s');\n", l.getName(), l.getTranslated()));
                    }
                }
            } finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
        }
    }

    private void load(File en, File pt) throws IOException {
        if (en.isDirectory()) {
            for (File f : en.listFiles()) {
                load(f, new File(pt, f.getName()));
            }
        } else {
            fileLabels.put(en, new HashMap<Integer, Label>());
            BufferedReader in = new BufferedReader(new FileReader(en));
            try {
                String line;
                int count = 0;
                while ((line = in.readLine()) != null) {
                    count++;
                    Matcher m = DEFINE.matcher(line);
                    if (m.matches()) {
                        Label l = new Label();
                        l.setFileOriginal(en);
                        l.setFileTraduzido(pt);
                        l.setLine(count);
                        l.setName(m.group(1));
                        l.setOriginal(m.group(2));
                        labels.put(l.getName(), l);
                        fileLabels.get(en).put(count, l);
                    }
                }
            } finally {
                in.close();
            }
            if (!pt.exists()) {
                pt.createNewFile();
            }
            in = new BufferedReader(new FileReader(pt));
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    Matcher m = DEFINE.matcher(line);
                    if (m.matches()) {
                        Label l = labels.get(m.group(1));
                        if (l != null) {
                            l.setTranslated(m.group(2));
                        }
                    }
                }
            } finally {
                in.close();
            }
        }
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLabels = new ToolTipTable();
        btnLoad = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        lblStats = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        tblLabels.setAutoCreateRowSorter(true);
        jScrollPane1.setViewportView(tblLabels);
        btnLoad.setText("Load...");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });
        btnSave.setText("Save");
        btnSave.setEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        lblStats.setText("Clique em \"Load\" para carregar os arquivos");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(lblStats).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 342, Short.MAX_VALUE).addComponent(btnSave).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnLoad))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnLoad).addComponent(btnSave).addComponent(lblStats)).addContainerGap()));
        pack();
    }

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {
        labels.clear();
        fileLabels.clear();
        dirtyFiles.clear();
        btnSave.setEnabled(false);
        try {
            File folder = new File(FOLDER);
            load(new File(folder, "english.php"), new File(folder, "portuguesbr.php"));
            load(new File(folder, "english"), new File(folder, "portuguesbr"));
            count();
            tblLabels.setModel(new LabelTableModel());
            btnSave.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e);
        }
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {
        int i = JOptionPane.showConfirmDialog(this, "Esta operação irá sobrescrever todos os arquivos modificados. Deseja continuar?", "Confirme operação", JOptionPane.YES_NO_OPTION);
        if (i == JOptionPane.NO_OPTION) {
            return;
        }
        try {
            save();
            dirtyFiles.clear();
            JOptionPane.showMessageDialog(this, "Salvo com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new LabelsFrame().setVisible(true);
            }
        });
    }

    private javax.swing.JButton btnLoad;

    private javax.swing.JButton btnSave;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JLabel lblStats;

    private javax.swing.JTable tblLabels;

    private static class Label {

        private String name;

        private String original;

        private String translated;

        private int line;

        private File fileOriginal;

        private File fileTraduzido;

        public File getFileOriginal() {
            return fileOriginal;
        }

        public void setFileOriginal(File file) {
            this.fileOriginal = file;
        }

        public File getFileTraduzido() {
            return fileTraduzido;
        }

        public void setFileTraduzido(File fileTraduzido) {
            this.fileTraduzido = fileTraduzido;
        }

        public int getLine() {
            return line;
        }

        public void setLine(int line) {
            this.line = line;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public String getTranslated() {
            return translated;
        }

        public void setTranslated(String translated) {
            this.translated = translated;
        }
    }

    private class LabelTableModel extends AbstractTableModel {

        private List<String> lblNames;

        public LabelTableModel() {
            lblNames = new ArrayList<String>(labels.keySet());
        }

        public int getRowCount() {
            return lblNames.size();
        }

        public int getColumnCount() {
            return 4;
        }

        @Override
        public String getColumnName(int col) {
            switch(col) {
                case 0:
                    return "Arquivo";
                case 1:
                    return "Label";
                case 2:
                    return "Original";
                case 3:
                    return "Traduzido";
                default:
                    return null;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 3;
        }

        public Object getValueAt(int row, int col) {
            Label l = labels.get(lblNames.get(row));
            switch(col) {
                case 0:
                    return l.getFileOriginal().getName();
                case 1:
                    return l.getName();
                case 2:
                    return l.getOriginal();
                case 3:
                    return l.getTranslated();
                default:
                    return null;
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 3) {
                Label l = labels.get(lblNames.get(rowIndex));
                l.setTranslated(aValue.toString());
                dirtyFiles.put(l.getFileOriginal(), l.getFileTraduzido());
                count();
            }
        }
    }

    private static class ToolTipTable extends JTable {

        @Override
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
            Component c = super.prepareRenderer(renderer, row, column);
            if (c instanceof JComponent) {
                JComponent jc = (JComponent) c;
                Object val = getValueAt(row, column);
                if ((val != null) && !"".equals(val)) {
                    String v = (String) val;
                    if (v.length() < 100) {
                        jc.setToolTipText(v);
                    } else {
                        jc.setToolTipText("<html><div style='width: 300px; text-align: justify'>" + val + "</div></html>");
                    }
                }
            }
            return c;
        }

        @Override
        public TableCellRenderer getCellRenderer(int row, int column) {
            if (column < 3) {
                return super.getCellRenderer(row, column);
            } else {
                return new CheckCellRenderer();
            }
        }
    }

    private static class CheckCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            Object t = table.getValueAt(row, 3);
            Object o = table.getValueAt(row, 2);
            if ((t == null) || "".equals(t)) {
                if (!"".equals(o)) {
                    c.setBackground(Color.RED);
                }
            } else if (o.equals(t)) {
                c.setBackground(Color.YELLOW);
            }
            return c;
        }
    }
}
