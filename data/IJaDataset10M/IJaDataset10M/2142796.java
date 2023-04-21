package org.koossery.adempiere.dao.impl.generated;

import java.util.ArrayList;
import org.koossery.adempiere.dao.impl.AbstractCommonDAO;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.koossery.adempiere.core.contract.dto.generated.C_DocTypeDTO;
import org.koossery.adempiere.core.contract.criteria.generated.C_DocTypeCriteria;
import org.koossery.adempiere.core.backend.interfaces.dao.generated.IC_DocTypeDAO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.exceptions.KTAdempierePersistenceException;

public class C_DocTypeDAOImpl extends AbstractCommonDAO implements IC_DocTypeDAO {

    SqlMapClientTemplate mapper = getMapperInstance();

    public boolean isDuplicate(String name) throws KTAdempiereException {
        int nboccur = -1;
        try {
            C_DocTypeCriteria criteria = new C_DocTypeCriteria();
            criteria.setName(name.toLowerCase());
            nboccur = Integer.parseInt(mapper.queryForObject("C_DocType.verify", criteria).toString());
            return nboccur <= 0 ? false : true;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "C_DOCTYPE_DAO_000");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<C_DocTypeDTO> getC_DocType(C_DocTypeCriteria c_DocTypeCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<C_DocTypeDTO>) mapper.queryForList("C_DocType.find", c_DocTypeCriteria);
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "C_DOCTYPE_DAO_001");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    public boolean update(C_DocTypeCriteria c_DocTypeCriteria) throws KTAdempiereException {
        try {
            int result = mapper.update("C_DocType.update", c_DocTypeCriteria);
            return result > 0 ? true : false;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "C_DOCTYPE_DAO_002");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }
}
