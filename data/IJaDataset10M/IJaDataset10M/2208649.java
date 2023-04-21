package ui.view.component;

import org.joda.time.ReadableInterval;
import ui.controller.action.Action;

public interface CommissionCalculationUI {

    ReadableInterval getInterval();

    void setAcceptAction(Action action);
}
