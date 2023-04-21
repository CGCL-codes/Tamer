package org.sysmhg.base.dao;

import java.util.List;
import org.sysmhg.base.model.Company;
import org.sysmhg.util.Pagination;

public interface CompanyDAO {

    @SuppressWarnings("unchecked")
    public List getCompanies(Company company, Pagination paginacion);

    public Company getCompany(Company company);

    public void createCompany(Company company);

    public void deleteCompany(Company company);

    public void updateCompany(Company company);
}
