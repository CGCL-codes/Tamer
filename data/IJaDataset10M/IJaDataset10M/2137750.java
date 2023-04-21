package org.koossery.adempiere.dao.impl.ad;

import java.util.ArrayList;
import org.koossery.adempiere.dao.impl.AbstractCommonDAO;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.koossery.adempiere.core.contract.dto.ad.AD_Process_ParaDTO;
import org.koossery.adempiere.core.contract.criteria.ad.AD_Process_ParaCriteria;
import org.koossery.adempiere.core.backend.interfaces.dao.ad.IAD_Process_ParaDAO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.exceptions.KTAdempierePersistenceException;

public class AD_Process_ParaDAOImpl extends AbstractCommonDAO implements IAD_Process_ParaDAO {

    SqlMapClientTemplate mapper = getMapperInstance();

    @SuppressWarnings("unchecked")
    public ArrayList<AD_Process_ParaDTO> getAD_Process_Para(AD_Process_ParaCriteria aD_Process_ParaCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<AD_Process_ParaDTO>) mapper.queryForList("AD_Process_Para.find", aD_Process_ParaCriteria);
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "AD_PROCESS_PARA_DAO_001");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    public boolean update(AD_Process_ParaCriteria aD_Process_ParaCriteria) throws KTAdempiereException {
        try {
            int result = mapper.update("AD_Process_Para.update", aD_Process_ParaCriteria);
            return result > 0 ? true : false;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "AD_PROCESS_PARA_DAO_002");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    public boolean isDuplicate(AD_Process_ParaCriteria process_ParaCriteria) throws KTAdempiereException {
        int nboccur = -1;
        try {
            nboccur = Integer.parseInt(mapper.queryForObject("AD_Process_Para.verify", process_ParaCriteria).toString());
            return nboccur <= 0 ? false : true;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "AD_PROCESS_PARA_DAO_000");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }
}
