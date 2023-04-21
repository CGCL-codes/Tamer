package com.ivis.xprocess.ui.views.burndown;

import java.util.Comparator;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.diagram.model.DiagNode;
import com.ivis.xprocess.ui.diagram.model.DiagNodeImpl;
import com.ivis.xprocess.ui.util.dates.DateType;
import com.ivis.xprocess.util.Day;

public class MarkerLabelNode extends DiagNodeImpl {

    public static final String NODE_TYPE = "MARKER_LABEL";

    private final DateType myDateType;

    private final Day myDay;

    public MarkerLabelNode(DateType dateType, Day day) {
        myDay = day;
        myDateType = dateType;
    }

    public boolean allowHighlight() {
        return false;
    }

    public IElementWrapper getElementWrapper() {
        return null;
    }

    public String getType() {
        return NODE_TYPE;
    }

    public DateType getDateType() {
        return myDateType;
    }

    public Day getDay() {
        return myDay;
    }

    @Override
    public String getLabel(String labelId) {
        if (labelId == null) {
            return BurndownHelper.getLabelText(getDateType());
        }
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + getDateType() + "=" + DateType.toString(getDay());
    }

    public static class DayComparator implements Comparator<DiagNode> {

        public int compare(DiagNode n1, DiagNode n2) {
            MarkerLabelNode o1 = (MarkerLabelNode) n1;
            MarkerLabelNode o2 = (MarkerLabelNode) n2;
            Day d1 = o1.getDay();
            Day d2 = o2.getDay();
            if (!DateType.isFinite(d1) && !DateType.isFinite(d2)) {
                return o1.getDateType().compareTo(o2.getDateType());
            } else if (d1 == null) {
                return -1;
            } else {
                return d1.compareTo(d2);
            }
        }
    }
}
