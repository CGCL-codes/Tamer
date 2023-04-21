package org.koossery.adempiere.sisv.impl.ad;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.compiere.model.X_AD_Message;
import org.koossery.adempiere.core.backend.interfaces.dao.ad.IAD_MessageDAO;
import org.koossery.adempiere.core.backend.interfaces.sisv.ad.IAD_MessageSISV;
import org.koossery.adempiere.core.contract.criteria.ad.AD_MessageCriteria;
import org.koossery.adempiere.core.contract.dto.ad.AD_MessageDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;
import org.koossery.adempiere.sisv.impl.AbstractCommonSISV;
import org.springframework.beans.factory.InitializingBean;

public class AD_MessageSISVImpl extends AbstractCommonSISV implements IAD_MessageSISV, InitializingBean {

    private IAD_MessageDAO admessageDAOImpl;

    private static Logger logger = Logger.getLogger(AD_MessageSISVImpl.class);

    public AD_MessageSISVImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getDaoController() == null) System.out.println("finder null");
            this.admessageDAOImpl = (IAD_MessageDAO) this.getDaoController().get("DAO/AD_Message");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createAD_Message(Properties ctx, AD_MessageDTO aD_MessageDTO, String trxname) throws KTAdempiereAppException {
        try {
            X_AD_Message model = new X_AD_Message(ctx, 0, trxname);
            AD_MessageCriteria criteria = new AD_MessageCriteria();
            criteria.setValue(aD_MessageDTO.getValue());
            if (admessageDAOImpl.isDuplicate(criteria)) {
                throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_MESSAGE_SISV_000");
            } else {
                System.out.println("NO duplicates names");
                model.setEntityType(aD_MessageDTO.getEntityType());
                model.setMsgText(aD_MessageDTO.getMsgText());
                model.setMsgTip(aD_MessageDTO.getMsgTip());
                model.setMsgType(aD_MessageDTO.getMsgType());
                model.setValue(aD_MessageDTO.getValue());
                model.setIsActive(aD_MessageDTO.getIsActive() == "Y" ? true : false);
                model.setAD_Org_ID(0);
                this.setPO(model);
                this.save();
                aD_MessageDTO.setAd_Message_ID(model.get_ID());
                return model.get_ID();
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_MESSAGE_SISV_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public AD_MessageDTO getAD_Message(Properties ctx, int ad_Message_ID, String trxname) throws KTAdempiereAppException {
        try {
            if (ad_Message_ID == 0) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_MESSAGE_SISV_002");
            X_AD_Message mmessage = new X_AD_Message(ctx, ad_Message_ID, trxname);
            return convertModelToDTO(mmessage);
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_MESSAGE_SISV_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<AD_MessageDTO> findAD_Message(Properties ctx, AD_MessageCriteria aD_MessageCriteria) throws KTAdempiereAppException {
        try {
            String whereclause = getWhereClause(ctx, aD_MessageCriteria);
            int id[] = X_AD_Message.getAllIDs("AD_Message", whereclause, null);
            ArrayList<AD_MessageDTO> list = new ArrayList<AD_MessageDTO>();
            AD_MessageDTO dto;
            for (int i = 0; i < id.length; i++) {
                dto = this.getAD_Message(ctx, id[i], null);
                if (dto != null) list.add(dto);
            }
            return list;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_MESSAGE_SISV_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateAD_Message(Properties ctx, AD_MessageDTO aD_MessageDTO) throws KTAdempiereAppException {
        try {
            X_AD_Message model = new X_AD_Message(ctx, aD_MessageDTO.getAd_Message_ID(), null);
            if (model != null) {
                String oldname = model.getValue() + "";
                String newname = aD_MessageDTO.getValue() + "";
                if (oldname.compareToIgnoreCase(newname) != 0) {
                    AD_MessageCriteria criteria = new AD_MessageCriteria();
                    criteria.setValue(newname);
                    if (admessageDAOImpl.isDuplicate(criteria)) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_MESSAGE_SISV_000");
                }
                System.out.println("NO duplicates names for modification");
                model.setEntityType(aD_MessageDTO.getEntityType());
                model.setMsgText(aD_MessageDTO.getMsgText());
                model.setMsgTip(aD_MessageDTO.getMsgTip());
                model.setMsgType(aD_MessageDTO.getMsgType());
                model.setValue(aD_MessageDTO.getValue());
                model.setIsActive(aD_MessageDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                aD_MessageDTO.setAd_Message_ID(model.get_ID());
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_MESSAGE_SISV_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public boolean deleteAD_Message(Properties ctx, AD_MessageCriteria aD_MessageCriteria) throws KTAdempiereAppException {
        try {
            int id = aD_MessageCriteria.getAd_Message_ID();
            X_AD_Message model = new X_AD_Message(ctx, id, null);
            return model.delete(true);
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_MESSAGE_SISV_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    protected String getWhereClause(Properties ctx, AD_MessageCriteria criteria) {
        StringBuffer temp = new StringBuffer(super.getWhereClause(ctx, criteria));
        if (criteria.getAd_Message_ID() > 0) temp.append(" AND (AD_MESSAGE_ID=" + criteria.getAd_Message_ID() + ")");
        if (criteria.getEntityType() != null) temp.append(" AND ( ENTITYTYPE LIKE '%" + criteria.getEntityType() + "%')");
        if (criteria.getMsgText() != null) temp.append(" AND ( MSGTEXT LIKE '%" + criteria.getMsgText() + "%')");
        if (criteria.getMsgTip() != null) temp.append(" AND ( MSGTIP LIKE '%" + criteria.getMsgTip() + "%')");
        if (criteria.getMsgType() != null) temp.append(" AND ( MSGTYPE LIKE '%" + criteria.getMsgType() + "%')");
        if (criteria.getValue() != null) temp.append(" AND ( VALUE LIKE '%" + criteria.getValue() + "%')");
        if (criteria.getIsActive() != null) temp.append(" AND (ISACTIVE='" + criteria.getIsActive() + "')");
        return temp.toString();
    }

    protected AD_MessageDTO convertModelToDTO(X_AD_Message model) {
        AD_MessageDTO dto = null;
        Object obj;
        if (model != null) {
            dto = new AD_MessageDTO();
            if ((obj = model.get_Value("AD_MESSAGE_ID")) != null) dto.setAd_Message_ID((Integer) obj);
            if ((obj = model.get_Value("ENTITYTYPE")) != null) dto.setEntityType((String) obj);
            if ((obj = model.get_Value("MSGTEXT")) != null) dto.setMsgText((String) obj);
            if ((obj = model.get_Value("MSGTIP")) != null) dto.setMsgTip((String) obj);
            if ((obj = model.get_Value("MSGTYPE")) != null) dto.setMsgType((String) obj);
            if ((obj = model.get_Value("VALUE")) != null) dto.setValue((String) obj);
            dto.setIsActive(((Boolean) model.get_Value("ISACTIVE")) == true ? "Y" : "N");
            dto.setAd_Client_ID((Integer) model.get_Value("AD_CLIENT_ID"));
            dto.setAd_Org_ID((Integer) model.get_Value("AD_Org_ID"));
        }
        return dto;
    }
}
