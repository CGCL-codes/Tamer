package influx.biz.test.listener.impl;

import influx.biz.event.IBizEvent;
import influx.biz.exception.AbortBizEventProcessingException;
import influx.biz.listener.impl.AbstractBizSearchListener;
import influx.biz.phase.IBizSearchPhase;
import influx.biz.test.dao.ITestDAO;
import influx.biz.test.listener.ITestBizSearchKeywordListener;
import influx.biz.test.model.impl.TestPerson;
import influx.dtc.collection.IEntityKeyListDTC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test business listener
 * 
 * @author whoover
 */
public class TestBizSearchKeywordListener extends AbstractBizSearchListener<IEntityKeyListDTC<String, TestPerson>, IBizSearchPhase> implements ITestBizSearchKeywordListener {

    private static final Logger LOG = LoggerFactory.getLogger(TestBizSearchKeywordListener.class);

    /**
	 * {@inheritDoc}
	 */
    public void process(final IBizEvent<IEntityKeyListDTC<String, TestPerson>, IBizSearchPhase> bizEvent) throws AbortBizEventProcessingException {
        LOG.debug("PROCESSING KEYWORD SEARCH BUSINESS LISTENER... " + bizEvent);
        super.autoProcessKeywordSearch(getServiceDAO(ITestDAO.class), bizEvent);
    }
}
