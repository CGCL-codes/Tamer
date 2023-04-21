package org.demis.elf.mailbox;

import java.util.Collection;

/**
  * DAO (Data Access Object) interface for Mailbox.
  */
public interface MailboxDAO {

    public Mailbox findById(java.lang.String mailboxId);

    public Collection<Mailbox> findByExemple(Mailbox mailbox);

    public int findCount(final Mailbox mailbox);

    public void save(Mailbox mailbox);

    public void saveAll(final Collection<Mailbox> mailboxs);

    public void delete(Mailbox mailbox);

    public void deleteAll(final Collection<Mailbox> mailboxs);
}
