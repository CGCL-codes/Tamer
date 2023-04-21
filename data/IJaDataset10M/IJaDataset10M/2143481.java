package org.koossery.adempiere.core.contract.criteria.report;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.koossery.adempiere.core.contract.criteria.KTADempiereBaseCriteria;

public class StatementOfAccountCriteria extends KTADempiereBaseCriteria {

    private static final long serialVersionUID = 1L;

    private int ad_PInstance_ID;

    private BigDecimal amtAcctCr;

    private BigDecimal amtAcctDr;

    private BigDecimal balance;

    private Timestamp dateAcct = null;

    private Timestamp dateAcctInf = null;

    private Timestamp dateAcctSup = null;

    private String description;

    private int fact_Acct_ID;

    private int levelNo;

    private String name;

    private BigDecimal qty;

    private String isActive;

    private int c_AcctSchema_ID;

    private String postingType = "A";

    private int c_Period_ID;

    private int ad_Org_ID = -1;

    private int c_Account_ID = -1;

    private int c_BPartner_ID;

    private int m_Product_ID;

    private int c_Project_ID;

    private int c_Activity_ID;

    private int c_SalesRegion_ID;

    private int c_Campaign_ID;

    private String UpdateBalances;

    private int pa_Hierarchy_ID;

    public int getAd_PInstance_ID() {
        return ad_PInstance_ID;
    }

    public void setAd_PInstance_ID(int ad_PInstance_ID) {
        this.ad_PInstance_ID = ad_PInstance_ID;
    }

    public BigDecimal getAmtAcctCr() {
        return amtAcctCr;
    }

    public void setAmtAcctCr(BigDecimal amtAcctCr) {
        this.amtAcctCr = amtAcctCr;
    }

    public BigDecimal getAmtAcctDr() {
        return amtAcctDr;
    }

    public void setAmtAcctDr(BigDecimal amtAcctDr) {
        this.amtAcctDr = amtAcctDr;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Timestamp getDateAcct() {
        return dateAcct;
    }

    public void setDateAcct(Timestamp dateAcct) {
        this.dateAcct = dateAcct;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFact_Acct_ID() {
        return fact_Acct_ID;
    }

    public void setFact_Acct_ID(int fact_Acct_ID) {
        this.fact_Acct_ID = fact_Acct_ID;
    }

    public int getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(int levelNo) {
        this.levelNo = levelNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }

    public Timestamp getDateAcctInf() {
        return dateAcctInf;
    }

    public void setDateAcctInf(Timestamp dateAcctInf) {
        this.dateAcctInf = dateAcctInf;
    }

    public Timestamp getDateAcctSup() {
        return dateAcctSup;
    }

    public void setDateAcctSup(Timestamp dateAcctSup) {
        this.dateAcctSup = dateAcctSup;
    }

    public String getPostingType() {
        return postingType;
    }

    public void setPostingType(String postingType) {
        this.postingType = postingType;
    }

    public int getC_Period_ID() {
        return c_Period_ID;
    }

    public void setC_Period_ID(int period_ID) {
        c_Period_ID = period_ID;
    }

    public int getC_Account_ID() {
        return c_Account_ID;
    }

    public void setC_Account_ID(int account_ID) {
        c_Account_ID = account_ID;
    }

    public int getC_BPartner_ID() {
        return c_BPartner_ID;
    }

    public void setC_BPartner_ID(int partner_ID) {
        c_BPartner_ID = partner_ID;
    }

    public int getM_Product_ID() {
        return m_Product_ID;
    }

    public void setM_Product_ID(int product_ID) {
        m_Product_ID = product_ID;
    }

    public int getC_Project_ID() {
        return c_Project_ID;
    }

    public void setC_Project_ID(int project_ID) {
        c_Project_ID = project_ID;
    }

    public int getC_Activity_ID() {
        return c_Activity_ID;
    }

    public void setC_Activity_ID(int activity_ID) {
        c_Activity_ID = activity_ID;
    }

    public int getC_SalesRegion_ID() {
        return c_SalesRegion_ID;
    }

    public void setC_SalesRegion_ID(int salesRegion_ID) {
        c_SalesRegion_ID = salesRegion_ID;
    }

    public String getUpdateBalance() {
        return UpdateBalances;
    }

    public void setUpdateBalance(String updateBalance) {
        UpdateBalances = updateBalance;
    }

    public int getAd_ReportHierarchy_ID() {
        return pa_Hierarchy_ID;
    }

    public void setAd_ReportHierarchy_ID(int ad_ReportHierarchy_ID) {
        this.pa_Hierarchy_ID = ad_ReportHierarchy_ID;
    }

    public int getC_AcctSchema_ID() {
        return c_AcctSchema_ID;
    }

    public void setC_AcctSchema_ID(int acctSchema_ID) {
        c_AcctSchema_ID = acctSchema_ID;
    }

    public int getC_Campaign_ID() {
        return c_Campaign_ID;
    }

    public void setC_Campaign_ID(int campaign_ID) {
        c_Campaign_ID = campaign_ID;
    }

    public int getAd_Org_ID() {
        return ad_Org_ID;
    }

    public void setAd_Org_ID(int ad_Org_ID) {
        this.ad_Org_ID = ad_Org_ID;
    }
}
