package org.commsuite.managers.impl;

import java.util.Date;
import java.util.List;
import org.commsuite.enums.Direction;
import org.commsuite.enums.FormatType;
import org.commsuite.enums.Priority;
import org.commsuite.enums.Status;
import org.commsuite.managers.ContentsManager;
import org.commsuite.managers.MessageManager;
import org.commsuite.managers.SentContentManager;
import org.commsuite.model.Contents;
import org.commsuite.model.Message;
import org.commsuite.model.SentContent;
import org.commsuite.testing.AbstractCommunicationsSuiteTestCase;
import org.commsuite.util.SpringMiddlewareContext;

/**
 * @author Marek Musielak
 * @author Marcin Zduniak
 */
public class ContentsManagerTest extends AbstractCommunicationsSuiteTestCase {

    private ContentsManager mgr;

    private MessageManager msgMgr;

    private SentContentManager scMgr;

    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        mgr = SpringMiddlewareContext.getContentsManager();
        msgMgr = SpringMiddlewareContext.getMessageManager();
        scMgr = SpringMiddlewareContext.getSentContentManager();
    }

    public Contents createContents(int i) {
        final Contents contents = new Contents();
        contents.setData(new byte[i]);
        contents.setDescription("desc" + i);
        contents.setMimeType("mime" + i);
        return contents;
    }

    public Message createMessage(int i) {
        Message msg = new Message();
        msg.setSapID("sapid" + i);
        msg.setCreationDate(new Date());
        msg.setDirection(Direction.INBOUND);
        msg.setFormatType(FormatType.EMAIL);
        msg.setLastProcessDate(new Date());
        msg.setPriority(Priority.LEVEL_0);
        msg.setReceiver("Receiver");
        msg.setSendDate(new Date());
        msg.setSender("Sender");
        msg.setStatus(Status.DELIVERED_TO_SAP_SERVER);
        return msg;
    }

    public void testSaveAndDeleteContents1() {
        Contents contents = createContents(1);
        contents = mgr.saveContents(contents);
        Long id = contents.getId();
        assertEquals("Contents should be the same", contents, mgr.getContents(contents.getId().toString()));
        mgr.deleteContents(contents.getId().toString());
        assertNull("There should not be contents with id " + id, mgr.getContents(id.toString()));
        for (SentContent sc : scMgr.getSentContentsList()) {
            System.out.println(sc);
        }
    }

    public void testSaveAndDeleteContents2() {
        Contents contents = createContents(2);
        contents = mgr.saveContents(contents);
        Long id = contents.getId();
        assertEquals("Contents should be the same", contents, mgr.getContents(contents.getId().toString()));
        mgr.deleteContents(contents);
        assertNull("There should not be contents with id " + id, mgr.getContents(id.toString()));
    }

    public void testGetContents() {
        Contents contents1 = createContents(3);
        contents1 = mgr.saveContents(contents1);
        Contents contents2 = createContents(4);
        contents2 = mgr.saveContents(contents2);
        List<Contents> resultList = mgr.getContentsList();
        if (!resultList.contains(contents1) || !resultList.contains(contents2)) fail("Returned list does not contains contents1 and contents2");
        assertEquals("Returned list has not size 2", 2, resultList.size());
        mgr.deleteContents(contents1);
        mgr.deleteContents(contents2);
    }

    public void testGetContentsByMessage() {
        final Message msg = createMessage(1);
        final Message msg2 = createMessage(2);
        final Contents contents1 = createContents(5);
        final Contents contents2 = createContents(6);
        final Contents contents3 = createContents(7);
        msg.addContents(contents1);
        msg.addContents(contents2, "internal_id");
        msg2.addContents(contents3);
        msgMgr.saveMessage(msg);
        msgMgr.saveMessage(msg2);
        assertEquals("Message should have exactly 2 content objects", 2, msg.listContents().size());
        assertEquals("Method should return 2 sentContents", 2, scMgr.getSentContentsByMessage(msg.getId().toString()).size());
        assertEquals("Method should return 2 contents", 2, mgr.getContentsByMessage(msg.getId().toString()).size());
        assertNotNull("Sent content of internal_id 'internal_id' should not be null", scMgr.getSentContentByInternalId("internal_id"));
        ((SentContent) (msg2.getSentContents().toArray()[0])).setInternalId("InternalId");
        scMgr.saveSentContent((SentContent) (msg2.getSentContents().toArray()[0]));
        assertEquals("SentContents must be the same", (SentContent) (msg2.getSentContents().toArray()[0]), scMgr.getSentContentByInternalId("InternalId"));
        final List<Contents> resultList = mgr.getContentsByMessage(msg.getId().toString());
        if (!resultList.contains(contents1) || !resultList.contains(contents2)) {
            fail("Returned list does not contains contents1 and contents2");
        }
        assertEquals("list has size 2", 2, resultList.size());
        msgMgr.deleteMessage(msg);
        msgMgr.deleteMessage(msg2);
    }

    public void testMessageRemoval2() {
        Message msg1 = createMessage(3);
        Message msg2 = createMessage(4);
        Message msg3 = createMessage(5);
        final Contents contents1 = createContents(8);
        final Contents contents2 = createContents(9);
        final Contents contents3 = createContents(10);
        msg1.addContents(contents1);
        msg1.addContents(contents2);
        msg1.addContents(contents3);
        msg2.addContents(contents1);
        msg2.addContents(contents2);
        msg3.addContents(contents1);
        msg3.addContents(contents2);
        msg1 = msgMgr.saveMessage(msg1);
        msg2 = msgMgr.saveMessage(msg2);
        msg3 = msgMgr.saveMessage(msg3);
        assertEquals("there should be 7 SentContents in db", 7, scMgr.getSentContentsList().size());
        assertEquals("there should be 3 Contents in db", 3, mgr.getContentsList().size());
        assertEquals("msg1 has 3 contents", 3, mgr.getContentsByMessage(msg1.getId().toString()).size());
        assertEquals("msg2 has 2 contents", 2, mgr.getContentsByMessage(msg2.getId().toString()).size());
        assertEquals("msg3 has 2 contents", 2, mgr.getContentsByMessage(msg3.getId().toString()).size());
        msgMgr.deleteMessage(msg1);
        assertEquals("there should be 2 Messages in db", 2, msgMgr.getMessages().size());
        assertEquals("there should be 5 SentContents in db", 4, scMgr.getSentContentsList().size());
        assertEquals("there should be 2 Contents in db", 2, mgr.getContentsList().size());
    }

    public void testManyReceivers() {
        Message msg1 = createMessage(3);
        Message msg2 = createMessage(4);
        Message msg3 = createMessage(5);
        final Contents contents1 = createContents(8);
        final Contents contents2 = createContents(9);
        final Contents contents3 = createContents(10);
        msg1.addContents(contents1);
        msg1.addContents(contents2);
        msg1.addContents(contents3);
        msg2.addContents(contents1);
        msg2.addContents(contents2);
        msg3.addContents(contents1);
        msg3.addContents(contents2);
        msg1 = msgMgr.saveMessage(msg1);
        msg2 = msgMgr.saveMessage(msg2);
        msg3 = msgMgr.saveMessage(msg3);
        assertEquals("there should be 7 SentContents in db", 7, scMgr.getSentContentsList().size());
        assertEquals("there should be 3 Contents in db", 3, mgr.getContentsList().size());
        assertEquals("msg1 has 3 contents", 3, mgr.getContentsByMessage(msg1.getId().toString()).size());
        assertEquals("msg2 has 2 contents", 2, mgr.getContentsByMessage(msg2.getId().toString()).size());
        assertEquals("msg3 has 2 contents", 2, mgr.getContentsByMessage(msg3.getId().toString()).size());
        msgMgr.deleteMessage(msg2);
        assertEquals("there should be 2 Messages in db", 2, msgMgr.getMessages().size());
        assertEquals("there should be 5 SentContents in db", 5, scMgr.getSentContentsList().size());
        assertEquals("there should be 3 Contents in db", 3, mgr.getContentsList().size());
        msgMgr.deleteMessage(msg1);
        assertEquals("there should be 1 Message in db", 1, msgMgr.getMessages().size());
        assertEquals("there should be 2 SentContents in db", 2, scMgr.getSentContentsList().size());
        assertEquals("there should be 2 Contents in db", 2, mgr.getContentsList().size());
        msgMgr.deleteMessage(msg3);
        assertEquals("there should be no Message in db", 0, msgMgr.getMessages().size());
        assertEquals("there should be no SentContents in db", 0, scMgr.getSentContentsList().size());
        assertEquals("there should be no Contents in db", 0, mgr.getContentsList().size());
    }
}
