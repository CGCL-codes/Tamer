package net.tourbook.statistic;

import net.tourbook.Messages;
import net.tourbook.application.TourbookPlugin;
import net.tourbook.ui.UI;
import org.eclipse.jface.action.Action;

public class ActionSynchChartScale extends Action {

    private final StatContainer _statContainer;

    public ActionSynchChartScale(final StatContainer container) {
        super(UI.EMPTY_STRING, AS_CHECK_BOX);
        _statContainer = container;
        setImageDescriptor(TourbookPlugin.getImageDescriptor(Messages.Image__synch_statistics));
        setToolTipText(Messages.tourCatalog_view_action_synch_chart_years_tooltip);
    }

    @Override
    public void run() {
        _statContainer.actionSynchScale(isChecked());
    }
}
