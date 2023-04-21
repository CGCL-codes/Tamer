package com.medsol.company.service;

import com.medsol.company.model.Company;
import com.medsol.common.service.GenericService;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: me
 * Date: 15 May, 2008
 * Time: 6:52:16 PM
 */
@Transactional(readOnly = false)
public interface CompanyService extends GenericService<Company, Long, Exception> {

    public List<Company> getCompanyByName(String name);

    public List<Company> getOwnerCompanies();

    public List<Company> getNonOwnerCompanies();

    public List<Company> getCompaniesByTypes(String[] types);

    public List<Company> getCompaniesWithNoLH();

    public List<Company> getSearchResults(Company c);
}
