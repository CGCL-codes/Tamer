package org.koossery.adempiere.dao.impl.user;

import java.util.ArrayList;
import org.koossery.adempiere.dao.impl.AbstractCommonDAO;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.koossery.adempiere.core.contract.dto.user.AD_UserDTO;
import org.koossery.adempiere.core.contract.criteria.user.AD_UserCriteria;
import org.koossery.adempiere.core.backend.interfaces.dao.user.IAD_UserDAO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.exceptions.KTAdempierePersistenceException;

public class AD_UserDAOImpl extends AbstractCommonDAO implements IAD_UserDAO {

    SqlMapClientTemplate mapper = getMapperInstance();

    public boolean isDuplicate(AD_UserCriteria criteria) throws KTAdempiereException {
        int nboccur = -1;
        try {
            nboccur = Integer.parseInt(mapper.queryForObject("AD_User.verify", criteria).toString());
            return nboccur <= 0 ? false : true;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "AD_USER_DAO_000");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<AD_UserDTO> getAD_User(AD_UserCriteria aD_UserCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<AD_UserDTO>) mapper.queryForList("AD_User.find", aD_UserCriteria);
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "AD_USER_DAO_001");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    public boolean update(AD_UserCriteria aD_UserCriteria) throws KTAdempiereException {
        try {
            int result = mapper.update("AD_User.update", aD_UserCriteria);
            return result > 0 ? true : false;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "AD_USER_DAO_002");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }
}
