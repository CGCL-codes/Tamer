package org.grill.fatwhacker.gui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.grill.fatwhacker.data.DailyRecord;

public class DayDetailView {

    private DayItemTableView foodView;

    private DayItemTableView exerciseView;

    public DayDetailView(Composite parent) {
        foodView = new DayItemTableView(parent, true);
        exerciseView = new DayItemTableView(parent, false);
    }

    public void setDay(DailyRecord day) {
        foodView.setDay(day);
        exerciseView.setDay(day);
    }

    public void render() {
        foodView.render();
        exerciseView.render();
    }

    public void addChangeListener(Listener l) {
        foodView.addChangeListener(l);
        exerciseView.addChangeListener(l);
    }
}
