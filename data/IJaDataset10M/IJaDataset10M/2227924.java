package org.xorm.tools.editor;

import javax.jdo.*;
import org.xorm.XORM;

public class Main {

    public static void main(String[] argv) {
        PersistenceManagerFactory factory;
        if (argv.length > 0) {
            factory = XORM.newPersistenceManagerFactory(argv[0]);
        } else {
            factory = XORM.newPersistenceManagerFactory();
        }
        PersistenceManager mgr = factory.getPersistenceManager();
        mgr.currentTransaction().begin();
        MainMenu menu = new MainMenu(mgr);
        menu.go();
    }
}
