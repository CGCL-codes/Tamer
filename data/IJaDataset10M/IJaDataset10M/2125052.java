package com.inet.qlcbcc.repository.support;

import org.springframework.stereotype.Repository;
import org.webos.repository.hibernate.AbstractHibernateModifiableRepository;
import com.inet.qlcbcc.domain.WorkingProcess;
import com.inet.qlcbcc.repository.WorkingProcessModifiableRepository;

/**
 * WorkingProcessModifiableRepositorySupport.
 *
 * @author Thoang Tran
 * @version $Id: WorkingProcessModifiableRepositorySupport.java Nov 25, 2011 10:43:39 PM thoangtd $
 *
 * @since 1.0
 */
@Repository(value = "workingProcessModifiableRepository")
public class WorkingProcessModifiableRepositorySupport extends AbstractHibernateModifiableRepository<WorkingProcess, String> implements WorkingProcessModifiableRepository {

    /**
   * @param clazz
   */
    protected WorkingProcessModifiableRepositorySupport() {
        super(WorkingProcess.class);
    }
}
