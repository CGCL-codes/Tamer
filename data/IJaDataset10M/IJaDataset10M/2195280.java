package org.koossery.adempiere.dao.impl.generated;

import java.util.ArrayList;
import org.koossery.adempiere.dao.impl.AbstractCommonDAO;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.koossery.adempiere.core.contract.dto.generated.GL_FundDTO;
import org.koossery.adempiere.core.contract.criteria.generated.GL_FundCriteria;
import org.koossery.adempiere.core.backend.interfaces.dao.generated.IGL_FundDAO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.exceptions.KTAdempierePersistenceException;

public class GL_FundDAOImpl extends AbstractCommonDAO implements IGL_FundDAO {

    SqlMapClientTemplate mapper = getMapperInstance();

    public boolean isDuplicate(String name) throws KTAdempiereException {
        int nboccur = -1;
        try {
            GL_FundCriteria criteria = new GL_FundCriteria();
            criteria.setName(name.toLowerCase());
            nboccur = Integer.parseInt(mapper.queryForObject("GL_Fund.verify", criteria).toString());
            return nboccur <= 0 ? false : true;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "GL_FUND_DAO_000");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<GL_FundDTO> getGL_Fund(GL_FundCriteria gL_FundCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<GL_FundDTO>) mapper.queryForList("GL_Fund.find", gL_FundCriteria);
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "GL_FUND_DAO_001");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    public boolean update(GL_FundCriteria gL_FundCriteria) throws KTAdempiereException {
        try {
            int result = mapper.update("GL_Fund.update", gL_FundCriteria);
            return result > 0 ? true : false;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "GL_FUND_DAO_002");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }
}
