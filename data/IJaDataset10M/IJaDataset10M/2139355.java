package uk.ac.ebi.intact.core.persistence.dao;

import org.joda.time.DateTime;
import uk.ac.ebi.intact.annotation.Mockable;
import uk.ac.ebi.intact.model.meta.ImexImport;
import uk.ac.ebi.intact.model.meta.ImexImportActivationType;

/**
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: ImexImportDao.java 13109 2009-05-11 10:30:49Z baranda $
 * @since 1.5
 */
@Mockable
public interface ImexImportDao extends BaseDao<ImexImport> {

    DateTime getLatestUpdate(ImexImportActivationType activationType);
}
