package org.qtitools.mathassess.tools.qticasbridge.maxima;

import org.qtitools.mathassess.tools.maxima.MaximaTimeoutException;
import org.qtitools.mathassess.tools.qticasbridge.BadQTICASCodeException;
import org.qtitools.mathassess.tools.qticasbridge.TypeConversionException;
import org.qtitools.mathassess.tools.qticasbridge.types.ValueWrapper;
import java.util.Collection;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests {@link QTIMaximaSession#executeGrindOutput(String, Class)} using the sample data
 * provided by {@link MaximaDataBindingSamples#CIRCULAR_EXAMPLES}
 *
 * @author  David McKain
 * @version $Revision: 1955 $
 */
@RunWith(Parameterized.class)
public class QTIMaximaSessionGrindTests {

    @Parameters
    public static Collection<Object[]> data() throws Exception {
        return MaximaDataBindingSamples.CIRCULAR_EXAMPLES;
    }

    private QTIMaximaSession session;

    private final String maximaRepresentation;

    private final ValueWrapper valueWrapper;

    public QTIMaximaSessionGrindTests(String maximaRepresentation, ValueWrapper valueWrapper) {
        this.maximaRepresentation = maximaRepresentation;
        this.valueWrapper = valueWrapper;
    }

    @Test
    public void runTest() throws MaximaTimeoutException, BadQTICASCodeException, TypeConversionException {
        ValueWrapper valueWrapperResult = session.executeGrindOutput(maximaRepresentation, valueWrapper.getClass());
        Assert.assertEquals(valueWrapper, valueWrapperResult);
    }

    @Before
    public void setup() throws MaximaTimeoutException {
        session = new QTIMaximaSession();
        session.open();
    }

    @After
    public void teardown() {
        session.close();
    }
}
