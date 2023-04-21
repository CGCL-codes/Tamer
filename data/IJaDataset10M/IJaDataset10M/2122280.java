package org.lokee.punchcard.persistence;

import java.util.ArrayList;
import junit.framework.TestCase;
import org.lokee.punchcard.PunchCardException;
import org.lokee.punchcard.PunchCardManagerFactory;
import org.lokee.punchcard.config.LookupContextConfig;
import org.lokee.punchcard.config.NativeQueryConfig;

/**
 * @author CLaguerre
 *
 */
public class IPersistentManagerTest extends TestCase {

    /**
	 * Test method for {@link org.lokee.punchcard.persistence.IPersistentManager#detachedSearch(java.lang.String, java.lang.String, java.lang.String, org.lokee.punchcard.config.LookupContextConfig)}.
	 */
    public void testDetachedSearch() {
        PunchCardManagerFactory.initializePunchCard("properties/punchcard");
        NativeQueryConfig query = new NativeQueryConfig();
        query.setQueryString("select * from edbm_examples.company");
        ISessionContext sessionContext = null;
        try {
            sessionContext = PersistentManagerFactory.getInstance(null).fetchNewSessionContext("edbm_examples", true);
        } catch (PersistentException e1) {
            e1.printStackTrace();
            return;
        }
        LookupContextConfig context = new LookupContextConfig();
        context.addNativeQueryConfig(query);
        DetachedCard[] cards = null;
        try {
            cards = PersistentManagerFactory.getInstance(null).detachedSearch("company", "punchcard_examples", "", "edbm_examples", new String[] { "company" }, context, new ArrayList(), sessionContext);
        } catch (PersistentException e) {
            e.printStackTrace();
        } catch (PunchCardException e) {
            e.printStackTrace();
        } finally {
            sessionContext.closeSession();
        }
        System.out.println(cards);
        for (int i = 0; i < cards.length; i++) {
            System.out.println(cards[i]);
        }
    }
}
