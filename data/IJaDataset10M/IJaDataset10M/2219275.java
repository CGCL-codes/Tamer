package UI.Panels;

import Reps.*;
import Shapes.*;
import UI.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * <p>Title: ISAPanel</p>
 * <p>Description: A class representing the property panel for ISA Relationships</p>
 * <p>Jaarproject Programmeren 2002-2003</p>
 * <p>Company: UIA</p>
 * @author Helsen Gert (s985177)
 * @version 15/06/2003
 */
public class ISAPanel extends ElementPanel {

    private JTable fTable1;

    private ISATableModel fTableModel1;

    public ISAPanel() {
        fTableModel1 = new ISATableModel();
        fTable1 = new JTable(fTableModel1);
        fTable1.setRowSelectionAllowed(false);
        add(fTable1);
    }

    public void update(ERDiagram diagram, ISA isa) {
        super.update(diagram, isa);
        fTableModel1.setISA(diagram, isa);
        fTableModel1.fireTableStructureChanged();
    }
}

/**
 * Table Model to display super/sub entities
 */
class ISATableModel extends AbstractTableModel {

    private ISARep fIsaRep;

    private ISA fIsa;

    private ERDiagram fDiagram;

    private ArrayList fSubEntities;

    private EntityRep fSuperEntity;

    public Object getValueAt(int row, int col) {
        if (row == 0 && col == 0) return "Super Entity"; else if (row == 0 && col == 1) {
            if (fSuperEntity != null) return fSuperEntity.getName(); else return "";
        } else if (row == 1 && col == 0) return "Sub Entities";
        if (row > 0 && col == 1) {
            if (fSubEntities != null && !fSubEntities.isEmpty()) return ((EntityRep) fSubEntities.get(row - 1)).getName(); else return "";
        }
        return "";
    }

    public int getColumnCount() {
        return 2;
    }

    public int getRowCount() {
        if (fSubEntities != null && !fSubEntities.isEmpty()) {
            return fSubEntities.size() + 1;
        } else {
            return 2;
        }
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setISA(ERDiagram diagram, ISA isa) {
        fIsa = isa;
        fIsaRep = (ISARep) isa.getRep();
        fDiagram = diagram;
        if (fIsaRep != null) {
            fSuperEntity = fIsaRep.getSuperEntity();
            fSubEntities = fIsaRep.getSubEntities();
        } else {
            fSuperEntity = null;
            fSubEntities = null;
        }
    }
}
