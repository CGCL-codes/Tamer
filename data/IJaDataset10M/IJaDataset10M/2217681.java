package de.icehorsetools.iceoffice.service.test;

import java.util.List;
import de.icehorsetools.dataAccess.objects.Participant;
import de.icehorsetools.dataAccess.objects.Test;

/**
 * @author tkr
 * @version $Id: ITestSv.java 351 2009-07-27 16:05:06Z kruegertom $
 */
public interface ITestSv {

    /**
	 * initialize a new {@link Test} with standard values
	 *
	 * @param test
	 * @return
	 */
    public Test initNewTest(Test test);

    /**
	 * read the next number from database = highest number +1
	 *
	 * @return
	 */
    public Integer getNextTestNumber();

    /**
	 * validates whether the given {@link Participant} is valid for the given {@link Test}
	 * @return true, if valid
	 */
    public boolean isTestValidForParticipant(Test test, Participant participant);

    /**
   * validates whether the given {@link Test} is available for the given {@link Participant}
   * @return true, if valid
   */
    public boolean isTestAvailableForParticipant(Test test, Participant participant);

    public List getTestCombinations();

    public List getTestCombinationsTests();
}
