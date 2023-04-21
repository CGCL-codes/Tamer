package com.simpledata.bc.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import com.simpledata.bc.datamodel.Tarification;
import com.simpledata.bc.datamodel.WorkSheet;
import com.simpledata.bc.tools.Lang;

/**
 * Clear a Simulation<BR> 
 * Remove all Transactions, Futures and Assets from a tarification
 */
public class ActionClearSimulation {

    /**
     * get a MenuItem that will enable to Clear A tarification
     */
    public static JMenuItem getMenuItem(final Tarification t) {
        JMenu mi = new JMenu(Lang.translate("Clear"));
        JMenuItem confirm = new JMenuItem(Lang.translate("Remove all user options."));
        mi.add(confirm);
        confirm.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doAction(t);
            }
        });
        return mi;
    }

    /**
     * Call WorkSheet.Cleareable.clear(); on all
     * WorkSheet.Cleareable of this tarification<BR>
     * ACTION: remove all user options 
     */
    public static void doAction(Tarification t) {
        t.comCalc().groupedStart();
        Iterator i = t.getAllInstancesOf(WorkSheet.Cleareable.class).iterator();
        while (i.hasNext()) {
            ((WorkSheet.Cleareable) (i.next())).clear();
        }
        t.comCalc().groupedStop();
    }
}
