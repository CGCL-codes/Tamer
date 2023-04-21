package balmysundaycandy.scalatool.shared;

import java.util.Date;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

public class LogEnrty {

    /**
	 * line separator.
	 */
    private static final String separator = System.getProperty("line.separator");

    private Key key;

    private String serviceName;

    private String methodName;

    private String request;

    private String response;

    private String callTrace;

    private Date callStart;

    private Date callEnd;

    private long spendTimeMilles;

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getCallTrace() {
        return callTrace;
    }

    public void setCallTrace(String callTrace) {
        this.callTrace = callTrace;
    }

    public Date getCallStart() {
        return callStart;
    }

    public void setCallStart(Date callStart) {
        this.callStart = callStart;
    }

    public Date getCallEnd() {
        return callEnd;
    }

    public void setCallEnd(Date callEnd) {
        this.callEnd = callEnd;
    }

    public long getSpendTimeMilles() {
        return spendTimeMilles;
    }

    public void setSpendTimeMilles(long spendTimeMilles) {
        this.spendTimeMilles = spendTimeMilles;
    }

    public static String getSeparator() {
        return separator;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("serviceName:" + serviceName + separator);
        stringBuilder.append("methodName:" + methodName + separator);
        stringBuilder.append("spendTimeMilles:" + spendTimeMilles + separator);
        stringBuilder.append("request:" + separator + request + separator);
        stringBuilder.append("response:" + separator + response + separator);
        return stringBuilder.toString();
    }
}
