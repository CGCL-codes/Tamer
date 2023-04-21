package uk.icat3.sessionbeans.interceptor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import org.apache.log4j.Logger;
import uk.icat3.sessionbeans.user.UserSession;
import uk.icat3.sessionbeans.util.QueueNames;

/**
 *
 * @author scb24683
 */
public class DownloadInterceptor {

    static Queue queue;

    static QueueConnectionFactory cf;

    static Logger log;

    @EJB
    UserSession user;

    @AroundInvoke
    public Object checkArguments(InvocationContext ctx) throws Exception {
        log = Logger.getLogger(DownloadInterceptor.class);
        Object[] args = ctx.getParameters();
        String className = ctx.getTarget().getClass().getSimpleName();
        log.trace("Class name: " + className);
        String methodName = ctx.getMethod().getName();
        log.trace("Method: " + methodName);
        try {
            String sessionId = (String) args[0];
            String userId = user.getUserIdFromSessionId(sessionId);
            Timestamp time = new Timestamp(new Date().getTime());
            cf = (QueueConnectionFactory) new InitialContext().lookup(QueueNames.CONNECTION_FACTORY);
            queue = (Queue) new InitialContext().lookup(QueueNames.DOWNLOAD_QUEUE);
            log.trace("Queue: " + queue.getQueueName());
            QueueConnection connection = cf.createQueueConnection();
            QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            QueueSender sender = session.createSender(queue);
            if (methodName.equals("downloadDataset")) {
                TextMessage datasetMsg = session.createTextMessage(args[1].toString());
                datasetMsg = (TextMessage) PropertySetter.setProperties(datasetMsg, sessionId, userId, methodName, time);
                sender.send(datasetMsg);
            } else {
                ObjectMessage downloadMsg = session.createObjectMessage();
                downloadMsg = (ObjectMessage) PropertySetter.setProperties(downloadMsg, sessionId, userId, methodName, time);
                ArrayList<Long> ids = new ArrayList<Long>();
                if (args[1] instanceof Long) {
                    log.debug("====Datafile id: " + args[1].toString());
                    ids.add(new Long(args[1].toString()));
                } else if (args[1] instanceof Collection) {
                    @SuppressWarnings("unchecked") Collection<Long> fileIds = (Collection<Long>) args[1];
                    ids.addAll(fileIds);
                }
                downloadMsg.setObject(ids);
                sender.send(downloadMsg);
            }
            session.close();
        } catch (Exception e) {
            log.fatal("Error in DownloadInterceptor", e);
        }
        return ctx.proceed();
    }
}
