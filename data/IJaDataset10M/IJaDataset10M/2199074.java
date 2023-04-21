package org.koossery.adempiere.sisv.impl.data.replication;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.compiere.model.X_AD_ReplicationStrategy;
import org.koossery.adempiere.core.backend.interfaces.dao.data.replication.IAD_ReplicationStrategyDAO;
import org.koossery.adempiere.core.backend.interfaces.sisv.data.replication.IAD_ReplicationStrategySISV;
import org.koossery.adempiere.core.contract.criteria.data.replication.AD_ReplicationStrategyCriteria;
import org.koossery.adempiere.core.contract.dto.data.replication.AD_ReplicationStrategyDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;
import org.koossery.adempiere.sisv.impl.AbstractCommonSISV;
import org.springframework.beans.factory.InitializingBean;

public class AD_ReplicationStrategySISVImpl extends AbstractCommonSISV implements IAD_ReplicationStrategySISV, InitializingBean {

    private IAD_ReplicationStrategyDAO adreplicationstrategyDAOImpl;

    private static Logger logger = Logger.getLogger(AD_ReplicationStrategySISVImpl.class);

    public AD_ReplicationStrategySISVImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getDaoController() == null) System.out.println("finder null");
            this.adreplicationstrategyDAOImpl = (IAD_ReplicationStrategyDAO) this.getDaoController().get("DAO/AD_ReplicationStrategy");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createAD_ReplicationStrategy(Properties ctx, AD_ReplicationStrategyDTO aD_ReplicationStrategyDTO, String trxname) throws KTAdempiereAppException {
        try {
            X_AD_ReplicationStrategy model = new X_AD_ReplicationStrategy(ctx, 0, trxname);
            AD_ReplicationStrategyCriteria criteria = new AD_ReplicationStrategyCriteria();
            if (adreplicationstrategyDAOImpl.isDuplicate(criteria)) {
                throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_REPLICATIONSTRATEGY_SISV_000");
            } else {
                System.out.println("NO duplicates names");
                model.setDescription(aD_ReplicationStrategyDTO.getDescription());
                model.setEntityType(aD_ReplicationStrategyDTO.getEntityType());
                model.setHelp(aD_ReplicationStrategyDTO.getHelp());
                model.setName(aD_ReplicationStrategyDTO.getName());
                model.setIsActive(aD_ReplicationStrategyDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                aD_ReplicationStrategyDTO.setAd_ReplicationStrategy_ID(model.get_ID());
                return model.get_ID();
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_REPLICATIONSTRATEGY_SISV_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public AD_ReplicationStrategyDTO getAD_ReplicationStrategy(Properties ctx, int ad_ReplicationStrategy_ID, String trxname) throws KTAdempiereAppException {
        try {
            if (ad_ReplicationStrategy_ID == 0) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_REPLICATIONSTRATEGY_SISV_002");
            X_AD_ReplicationStrategy mreplicationstrategy = new X_AD_ReplicationStrategy(ctx, ad_ReplicationStrategy_ID, trxname);
            return convertModelToDTO(mreplicationstrategy);
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_REPLICATIONSTRATEGY_SISV_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<AD_ReplicationStrategyDTO> findAD_ReplicationStrategy(Properties ctx, AD_ReplicationStrategyCriteria aD_ReplicationStrategyCriteria) throws KTAdempiereAppException {
        try {
            String whereclause = getWhereClause(ctx, aD_ReplicationStrategyCriteria);
            int id[] = X_AD_ReplicationStrategy.getAllIDs("AD_ReplicationStrategy", whereclause, null);
            ArrayList<AD_ReplicationStrategyDTO> list = new ArrayList<AD_ReplicationStrategyDTO>();
            AD_ReplicationStrategyDTO dto;
            for (int i = 0; i < id.length; i++) {
                dto = this.getAD_ReplicationStrategy(ctx, id[i], null);
                if (dto != null) list.add(dto);
            }
            return list;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_REPLICATIONSTRATEGY_SISV_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateAD_ReplicationStrategy(Properties ctx, AD_ReplicationStrategyDTO aD_ReplicationStrategyDTO) throws KTAdempiereAppException {
        try {
            X_AD_ReplicationStrategy model = new X_AD_ReplicationStrategy(ctx, aD_ReplicationStrategyDTO.getAd_ReplicationStrategy_ID(), null);
            if (model != null) {
                AD_ReplicationStrategyCriteria criteria = new AD_ReplicationStrategyCriteria();
                if (adreplicationstrategyDAOImpl.isDuplicate(criteria)) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_REPLICATIONSTRATEGY_SISV_000");
                System.out.println("NO duplicates names for modification");
                model.setDescription(aD_ReplicationStrategyDTO.getDescription());
                model.setEntityType(aD_ReplicationStrategyDTO.getEntityType());
                model.setHelp(aD_ReplicationStrategyDTO.getHelp());
                model.setName(aD_ReplicationStrategyDTO.getName());
                model.setIsActive(aD_ReplicationStrategyDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                aD_ReplicationStrategyDTO.setAd_ReplicationStrategy_ID(model.get_ID());
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_REPLICATIONSTRATEGY_SISV_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public boolean deleteAD_ReplicationStrategy(Properties ctx, AD_ReplicationStrategyCriteria aD_ReplicationStrategyCriteria) throws KTAdempiereAppException {
        try {
            int id = aD_ReplicationStrategyCriteria.getAd_ReplicationStrategy_ID();
            X_AD_ReplicationStrategy model = new X_AD_ReplicationStrategy(ctx, id, null);
            return model.delete(true);
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_REPLICATIONSTRATEGY_SISV_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    protected String getWhereClause(Properties ctx, AD_ReplicationStrategyCriteria criteria) {
        StringBuffer temp = new StringBuffer(super.getWhereClause(ctx, criteria));
        if (criteria.getAd_ReplicationStrategy_ID() > 0) temp.append(" AND (AD_REPLICATIONSTRATEGY_ID=" + criteria.getAd_ReplicationStrategy_ID() + ")");
        if (criteria.getDescription() != null) temp.append(" AND ( DESCRIPTION LIKE '%" + criteria.getDescription() + "%')");
        if (criteria.getEntityType() != null) temp.append(" AND ( ENTITYTYPE LIKE '%" + criteria.getEntityType() + "%')");
        if (criteria.getHelp() != null) temp.append(" AND ( HELP LIKE '%" + criteria.getHelp() + "%')");
        if (criteria.getName() != null) temp.append(" AND ( NAME LIKE '%" + criteria.getName() + "%')");
        if (criteria.getIsActive() != null) temp.append(" AND (ISACTIVE='" + criteria.getIsActive() + "')");
        return temp.toString();
    }

    protected AD_ReplicationStrategyDTO convertModelToDTO(X_AD_ReplicationStrategy model) {
        AD_ReplicationStrategyDTO dto = null;
        Object obj;
        if (model != null) {
            dto = new AD_ReplicationStrategyDTO();
            if ((obj = model.get_Value("AD_REPLICATIONSTRATEGY_ID")) != null) dto.setAd_ReplicationStrategy_ID((Integer) obj);
            if ((obj = model.get_Value("DESCRIPTION")) != null) dto.setDescription((String) obj);
            if ((obj = model.get_Value("ENTITYTYPE")) != null) dto.setEntityType((String) obj);
            if ((obj = model.get_Value("HELP")) != null) dto.setHelp((String) obj);
            if ((obj = model.get_Value("NAME")) != null) dto.setName((String) obj);
            dto.setIsActive(((Boolean) model.get_Value("ISACTIVE")) == true ? "Y" : "N");
            dto.setAd_Client_ID((Integer) model.get_Value("AD_CLIENT_ID"));
            dto.setAd_Org_ID((Integer) model.get_Value("AD_Org_ID"));
        }
        return dto;
    }
}
