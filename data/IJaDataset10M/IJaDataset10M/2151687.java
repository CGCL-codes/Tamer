package ca.sqlpower.wabit.swingui.action;

import java.awt.Window;
import ca.sqlpower.wabit.WabitSession;
import ca.sqlpower.wabit.report.Report;

public class CopyReportAction extends CopyAction {

    private Report layout;

    private WabitSession session;

    public CopyReportAction(Report layout, WabitSession session, Window dialogOwner) {
        super(layout, dialogOwner);
        this.layout = layout;
        this.session = session;
    }

    public void copy(String name) {
        Report layoutCopy = new Report(layout, session);
        layoutCopy.setParent(layout.getParent());
        layoutCopy.setName(name);
        session.getWorkspace().addReport(layoutCopy);
    }
}
