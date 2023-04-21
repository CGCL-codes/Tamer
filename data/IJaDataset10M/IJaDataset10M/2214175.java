package ircam.jmax.toolkit.menus;

import java.util.*;
import javax.swing.AbstractButton;
import javax.swing.JMenuItem;
import java.awt.event.KeyEvent;
import ircam.jmax.fts.*;
import ircam.jmax.toolkit.actions.*;

public class DefaultHelpMenu extends EditorMenu {

    private JMenuItem aboutItem = null;

    protected int objectSummariesOffset = 0;

    public DefaultHelpMenu() {
        super("Help");
        setHorizontalTextPosition(AbstractButton.LEFT);
        setMnemonic(KeyEvent.VK_H);
        add(DefaultActions.statisticsAction);
    }

    public void updateMenu() {
        if (FtsHelpPatchTable.getNumSummaries() > numEntries) {
            Enumeration en = FtsHelpPatchTable.getSummaries();
            int count = 1;
            while (en.hasMoreElements()) {
                final String str = (String) en.nextElement();
                if (count > numEntries) {
                    insert(new OpenHelpSummaryAction(FtsHelpPatchTable.getHelpSummaryPatch(str), str), count - 1 + objectSummariesOffset);
                    numEntries++;
                }
                count++;
            }
            insertSeparator(count - 1 + objectSummariesOffset);
        }
    }

    private int numEntries = 0;
}
