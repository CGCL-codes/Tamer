package org.kaleidofoundry.core.naming;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.kaleidofoundry.core.config.ConfigurationFactory;
import org.kaleidofoundry.core.context.ProviderException;
import org.kaleidofoundry.core.store.ResourceException;
import org.kaleidofoundry.core.util.ThrowableHelper;

/**
 * @author Jerome RADUGET
 */
public class NamingServiceJndiSampler extends AbstractJavaSamplerClient {

    private static final String ConfigurationUri = "configurationUri";

    private static final String EchoMessage = "echoMessage";

    @Override
    public void setupTest(final JavaSamplerContext context) {
        try {
            ConfigurationFactory.provides("myConfig", context.getParameter(ConfigurationUri));
        } catch (ProviderException rte) {
            throw rte;
        }
    }

    @Override
    public void teardownTest(final JavaSamplerContext context) {
        try {
            ConfigurationFactory.unregister("myConfig");
        } catch (ResourceException rse) {
            throw new IllegalStateException(rse);
        }
    }

    @Override
    public SampleResult runTest(final JavaSamplerContext context) {
        SampleResult parentResults = new SampleResult();
        SampleResult childResults;
        boolean mainSampleStatusOK = true;
        String userMessage = context.getParameter(EchoMessage);
        NamingServiceJndiSample01 sample;
        parentResults.sampleStart();
        parentResults.setThreadName("naming-service");
        parentResults.setSampleLabel("naming-service-sampler");
        childResults = new SampleResult();
        childResults.sampleStart();
        childResults.setSampleLabel("context injection");
        sample = new NamingServiceJndiSample01();
        childResults.sampleEnd();
        childResults.setResponseCodeOK();
        childResults.setSuccessful(true);
        parentResults.addSubResult(childResults);
        childResults = new SampleResult();
        try {
            childResults.sampleStart();
            childResults.setSampleLabel("echoFromEJB");
            childResults.setResponseMessage(sample.echoFromEJB(userMessage));
            childResults.sampleEnd();
            childResults.setResponseCodeOK();
            childResults.setSuccessful(true);
        } catch (Throwable th) {
            childResults.setResponseMessage(ThrowableHelper.getStackTrace(th));
            childResults.setSuccessful(false);
            mainSampleStatusOK = false;
        }
        parentResults.addSubResult(childResults);
        childResults = new SampleResult();
        try {
            childResults.sampleStart();
            childResults.setSampleLabel("echoFromDatabase");
            childResults.setResponseMessage(sample.echoFromDatabase(userMessage));
            childResults.sampleEnd();
            childResults.setResponseCodeOK();
            childResults.setSuccessful(true);
        } catch (Throwable th) {
            childResults.setResponseMessage(ThrowableHelper.getStackTrace(th));
            childResults.setSuccessful(false);
            mainSampleStatusOK = false;
        }
        parentResults.addSubResult(childResults);
        childResults = new SampleResult();
        try {
            childResults.sampleStart();
            childResults.setSampleLabel("echoFromJMS");
            childResults.setResponseMessage(sample.echoFromJMS(userMessage).getJMSCorrelationID());
            childResults.sampleEnd();
            childResults.setResponseCodeOK();
            childResults.setSuccessful(true);
        } catch (Throwable th) {
            childResults.setResponseMessage(ThrowableHelper.getStackTrace(th));
            childResults.setSuccessful(false);
            mainSampleStatusOK = false;
        }
        parentResults.addSubResult(childResults);
        parentResults.setSuccessful(mainSampleStatusOK);
        if (mainSampleStatusOK) {
            parentResults.setResponseCodeOK();
        }
        return parentResults;
    }

    @Override
    public Arguments getDefaultParameters() {
        Arguments args = new Arguments();
        args.addArgument(ConfigurationUri, "classpath:/naming/myContext.properties");
        args.addArgument(EchoMessage, "Hello world!");
        return args;
    }
}
