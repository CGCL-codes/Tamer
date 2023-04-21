package com.nokia.ats4.appmodel.grapheditor.swing;

import com.nokia.ats4.appmodel.model.domain.CompositeState;
import com.nokia.ats4.appmodel.model.domain.State;
import com.nokia.ats4.appmodel.util.Settings;
import org.jgraph.graph.DefaultGraphCell;

/**
 * KendoGraphCell
 *
 * This is used with states so that we can display tooltips
 *
 * @author Hannu-Pekka Hakam&auml;ki
 * @version $Revision: 2 $
 */
public class KendoGraphCell extends DefaultGraphCell {

    /** Enumeration of cell focus types */
    public enum FocusType {

        NO_FOCUS, FAILED_FOCUS, SUCCESS_FOCUS
    }

    ;

    /** This indicates whether this cell has special focus */
    private FocusType hasSpecialFocus = KendoGraphCell.FocusType.NO_FOCUS;

    /**
     * Creates a new instance of KendoGraphCell
     */
    public KendoGraphCell() {
        super();
    }

    /**
     * Creates a new instance of KendoGraphCell
     * @param o UserObject
     */
    public KendoGraphCell(Object o) {
        super(o);
    }

    /**
     * Indicates whether this has special focus
     *
     * @return returns true if this edge has special focus
     */
    public FocusType hasSpecialFocus() {
        return this.hasSpecialFocus;
    }

    /**
     * This sets the special focus to preferred value
     * 
     * @param focusType FocusType to set
     */
    public void setSpecialFocus(FocusType focusType) {
        this.hasSpecialFocus = focusType;
    }

    /**
     * This returns the tooltip text that is showed in graph
     *
     * @return Tooltip String
     */
    public String getToolTipString() {
        String retval = "<html><b>";
        Object uo = this.getUserObject();
        if (uo != null) {
            if (uo instanceof CompositeState) {
                CompositeState cm = (CompositeState) uo;
                if (cm.getName() != null) {
                    retval += "<b><i>" + cm.getName() + "</i></b>";
                }
            } else if (uo instanceof State) {
                State s = (State) uo;
                if (s.getName() == null || !Settings.getBooleanProperty(Settings.SHOW_EVENT_NAMES)) {
                    retval += "<b>" + s.getEventId() + "</b>";
                } else {
                    retval += "<b>" + s.getEventId() + " <i>" + s.getName() + "</i></b>";
                }
            }
        }
        retval += "</b></html>";
        return retval;
    }
}
