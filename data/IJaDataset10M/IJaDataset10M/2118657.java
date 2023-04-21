package net.fortuna.mstor;

import java.util.ArrayList;
import java.util.List;
import javax.mail.Folder;
import javax.mail.Store;
import junit.framework.TestCase;

/**
 * Abstract base class for MStor unit tests. Provides setup of a mail store.
 * 
 * @author Ben Fortuna
 * 
 * <pre>
 * $Id: AbstractMStorTest.java,v 1.6 2011/02/19 07:36:01 fortuna Exp $
 *
 * Created on 6/05/2006
 * </pre>
 * 
 */
public abstract class AbstractMStorTest extends TestCase {

    protected StoreLifecycle lifecycle;

    protected Store store;

    protected String username;

    protected String password;

    protected String[] folderNames;

    public AbstractMStorTest(String method, StoreLifecycle lifecycle, String username, String password) {
        super(method);
        this.lifecycle = lifecycle;
        this.username = username;
        this.password = password;
    }

    protected void setUp() throws Exception {
        super.setUp();
        lifecycle.startup();
        store = lifecycle.getStore();
        store.connect(username, password);
        List<String> folderList = new ArrayList<String>();
        Folder[] folders = store.getDefaultFolder().list();
        if (folders.length == 0) {
            for (int i = 0; i < 10; i++) {
                Folder f = store.getDefaultFolder().getFolder("folder_" + i);
                f.create(Folder.HOLDS_MESSAGES);
            }
            folders = store.getDefaultFolder().list();
        }
        for (int i = 0; i < folders.length; i++) {
            folderList.add(folders[i].getName());
        }
        folderNames = folderList.toArray(new String[folderList.size()]);
    }

    protected void tearDown() throws Exception {
        store.close();
        lifecycle.shutdown();
        super.tearDown();
    }
}
