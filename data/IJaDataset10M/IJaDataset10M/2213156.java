package org.demis.elf.accountingEntry;

import java.util.Collection;

public interface AccountingEntryManager {

    public AccountingEntry findById(java.lang.String accountingEntryId);

    public Collection<AccountingEntry> findByExemple(AccountingEntry accountingEntry);

    public int findCount(final AccountingEntry accountingEntry);

    public void save(AccountingEntry accountingEntry);

    public void saveAll(final Collection<AccountingEntry> accountingEntrys);

    public void delete(AccountingEntry accountingEntry);

    public void deleteAll(final Collection<AccountingEntry> accountingEntrys);

    public void setAccountingEntryDAO(AccountingEntryDAO accountingEntryDAO);
}
