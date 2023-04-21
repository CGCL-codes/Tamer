package org.koossery.adempiere.dao.impl.generated;

import java.util.ArrayList;
import org.koossery.adempiere.dao.impl.AbstractCommonDAO;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.koossery.adempiere.core.contract.dto.generated.GL_DistributionLineDTO;
import org.koossery.adempiere.core.contract.criteria.generated.GL_DistributionLineCriteria;
import org.koossery.adempiere.core.backend.interfaces.dao.generated.IGL_DistributionLineDAO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.exceptions.KTAdempierePersistenceException;

public class GL_DistributionLineDAOImpl extends AbstractCommonDAO implements IGL_DistributionLineDAO {

    SqlMapClientTemplate mapper = getMapperInstance();

    public boolean isDuplicate(String name) throws KTAdempiereException {
        try {
            return false;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "GL_DISTRIBUTIONLINE_DAO_000");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<GL_DistributionLineDTO> getGL_DistributionLine(GL_DistributionLineCriteria gL_DistributionLineCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<GL_DistributionLineDTO>) mapper.queryForList("GL_DistributionLine.find", gL_DistributionLineCriteria);
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "GL_DISTRIBUTIONLINE_DAO_001");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    public boolean update(GL_DistributionLineCriteria gL_DistributionLineCriteria) throws KTAdempiereException {
        try {
            int result = mapper.update("GL_DistributionLine.update", gL_DistributionLineCriteria);
            return result > 0 ? true : false;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "GL_DISTRIBUTIONLINE_DAO_002");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }
}
