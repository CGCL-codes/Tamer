package ch.unibas.jmeter.snmp.expect.sampler;

import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;
import ch.unibas.jmeter.snmp.expect.helper.ExpectSpawnHandler;
import expectj.Spawn;
import expectj.TimeoutException;

public class ExpectSampler extends AbstractSampler implements TestBean {

    private String executable;

    private String expectString;

    private String sendString;

    private long timeout;

    private static final long serialVersionUID = 8731471978101948335L;

    public SampleResult sample(Entry e) {
        SampleResult result = new SampleResult();
        boolean isOK = false;
        result.setSampleLabel(getName());
        result.sampleStart();
        isOK = expect(result);
        result.sampleEnd();
        result.setSuccessful(isOK);
        result.setResponseCode(isOK ? "200" : "500");
        return result;
    }

    private boolean expect(SampleResult result) {
        if (getSpawn() == null) {
            result.setResponseMessage("Expect spawn is null");
            return false;
        }
        try {
            if (!StringUtils.isBlank(expectString)) {
                getSpawn().expect(expectString, timeout);
            }
            getSpawn().send(sendString);
            result.setResponseMessage(getSpawn().getCurrentStandardOutContents());
        } catch (IOException e) {
            result.setResponseMessage("Expect got IOException: " + e.getMessage());
            return false;
        } catch (TimeoutException e) {
            result.setResponseMessage("Expect got Timeout: " + e.getMessage());
            return false;
        }
        return true;
    }

    public void setExpectString(String expectString) {
        this.expectString = expectString;
    }

    public String getExpectString() {
        return expectString;
    }

    public void setSendString(String sendString) {
        this.sendString = sendString;
    }

    public String getSendString() {
        return sendString;
    }

    public void setTimeout(String timeout) {
        try {
            this.timeout = Long.parseLong(timeout);
        } catch (NumberFormatException e) {
            this.timeout = -1;
        }
    }

    public String getTimeout() {
        return "" + timeout;
    }

    public void setExecutable(String executable) {
        this.executable = executable;
    }

    public String getExecutable() {
        return executable;
    }

    public Spawn getSpawn() {
        return ExpectSpawnHandler.getSpawn(executable);
    }
}
