package issrg.policytester;

import issrg.editor2.DateChooser;
import issrg.editor2.LDAPPolicyRetrievalDialog;
import issrg.editor2.LDAPPolicyRetrievalPanel;
import issrg.utils.gui.ifcondition.TrueFalseDialog;
import issrg.utils.gui.timedate.DurationDialog;
import issrg.utils.gui.timedate.TimeDateDialog;
import issrg.utils.gui.timedate.TimeDialog;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

/**
 *
 * @author Christian Azzopardi
 */
public class EnvironmentTable extends AbstractParameterValueTable {

    Vector column1Store, column2Store, column3Store, column4Store, column5Store;

    JFrame owner;

    ResourceBundle rb = ResourceBundle.getBundle("issrg/policytester/PTIFConditions_i18n");

    ResourceBundle rbl = ResourceBundle.getBundle("issrg/policytester/PTComponent_i18n");

    String column1 = rbl.getString("EnvironmentTable_Column1");

    String column2 = rbl.getString("EnvironmentTable_Column2");

    String column3 = rbl.getString("EnvironmentTable_Column3");

    String column4 = rbl.getString("EnvironmentTable_Column4");

    String column5 = rbl.getString("EnvironmentTable_Column5");

    String selectDateCaption = rbl.getString("TimeDatePanel_DateDialog_Name");

    String selectTimeCaption = rbl.getString("TimePanel_Dialog_Name");

    String selectDurationCaption = rbl.getString("DurationPanel_Dialog_Name");

    String clickHereCaption = rbl.getString("Click_Here");

    /** Creates a new instance of EnvironmentTable */
    public EnvironmentTable(JFrame owner) {
        this.owner = owner;
        column1Store = new Vector();
        column2Store = new Vector();
        column3Store = new Vector();
        column4Store = new Vector();
        column5Store = new Vector();
    }

    public void addToTableModel() {
        tableModel.addColumn(column1);
        tableModel.addColumn(column2);
        tableModel.addColumn(column3);
        tableModel.addColumn(column4);
        tableModel.addColumn(column5);
    }

    public void setColumnWidths() {
        TableColumn col0 = table.getColumnModel().getColumn(0);
        TableColumn col1 = table.getColumnModel().getColumn(1);
        TableColumn col2 = table.getColumnModel().getColumn(2);
        TableColumn col3 = table.getColumnModel().getColumn(3);
        TableColumn col4 = table.getColumnModel().getColumn(4);
        col0.setPreferredWidth(150);
        col1.setPreferredWidth(70);
        col2.setPreferredWidth(150);
        col3.setPreferredWidth(40);
        col4.setPreferredWidth(40);
    }

    public void addRow(final int rowLevel) {
        final EnvironmentParametersDropDownList envParamDDL = new EnvironmentParametersDropDownList();
        envParamDDL.addActionListener(this);
        envParamDDL.setActionCommand("COMBO_ONE_SELECTED");
        column1Store.add(envParamDDL);
        if (envParamDDL.getTypes() != null && envParamDDL.getTypes().length > 0) {
            JTextField tf = new JTextField("=");
            tf.setEditable(false);
            tf.setEnabled(false);
            column2Store.add(tf);
            JTextField textField = new JTextField();
            textField.addMouseListener(this);
            column3Store.add(setUpColumnThree(textField, envParamDDL.getTypes()[0]));
        } else {
            column2Store.add(new JTextField());
            column3Store.add(new JTextField());
        }
        JButton delete = new JButton("-");
        delete.addActionListener(this);
        delete.setActionCommand("DELETE_BUTTON_SELECTED");
        if (column4Store.size() == 0) delete.setEnabled(false);
        column4Store.add(delete);
        JButton add = new JButton("+");
        add.addActionListener(this);
        add.setActionCommand("ADD_BUTTON_SELECTED");
        column5Store.add(add);
        Object[] constraint = { ((JComboBox) column1Store.get(rowLevel)), ((JTextField) column2Store.get(rowLevel)), ((JTextField) column3Store.get(rowLevel)), ((JButton) column4Store.get(rowLevel)), ((JButton) column5Store.get(rowLevel)) };
        tableModel.addRow(constraint);
        setRowHeight(20);
    }

    public void removeFromVectors(final int rowLevel) {
        column1Store.remove(rowLevel);
        column2Store.remove(rowLevel);
        column3Store.remove(rowLevel);
        column4Store.remove(rowLevel);
        column5Store.remove(rowLevel);
    }

    public JTextField setUpColumnTwo() {
        JTextField tf = new JTextField("=");
        tf.setEditable(false);
        tf.setEnabled(false);
        return tf;
    }

