package com.vmware.spring.workshop.dao.impl.jpa;

import java.util.List;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import com.vmware.spring.workshop.dao.api.BranchDao;
import com.vmware.spring.workshop.model.banking.Branch;

/**
 * @author lgoldstein
 */
@Repository("branchDao")
@Transactional
public class BranchDaoImpl extends AbstractIdentifiedJpaDaoImpl<Branch> implements BranchDao {

    public BranchDaoImpl() {
        super(Branch.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Branch findByBranchCode(int code) {
        Assert.isTrue(code > 0, "Non positive code N/A");
        final TypedQuery<Branch> query = getNamedQuery("findByBranchCode").setParameter("code", Integer.valueOf(code));
        return getUniqueResult(query);
    }

    @Override
    @Transactional(readOnly = true)
    public Branch findByBranchName(String name) {
        Assert.hasText(name, "No bank name provided");
        final TypedQuery<Branch> query = getNamedQuery("findByBranchName").setParameter("name", name);
        return getUniqueResult(query);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Branch> findByBankId(Long bankId) {
        return getNamedIdQueryResults("findByBankId", bankId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Branch> findByBranchLocation(String location) {
        Assert.hasText(location, "No location provided");
        final TypedQuery<Branch> query = getNamedQuery("findByBranchLocation").setParameter("location", "%" + location.toLowerCase() + "%");
        return getQueryResults(query);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Branch> findByBranchBankCode(int bankCode) {
        Assert.isTrue(bankCode > 0, "Non-positive bank code N/A");
        final TypedQuery<Branch> query = getNamedQuery("findByBranchBankCode").setParameter("code", Integer.valueOf(bankCode));
        return getQueryResults(query);
    }
}
