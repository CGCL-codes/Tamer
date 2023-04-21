package com.vlee.bean.loyalty;

import java.sql.*;
import javax.servlet.http.HttpSession;
import javax.sql.*;
import java.util.*;
import java.io.Serializable;
import java.math.BigDecimal;
import com.vlee.util.*;
import com.vlee.ejb.supplier.*;
import com.vlee.ejb.accounting.*;
import com.vlee.ejb.inventory.*;
import com.vlee.ejb.customer.*;
import com.vlee.ejb.user.*;
import com.vlee.util.*;

public class MembershipCampaignEditForm extends java.lang.Object implements Serializable {

    public Integer userId;

    public CustMembershipCampaignIndexObject campaign = null;

    public boolean dirtyFlag = false;

    public TreeMap treeWidgetRules;

    public MembershipCampaignEditForm(Integer usrId) {
        this.userId = usrId;
        this.dirtyFlag = false;
        this.treeWidgetRules = new TreeMap();
    }

    public TreeMap getTreeWidgetRules() {
        return this.treeWidgetRules;
    }

    public void reset() {
        this.treeWidgetRules.clear();
        this.campaign = null;
    }

    public void loadCampaign(Integer campaignId) throws Exception {
        this.treeWidgetRules.clear();
        if (this.dirtyFlag && this.campaign != null) {
            throw new Exception("You have not saved the previous campaign!");
        }
        this.campaign = CustMembershipCampaignIndexNut.getObjectTree(campaignId);
        for (int cnt1 = 0; cnt1 < this.campaign.vecRules.size(); cnt1++) {
            CustMembershipCampaignRulesObject rulesObj = (CustMembershipCampaignRulesObject) this.campaign.vecRules.get(cnt1);
            MembershipCampaignWidgetRewardRulesForm widgetRewardRules = new MembershipCampaignWidgetRewardRulesForm(this.userId);
            widgetRewardRules.setRules(rulesObj);
            this.treeWidgetRules.put(widgetRewardRules.getGuid(), widgetRewardRules);
        }
    }

    public TreeMap getSortedWidgetRules() {
        TreeMap sortedWidget = new TreeMap();
        Vector vecWidgetRules = new Vector(this.treeWidgetRules.values());
        for (int cnt1 = 0; cnt1 < vecWidgetRules.size(); cnt1++) {
            MembershipCampaignWidgetRewardRulesForm widgetRules = (MembershipCampaignWidgetRewardRulesForm) vecWidgetRules.get(cnt1);
            String theKey = CurrencyFormat.format(new BigDecimal(widgetRules.getRules().sequence.intValue()), "0000000000") + widgetRules.getGuid();
            sortedWidget.put(theKey, widgetRules);
        }
        return sortedWidget;
    }

    public void addWidgetRules(MembershipCampaignWidgetRewardRulesForm widgetRules) {
        this.treeWidgetRules.put(widgetRules.getGuid(), widgetRules);
    }

    public void removeWidgetRules(String guid) {
        this.treeWidgetRules.remove(guid);
    }

    public CustMembershipCampaignIndexObject getCampaign() {
        return this.campaign;
    }

    public void createNewCampaign() throws Exception {
        if (this.dirtyFlag && this.campaign != null) {
            throw new Exception("You have not saved the previous campaign!");
        }
        this.campaign = new CustMembershipCampaignIndexObject();
        this.campaign.type1 = CustMembershipCampaignIndexBean.TYPE1_REWARD;
        CustMembershipCampaignIndex ejb = CustMembershipCampaignIndexNut.fnCreate(this.campaign);
        {
            AuditTrailObject atObj = new AuditTrailObject();
            atObj.userId = this.userId;
            atObj.auditType = AuditTrailBean.TYPE_CONFIG;
            atObj.time = TimeFormat.getTimestamp();
            atObj.remarks = "create campaign: " + this.campaign.name;
            atObj.tc_entity_table = CustMembershipCampaignIndexBean.TABLENAME;
            atObj.tc_entity_id = ejb.getPkid();
            atObj.tc_action = AuditTrailBean.TC_ACTION_CREATE;
            AuditTrailNut.fnCreate(atObj);
        }
        this.dirtyFlag = true;
    }

    public void setDetails(String code, String name, String description, Timestamp tsStart, Timestamp tsEnd, String membershipLogic, String branchLogic, String branchConditions, String status) {
        this.campaign.code = code;
        this.campaign.name = name;
        this.campaign.description = description;
        this.campaign.date_start = tsStart;
        this.campaign.date_end = tsEnd;
        this.campaign.membership_logic = membershipLogic;
        this.campaign.branch_logic = branchLogic;
        this.campaign.branch_conditions = branchConditions;
        this.campaign.status = status;
    }

