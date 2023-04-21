package com.nokia.ats4.appmodel.model.domain.testdata;

import com.nokia.ats4.appmodel.model.impl.MainApplicationModel;
import com.nokia.ats4.appmodel.util.Settings;
import java.util.ArrayList;
import java.util.List;

/**
 * TestData
 *
 * @author Hannu-Pekka Hakam&auml;ki
 * @version $Revision: 2 $
 */
public class TestData {

    /** The logical name that is used identify this testdata **/
    private String logicalName;

    /** The description of the testdata item **/
    private String description = "";

    /** The list of tetsdata items that are assosiated whit this testdata **/
    private List<TestDataItem> testDataItem = new ArrayList<TestDataItem>();

    /** Creates a new instance of TestData */
    public TestData(String name) {
        name = reformatName(name);
        this.logicalName = name;
    }

    public String getLogicalName() {
        return logicalName;
    }

    private String reformatName(String name) {
        String prefix = Settings.getProperty("testdata.prefix");
        if (prefix == null) {
            prefix = "$";
        }
        if (!name.startsWith(prefix)) {
            name = prefix + name;
        }
        if (!name.endsWith(prefix)) {
            name = name + prefix;
        }
        int i = 1;
        String tempName = name.substring(0, name.lastIndexOf(prefix));
        while (MainApplicationModel.getInstance().getActiveProject().getTestDataModel().getTestData(name) != null) {
            name = tempName + "_" + i + prefix;
            i++;
        }
        return name;
    }

    /**
     * This returns list of testdataitems that have given gategory
     * @param gategory the gategory that determines which items are selected
     * @return list of items with given gategory
     */
    public List<TestDataItem> getTestDataItemSet(TestDataItem.Category gategory) {
        List<TestDataItem> retval = new ArrayList<TestDataItem>();
        for (TestDataItem item : this.testDataItem) {
            if (item.getPriority() == gategory) {
                retval.add(item);
            }
        }
        return retval;
    }

    /**
     * This returns list of testdataitems that have same path
      *
     * @param path Path for which the items are fetched
     * @return list of items with given gategory
     */
    public List<TestDataItem> getTestDataItemSet(String path) {
        List<TestDataItem> retval = new ArrayList<TestDataItem>();
        for (TestDataItem item : this.testDataItem) {
            if (item.getPath().equalsIgnoreCase(path)) {
                retval.add(item);
            }
        }
        return retval;
    }

    /**
      * This creates and adds testdataitem with given name and path to the testdata
      * @param name the name of testdataitem (e.g. filename)
      */
    public TestDataItem createTestSetItem(String name) {
        TestDataItem item = new TestDataItem(name, "");
        this.testDataItem.add(item);
        return item;
    }

    /**
      * This adds given testdataitem to the testdata
      * @param item item to be added
      */
    public void addTestDataItem(TestDataItem item) {
        if (item != null) {
            this.testDataItem.add(item);
        }
    }

    /**
      * This adds testdataitem with given name and path to the testdata
      * @param name the name of testdataitem (e.g. filename)
      * @param path the path where the item  can be found
      * @param gategory the gategory of item, if null default gategory is used
      */
    public TestDataItem createTestSetItem(String name, String path, TestDataItem.Category gategory) {
        TestDataItem item = new TestDataItem(name, path);
        if (gategory != null) {
            item.setGategory(gategory);
        }
        this.testDataItem.add(item);
        return item;
    }

    /**
      * Removes all the item from the object
      */
    public void removeAll() {
        this.testDataItem.clear();
    }

    /**
      * Removes selected testdata item
      */
    public void removeItem(TestDataItem toBeRemoved) {
        if (this.testDataItem.contains(toBeRemoved)) {
            this.testDataItem.remove(toBeRemoved);
        }
    }

    public void setLogicalName(String logicalName) {
        if (MainApplicationModel.getInstance().getActiveProject().getTestDataModel().getTestData(logicalName) == null) {
            logicalName = reformatName(logicalName);
            this.logicalName = logicalName;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TestDataItem> getTestDataItem() {
        return testDataItem;
    }

    public void setTestDataItem(List<TestDataItem> testDataItem) {
        this.testDataItem = testDataItem;
    }

    public String toString() {
        return this.logicalName + " (" + this.testDataItem.size() + " files)";
    }
}
