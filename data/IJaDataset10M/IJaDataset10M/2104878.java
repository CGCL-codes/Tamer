package org.boffyflow.ru.jathlete.gui;

import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.text.*;
import javax.swing.*;
import org.boffyflow.ru.util.*;
import org.boffyflow.ru.jathlete.compute.*;
import org.boffyflow.ru.jathlete.bin.*;
import org.boffyflow.ru.jathlete.data.*;

/********************************************************************
 <pre>
 <B>DistanceDialog</B>

 @version        1.0
 @author         Robert Uebbing
 
 Creation Date : 17-Jul-2001
  
 </pre>
********************************************************************/
public abstract class DistanceDialog extends WorkoutDialog {

    protected JTable _splitTable = null;

    protected SplitTableModel _splitTableModel = null;

    private JCheckBox _indoorsCheckBox = null;

    public DistanceDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
    }

    public DistanceDialog(Dialog parent, String title, boolean modal) {
        super(parent, title, modal);
    }

    protected void setupDistance() {
        JPanel bpane = new JPanel();
        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(_strings.getString("editday.workouts")), BorderFactory.createEmptyBorder(5, 3, 3, 3)));
        bpane.add(new JButton("Add Split"));
        bpane.add(new JButton("Delete Split"));
        _indoorsCheckBox = new JCheckBox(_strings.getString("indoors"));
        _indoorsCheckBox.setSelected(_workout.isIndoors());
        bpane.add(_indoorsCheckBox);
        pane.add(bpane);
        _splitTableModel = new SplitTableModel(_workout);
        _splitTable = new JTable(_splitTableModel);
        JScrollPane scrollPane = new JScrollPane(_splitTable);
        _splitTable.setPreferredScrollableViewportSize(new Dimension(450, 60));
        pane.add(scrollPane);
        JLabel label;
        String dist = cDistance.m2dist(_workout.getDistance(), _units.getDistance(), 1) + " " + _units.getDistanceString();
        label = new JLabel(dist);
        pane.add(label);
        label = new JLabel(cTime.getHMS(_workout.getTime()));
        pane.add(label);
        label = new JLabel(cTime.getHMS(cPace.getPace(_workout.getDistance(), _workout.getTime(), _units.getDistance())) + " min/" + _units.getDistanceString());
        pane.add(label);
        _c.add(pane);
    }
}
