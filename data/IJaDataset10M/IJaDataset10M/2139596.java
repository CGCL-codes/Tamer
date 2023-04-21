package de.fau.cs.dosis.drug.service;

import java.io.FileInputStream;
import java.util.List;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import de.fau.cs.dosis.DataFactory;
import de.fau.cs.dosis.DosisAssert;
import de.fau.cs.dosis.account.dto.AccountDigest;
import de.fau.cs.dosis.account.service.AccountService;
import de.fau.cs.dosis.account.service.AccountServiceException;
import de.fau.cs.dosis.drug.DrugServiceException;
import de.fau.cs.dosis.drug.dto.Drug;
import de.fau.cs.dosis.drug.dto.DrugCreate;
import de.fau.cs.dosis.drug.dto.DrugCreateRevision;
import de.fau.cs.dosis.drug.dto.DrugRevisionFilter;
import de.fau.cs.dosis.drug.manager.ActiveIngredientManagerException;
import de.fau.cs.dosis.drug.manager.ActiveIngredientManagerJpaStaticTest;
import de.fau.cs.dosis.drug.model.ActiveIngredient;
import de.fau.cs.dosis.drug.model.ActiveIngredientRevision;
import de.fau.cs.dosis.drug.model.RevisionStatus;
import de.fau.cs.dosis.service.AccessDeniedException;
import de.fau.cs.dosis.util.StringUtils;

public class DrugServiceIntegratedTest {

    private static final String REFERENCE_FILE = "src/test/resources/references/" + ActiveIngredientManagerJpaStaticTest.class.getName() + ".xml";

    private static DrugService service;

    private static AccountService accountService;

    @BeforeClass
    public static void setupClass() throws Exception {
        AbstractXmlApplicationContext ctx = new FileSystemXmlApplicationContext("src/test/resources/spring/DrugServiceIntegratedContext.xml");
        service = ctx.getBean(DrugService.class);
        accountService = ctx.getBean(AccountService.class);
        IDatabaseTester databaseTester = new JdbcDatabaseTester("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:mem_ds", "sa", "");
        IDataSet dataSet = new XmlDataSet(new FileInputStream(REFERENCE_FILE));
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(), dataSet);
    }

    private AccountDigest getEditor() {
        try {
            return accountService.getAuthorizedAccount("editor", "editor");
        } catch (AccountServiceException e) {
            return null;
        }
    }

    private AccountDigest getAdmin() {
        try {
            return accountService.getAuthorizedAccount("admin", "admin");
        } catch (AccountServiceException e) {
            return null;
        }
    }

    private AccountDigest getReviewer() {
        try {
            return accountService.getAuthorizedAccount("reviewer", "reviewer");
        } catch (AccountServiceException e) {
            return null;
        }
    }

    @Test
    public void testGetBySlug() throws AccessDeniedException, DrugServiceException {
        Drug drug = service.getDrugBySlug(getEditor(), "slug");
        Assert.assertNotNull(drug);
        Assert.assertEquals("name", drug.getName());
        Assert.assertEquals("slug", drug.getSlug());
        Assert.assertNull(service.getDrugBySlug(getEditor(), "slug-not-existing"));
    }

    @Test
    public void testCreateDrugDetails() throws AccessDeniedException, ActiveIngredientManagerException, DrugServiceException {
        DrugCreate create = DataFactory.createDrug("test2");
        service.createDrug(getAdmin(), create);
        Drug drug = service.getDrugBySlug(getAdmin(), create.getSlug());
        DosisAssert.equals("createDetails", create, drug);
    }

    @Test
    public void testCreateAndReview() throws AccessDeniedException, DrugServiceException {
        Drug[] revisions = service.getDrugRevisions(getAdmin(), DrugRevisionFilter.UNREVIEWED);
        int beforeCount = revisions.length;
        DrugCreate create = DataFactory.createDrug("_createAndReview");
        Drug d = service.createDrug(getAdmin(), create);
        Drug drug = service.getDrugBySlug(getAdmin(), create.getSlug());
        Assert.assertNull(drug.getRevision());
        revisions = service.getDrugRevisions(getAdmin(), DrugRevisionFilter.UNREVIEWED);
        Assert.assertEquals(beforeCount + 1, revisions.length);
        DrugRevisionFilter filter = new DrugRevisionFilter();
        filter.setStatus(RevisionStatus.AWAITING);
        filter.setDrug(drug);
        revisions = service.getDrugRevisions(getAdmin(), filter);
        Assert.assertEquals(1, revisions.length);
        service.review(getAdmin(), revisions[0].getRevision());
        revisions = service.getDrugRevisions(getAdmin(), DrugRevisionFilter.UNREVIEWED);
        Assert.assertEquals(beforeCount, revisions.length);
        drug = service.getDrugByGuid(getAdmin(), drug.getGuid());
        Assert.assertNotNull(drug.getRevision());
        DosisAssert.equals("", create, drug);
    }

    @Test
    public void getCreateDrugUsers() throws AccessDeniedException, DrugServiceException {
        Drug[] unreviewed = service.getDrugRevisions(getAdmin(), DrugRevisionFilter.UNREVIEWED);
        int beforeCount = unreviewed.length;
        DrugCreate create = DataFactory.createDrug("_uradmin");
        service.createDrug(getAdmin(), create);
        unreviewed = service.getDrugRevisions(getAdmin(), DrugRevisionFilter.UNREVIEWED);
        create = DataFactory.createDrug("_ureditor");
        service.createDrug(getAdmin(), create);
        unreviewed = service.getDrugRevisions(getEditor(), DrugRevisionFilter.UNREVIEWED);
        create = DataFactory.createDrug("_urreviewer");
        service.createDrug(getAdmin(), create);
        unreviewed = service.getDrugRevisions(getReviewer(), DrugRevisionFilter.UNREVIEWED);
        Assert.assertEquals(beforeCount + 3, unreviewed.length);
    }

    public void testGetDigestFiltered() {
    }
}
