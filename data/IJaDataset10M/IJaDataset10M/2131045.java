package com.inet.qlcbcc.repository.support;

import org.springframework.stereotype.Repository;
import org.webos.repository.hibernate.AbstractHibernateModifiableRepository;
import com.inet.qlcbcc.repository.AttachmentModifiableRepository;
import com.inet.qlcbcc.domain.Attachment;

/**
 * AttachmentModifiableRepositorySupport.
 *
 * @author Dung Nguyen
 * @version $Id: AttachmentModifiableRepositorySupport.java 2011-06-17 17:51:26z nguyen_dv $
 *
 * @since 1.0
 */
@Repository("attachmentModifiableRepository")
public class AttachmentModifiableRepositorySupport extends AbstractHibernateModifiableRepository<Attachment, String> implements AttachmentModifiableRepository {

    /**
   * Creates {@link AttachmentModifiableRepository} object.
   */
    public AttachmentModifiableRepositorySupport() {
        super(Attachment.class);
    }
}
