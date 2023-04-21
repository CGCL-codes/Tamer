package realcix20.guis.relationships;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import realcix20.classes.basic.Column;
import realcix20.classes.basic.Row;
import realcix20.classes.plugins.CurrencyPlugin;
import realcix20.classes.plugins.UomPlugin;
import realcix20.guis.utils.ComponentExt;
import realcix20.guis.utils.ComponentManager;
import realcix20.guis.utils.Item;
import realcix20.utils.DAO;

/**
 *
 * @author JerryChen
 */
public class BaseRelationship {

    public BaseRelationship(int clsId, Vector headComponentExts, Vector itemComponentExts) {
        initialNoBeControledComponents(itemComponentExts);
    }

    public BaseRelationship(int clsId, Vector componentExts) {
        initialNoBeControledComponents(componentExts);
    }

    private void initialNoBeControledComponents(Vector componentExts) {
        Iterator componentExtIter = componentExts.iterator();
        while (componentExtIter.hasNext()) {
            ComponentExt componentExt = (ComponentExt) componentExtIter.next();
            Column column = componentExt.getColumn();
            JComponent component = componentExt.getComponent();
            switch(column.getInputType()) {
                case 1:
                case 2:
                    if ((column.getPControls() == null) || (column.getPControls().equals(""))) {
                        DAO dao = DAO.getInstance();
                        dao.query(column.getInputPar());
                        ResultSet rs = dao.executeQuery();
                        try {
                            ResultSetMetaData rsmd = rs.getMetaData();
                            if (rsmd.getColumnCount() == 1) {
                                while (rs.next()) {
                                    Item item = new Item(rs.getObject(1), rs.getObject(1));
                                    ((JComboBox) component).addItem(item);
                                }
                            } else {
                                while (rs.next()) {
                                    StringBuffer sb = new StringBuffer();
                                    for (int i = 2; i < rsmd.getColumnCount(); i++) sb.append(rs.getObject(i) + " | ");
                                    sb.append(rs.getObject(rsmd.getColumnCount()));
                                    Item item = new Item(rs.getObject(1), sb.toString());
                                    ((JComboBox) component).addItem(item);
                                }
                            }
                            rs.close();
                        } catch (SQLException sqle) {
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    protected void contactRelationship1_11(final JComponent component, final JComponent childComponent) {
        if (ComponentManager.getValue(component) != null) {
            CurrencyPlugin cp = CurrencyPlugin.getInstance();
            StringBuffer sb = new StringBuffer("###" + cp.getSepor() + "###");
            int fraction = cp.getFraction(ComponentManager.getValue(component).toString());
            if (fraction > 0) {
                sb.append(cp.getFpoint());
            }
            for (int i = 1; i <= fraction; i++) {
                sb.append("0");
            }
            DecimalFormat df = new DecimalFormat(sb.toString());
            NumberFormatter nf = new NumberFormatter(df);
            DefaultFormatterFactory factory = new DefaultFormatterFactory(nf);
            ((JFormattedTextField) childComponent).setFormatterFactory(factory);
        }
        ((JComboBox) component).addActionListener(new ActionAdapter() {

            public void actionPerformed(ActionEvent e) {
                if (ComponentManager.getValue(component) != null) {
                    CurrencyPlugin cp = CurrencyPlugin.getInstance();
                    StringBuffer sb = new StringBuffer("###" + cp.getSepor() + "###");
                    int fraction = cp.getFraction(ComponentManager.getValue(component).toString());
                    if (fraction > 0) {
                        sb.append(cp.getFpoint());
                    }
                    for (int i = 1; i <= fraction; i++) {
                        sb.append("0");
                    }
                    DecimalFormat df = new DecimalFormat(sb.toString());
                    NumberFormatter nf = new NumberFormatter(df);
                    DefaultFormatterFactory factory = new DefaultFormatterFactory(nf);
                    ((JFormattedTextField) childComponent).setFormatterFactory(factory);
                }
            }
        });
    }

    protected void contactRelationship1_12(final JComponent component, final JComponent childComponent) {
        if (ComponentManager.getValue(component) != null) {
            CurrencyPlugin cp = CurrencyPlugin.getInstance();
            UomPlugin up = UomPlugin.getInstance();
            StringBuffer sb = new StringBuffer("###" + cp.getSepor() + "###");
            int fraction = up.getFraction(ComponentManager.getValue(component).toString());
            if (fraction > 0) {
                sb.append(".");
            }
            for (int i = 1; i <= fraction; i++) {
                sb.append("0");
            }
            DecimalFormat df = new DecimalFormat(sb.toString());
            NumberFormatter nf = new NumberFormatter(df);
            DefaultFormatterFactory factory = new DefaultFormatterFactory(nf);
            ((JFormattedTextField) childComponent).setFormatterFactory(factory);
        }
        ((JComboBox) component).addActionListener(new ActionAdapter() {

            public void actionPerformed(ActionEvent e) {
                if (ComponentManager.getValue(component) != null) {
                    CurrencyPlugin cp = CurrencyPlugin.getInstance();
                    UomPlugin up = UomPlugin.getInstance();
                    StringBuffer sb = new StringBuffer("###" + cp.getSepor() + "###");
                    int fraction = up.getFraction(ComponentManager.getValue(component).toString());
                    if (fraction > 0) {
                        sb.append(".");
                    }
                    for (int i = 1; i <= fraction; i++) {
                        sb.append("0");
                    }
                    DecimalFormat df = new DecimalFormat(sb.toString());
                    NumberFormatter nf = new NumberFormatter(df);
                    DefaultFormatterFactory factory = new DefaultFormatterFactory(nf);
                    ((JFormattedTextField) childComponent).setFormatterFactory(factory);
                }
            }
        });
    }

    protected void contactRelationship7_7(final JComponent component1, final JComponent component2) {
        ((JComboBox) component1).addActionListener(new ActionAdapter() {

            public void actionPerformed(ActionEvent e) {
                if (ComponentManager.getValue(component1) != null) {
                    ((JComboBox) component2).setSelectedItem(((JComboBox) component1).getSelectedItem());
                }
            }
        });
    }

    protected void contactRelationship1_1(final Vector components, final JComponent childComponent, final String sql) {
        ((JComboBox) childComponent).removeAllItems();
        DAO dao = DAO.getInstance();
        dao.query(sql);
        for (int i = 1; i <= components.size(); i++) {
            JComponent tempComponent = (JComponent) components.get(i - 1);
            dao.setObject(i, ComponentManager.getValue(tempComponent));
        }
        ResultSet rs = dao.executeQuery();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            if (rsmd.getColumnCount() == 1) {
                while (rs.next()) {
                    Item item = new Item(rs.getObject(1), rs.getObject(1));
                    ((JComboBox) childComponent).addItem(item);
                }
            } else {
                while (rs.next()) {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 2; i < rsmd.getColumnCount(); i++) sb.append(rs.getObject(i) + " | ");
                    sb.append(rs.getObject(rsmd.getColumnCount()));
                    Item item = new Item(rs.getObject(1), sb.toString());
                    ((JComboBox) childComponent).addItem(item);
                }
            }
            rs.close();
        } catch (SQLException sqle) {
        }
        Iterator componentIter = components.iterator();
        while (componentIter.hasNext()) {
            JComponent component = (JComponent) componentIter.next();
            ((JComboBox) component).addActionListener(new ActionAdapter() {

                public void actionPerformed(ActionEvent e) {
                    ((JComboBox) childComponent).removeAllItems();
                    DAO dao = DAO.getInstance();
                    dao.query(sql);
                    for (int i = 1; i <= components.size(); i++) {
                        JComponent tempComponent = (JComponent) components.get(i - 1);
                        dao.setObject(i, ComponentManager.getValue(tempComponent));
                    }
                    ResultSet rs = dao.executeQuery();
                    try {
                        ResultSetMetaData rsmd = rs.getMetaData();
                        if (rsmd.getColumnCount() == 1) {
                            while (rs.next()) {
                                Item item = new Item(rs.getObject(1), rs.getObject(1));
                                ((JComboBox) childComponent).addItem(item);
                            }
                        } else {
                            while (rs.next()) {
                                StringBuffer sb = new StringBuffer();
                                for (int i = 2; i < rsmd.getColumnCount(); i++) sb.append(rs.getObject(i) + " | ");
                                sb.append(rs.getObject(rsmd.getColumnCount()));
                                Item item = new Item(rs.getObject(1), sb.toString());
                                ((JComboBox) childComponent).addItem(item);
                            }
                        }
                        rs.close();
                    } catch (SQLException sqle) {
                    }
                }
            });
        }
    }

    protected Column findColumn(String tableName, String columnName, Vector componentExts) {
        Column column = null;
        Iterator componentExtIter = componentExts.iterator();
        while (componentExtIter.hasNext()) {
            ComponentExt componentExt = (ComponentExt) componentExtIter.next();
            Column tempColumn = componentExt.getColumn();
            if ((tempColumn.getTableName().equals(tableName)) && (tempColumn.getColumnName().equals(columnName))) {
                column = tempColumn;
                break;
            }
        }
        return column;
    }

    protected Row findRow(String tableName, String columnName, Vector componentExts) {
        Row row = null;
        Iterator componentExtIter = componentExts.iterator();
        while (componentExtIter.hasNext()) {
            ComponentExt componentExt = (ComponentExt) componentExtIter.next();
            Column column = componentExt.getColumn();
            if ((column.getTableName().equals(tableName)) && (column.getColumnName().equals(columnName))) {
                row = componentExt.getRow();
                break;
            }
        }
        return row;
    }

    protected JComponent findComponent(String tableName, String columnName, Vector componentExts) {
        JComponent component = null;
        Iterator componentExtIter = componentExts.iterator();
        while (componentExtIter.hasNext()) {
            ComponentExt componentExt = (ComponentExt) componentExtIter.next();
            Column column = componentExt.getColumn();
            if ((column.getTableName().equals(tableName)) && (column.getColumnName().equals(columnName))) {
                component = componentExt.getComponent();
                break;
            }
        }
        return component;
    }

    protected void initialComponent7Value(JComponent component, String columnName, String sql, Vector parameters) {
        DAO dao = DAO.getInstance();
        dao.query(sql);
        if (parameters != null) {
            for (int i = 1; i <= parameters.size(); i++) {
                dao.setObject(i, parameters.get(i - 1));
            }
        }
        ResultSet rs = dao.executeQuery();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                Object displayValue = rs.getObject(columnName);
                StringBuffer factValueBuffer = new StringBuffer();
                for (int i = 1; i < rsmd.getColumnCount(); i++) {
                    factValueBuffer.append(rs.getObject(i) + "-");
                }
                factValueBuffer.append(rs.getObject(rsmd.getColumnCount()));
                Item item = new Item(factValueBuffer.toString(), displayValue);
                ((JComboBox) component).addItem(item);
            }
            rs.close();
        } catch (SQLException e) {
        }
    }

    public abstract class ActionAdapter extends Object implements ActionListener {

        public ActionAdapter() {
        }

        public abstract void actionPerformed(ActionEvent ae);
    }
}
