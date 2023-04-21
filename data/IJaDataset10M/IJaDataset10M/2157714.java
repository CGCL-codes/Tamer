package net.sf.csutils.impexp;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.LocalizedString;
import javax.xml.registry.infomodel.RegistryObject;
import net.sf.csutils.core.model.QName;
import net.sf.csutils.core.model.ROMetaModel;
import net.sf.csutils.core.model.impl.ROMetaModelReader;
import net.sf.csutils.core.registry.ModelDrivenRegistryFacade;
import net.sf.csutils.core.registry.ROMetaModelAccessor;
import net.sf.csutils.core.tests.AbstractJaxMasTestCase;
import net.sf.csutils.core.utils.Generics;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test case for the {@link Importer}.
 */
public class ImporterTest extends AbstractJaxMasTestCase {

    private static final String NS = "http://namespaces.csutils.sf.net/importer/model/1.0.0/test";

    private static final QName QNAME_ORG = new QName(NS, "OrgExample");

    private static final QName QNAME_USER = new QName(NS, "UserExample");

    private static final QName QNAME_ORG2 = new QName(NS, "OrgExample2");

    private static final QName QNAME_USER2 = new QName(NS, "UserExample2");

    private static final String[] DATABASES = new String[] { "Oracle", "DB2", "Tamino", "Adabas", "MySQL", "PostgreSQL" };

    private URL require(String pResource) {
        final URL url = getClass().getResource(pResource);
        if (url == null) {
            throw new IllegalStateException("Unable to locate resource: " + pResource);
        }
        return url;
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        initDatabase();
    }

    private List<RegistryObject> getObjects(QName pQName) throws JAXRException {
        final String q = "DECLARE NAMESPACE ns='" + pQName.getNamespaceURI() + "' FROM ns:" + pQName.getLocalPart() + " objs";
        return getModelAccessor().executeQuery(q);
    }

    private List<RegistryObject> getOrgs() throws JAXRException {
        return getObjects(QNAME_ORG);
    }

    private List<RegistryObject> getUsers() throws JAXRException {
        return getObjects(QNAME_USER);
    }

    private List<RegistryObject> getOrgs2() throws JAXRException {
        return getObjects(QNAME_ORG2);
    }

    private List<RegistryObject> getUsers2() throws JAXRException {
        return getObjects(QNAME_USER2);
    }

    /**
	 * Creates the "Database" taxonomy.
	 */
    private void createDatabaseTaxonomy(ModelDrivenRegistryFacade pFacade) throws JAXRException {
        ClassificationScheme cs = (ClassificationScheme) pFacade.findObjectByPath("/Database");
        if (cs == null) {
            cs = pFacade.getBusinessLifeCycleManager().createClassificationScheme("Database", "Taxonomy of databases");
            pFacade.getBusinessLifeCycleManager().saveClassificationSchemes(Collections.singleton(cs));
        }
        for (String db : DATABASES) {
            Concept concept = (Concept) pFacade.findObjectByPath("/Database/" + db);
            if (concept == null) {
                concept = pFacade.getBusinessLifeCycleManager().createConcept(cs, db, db);
                pFacade.getBusinessLifeCycleManager().saveConcepts(Collections.singleton(concept));
            }
        }
    }

    /**
     * Ensures a clean registry state by deleting the necessary objects.
     */
    private void prepareRegistry() throws Exception {
        open();
        ModelDrivenRegistryFacade facade = getFacade();
        ROMetaModelAccessor mmAcc = facade.getMetaModelAccessor();
        if (mmAcc.getMetaModel().getROType(QNAME_USER) != null) {
            mmAcc.remove(QNAME_USER);
        }
        if (mmAcc.getMetaModel().getROType(QNAME_ORG) != null) {
            mmAcc.remove(QNAME_ORG);
        }
        final ROMetaModel model = new ROMetaModelReader().read(require("registryModel.xml"));
        mmAcc.create(model.getROType(QNAME_ORG));
        mmAcc.create(model.getROType(QNAME_USER));
        Assert.assertEquals(0, getOrgs().size());
        Assert.assertEquals(0, getUsers().size());
        createDatabaseTaxonomy(facade);
        for (String db : DATABASES) {
            final Object concept = facade.findObjectByPath("/Database/" + db);
            Assert.assertNotNull(concept);
            Assert.assertTrue(concept instanceof Concept);
        }
        close();
    }

