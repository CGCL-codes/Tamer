package ch.bbv.unittests.tests;

import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import ch.bbv.application.Application;
import ch.bbv.dog.DataObjectHandler;
import ch.bbv.unittests.indexedprops.*;

public class IndexedPropertyTest {

    @Test
    public void test() {
        Log log = LogFactory.getLog(IndexedPropertyTest.class);
        log.info("*** Indexed Properties Test ***");
        log.info("    Creating a few test objects...");
        IPRoot root = new IPRoot();
        for (int i = 0; i < 10; i++) {
            IPTopItem topItem = new IPTopItem();
            IPSingleItem singleItem = new IPSingleItem();
            singleItem.setIpSingleItemAttribute(2 * i);
            IPListItem listItem = new IPListItem();
            listItem.setIpListItemAttribute(2 * i + 1);
            singleItem.addItem(listItem);
            topItem.setItem(singleItem);
            root.addItem(topItem);
        }
        log.info("    Saving the test objects...");
        DataObjectHandler dataHandler = Application.getApplication().getDataObjectMgr().getDataObjectHandler();
        String dataSource = Application.getApplication().getDataObjectMgr().getDatasource();
        dataHandler.store(dataSource, IPRoot.class, root);
        log.info("    Successfully created and saved the test objects");
        log.info("    Reloading the test objects...");
        IPRoot rootLoaded = (IPRoot) dataHandler.retrieve(dataSource, IPRoot.class, root.getId());
        log.info("    Successfully loaded the test objects");
        log.info("    Compare the created and loaded test objects...");
        compareSavedAndLoaded(root, rootLoaded);
        log.info("    Saved and loaded test objects are equal");
        log.info("    Modifiy loaded objects...");
        IPTopItem topItemNew = new IPTopItem();
        IPSingleItem singleItemNew = new IPSingleItem();
        singleItemNew.setIpSingleItemAttribute(100);
        IPListItem listItemNew = new IPListItem();
        listItemNew.setIpListItemAttribute(101);
        singleItemNew.addItem(listItemNew);
        topItemNew.setItem(singleItemNew);
        rootLoaded.addItem(topItemNew);
        IPSingleItem singleItemNew2 = new IPSingleItem();
        singleItemNew2.setIpSingleItemAttribute(200);
        IPListItem listItemNew2 = new IPListItem();
        listItemNew2.setIpListItemAttribute(201);
        singleItemNew2.addItem(listItemNew2);
        rootLoaded.getItem(0).setItem(singleItemNew2);
        IPListItem listItemOld = rootLoaded.getItem(1).getItem().getItem(0);
        IPListItem listItemNew3 = new IPListItem();
        listItemNew3.setIpListItemAttribute(301);
        rootLoaded.getItem(1).getItem().addItem(0, listItemNew3);
        rootLoaded.getItem(1).getItem().removeItem(listItemOld);
        log.info("    Saving the modified objects...");
        dataHandler.store(dataSource, IPRoot.class, rootLoaded);
        log.info("    Successfully saved the modified objects");
        log.info("    Reloading the modified objects...");
        IPRoot rootLoaded2 = (IPRoot) dataHandler.retrieve(dataSource, IPRoot.class, rootLoaded.getId());
        log.info("    Successfully loaded the test objects");
        log.info("    Compare the modified and loaded test objects...");
        compareSavedAndLoaded(rootLoaded, rootLoaded2);
        log.info("    Saved and loaded test objects are equal");
        log.info("*** Indexed Properties Test successful :) ***");
    }

    private void compareSavedAndLoaded(IPRoot rootSaved, IPRoot rootLoaded) {
        assertEquals("Saved and loaded root.getId() differ.", rootSaved.getId(), rootLoaded.getId());
        assertEquals("Number of items in the saved and loaded root (root.getItems().size()) differ.", rootSaved.getItems().size(), rootLoaded.getItems().size());
        int topListSize = rootSaved.getItems().size();
        for (int i = 0; i < topListSize; i++) {
            IPTopItem topItemSaved = rootSaved.getItem(i);
            IPTopItem topItemLoaded = rootLoaded.getItem(i);
            assertEquals("Saved and loaded root.getItem(" + i + ").getId() differ.", topItemSaved.getId(), topItemLoaded.getId());
            IPSingleItem singleItemSaved = topItemSaved.getItem();
            IPSingleItem singleItemLoaded = topItemLoaded.getItem();
            assertEquals("Saved and loaded root.getItem(" + i + ").getItem().getId() differ.", singleItemSaved.getId(), singleItemLoaded.getId());
            assertEquals("Saved and loaded root.getItem(" + i + ").getItem().getIpSingleItemAttribute() differ.", singleItemSaved.getIpSingleItemAttribute(), singleItemLoaded.getIpSingleItemAttribute());
            assertEquals("Number of items in the saved and loaded single item (root.getItem(" + i + ").getItems().size()) differ.", singleItemSaved.getItems().size(), singleItemLoaded.getItems().size());
            int singleListSize = singleItemSaved.getItems().size();
            for (int j = 0; j < singleListSize; j++) {
                IPListItem listItemSaved = singleItemSaved.getItem(j);
                IPListItem listItemLoaded = singleItemLoaded.getItem(j);
                assertEquals("Saved and loaded root.getItem(" + i + ").getItem().getItem(" + j + ").getId() differ.", listItemSaved.getId(), listItemLoaded.getId());
                assertEquals("Saved and loaded root.getItem(" + i + ").getItem().getItem(" + j + ").getIpListItemAttribute() differ.", listItemSaved.getIpListItemAttribute(), listItemLoaded.getIpListItemAttribute());
            }
        }
    }
}
