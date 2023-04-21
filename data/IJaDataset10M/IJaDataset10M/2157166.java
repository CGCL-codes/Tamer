package com.medsol.reports.repository;

import com.medsol.common.repository.GenericRepositoryImpl;
import com.medsol.reports.detail.TrialBalanceDetail;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: vinay
 * Date: 7 Sep, 2008
 * Time: 4:57:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrialBalanceReportRepository extends GenericRepositoryImpl<TrialBalanceDetail, Long> {

    public List<TrialBalanceDetail> getTrialBalanceReport(Map params) {
        return findUsingSQLQuery("trialBalanceQuery", TrialBalanceDetail.class, params);
    }

    public List<TrialBalanceDetail> getAcctGrpBalReport(Map params) {
        return findUsingSQLQuery("acctGrpBalanceQuery", TrialBalanceDetail.class, params);
    }
}
