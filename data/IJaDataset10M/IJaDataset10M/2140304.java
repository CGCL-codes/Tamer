package com.mockturtlesolutions.snifflib.graphics;

import com.mockturtlesolutions.snifflib.stats.Plot;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class AnnotationSelectionListener implements ListSelectionListener {

    private JTable maintable;

    private Plot mainplot;

    private int last_selection;

    public AnnotationSelectionListener(JTable t, Plot p) {
        this.maintable = t;
        this.maintable.getSelectionModel().addListSelectionListener(this);
        this.maintable.getColumnModel().getSelectionModel().addListSelectionListener(this);
        this.mainplot = p;
        this.last_selection = -1;
        this.mainplot.addLinePointSelectionListener(new LinePointSelectionListener() {

            public void actionPerformed(LinePointSelectionEvent ev) {
                int selectedLineIndex = ev.getSelectedIndex();
                SLLine selectedLine = ev.getSelectedLine();
                int datarow = mainplot.getDataIndexForLineIndex(selectedLine.getName(), selectedLineIndex);
                maintable.addRowSelectionInterval(datarow, datarow);
            }
        });
    }

    public void reset() {
        this.last_selection = -1;
    }

    public void valueChanged(ListSelectionEvent e) {
        int first = -1;
        int last = -1;
        if (e.getSource() == this.maintable.getSelectionModel() && this.maintable.getRowSelectionAllowed()) {
            first = e.getFirstIndex();
            last = e.getLastIndex();
            if (first == last_selection) {
                first = last;
            }
            this.last_selection = first;
            if (this.maintable.isRowSelected(first)) {
                String text = (new Integer(first + 1)).toString();
                this.mainplot.addAnnotation(first, text);
                this.mainplot.addAnnotation(first, text);
            }
        } else if (e.getSource() == this.maintable.getColumnModel().getSelectionModel() && this.maintable.getColumnSelectionAllowed()) {
            first = e.getFirstIndex();
            last = e.getLastIndex();
            String text = (new Integer(first + 1)).toString();
            this.mainplot.addAnnotation(first, text);
        }
        if (e.getValueIsAdjusting()) {
        }
    }
}
