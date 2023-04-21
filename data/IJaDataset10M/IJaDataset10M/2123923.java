package org.koossery.adempiere.dao.impl.Requisition_to_invoice.RfQ_Topic;

import java.util.ArrayList;
import org.koossery.adempiere.core.backend.interfaces.dao.Requisition_to_invoice.RfQ_Topic.IC_RfQ_TopicSubscriberDAO;
import org.koossery.adempiere.core.contract.criteria.Requisition_to_invoice.RfQ_Topic.C_RfQ_TopicSubscriberCriteria;
import org.koossery.adempiere.core.contract.dto.Requisition_to_invoice.RfQ_Topic.C_RfQ_TopicSubscriberDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.exceptions.KTAdempierePersistenceException;
import org.koossery.adempiere.dao.impl.AbstractCommonDAO;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class C_RfQ_TopicSubscriberDAOImpl extends AbstractCommonDAO implements IC_RfQ_TopicSubscriberDAO {

    SqlMapClientTemplate mapper = getMapperInstance();

    public boolean isDuplicate(C_RfQ_TopicSubscriberCriteria criteria) throws KTAdempiereException {
        try {
            return false;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "C_RFQ_TOPICSUBSCRIBER_DAO_000");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<C_RfQ_TopicSubscriberDTO> getC_RfQ_TopicSubscriber(C_RfQ_TopicSubscriberCriteria c_RfQ_TopicSubscriberCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<C_RfQ_TopicSubscriberDTO>) mapper.queryForList("C_RfQ_TopicSubscriber.find", c_RfQ_TopicSubscriberCriteria);
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "C_RFQ_TOPICSUBSCRIBER_DAO_001");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }

    public boolean update(C_RfQ_TopicSubscriberCriteria c_RfQ_TopicSubscriberCriteria) throws KTAdempiereException {
        try {
            int result = mapper.update("C_RfQ_TopicSubscriber.update", c_RfQ_TopicSubscriberCriteria);
            return result > 0 ? true : false;
        } catch (Exception e) {
            KTAdempierePersistenceException ktape = new KTAdempierePersistenceException(getErrorMessageXmlFileName(), "C_RFQ_TOPICSUBSCRIBER_DAO_002");
            logger.error(ktape.getHumanMessage(), e);
            throw ktape;
        }
    }
}
