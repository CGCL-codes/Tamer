package com.rapidlogix.agent.java;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import com.rapidlogix.agent.java.config.JavaConfigManager;
import com.rapidlogix.agent.sync.SyncManager;
import com.rapidlogix.agent.sync.objects.OperationParam;
import com.rapidlogix.agent.sync.objects.Transaction;
import com.rapidlogix.agent.sync.objects.TransactionParam;
import com.rapidlogix.agent.variable.DefaultVariable;
import com.rapidlogix.agent.variable.VariableIntervals;
import com.rapidlogix.agent.variable.VariableUnits;
import com.rapidlogix.agent.variable.java.JvmVariables;
import com.rapidlogix.agent.variable.java.TransactionVariable;
import com.rapidlogix.agent.variable.java.VariableUtils;

public class HttpRequestAspectUtils {

    public static final String TOTAL_TRANSACTION_COUNT = "Total transaction count";

    public static final String TRANSACTION_COUNT = "Transaction count";

    public static final String RESPONSE_TIME = "Response time";

    public static final String SCOPE_APP = "Application";

    public static final String TRANS_HTTP_JAVA = "http-java";

    public static final String HEADER_REFERER = "Referer";

    public static final String PARAM_URL = "URL";

    public static final String PARAM_REFERER = "Referer";

    private static Pattern transactionPattern = null;

    public static DefaultVariable getCountVariable(String scope) {
        DefaultVariable variable = (DefaultVariable) VariableUtils.getVariable(TRANSACTION_COUNT, scope);
        if (variable == null) {
            variable = new DefaultVariable();
            variable.setId(SyncManager.getInstance().generateNextId());
            variable.setName(TRANSACTION_COUNT);
            variable.setScope(scope);
            variable.setUnit(VariableUnits.UNIT_COUNT);
            variable.setInterval(VariableIntervals.INTERVAL_MINUTE);
            variable.setOrder(2);
            VariableUtils.addVariable(variable);
        }
        return variable;
    }

    public static TransactionVariable getTransactionVariable(String scope) {
        TransactionVariable variable = (TransactionVariable) VariableUtils.getVariable(RESPONSE_TIME, scope);
        if (variable == null) {
            variable = new TransactionVariable();
            variable.setId(SyncManager.getInstance().generateNextId());
            variable.setName(RESPONSE_TIME);
            variable.setScope(scope);
            variable.setUnit(VariableUnits.UNIT_MILLISECOND);
            variable.setOrder(1);
            VariableUtils.addVariable(variable);
        }
        return variable;
    }

    public static String buildTransactionScope(JoinPoint.StaticPart staticJoinPoint, HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        if (transactionPattern == null) {
            transactionPattern = Pattern.compile(JavaConfigManager.getInstance().getConfig().getTransactionScope());
        }
        Matcher matcher = transactionPattern.matcher(requestUrl);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return requestUrl;
        }
    }

    public static Transaction buildHttpRequest(JoinPoint.StaticPart staticJoinPoint, HttpServletRequest request, HttpServletResponse response) {
        Transaction transaction = new Transaction();
        transaction.setName(TRANS_HTTP_JAVA);
        return transaction;
    }

    public static void buildHttpParams(HttpServletRequest request, List params) {
        TransactionParam param = new TransactionParam();
        param.setId(SyncManager.getInstance().generateNextId());
        param.setName(PARAM_URL);
        StringBuffer dataBuf = new StringBuffer();
        dataBuf.append(request.getRequestURL());
        if (request.getQueryString() != null) {
            dataBuf.append("?");
            dataBuf.append(request.getQueryString());
        }
        param.setValue(dataBuf.toString());
        params.add(param);
        if (request.getHeader(HEADER_REFERER) != null) {
            param = new TransactionParam();
            param.setId(SyncManager.getInstance().generateNextId());
            param.setName(PARAM_REFERER);
            param.setValue(request.getHeader(PARAM_REFERER));
            params.add(param);
        }
    }
}
