package org.frameworkset.spi.assemble;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.frameworkset.spi.async.annotation.Async;
import org.frameworkset.spi.async.annotation.Constants;
import org.frameworkset.spi.async.annotation.Result;
import org.frameworkset.spi.remote.RPCMethodCall;
import com.frameworkset.orm.annotation.TransactionType;
import com.frameworkset.orm.transaction.TransactionException;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.RegexUtil;

/**
 * <p>Title: SynchronizedMethod</p>
 *
 * <p>Description: ��װͬ�������������Է�����Ϣ</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: ��ɳ�ƴ������ϵͳ�������޹�˾</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class SynchronizedMethod implements java.io.Serializable {

    private static Logger log = Logger.getLogger(SynchronizedMethod.class);

    /**
	 * �����ķ�����ƣ����ָ����pattern���ԣ�������ƾͻ���Ч
	 */
    private String methodName;

    /**
     * ƥ�䷽����Ƶ�������ʽģʽ��
     */
    private String pattern;

    /**
     * �������ʹ��б�
     * List<Pro>
     */
    private List<Pro> params;

    private Map<String, Pro> paramIndexs;

    /**
     * ��������ع��쳣�б�
     * List<RollbackException>
     */
    private List<RollbackException> rollbackExceptions;

    /**
     * �����������쳣��Ϣ�����
     * ���һ���쳣���������쳣�������쳣������Ϊ�����Boolean(true)Ϊ����ֵ
     * ���һ���쳣�����������쳣�������쳣������Ϊ�����Boolean(false)Ϊ����ֵ
     * �´�ϵͳ���б���쳣ʱ��ֱ���Ը�����Ϊ׼�����ڽ��и��ӵ��߼��ж�
     * Map<exceptionClass,Boolean>
     */
    private Map<String, Boolean> rollbackExceptionIndexs;

    private long asynctimeout;

    private Result asyncResult;

    /**
     * ��������
     */
    private TransactionType txtype = TransactionManager.REQUIRED_TRANSACTION;

    public SynchronizedMethod() {
        params = new ArrayList<Pro>();
        paramIndexs = new HashMap<String, Pro>();
        rollbackExceptions = new ArrayList<RollbackException>();
        rollbackExceptionIndexs = new HashMap<String, Boolean>();
    }

    public SynchronizedMethod(Method method, Async async) {
        this.uuid = buildMethodUUID(method);
        this.asynctimeout = async.timeout();
        if (async.callback().equals(Constants.NULLCALLBACK)) this.asyncCallback = null; else this.asyncCallback = async.callback();
        asyncResult = async.result();
    }

    public long getAsyncTimeout() {
        return asynctimeout;
    }

    private String asyncCallback;

    public String getAsyncCallback() {
        return asyncCallback;
    }

    public Result getAsyncResultMode() {
        return asyncResult;
    }

    public SynchronizedMethod(Method method, TransactionType txtype, String[] rollbacksexceptions) {
        rollbackExceptions = new ArrayList<RollbackException>();
        rollbackExceptionIndexs = new HashMap<String, Boolean>();
        this.uuid = buildMethodUUID(method);
        this.setTxtype(txtype);
        if (rollbacksexceptions != null && rollbacksexceptions.length > 0) buildRollbackExceptios(rollbacksexceptions);
    }

    private void buildRollbackExceptios(String[] rollbacksexceptions) {
        if (rollbacksexceptions == null || rollbacksexceptions.length == 0) return;
        for (String e : rollbacksexceptions) {
            if (e.equals("")) continue;
            int i = e.indexOf('@');
            if (i > 0) {
                String exceptionName = e.substring(0, i);
                String exceptionType = e.substring(i + 1);
                RollbackException RollbackException = new RollbackException();
                RollbackException.setExceptionName(exceptionName);
                RollbackException.setExceptionType(exceptionType);
                this.rollbackExceptions.add(RollbackException);
            } else {
                RollbackException RollbackException = new RollbackException();
                RollbackException.setExceptionName(e);
                RollbackException.setExceptionType("INSTANCEOF");
                this.rollbackExceptions.add(RollbackException);
            }
        }
    }

    public static void main(String[] args) {
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void addParam(Pro param) {
        this.params.add(param);
        this.paramIndexs.put(param.getClazz(), param);
    }

    public List getParams() {
        return this.params;
    }

    public List getRollbackExceptions() {
        return this.rollbackExceptions;
    }

    public void addRollbackException(RollbackException rollbackException) {
        this.rollbackExceptions.add(rollbackException);
    }

    public Param getParam(String paramtype) {
        return (Param) this.paramIndexs.get(paramtype);
    }

    private String uuid;

    /**
     * ��ȡ������Ωһ��ʶ
     * @return
     */
    public String getUUID() {
        try {
            if (uuid != null) return uuid;
            if (this.isPattern()) {
                return this.uuid = this.pattern;
            }
            StringBuffer uuid_ = new StringBuffer();
            uuid_.append(this.methodName);
            if (this.params != null) {
                for (int i = 0; i < params.size(); i++) {
                    Pro param = (Pro) params.get(i);
                    uuid_.append("_").append(param.getClazz());
                }
            }
            uuid = uuid_.toString();
            return uuid;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    /**
     * ����������Ωһ��ʶid��һ�����е�һ������ֻ��Ӧһ����ʶ����ʶ��ɵĹ���
     * �ǣ�������+'_' + ��������1 + ... + '_' +  ��������n
     * @param method �������󣬴�����еķ�����Ϣ�������������������������飬��������ֵ���ͣ������쳣���͵ȵ�
     * @param args ��������
     * @return ������ʶ
     */
    public static String buildMethodUUID(RPCMethodCall method_call) {
        try {
            StringBuffer uuid = new StringBuffer();
            Class[] paramTypes = method_call.getTypes();
            uuid.append(method_call.getName());
            if (paramTypes == null || paramTypes.length == 0) {
                return uuid.toString();
            }
            for (int i = 0; i < paramTypes.length; i++) {
                uuid.append("_").append(paramTypes[i].getName());
            }
            return uuid.toString();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            return null;
        }
    }

    /**
     * ����������Ωһ��ʶid��һ�����е�һ������ֻ��Ӧһ����ʶ����ʶ��ɵĹ���
     * �ǣ�������+'_' + ��������1 + ... + '_' +  ��������n
     * @param method �������󣬴�����еķ�����Ϣ�������������������������飬��������ֵ���ͣ������쳣���͵ȵ�
     * @param args ��������
     * @return ������ʶ
     */
    public static String buildMethodUUID(Method method, Object[] args) {
        return buildMethodUUID(method.getName(), method.getParameterTypes());
    }

    /**
     * ����������Ωһ��ʶid��һ�����е�һ������ֻ��Ӧһ����ʶ����ʶ��ɵĹ���
     * �ǣ�������+'_' + ��������1 + ... + '_' +  ��������n
     * @param method �������󣬴�����еķ�����Ϣ�������������������������飬��������ֵ���ͣ������쳣���͵ȵ�
     * @param args ��������
     * @return ������ʶ
     */
    public static String buildMethodUUID(String method, Class[] paramTypes) {
        try {
            StringBuffer uuid = new StringBuffer();
            uuid.append(method);
            if (paramTypes == null || paramTypes.length == 0) {
                return uuid.toString();
            }
            for (int i = 0; i < paramTypes.length; i++) {
                uuid.append("_").append(paramTypes[i].getName());
            }
            return uuid.toString();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            return null;
        }
    }

    public static MethodInfo refactorMehtodfromUUID(String methodUUID) throws ClassNotFoundException {
        int index = methodUUID.lastIndexOf("_");
        MethodInfo methodInfo = new MethodInfo();
        if (index <= 0) {
            methodInfo.setMethodName(methodUUID);
            return methodInfo;
        } else {
            String[] infos = methodUUID.split("_");
            methodInfo.setMethodName(infos[0]);
            Class[] classes = new Class[infos.length - 1];
            for (int i = 1; i < infos.length; i++) {
                classes[i - 1] = BeanAccembleHelper.getClass(infos[i]);
            }
            methodInfo.setParamTypes(classes);
        }
        return methodInfo;
    }

    public static String buildMethodUUID(Method method) {
        return buildMethodUUID(method, null);
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
	 * �жϷ����ǲ���һ��ģʽƥ�䷽��������ǣ�����ģʽ��ƥ����Ҫͬ������Ҫ����������Ƶķ���
	 * 
	 * @return
	 */
    public boolean isPattern() {
        return this.pattern != null && !this.pattern.equals("");
    }

    /**
	 * �жϲ�����ķ����Ƿ���ͬ������ƥ��
	 * �����б����е�ͬ���������Ƿ���ģʽƥ�䷽��
     * û����ֱ�ӹ�������Ωһid
	 * @return true ƥ��
	 *         false ��ƥ��
	 */
    public boolean match(Method method) {
        return match(method, null);
    }

    /**
	 * �жϷ����Ƿ��뵱ǰ����ƥ��
	 * ע�⣺�������Ѿ��������ϰ�ϵͳ������ֱ��ͨ�����ָ��ͬ��������ʹ��ģʽ��������
	 * �ϰ�Ǩ�Ƶ��°�ʱ����Ҫ��ȷ��ָ����������ƺͷ����Ĳ������������
	 * @param method �����������methodΪnull��������ָ���쳣
	 * @param methodUUID ����Ωһ��ʶ
	 * @return true-ƥ����
	 *         false-û��ƥ����
	 *         
	 */
    public boolean match(Method method, String methodUUID) {
        try {
            boolean match = false;
            if (isPattern()) {
                if (pattern.equals("*")) return true;
                String methodname = method.getName();
                match = RegexUtil.isMatch(methodname, this.getPattern());
                return match;
            }
            if (methodUUID == null) methodUUID = SynchronizedMethod.buildMethodUUID(method);
            if (this.getUUID().equals(methodUUID)) {
                match = true;
            }
            return match;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            return false;
        }
    }

    /**
	 * 
	 * @return
	 */
    public TransactionType getTxtype() {
        return txtype;
    }

    /**
	 * 
	 * @param txtype
	 */
    public void setTxtype(String txtype) {
        if (txtype == null) return;
        if (txtype.equals("NEW_TRANSACTION")) {
            this.txtype = TransactionManager.NEW_TRANSACTION;
        } else if (txtype.equals("REQUIRED_TRANSACTION")) {
            this.txtype = TransactionManager.REQUIRED_TRANSACTION;
        } else if (txtype.equals("MAYBE_TRANSACTION")) {
            this.txtype = TransactionManager.MAYBE_TRANSACTION;
        } else if (txtype.equals("NO_TRANSACTION")) {
            this.txtype = TransactionManager.NO_TRANSACTION;
        } else if (txtype.equals("RW_TRANSACTION")) {
            this.txtype = TransactionManager.RW_TRANSACTION;
        }
    }

    public void setTxtype(TransactionType txtype) {
        if (txtype == null) return;
        this.txtype = txtype;
    }

    /**
	 * �ж��쳣�Ƿ�����Ҫ�ع�������쳣
	 * �����쳣����ع���
	 * com.frameworkset.orm.transaction.TransactionException
	 * javax.transaction.RollbackException
	 * java.sql.SQLException
	 * 
	 * ϵͳ������쳣Ҳ����ع���
	 * java.lang.NullpointException
	 * 
	 * �������͵��쳣Ҫ�����Ƿ���rollbackExceptions�У����������Ҫ�ع�����
	 * �����ύ����
	 * �ж��쳣�Ƿ��ǻع������쳣����������
	 * ����1���쳣ֻҪ�ǻع��쳣�б���������쳣�ı���������࣬��Ϊ�ع��쳣
	 * ����2���쳣������
	 * @param throwable
	 * @return
	 */
    public boolean isRollbackException(Throwable throwable) {
        String key = throwable.getClass().getName();
        Boolean t = (Boolean) rollbackExceptionIndexs.get(key);
        if (t != null) {
            return t.booleanValue();
        } else {
            try {
                if ((throwable instanceof TransactionException) || (throwable instanceof javax.transaction.RollbackException) || throwable instanceof java.sql.SQLException || throwable instanceof java.lang.RuntimeException || throwable instanceof java.lang.Error) {
                    t = new Boolean(true);
                    rollbackExceptionIndexs.put(key, t);
                    return true;
                } else if (rollbackExceptions != null && rollbackExceptions.size() > 0) {
                    for (int i = 0; i < this.rollbackExceptions.size(); i++) {
                        RollbackException re = (RollbackException) rollbackExceptions.get(i);
                        if (re.getExceptionType() == RollbackException.TYPE_IMPLEMENTS) {
                            if (throwable.getClass() == re.getExceptionClass()) {
                                t = new Boolean(true);
                                rollbackExceptionIndexs.put(key, t);
                                return true;
                            }
                        } else if (re.getExceptionType() == RollbackException.TYPE_INSTANCEOF) {
                            if (re.getExceptionClass() != null && re.getExceptionClass().isAssignableFrom(throwable.getClass())) {
                                t = new Boolean(true);
                                rollbackExceptionIndexs.put(key, t);
                                return true;
                            }
                        }
                    }
                    t = new Boolean(false);
                    rollbackExceptionIndexs.put(key, t);
                    return false;
                } else {
                    t = new Boolean(true);
                    rollbackExceptionIndexs.put(key, t);
                    return true;
                }
            } catch (Exception e) {
                t = new Boolean(true);
                rollbackExceptionIndexs.put(key, t);
                log.error(e);
                return true;
            }
        }
    }

    public static class MethodInfo implements java.io.Serializable {

        private String methodName;

        private Class[] paramTypes;

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public Class[] getParamTypes() {
            return paramTypes;
        }

        public void setParamTypes(Class[] paramTypes) {
            this.paramTypes = paramTypes;
        }
    }
}
