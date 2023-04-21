package org.demoiselle.sample.auction5.view.managedbean;

import java.util.Collection;
import java.util.Date;
import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.demoiselle.sample.auction5.bean.Item;
import org.demoiselle.sample.auction5.constant.AliasNavigationRule;
import org.demoiselle.sample.auction5.message.InfoMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import br.gov.framework.demoiselle.core.context.ContextLocator;
import br.gov.framework.demoiselle.core.message.IMessage;
import br.gov.framework.demoiselle.core.message.Severity;
import br.gov.framework.demoiselle.web.message.WebMessageContext;
import br.gov.framework.demoiselle.web.transaction.WebTransactionContext;

public class ItemMBTest {

    private static Logger log = Logger.getLogger(ItemMBTest.class);

    private ItemMB itemMB;

    @Before
    public void setUp() throws Exception {
        log.debug("Starting contexts");
        WebTransactionContext.getInstance().init();
        WebMessageContext.getInstance();
        itemMB = new ItemMB();
    }

    @After
    public void tearDown() throws Exception {
        log.debug("Finalizing contexts");
        WebTransactionContext.getInstance().end();
        WebMessageContext.getInstance().clear();
    }

    /**
	 * Test method for {@link org.demoiselle.sample.auction5.view.managedbean.ItemMB#save()}.
	 */
    @Test
    public void testSave() {
        Item item = new Item();
        item.setDescription("JUnit test:" + new Date().getTime());
        item.setCategory(itemMB.getListCategory().get(0));
        itemMB.setItem(item);
        Assert.assertNull(item.getId());
        Assert.assertEquals(AliasNavigationRule.ALIAS_LIST_ITEM, itemMB.save());
        Assert.assertNotNull(itemMB.getItem().getId());
        item.setDescription(item.getDescription() + "updated");
        Assert.assertEquals(AliasNavigationRule.ALIAS_LIST_ITEM, itemMB.save());
        itemMB.setItem(new Item());
        Assert.assertEquals(AliasNavigationRule.ALIAS_STAY, itemMB.save());
        Assert.assertTrue("No error messages added", hasMessages(Severity.ERROR));
    }

    /**
	 * Test method for {@link org.demoiselle.sample.auction5.view.managedbean.ItemMB#edit()}.
	 */
    @Test
    public void testEdit() {
        Assert.assertEquals(AliasNavigationRule.ALIAS_EDIT_ITEM, itemMB.edit());
    }

    /**
	 * Test method for {@link org.demoiselle.sample.auction5.view.managedbean.ItemMB#delete()}.
	 */
    @Test
    public void testDelete() {
        Assert.assertEquals(AliasNavigationRule.ALIAS_VIEW_ITEM, itemMB.delete());
    }

    /**
	 * Test method for {@link org.demoiselle.sample.auction5.view.managedbean.ItemMB#deleteConfirmed()}.
	 */
    @Test
    public void testDeleteConfirmed() {
        Item item = new Item();
        item.setDescription("JUnit test:" + new Date().getTime());
        item.setCategory(itemMB.getListCategory().get(0));
        itemMB.setItem(item);
        itemMB.save();
        Assert.assertNotNull("Coudn't save for later delete", itemMB.getItem().getId());
        Assert.assertEquals(AliasNavigationRule.ALIAS_LIST_ITEM, itemMB.deleteConfirmed());
        ContextLocator.getInstance().getMessageContext().clear();
        Assert.assertEquals(AliasNavigationRule.ALIAS_STAY, itemMB.deleteConfirmed());
        Assert.assertTrue("No error messages added", hasMessages(Severity.ERROR));
        ContextLocator.getInstance().getMessageContext().clear();
        itemMB.getItem().setId(999999);
        Assert.assertEquals(AliasNavigationRule.ALIAS_STAY, itemMB.deleteConfirmed());
        Assert.assertTrue("No error messages added", hasMessages(Severity.ERROR));
    }

    private boolean hasMessages(Severity severity) {
        Collection<IMessage> msgs = ContextLocator.getInstance().getMessageContext().getMessages();
        if (msgs != null && !msgs.isEmpty()) {
            for (IMessage msg : msgs) {
                if (severity.equals(msg.getSeverity())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * Test method for {@link org.demoiselle.sample.auction5.view.managedbean.ItemMB#list()}.
	 */
    @Test
    public void testList() {
        Assert.assertEquals(AliasNavigationRule.ALIAS_LIST_ITEM, itemMB.list());
        if (itemMB.getListItem().isEmpty()) {
            Assert.assertTrue(itemMB.getListItem().contains(InfoMessage.ADM_ITEM_LIST_LOAD_EMPITY));
        }
    }

    /**
	 * Test method for {@link org.demoiselle.sample.auction5.view.managedbean.ItemMB#cancel()}.
	 */
    @Test
    public void testCancel() {
        Assert.assertEquals(AliasNavigationRule.ALIAS_LIST_ITEM, itemMB.cancel());
    }

    /**
	 * Test method for {@link org.demoiselle.sample.auction5.view.managedbean.ItemMB#newItem()}.
	 */
    @Test
    public void testNewItem() {
        Assert.assertEquals(AliasNavigationRule.ALIAS_EDIT_ITEM, itemMB.newItem());
        Assert.assertNull(itemMB.getItem().getId());
    }
}
