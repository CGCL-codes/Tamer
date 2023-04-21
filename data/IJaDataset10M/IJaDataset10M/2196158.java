package com.medsol.account.service;

import com.medsol.account.model.LedgerHead;
import com.medsol.common.service.GenericService;
import java.util.List;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: me
 * Date: 15 May, 2008
 * Time: 6:52:16 PM
 */
public interface LedgerHeadService extends GenericService<LedgerHead, Long, Exception> {

    public List<LedgerHead> findByOwnerId(Integer ownerId);

    public List<LedgerHead> findLHByCashBank(Integer ownerId);

    public List<LedgerHead> findLHByType(Integer ownerId, Integer[] trnsCode);

    public Iterator getSumOfAmounts(Integer ownerId);

    public List<LedgerHead> getLHByCompanyId(Integer ownerId, Long CompanyId);

    public List<LedgerHead> getLHByAcctGrp(Integer ownerId, String acctGrpId);

    public List<LedgerHead> getLHByFilter(Map params);
}
