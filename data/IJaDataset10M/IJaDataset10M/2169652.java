package org.jtestcase.core.mapping;

import java.util.Iterator;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.jtestcase.JTestCase;
import org.jtestcase.JTestCaseException;
import org.jtestcase.TestCaseInstance;
import org.jtestcase.core.converter.ComplexTypeConverter;
import org.jtestcase.core.digester.Digester;
import org.jtestcase.core.digester.DigesterException;
import org.jtestcase.core.type.TestCaseInstanceType;

public class TestCaseInstanceBuilder {

    public TestCaseInstanceBuilder(JTestCase jtestcase, Digester digester, ComplexTypeConverter converter) {
        this._jtestcase = jtestcase;
        this.digester = digester;
        this.typeConverter = converter;
    }

    /**
	 * JTestCase to which this digester belongs
	 */
    private JTestCase _jtestcase;

    /**
	 * The digester used for parsing the XML
	 */
    private Digester digester;

    /**
	 * Type convert facility. This class is used to map "string" representation
	 * of type to contrete instances of the type represented
	 */
    private ComplexTypeConverter typeConverter;

    /**
	 * Logger (log4j)
	 */
    private Logger log = Logger.getLogger(TestCaseInstanceBuilder.class);

    public Vector getTestCasesInstances(String className, String methodName) throws JTestCaseException {
        Vector testcases;
        try {
            testcases = digester.getTestCases(className, methodName);
        } catch (DigesterException e) {
            throw new JTestCaseException(e.getMessage());
        }
        Vector testcaseInstances = new Vector();
        log.debug("getting testcase instances ");
        for (Iterator itor = testcases.iterator(); itor.hasNext(); ) {
            TestCaseInstanceType tcit = (TestCaseInstanceType) itor.next();
            log.debug("*class: " + tcit.getClassName());
            log.debug("*method: " + tcit.getMethodName());
            log.debug("*test case: " + tcit.getTestcaseName());
            testcaseInstances.add(new TestCaseInstance(tcit.getClassName(), tcit.getMethodName(), tcit.getTestcaseName(), _jtestcase));
        }
        return testcaseInstances;
    }
}
