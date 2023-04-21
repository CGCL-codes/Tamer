package org.koossery.adempiere.dao.impl.product;

import java.util.ArrayList;
import org.koossery.adempiere.dao.impl.AbstractCommonDAO;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.koossery.adempiere.core.contract.dto.product.M_ProductionLineDTO;
import org.koossery.adempiere.core.contract.criteria.product.M_ProductionLineCriteria;
import org.koossery.adempiere.core.backend.interfaces.dao.product.IM_ProductionLineDAO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.exceptions.KTAdempierePersistenceException;

public class M_ProductionLineDAOImpl extends AbstractCommonDAO implements IM_ProductionLineDAO {

    SqlMapClientTemplate mapper = getMapperInstance();

    public boolean isDuplicate(String name) throws KTAdempiereException {
        int nboccur = -1;
        try {
            M_ProductionLineCriteria criteria = new M_ProductionLineCriteria();
            nboccur = Integer.parseInt(mapper.queryForObject("M_ProductionLine.verify", criteria).toString());
            return nboccur <= 0 ? false : true;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "M_PRODUCTIONLINE_DAO_000");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<M_ProductionLineDTO> getM_ProductionLine(M_ProductionLineCriteria m_ProductionLineCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<M_ProductionLineDTO>) mapper.queryForList("M_ProductionLine.find", m_ProductionLineCriteria);
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "M_PRODUCTIONLINE_DAO_001");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    public boolean update(M_ProductionLineCriteria m_ProductionLineCriteria) throws KTAdempiereException {
        try {
            int result = mapper.update("M_ProductionLine.update", m_ProductionLineCriteria);
            return result > 0 ? true : false;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "M_PRODUCTIONLINE_DAO_002");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }
}
