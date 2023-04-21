package org.koossery.adempiere.dao.impl.cost;

import java.util.ArrayList;
import org.koossery.adempiere.dao.impl.AbstractCommonDAO;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.koossery.adempiere.core.contract.dto.cost.M_CostDTO;
import org.koossery.adempiere.core.contract.criteria.cost.M_CostCriteria;
import org.koossery.adempiere.core.backend.interfaces.dao.cost.IM_CostDAO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.exceptions.KTAdempierePersistenceException;

public class M_CostDAOImpl extends AbstractCommonDAO implements IM_CostDAO {

    SqlMapClientTemplate mapper = getMapperInstance();

    public boolean isDuplicate(String name) throws KTAdempiereException {
        int nboccur = -1;
        try {
            M_CostCriteria criteria = new M_CostCriteria();
            nboccur = Integer.parseInt(mapper.queryForObject("M_Cost.verify", criteria).toString());
            return nboccur <= 0 ? false : true;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "M_COST_DAO_000");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<M_CostDTO> getM_Cost(M_CostCriteria m_CostCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<M_CostDTO>) mapper.queryForList("M_Cost.find", m_CostCriteria);
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "M_COST_DAO_001");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    public boolean update(M_CostCriteria m_CostCriteria) throws KTAdempiereException {
        try {
            int result = mapper.update("M_Cost.update", m_CostCriteria);
            return result > 0 ? true : false;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "M_COST_DAO_002");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }
}
