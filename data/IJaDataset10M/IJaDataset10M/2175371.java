package fr.lig.sigma.astral.gui.result;

import fr.lig.sigma.astral.common.Batch;
import fr.lig.sigma.astral.common.Tuple;
import fr.lig.sigma.astral.common.structure.TupleSet;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.util.*;

/**
 * @author Loic Petit
 */
public class RelationView implements EntityView, ChangeListener {

    private JSlider timeSlider;

    private JTable contentTable;

    private JPanel panel;

    private JLabel timerLabel;

    private Map<Integer, Object[][]> content = new HashMap<Integer, Object[][]>();

    private Map<Integer, Batch> batches = new HashMap<Integer, Batch>();

    private Batch baseTimestamp;

    private Object[] attribVector;

    private DefaultTableModel tableModel;

    private long guiTime;

    private long guiTimeDiff = 0;

    private int i = 0;

    public RelationView() {
        timeSlider.addChangeListener(this);
    }

    public void setAttributes(Set<String> attributes) {
        timeSlider.setValue(0);
        guiTime = 0;
        attribVector = new Vector<String>(attributes).toArray();
        tableModel = new DefaultTableModel(attribVector, 1);
        contentTable.setModel(tableModel);
        contentTable.validate();
        content.clear();
    }

    public JPanel getPanel() {
        return panel;
    }

    public long getGuiTime() {
        return guiTime / 1000000;
    }

    public long getGuiTimeDiff() {
        long t = guiTime - guiTimeDiff;
        guiTimeDiff = guiTime;
        return t;
    }

    public void setContent(Batch batch, TupleSet ts) {
        long time = System.nanoTime();
        if (content.isEmpty()) baseTimestamp = batch;
        timeSlider.setMaximum(i);
        int count = attribVector.length;
        contentTable.removeAll();
        Object[][] table = new Object[ts.size()][count];
        int j = 0;
        for (Tuple t : ts) {
            Object[] row = new Object[count];
            for (int i = 0; i < count; i++) {
                row[i] = t.get(attribVector[i].toString());
            }
            table[j++] = row;
        }
        content.put(i, table);
        batches.put(i, batch);
        i++;
        if (batch.equals(baseTimestamp)) tableModel.setDataVector(table, attribVector);
        guiTime += System.nanoTime() - time;
    }

    private Batch currentTimestamp = null;

    public void stateChanged(ChangeEvent changeEvent) {
        int value = timeSlider.getValue();
        Object[][] data = content.get(value);
        Batch timestamp = batches.get(value);
        if (timestamp != currentTimestamp) {
            timerLabel.setText("T=" + timestamp);
            contentTable.removeAll();
            tableModel.setDataVector(data, attribVector);
            currentTimestamp = timestamp;
        }
    }
}
