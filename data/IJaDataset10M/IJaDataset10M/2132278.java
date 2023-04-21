package uk.ac.ebi.intact.update.persistence.dao.protein.impl;

import junit.framework.Assert;
import org.junit.Test;
import uk.ac.ebi.intact.dbupdate.prot.errors.UpdateError;
import uk.ac.ebi.intact.update.model.protein.ProteinUpdateProcess;
import uk.ac.ebi.intact.update.model.protein.errors.DeadUniprotAc;
import uk.ac.ebi.intact.update.model.protein.errors.DefaultPersistentUpdateError;
import uk.ac.ebi.intact.update.model.protein.errors.ImpossibleParentToReview;
import uk.ac.ebi.intact.update.model.unit.UpdateBasicTestCase;
import uk.ac.ebi.intact.update.persistence.dao.protein.ProteinUpdateErrorDao;

/**
 * unit test for protein update error dao
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>10/08/11</pre>
 */
public class ProteinUpdateErrorDaoImplTest extends UpdateBasicTestCase {

    @Test
    public void create_protein_error() {
        ProteinUpdateErrorDao<DefaultPersistentUpdateError> errorDao = getUpdateDaoFactory().getProteinUpdateErrorDao(DefaultPersistentUpdateError.class);
        DefaultPersistentUpdateError error = getMockBuilder().createDefaultError();
        ImpossibleParentToReview error2 = getMockBuilder().createImpossibleParentToReviewError();
        errorDao.persist(error);
        errorDao.persist(error2);
        Assert.assertEquals(2, getUpdateDaoFactory().getProteinUpdateErrorDao(DefaultPersistentUpdateError.class).countAll());
        Assert.assertEquals(1, getUpdateDaoFactory().getProteinUpdateErrorDao(ImpossibleParentToReview.class).getAll().size());
        Assert.assertEquals(1, getUpdateDaoFactory().getProteinUpdateErrorDao(DeadUniprotAc.class).getAll().size());
    }

    @Test
    public void updated_error_event() {
        ProteinUpdateErrorDao<DefaultPersistentUpdateError> errorDao = getUpdateDaoFactory().getProteinUpdateErrorDao(DefaultPersistentUpdateError.class);
        DefaultPersistentUpdateError error = getMockBuilder().createDefaultError();
        errorDao.persist(error);
        Assert.assertEquals(1, errorDao.countAll());
        Assert.assertEquals(UpdateError.dead_uniprot_ac, error.getErrorLabel());
        DefaultPersistentUpdateError error2 = errorDao.getById(error.getId());
        Assert.assertNotNull(error2);
        error2.setErrorLabel(UpdateError.both_isoform_and_chain_xrefs);
        errorDao.update(error2);
        DefaultPersistentUpdateError error3 = errorDao.getById(error.getId());
        Assert.assertEquals(UpdateError.both_isoform_and_chain_xrefs, error3.getErrorLabel());
    }

    @Test
    public void delete_error_event() {
        ProteinUpdateErrorDao<DefaultPersistentUpdateError> errorDao = getUpdateDaoFactory().getProteinUpdateErrorDao(DefaultPersistentUpdateError.class);
        DefaultPersistentUpdateError error = getMockBuilder().createDefaultError();
        errorDao.persist(error);
        Assert.assertEquals(1, errorDao.countAll());
        errorDao.delete(error);
        Assert.assertEquals(0, errorDao.countAll());
    }

    @Test
    public void search_error_event_by_label() {
        ProteinUpdateErrorDao<DefaultPersistentUpdateError> errorDao = getUpdateDaoFactory().getProteinUpdateErrorDao(DefaultPersistentUpdateError.class);
        DefaultPersistentUpdateError error = getMockBuilder().createDefaultError();
        ProteinUpdateProcess process = getMockBuilder().createUpdateProcess();
        error.setUpdateProcess(process);
        errorDao.persist(error);
        UpdateError label = error.getErrorLabel();
        Long id = process.getId();
        Assert.assertEquals(1, errorDao.getAll().size());
        Assert.assertEquals(1, errorDao.getUpdateErrorByLabel(id, label).size());
    }
}
