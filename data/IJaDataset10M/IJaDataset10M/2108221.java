package com.onyourmind.tra;

import java.awt.Component;
import java.awt.Container;
import java.awt.TextField;
import com.onyourmind.tools.Date;

public class HolidayRow {

    int columnCount = 3;

    public TextField textFieldHolidayName = null;

    public TextField textFieldStartDate = null;

    public TextField textFieldEndDate = null;

    public Component[] components = new Component[columnCount];

    private PanelTraMain startPage = null;

    public HolidayRow(PanelTraMain startPage) {
        this.startPage = startPage;
    }

    public void removeRow(Container parent) {
        updatePointers();
        for (int nComp = 0; nComp < components.length; nComp++) {
            parent.remove(components[nComp]);
        }
    }

    public void updatePointers() {
        components[0] = textFieldHolidayName;
        components[1] = textFieldStartDate;
        components[2] = textFieldEndDate;
    }

    public double getStartTimeUnit() {
        return Date.getTimeUnitFromString(textFieldStartDate.getText(), startPage.getDaysPerTimeUnit());
    }

    public double getDuration() {
        double fDuration = 0.0;
        try {
            fDuration = Date.getDifference(textFieldStartDate.getText(), textFieldEndDate.getText(), startPage.getDaysPerTimeUnit());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fDuration;
    }
}
