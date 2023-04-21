package org.koossery.adempiere.dao.impl.generated;

import java.util.ArrayList;
import org.koossery.adempiere.dao.impl.AbstractCommonDAO;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.koossery.adempiere.core.contract.dto.generated.C_Currency_AcctDTO;
import org.koossery.adempiere.core.contract.criteria.generated.C_Currency_AcctCriteria;
import org.koossery.adempiere.core.backend.interfaces.dao.generated.IC_Currency_AcctDAO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.exceptions.KTAdempierePersistenceException;

public class C_Currency_AcctDAOImpl extends AbstractCommonDAO implements IC_Currency_AcctDAO {

    SqlMapClientTemplate mapper = getMapperInstance();

    public boolean isDuplicate(String name) throws KTAdempiereException {
        try {
            return false;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "C_CURRENCY_ACCT_DAO_000");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<C_Currency_AcctDTO> getC_Currency_Acct(C_Currency_AcctCriteria c_Currency_AcctCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<C_Currency_AcctDTO>) mapper.queryForList("C_Currency_Acct.find", c_Currency_AcctCriteria);
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "C_CURRENCY_ACCT_DAO_001");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    public boolean update(C_Currency_AcctCriteria c_Currency_AcctCriteria) throws KTAdempiereException {
        try {
            int result = mapper.update("C_Currency_Acct.update", c_Currency_AcctCriteria);
            return result > 0 ? true : false;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "C_CURRENCY_ACCT_DAO_002");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }
}
