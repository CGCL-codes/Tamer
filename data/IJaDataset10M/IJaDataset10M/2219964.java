package org.demis.orc.emailMessageAttachment;

import java.util.Collection;

/**
  * DAO (Data Access Object) interface for EmailMessageAttachment.
  */
public interface EmailMessageAttachmentDAO {

    public EmailMessageAttachment findById(java.lang.String emailMessageAttachmentId);

    public Collection<EmailMessageAttachment> findByExemple(EmailMessageAttachment emailMessageAttachment);

    public int findCount(final EmailMessageAttachment emailMessageAttachment);

    public void save(EmailMessageAttachment emailMessageAttachment);

    public void saveAll(final Collection<EmailMessageAttachment> emailMessageAttachments);

    public void delete(EmailMessageAttachment emailMessageAttachment);

    public void deleteAll(final Collection<EmailMessageAttachment> emailMessageAttachments);
}
