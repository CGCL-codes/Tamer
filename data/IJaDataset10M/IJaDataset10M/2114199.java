package org.koossery.adempiere.sisv.impl.user;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.compiere.model.X_AD_User;
import org.koossery.adempiere.core.backend.interfaces.dao.user.IAD_UserDAO;
import org.koossery.adempiere.core.backend.interfaces.sisv.user.IAD_UserSISV;
import org.koossery.adempiere.core.contract.criteria.user.AD_UserCriteria;
import org.koossery.adempiere.core.contract.dto.user.AD_UserDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;
import org.koossery.adempiere.sisv.impl.AbstractCommonSISV;
import org.springframework.beans.factory.InitializingBean;

public class AD_UserSISVImpl extends AbstractCommonSISV implements IAD_UserSISV, InitializingBean {

    private IAD_UserDAO aduserDAOImpl;

    private static Logger logger = Logger.getLogger(AD_UserSISVImpl.class);

    public AD_UserSISVImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getDaoController() == null) System.out.println("finder null");
            this.aduserDAOImpl = (IAD_UserDAO) this.getDaoController().get("DAO/AD_User");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createAD_User(Properties ctx, AD_UserDTO aD_UserDTO, String trxname) throws KTAdempiereAppException {
        try {
            X_AD_User model = new X_AD_User(ctx, 0, trxname);
            AD_UserCriteria criteria = new AD_UserCriteria();
            criteria.setName(aD_UserDTO.getName());
            if (aduserDAOImpl.isDuplicate(criteria)) {
                throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_USER_SISV_000");
            } else {
                System.out.println("NO duplicates names");
                model.setAD_OrgTrx_ID(aD_UserDTO.getAd_OrgTrx_ID());
                model.setBirthday(aD_UserDTO.getBirthday());
                model.setC_BPartner_ID(aD_UserDTO.getC_BPartner_ID());
                model.setC_BPartner_Location_ID(aD_UserDTO.getC_BPartner_Location_ID());
                model.setC_Greeting_ID(aD_UserDTO.getC_Greeting_ID());
                model.setC_Job_ID(aD_UserDTO.getC_Job_ID());
                model.setComments(aD_UserDTO.getComments());
                model.setConnectionProfile(aD_UserDTO.getConnectionProfile());
                model.setDescription(aD_UserDTO.getDescription());
                model.setEMail(aD_UserDTO.getEmail());
                model.setEMailUser(aD_UserDTO.getEmailUser());
                model.setEMailUserPW(aD_UserDTO.getEmailUserPW());
                model.setEMailVerify(aD_UserDTO.getEmailVerify());
                model.setEMailVerifyDate(aD_UserDTO.getEmailVerifyDate());
                model.setFax(aD_UserDTO.getFax());
                model.setLastContact(aD_UserDTO.getLastContact());
                model.setLastResult(aD_UserDTO.getLastResult());
                model.setLDAPUser(aD_UserDTO.getLdAPUser());
                model.setName(aD_UserDTO.getName());
                model.setNotificationType(aD_UserDTO.getNotificationType());
                model.setPassword(aD_UserDTO.getPassword());
                model.setPhone(aD_UserDTO.getPhone());
                model.setPhone2(aD_UserDTO.getPhone2());
                model.setSupervisor_ID(aD_UserDTO.getSupervisor_ID());
                model.setTitle(aD_UserDTO.getTitle());
                model.setUserPIN(aD_UserDTO.getUserPIN());
                model.setValue(aD_UserDTO.getValue());
                model.setIsFullBPAccess(aD_UserDTO.getIsFullBPAccess() == "Y" ? true : false);
                model.setIsActive(aD_UserDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                aD_UserDTO.setAd_User_ID(model.get_ID());
                return model.get_ID();
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_USER_SISV_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public AD_UserDTO getAD_User(Properties ctx, int ad_User_ID, String trxname) throws KTAdempiereAppException {
        try {
            if (ad_User_ID == -1) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_USER_SISV_002");
            X_AD_User muser = new X_AD_User(ctx, ad_User_ID, trxname);
            return convertModelToDTO(muser);
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_USER_SISV_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<AD_UserDTO> findAD_User(Properties ctx, AD_UserCriteria aD_UserCriteria) throws KTAdempiereAppException {
        try {
            String whereclause = getWhereClause(ctx, aD_UserCriteria);
            int id[] = X_AD_User.getAllIDs("AD_User", whereclause, null);
            ArrayList<AD_UserDTO> list = new ArrayList<AD_UserDTO>();
            AD_UserDTO dto;
            for (int i = 0; i < id.length; i++) {
                dto = this.getAD_User(ctx, id[i], null);
                if (dto != null) list.add(dto);
            }
            return list;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_USER_SISV_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateAD_User(Properties ctx, AD_UserDTO aD_UserDTO) throws KTAdempiereAppException {
        try {
            X_AD_User model = new X_AD_User(ctx, aD_UserDTO.getAd_User_ID(), null);
            if (model != null) {
                String oldname = model.getName() + "";
                String newname = aD_UserDTO.getName() + "";
                if (oldname.compareToIgnoreCase(newname) != 0) {
                    AD_UserCriteria criteria = new AD_UserCriteria();
                    criteria.setName(newname);
                    if (aduserDAOImpl.isDuplicate(criteria)) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_USER_SISV_000");
                }
                System.out.println("NO duplicates names for modification");
                model.setAD_OrgTrx_ID(aD_UserDTO.getAd_OrgTrx_ID());
                model.setBirthday(aD_UserDTO.getBirthday());
                model.setC_BPartner_ID(aD_UserDTO.getC_BPartner_ID());
                model.setC_BPartner_Location_ID(aD_UserDTO.getC_BPartner_Location_ID());
                model.setC_Greeting_ID(aD_UserDTO.getC_Greeting_ID());
                model.setC_Job_ID(aD_UserDTO.getC_Job_ID());
                model.setComments(aD_UserDTO.getComments());
                model.setConnectionProfile(aD_UserDTO.getConnectionProfile());
                model.setDescription(aD_UserDTO.getDescription());
                model.setEMail(aD_UserDTO.getEmail());
                model.setEMailUser(aD_UserDTO.getEmailUser());
                model.setEMailUserPW(aD_UserDTO.getEmailUserPW());
                model.setEMailVerify(aD_UserDTO.getEmailVerify());
                model.setEMailVerifyDate(aD_UserDTO.getEmailVerifyDate());
                model.setFax(aD_UserDTO.getFax());
                model.setLastContact(aD_UserDTO.getLastContact());
                model.setLastResult(aD_UserDTO.getLastResult());
                model.setLDAPUser(aD_UserDTO.getLdAPUser());
                model.setName(aD_UserDTO.getName());
                model.setNotificationType(aD_UserDTO.getNotificationType());
                model.setPassword(aD_UserDTO.getPassword());
                model.setPhone(aD_UserDTO.getPhone());
                model.setPhone2(aD_UserDTO.getPhone2());
                model.setSupervisor_ID(aD_UserDTO.getSupervisor_ID());
                model.setTitle(aD_UserDTO.getTitle());
                model.setUserPIN(aD_UserDTO.getUserPIN());
                model.setValue(aD_UserDTO.getValue());
                model.setIsFullBPAccess(aD_UserDTO.getIsFullBPAccess() == "Y" ? true : false);
                model.setIsActive(aD_UserDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                aD_UserDTO.setAd_User_ID(model.get_ID());
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_USER_SISV_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public boolean deleteAD_User(Properties ctx, AD_UserCriteria aD_UserCriteria) throws KTAdempiereAppException {
        try {
            int id = aD_UserCriteria.getAd_User_ID();
            X_AD_User model = new X_AD_User(ctx, id, null);
            return model.delete(true);
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_USER_SISV_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    protected String getWhereClause(Properties ctx, AD_UserCriteria criteria) {
        StringBuffer temp = new StringBuffer(super.getWhereClause(ctx, criteria));
        if (criteria.getAd_OrgTrx_ID() > 0) temp.append(" AND (AD_ORGTRX_ID=" + criteria.getAd_OrgTrx_ID() + ")");
        if (criteria.getAd_User_ID() > 0) temp.append(" AND (AD_USER_ID=" + criteria.getAd_User_ID() + ")");
        if (criteria.getBirthday() != null) temp.append(" AND (BIRTHDAY=" + criteria.getBirthday() + ")");
        if (criteria.getC_BPartner_ID() > 0) temp.append(" AND (C_BPARTNER_ID=" + criteria.getC_BPartner_ID() + ")");
        if (criteria.getC_BPartner_Location_ID() > 0) temp.append(" AND (C_BPARTNER_LOCATION_ID=" + criteria.getC_BPartner_Location_ID() + ")");
        if (criteria.getC_Greeting_ID() > 0) temp.append(" AND (C_GREETING_ID=" + criteria.getC_Greeting_ID() + ")");
        if (criteria.getC_Job_ID() > 0) temp.append(" AND (C_JOB_ID=" + criteria.getC_Job_ID() + ")");
        if (criteria.getComments() != null) temp.append(" AND ( COMMENTS LIKE '%" + criteria.getComments() + "%')");
        if (criteria.getConnectionProfile() != null) temp.append(" AND ( CONNECTIONPROFILE LIKE '%" + criteria.getConnectionProfile() + "%')");
        if (criteria.getDescription() != null) temp.append(" AND ( DESCRIPTION LIKE '%" + criteria.getDescription() + "%')");
        if (criteria.getEmail() != null) temp.append(" AND ( EMAIL LIKE '%" + criteria.getEmail() + "%')");
        if (criteria.getEmailUser() != null) temp.append(" AND ( EMAILUSER LIKE '%" + criteria.getEmailUser() + "%')");
        if (criteria.getEmailUserPW() != null) temp.append(" AND ( EMAILUSERPW LIKE '%" + criteria.getEmailUserPW() + "%')");
        if (criteria.getEmailVerify() != null) temp.append(" AND ( EMAILVERIFY LIKE '%" + criteria.getEmailVerify() + "%')");
        if (criteria.getEmailVerifyDate() != null) temp.append(" AND (EMAILVERIFYDATE=" + criteria.getEmailVerifyDate() + ")");
        if (criteria.getFax() != null) temp.append(" AND ( FAX LIKE '%" + criteria.getFax() + "%')");
        if (criteria.getLastContact() != null) temp.append(" AND (LASTCONTACT=" + criteria.getLastContact() + ")");
        if (criteria.getLastResult() != null) temp.append(" AND ( LASTRESULT LIKE '%" + criteria.getLastResult() + "%')");
        if (criteria.getLdAPUser() != null) temp.append(" AND ( LDAPUSER LIKE '%" + criteria.getLdAPUser() + "%')");
        if (criteria.getName() != null) temp.append(" AND ( NAME LIKE '%" + criteria.getName() + "%')");
        if (criteria.getNotificationType() != null) temp.append(" AND ( NOTIFICATIONTYPE LIKE '%" + criteria.getNotificationType() + "%')");
        if (criteria.getPassword() != null) temp.append(" AND ( PASSWORD LIKE '%" + criteria.getPassword() + "%')");
        if (criteria.getPhone() != null) temp.append(" AND ( PHONE LIKE '%" + criteria.getPhone() + "%')");
        if (criteria.getPhone2() != null) temp.append(" AND ( PHONE2 LIKE '%" + criteria.getPhone2() + "%')");
        if (criteria.getSupervisor_ID() > 0) temp.append(" AND (SUPERVISOR_ID=" + criteria.getSupervisor_ID() + ")");
        if (criteria.getTitle() != null) temp.append(" AND ( TITLE LIKE '%" + criteria.getTitle() + "%')");
        if (criteria.getUserPIN() != null) temp.append(" AND ( USERPIN LIKE '%" + criteria.getUserPIN() + "%')");
        if (criteria.getValue() != null) temp.append(" AND ( VALUE LIKE '%" + criteria.getValue() + "%')");
        if (criteria.getIsFullBPAccess() != null) temp.append(" AND (ISFULLBPACCESS='" + criteria.getIsFullBPAccess() + "')");
        if (criteria.getIsProcessing() != null) temp.append(" AND (PROCESSING='" + criteria.getIsProcessing() + "')");
        if (criteria.getIsActive() != null) temp.append(" AND (ISACTIVE='" + criteria.getIsActive() + "')");
        return temp.toString();
    }

    protected AD_UserDTO convertModelToDTO(X_AD_User model) {
        AD_UserDTO dto = null;
        Object obj;
        if (model != null) {
            dto = new AD_UserDTO();
            if ((obj = model.get_Value("AD_ORGTRX_ID")) != null) dto.setAd_OrgTrx_ID((Integer) obj);
            if ((obj = model.get_Value("AD_USER_ID")) != null) dto.setAd_User_ID((Integer) obj);
            if ((obj = model.get_Value("BIRTHDAY")) != null) dto.setBirthday((Timestamp) obj);
            if ((obj = model.get_Value("C_BPARTNER_ID")) != null) dto.setC_BPartner_ID((Integer) obj);
            if ((obj = model.get_Value("C_BPARTNER_LOCATION_ID")) != null) dto.setC_BPartner_Location_ID((Integer) obj);
            if ((obj = model.get_Value("C_GREETING_ID")) != null) dto.setC_Greeting_ID((Integer) obj);
            if ((obj = model.get_Value("C_JOB_ID")) != null) dto.setC_Job_ID((Integer) obj);
            if ((obj = model.get_Value("COMMENTS")) != null) dto.setComments((String) obj);
            if ((obj = model.get_Value("CONNECTIONPROFILE")) != null) dto.setConnectionProfile((String) obj);
            if ((obj = model.get_Value("DESCRIPTION")) != null) dto.setDescription((String) obj);
            if ((obj = model.get_Value("EMAIL")) != null) dto.setEmail((String) obj);
            if ((obj = model.get_Value("EMAILUSER")) != null) dto.setEmailUser((String) obj);
            if ((obj = model.get_Value("EMAILUSERPW")) != null) dto.setEmailUserPW((String) obj);
            if ((obj = model.get_Value("EMAILVERIFY")) != null) dto.setEmailVerify((String) obj);
            if ((obj = model.get_Value("EMAILVERIFYDATE")) != null) dto.setEmailVerifyDate((Timestamp) obj);
            if ((obj = model.get_Value("FAX")) != null) dto.setFax((String) obj);
            if ((obj = model.get_Value("LASTCONTACT")) != null) dto.setLastContact((Timestamp) obj);
            if ((obj = model.get_Value("LASTRESULT")) != null) dto.setLastResult((String) obj);
            if ((obj = model.get_Value("LDAPUSER")) != null) dto.setLdAPUser((String) obj);
            if ((obj = model.get_Value("NAME")) != null) dto.setName((String) obj);
            if ((obj = model.get_Value("NOTIFICATIONTYPE")) != null) dto.setNotificationType((String) obj);
            if ((obj = model.get_Value("PASSWORD")) != null) dto.setPassword((String) obj);
            if ((obj = model.get_Value("PHONE")) != null) dto.setPhone((String) obj);
            if ((obj = model.get_Value("PHONE2")) != null) dto.setPhone2((String) obj);
            if ((obj = model.get_Value("SUPERVISOR_ID")) != null) dto.setSupervisor_ID((Integer) obj);
            if ((obj = model.get_Value("TITLE")) != null) dto.setTitle((String) obj);
            if ((obj = model.get_Value("USERPIN")) != null) dto.setUserPIN((String) obj);
            if ((obj = model.get_Value("VALUE")) != null) dto.setValue((String) obj);
            if ((obj = model.get_Value("ISFULLBPACCESS")) != null) dto.setIsFullBPAccess(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("PROCESSING")) != null) dto.setIsProcessing(((Boolean) obj) == true ? "Y" : "N");
            dto.setIsActive(((Boolean) model.get_Value("ISACTIVE")) == true ? "Y" : "N");
            dto.setAd_Client_ID((Integer) model.get_Value("AD_CLIENT_ID"));
            dto.setAd_Org_ID((Integer) model.get_Value("AD_Org_ID"));
        }
        return dto;
    }
}
