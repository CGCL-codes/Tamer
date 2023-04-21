package ca.sqlpower.wabit.report;

import ca.sqlpower.wabit.AbstractWabitObjectTest;
import ca.sqlpower.wabit.WabitObject;
import ca.sqlpower.wabit.report.Guide.Axis;

public class GuideTest extends AbstractWabitObjectTest {

    Guide guide;

    @Override
    public Class<? extends WabitObject> getParentClass() {
        return Page.class;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        guide = new Guide(Axis.HORIZONTAL, 123);
        Report report = new Report("report");
        report.getPage().addGuide(guide);
        getWorkspace().addReport(report);
    }

    @Override
    public WabitObject getObjectUnderTest() {
        return guide;
    }
}
