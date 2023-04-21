package net.lukemurphey.nsia.tests;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Vector;
import net.lukemurphey.nsia.Application;
import net.lukemurphey.nsia.NoDatabaseConnectionException;
import net.lukemurphey.nsia.Application.DatabaseAccessType;
import net.lukemurphey.nsia.scan.DefinitionPolicyDescriptor;
import net.lukemurphey.nsia.scan.DefinitionPolicyManagement;
import net.lukemurphey.nsia.scan.MetaDefinition;
import net.lukemurphey.nsia.scan.DefinitionPolicyDescriptor.DefinitionPolicyAction;
import net.lukemurphey.nsia.upgrade.UpgradeFailureException;
import net.lukemurphey.nsia.upgrade.processors.ConvertMetaDefinitionExceptions;
import junit.framework.TestCase;

public class ConvertMetaDefinitionExceptionsTest extends TestCase {

    Application app = null;

    ConvertMetaDefinitionExceptions converter = null;

    DefinitionPolicyManagement policyManagement = null;

    Vector<DefinitionPolicyDescriptor> policyDescriptors = new Vector<DefinitionPolicyDescriptor>();

    public void setUp() throws TestApplicationException {
        app = TestApplication.getApplication();
        policyManagement = new DefinitionPolicyManagement(app);
        converter = new ConvertMetaDefinitionExceptions();
    }

    public void tearDown() {
        TestApplication.stopApplication();
    }

    private void populateSamples() throws SQLException, NoDatabaseConnectionException {
        createSample(DefinitionPolicyDescriptor.createDefinitionPolicy(1, 20, "Def", "Category", "SubCategory", null, DefinitionPolicyAction.EXCLUDE));
        createSample(DefinitionPolicyDescriptor.createCategoryPolicy("Category", DefinitionPolicyAction.EXCLUDE));
        createSample(DefinitionPolicyDescriptor.createSubCategoryPolicy("Category", "SubCategory", DefinitionPolicyAction.EXCLUDE));
    }

    private void createSample(DefinitionPolicyDescriptor desc) throws SQLException, NoDatabaseConnectionException {
        desc.saveToDatabase(app.getDatabaseConnection(DatabaseAccessType.ADMIN));
        policyDescriptors.add(desc);
    }

    private String checkSamples() throws MalformedURLException, SQLException, NoDatabaseConnectionException {
        for (DefinitionPolicyDescriptor desc : policyDescriptors) {
            DefinitionPolicyDescriptor desc2 = policyManagement.getPolicy(desc.getPolicyID());
            if (desc2 == null) {
                return "Policy with ID " + desc.getPolicyID() + " not found";
            }
            if ((desc2.getDefinitionCategory() == null || desc.getDefinitionCategory() == null) && desc.getDefinitionCategory() != desc2.getDefinitionCategory()) {
                fail(desc2.getDefinitionCategory() + " does not match " + desc.getDefinitionCategory());
            } else if (desc2.getDefinitionCategory() != null && !desc2.getDefinitionCategory().equals(desc.getDefinitionCategory())) {
                fail(desc2.getDefinitionCategory() + " does not match " + desc.getDefinitionCategory());
            }
            if ((desc2.getDefinitionSubCategory() == null || desc.getDefinitionSubCategory() == null) && desc.getDefinitionSubCategory() != desc2.getDefinitionSubCategory()) {
                fail(desc2.getDefinitionSubCategory() + " does not match " + desc.getDefinitionSubCategory());
            } else if (desc2.getDefinitionSubCategory() != null && !desc2.getDefinitionSubCategory().equals(desc.getDefinitionSubCategory())) {
                return desc2.getDefinitionSubCategory() + " does not match " + desc.getDefinitionSubCategory();
            }
            if ((desc2.getDefinitionName() == null || desc.getDefinitionName() == null) && desc.getDefinitionName() != desc2.getDefinitionName()) {
                fail(desc2.getDefinitionName() + " does not match " + desc.getDefinitionName());
            } else if (desc2.getDefinitionName() != null && !desc2.getDefinitionName().equals(desc.getDefinitionName())) {
                fail(desc2.getDefinitionName() + " does not match " + desc.getDefinitionName());
            }
        }
        return null;
    }

