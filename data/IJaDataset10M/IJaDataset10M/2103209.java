package net.sf.strampel.runtest;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultFormatterFactory;
import com.jeta.forms.components.panel.FormPanel;
import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.binding.formatter.EmptyDateFormatter;

public class TriathlonSzeneRunningTestView {

    private final TriathlonSzeneRunningTestModel model;

    private final FormPanel PANEL = new FormPanel("net/sf/strampel/runtest/gui/TriaSzeneTestEdit.jfrm");

    private static final String ID_DATE_FIELD = "date.field";

    private static final String ID_STAGE_TABLE = "stages.table";

    private static final String ID_NEW_BUTTON = "add.btn";

    private static final String ID_EDIT_BUTTON = "edit.btn";

    private static final String ID_DELETE_BUTTON = "delete.btn";

    private static final String ID_OK_BUTTON = "ok.btn";

    private static final String ID_CANCEL_BUTTON = "cancel.btn";

    public TriathlonSzeneRunningTestView(TriathlonSzeneRunningTestModel model) {
        this.model = model;
    }

    public JComponent buildPanel() {
        initComponents();
        initEventHandling();
        return PANEL;
    }

    private void initComponents() {
        JFormattedTextField dateField = (JFormattedTextField) PANEL.getComponentByName(ID_DATE_FIELD);
        DateFormat shortFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        shortFormat.setLenient(false);
        JFormattedTextField.AbstractFormatter defaultFormatter = new EmptyDateFormatter(shortFormat);
        JFormattedTextField.AbstractFormatter displayFormatter = new EmptyDateFormatter();
        DefaultFormatterFactory formatterFactory = new DefaultFormatterFactory(defaultFormatter, displayFormatter);
        dateField.setFormatterFactory(formatterFactory);
        Bindings.bind(dateField, model.getModel(TriathlonSzeneRunningTest.PROPERTY_DATE));
        JTable table = PANEL.getTable(ID_STAGE_TABLE);
        table.setModel(getStageTableModel());
        table.setSelectionModel(new SingleListSelectionAdapter(model.getStageSelection().getSelectionIndexHolder()));
        table.addMouseListener(model.getDoubleClickHandler());
    }

    private void initEventHandling() {
        PANEL.getButton(ID_NEW_BUTTON).addActionListener(model.getNewAction());
        PANEL.getButton(ID_EDIT_BUTTON).addActionListener(model.getEditAction());
        PANEL.getButton(ID_DELETE_BUTTON).addActionListener(model.getDeleteAction());
    }

    /**
     * Enables or disables this model's Actions when it is notified
     * about a change in the <em>selectionEmpty</em> property
     * of the SelectionInList.
     */
    private final class SelectionEmptyHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            updateActionEnablement();
        }

        private void updateActionEnablement() {
            boolean hasSelection = model.getStageSelection().hasSelection();
            PANEL.getButton(ID_EDIT_BUTTON).getAction().setEnabled(hasSelection);
            PANEL.getButton(ID_DELETE_BUTTON).getAction().setEnabled(hasSelection);
        }
    }

    private TableModel getStageTableModel() {
        return (TableModel) new StageTableModel(model.getBean().getStagesModel());
    }

    private static final class StageTableModel extends AbstractTableAdapter<Stage> {

        private static String[] COLUMNS = { "Level", "Distance", "Time", "HearRateAverage", "Aerob" };

        private StageTableModel(ListModel listModel) {
            super(listModel, COLUMNS);
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Stage stage = getRow(rowIndex);
            switch(columnIndex) {
                case 0:
                    return stage.getLevel();
                case 1:
                    return stage.getDistance() + "m";
                case 2:
                    return stage.getTime() + " sec";
                case 3:
                    return stage.getHeartRateAvg();
                case 4:
                    return stage.isAerob();
                default:
                    throw new IllegalStateException("unknown column");
            }
        }
    }
}
