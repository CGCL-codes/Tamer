package org.koossery.adempiere.dao.impl.server.alert;

import java.util.ArrayList;
import org.koossery.adempiere.core.backend.interfaces.dao.server.alert.IAD_AlertProcessorDAO;
import org.koossery.adempiere.core.contract.criteria.server.alert.AD_AlertProcessorCriteria;
import org.koossery.adempiere.core.contract.dto.server.alert.AD_AlertProcessorDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.exceptions.KTAdempierePersistenceException;
import org.koossery.adempiere.dao.impl.AbstractCommonDAO;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class AD_AlertProcessorDAOImpl extends AbstractCommonDAO implements IAD_AlertProcessorDAO {

    SqlMapClientTemplate mapper = getMapperInstance();

    public boolean isDuplicate(AD_AlertProcessorCriteria criteria) throws KTAdempiereException {
        try {
            return false;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "AD_ALERTPROCESSOR_DAO_000");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<AD_AlertProcessorDTO> getAD_AlertProcessor(AD_AlertProcessorCriteria aD_AlertProcessorCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<AD_AlertProcessorDTO>) mapper.queryForList("AD_AlertProcessor.find", aD_AlertProcessorCriteria);
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "AD_ALERTPROCESSOR_DAO_001");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    public boolean update(AD_AlertProcessorCriteria aD_AlertProcessorCriteria) throws KTAdempiereException {
        try {
            int result = mapper.update("AD_AlertProcessor.update", aD_AlertProcessorCriteria);
            return result > 0 ? true : false;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "AD_ALERTPROCESSOR_DAO_002");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }
}
