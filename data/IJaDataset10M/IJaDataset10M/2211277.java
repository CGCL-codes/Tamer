package org.koossery.adempiere.dao.impl.pa;

import java.util.ArrayList;
import org.koossery.adempiere.dao.impl.AbstractCommonDAO;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.koossery.adempiere.core.contract.dto.pa.PA_BenchmarkDataDTO;
import org.koossery.adempiere.core.contract.criteria.pa.PA_BenchmarkDataCriteria;
import org.koossery.adempiere.core.backend.interfaces.dao.pa.IPA_BenchmarkDataDAO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.exceptions.KTAdempierePersistenceException;

public class PA_BenchmarkDataDAOImpl extends AbstractCommonDAO implements IPA_BenchmarkDataDAO {

    SqlMapClientTemplate mapper = getMapperInstance();

    public boolean isDuplicate(String name) throws KTAdempiereException {
        int nboccur = -1;
        try {
            PA_BenchmarkDataCriteria criteria = new PA_BenchmarkDataCriteria();
            criteria.setName(name.toLowerCase());
            nboccur = Integer.parseInt(mapper.queryForObject("PA_BenchmarkData.verify", criteria).toString());
            return nboccur <= 0 ? false : true;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "PA_BENCHMARKDATA_DAO_000");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<PA_BenchmarkDataDTO> getPA_BenchmarkData(PA_BenchmarkDataCriteria pA_BenchmarkDataCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<PA_BenchmarkDataDTO>) mapper.queryForList("PA_BenchmarkData.find", pA_BenchmarkDataCriteria);
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "PA_BENCHMARKDATA_DAO_001");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    public boolean update(PA_BenchmarkDataCriteria pA_BenchmarkDataCriteria) throws KTAdempiereException {
        try {
            int result = mapper.update("PA_BenchmarkData.update", pA_BenchmarkDataCriteria);
            return result > 0 ? true : false;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "PA_BENCHMARKDATA_DAO_002");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }
}
