package net.sourceforge.harness.xml.factory;

import net.sourceforge.harness.xml.jaxb.Execution;
import net.sourceforge.harness.xml.jaxb.Java;
import net.sourceforge.harness.xml.jaxb.Os;
import net.sourceforge.harness.xml.jaxb.ResultEnum;
import net.sourceforge.harness.xml.jaxb.TestRun;
import net.sourceforge.harness.xml.jaxb.User;
import org.apache.log4j.Logger;

/**
 * XML 'testRun' element factory. 
 *
 * <p><b>History:</b>
 * <ul>
 *   <li>20020408, Initial file</li>
 * </ul>
 *
 * <p><b>CVS Information:</b><br>
 * <i>
 * $Date: 2002/10/09 13:11:32 $<br>
 * $Revision: 1.3 $<br>
 * $Author: mgl $<br>
 * </i>
 *
 * @author <a href="mailto:mgl@users.sourceforge.net">Marcel Schepers</a>
 */
public class TestRunFactory implements Factory {

    private static final String CLASSNAME = TestRunFactory.class.getName();

    private static final Logger LOGGER = Logger.getLogger(CLASSNAME);

    private static TestRunFactory factory = null;

    private TestRunFactory() {
    }

    /**
   * Creates a new instance of JavaFactory. Using this method ensures 
   * singleton behaviour.
   */
    public static TestRunFactory getInstance() {
        if (factory == null) {
            factory = new TestRunFactory();
        }
        return factory;
    }

    /**
   * creates a new validate XML element.
   *
   * @return a new created XML element.
   */
    public Object createElement() {
        LOGGER.debug("createElement() [IN ]");
        TestRun testRun = new TestRun();
        testRun.setResult(ResultEnum.FAILED);
        testRun.setJava((Java) JavaFactory.getInstance().createElement());
        testRun.setOs((Os) OsFactory.getInstance().createElement());
        testRun.setUser((User) UserFactory.getInstance().createElement());
        testRun.setExecution((Execution) ExecutionFactory.getInstance().createElement());
        LOGGER.debug("createElement() [OUT]");
        LOGGER.debug("  return: " + testRun);
        return testRun;
    }
}
