package org.qtitools.mathassess.tools.qticasbridge.maxima;

import org.qtitools.mathassess.tools.qticasbridge.types.ValueWrapper;
import java.util.Collection;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests {@link QTIMaximaSession#executeStringOutput(String, boolean, Class)} using the sample data
 * provided by {@link MaximaDataBindingSamples#CIRCULAR_EXAMPLES}
 *
 * @author  David McKain
 * @version $Revision: 2428 $
 */
@RunWith(Parameterized.class)
public class QTIMaximaSessionExecuteStringCircularTests extends QTIMaximaSessionTestBase {

    @Parameters
    public static Collection<Object[]> data() throws Exception {
        return MaximaDataBindingSamples.CIRCULAR_EXAMPLES;
    }

    private final String maximaRepresentation;

    private final ValueWrapper valueWrapper;

    public QTIMaximaSessionExecuteStringCircularTests(String maximaRepresentation, ValueWrapper valueWrapper) {
        this.maximaRepresentation = maximaRepresentation;
        this.valueWrapper = valueWrapper;
    }

    @Test
    public void runTest() throws Exception {
        ValueWrapper valueWrapperResult = session.executeStringOutput(maximaRepresentation, false, valueWrapper.getClass());
        Assert.assertEquals(valueWrapper, valueWrapperResult);
    }
}
