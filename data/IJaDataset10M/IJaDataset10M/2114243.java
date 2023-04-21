package org.koossery.adempiere.dao.impl.payroll;

import java.util.ArrayList;
import org.koossery.adempiere.dao.impl.AbstractCommonDAO;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.koossery.adempiere.core.contract.dto.payroll.HR_Profile_bpDTO;
import org.koossery.adempiere.core.contract.criteria.payroll.HR_Profile_bpCriteria;
import org.koossery.adempiere.core.backend.interfaces.dao.payroll.IHR_Profile_bpDAO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.exceptions.KTAdempierePersistenceException;

public class HR_Profile_bpDAOImpl extends AbstractCommonDAO implements IHR_Profile_bpDAO {

    SqlMapClientTemplate mapper = getMapperInstance();

    public boolean isDuplicate(String name) throws KTAdempiereException {
        try {
            return false;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "HR_PROFILE_BP_DAO_000");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<HR_Profile_bpDTO> getHR_Profile_bp(HR_Profile_bpCriteria hR_Profile_bpCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<HR_Profile_bpDTO>) mapper.queryForList("HR_Profile_bp.find", hR_Profile_bpCriteria);
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "HR_PROFILE_BP_DAO_001");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    public boolean update(HR_Profile_bpCriteria hR_Profile_bpCriteria) throws KTAdempiereException {
        try {
            int result = mapper.update("HR_Profile_bp.update", hR_Profile_bpCriteria);
            return result > 0 ? true : false;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "HR_PROFILE_BP_DAO_002");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }
}
