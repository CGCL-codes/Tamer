package phsperformance.data;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Vector;
import phsperformance.util.PhsSerialization;
import phsperformance.util.TextUtil;
import phsperformance.util.data.RequestCollection;
import phsperformance.util.data.RequestInfo;

public class SubmitTask extends Task {

    private static long m_nexecution = 0;

    private static String m_id = null;

    private RequestCollection m_collection;

    public SubmitTask(Map paramMap, InstanceInfo instanceInfo) throws Exception {
        super(paramMap, instanceInfo);
        String filename = getParam("obsfilename").toString();
        m_collection = RequestCollection.recoverFromProperties(filename);
        if (m_id == null) {
            m_id = TextUtil.getHostname() + "_" + Long.toHexString(System.currentTimeMillis());
        }
    }

    @Override
    public synchronized TaskResult execute() {
        TaskResult result = null;
        String server = getServerHost();
        int port = getServerPort();
        String worker = getInstanceWorker();
        ClassLoader classLoader = getInstanceClassloader();
        m_nexecution++;
        String title = m_id + "_" + m_nexecution;
        getModel().addDeferredOperation(new DeferredOperation(title, "submit"));
        String programme = getParam("programme", "KPOT");
        String username = getUserName();
        String password = getUserPasswd();
        try {
            double ra = 12.3;
            double dec = 23.3;
            Vector observations = new Vector();
            for (RequestInfo reqInfo : m_collection.getRequests()) {
                Object request = PhsSerialization.createObservationRequest(classLoader, reqInfo, ra, dec);
                observations.add(request);
            }
            Object proposal = PhsSerialization.createProposal(classLoader, title, null, programme, observations);
            Object user = PhsSerialization.createUser(classLoader, username, password);
            Vector out = PhsSerialization.doSubmit(user, proposal, server, port, worker, classLoader);
            String message = null;
            Class execStatusClass = Class.forName("edu.caltech.ipac.util.ExecStatus", false, classLoader);
            Object es = out.get(0);
            Method isOk = execStatusClass.getMethod("isOK", null);
            Boolean resultIsOk = (Boolean) isOk.invoke(es, null);
            if (resultIsOk.booleanValue()) {
                message = "<html><table><tr><td style=\"color: green;\">Sucess</td><td>Check the deferred report</td></tr></table></html>";
            } else {
                Method getException = execStatusClass.getMethod("getException", null);
                message = "<html><table><tr><td style=\"color: red;\">Failure</td>td>" + TextUtil.wrapText(getException.invoke(es, null).toString(), 40) + "</td></tr></table></html>";
            }
            result = new SuccessTaskResult(message);
        } catch (Exception e) {
            result = new ExceptionTaskResult(e.getMessage());
        }
        return result;
    }
}
