package org.koossery.adempiere.sisv.impl.role;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.compiere.model.X_AD_Role;
import org.koossery.adempiere.core.backend.interfaces.dao.role.IAD_RoleDAO;
import org.koossery.adempiere.core.backend.interfaces.sisv.role.IAD_RoleSISV;
import org.koossery.adempiere.core.contract.criteria.role.AD_RoleCriteria;
import org.koossery.adempiere.core.contract.dto.role.AD_RoleDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;
import org.koossery.adempiere.sisv.impl.AbstractCommonSISV;
import org.springframework.beans.factory.InitializingBean;

public class AD_RoleSISVImpl extends AbstractCommonSISV implements IAD_RoleSISV, InitializingBean {

    private IAD_RoleDAO adroleDAOImpl;

    private static Logger logger = Logger.getLogger(AD_RoleSISVImpl.class);

    public AD_RoleSISVImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getDaoController() == null) System.out.println("finder null");
            this.adroleDAOImpl = (IAD_RoleDAO) this.getDaoController().get("DAO/AD_Role");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createAD_Role(Properties ctx, AD_RoleDTO aD_RoleDTO, String trxname) throws KTAdempiereAppException {
        try {
            X_AD_Role model = new X_AD_Role(ctx, 0, trxname);
            AD_RoleCriteria criteria = new AD_RoleCriteria();
            criteria.setName(aD_RoleDTO.getName());
            if (adroleDAOImpl.isDuplicate(criteria)) {
                throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ROLE_SISV_000");
            } else {
                System.out.println("NO duplicates names");
                model.setAD_Tree_Menu_ID(aD_RoleDTO.getAd_Tree_Menu_ID());
                model.setAD_Tree_Org_ID(aD_RoleDTO.getAd_Tree_Org_ID());
                model.setAmtApproval(aD_RoleDTO.getAmtApproval());
                model.setC_Currency_ID(aD_RoleDTO.getC_Currency_ID());
                model.setConfirmQueryRecords(aD_RoleDTO.getConfirmQueryRecords());
                model.setConnectionProfile(aD_RoleDTO.getConnectionProfile());
                model.setDescription(aD_RoleDTO.getDescription());
                model.setMaxQueryRecords(aD_RoleDTO.getMaxQueryRecords());
                model.setName(aD_RoleDTO.getName());
                model.setPreferenceType(aD_RoleDTO.getPreferenceType());
                model.setSupervisor_ID(aD_RoleDTO.getSupervisor_ID());
                model.setUserDiscount(aD_RoleDTO.getUserDiscount());
                model.setUserLevel(aD_RoleDTO.getUserLevel());
                model.setIsAccessAllOrgs(aD_RoleDTO.getIsAccessAllOrgs() == "Y" ? true : false);
                model.setAllow_Info_Account(aD_RoleDTO.getAllow_Info_Account() == "Y" ? true : false);
                model.setAllow_Info_Asset(aD_RoleDTO.getAllow_Info_Asset() == "Y" ? true : false);
                model.setAllow_Info_BPartner(aD_RoleDTO.getAllow_Info_BPartner() == "Y" ? true : false);
                model.setAllow_Info_CashJournal(aD_RoleDTO.getAllow_Info_CashJournal() == "Y" ? true : false);
                model.setAllow_Info_InOut(aD_RoleDTO.getAllow_Info_InOut() == "Y" ? true : false);
                model.setAllow_Info_Invoice(aD_RoleDTO.getAllow_Info_Invoice() == "Y" ? true : false);
                model.setAllow_Info_Order(aD_RoleDTO.getAllow_Info_Order() == "Y" ? true : false);
                model.setAllow_Info_Payment(aD_RoleDTO.getAllow_Info_Payment() == "Y" ? true : false);
                model.setAllow_Info_Product(aD_RoleDTO.getAllow_Info_Product() == "Y" ? true : false);
                model.setAllow_Info_Resource(aD_RoleDTO.getAllow_Info_Resource() == "Y" ? true : false);
                model.setAllow_Info_Schedule(aD_RoleDTO.getAllow_Info_Schedule() == "Y" ? true : false);
                model.setIsCanApproveOwnDoc(aD_RoleDTO.getIsCanApproveOwnDoc() == "Y" ? true : false);
                model.setIsCanExport(aD_RoleDTO.getIsCanExport() == "Y" ? true : false);
                model.setIsCanReport(aD_RoleDTO.getIsCanReport() == "Y" ? true : false);
                model.setIsChangeLog(aD_RoleDTO.getIsChangeLog() == "Y" ? true : false);
                model.setIsManual(aD_RoleDTO.getIsManual() == "Y" ? true : false);
                model.setOverwritePriceLimit(aD_RoleDTO.getOverwritePriceLimit() == "Y" ? true : false);
                model.setIsPersonalAccess(aD_RoleDTO.getIsPersonalAccess() == "Y" ? true : false);
                model.setIsPersonalLock(aD_RoleDTO.getIsPersonalLock() == "Y" ? true : false);
                model.setIsShowAcct(aD_RoleDTO.getIsShowAcct() == "Y" ? true : false);
                model.setIsUseUserOrgAccess(aD_RoleDTO.getIsUseUserOrgAccess() == "Y" ? true : false);
                model.setIsActive(aD_RoleDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                aD_RoleDTO.setAd_Role_ID(model.get_ID());
                return model.get_ID();
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ROLE_SISV_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public AD_RoleDTO getAD_Role(Properties ctx, int ad_Role_ID, String trxname) throws KTAdempiereAppException {
        try {
            if (ad_Role_ID == 0) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ROLE_SISV_002");
            X_AD_Role mrole = new X_AD_Role(ctx, ad_Role_ID, trxname);
            return convertModelToDTO(mrole);
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ROLE_SISV_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<AD_RoleDTO> findAD_Role(Properties ctx, AD_RoleCriteria aD_RoleCriteria) throws KTAdempiereAppException {
        try {
            String whereclause = getWhereClause(ctx, aD_RoleCriteria);
            int id[] = X_AD_Role.getAllIDs("AD_Role", whereclause, null);
            ArrayList<AD_RoleDTO> list = new ArrayList<AD_RoleDTO>();
            AD_RoleDTO dto;
            for (int i = 0; i < id.length; i++) {
                dto = id[i] == 0 ? null : this.getAD_Role(ctx, id[i], null);
                if (dto != null) list.add(dto);
            }
            return list;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ROLE_SISV_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateAD_Role(Properties ctx, AD_RoleDTO aD_RoleDTO) throws KTAdempiereAppException {
        try {
            X_AD_Role model = new X_AD_Role(ctx, aD_RoleDTO.getAd_Role_ID(), null);
            if (model != null) {
                String oldname = model.getName() + "";
                String newname = aD_RoleDTO.getName() + "";
                if (oldname.compareToIgnoreCase(newname) != 0) {
                    AD_RoleCriteria criteria = new AD_RoleCriteria();
                    criteria.setName(newname);
                    if (adroleDAOImpl.isDuplicate(criteria)) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ROLE_SISV_000");
                }
                System.out.println("NO duplicates names for modification");
                model.setAD_Tree_Menu_ID(aD_RoleDTO.getAd_Tree_Menu_ID());
                model.setAD_Tree_Org_ID(aD_RoleDTO.getAd_Tree_Org_ID());
                model.setAmtApproval(aD_RoleDTO.getAmtApproval());
                model.setC_Currency_ID(aD_RoleDTO.getC_Currency_ID());
                model.setConfirmQueryRecords(aD_RoleDTO.getConfirmQueryRecords());
                model.setConnectionProfile(aD_RoleDTO.getConnectionProfile());
                model.setDescription(aD_RoleDTO.getDescription());
                model.setMaxQueryRecords(aD_RoleDTO.getMaxQueryRecords());
                model.setName(aD_RoleDTO.getName());
                model.setPreferenceType(aD_RoleDTO.getPreferenceType());
                model.setSupervisor_ID(aD_RoleDTO.getSupervisor_ID());
                model.setUserDiscount(aD_RoleDTO.getUserDiscount());
                model.setUserLevel(aD_RoleDTO.getUserLevel());
                model.setIsAccessAllOrgs(aD_RoleDTO.getIsAccessAllOrgs() == "Y" ? true : false);
                model.setAllow_Info_Account(aD_RoleDTO.getAllow_Info_Account() == "Y" ? true : false);
                model.setAllow_Info_Asset(aD_RoleDTO.getAllow_Info_Asset() == "Y" ? true : false);
                model.setAllow_Info_BPartner(aD_RoleDTO.getAllow_Info_BPartner() == "Y" ? true : false);
                model.setAllow_Info_CashJournal(aD_RoleDTO.getAllow_Info_CashJournal() == "Y" ? true : false);
                model.setAllow_Info_InOut(aD_RoleDTO.getAllow_Info_InOut() == "Y" ? true : false);
                model.setAllow_Info_Invoice(aD_RoleDTO.getAllow_Info_Invoice() == "Y" ? true : false);
                model.setAllow_Info_Order(aD_RoleDTO.getAllow_Info_Order() == "Y" ? true : false);
                model.setAllow_Info_Payment(aD_RoleDTO.getAllow_Info_Payment() == "Y" ? true : false);
                model.setAllow_Info_Product(aD_RoleDTO.getAllow_Info_Product() == "Y" ? true : false);
                model.setAllow_Info_Resource(aD_RoleDTO.getAllow_Info_Resource() == "Y" ? true : false);
                model.setAllow_Info_Schedule(aD_RoleDTO.getAllow_Info_Schedule() == "Y" ? true : false);
                model.setIsCanApproveOwnDoc(aD_RoleDTO.getIsCanApproveOwnDoc() == "Y" ? true : false);
                model.setIsCanExport(aD_RoleDTO.getIsCanExport() == "Y" ? true : false);
                model.setIsCanReport(aD_RoleDTO.getIsCanReport() == "Y" ? true : false);
                model.setIsChangeLog(aD_RoleDTO.getIsChangeLog() == "Y" ? true : false);
                model.setIsManual(aD_RoleDTO.getIsManual() == "Y" ? true : false);
                model.setOverwritePriceLimit(aD_RoleDTO.getOverwritePriceLimit() == "Y" ? true : false);
                model.setIsPersonalAccess(aD_RoleDTO.getIsPersonalAccess() == "Y" ? true : false);
                model.setIsPersonalLock(aD_RoleDTO.getIsPersonalLock() == "Y" ? true : false);
                model.setIsShowAcct(aD_RoleDTO.getIsShowAcct() == "Y" ? true : false);
                model.setIsUseUserOrgAccess(aD_RoleDTO.getIsUseUserOrgAccess() == "Y" ? true : false);
                model.setIsActive(aD_RoleDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                aD_RoleDTO.setAd_Role_ID(model.get_ID());
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ROLE_SISV_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public boolean deleteAD_Role(Properties ctx, AD_RoleCriteria aD_RoleCriteria) throws KTAdempiereAppException {
        try {
            int id = aD_RoleCriteria.getAd_Role_ID();
            X_AD_Role model = new X_AD_Role(ctx, id, null);
            return model.delete(true);
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ROLE_SISV_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    protected String getWhereClause(Properties ctx, AD_RoleCriteria criteria) {
        StringBuffer temp = new StringBuffer(super.getWhereClause(ctx, criteria));
        if (criteria.getAd_Role_ID() > 0) temp.append(" AND (AD_ROLE_ID=" + criteria.getAd_Role_ID() + ")");
        if (criteria.getAd_Tree_Menu_ID() > 0) temp.append(" AND (AD_TREE_MENU_ID=" + criteria.getAd_Tree_Menu_ID() + ")");
        if (criteria.getAd_Tree_Org_ID() > 0) temp.append(" AND (AD_TREE_ORG_ID=" + criteria.getAd_Tree_Org_ID() + ")");
        if (criteria.getAmtApproval() != null) temp.append(" AND (AMTAPPROVAL=" + criteria.getAmtApproval() + ")");
        if (criteria.getC_Currency_ID() > 0) temp.append(" AND (C_CURRENCY_ID=" + criteria.getC_Currency_ID() + ")");
        if (criteria.getConfirmQueryRecords() > 0) temp.append(" AND (CONFIRMQUERYRECORDS=" + criteria.getConfirmQueryRecords() + ")");
        if (criteria.getConnectionProfile() != null) temp.append(" AND ( CONNECTIONPROFILE LIKE '%" + criteria.getConnectionProfile() + "%')");
        if (criteria.getDescription() != null) temp.append(" AND ( DESCRIPTION LIKE '%" + criteria.getDescription() + "%')");
        if (criteria.getMaxQueryRecords() > 0) temp.append(" AND (MAXQUERYRECORDS=" + criteria.getMaxQueryRecords() + ")");
        if (criteria.getName() != null) temp.append(" AND ( NAME LIKE '%" + criteria.getName() + "%')");
        if (criteria.getPreferenceType() != null) temp.append(" AND ( PREFERENCETYPE LIKE '%" + criteria.getPreferenceType() + "%')");
        if (criteria.getSupervisor_ID() > 0) temp.append(" AND (SUPERVISOR_ID=" + criteria.getSupervisor_ID() + ")");
        if (criteria.getUserDiscount() != null) temp.append(" AND (USERDISCOUNT=" + criteria.getUserDiscount() + ")");
        if (criteria.getUserLevel() != null) temp.append(" AND ( USERLEVEL LIKE '%" + criteria.getUserLevel() + "%')");
        if (criteria.getIsAccessAllOrgs() != null) temp.append(" AND (ISACCESSALLORGS='" + criteria.getIsAccessAllOrgs() + "')");
        if (criteria.getAllow_Info_Account() != null) temp.append(" AND (ALLOW_INFO_ACCOUNT='" + criteria.getAllow_Info_Account() + "')");
        if (criteria.getAllow_Info_Asset() != null) temp.append(" AND (ALLOW_INFO_ASSET='" + criteria.getAllow_Info_Asset() + "')");
        if (criteria.getAllow_Info_BPartner() != null) temp.append(" AND (ALLOW_INFO_BPARTNER='" + criteria.getAllow_Info_BPartner() + "')");
        if (criteria.getAllow_Info_CashJournal() != null) temp.append(" AND (ALLOW_INFO_CASHJOURNAL='" + criteria.getAllow_Info_CashJournal() + "')");
        if (criteria.getAllow_Info_InOut() != null) temp.append(" AND (ALLOW_INFO_INOUT='" + criteria.getAllow_Info_InOut() + "')");
        if (criteria.getAllow_Info_Invoice() != null) temp.append(" AND (ALLOW_INFO_INVOICE='" + criteria.getAllow_Info_Invoice() + "')");
        if (criteria.getAllow_Info_Order() != null) temp.append(" AND (ALLOW_INFO_ORDER='" + criteria.getAllow_Info_Order() + "')");
        if (criteria.getAllow_Info_Payment() != null) temp.append(" AND (ALLOW_INFO_PAYMENT='" + criteria.getAllow_Info_Payment() + "')");
        if (criteria.getAllow_Info_Product() != null) temp.append(" AND (ALLOW_INFO_PRODUCT='" + criteria.getAllow_Info_Product() + "')");
        if (criteria.getAllow_Info_Resource() != null) temp.append(" AND (ALLOW_INFO_RESOURCE='" + criteria.getAllow_Info_Resource() + "')");
        if (criteria.getAllow_Info_Schedule() != null) temp.append(" AND (ALLOW_INFO_SCHEDULE='" + criteria.getAllow_Info_Schedule() + "')");
        if (criteria.getIsCanApproveOwnDoc() != null) temp.append(" AND (ISCANAPPROVEOWNDOC='" + criteria.getIsCanApproveOwnDoc() + "')");
        if (criteria.getIsCanExport() != null) temp.append(" AND (ISCANEXPORT='" + criteria.getIsCanExport() + "')");
        if (criteria.getIsCanReport() != null) temp.append(" AND (ISCANREPORT='" + criteria.getIsCanReport() + "')");
        if (criteria.getIsChangeLog() != null) temp.append(" AND (ISCHANGELOG='" + criteria.getIsChangeLog() + "')");
        if (criteria.getIsManual() != null) temp.append(" AND (ISMANUAL='" + criteria.getIsManual() + "')");
        if (criteria.getOverwritePriceLimit() != null) temp.append(" AND (OVERWRITEPRICELIMIT='" + criteria.getOverwritePriceLimit() + "')");
        if (criteria.getIsPersonalAccess() != null) temp.append(" AND (ISPERSONALACCESS='" + criteria.getIsPersonalAccess() + "')");
        if (criteria.getIsPersonalLock() != null) temp.append(" AND (ISPERSONALLOCK='" + criteria.getIsPersonalLock() + "')");
        if (criteria.getIsShowAcct() != null) temp.append(" AND (ISSHOWACCT='" + criteria.getIsShowAcct() + "')");
        if (criteria.getIsUseUserOrgAccess() != null) temp.append(" AND (ISUSEUSERORGACCESS='" + criteria.getIsUseUserOrgAccess() + "')");
        if (criteria.getIsActive() != null) temp.append(" AND (ISACTIVE='" + criteria.getIsActive() + "')");
        return temp.toString();
    }

    protected AD_RoleDTO convertModelToDTO(X_AD_Role model) {
        AD_RoleDTO dto = null;
        Object obj;
        if (model != null) {
            dto = new AD_RoleDTO();
            if ((obj = model.get_Value("AD_ROLE_ID")) != null) dto.setAd_Role_ID((Integer) obj);
            if ((obj = model.get_Value("AD_TREE_MENU_ID")) != null) dto.setAd_Tree_Menu_ID((Integer) obj);
            if ((obj = model.get_Value("AD_TREE_ORG_ID")) != null) dto.setAd_Tree_Org_ID((Integer) obj);
            if ((obj = model.get_Value("AMTAPPROVAL")) != null) dto.setAmtApproval((BigDecimal) obj);
            if ((obj = model.get_Value("C_CURRENCY_ID")) != null) dto.setC_Currency_ID((Integer) obj);
            if ((obj = model.get_Value("CONFIRMQUERYRECORDS")) != null) dto.setConfirmQueryRecords((Integer) obj);
            if ((obj = model.get_Value("CONNECTIONPROFILE")) != null) dto.setConnectionProfile((String) obj);
            if ((obj = model.get_Value("DESCRIPTION")) != null) dto.setDescription((String) obj);
            if ((obj = model.get_Value("MAXQUERYRECORDS")) != null) dto.setMaxQueryRecords((Integer) obj);
            if ((obj = model.get_Value("NAME")) != null) dto.setName((String) obj);
            if ((obj = model.get_Value("PREFERENCETYPE")) != null) dto.setPreferenceType((String) obj);
            if ((obj = model.get_Value("SUPERVISOR_ID")) != null) dto.setSupervisor_ID((Integer) obj);
            if ((obj = model.get_Value("USERDISCOUNT")) != null) dto.setUserDiscount((BigDecimal) obj);
            if ((obj = model.get_Value("USERLEVEL")) != null) dto.setUserLevel((String) obj);
            if ((obj = model.get_Value("ISACCESSALLORGS")) != null) dto.setIsAccessAllOrgs(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ALLOW_INFO_ACCOUNT")) != null) dto.setAllow_Info_Account(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ALLOW_INFO_ASSET")) != null) dto.setAllow_Info_Asset(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ALLOW_INFO_BPARTNER")) != null) dto.setAllow_Info_BPartner(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ALLOW_INFO_CASHJOURNAL")) != null) dto.setAllow_Info_CashJournal(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ALLOW_INFO_INOUT")) != null) dto.setAllow_Info_InOut(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ALLOW_INFO_INVOICE")) != null) dto.setAllow_Info_Invoice(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ALLOW_INFO_ORDER")) != null) dto.setAllow_Info_Order(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ALLOW_INFO_PAYMENT")) != null) dto.setAllow_Info_Payment(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ALLOW_INFO_PRODUCT")) != null) dto.setAllow_Info_Product(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ALLOW_INFO_RESOURCE")) != null) dto.setAllow_Info_Resource(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ALLOW_INFO_SCHEDULE")) != null) dto.setAllow_Info_Schedule(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ISCANAPPROVEOWNDOC")) != null) dto.setIsCanApproveOwnDoc(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ISCANEXPORT")) != null) dto.setIsCanExport(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ISCANREPORT")) != null) dto.setIsCanReport(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ISCHANGELOG")) != null) dto.setIsChangeLog(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ISMANUAL")) != null) dto.setIsManual(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("OVERWRITEPRICELIMIT")) != null) dto.setOverwritePriceLimit(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ISPERSONALACCESS")) != null) dto.setIsPersonalAccess(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ISPERSONALLOCK")) != null) dto.setIsPersonalLock(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ISSHOWACCT")) != null) dto.setIsShowAcct(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ISUSEUSERORGACCESS")) != null) dto.setIsUseUserOrgAccess(((Boolean) obj) == true ? "Y" : "N");
            dto.setIsActive(((Boolean) model.get_Value("ISACTIVE")) == true ? "Y" : "N");
            dto.setAd_Client_ID((Integer) model.get_Value("AD_CLIENT_ID"));
            dto.setAd_Org_ID((Integer) model.get_Value("AD_Org_ID"));
        }
        return dto;
    }
}
