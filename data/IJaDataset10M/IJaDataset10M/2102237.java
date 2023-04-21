package plugin.initiative;

import gmgen.plugin.PcgCombatant;
import plugin.initiative.gui.TableColumnInformation;
import javax.swing.table.AbstractTableModel;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>
 * Essentialy sets up a list of <code>PcgCombatant</code> items.
 * </p>
 *
 * <p>
 * Current Ver: $Revision: 1.6 $
 * </p>
 * <p>
 * Last Editor: $Author: karianna $
 * </p>
 * <p>
 * Last Edited: $Date: 2006/02/08 12:19:13 $
 * </p>
 *
 * @author LodgeR
 */
public class OpposedSkillBasicModel extends AbstractTableModel {

    /**
	 * <p>
	 * A "wrapper" class for the combatants
	 * </p>
	 */
    protected class InitWrapper {

        PcgCombatant initiative = null;

        /** 
		 * Constructor 
		 * @param init
		 */
        public InitWrapper(PcgCombatant init) {
            initiative = init;
        }
    }

    /** Columns for the table */
    protected TableColumnInformation columns = new TableColumnInformation(10);

    /** Combatants */
    protected TreeMap combatants = new TreeMap();

    /** Constructor -- creates columns */
    public OpposedSkillBasicModel() {
        columns.addColumn("COMBATANT", String.class, null, false, "Combatant");
    }

    /**
	 * <p>
	 * Constructs columns and builds the combatant list.
	 * </p>
	 *
	 * @param combatantList
	 */
    public OpposedSkillBasicModel(List combatantList) {
        this();
        buildCombatantList(combatantList);
    }

    /**
	 * <p>
	 * Builds the combatant list
	 * </p>
	 *
	 * @param combatantList
	 */
    protected void buildCombatantList(List combatantList) {
        for (Iterator i = combatantList.iterator(); i.hasNext(); ) {
            Object o = i.next();
            if (o != null && o instanceof PcgCombatant) {
                PcgCombatant cbt = (PcgCombatant) o;
                addCombatant(cbt);
            }
        }
    }

    public Class getColumnClass(int columnIndex) {
        return columns.getClass(columnIndex);
    }

    public int getColumnCount() {
        return columns.getColumCount();
    }

    public String getColumnName(int column) {
        return columns.getLabel(column);
    }

    public int getRowCount() {
        return Math.max(combatants.size(), 1);
    }

    /**
	 * <p>
	 * Gets a row entry for the specified index.
	 * </p>
	 *
	 * @param rowIndex
	 * @return InitWrapper
	 */
    protected InitWrapper getRowEntry(int rowIndex) {
        InitWrapper returnValue = null;
        if (rowIndex < combatants.size()) {
            returnValue = (InitWrapper) ((Map.Entry) combatants.entrySet().toArray()[rowIndex]).getValue();
        }
        return returnValue;
    }

    /**
	 * <p>
	 * Returns the index for the given name
	 * </p>
	 *
	 * @param name
	 * @return index
	 */
    protected int getIndexOf(String name) {
        int returnValue = -1;
        int counter = -1;
        for (Iterator i = combatants.keySet().iterator(); i.hasNext() && returnValue < 0; ) {
            counter++;
            if (i.next().equals(name)) {
                returnValue = counter;
            }
        }
        return returnValue;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Object returnValue = null;
        if (rowIndex < combatants.size()) {
            InitWrapper entry = getRowEntry(rowIndex);
            switch(columnIndex) {
                case 0:
                    returnValue = entry.initiative.getName();
                    break;
            }
        }
        return returnValue;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        boolean returnValue = false;
        if (rowIndex < getRowCount()) {
            returnValue = columns.isColumnEditable(columnIndex);
        }
        return returnValue;
    }

    /**
	 * <p>
	 * Adds the specified combatant
	 * </p>
	 *
	 * @param combatant
	 */
    public void addCombatant(PcgCombatant combatant) {
        combatants.put(combatant.getName(), new InitWrapper(combatant));
        int rowIndex = getIndexOf(combatant.getName());
        fireTableRowsInserted(rowIndex, rowIndex);
    }

    /**
	 * <p>
	 * Removes the specified combatant
	 * </p>
	 *
	 * @param rowIndex
	 */
    public void removeCombatant(int rowIndex) {
        if (rowIndex < combatants.size()) {
            InitWrapper entry = getRowEntry(rowIndex);
            combatants.remove(entry.initiative.getName());
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    /**
	 * <p>
	 * Removes the specified combatant by name
	 * </p>
	 *
	 * @param name
	 */
    public void removeCombatant(String name) {
        int rowIndex = getIndexOf(name);
        combatants.remove(name);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    /**
	 * <p>
	 * Gets the specified combatant.
	 * </p>
	 *
	 * @param rowIndex
	 * @return PcgCombatant
	 */
    public PcgCombatant getCombatant(int rowIndex) {
        PcgCombatant returnValue = null;
        if (rowIndex < combatants.size()) {
            returnValue = getRowEntry(rowIndex).initiative;
        }
        return returnValue;
    }
}
