package org.koossery.adempiere.sisv.impl.system;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.compiere.model.X_AD_System;
import org.koossery.adempiere.core.backend.interfaces.dao.system.IAD_SystemDAO;
import org.koossery.adempiere.core.backend.interfaces.sisv.system.IAD_SystemSISV;
import org.koossery.adempiere.core.contract.criteria.system.AD_SystemCriteria;
import org.koossery.adempiere.core.contract.dto.system.AD_SystemDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;
import org.koossery.adempiere.sisv.impl.AbstractCommonSISV;
import org.springframework.beans.factory.InitializingBean;

public class AD_SystemSISVImpl extends AbstractCommonSISV implements IAD_SystemSISV, InitializingBean {

    private IAD_SystemDAO adsystemDAOImpl;

    private static Logger logger = Logger.getLogger(AD_SystemSISVImpl.class);

    public AD_SystemSISVImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getDaoController() == null) System.out.println("finder null");
            this.adsystemDAOImpl = (IAD_SystemDAO) this.getDaoController().get("DAO/AD_System");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createAD_System(Properties ctx, AD_SystemDTO aD_SystemDTO, String trxname) throws KTAdempiereAppException {
        try {
            X_AD_System model = new X_AD_System(ctx, 0, trxname);
            AD_SystemCriteria criteria = new AD_SystemCriteria();
            criteria.setName(aD_SystemDTO.getName());
            if (adsystemDAOImpl.isDuplicate(criteria)) {
                throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_SYSTEM_SISV_000");
            } else {
                System.out.println("NO duplicates names");
                model.setCustomPrefix(aD_SystemDTO.getCustomPrefix());
                model.setDBAddress(aD_SystemDTO.getDbAddress());
                model.setDBInstance(aD_SystemDTO.getDbInstance());
                model.setDescription(aD_SystemDTO.getDescription());
                model.setEncryptionKey(aD_SystemDTO.getEncryptionKey());
                model.setIDRangeEnd(aD_SystemDTO.getIdRangeEnd());
                model.setIDRangeStart(aD_SystemDTO.getIdRangeStart());
                model.setInfo(aD_SystemDTO.getInfo());
                model.setLDAPDomain(aD_SystemDTO.getLdAPDomain());
                model.setLDAPHost(aD_SystemDTO.getLdAPHost());
                model.setName(aD_SystemDTO.getName());
                model.setNoProcessors(aD_SystemDTO.getNoProcessors());
                model.setOldName(aD_SystemDTO.getOldName());
                model.setPassword(aD_SystemDTO.getPassword());
                model.setProfileInfo(aD_SystemDTO.getProfileInfo());
                model.setRecord_ID(aD_SystemDTO.getRecord_ID());
                model.setReleaseNo(aD_SystemDTO.getReleaseNo());
                model.setReplicationType(aD_SystemDTO.getReplicationType());
                model.setStatisticsInfo(aD_SystemDTO.getStatisticsInfo());
                model.setSummary(aD_SystemDTO.getSummary());
                model.setSupportEMail(aD_SystemDTO.getSupportEMail());
                model.setSupportExpDate(aD_SystemDTO.getSupportExpDate());
                model.setSupportUnits(aD_SystemDTO.getSupportUnits());
                model.setSystemStatus(aD_SystemDTO.getSystemStatus());
                model.setUserName(aD_SystemDTO.getUserName());
                model.setVersion(aD_SystemDTO.getVersion());
                model.setIsAllowStatistics(aD_SystemDTO.getIsAllowStatistics() == "Y" ? true : false);
                model.setIsAutoErrorReport(aD_SystemDTO.getIsAutoErrorReport() == "Y" ? true : false);
                model.setIsFailOnMissingModelValidator(aD_SystemDTO.getIsFailOnMissingModelValidator() == "Y" ? true : false);
                model.setIsJustMigrated(aD_SystemDTO.getIsJustMigrated() == "Y" ? true : false);
                model.set_CustomColumn("PROCESSING", aD_SystemDTO.getIsProcessing() == "Y" ? true : false);
                model.setIsActive(aD_SystemDTO.getIsActive() == "Y" ? true : false);
                model.setAD_Org_ID(0);
                this.setPO(model);
                this.save();
                aD_SystemDTO.setAd_System_ID(model.getAD_System_ID());
                aD_SystemDTO.setAd_Client_ID(model.getAD_Client_ID());
                return model.get_ID();
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_SYSTEM_SISV_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public AD_SystemDTO getAD_System(Properties ctx, int ad_System_ID, int ad_Client_ID, String trxname) throws KTAdempiereAppException {
        try {
            if (ad_System_ID == -1) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_SYSTEM_SISV_002");
            X_AD_System msystem = new X_AD_System(ctx, ad_System_ID, trxname);
            return convertModelToDTO(msystem);
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_SYSTEM_SISV_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<AD_SystemDTO> findAD_System(Properties ctx, AD_SystemCriteria aD_SystemCriteria) throws KTAdempiereAppException {
        try {
            String whereclause = getWhereClause(ctx, aD_SystemCriteria);
            int id1[] = X_AD_System.getAllIDs("AD_System", whereclause, null);
            int id2[] = X_AD_System.getAllIDs("AD_Client", whereclause, null);
            ArrayList<AD_SystemDTO> list = new ArrayList<AD_SystemDTO>();
            AD_SystemDTO dto;
            for (int i = 0; i < id1.length; i++) {
                dto = this.getAD_System(ctx, id1[i], id2[i], null);
                if (dto != null) list.add(dto);
            }
            return list;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_SYSTEM_SISV_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateAD_System(Properties ctx, AD_SystemDTO aD_SystemDTO) throws KTAdempiereAppException {
        try {
            X_AD_System model = new X_AD_System(ctx, aD_SystemDTO.getAd_System_ID(), null);
            if (model != null) {
                String oldname = model.getName() + "";
                String newname = aD_SystemDTO.getName() + "";
                if (oldname.compareToIgnoreCase(newname) != 0) {
                    AD_SystemCriteria criteria = new AD_SystemCriteria();
                    criteria.setName(newname);
                    if (adsystemDAOImpl.isDuplicate(criteria)) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_SYSTEM_SISV_000");
                }
                System.out.println("NO duplicates names for modification");
                model.setCustomPrefix(aD_SystemDTO.getCustomPrefix());
                model.setDBAddress(aD_SystemDTO.getDbAddress());
                model.setDBInstance(aD_SystemDTO.getDbInstance());
                model.setDescription(aD_SystemDTO.getDescription());
                model.setEncryptionKey(aD_SystemDTO.getEncryptionKey());
                model.setIDRangeEnd(aD_SystemDTO.getIdRangeEnd());
                model.setIDRangeStart(aD_SystemDTO.getIdRangeStart());
                model.setInfo(aD_SystemDTO.getInfo());
                model.setLDAPDomain(aD_SystemDTO.getLdAPDomain());
                model.setLDAPHost(aD_SystemDTO.getLdAPHost());
                model.setName(aD_SystemDTO.getName());
                model.setNoProcessors(aD_SystemDTO.getNoProcessors());
                model.setOldName(aD_SystemDTO.getOldName());
                model.setPassword(aD_SystemDTO.getPassword());
                model.setProfileInfo(aD_SystemDTO.getProfileInfo());
                model.setRecord_ID(aD_SystemDTO.getRecord_ID());
                model.setReleaseNo(aD_SystemDTO.getReleaseNo());
                model.setReplicationType(aD_SystemDTO.getReplicationType());
                model.setStatisticsInfo(aD_SystemDTO.getStatisticsInfo());
                model.setSummary(aD_SystemDTO.getSummary());
                model.setSupportEMail(aD_SystemDTO.getSupportEMail());
                model.setSupportExpDate(aD_SystemDTO.getSupportExpDate());
                model.setSupportUnits(aD_SystemDTO.getSupportUnits());
                model.setSystemStatus(aD_SystemDTO.getSystemStatus());
                model.setUserName(aD_SystemDTO.getUserName());
                model.setVersion(aD_SystemDTO.getVersion());
                model.setIsAllowStatistics(aD_SystemDTO.getIsAllowStatistics() == "Y" ? true : false);
                model.setIsAutoErrorReport(aD_SystemDTO.getIsAutoErrorReport() == "Y" ? true : false);
                model.setIsFailOnMissingModelValidator(aD_SystemDTO.getIsFailOnMissingModelValidator() == "Y" ? true : false);
                model.setIsJustMigrated(aD_SystemDTO.getIsJustMigrated() == "Y" ? true : false);
                model.set_CustomColumn("PROCESSING", aD_SystemDTO.getIsProcessing() == "Y" ? true : false);
                model.setIsActive(aD_SystemDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                aD_SystemDTO.setAd_System_ID(model.get_ID());
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_SYSTEM_SISV_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public boolean deleteAD_System(Properties ctx, AD_SystemCriteria aD_SystemCriteria) throws KTAdempiereAppException {
        try {
            int id = aD_SystemCriteria.getAd_System_ID();
            X_AD_System model = new X_AD_System(ctx, id, null);
            return model.delete(true);
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_SYSTEM_SISV_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    protected String getWhereClause(Properties ctx, AD_SystemCriteria criteria) {
        StringBuffer temp = new StringBuffer(super.getWhereClause(ctx, criteria));
        if (criteria.getAd_System_ID() > 0) temp.append(" AND (AD_SYSTEM_ID=" + criteria.getAd_System_ID() + ")");
        if (criteria.getCustomPrefix() != null) temp.append(" AND ( CUSTOMPREFIX LIKE '%" + criteria.getCustomPrefix() + "%')");
        if (criteria.getDbAddress() != null) temp.append(" AND ( DBADDRESS LIKE '%" + criteria.getDbAddress() + "%')");
        if (criteria.getDbInstance() != null) temp.append(" AND ( DBINSTANCE LIKE '%" + criteria.getDbInstance() + "%')");
        if (criteria.getDescription() != null) temp.append(" AND ( DESCRIPTION LIKE '%" + criteria.getDescription() + "%')");
        if (criteria.getEncryptionKey() != null) temp.append(" AND ( ENCRYPTIONKEY LIKE '%" + criteria.getEncryptionKey() + "%')");
        if (criteria.getIdRangeEnd() != null) temp.append(" AND (IDRANGEEND=" + criteria.getIdRangeEnd() + ")");
        if (criteria.getIdRangeStart() != null) temp.append(" AND (IDRANGESTART=" + criteria.getIdRangeStart() + ")");
        if (criteria.getInfo() != null) temp.append(" AND ( INFO LIKE '%" + criteria.getInfo() + "%')");
        if (criteria.getLdAPDomain() != null) temp.append(" AND ( LDAPDOMAIN LIKE '%" + criteria.getLdAPDomain() + "%')");
        if (criteria.getLdAPHost() != null) temp.append(" AND ( LDAPHOST LIKE '%" + criteria.getLdAPHost() + "%')");
        if (criteria.getName() != null) temp.append(" AND ( NAME LIKE '%" + criteria.getName() + "%')");
        if (criteria.getNoProcessors() > 0) temp.append(" AND (NOPROCESSORS=" + criteria.getNoProcessors() + ")");
        if (criteria.getOldName() != null) temp.append(" AND ( OLDNAME LIKE '%" + criteria.getOldName() + "%')");
        if (criteria.getPassword() != null) temp.append(" AND ( PASSWORD LIKE '%" + criteria.getPassword() + "%')");
        if (criteria.getProfileInfo() != null) temp.append(" AND ( PROFILEINFO LIKE '%" + criteria.getProfileInfo() + "%')");
        if (criteria.getRecord_ID() > 0) temp.append(" AND (RECORD_ID=" + criteria.getRecord_ID() + ")");
        if (criteria.getReleaseNo() != null) temp.append(" AND ( RELEASENO LIKE '%" + criteria.getReleaseNo() + "%')");
        if (criteria.getReplicationType() != null) temp.append(" AND ( REPLICATIONTYPE LIKE '%" + criteria.getReplicationType() + "%')");
        if (criteria.getStatisticsInfo() != null) temp.append(" AND ( STATISTICSINFO LIKE '%" + criteria.getStatisticsInfo() + "%')");
        if (criteria.getSummary() != null) temp.append(" AND ( SUMMARY LIKE '%" + criteria.getSummary() + "%')");
        if (criteria.getSupportEMail() != null) temp.append(" AND ( SUPPORTEMAIL LIKE '%" + criteria.getSupportEMail() + "%')");
        if (criteria.getSupportExpDate() != null) temp.append(" AND (SUPPORTEXPDATE=" + criteria.getSupportExpDate() + ")");
        if (criteria.getSupportUnits() > 0) temp.append(" AND (SUPPORTUNITS=" + criteria.getSupportUnits() + ")");
        if (criteria.getSystemStatus() != null) temp.append(" AND ( SYSTEMSTATUS LIKE '%" + criteria.getSystemStatus() + "%')");
        if (criteria.getUserName() != null) temp.append(" AND ( USERNAME LIKE '%" + criteria.getUserName() + "%')");
        if (criteria.getVersion() != null) temp.append(" AND ( VERSION LIKE '%" + criteria.getVersion() + "%')");
        if (criteria.getIsAllowStatistics() != null) temp.append(" AND (ISALLOWSTATISTICS='" + criteria.getIsAllowStatistics() + "')");
        if (criteria.getIsAutoErrorReport() != null) temp.append(" AND (ISAUTOERRORREPORT='" + criteria.getIsAutoErrorReport() + "')");
        if (criteria.getIsFailOnMissingModelValidator() != null) temp.append(" AND (ISFAILONMISSINGMODELVALIDATOR='" + criteria.getIsFailOnMissingModelValidator() + "')");
        if (criteria.getIsJustMigrated() != null) temp.append(" AND (ISJUSTMIGRATED='" + criteria.getIsJustMigrated() + "')");
        if (criteria.getIsProcessing() != null) temp.append(" AND (PROCESSING='" + criteria.getIsProcessing() + "')");
        if (criteria.getIsActive() != null) temp.append(" AND (ISACTIVE='" + criteria.getIsActive() + "')");
        return temp.toString();
    }

    protected AD_SystemDTO convertModelToDTO(X_AD_System model) {
        AD_SystemDTO dto = null;
        Object obj;
        if (model != null) {
            dto = new AD_SystemDTO();
            if ((obj = model.get_Value("AD_SYSTEM_ID")) != null) dto.setAd_System_ID((Integer) obj);
            if ((obj = model.get_Value("CUSTOMPREFIX")) != null) dto.setCustomPrefix((String) obj);
            if ((obj = model.get_Value("DBADDRESS")) != null) dto.setDbAddress((String) obj);
            if ((obj = model.get_Value("DBINSTANCE")) != null) dto.setDbInstance((String) obj);
            if ((obj = model.get_Value("DESCRIPTION")) != null) dto.setDescription((String) obj);
            if ((obj = model.get_Value("ENCRYPTIONKEY")) != null) dto.setEncryptionKey((String) obj);
            if ((obj = model.get_Value("IDRANGEEND")) != null) dto.setIdRangeEnd((BigDecimal) obj);
            if ((obj = model.get_Value("IDRANGESTART")) != null) dto.setIdRangeStart((BigDecimal) obj);
            if ((obj = model.get_Value("INFO")) != null) dto.setInfo((String) obj);
            if ((obj = model.get_Value("LDAPDOMAIN")) != null) dto.setLdAPDomain((String) obj);
            if ((obj = model.get_Value("LDAPHOST")) != null) dto.setLdAPHost((String) obj);
            if ((obj = model.get_Value("NAME")) != null) dto.setName((String) obj);
            if ((obj = model.get_Value("NOPROCESSORS")) != null) dto.setNoProcessors((Integer) obj);
            if ((obj = model.get_Value("OLDNAME")) != null) dto.setOldName((String) obj);
            if ((obj = model.get_Value("PASSWORD")) != null) dto.setPassword((String) obj);
            if ((obj = model.get_Value("PROFILEINFO")) != null) dto.setProfileInfo((String) obj);
            if ((obj = model.get_Value("RECORD_ID")) != null) dto.setRecord_ID((Integer) obj);
            if ((obj = model.get_Value("RELEASENO")) != null) dto.setReleaseNo((String) obj);
            if ((obj = model.get_Value("REPLICATIONTYPE")) != null) dto.setReplicationType((String) obj);
            if ((obj = model.get_Value("STATISTICSINFO")) != null) dto.setStatisticsInfo((String) obj);
            if ((obj = model.get_Value("SUMMARY")) != null) dto.setSummary((String) obj);
            if ((obj = model.get_Value("SUPPORTEMAIL")) != null) dto.setSupportEMail((String) obj);
            if ((obj = model.get_Value("SUPPORTEXPDATE")) != null) dto.setSupportExpDate((Timestamp) obj);
            if ((obj = model.get_Value("SUPPORTUNITS")) != null) dto.setSupportUnits((Integer) obj);
            if ((obj = model.get_Value("SYSTEMSTATUS")) != null) dto.setSystemStatus((String) obj);
            if ((obj = model.get_Value("USERNAME")) != null) dto.setUserName((String) obj);
            if ((obj = model.get_Value("VERSION")) != null) dto.setVersion((String) obj);
            if ((obj = model.get_Value("ISALLOWSTATISTICS")) != null) dto.setIsAllowStatistics(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ISAUTOERRORREPORT")) != null) dto.setIsAutoErrorReport(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ISFAILONMISSINGMODELVALIDATOR")) != null) dto.setIsFailOnMissingModelValidator(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ISJUSTMIGRATED")) != null) dto.setIsJustMigrated(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("PROCESSING")) != null) dto.setIsProcessing(((Boolean) obj) == true ? "Y" : "N");
            dto.setIsActive(((Boolean) model.get_Value("ISACTIVE")) == true ? "Y" : "N");
            dto.setAd_Client_ID((Integer) model.get_Value("AD_CLIENT_ID"));
            dto.setAd_Org_ID((Integer) model.get_Value("AD_Org_ID"));
        }
        return dto;
    }
}
