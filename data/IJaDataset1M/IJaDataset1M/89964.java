package org.koossery.adempiere.sisv.impl.user;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.compiere.model.MUser;
import org.compiere.model.MUserOrgAccess;
import org.compiere.model.X_AD_User;
import org.compiere.model.X_AD_User_OrgAccess;
import org.compiere.util.Trx;
import org.koossery.adempiere.core.backend.interfaces.dao.user.IAD_User_OrgAccessDAO;
import org.koossery.adempiere.core.backend.interfaces.sisv.user.IAD_User_OrgAccessSISV;
import org.koossery.adempiere.core.contract.criteria.user.AD_User_OrgAccessCriteria;
import org.koossery.adempiere.core.contract.dto.user.AD_User_OrgAccessDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;
import org.koossery.adempiere.sisv.impl.AbstractCommonSISV;
import org.springframework.beans.factory.InitializingBean;

public class AD_User_OrgAccessSISVImpl extends AbstractCommonSISV implements IAD_User_OrgAccessSISV, InitializingBean {

    private IAD_User_OrgAccessDAO aduserorgaccessDAOImpl;

    private static Logger logger = Logger.getLogger(AD_User_OrgAccessSISVImpl.class);

    public AD_User_OrgAccessSISVImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getDaoController() == null) System.out.println("finder null");
            this.aduserorgaccessDAOImpl = (IAD_User_OrgAccessDAO) this.getDaoController().get("DAO/AD_User_OrgAccess");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createAD_User_OrgAccess(Properties ctx, AD_User_OrgAccessDTO aD_User_OrgAccessDTO, String trxname) throws KTAdempiereAppException {
        try {
            X_AD_User_OrgAccess model = new X_AD_User_OrgAccess(ctx, 0, trxname);
            AD_User_OrgAccessCriteria criteria = new AD_User_OrgAccessCriteria();
            criteria.setAd_User_ID(aD_User_OrgAccessDTO.getAd_User_ID());
            criteria.setAd_Org_ID(aD_User_OrgAccessDTO.getAd_Org_ID());
            if (aduserorgaccessDAOImpl.isDuplicate(criteria)) {
                throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_USER_ORGACCESS_SISV_000");
            } else {
                System.out.println("NO duplicates names");
                model.setAD_User_ID(aD_User_OrgAccessDTO.getAd_User_ID());
                model.setAD_Org_ID(aD_User_OrgAccessDTO.getAd_Org_ID());
                model.setIsReadOnly(aD_User_OrgAccessDTO.getIsReadOnly() == "Y" ? true : false);
                model.setIsActive(aD_User_OrgAccessDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                return model.get_ID();
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_USER_ORGACCESS_SISV_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public AD_User_OrgAccessDTO getAD_User_OrgAccess(Properties ctx, int ad_User_ID, int ad_org_id, String trxname) throws KTAdempiereAppException {
        try {
            AD_User_OrgAccessCriteria criteria = new AD_User_OrgAccessCriteria();
            criteria.setAd_User_ID(ad_User_ID);
            criteria.setAd_Org_ID(ad_org_id);
            ArrayList<AD_User_OrgAccessDTO> list = new ArrayList<AD_User_OrgAccessDTO>();
            list = aduserorgaccessDAOImpl.getAD_User_OrgAccess(criteria);
            return ((list != null) && (list.size() > 0)) ? list.get(0) : null;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_USER_ORGACCESS_SISV_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<AD_User_OrgAccessDTO> findAD_User_OrgAccess(Properties ctx, AD_User_OrgAccessCriteria aD_User_OrgAccessCriteria) throws KTAdempiereAppException {
        try {
            ArrayList<AD_User_OrgAccessDTO> list = new ArrayList<AD_User_OrgAccessDTO>();
            list = aduserorgaccessDAOImpl.getAD_User_OrgAccess(aD_User_OrgAccessCriteria);
            return list;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_USER_ORGACCESS_SISV_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateAD_User_OrgAccess(Properties ctx, AD_User_OrgAccessDTO aD_User_OrgAccessDTO) throws KTAdempiereAppException {
        try {
            int userID = aD_User_OrgAccessDTO.getAd_User_ID();
            int orgID = aD_User_OrgAccessDTO.getAd_Org_ID();
            String trxName = "update-AD_User_OrgAccess";
            Trx trx = Trx.get(trxName, true);
            MUserOrgAccess uoa = new MUserOrgAccess(ctx, 0, trxName);
            MUserOrgAccess[] MuserOrg = uoa.getOfUser(ctx, userID);
            for (int index = 0; index < MuserOrg.length; index++) {
                if (MuserOrg[index].getAD_Org_ID() != orgID) {
                    MUserOrgAccess model = MuserOrg[index];
                    model.setIsReadOnly(aD_User_OrgAccessDTO.getIsReadOnly() == "Y" ? true : false);
                    model.setIsActive(aD_User_OrgAccessDTO.getIsActive() == "Y" ? true : false);
                    model.save(trxName);
                    trx.commit();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_USER_ORGACCESS_SISV_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public boolean deleteAD_User_OrgAccess(Properties ctx, AD_User_OrgAccessCriteria aD_User_OrgAccessCriteria) throws KTAdempiereAppException {
        try {
            X_AD_User_OrgAccess model = new X_AD_User_OrgAccess(ctx, 0, null);
            model.setAD_User_ID(aD_User_OrgAccessCriteria.getAd_User_ID());
            model.setAD_Org_ID(aD_User_OrgAccessCriteria.getAd_Org_ID());
            return model.delete(true);
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_USER_ORGACCESS_SISV_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    protected String getWhereClause(Properties ctx, AD_User_OrgAccessCriteria criteria) {
        StringBuffer temp = new StringBuffer(super.getWhereClause(ctx, criteria));
        if (criteria.getAd_User_ID() > 0) temp.append(" AND (AD_USER_ID=" + criteria.getAd_User_ID() + ")");
        if (criteria.getAd_Org_ID() > 0) temp.append(" AND (AD_ORG_ID=" + criteria.getAd_Org_ID() + ")");
        if (criteria.getIsReadOnly() != null) temp.append(" AND (ISREADONLY='" + criteria.getIsReadOnly() + "')");
        if (criteria.getIsActive() != null) temp.append(" AND (ISACTIVE='" + criteria.getIsActive() + "')");
        return temp.toString();
    }

    protected AD_User_OrgAccessDTO convertModelToDTO(X_AD_User_OrgAccess model) {
        AD_User_OrgAccessDTO dto = null;
        Object obj;
        if (model != null) {
            dto = new AD_User_OrgAccessDTO();
            if ((obj = model.get_Value("AD_USER_ID")) != null) dto.setAd_User_ID((Integer) obj);
            if ((obj = model.get_Value("ISREADONLY")) != null) dto.setIsReadOnly(((Boolean) obj) == true ? "Y" : "N");
            dto.setIsActive(((Boolean) model.get_Value("ISACTIVE")) == true ? "Y" : "N");
            dto.setAd_Client_ID((Integer) model.get_Value("AD_CLIENT_ID"));
            dto.setAd_Org_ID((Integer) model.get_Value("AD_Org_ID"));
        }
        return dto;
    }
}
