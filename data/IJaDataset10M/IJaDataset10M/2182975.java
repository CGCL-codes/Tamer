package nl.tranquilizedquality.adm.core.persistence.dao;

import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStepExecution;
import nl.tranquilizedquality.adm.commons.hibernate.dao.BaseDao;

/**
 * DAO that manager {@link ReleaseStepExecution} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@gmail.com)
 * @since 3 jun. 2011
 * @param <T>
 *            The implementation type.
 */
public interface ReleaseStepExecutionDao<T extends ReleaseStepExecution> extends BaseDao<T, Long> {
}
