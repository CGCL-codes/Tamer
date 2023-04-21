package main.gui.stores;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import utils.MyException;
import utils.UIDecorations;
import main.gui.components.MyTableTextField;
import main.gui.users.User;
import main.managers.ReferenceManager;

public class StoresTableRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    private JTextField textField = new MyTableTextField();

    private Map<String, String> usersNames = new HashMap<String, String>();

    ;

    public StoresTableRenderer() {
        try {
            for (User user : ReferenceManager.getUserManager().getUsers(new User(null))) {
                usersNames.put(user.getUserId(), user.getFullName());
            }
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        StoreDetails currRow = (StoreDetails) table.getModel().getValueAt(row, column);
        String returnValue = "";
        switch(column) {
            case StoresTable.ID_COL_NUM:
                {
                    returnValue = currRow.getId();
                    break;
                }
            case StoresTable.NAME_COL_NUM:
                {
                    returnValue = currRow.getName();
                    break;
                }
            case StoresTable.MANAGER_ID_COL_NUM:
                {
                    returnValue = usersNames.get(currRow.getManagerId());
                    if (returnValue == null) returnValue = "";
                    break;
                }
            case StoresTable.TELEPHONE_COL_NUM:
                {
                    returnValue = currRow.getTelephoneNo();
                    break;
                }
            case StoresTable.ADDRESS_COL_NUM:
                {
                    returnValue = currRow.getCity() + ", " + currRow.getStreetAddress();
                    break;
                }
            default:
                {
                }
        }
        if (isSelected) {
            textField.setBackground(UIDecorations.selectedRowColor);
        } else {
            textField.setBackground(UIDecorations.notSelectedRowColor);
        }
        textField.setText(returnValue);
        return textField;
    }
}
