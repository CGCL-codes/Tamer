package org.pojosoft.catalog.web.gwt.server.test;

import org.pojosoft.catalog.CatalogService;
import org.pojosoft.catalog.model.Catalog;
import org.pojosoft.catalog.model.CatalogItem;
import org.pojosoft.catalog.model.CatalogStatus;
import org.pojosoft.core.reference.ReferenceService;
import org.pojosoft.core.support.ServiceLocator;
import org.pojosoft.ria.gwt.client.service.DateTimePattern;
import org.pojosoft.ria.gwt.client.service.NumberPattern;
import org.pojosoft.ria.gwt.client.service.UserProfile;
import org.pojosoft.user.model.DemoUser;

/**
 * Check and setup default data for the demo.
 *
 * @author POJO Software
 * @version 1.0
 * @since 1.0
 */
public class SetupDemoData {

    static ReferenceService referenceService;

    static CatalogService catalogService;

    public static void setUpDefaultData() {
        referenceService = (ReferenceService) ServiceLocator.getService("reference.referenceService");
        catalogService = (CatalogService) ServiceLocator.getService("catalog.catalogService");
        CatalogStatus csA = addStatus("active");
        CatalogStatus csIA = addStatus("inactive");
        String catalogID = "Main";
        Catalog c = catalogService.getCatalog(catalogID);
        if (c == null) {
            c = new Catalog();
            c.setId(catalogID);
            c.setName("Main catalog");
            c.setStatus(csA);
            c.setLastUpdateUser("System");
            c = catalogService.addCatalog(c);
            addCatalogItem(c, "CI_01", "Java en concentr� : Manuel de r�f�rence pour Java", "A Online Java book", new Double(51.30));
            addCatalogItem(c, "CI_02", "Advanced Flash", "A Online course", new Double(295.00));
            addCatalogItem(c, "CI_03", "Dancing Class 101", "3 days a week for 6 months.", new Double(1200));
        }
    }

    protected static CatalogStatus addStatus(String statusID) {
        CatalogStatus cs = (CatalogStatus) referenceService.getReference(CatalogStatus.class, statusID);
        if (cs == null) {
            cs = (CatalogStatus) referenceService.addReference(new CatalogStatus(statusID, statusID));
        }
        return cs;
    }

    protected static void addCatalogItem(Catalog c, String itemId, String name, String desc, Double price) {
        CatalogItem item = new CatalogItem();
        item.setCatalog(c);
        item.setId(itemId);
        item.setDescription(desc);
        item.setPrice(price);
        item.setName(name);
        item.setLastUpdateUser("System");
        catalogService.addItem(item);
    }

    public static UserProfile createUserProfile() {
        DemoUser demoUser = new DemoUser();
        demoUser.setId("Admin");
        demoUser.setPassword("Admin");
        demoUser.setFirstName("Admin");
        demoUser.setLastName("Demo");
        UserProfile userProfile = new UserProfile();
        userProfile.userId = demoUser.getId();
        userProfile.firstName = demoUser.getFirstName();
        userProfile.lastName = demoUser.getLastName();
        DateTimePattern datePattern = new DateTimePattern();
        datePattern.hint = "1/1/2008";
        datePattern.pattern = "M/d/yyyy";
        userProfile.datePattern = datePattern;
        userProfile.currencySymbol = "$";
        userProfile.percentageSymbol = "%";
        userProfile.isoLanguageCode = "en";
        userProfile.isoCountryCode = "US";
        userProfile.timeZone = "EST";
        userProfile.longPattern = new NumberPattern();
        userProfile.longPattern.groupingSeparator = ",";
        userProfile.longPattern.hint = "123,456";
        userProfile.doublePattern = new NumberPattern();
        userProfile.doublePattern.groupingSeparator = ",";
        userProfile.doublePattern.decimalSeparator = ".";
        userProfile.doublePattern.hint = "123,456.00";
        userProfile.doublePattern.maxFractionDigit = 2L;
        userProfile.doublePattern.minFractionDigit = 2L;
        userProfile.addAuthorizedFunction("Demo", false);
        userProfile.addAuthorizedFunction("Catalog", false);
        return userProfile;
    }
}
