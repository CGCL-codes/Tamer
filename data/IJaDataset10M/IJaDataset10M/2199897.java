package net.fortuna.mstor.tag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import junit.framework.TestCase;
import net.fortuna.mstor.StoreLifecycle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Unit tests for Tag support.
 * 
 * @author Ben Fortuna
 * 
 * <pre>
 * $Id: TagTest.java,v 1.4 2011/02/19 07:36:01 fortuna Exp $
 *
 * Created on 6/05/2006
 * </pre>
 * 
 */
public class TagTest extends TestCase {

    private static final Log LOG = LogFactory.getLog(TagTest.class);

    private StoreLifecycle lifecycle;

    private Store store;

    private String username;

    private String password;

    private String[] folderNames;

    /**
     * Default constructor.
     */
    public TagTest(String method, StoreLifecycle lifecycle, String username, String password) {
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

    /**
     * Logs a summary of all messages in the specified folder.
     * 
     * @param folder
     * @throws MessagingException
     */
    private void logMessages(final Folder folder) throws MessagingException {
        if (!folder.isOpen()) {
            folder.open(Folder.READ_ONLY);
        }
        for (int i = 1; i <= folder.getMessageCount(); i++) {
            Message message = folder.getMessage(i);
            LOG.info("Message [" + i + "]: " + message.getSubject());
            for (Iterator<String> it = Tags.getTags(message).iterator(); it.hasNext(); ) {
                LOG.info("Tag: " + it.next());
            }
        }
    }

    /**
     * A unit test that tags a message.
     */
    public void testTagMessage() throws MessagingException {
        for (int n = 0; n < folderNames.length; n++) {
            Folder folder = store.getFolder(folderNames[n]);
            folder.open(Folder.READ_WRITE);
            String tag = "Test 1";
            Message message = folder.getMessage(1);
            Tags.addTag(tag, message);
            assertTrue(Tags.getTags(message).contains(tag));
            logMessages(folder);
            folder.close(false);
        }
    }

    /**
     * A unit test that untags a message.
     */
    public void testUntagMessage() throws MessagingException {
        for (int n = 0; n < folderNames.length; n++) {
            Folder folder = store.getFolder(folderNames[n]);
            folder.open(Folder.READ_WRITE);
            String tag = "Test 1";
            Message message = folder.getMessage(1);
            Tags.addTag(tag, message);
            assertTrue(Tags.getTags(message).contains(tag));
            Tags.removeTag(tag, message);
            assertFalse(Tags.getTags(message).contains(tag));
            logMessages(folder);
            folder.close(false);
        }
    }
}
