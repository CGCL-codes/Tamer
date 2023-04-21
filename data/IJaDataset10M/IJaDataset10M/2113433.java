package bouttime.gui;

import bouttime.model.Group;
import bouttime.model.Wrestler;
import org.fest.swing.core.matcher.DialogMatcher;
import org.fest.swing.core.matcher.JButtonMatcher;
import org.fest.swing.fixture.DialogFixture;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the application's 'Free List' tab.
 */
public class FreeListTabTest extends BaseTabTest {

    @Override
    protected void onSetUp() {
        super.onSetUp();
        if (!setUpExisting(frame, "./test/bouttime/resources/potus.xml")) {
            return;
        }
        view = getView(frame);
        dao = getDao(frame);
        assertEquals(42, dao.getUngroupedWrestlers().size());
        frame.tabbedPane("mainTabPane").selectTab("Free List");
        frame.textBox("count").requireText("42");
    }

    @Test
    public void testFilters() {
        frame.comboBox("classComboBox").selectItem("Open");
        frame.textBox("count").requireText("21");
        frame.comboBox("divComboBox").selectItem("1");
        frame.textBox("count").requireText("11");
        frame.comboBox("classComboBox").selectItem("All");
        frame.textBox("count").requireText("21");
        frame.comboBox("divComboBox").selectItem("3");
        frame.textBox("count").requireText("0");
    }

    @Test
    public void testAddToGroup() {
        frame.table("table").selectRows(0);
        Wrestler w = getWrestlerFromDao(frame.table("table"), 0, dao);
        assertNotNull(w);
        assertNull(w.getGroup());
        frame.button("createGroupButton").click();
        frame.textBox("count").requireText("41");
        assertEquals(41, dao.getUngroupedWrestlers().size());
        Group grp = w.getGroup();
        assertNotNull(grp);
        assertEquals(1, dao.getAllGroups().size());
        frame.table("table").selectRows(0);
        frame.label("selectedCounterLabel").requireText("1");
        w = getWrestlerFromDao(frame.table("table"), 0, dao);
        assertNotNull(w);
        assertNull(w.getGroup());
        frame.button("addToGroupButton").click();
        DialogFixture dialog = frame.dialog(DialogMatcher.withTitle("Add wrestler to group"));
        dialog.comboBox().selectItem(0);
        dialog.button(JButtonMatcher.withName("OptionPane.button").andText("OK")).click();
        frame.textBox("count").requireText("40");
        assertEquals(40, dao.getUngroupedWrestlers().size());
        assertEquals(grp, w.getGroup());
        assertEquals(1, dao.getAllGroups().size());
        frame.table("table").selectRows(0, 1, 2);
        frame.label("selectedCounterLabel").requireText("3");
        Wrestler w1 = getWrestlerFromDao(frame.table("table"), 0, dao);
        Wrestler w2 = getWrestlerFromDao(frame.table("table"), 1, dao);
        Wrestler w3 = getWrestlerFromDao(frame.table("table"), 2, dao);
        assertNotNull(w1);
        assertNotNull(w2);
        assertNotNull(w3);
        assertNull(w1.getGroup());
        assertNull(w2.getGroup());
        assertNull(w3.getGroup());
        frame.button("addToGroupButton").click();
        dialog = frame.dialog(DialogMatcher.withTitle("Add wrestler to group"));
        dialog.comboBox().selectItem(0);
        dialog.button(JButtonMatcher.withName("OptionPane.button").andText("OK")).click();
        frame.textBox("count").requireText("37");
        assertEquals(37, dao.getUngroupedWrestlers().size());
        assertEquals(grp, w1.getGroup());
        assertEquals(grp, w2.getGroup());
        assertEquals(grp, w3.getGroup());
        assertEquals(1, dao.getAllGroups().size());
        w = getWrestlerFromDao(frame.table("table"), 0, dao);
        assertNotNull(w);
        assertNull(w.getGroup());
        frame.table("table").selectRows(0).rightClick();
        frame.label("selectedCounterLabel").requireText("1");
        frame.menuItem("addToGroupMenuItem").click();
        dialog = frame.dialog(DialogMatcher.withTitle("Add wrestler to group"));
        dialog.comboBox().selectItem(0);
        dialog.button(JButtonMatcher.withName("OptionPane.button").andText("OK")).click();
        frame.textBox("count").requireText("36");
        assertEquals(36, dao.getUngroupedWrestlers().size());
        assertEquals(grp, w.getGroup());
        assertEquals(1, dao.getAllGroups().size());
    }

    @Test
    public void testCreateGroup() {
        frame.table("table").selectRows(0);
        frame.label("selectedCounterLabel").requireText("1");
        Wrestler w = getWrestlerFromDao(frame.table("table"), 0, dao);
        assertNotNull(w);
        assertNull(w.getGroup());
        frame.button("createGroupButton").click();
        frame.textBox("count").requireText("41");
        assertEquals(41, dao.getUngroupedWrestlers().size());
        assertNotNull(w.getGroup());
        assertEquals(1, dao.getAllGroups().size());
        frame.table("table").selectRows(0, 1, 2);
        frame.label("selectedCounterLabel").requireText("3");
        Wrestler w1 = getWrestlerFromDao(frame.table("table"), 0, dao);
        Wrestler w2 = getWrestlerFromDao(frame.table("table"), 1, dao);
        Wrestler w3 = getWrestlerFromDao(frame.table("table"), 2, dao);
        assertNotNull(w1);
        assertNotNull(w2);
        assertNotNull(w3);
        assertNull(w1.getGroup());
        assertNull(w2.getGroup());
        assertNull(w3.getGroup());
        frame.button("createGroupButton").click();
        frame.textBox("count").requireText("38");
        assertEquals(38, dao.getUngroupedWrestlers().size());
        assertNotNull(w.getGroup());
        assertEquals(2, dao.getAllGroups().size());
        w = getWrestlerFromDao(frame.table("table"), 0, dao);
        assertNotNull(w);
        assertNull(w.getGroup());
        frame.table("table").selectRows(0).rightClick();
        frame.label("selectedCounterLabel").requireText("1");
        frame.menuItem("createGroupMenuItem").click();
        frame.textBox("count").requireText("37");
        assertEquals(37, dao.getUngroupedWrestlers().size());
        assertNotNull(w.getGroup());
        assertEquals(3, dao.getAllGroups().size());
    }
}
