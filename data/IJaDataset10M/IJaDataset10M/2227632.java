package gui.membertrainingtab;

import gui.generic.FixedJTable;
import gui.generic.GenericTable;
import gui.generic.RowIndexRenderer;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import util.util;
import data.GlobalData;
import datascheme.Ausbildung;
import datascheme.Lehrgang;
import datascheme.Mitglied;
import datascheme.RankedElement;
import java.awt.GridBagConstraints;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author noamik
 *
 */
public class MemberTrainingsPanel extends JPanel implements TableModelListener {

    private static final long serialVersionUID = 1L;

    private JScrollPane jScrollPane = null;

    private JTable jTable = null;

    private String[] titles = null;

    private GlobalData gd = null;

    private GenericTable gt = null;

    /**
	 * This is the default constructor
	 */
    public MemberTrainingsPanel() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        gd = GlobalData.getInstance();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridx = 0;
        this.setSize(750, 450);
        this.setLayout(new GridBagLayout());
        this.add(getJScrollPane(), gridBagConstraints);
    }

    /**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane(getJTable());
            jScrollPane.setAutoscrolls(true);
        }
        return jScrollPane;
    }

    /**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JTable getJTable() {
        gt = new GenericTable(createTitles(), createData());
        if (jTable == null) {
            jTable = new FixedJTable(gt);
            configureJTable();
        }
        return jTable;
    }

    private void configureJTable() {
        jTable.getModel().addTableModelListener(this);
        jTable.setPreferredScrollableViewportSize(new Dimension(740, 450));
        jTable.setFillsViewportHeight(true);
        jTable.setAutoCreateRowSorter(true);
        jTable.getColumnModel().getColumn(0).setMaxWidth(25);
        for (int i = 1; i < titles.length; i++) jTable.getColumnModel().getColumn(i).setMinWidth(75);
        replaceFirstColumnCellRenderer(jTable.getColumnModel().getColumn(0));
        gt.setColumnEditable(0, false);
    }

    private Object[][] createData() {
        if (util.DebugMemberTrainings) System.out.println("updateData:");
        HashMap<Integer, Mitglied> mitglieder = gd.getMitglieder();
        Object[][] data = new Object[mitglieder.size()][titles.length];
        Iterator<Mitglied> it = mitglieder.values().iterator();
        Mitglied temp = null;
        Ausbildung tempa = null;
        Integer id = null;
        Integer i = -1;
        while (it.hasNext()) {
            i++;
            temp = it.next();
            if (util.DebugMemberTrainings) System.out.println("updateData:" + temp.getName());
            tempa = temp.getAusbildung();
            data[i][0] = temp.getId();
            data[i][1] = temp.getName();
            data[i][2] = temp.getVorname();
            for (int j = 3; j < titles.length; j++) {
                id = gd.getLehrgänge().getId(titles[j]);
                if (tempa.getLehrgänge().containsKey(id)) {
                    if (util.DebugMemberTrainings) System.out.println("updateData: " + titles[j] + " - " + util.DateToString(tempa.getLehrgänge().get(id)));
                    data[i][j] = util.DateToString(tempa.getLehrgänge().get(id));
                } else {
                    data[i][j] = "";
                }
            }
        }
        return data;
    }

    public void updateTableData() {
        Object[][] data = createData();
        gt.setData(data);
        gt.setColumnEditable(0, false);
        gt.fireTableDataChanged();
    }

    private void updateTableTitles() {
        createTitles();
        gt.updateTableStructure(titles, createData());
    }

    public void updateTable() {
        updateTableTitles();
    }

    private String[] createTitles() {
        if (util.DebugMemberTrainings) System.out.println("updateTitles:");
        HashMap<Integer, RankedElement> hs = gd.getLehrgänge().getElements();
        Iterator<RankedElement> it = hs.values().iterator();
        titles = new String[hs.size() + 3];
        titles[0] = "Nr.";
        titles[1] = "Name";
        titles[2] = "Vorname";
        Integer i = 2;
        RankedElement temp = null;
        while (it.hasNext()) {
            i++;
            temp = it.next();
            if (util.DebugMemberTrainings) System.out.println("updateTitles: " + temp.getName());
            titles[i] = temp.getName();
        }
        return titles;
    }

    private void replaceFirstColumnCellRenderer(TableColumn dgc) {
        dgc.setCellRenderer(new RowIndexRenderer());
    }

    @Override
    public void tableChanged(TableModelEvent arg0) {
        if (arg0.getColumn() == -1 || arg0.getFirstRow() == -1) return;
        Integer col = arg0.getColumn();
        Integer id = (Integer) gt.getValueAt(arg0.getFirstRow(), 0);
        Integer lehrid = GlobalData.getInstance().getLehrgänge().getId(titles[col]);
        Date lehrdate = util.StringToDate((String) gt.getValueAt(arg0.getFirstRow(), col));
        if (lehrdate == null) GlobalData.getInstance().removeMemberTraining(id, lehrid); else {
            Lehrgang lehr = new Lehrgang(lehrid, lehrdate);
            GlobalData.getInstance().addMemberTraining(id, lehr);
        }
        if (util.DebugMemberTrainings) System.out.println("Changed Member with id: " + id);
    }
}