    /**
     * Ensures a clean registry state by deleting the necessary objects.
     */
    private void prepareRegistry2() throws Exception {
        open();
        ModelDrivenRegistryFacade facade = getFacade();
        ROMetaModelAccessor mmAcc = facade.getMetaModelAccessor();
        if (mmAcc.getMetaModel().getROType(QNAME_USER2) != null) {
            mmAcc.remove(QNAME_USER2);
        }
        if (mmAcc.getMetaModel().getROType(QNAME_ORG2) != null) {
            mmAcc.remove(QNAME_ORG2);
        }
        final ROMetaModel model = new ROMetaModelReader().read(require("registryModel.xml"));
        mmAcc.create(model.getROType(QNAME_ORG2));
        mmAcc.create(model.getROType(QNAME_USER2));
        Assert.assertEquals(0, getOrgs2().size());
        Assert.assertEquals(0, getUsers2().size());
        close();
    }

    private RegistryObject findByName(List<RegistryObject> pRos, String pName) throws JAXRException {
        for (RegistryObject ro : pRos) {
            final Collection<LocalizedString> names = Generics.cast(ro.getName().getLocalizedStrings());
            for (LocalizedString ls : names) {
                if (pName.equals(ls.getValue())) {
                    return ro;
                }
            }
        }
        return null;
    }

    private RegistryObject findByUniqueSlot(List<RegistryObject> pRos, String pKey) throws JAXRException {
        for (RegistryObject ro : pRos) {
            final Collection<String> keys = getModelAccessor().getSlotValues("uniqueKey", ro);
            for (String key : keys) {
                if (pKey.equals(key)) {
                    assertSlotValues(ro, "uniqueKey", pKey);
                    return ro;
                }
            }
        }
        return null;
    }

    private void assertClassificationMissing(RegistryObject pRo, String pAttr) throws JAXRException {
        final Concept concept = getModelAccessor().getClassificationValue("vendorOf", pRo);
        Assert.assertNull(concept);
    }

    private void assertClassification(RegistryObject pRo, String pAttr, String pPath) throws JAXRException {
        int offset = pPath.indexOf('/', 1);
        Assert.assertTrue(offset != -1);
        final Concept concept = getModelAccessor().getClassificationValue("vendorOf", pRo);
        Assert.assertNotNull(concept);
        Assert.assertEquals(pPath, pPath.substring(0, offset) + concept.getPath());
    }

    private void assertRelationships(RegistryObject pRo, String pAssociationType, RegistryObject... pTargets) throws JAXRException {
        final Collection<RegistryObject> targets = getModelAccessor().getRelationValues(pAssociationType, pRo);
        if (pTargets == null) {
            Assert.assertTrue(targets.isEmpty());
        } else {
            Assert.assertEquals(pTargets.length, targets.size());
            final Iterator<RegistryObject> iter = targets.iterator();
            for (int i = 0; i < pTargets.length; i++) {
                Assert.assertEquals(pTargets[i].getKey(), iter.next().getKey());
            }
        }
    }

    private void assertSlotValues(RegistryObject pRo, String pSlotName, String... pValues) throws JAXRException {
        final Collection<String> valueCollection = getModelAccessor().getSlotValues(pSlotName, pRo);
        if (pValues == null) {
            Assert.assertTrue(valueCollection.isEmpty());
        } else {
            Assert.assertEquals(pValues.length, valueCollection.size());
            final Iterator<String> valueIter = valueCollection.iterator();
            for (int i = 0; i < pValues.length; i++) {
                Assert.assertEquals(pValues[i], valueIter.next());
            }
        }
    }

    private void assertNames(RegistryObject pRo, String... pNames) throws JAXRException {
        final Collection<LocalizedString> nameCollection = Generics.cast(pRo.getName().getLocalizedStrings());
        final List<LocalizedString> names = new ArrayList<LocalizedString>(nameCollection);
        for (int i = 0; i < pNames.length; i += 2) {
            final String locale = pNames[i];
            final String name = pNames[i + 1];
            boolean found = false;
            for (int j = 0; j < names.size(); j++) {
                final LocalizedString ls = names.get(j);
                if (ls.getValue().equals(name) && ls.getLocale().toString().equals(locale)) {
                    found = true;
                    names.remove(j);
                    break;
                }
            }
            Assert.assertTrue("Name not found: " + locale + ", " + name, found);
        }
        if (!names.isEmpty()) {
            Assert.fail("Unexpected name: " + names.get(0).getLocale() + ", " + names.get(0).getValue());
        }
    }

