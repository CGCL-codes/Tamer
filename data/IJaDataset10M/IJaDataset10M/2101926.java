package com.inet.qlcbcc.repository.support;

import org.springframework.stereotype.Repository;
import org.webos.repository.hibernate.AbstractHibernateModifiableRepository;
import com.inet.qlcbcc.repository.FileContentModifiableRepository;
import com.inet.qlcbcc.domain.FileContent;

/**
 * FileContentModifiableRepositorySupport.
 *
 * @author Dung Nguyen
 * @version $Id: FileContentModifiableRepositorySupport.java 2011-06-17 17:00:19z nguyen_dv $
 *
 * @since 1.0
 */
@Repository("fileContentModifiableRepository")
public class FileContentModifiableRepositorySupport extends AbstractHibernateModifiableRepository<FileContent, String> implements FileContentModifiableRepository {

    /**
   * Creates {@link FileContentModifiableRepositorySupport}
   */
    public FileContentModifiableRepositorySupport() {
        super(FileContent.class);
    }
}
