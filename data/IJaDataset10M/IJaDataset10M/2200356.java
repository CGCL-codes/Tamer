package org.koossery.adempiere.dao.impl.hr;

import java.util.ArrayList;
import org.koossery.adempiere.dao.impl.AbstractCommonDAO;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.koossery.adempiere.core.contract.dto.hr.HR_JobDTO;
import org.koossery.adempiere.core.contract.criteria.hr.HR_JobCriteria;
import org.koossery.adempiere.core.backend.interfaces.dao.hr.IHR_JobDAO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.exceptions.KTAdempierePersistenceException;

public class HR_JobDAOImpl extends AbstractCommonDAO implements IHR_JobDAO {

    SqlMapClientTemplate mapper = getMapperInstance();

    public boolean isDuplicate(String name) throws KTAdempiereException {
        int nboccur = -1;
        try {
            HR_JobCriteria criteria = new HR_JobCriteria();
            criteria.setName(name.toLowerCase());
            nboccur = Integer.parseInt(mapper.queryForObject("HR_Job.verify", criteria).toString());
            return nboccur <= 0 ? false : true;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "HR_JOB_DAO_000");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<HR_JobDTO> getHR_Job(HR_JobCriteria hR_JobCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<HR_JobDTO>) mapper.queryForList("HR_Job.find", hR_JobCriteria);
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "HR_JOB_DAO_001");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    public boolean update(HR_JobCriteria hR_JobCriteria) throws KTAdempiereException {
        try {
            int result = mapper.update("HR_Job.update", hR_JobCriteria);
            return result > 0 ? true : false;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "HR_JOB_DAO_002");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }
}
