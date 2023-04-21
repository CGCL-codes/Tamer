package de.javatt.data.scenario.swt;

import java.awt.AWTException;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import de.javatt.testrunner.finder.ComponentNotFoundException;
import de.javatt.testrunner.finder.SWTClassNameFinder;
import de.javatt.testrunner.finder.SWTFinderGroup;
import de.javatt.testrunner.finder.SWTSimpleDataFinder;
import de.javatt.testrunner.finder.SWTTextFinder;
import junit.framework.TestCase;

public class TestSWTMouseRobotTimeOutStopped extends TestCase {

    public void testSWTMouseRobotTimeOutStopped() {
        DummyGUICreator creator = new DummyGUICreator();
        creator.createGUI();
        DummyMouseRobot robot = new DummyMouseRobot();
        SWTAccessor accessor = robot.getSWTAccessor();
        assertNotNull(accessor);
        try {
            SWTClassNameFinder classFinder = new SWTClassNameFinder(MenuItem.class.getName());
            SWTTextFinder dataFinder = new SWTTextFinder("File");
            SWTFinderGroup groupFinder = new SWTFinderGroup();
            groupFinder.addSWTFinder(classFinder);
            groupFinder.addSWTFinder(dataFinder);
            Widget widget = null;
            widget = accessor.findWidget(groupFinder);
            robot.click(widget);
            dataFinder = new SWTTextFinder("Show Popup");
            groupFinder = new SWTFinderGroup();
            groupFinder.addSWTFinder(classFinder);
            groupFinder.addSWTFinder(dataFinder);
            widget = null;
            widget = accessor.findWidget(groupFinder);
            robot.click(widget);
            classFinder = new SWTClassNameFinder(Shell.class.getName());
            dataFinder = new SWTTextFinder("Popup");
            groupFinder = new SWTFinderGroup();
            groupFinder.addSWTFinder(classFinder);
            groupFinder.addSWTFinder(dataFinder);
            widget = null;
            widget = accessor.findWidget(groupFinder, 7000);
            assertNotNull(widget);
        } catch (ComponentNotFoundException notFound) {
            assertNull(notFound);
        } catch (AWTException awtEx) {
            assertNull(awtEx);
        }
    }
}
