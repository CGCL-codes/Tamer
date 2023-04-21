package influx.biz.test.listener;

import influx.biz.listener.IBizListener;
import influx.biz.phase.IBizSearchPhase;
import influx.biz.test.model.impl.TestPerson;
import influx.dtc.collection.IEntityKeyListDTC;

/**
 * Test business listener
 * 
 * @author whoover
 */
public interface ITestBizSearchKeywordListener extends IBizListener<IEntityKeyListDTC<String, TestPerson>, IBizSearchPhase> {
}
