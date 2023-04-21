package game.report.subreports;

import game.report.ISubreport;
import game.report.SRRenderer;
import game.report.SubreportContainer;
import game.report.srobjects.*;

/**
 * Created by IntelliJ IDEA.
 * User: drchaj1
 * Date: Oct 6, 2009
 * Time: 1:44:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestSR implements ISubreport {

    private final int level;

    public TestSR() {
        level = 1;
    }

    public TestSR(ISubreport parent) {
        level = 1;
    }

    public SubreportContainer getContainer() {
        SubreportContainer sc = new SubreportContainer();
        ISRObject h1 = new SRHeader("H1", "Heading 1");
        SRTable tab1 = new SRTable("TAB1", "Tab.1 caption");
        tab1.set(0, 0, "1234567890");
        tab1.set(0, 1, "3434.0");
        tab1.set(0, 2, "-1.123455432155555");
        tab1.set(0, 3, "uh");
        tab1.set(0, 4, "3434.4");
        tab1.set(0, 5, "434.232");
        tab1.set(0, 6, "3434");
        tab1.set(0, 7, "3434");
        tab1.set(0, 8, "3434");
        tab1.set(1, 0, "123456789009876");
        tab1.set(1, 1, "44");
        tab1.set(1, 2, "44");
        tab1.set(1, 3, "44");
        tab1.set(1, 4, "44");
        tab1.set(1, 5, "44");
        tab1.set(1, 6, "44");
        tab1.set(1, 7, "44");
        tab1.set(1, 8, "44");
        tab1.set(2, 0, "123456789009876");
        tab1.set(2, 1, "44");
        tab1.set(2, 2, "44");
        tab1.set(2, 3, "44");
        tab1.set(2, 4, "44");
        tab1.set(2, 5, "44");
        tab1.set(2, 6, "44");
        tab1.set(2, 7, "44");
        tab1.set(2, 8, "44");
        tab1.set(3, 0, "12345678900987654321");
        tab1.set(3, 1, "44");
        tab1.set(3, 2, "44");
        tab1.set(3, 3, "44");
        tab1.set(3, 4, "44");
        tab1.set(3, 5, "44");
        tab1.set(3, 6, "44");
        tab1.set(3, 7, "44");
        tab1.set(3, 8, "44");
        tab1.set(4, 0, "12345678900987654321");
        tab1.set(4, 1, "44");
        tab1.set(4, 2, "44");
        tab1.set(4, 3, "44");
        tab1.set(4, 4, "44");
        tab1.set(4, 5, "44");
        tab1.set(4, 6, "44");
        tab1.set(4, 7, "44");
        tab1.set(4, 8, "44");
        SRTable tab2 = new SRTable("TAB2", "Tab.2 caption");
        tab2.set(0, 0, "1");
        tab2.set(0, 1, "2");
        tab2.set(1, 0, "3");
        tab2.set(1, 1, "4");
        ISRObject t1 = new SRText("T1", "Text 1");
        ISRObject t2 = new SRText("T2", "Text 2");
        sc.addSRObject(t1);
        sc.addSRObject(tab1);
        return sc;
    }

    @Override
    public String getName() {
        return "Test Subreport";
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public ISRObjectRenderer getRenderer(SRRenderer renderer) {
        return renderer.createSubreportRenderer(this);
    }
}
