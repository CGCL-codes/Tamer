package net.fortuna.mstor;

import java.util.ArrayList;
import java.util.List;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.UIDFolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Unit tests applicable to UIDFolder support for mstor.
 * 
 * @author Ben Fortuna
 * 
 * <pre>
 * $Id: UIDFolderTest.java,v 1.4 2011/02/19 07:36:01 fortuna Exp $
 *
 * Created on 01/10/2006
 * </pre>
 * 
 */
public class UIDFolderTest extends AbstractMStorTest {

    private static final Log LOG = LogFactory.getLog(UIDFolderTest.class);

    private UIDFolder[] uidFolders;

    /**
     * Default constructor.
     */
    public UIDFolderTest(String method, StoreLifecycle lifecycle, String username, String password) {
        super(method, lifecycle, username, password);
    }

    protected void setUp() throws Exception {
        super.setUp();
        List<UIDFolder> uidFolderList = new ArrayList<UIDFolder>();
        Folder[] folders = store.getDefaultFolder().list();
        for (int i = 0; i < folders.length; i++) {
            folders[i].open(Folder.READ_ONLY);
            uidFolderList.add((UIDFolder) folders[i]);
        }
        uidFolders = uidFolderList.toArray(new UIDFolder[uidFolderList.size()]);
    }

    protected void tearDown() throws Exception {
        for (int i = 0; i < uidFolders.length; i++) {
            ((Folder) uidFolders[i]).close(false);
        }
        super.tearDown();
    }

    /**
     * Tests support for UIDFolder.
     * 
     * @throws MessagingException
     */
    public void testGetUIDValidity() throws MessagingException {
        for (int i = 0; i < uidFolders.length; i++) {
            long uidValidity = uidFolders[i].getUIDValidity();
            assertTrue(uidValidity > 0);
            assertEquals(uidValidity, uidFolders[i].getUIDValidity());
        }
    }

    /**
     * Tests support for UIDFolder.
     * 
     * @throws MessagingException
     */
    public void testGetMessageByUID() throws MessagingException {
        for (int i = 0; i < uidFolders.length; i++) {
            MStorMessage message = (MStorMessage) uidFolders[i].getMessageByUID(1);
            assertEquals(1, message.getUid());
        }
    }

    /**
     * Tests support for UIDFolder.
     * 
     * @throws MessagingException
     */
    public void testGetMessagesByUIDlonglong() throws MessagingException {
        for (int i = 0; i < uidFolders.length; i++) {
            Message[] messages = uidFolders[i].getMessagesByUID(1, 1);
            assertEquals(1, messages.length);
            assertEquals(1, ((MStorMessage) messages[0]).getUid());
            messages = uidFolders[i].getMessagesByUID(1, UIDFolder.LASTUID);
            assertEquals(1, messages.length);
            assertEquals(1, ((MStorMessage) messages[0]).getUid());
        }
    }

    /**
     * Tests support for UIDFolder.
     * 
     * @throws MessagingException
     */
    public void testGetMessagesByUIDArray() throws MessagingException {
        long[] uids = new long[] { 1 };
        for (int i = 0; i < uidFolders.length; i++) {
            Message[] messages = uidFolders[i].getMessagesByUID(uids);
            assertEquals(1, messages.length);
            assertEquals(1, ((MStorMessage) messages[0]).getUid());
        }
    }

    /**
     * Tests support for UIDFolder.
     * 
     * @throws MessagingException
     */
    public void testGetUID() throws MessagingException {
        for (int i = 0; i < uidFolders.length; i++) {
            long uid = uidFolders[i].getUID(((Folder) uidFolders[i]).getMessage(1));
            assertEquals(1, uid);
        }
    }
}
