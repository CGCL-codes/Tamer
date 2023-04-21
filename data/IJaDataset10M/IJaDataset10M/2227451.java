package KFrameWork.Widgets;

import javax.swing.*;
import java.util.*;
import KFrameWork.Base.*;

public class customOrderClass extends java.lang.Object {

    private KConfigurationClass configuration;

    private KLogClass log;

    private JList visualSourceList;

    private JList visualdestinationList;

    private KDataBrowserBaseClass tableFiller;

    private DefaultListModel sourceListModel;

    private DefaultListModel destinationListModel;

    /** Creates new listFillerClass */
    public customOrderClass(KConfigurationClass configurationParam, KLogClass logParam, JList sourceListParam, JList destinationListParam, KDataBrowserBaseClass tableFillerParam) {
        configuration = configurationParam;
        log = logParam;
        visualSourceList = sourceListParam;
        visualdestinationList = destinationListParam;
        tableFiller = tableFillerParam;
        sourceListModel = new DefaultListModel();
        destinationListModel = new DefaultListModel();
    }

    /** Fill the lists with certain items */
    public void fillList() throws KExceptionClass {
        java.util.List nameList = new ArrayList();
        tableFiller.getColumnNames(nameList);
        Iterator columnNames = nameList.iterator();
        java.util.List customOrders = tableFiller.GetCustomOrderData();
        while (columnNames.hasNext()) {
            String column_name = (String) columnNames.next();
            if (!column_name.equals("Select")) if (!customOrders.contains(column_name)) sourceListModel.addElement((Object) column_name);
        }
        Iterator orderNames = customOrders.iterator();
        while (orderNames.hasNext()) {
            String column_name = (String) orderNames.next();
            destinationListModel.addElement((Object) column_name);
        }
        visualSourceList.setModel(sourceListModel);
        visualdestinationList.setModel(destinationListModel);
    }

    /**   Reload the table, because of changed SQL order.  */
    public void refreshTable() throws KExceptionClass {
        java.util.List orderList = tableFiller.GetCustomOrderData();
        orderList.clear();
        int size = destinationListModel.getSize();
        for (int i = 0; i < size; i++) {
            String name = (String) destinationListModel.get(i);
            orderList.add(name);
        }
        tableFiller.setCustomOrder(orderList);
        tableFiller.refresh();
    }
}
