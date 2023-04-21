package net.comensus.gh.core.dto;

import java.util.HashSet;
import java.util.Set;
import net.comensus.gh.core.entity.AccountingEntryType;
import net.comensus.gh.core.entity.Company;

/**
 *
 * @author fab
 */
public class SetupPlaceHolderDTO {

    private Set<Company> companies;

    private Set<AccountingEntryType> accountingEntryTypes;

    public Set<AccountingEntryType> getAccountingEntryTypes() {
        if (accountingEntryTypes == null) {
            accountingEntryTypes = new HashSet<AccountingEntryType>();
        }
        return accountingEntryTypes;
    }

    public void setAccountingEntryTypes(Set<AccountingEntryType> accountingEntryTypes) {
        this.accountingEntryTypes = accountingEntryTypes;
    }

    public Set<Company> getCompanies() {
        if (companies == null) {
            companies = new HashSet<Company>();
        }
        return companies;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
    }
}
