package de.nava.informa.impl.hibernate;

import java.net.URL;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.utils.InformaHibernateTestCase;

/**
 * Test for making channel categories persistent while using the hibernate
 * mapping backend.
 *
 * @author Niko Schmuck
 */
public class TestUnreadItems extends InformaHibernateTestCase {

    private static Log logger = LogFactory.getLog(TestUnreadItems.class);

    public TestUnreadItems(String name) {
        super("TestUnreadItems", name);
    }

    public void testUnreadItems() throws Exception {
        ChannelBuilder builder = new ChannelBuilder(session);
        int chId = -1;
        String chanName = "Unread Tester";
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ChannelIF channel = builder.createChannel(chanName);
            channel.setDescription("Test Channel: " + chanName);
            session.save(channel);
            int items;
            for (items = 0; items < 20; items++) {
                boolean unreadflag = ((items > 0 && items < 5)) || (items > 10 && items < 15);
                String desc = unreadflag ? "UnreadItem" : "ReadItem";
                ItemIF anItem = builder.createItem(channel, "Item: " + items, desc, new URL("http://www.sf.net"));
                anItem.setUnRead(unreadflag);
            }
            session.save(channel);
            chId = (int) channel.getId();
            tx.commit();
        } catch (HibernateException he) {
            logger.warn("trying to rollback the transaction");
            if (tx != null) tx.rollback();
            throw he;
        }
        assertTrue("No valid channel created.", chId >= 0);
        try {
            logger.info("Searching for channel " + chId);
            Object result = session.get(Channel.class, new Long(chId));
            assertNotNull(result);
            ChannelIF c = (ChannelIF) result;
            logger.info("retrieved channel --> " + c);
            Iterator itemsIter = c.getItems().iterator();
            while (itemsIter.hasNext()) {
                ItemIF anItem = (ItemIF) itemsIter.next();
                if (anItem.getUnRead()) assertTrue("Item marked as Unread isn't supposed to be Unread", anItem.getDescription().compareTo("UnreadItem") == 0);
                if (!anItem.getUnRead()) assertTrue("Item marked as Read isn't supposed to be Read", anItem.getDescription().compareTo("ReadItem") == 0);
            }
        } catch (HibernateException he) {
            logger.warn("Error while querying for channel");
            throw he;
        }
    }
}
