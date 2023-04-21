package com.inet.qlcbcc.repository.support;

import org.springframework.stereotype.Repository;
import org.webos.repository.hibernate.AbstractHibernateReadableRepository;
import com.inet.qlcbcc.domain.ChangesSalary;
import com.inet.qlcbcc.repository.ChangesSalaryReadableRepository;

/**
 * ChangesSalaryReadableRepositorySupport.
 *
 * @author Thoang Tran
 * @version $Id: ChangesSalaryReadableRepositorySupport.java Nov 24, 2011 9:12:00 PM thoangtd $
 *
 * @since 1.0
 */
@Repository(value = "changesSalaryReadableRepository")
public class ChangesSalaryReadableRepositorySupport extends AbstractHibernateReadableRepository<ChangesSalary, String> implements ChangesSalaryReadableRepository {

    /**
   * @param clazz
   */
    protected ChangesSalaryReadableRepositorySupport() {
        super(ChangesSalary.class);
    }
}
