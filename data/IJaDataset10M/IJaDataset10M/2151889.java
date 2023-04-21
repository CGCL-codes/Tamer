package org.koossery.adempiere.dao.impl.org.bank;

import java.util.ArrayList;
import org.koossery.adempiere.core.backend.interfaces.dao.org.bank.IC_BP_BankAccountDAO;
import org.koossery.adempiere.core.contract.criteria.org.bank.C_BP_BankAccountCriteria;
import org.koossery.adempiere.core.contract.dto.org.bank.C_BP_BankAccountDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.exceptions.KTAdempierePersistenceException;
import org.koossery.adempiere.dao.impl.AbstractCommonDAO;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class C_BP_BankAccountDAOImpl extends AbstractCommonDAO implements IC_BP_BankAccountDAO {

    SqlMapClientTemplate mapper = getMapperInstance();

    public boolean isDuplicate(C_BP_BankAccountCriteria criteria) throws KTAdempiereException {
        int nboccur = -1;
        try {
            nboccur = Integer.parseInt(mapper.queryForObject("C_BP_BankAccount.verify", criteria).toString());
            return nboccur <= 0 ? false : true;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "C_BP_BANKACCOUNT_DAO_000");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<C_BP_BankAccountDTO> getC_BP_BankAccount(C_BP_BankAccountCriteria c_BP_BankAccountCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<C_BP_BankAccountDTO>) mapper.queryForList("C_BP_BankAccount.find", c_BP_BankAccountCriteria);
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "C_BP_BANKACCOUNT_DAO_001");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    public boolean update(C_BP_BankAccountCriteria c_BP_BankAccountCriteria) throws KTAdempiereException {
        try {
            int result = mapper.update("C_BP_BankAccount.update", c_BP_BankAccountCriteria);
            return result > 0 ? true : false;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "C_BP_BANKACCOUNT_DAO_002");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }
}