    /**
     * Tests running the importer with named identifications.
     */
    @Test
    public void testImporterWithNames() throws Exception {
        prepareRegistry();
        readWithoutErrors("insertWithNames.xml");
        {
            open();
            final List<RegistryObject> orgs = getOrgs();
            Assert.assertEquals(2, orgs.size());
            final RegistryObject org1 = findByName(orgs, "Org1");
            Assert.assertNotNull(org1);
            assertNames(org1, "de_DE", "Org1", "en_US", "Org1_en", "en_GB", "Org1_en");
            assertSlotValues(org1, "OrgExampleAttr1", "Value 5");
            assertSlotValues(org1, "OrgExampleAttr2", "Value 2");
            assertClassification(org1, "vendorOf", "/Database/Adabas");
            final RegistryObject org2 = findByName(orgs, "Org2");
            Assert.assertNotNull(org2);
            assertSlotValues(org2, "OrgExampleAttr1", "Value 3");
            assertSlotValues(org2, "OrgExampleAttr3", "Value 6");
            assertClassification(org2, "vendorOf", "/Database/DB2");
            assertNames(org2, "de_DE", "Org2");
            final List<RegistryObject> users = getUsers();
            Assert.assertEquals(2, users.size());
            final RegistryObject user1 = findByName(users, "User1");
            assertNames(user1, "de_DE", "User1");
            assertSlotValues(user1, "UserExampleAttr1", "Val 1");
            assertRelationships(user1, "EmployeeOf", org2);
            final RegistryObject user2 = findByName(users, "User2");
            assertNames(user2, "de_DE", "User2");
            assertSlotValues(user2, "UserExampleAttr1", "Val 2");
            assertRelationships(user2, "EmployeeOf", org1, org2);
            close();
        }
        readWithoutErrors("updateWithNames.xml");
        {
            open();
            final List<RegistryObject> orgs = getOrgs();
            Assert.assertEquals(2, orgs.size());
            final RegistryObject org1 = findByName(orgs, "Org1");
            Assert.assertNotNull(org1);
            assertNames(org1, "de_DE", "Org1");
            assertSlotValues(org1, "OrgExampleAttr1", "Value 1");
            assertSlotValues(org1, "OrgExampleAttr2", "Value 2");
            assertClassification(org1, "vendorOf", "/Database/Tamino");
            final RegistryObject org2 = findByName(orgs, "Org2");
            Assert.assertNotNull(org2);
            assertNames(org2, "de_DE", "Org2", "en_US", "Org2_en");
            assertSlotValues(org2, "OrgExampleAttr1", "Value 3");
            assertSlotValues(org2, "OrgExampleAttr2", "Value 4");
            assertClassificationMissing(org2, "vendorOf");
            final List<RegistryObject> users = getUsers();
            Assert.assertEquals(2, users.size());
            final RegistryObject user1 = findByName(users, "User1");
            assertNames(user1, "de_DE", "User1");
            assertSlotValues(user1, "UserExampleAttr1", "Val 1");
            assertRelationships(user1, "EmployeeOf", org1);
            final RegistryObject user2 = findByName(users, "User2");
            assertNames(user2, "de_DE", "User2");
            assertSlotValues(user2, "UserExampleAttr1", "Val 2");
            assertRelationships(user2, "EmployeeOf", org1);
            close();
            open();
            final List<RegistryObject> loadedUsers = getUsers();
            Assert.assertEquals(2, loadedUsers.size());
            final RegistryObject loadedUser2 = findByName(loadedUsers, "User2");
            assertNames(loadedUser2, "de_DE", "User2");
            assertSlotValues(loadedUser2, "UserExampleAttr1", "Val 2");
            assertRelationships(user2, "EmployeeOf", org1);
            close();
        }
        readWithoutErrors("deleteWithNames.xml");
        {
            open();
            final List<RegistryObject> orgs = getOrgs();
            Assert.assertEquals(1, orgs.size());
            final RegistryObject org2 = findByName(orgs, "Org2");
            Assert.assertNotNull(org2);
            assertNames(org2, "de_DE", "Org2", "en_US", "Org2_en");
            assertSlotValues(org2, "OrgExampleAttr1", "Value 3");
            assertSlotValues(org2, "OrgExampleAttr2", "Value 4");
            final List<RegistryObject> users = getUsers();
            Assert.assertEquals(1, users.size());
            final RegistryObject user2 = findByName(users, "User2");
            assertNames(user2, "de_DE", "User2");
            assertSlotValues(user2, "UserExampleAttr1", "Val 2");
            close();
        }
    }

    private void readWithoutErrors(String pResource) throws JAXRException {
        final Importer importer = new Importer();
        final List<Importer.Error> errors = importer.read(newConnectionProvider(), require(pResource));
        if (!errors.isEmpty()) {
            final Importer.Error error = errors.get(0);
            Assert.fail(error.getLineNumber() + "," + error.getColumnNumber() + ": " + error.getMessage());
        }
    }

