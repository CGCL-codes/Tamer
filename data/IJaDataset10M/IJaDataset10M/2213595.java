package org.demis.orc.emailMessageStatus;

import java.util.Collection;

/**
  * DAO (Data Access Object) interface for EmailMessageStatus.
  */
public interface EmailMessageStatusDAO {

    public EmailMessageStatus findById(java.lang.String emailMessageStatusId);

    public Collection<EmailMessageStatus> findByExemple(EmailMessageStatus emailMessageStatus);

    public int findCount(final EmailMessageStatus emailMessageStatus);

    public void save(EmailMessageStatus emailMessageStatus);

    public void saveAll(final Collection<EmailMessageStatus> emailMessageStatuss);

    public void delete(EmailMessageStatus emailMessageStatus);

    public void deleteAll(final Collection<EmailMessageStatus> emailMessageStatuss);
}
