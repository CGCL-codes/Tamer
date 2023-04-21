package net.pepperbytes.eqc.gui.itemsearch;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import net.pepperbytes.eqc.database.persistentobjects.EquipmentUser;
import net.pepperbytes.eqc.database.persistentobjects.Item;
import net.pepperbytes.eqc.database.persistentobjects.ItemType;
import net.pepperbytes.eqc.database.persistentobjects.Status;
import net.pepperbytes.eqc.gui.EquipmentUserCellRenderer;
import net.pepperbytes.eqc.gui.SearchItemDialog.ItemTableModel;

public class ItemJTable extends JTable {

    /**
	 * TODO
	 */
    private static final long serialVersionUID = -3412281304347834945L;

    private EquipmentUserCellRenderer eqRenderer = new EquipmentUserCellRenderer();

    private ItemTypeRenderer itRenderer = new ItemTypeRenderer();

    private ItemStatusRenderer statusRenderer = new ItemStatusRenderer();

    private JComboBox itemTypeComboBox;

    private JComboBox ownerTypeComboBox;

    public ItemJTable(ItemTableModel model) {
        super(model);
        setAutoCreateRowSorter(true);
        setDefaultRenderer(EquipmentUser.class, eqRenderer);
        Vector<Field> fields = Item.getFieldList();
        int counter = 0;
        for (Iterator<Field> iterator = fields.iterator(); iterator.hasNext(); ) {
            Field field = (Field) iterator.next();
            if (field.getName().equals("theOwner")) {
                TableColumn tc = getColumnModel().getColumn(counter);
                tc.setCellEditor(new DefaultCellEditor(getOwnerTypeComboBox()));
            } else if (field.getType().equals(ItemType.class)) {
                TableColumn tc = getColumnModel().getColumn(counter);
                tc.setCellEditor(new DefaultCellEditor(getItemTypeComboBox()));
            } else if (field.getType().equals(Status.class)) {
                TableColumn tc = getColumnModel().getColumn(counter);
                tc.setCellEditor(new StatusCellEditor());
            }
            counter++;
        }
    }

    @SuppressWarnings("unchecked")
    private JComboBox getOwnerTypeComboBox() {
        if (ownerTypeComboBox == null) {
            ownerTypeComboBox = new JComboBox();
            ownerTypeComboBox.setRenderer(new GeneralPersistentObjectListCellRenderer());
            List<EquipmentUser> owners = EquipmentUser.listAll();
            for (Iterator<EquipmentUser> iterator = owners.iterator(); iterator.hasNext(); ) {
                EquipmentUser it = iterator.next();
                ownerTypeComboBox.addItem(it);
            }
        }
        return ownerTypeComboBox;
    }

    public TableCellRenderer getCellRenderer(int row, int column) {
        Vector<Field> fields = Item.getFieldList();
        Field field = fields.get(column);
        Class<?> fieldType = field.getType();
        if (fieldType.equals(ItemType.class)) {
            return itRenderer;
        } else if (fieldType.equals(Status.class)) {
            return statusRenderer;
        }
        return super.getCellRenderer(row, column);
    }

    @SuppressWarnings("unchecked")
    private JComboBox getItemTypeComboBox() {
        if (itemTypeComboBox == null) {
            itemTypeComboBox = new JComboBox();
            itemTypeComboBox.setRenderer(new GeneralPersistentObjectListCellRenderer());
            List<ItemType> itemTypes = ItemType.listAll();
            for (Iterator<ItemType> iterator = itemTypes.iterator(); iterator.hasNext(); ) {
                ItemType it = (ItemType) iterator.next();
                itemTypeComboBox.addItem(it);
            }
        }
        return itemTypeComboBox;
    }
}
