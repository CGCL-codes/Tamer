package net.sourceforge.hourglass.acceptance.gui.framework;

import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.eventdata.MouseEventData;
import junit.extensions.jfcunit.finder.AbstractWindowFinder;
import junit.extensions.jfcunit.finder.JMenuItemFinder;
import net.sourceforge.hourglass.swingui.Strings;

/**
 * 
 * @author Michael K. Grant <mike@acm.jhu.edu>
 */
public class MainScreen extends BaseScreen {

    private JFrame m_frame;

    public MainScreen(JFCTestCase testCase) throws Exception {
        super(testCase);
        System.out.println("Initializing main screen.");
        AbstractWindowFinder finder = new AbstractWindowFinder(null) {

            public boolean testComponent(Component c) {
                return (c instanceof JFrame) && c.getName().equals(Strings.SUMMARY_FRAME);
            }
        };
        m_frame = (JFrame) finder.find();
        if (m_frame == null) {
            throw new RuntimeException("Could not find main frame (got null).");
        }
        System.out.println("Frame: " + m_frame.getName());
    }

    public void clickMenu(String[] menuPath) throws Exception {
        if (menuPath.length == 0) {
            throw new IllegalArgumentException("Menu path must be nonzero.");
        }
        JMenuItemFinder finder;
        for (int i = 0; i < menuPath.length; ++i) {
            finder = new JMenuItemFinder(menuPath[i]);
            JMenuItem menuItem = (JMenuItem) finder.find();
            getHelper().enterClickAndLeave(new MouseEventData(getTestCase(), menuItem));
        }
    }
}
