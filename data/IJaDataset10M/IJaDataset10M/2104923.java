package org.koossery.adempiere.dao.impl.ad;

import java.util.ArrayList;
import org.koossery.adempiere.dao.impl.AbstractCommonDAO;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.koossery.adempiere.core.contract.dto.ad.AD_ProcessDTO;
import org.koossery.adempiere.core.contract.criteria.ad.AD_ProcessCriteria;
import org.koossery.adempiere.core.backend.interfaces.dao.ad.IAD_ProcessDAO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.exceptions.KTAdempierePersistenceException;

public class AD_ProcessDAOImpl extends AbstractCommonDAO implements IAD_ProcessDAO {

    SqlMapClientTemplate mapper = getMapperInstance();

    public boolean isDuplicate(String name) throws KTAdempiereException {
        int nboccur = -1;
        try {
            AD_ProcessCriteria criteria = new AD_ProcessCriteria();
            criteria.setName(name.toLowerCase());
            nboccur = Integer.parseInt(mapper.queryForObject("AD_Process.verify", criteria).toString());
            return nboccur <= 0 ? false : true;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "AD_PROCESS_DAO_000");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<AD_ProcessDTO> getAD_Process(AD_ProcessCriteria aD_ProcessCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<AD_ProcessDTO>) mapper.queryForList("AD_Process.find", aD_ProcessCriteria);
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "AD_PROCESS_DAO_001");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    public boolean update(AD_ProcessCriteria aD_ProcessCriteria) throws KTAdempiereException {
        try {
            int result = mapper.update("AD_Process.update", aD_ProcessCriteria);
            return result > 0 ? true : false;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "AD_PROCESS_DAO_002");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }
}
