package influx.biz.test.phase;

import influx.biz.phase.IBizSearchPhase;

/**
 * Test search phase
 * 
 * @author whoover
 */
public interface ITestSearchPhase extends IBizSearchPhase {

    /**
	 * Gets a relational business model phase that can be used for comparison operations (null if there is no direct relationship to the business model phase)
	 * 
	 * @return a relational business model phase that can be used for comparison operations (null if there is no direct relationship to the business model phase)
	 */
    public IBizSearchPhase getBizSearchPhase();
}
