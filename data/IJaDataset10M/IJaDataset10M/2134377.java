package com.inet.qlcbcc.repository.support;

import org.springframework.stereotype.Repository;
import org.webos.repository.hibernate.AbstractHibernateModifiableRepository;
import com.inet.qlcbcc.repository.CodeConfigurationModifiableRepository;
import com.inet.qlcbcc.domain.CodeConfiguration;

/**
 * CodeConfigurationModifiableRepositorySupport.
 *
 * @author Hien Nguyen Van
 * @version $Id: CodeConfigurationModifiableRepositorySupport.java 2011-03-20 12:00:00z nguyen_hv $
 *
 * @since 1.0
 */
@Repository(value = "codeConfigurationModifiableRepository")
public class CodeConfigurationModifiableRepositorySupport extends AbstractHibernateModifiableRepository<CodeConfiguration, String> implements CodeConfigurationModifiableRepository {

    /**
   * Constructor.
   */
    protected CodeConfigurationModifiableRepositorySupport() {
        super(CodeConfiguration.class);
    }
}
