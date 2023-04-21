package org.gromurph.javascore.gui;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.table.AbstractTableModel;
import org.gromurph.javascore.*;
import org.gromurph.javascore.exception.RatingOutOfBoundsException;
import org.gromurph.javascore.model.Boat;
import org.gromurph.javascore.model.Division;
import org.gromurph.javascore.model.Entry;
import org.gromurph.javascore.model.EntryList;
import org.gromurph.javascore.model.Finish;
import org.gromurph.javascore.model.FinishList;
import org.gromurph.javascore.model.FinishPosition;
import org.gromurph.javascore.model.Penalty;
import org.gromurph.javascore.model.Race;
import org.gromurph.javascore.model.Regatta;
import org.gromurph.javascore.model.ratings.RatingOneDesign;

/**
 * Tests on the Division Panel
 */
public class DialogFinishListEditorTests extends org.gromurph.util.UtilJfcTestCase {

    public DialogFinishListEditorTests(String name) {
        super(name);
    }

    public class LocalDialogFinishListEditor extends DialogFinishListEditor {

        public LocalDialogFinishListEditor() {
            super(null);
        }

        public EntryList getUnFinishedEntries() {
            return fUnFinishedEntries;
        }

        public FinishList getFinishers() {
            return fFinishers;
        }

        public AbstractTableModel getFinishModel() {
            return fFinishModel;
        }
    }

    Regatta regatta;

    Division div;

    Entry e1, e2, e3, e4, e5;

    Race race;

    public void setUp() throws Exception {
        super.setUp();
        regatta = new Regatta();
        div = new Division("Laser", new RatingOneDesign("Laser"), new RatingOneDesign("Laser"));
        e1 = new Entry();
        Boat b = new Boat("boat1", "1", "f1 l1");
        b.putRating(new RatingOneDesign("Laser"));
        e1.setBoat(b);
        try {
            e1.setDivision(div);
        } catch (RatingOutOfBoundsException ex) {
        }
        e2 = new Entry();
        b = new Boat("boat2", "2", "f2 l2");
        b.putRating(new RatingOneDesign("Laser"));
        e2.setBoat(b);
        try {
            e2.setDivision(div);
        } catch (RatingOutOfBoundsException ex) {
        }
        e3 = new Entry();
        b = new Boat("boat3", "3", "f3 l3");
        b.putRating(new RatingOneDesign("Laser"));
        e3.setBoat(b);
        try {
            e3.setDivision(div);
        } catch (RatingOutOfBoundsException ex) {
        }
        e4 = new Entry();
        b = new Boat("boat4", "4", "f4 l4");
        b.putRating(new RatingOneDesign("Laser"));
        e4.setBoat(b);
        try {
            e4.setDivision(div);
        } catch (RatingOutOfBoundsException ex) {
        }
        e5 = new Entry();
        b = new Boat("boat5", "5", "f5 l5");
        b.putRating(new RatingOneDesign("Laser"));
        e5.setBoat(b);
        try {
            e5.setDivision(div);
        } catch (RatingOutOfBoundsException ex) {
        }
        regatta.addEntry(e1);
        regatta.addEntry(e2);
        regatta.addEntry(e3);
        regatta.addEntry(e4);
        regatta.addEntry(e5);
        race = new Race(regatta, "1");
        race.setFinish(new Finish(race, e1, SailTime.NOTIME, new FinishPosition(1), new Penalty(Constants.NO_PENALTY)));
        race.setFinish(new Finish(race, e2, SailTime.NOTIME, new FinishPosition(2), new Penalty(Constants.NO_PENALTY)));
        race.setFinish(new Finish(race, e3, SailTime.NOTIME, new FinishPosition(3), new Penalty(Constants.NO_PENALTY)));
    }

    public void testUnfinishChanges() {
        assertEquals("Number of windows is incorrect", 0, getOpenWindowCount());
        LocalDialogFinishListEditor editor = new LocalDialogFinishListEditor();
        editor.setRace(race);
        displayDialog(editor);
        assertNotNull("DialogFinishListEditor is null", editor);
        JButton buttonRemaining = (JButton) findComponent(JButton.class, "fButtonFinishRemaining", editor);
        assertNotNull("buttonRemaining is null", buttonRemaining);
        JList unfinishList = (JList) findComponent(JList.class, "fListUnFinished", editor);
        assertNotNull("unfinishList is null", unfinishList);
        assertEquals("unfinish jlist should have 2 elements", 2, unfinishList.getModel().getSize());
        assertEquals("unfinishlist should have 2 elements", 2, editor.getUnFinishedEntries().size());
        clickOn(buttonRemaining);
        assertEquals("Number of windows is incorrect", 2, getOpenWindowCount());
        JDialog dialogRemaining = findDialog(DialogFinishListEditor.RES_FINISHREMAININGTITLE);
        JButton buttonDnc = (JButton) findComponent(JButton.class, "DNC", dialogRemaining);
        assertNotNull("dncButton is null", buttonDnc);
        clickOn(buttonDnc);
        assertEquals("Number of windows is incorrect", 1, getOpenWindowCount());
        assertEquals("unfinish jlist should have no elements", 0, unfinishList.getModel().getSize());
        assertEquals("unfinishlist should have no elements", 0, editor.getUnFinishedEntries().size());
        JButton buttonOk = (JButton) findComponent(JButton.class, "fButtonOk", editor);
        assertNotNull("buttonOk is null", buttonOk);
        clickOn(buttonOk);
        assertEquals("Number of windows is incorrect", 0, getOpenWindowCount());
    }
}
