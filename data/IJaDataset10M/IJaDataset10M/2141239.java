package com.seitenbau.testing.shared.aspects;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import junit.framework.Assert;
import org.junit.Before;
import com.seitenbau.testing.shared.config.RepositoryConfig;
import com.seitenbau.testing.shared.dao.DaoFactory;
import com.seitenbau.testing.shared.dao.IDaoFactory;
import com.seitenbau.testing.shared.model.RecordedTest;
import com.seitenbau.testing.shared.tracer.AbstractTracer;
import com.seitenbau.testing.shared.tracer.ITracer;
import com.seitenbau.testing.shared.tracer.ITracerWithListener;
import com.seitenbau.testing.shared.tracer.IUnderTrace;
import com.seitenbau.testing.shared.tracer.listener.ConsoleListener;

public class TracerBaseHelper {

    protected static RecordedTest myRecordedData;

    @Before
    public void setUp() throws Exception {
        Assert.assertTrue("No Aspect active start with: -javaagent:libs/aspectjweaver.jar", this instanceof IUnderTrace);
        myRecordedData = null;
    }

    public static ITracer createTracer() {
        ITracerWithListener tracer = new AbstractTracer() {

            public IDaoFactory getDaoFactory() {
                return DaoFactory.create((RepositoryConfig) null);
            }

            protected RecordedTest createRecordedTest() {
                return getDaoFactory().getRecordedTestDao().create();
            }

            protected void saveRecordedTest(RecordedTest theRecordedTest) {
                myRecordedData = getRecordedTest();
            }
        };
        tracer.addTracerListener(new ConsoleListener());
        return tracer;
    }

    public static IDaoFactory getDaoFactory() {
        return DaoFactory.create((RepositoryConfig) null);
    }

    public RecordedTest getCurrentRecordedTest() {
        return myRecordedData;
    }

    protected void validateVoidNoParameter(Class<?> clazz, String method) {
        assertNotNull(myRecordedData);
        assertEquals(method, myRecordedData.getMethodName());
        String name = myRecordedData.getClassName().replace("$", ".");
        assertEquals(clazz.getCanonicalName(), name);
        assertEquals(myRecordedData.isVoidMethod(), true);
        assertNotNull(myRecordedData.getArgumentTypes());
        assertEquals(myRecordedData.getArgumentTypes().length, 0);
    }
}
