package co.edu.unal.ungrid.services.client.applet.segmentation.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import co.edu.unal.ungrid.services.client.applet.segmentation.SegmentationServiceFactory;
import co.edu.unal.ungrid.services.client.applet.segmentation.model.Segmentation;
import co.edu.unal.ungrid.services.client.applet.segmentation.model.SegmentationDocument;
import co.edu.unal.ungrid.services.client.applet.segmentation.model.curve.ControlPointCurve;
import co.edu.unal.ungrid.util.ScreenHelper;

public class SegmentsEditor extends JDialog {

    /**
	 * @param owner
	 */
    public SegmentsEditor(Frame owner, SliceView view) {
        super(owner);
        initialize(view);
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize(SliceView view) {
        assert view != null;
        m_view = view;
        setTitle("Segments Editor");
        setModal(true);
        Dimension as = ScreenHelper.getActualSize(this);
        setLocation(100, 100);
        setSize(as.width - 200, as.height - 200);
        setContentPane(getJContentPane());
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setBackground(Color.WHITE);
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getControlPanel(), BorderLayout.SOUTH);
        }
        return jContentPane;
    }

    private JPanel getControlPanel() {
        if (jControl == null) {
            jControl = new JPanel();
            jControl.setLayout(new BorderLayout());
            jControl.setBackground(BACKG);
            jControl.setBorder(new EmptyBorder(10, 10, 10, 10));
            m_select = new SelectAction();
            m_delete = new DeleteAction();
            m_rename = new RenameAction();
            m_cancel = new CancelAction();
            m_accept = new AcceptAction();
            m_delete.setEnabled(false);
            m_rename.setEnabled(false);
            JPanel jWest = new JPanel();
            jWest.setLayout(new BorderLayout(5, 0));
            jWest.add(new JButton(m_select), BorderLayout.WEST);
            jWest.add(new JButton(m_delete), BorderLayout.CENTER);
            jWest.add(new JButton(m_rename), BorderLayout.EAST);
            JPanel jEast = new JPanel();
            jEast.setLayout(new BorderLayout(5, 0));
            jEast.add(new JButton(m_cancel), BorderLayout.WEST);
            jEast.add(new JButton(m_accept), BorderLayout.EAST);
            jControl.add(jWest, BorderLayout.WEST);
            jControl.add(jEast, BorderLayout.EAST);
        }
        return jControl;
    }

    private class SelectAction extends AbstractAction {

        public static final long serialVersionUID = 200609220000001L;

        public SelectAction() {
            putValue(Action.NAME, "Select All");
            putValue(Action.SHORT_DESCRIPTION, "Select All Segments");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        }

        public void actionPerformed(ActionEvent ae) {
            int numRows = jTable.getRowCount();
            int selected = jTable.getSelectedRowCount();
            if (selected == numRows) {
                deselectAll();
            } else {
                selectAll();
            }
        }
    }

    private class DeleteAction extends AbstractAction {

        public static final long serialVersionUID = 200609220000001L;

        public DeleteAction() {
            putValue(Action.NAME, "Delete");
            putValue(Action.SHORT_DESCRIPTION, "Delete Selected Segment(s)");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
        }

        public void actionPerformed(ActionEvent ae) {
            if (confirmDelete()) {
                deleteSegment();
            }
        }
    }

    private class RenameAction extends AbstractAction {

        public static final long serialVersionUID = 200609220000001L;

        public RenameAction() {
            putValue(Action.NAME, "Rename");
            putValue(Action.SHORT_DESCRIPTION, "Rename Selected Segment");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
        }

        public void actionPerformed(ActionEvent ae) {
            renameSegment();
        }
    }

    private class AcceptAction extends AbstractAction {

        public static final long serialVersionUID = 200609220000001L;

        public AcceptAction() {
            putValue(Action.NAME, "Accept");
            putValue(Action.SHORT_DESCRIPTION, "Accept Changes");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_ENTER);
        }

        public void actionPerformed(ActionEvent ae) {
            applyChanges();
        }
    }

    private class CancelAction extends AbstractAction {

        public static final long serialVersionUID = 200609220000001L;

        public CancelAction() {
            putValue(Action.NAME, "Cancel");
            putValue(Action.SHORT_DESCRIPTION, "Discard Changes");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_ESCAPE);
        }

        public void actionPerformed(ActionEvent ae) {
            discardChanges();
        }
    }

    private void changeToDeselectAll() {
        m_select.putValue(Action.NAME, "Deselect All");
        m_select.putValue(Action.SHORT_DESCRIPTION, "Deselect All Segments");
    }

    private void changeToSelectAll() {
        m_select.putValue(Action.NAME, "Select All");
        m_select.putValue(Action.SHORT_DESCRIPTION, "Select All Segments");
    }

    private void selectAll() {
        jTable.selectAll();
        changeToDeselectAll();
    }

    private void deselectAll() {
        jTable.clearSelection();
        changeToSelectAll();
    }

    private boolean confirmDelete() {
        return JOptionPane.showConfirmDialog(this, "Do you actually want to delete the selected curve(s) ?                \n\n\n\n", "Please confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION;
    }

    private void deleteSegment() {
        tModel.delete(jTable.getSelectedRows());
    }

    private void renameSegment() {
        int row = jTable.getSelectedRow();
        jTable.editCellAt(row, 1);
        TableCellEditor ce = jTable.getCellEditor(row, 1);
        Component cmp = ce.getTableCellEditorComponent(jTable, jTable.getValueAt(row, 1), true, row, 1);
        if (cmp instanceof JTextField) {
            JTextField tf = (JTextField) cmp;
            tf.select(0, tf.getText().length());
            tf.requestFocus();
        }
    }

    private Hashtable<Integer, String> getSegmentNames() {
        Object[][] segData = tModel.getData();
        Hashtable<Integer, String> segNames = new Hashtable<Integer, String>(segData.length);
        for (int idx = 0; idx < segData.length; idx++) {
            segNames.put((Integer) segData[idx][SEG_ID], (String) segData[idx][SEG_NAME]);
        }
        return segNames;
    }

    private void applyChanges() {
        if (m_bDebug) {
            System.exit(0);
        } else {
            SegmentationDocument doc = SegmentationServiceFactory.getDocument();
            Segmentation segm = doc.getSegmentation();
            if (segm != null) {
                segm.update(getSegmentNames());
            }
            setVisible(false);
        }
    }

    private void discardChanges() {
        if (m_bDebug) {
            System.exit(0);
        } else {
            setVisible(false);
        }
    }

    private JTable getTable(final Object[][] data) {
        if (jTable == null) {
            tModel = new TableModel(data);
            jTable = new JTable(tModel);
            jTable.setRowSelectionAllowed(true);
            jTable.setColumnSelectionAllowed(false);
            jTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
            int width = getWidth();
            TableColumn tcSegId = jTable.getColumnModel().getColumn(SEG_ID);
            tcSegId.setPreferredWidth(2 * width / 20);
            TableColumn tcSegName = jTable.getColumnModel().getColumn(SEG_NAME);
            tcSegName.setPreferredWidth(6 * width / 20);
            TableColumn tcSegCurve = jTable.getColumnModel().getColumn(SEG_CURVES);
            tcSegCurve.setPreferredWidth(9 * width / 20);
            TableColumn tcSegArea = jTable.getColumnModel().getColumn(SEG_AREA);
            tcSegArea.setPreferredWidth(3 * width / 20);
            ListSelectionModel selModel = jTable.getSelectionModel();
            selModel.addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        int sel = jTable.getSelectedRowCount();
                        m_delete.setEnabled(sel > 0);
                        m_rename.setEnabled(sel == 1);
                        int numRows = jTable.getRowCount();
                        if (sel == numRows) {
                            changeToDeselectAll();
                        }
                    }
                }
            });
            jTable.addKeyListener(new KeyAdapter() {

                public void keyPressed(KeyEvent ke) {
                    super.keyPressed(ke);
                    if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        discardChanges();
                    } else if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                        applyChanges();
                    }
                }
            });
        }
        return jTable;
    }

    class TableModel extends AbstractTableModel {

        public static final long serialVersionUID = 200609220000001L;

        public TableModel(Object[][] data) {
            this.data = data;
        }

        public int getColumnCount() {
            return colNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            assert 0 <= col && col < colNames.length;
            return colNames[col];
        }

        public Object getValueAt(int row, int col) {
            assert 0 <= row && row < data.length;
            assert 0 <= col && col < colNames.length;
            return data[row][col];
        }

        public boolean isCellEditable(int row, int col) {
            return (col == 1);
        }

        public void setValueAt(Object value, int row, int col) {
            for (int r = 0; r < data.length; r++) {
                if (r != row && data[r][0].equals(value)) {
                    JOptionPane.showMessageDialog(SegmentsEditor.this, "Segment name already exists.������������������\n\n\n");
                    return;
                }
            }
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }

        private boolean inSelection(int idx, int[] sel) {
            for (int i = 0; i < sel.length; i++) {
                if (idx == sel[i]) {
                    return true;
                }
            }
            return false;
        }

        public void delete(int[] sel) {
            Object[][] newData = new Object[data.length - sel.length][SEG_ENTRIES];
            if (sel.length < data.length) {
                for (int i = 0, a = 0; i < data.length; i++) {
                    if (inSelection(i, sel)) {
                        continue;
                    }
                    newData[a][SEG_ID] = data[i][SEG_ID];
                    newData[a][SEG_NAME] = data[i][SEG_NAME];
                    newData[a][SEG_CURVES] = data[i][SEG_CURVES];
                    newData[a][SEG_AREA] = data[i][SEG_AREA];
                    a++;
                }
            }
            data = newData;
            fireTableDataChanged();
        }

        public Object[][] getData() {
            return data;
        }

        private Object[][] data;

        private String[] colNames = { "Segment ID", "Segment Name", "Segment Curves", "Segment Area (mm2)" };
    }

    private String getCurves(Integer segId) {
        StringBuffer sb = new StringBuffer();
        SegmentationDocument doc = SegmentationServiceFactory.getDocument();
        Segmentation segm = doc.getSegmentation();
        if (segm != null) {
            ArrayList<ControlPointCurve> curves = segm.getSegmentCurves(segId);
            for (int c = 0; c < curves.size(); c++) {
                sb.append("C");
                sb.append(curves.get(c).getId());
                if (c < curves.size() - 1) {
                    sb.append(", ");
                }
            }
        }
        return sb.toString();
    }

    private double getArea(String sCurves) {
        double area = 0.0;
        SegmentationDocument doc = SegmentationServiceFactory.getDocument();
        Segmentation segm = doc.getSegmentation();
        if (segm != null) {
            StringTokenizer st = new StringTokenizer(sCurves, ",");
            while (st.hasMoreTokens()) {
                String sCurve = st.nextToken();
                ControlPointCurve curve = segm.getCurve(Integer.parseInt(sCurve.substring(1)));
                if (curve != null) {
                    area += m_view.toSquaredMm(curve.area());
                }
            }
        }
        return area;
    }

    private Object[][] htToObjectArray(Segmentation segm) {
        assert segm != null;
        Hashtable<Integer, String> ht = segm.getSegmentIds();
        Object[][] data = new Object[ht.size()][SEG_ENTRIES];
        int idx = 0;
        for (Integer id : ht.keySet()) {
            String sCurves = getCurves(id);
            data[idx][SEG_ID] = id;
            data[idx][SEG_NAME] = ht.get(id);
            data[idx][SEG_CURVES] = sCurves;
            data[idx][SEG_AREA] = String.format("%.2f", getArea(sCurves));
            idx++;
        }
        return data;
    }

    public void build() {
        if (jsPane != null) {
            jsPane.remove(jTable);
            jTable = null;
        }
        Object[][] data = null;
        if (m_bDebug) {
            data = new Object[100][SEG_ENTRIES];
            for (int s = 0; s < data.length; s++) {
                data[s][SEG_ID] = s;
                data[s][SEG_NAME] = "Segment" + s;
                data[s][SEG_CURVES] = "1, 2, 3, 4, 5";
                data[s][SEG_AREA] = "123.45";
            }
        } else {
            SegmentationDocument doc = SegmentationServiceFactory.getDocument();
            Segmentation segm = doc.getSegmentation();
            if (segm != null) {
                assert segm.numSegments() > 0;
                data = htToObjectArray(segm);
            }
        }
        if (jsPane == null) {
            jsPane = new JScrollPane();
            jsPane.getViewport().setBackground(BACKG);
            jContentPane.add(jsPane, BorderLayout.CENTER);
        }
        if (jsPane != null) {
            jsPane.setViewportView(getTable(data));
        }
    }

    public static void main(String[] args) {
        SegmentsEditor se = new SegmentsEditor(null, null);
        se.build();
        se.setVisible(true);
    }

    private SliceView m_view;

    private JPanel jContentPane;

    private JScrollPane jsPane;

    private JPanel jControl;

    private JTable jTable;

    private TableModel tModel;

    private SelectAction m_select;

    private DeleteAction m_delete;

    private RenameAction m_rename;

    private CancelAction m_cancel;

    private AcceptAction m_accept;

    private boolean m_bDebug;

    private static final long serialVersionUID = 1L;

    private static final Color BACKG = new Color(255, 255, 216);

    public static final int SEG_ID = 0;

    public static final int SEG_NAME = 1;

    public static final int SEG_CURVES = 2;

    public static final int SEG_AREA = 3;

    public static final int SEG_ENTRIES = 4;
}
