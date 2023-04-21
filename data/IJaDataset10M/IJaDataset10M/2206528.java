package net.sourceforge.jcoupling2.dao.simple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.jcoupling2.exception.JCouplingException;
import net.sourceforge.jcoupling2.persistence.DataMapper;
import net.sourceforge.jcoupling2.persistence.Message;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class TestMessage {

    private static final Logger logger = Logger.getLogger(TestMessage.class);

    DataMapper dMapper = null;

    Message message = new Message();

    Integer msgid = null;

    @Before
    public void setUp() throws JCouplingException {
        dMapper = new DataMapper();
        message.setBody("<JCMessage><Channel name=\"TestChannel1\" /><TimeStamp time=\"12.01.2010\" />" + "<Message><Ort>Raum 120</Ort><Name>Bernd M�ller</Name></Message></JCMessage>");
        msgid = new Integer(1111);
    }

    @Test
    public void tearDown() throws Exception {
        message = null;
        dMapper = null;
    }

    @Test
    public void testAddMessage() throws JCouplingException {
        logger.debug("Adding a message ...");
        try {
            message.setID(new Integer(dMapper.addMessage(message)));
            logger.debug("Message(ID: " + message.getID() + ") was added!");
        } catch (JCouplingException jex) {
        }
        logger.debug("Done!");
    }

    @Test
    public void testRetrieveMessages() throws JCouplingException {
        ArrayList<Message> array = new ArrayList<Message>();
        List<Integer> numberarray = Arrays.asList(new Integer(297), new Integer(298), new Integer(299));
        logger.debug("Retrieving messages ...");
        array = dMapper.retrieveMessages(numberarray);
        logger.debug("Retrieved Ids:");
        for (int i = 0; i < array.size(); i++) {
            logger.debug("Message " + (i + 1));
            logger.debug("ID: " + array.get(i).getID());
            logger.debug("ChannelID: " + array.get(i).getChannelID());
            logger.debug("TimeStamp: " + array.get(i).getTimeStamp());
        }
        logger.debug("Done");
    }

    @Test
    public void testRemoveMessage() throws JCouplingException {
        message.setID(msgid);
        logger.debug("Now Removing the message(ID: " + msgid + ") ...");
        dMapper.removeMessage(message);
        logger.debug("Done");
    }
}
