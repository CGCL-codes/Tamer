package com.lavans.lremote.connector.impl;

import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.lavans.lremote.connector.Connector;

public class AsyncConnectWrapper implements Connector {

    /** logger */
    private static Log logger = LogFactory.getLog(GroupConnector.class.getName());

    /** deleagte */
    private Connector connector;

    public AsyncConnectWrapper(Connector connector) {
        this.connector = connector;
    }

    public Object execute(String className, String methodName, Class<?>[] paramTypes, Object[] args) throws Exception {
        TimerTask task = new AsyncTimerTask(className, methodName, paramTypes, args);
        Timer timer = new Timer();
        timer.schedule(task, 1);
        return null;
    }

    private class AsyncTimerTask extends TimerTask {

        private String className;

        private String methodName;

        private Class<?>[] paramTypes;

        private Object[] args;

        /**
		 * constructor.
		 *
		 * @param className
		 * @param methodName
		 * @param paramTypes
		 * @param args
		 */
        public AsyncTimerTask(String className, String methodName, Class<?>[] paramTypes, Object[] args) {
            this.className = className;
            this.methodName = methodName;
            this.paramTypes = paramTypes;
            this.args = args;
        }

        /**
		 * execution method
		 */
        public void run() {
            try {
                connector.execute(className, methodName, paramTypes, args);
            } catch (Exception e) {
                logger.error("Aync execution is failed.", e);
            }
        }

        ;
    }
}
