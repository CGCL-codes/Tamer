package org.itracker.model;

import static org.itracker.Assert.assertEntityComparator;
import static org.itracker.Assert.assertEntityComparatorEquals;
import org.itracker.core.resources.ITrackerResources;
import org.itracker.services.ConfigurationService;
import org.itracker.services.util.CustomFieldUtilities;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class CustomFieldValueTest extends AbstractDependencyInjectionSpringContextTests {

    private CustomFieldValue cust;

    private ConfigurationService configurationService;

    @Test
    public void testSetCustomField() {
        try {
            cust.setCustomField(null);
            fail("did not throw IllegalArgumentException ");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetValue() {
        try {
            cust.setValue(null);
            fail("did not throw IllegalArgumentException ");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSortOrderComparator() throws Exception {
        CustomFieldValue valueA, valueB;
        CustomField fieldA = new CustomField();
        CustomField fieldB = new CustomField();
        fieldA.setId(0);
        fieldB.setId(1);
        valueA = new CustomFieldValue(fieldA, "1");
        valueA.setSortOrder(1);
        valueB = new CustomFieldValue(fieldB, "2");
        valueB.setSortOrder(2);
        valueA.setId(1);
        valueB.setId(2);
        assertEntityComparator("name comparator", CustomFieldValue.SORT_ORDER_COMPARATOR, valueA, valueB);
        assertEntityComparator("name comparator", CustomFieldValue.SORT_ORDER_COMPARATOR, valueA, null);
        valueA.setSortOrder(valueB.getSortOrder());
        assertEntityComparatorEquals("name comparator", CustomFieldValue.SORT_ORDER_COMPARATOR, valueA, valueB);
        assertEntityComparatorEquals("name comparator", CustomFieldValue.SORT_ORDER_COMPARATOR, valueA, valueB);
    }

    @Test
    public void testNameComparator() throws Exception {
        CustomFieldValue valueA, valueB;
        CustomField fieldA = new CustomField();
        CustomField fieldB = new CustomField();
        fieldA.setId(0);
        fieldB.setId(1);
        valueA = new CustomFieldValue(fieldA, "1");
        valueB = new CustomFieldValue(fieldB, "2");
        valueA.setId(1);
        valueB.setId(2);
        Language langA = new Language(ITrackerResources.getDefaultLocale(), CustomFieldUtilities.getCustomFieldOptionLabelKey(fieldA.getId(), valueA.getId()));
        langA.setResourceValue("a");
        Language langB = new Language(ITrackerResources.getDefaultLocale(), CustomFieldUtilities.getCustomFieldOptionLabelKey(fieldB.getId(), valueB.getId()));
        langB.setResourceValue("b");
        this.configurationService.updateLanguageItem(langA);
        this.configurationService.updateLanguageItem(langB);
        assertEntityComparator("name comparator", CustomFieldValue.NAME_COMPARATOR, valueA, valueB);
        assertEntityComparator("name comparator", CustomFieldValue.NAME_COMPARATOR, valueA, null);
        langA.setResourceValue(langB.getResourceValue());
        this.configurationService.updateLanguageItem(langA);
        this.configurationService.updateLanguageItem(langB);
        ITrackerResources.clearKeyFromBundles(langA.getResourceKey(), true);
        ITrackerResources.clearKeyFromBundles(langB.getResourceKey(), true);
        assertEntityComparatorEquals("name comparator", CustomFieldValue.NAME_COMPARATOR, valueA, valueA);
    }

    @Test
    public void testToString() {
        assertNotNull(cust.toString());
    }

    public void onSetUp() throws Exception {
        cust = new CustomFieldValue();
        configurationService = (ConfigurationService) applicationContext.getBean("configurationService");
    }

    public void onTearDown() throws Exception {
        cust = null;
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[] { "application-context.xml" };
    }
}
