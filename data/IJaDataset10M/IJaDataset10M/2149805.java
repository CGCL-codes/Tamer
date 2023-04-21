package org.doit.muffin;

import java.util.Enumeration;
import java.util.Vector;
import org.doit.util.*;

class Janitor implements Runnable {

    private Vector cleanable = new Vector();

    public void add(Cleanable c) {
        cleanable.addElement(c);
    }

    public void run() {
        Thread.currentThread().setName("Janitor");
        for (; ; ) {
            try {
                Thread.sleep(30 * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (Enumeration e = cleanable.elements(); e.hasMoreElements(); ) {
                ((Cleanable) e.nextElement()).clean();
            }
            Http.clean();
            FilterManager manager = Main.getFilterManager();
            Enumeration configs = manager.configs.keys();
            while (configs.hasMoreElements()) {
                String config = (String) configs.nextElement();
                Vector enabled = manager.getEnabledFilters(config);
                for (int i = 0; i < enabled.size(); i++) {
                    FilterFactory ff = (FilterFactory) enabled.elementAt(i);
                    if (ff instanceof AutoSaveable) {
                        ((AutoSaveable) ff).autoSave();
                    }
                }
            }
        }
    }
}