    public JTextField setUpColumnThree(JTextField tf, String type) {
        if (type.equals(rb.getString("type2"))) {
            tf.setEditable(false);
            tf.setText(clickHereCaption);
        } else if (type.equals(rb.getString("type1"))) {
            tf.setEditable(false);
            tf.setText(clickHereCaption);
        } else if (type.equals(rb.getString("type7"))) {
            tf.setEditable(false);
            tf.setText(clickHereCaption);
        } else if (type.equals(rb.getString("type6"))) {
            tf.setEditable(false);
            tf.setText(clickHereCaption);
        } else {
            tf.setEditable(true);
            tf.setText("");
        }
        return tf;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().intern().equals("COMBO_ONE_SELECTED")) {
            EnvironmentParametersDropDownList cb = (EnvironmentParametersDropDownList) e.getSource();
            if (cb.getSelectedIndex() < 0 || table.getEditingRow() < 0) return;
            if (cb.getTypes()[cb.getSelectedIndex()].intern().equals(rb.getString("type2"))) {
                ((JTextField) column3Store.get(table.getEditingRow())).setEditable(false);
                ((JTextField) column3Store.get(table.getEditingRow())).setText(clickHereCaption);
            } else if (cb.getTypes()[cb.getSelectedIndex()].intern().equals(rb.getString("type1"))) {
                ((JTextField) column3Store.get(table.getEditingRow())).setEditable(false);
                ((JTextField) column3Store.get(table.getEditingRow())).setText(clickHereCaption);
            } else if (cb.getTypes()[cb.getSelectedIndex()].intern().equals(rb.getString("type7"))) {
                ((JTextField) column3Store.get(table.getEditingRow())).setEditable(false);
                ((JTextField) column3Store.get(table.getEditingRow())).setText(clickHereCaption);
            } else if (cb.getTypes()[cb.getSelectedIndex()].intern().equals(rb.getString("type6"))) {
                ((JTextField) column3Store.get(table.getEditingRow())).setEditable(false);
                ((JTextField) column3Store.get(table.getEditingRow())).setText(clickHereCaption);
            } else if (cb.getTypes()[cb.getSelectedIndex()].intern().equals(rb.getString("type9"))) {
                ((JTextField) column3Store.get(table.getEditingRow())).setEditable(false);
                ((JTextField) column3Store.get(table.getEditingRow())).setText(clickHereCaption);
            } else {
                ((JTextField) column3Store.get(table.getEditingRow())).setEditable(true);
                ((JTextField) column3Store.get(table.getEditingRow())).setText("");
            }
            table.repaint();
            table.revalidate();
        } else if (e.getActionCommand().intern().equals("ADD_BUTTON_SELECTED")) {
            addRow(column5Store.size());
        } else if (e.getActionCommand().intern().equals("DELETE_BUTTON_SELECTED")) {
            deleteRow(table.getEditingRow());
        }
    }

    public void mouseClicked(MouseEvent e) {
        EnvironmentParametersDropDownList cb = (EnvironmentParametersDropDownList) column1Store.get(table.getEditingRow());
        if (cb.getTypes()[cb.getSelectedIndex()].intern().equals(rb.getString("type1"))) {
            Point parentPoint = ((JTextField) e.getSource()).getLocation();
            Point p = new Point(0, 0);
            TimeDialog td = new TimeDialog(owner, selectTimeCaption, p, rbl);
            String toShow = td.tp.getTime();
            td.repaint();
            if (!toShow.intern().equals("")) ((JTextField) column3Store.get(table.getEditingRow())).setText(toShow);
        } else if (cb.getTypes()[cb.getSelectedIndex()].intern().equals(rb.getString("type2"))) {
            DateChooser DATE_CHOOSER = new DateChooser(this.owner, selectDateCaption);
            Date d = DATE_CHOOSER.select();
            if (d == null) {
            } else ((JTextField) column3Store.get(table.getEditingRow())).setText(new SimpleDateFormat("yyyy-MM-dd").format(d));
        } else if (cb.getTypes()[cb.getSelectedIndex()].intern().equals(rb.getString("type7"))) {
            TimeDateDialog tdd = new TimeDateDialog(this.owner, new SimpleDateFormat("yyyy.MM.dd").format(new Date()), rbl);
            String toShow = tdd.tdp.getDateTime();
            if (toShow == null) return;
            if (!toShow.intern().equals("")) ((JTextField) column3Store.get(table.getEditingRow())).setText(toShow);
        } else if (cb.getTypes()[cb.getSelectedIndex()].intern().equals(rb.getString("type6"))) {
            Point parentPoint = ((JTextField) e.getSource()).getLocation();
            Point p = new Point(200 + parentPoint.x + 5, 200 + parentPoint.y + 28);
            TrueFalseDialog tfd = new TrueFalseDialog(this.owner, "True || False", p, rbl);
            String toShow = tfd.tfp.getBoolean();
            if (toShow == null) return;
            if (!toShow.intern().equals("")) ((JTextField) column3Store.get(table.getEditingRow())).setText(toShow);
        } else if (cb.getTypes()[cb.getSelectedIndex()].intern().equals(rb.getString("type9"))) {
            DurationDialog dd = new DurationDialog(owner, selectDurationCaption, rbl);
            String toShow = dd.dp.getDuration();
            dd.repaint();
            if (!toShow.intern().equals("")) ((JTextField) column3Store.get(table.getEditingRow())).setText(toShow);
        }
        table.repaint();
        table.revalidate();
    }
}
