package uk.ac.ebi.intact.core.persister.standard;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.ac.ebi.intact.core.persister.PersisterException;
import uk.ac.ebi.intact.model.*;
import uk.ac.ebi.intact.model.util.AnnotatedObjectUtils;
import uk.ac.ebi.intact.model.util.CvObjectUtils;
import java.util.Collection;

/**
 * TODO comment this
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: CvObjectPersister.java 9727 2007-09-18 15:51:02Z baranda $
 */
public class CvObjectPersister extends AbstractAnnotatedObjectPersister<CvObject> {

    /**
     * Sets up a logger for that class.
     */
    private static final Log log = LogFactory.getLog(CvObjectPersister.class);

    private static ThreadLocal<CvObjectPersister> instance = new ThreadLocal<CvObjectPersister>() {

        @Override
        protected CvObjectPersister initialValue() {
            return new CvObjectPersister();
        }
    };

    public static CvObjectPersister getInstance() {
        return instance.get();
    }

    protected CvObjectPersister() {
        super();
    }

    @Override
    protected void saveOrUpdateAttributes(CvObject intactObject) throws PersisterException {
        if (intactObject.getXrefs().isEmpty()) {
            log.warn("CvObject without Xrefs: " + intactObject.getShortLabel());
        }
        super.saveOrUpdateAttributes(intactObject);
    }

    /**
     * TODO: attempt identityXref(mi first and ia next)-cvType and then label-cvType
     */
    @Override
    protected CvObject fetchFromDataSource(CvObject intactObject) {
        CvObjectXref identityXref = CvObjectUtils.getPsiMiIdentityXref(intactObject);
        if (identityXref != null) {
            String mi = identityXref.getPrimaryId();
            CvObject cvObject = getIntactContext().getDataContext().getDaoFactory().getCvObjectDao(intactObject.getClass()).getByPsiMiRef(mi);
            if (cvObject != null) {
                return cvObject;
            }
        }
        return getIntactContext().getDataContext().getDaoFactory().getCvObjectDao().getByShortLabel(intactObject.getClass(), intactObject.getShortLabel());
    }
}