    public void setSequence(String guid, Integer iSequence) {
        Vector vecWidgetRules = new Vector(this.treeWidgetRules.values());
        for (int cnt1 = 0; cnt1 < vecWidgetRules.size(); cnt1++) {
            MembershipCampaignWidgetRewardRulesForm widgetRulesForm = (MembershipCampaignWidgetRewardRulesForm) vecWidgetRules.get(cnt1);
            CustMembershipCampaignRulesObject rulesObj = widgetRulesForm.getRules();
            if (rulesObj.guid.equals(guid)) {
                rulesObj.sequence = iSequence;
            }
        }
    }

    public synchronized void saveCampaign() throws Exception {
        CustMembershipCampaignIndex campaignEJB = CustMembershipCampaignIndexNut.getHandle(this.campaign.pkid);
        try {
            campaignEJB.setObject(this.campaign);
            {
                AuditTrailObject atObj = new AuditTrailObject();
                atObj.userId = this.userId;
                atObj.auditType = AuditTrailBean.TYPE_CONFIG;
                atObj.time = TimeFormat.getTimestamp();
                atObj.remarks = "update campaign ";
                atObj.tc_entity_table = CustMembershipCampaignIndexBean.TABLENAME;
                atObj.tc_entity_id = campaignEJB.getPkid();
                atObj.tc_action = AuditTrailBean.TC_ACTION_UPDATE;
                AuditTrailNut.fnCreate(atObj);
            }
            Vector vecWidgetRules = new Vector(this.treeWidgetRules.values());
            for (int cnt1 = 0; cnt1 < vecWidgetRules.size(); cnt1++) {
                MembershipCampaignWidgetRewardRulesForm widgetRulesForm = (MembershipCampaignWidgetRewardRulesForm) vecWidgetRules.get(cnt1);
                CustMembershipCampaignRulesObject rulesObj = widgetRulesForm.getRules();
                if (rulesObj.pkid.intValue() == 0) {
                    rulesObj.index_id = this.campaign.pkid;
                    CustMembershipCampaignRules rulesEJB = CustMembershipCampaignRulesNut.fnCreate(rulesObj);
                    {
                        AuditTrailObject atObj = new AuditTrailObject();
                        atObj.userId = this.userId;
                        atObj.auditType = AuditTrailBean.TYPE_CONFIG;
                        atObj.time = TimeFormat.getTimestamp();
                        atObj.remarks = "create campaign rules ";
                        atObj.tc_entity_table = CustMembershipCampaignRulesBean.TABLENAME;
                        atObj.tc_entity_id = rulesEJB.getPkid();
                        atObj.tc_action = AuditTrailBean.TC_ACTION_CREATE;
                        AuditTrailNut.fnCreate(atObj);
                    }
                } else {
                    CustMembershipCampaignRules rulesEJB = CustMembershipCampaignRulesNut.getHandle(rulesObj.pkid);
                    rulesEJB.setObject(rulesObj);
                    {
                        AuditTrailObject atObj = new AuditTrailObject();
                        atObj.userId = this.userId;
                        atObj.auditType = AuditTrailBean.TYPE_CONFIG;
                        atObj.time = TimeFormat.getTimestamp();
                        atObj.remarks = "update campaign rules ";
                        atObj.tc_entity_table = CustMembershipCampaignRulesBean.TABLENAME;
                        atObj.tc_entity_id = rulesEJB.getPkid();
                        atObj.tc_action = AuditTrailBean.TC_ACTION_UPDATE;
                        AuditTrailNut.fnCreate(atObj);
                    }
                }
            }
            for (int cnt1 = 0; cnt1 < this.campaign.vecRules.size(); cnt1++) {
                CustMembershipCampaignRulesObject rules = (CustMembershipCampaignRulesObject) this.campaign.vecRules.get(cnt1);
                boolean deleted = true;
                for (int cnt2 = 0; cnt2 < vecWidgetRules.size(); cnt2++) {
                    MembershipCampaignWidgetRewardRulesForm widgetRulesForm = (MembershipCampaignWidgetRewardRulesForm) vecWidgetRules.get(cnt2);
                    CustMembershipCampaignRulesObject rulesObj = widgetRulesForm.getRules();
                    if (rulesObj.pkid.equals(rules.pkid)) {
                        deleted = false;
                    }
                }
                if (deleted) {
                    CustMembershipCampaignRules rulesEJB = CustMembershipCampaignRulesNut.getHandle(rules.pkid);
                    try {
                        rulesEJB.remove();
                        {
                            AuditTrailObject atObj = new AuditTrailObject();
                            atObj.userId = this.userId;
                            atObj.auditType = AuditTrailBean.TYPE_CONFIG;
                            atObj.time = TimeFormat.getTimestamp();
                            atObj.remarks = "delete campaign rules ";
                            atObj.tc_entity_table = CustMembershipCampaignRulesBean.TABLENAME;
                            atObj.tc_entity_id = rulesEJB.getPkid();
                            atObj.tc_action = AuditTrailBean.TC_ACTION_DELETE;
                            AuditTrailNut.fnCreate(atObj);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        this.dirtyFlag = false;
        CustMembershipCampaignEngine.reloadCampaign();
    }
}
