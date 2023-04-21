package net.timedoctor.ui.statistics.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import net.timedoctor.ui.ITimeDoctorUIConstants;
import net.timedoctor.ui.statistics.IStatisticsViewPage;

public class PrintAction extends Action {

    private IStatisticsViewPage statisticsViewPage;

    public PrintAction(final IStatisticsViewPage statisticsViewPage) {
        this.statisticsViewPage = statisticsViewPage;
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(ITimeDoctorUIConstants.TD_UI_PLUGIN, ITimeDoctorUIConstants.LOCAL_TOOLBAR_ENABLED_IMG_PATH + "print.gif"));
        setToolTipText("Print");
    }

    @Override
    public void run() {
        statisticsViewPage.print();
    }
}