    /**
     * Tests running the importer with unique slot identifications.
     */
    @Test
    public void testImporterWithUniqueSlots() throws Exception {
        prepareRegistry2();
        readWithoutErrors("insertWithUniqueSlots.xml");
        {
            open();
            final List<RegistryObject> orgs = getOrgs2();
            Assert.assertEquals(2, orgs.size());
            final RegistryObject org1 = findByUniqueSlot(orgs, "Org1Key");
            Assert.assertNotNull(org1);
            assertNames(org1, "de_DE", "Org1", "en_US", "Org1_en", "en_GB", "Org1_en");
            assertSlotValues(org1, "OrgExampleAttr1", "Value 5");
            assertSlotValues(org1, "OrgExampleAttr2", "Value 2");
            final RegistryObject org2 = findByUniqueSlot(orgs, "Org2Key");
            Assert.assertNotNull(org2);
            assertSlotValues(org2, "OrgExampleAttr1", "Value 3");
            assertSlotValues(org2, "OrgExampleAttr3", "Value 6");
            assertNames(org2, "de_DE", "Org2");
            final List<RegistryObject> users = getUsers2();
            Assert.assertEquals(2, users.size());
            final RegistryObject user1 = findByUniqueSlot(users, "User1Key");
            assertNames(user1, "de_DE", "User1");
            assertSlotValues(user1, "UserExampleAttr1", "Val 1");
            assertRelationships(user1, "EmployeeOf", org2);
            final RegistryObject user2 = findByUniqueSlot(users, "User2Key");
            assertNames(user2, "de_DE", "User2");
            assertSlotValues(user2, "UserExampleAttr1", "Val 2");
            assertRelationships(user2, "EmployeeOf", org1, org2);
            close();
        }
        readWithoutErrors("updateWithUniqueSlots.xml");
        {
            open();
            final List<RegistryObject> orgs = getOrgs2();
            Assert.assertEquals(2, orgs.size());
            final RegistryObject org1 = findByUniqueSlot(orgs, "Org1Key");
            Assert.assertNotNull(org1);
            assertNames(org1, "de_DE", "Org1");
            assertSlotValues(org1, "OrgExampleAttr1", "Value 1");
            assertSlotValues(org1, "OrgExampleAttr2", "Value 2");
            final RegistryObject org2 = findByUniqueSlot(orgs, "Org2Key");
            Assert.assertNotNull(org2);
            assertNames(org2, "de_DE", "Org2", "en_US", "Org2_en");
            assertSlotValues(org2, "OrgExampleAttr1", "Value 3");
            assertSlotValues(org2, "OrgExampleAttr2", "Value 4");
            final List<RegistryObject> users = getUsers2();
            Assert.assertEquals(2, users.size());
            final RegistryObject user1 = findByUniqueSlot(users, "User1Key");
            assertNames(user1, "de_DE", "User1");
            assertSlotValues(user1, "UserExampleAttr1", "Val 1");
            assertRelationships(user1, "EmployeeOf", org1);
            final RegistryObject user2 = findByUniqueSlot(users, "User2Key");
            assertNames(user2, "de_DE", "User2");
            assertSlotValues(user2, "UserExampleAttr1", "Val 2");
            assertRelationships(user2, "EmployeeOf", org1);
            close();
            open();
            final List<RegistryObject> loadedUsers = getUsers2();
            Assert.assertEquals(2, loadedUsers.size());
            final RegistryObject loadedUser2 = findByUniqueSlot(loadedUsers, "User2Key");
            assertNames(loadedUser2, "de_DE", "User2");
            assertSlotValues(loadedUser2, "UserExampleAttr1", "Val 2");
            assertRelationships(user2, "EmployeeOf", org1);
            close();
        }
        readWithoutErrors("deleteWithUniqueSlots.xml");
        {
            open();
            final List<RegistryObject> orgs = getOrgs2();
            Assert.assertEquals(1, orgs.size());
            final RegistryObject org2 = findByUniqueSlot(orgs, "Org2Key");
            Assert.assertNotNull(org2);
            assertNames(org2, "de_DE", "Org2", "en_US", "Org2_en");
            assertSlotValues(org2, "OrgExampleAttr1", "Value 3");
            assertSlotValues(org2, "OrgExampleAttr2", "Value 4");
            final List<RegistryObject> users = getUsers2();
            Assert.assertEquals(1, users.size());
            final RegistryObject user2 = findByUniqueSlot(users, "User2Key");
            Assert.assertNotNull(user2);
            assertNames(user2, "de_DE", "User2");
            assertSlotValues(user2, "UserExampleAttr1", "Val 2");
            close();
        }
    }
}