    public void testConvertDefinitions() throws UpgradeFailureException, MalformedURLException, SQLException, NoDatabaseConnectionException {
        DefinitionPolicyDescriptor desc = DefinitionPolicyDescriptor.createDefinitionPolicy(1, 20, "Anomaly", "ContentError", MetaDefinition.BROKEN_LINK.getName(), null, DefinitionPolicyAction.EXCLUDE);
        desc.saveToDatabase(app.getDatabaseConnection(DatabaseAccessType.ADMIN));
        if (desc.getPolicyID() <= 0) {
            fail("Policy did not get created properly");
        }
        populateSamples();
        converter.doUpgrade(app);
        checkSamples();
        DefinitionPolicyDescriptor desc2 = policyManagement.getPolicy(desc.getPolicyID());
        if (!desc2.getDefinitionName().equalsIgnoreCase(MetaDefinition.BROKEN_LINK.getName())) {
            fail("Definition name was changed inappropriately");
        }
        if (!desc2.getDefinitionSubCategory().equalsIgnoreCase(MetaDefinition.BROKEN_LINK.getSubCategoryName())) {
            fail("Definition sub-category was not upgraded correctly");
        }
        if (!desc2.getDefinitionCategory().equalsIgnoreCase(MetaDefinition.BROKEN_LINK.getCategoryName())) {
            fail("Definition category was not upgraded correctly");
        }
    }

    public void testConvertCategories() throws UpgradeFailureException, MalformedURLException, SQLException, NoDatabaseConnectionException {
        DefinitionPolicyDescriptor desc = DefinitionPolicyDescriptor.createCategoryPolicy(21, "Anomaly", DefinitionPolicyDescriptor.DefinitionPolicyAction.EXCLUDE);
        desc.saveToDatabase(app.getDatabaseConnection(DatabaseAccessType.ADMIN));
        populateSamples();
        converter.doUpgrade(app);
        checkSamples();
        DefinitionPolicyDescriptor desc2 = policyManagement.getPolicy(desc.getPolicyID());
        if (!desc2.getDefinitionCategory().equalsIgnoreCase(MetaDefinition.BROKEN_LINK.getCategoryName())) {
            fail("Definition category was not upgraded correctly");
        }
    }

    public void testConvertSubCategories() throws UpgradeFailureException, SQLException, NoDatabaseConnectionException, MalformedURLException {
        DefinitionPolicyDescriptor desc = DefinitionPolicyDescriptor.createDefinitionPolicy(1, 20, "Anomaly", "ContentError", MetaDefinition.BROKEN_LINK.getName(), null, DefinitionPolicyAction.EXCLUDE);
        desc.saveToDatabase(app.getDatabaseConnection(DatabaseAccessType.ADMIN));
        populateSamples();
        converter.doUpgrade(app);
        checkSamples();
        DefinitionPolicyDescriptor desc2 = policyManagement.getPolicy(desc.getPolicyID());
        if (!desc2.getDefinitionName().equalsIgnoreCase(MetaDefinition.BROKEN_LINK.getName())) {
            fail("Definition name was changed inappropriately");
        }
        if (!desc2.getDefinitionSubCategory().equalsIgnoreCase(MetaDefinition.BROKEN_LINK.getSubCategoryName())) {
            fail("Definition sub-category was not upgraded correctly");
        }
        if (!desc2.getDefinitionCategory().equalsIgnoreCase(MetaDefinition.BROKEN_LINK.getCategoryName())) {
            fail("Definition category was not upgraded correctly");
        }
    }
}
