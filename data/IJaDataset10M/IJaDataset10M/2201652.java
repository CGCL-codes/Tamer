package org.dcm4chex.arr.mbean;

import java.security.Principal;
import java.text.MessageFormat;
import java.util.Date;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.dcm4che.server.ServerFactory;
import org.dcm4che.server.SyslogService;
import org.dcm4che.server.UDPServer;
import org.dcm4che.util.HostNameUtils;
import org.dcm4chex.arr.ejb.session.StoreAuditRecordLocal;
import org.dcm4chex.arr.ejb.session.StoreAuditRecordLocalHome;
import org.jboss.security.SecurityAssociation;
import org.jboss.system.ServiceMBeanSupport;

/**
 * <description>
 * 
 * @author <a href="mailto:gunter@tiani.com">gunter zeilinger </a>
 * @author <a href="mailto:joseph@tiani.com">joseph foraci </a>
 * @since February 13, 2003
 * @version $Revision: 4476 $
 */
public class ARRServer extends ServiceMBeanSupport implements SyslogService {

    private static final String START = "Start";

    private static final String STOP = "Stop";

    private static final String ACTOR_START_STOP = "<IHEYr4>" + "<ActorStartStop>" + "<ActorName>{0}</ActorName>" + "<ApplicationAction>{1}</ApplicationAction>" + "<User><LocalUser>{2}</LocalUser></User>" + "</ActorStartStop>" + "<Host>{3}</Host>" + "<TimeStamp>{4,date,yyyy-MM-dd'T'HH:mm:ss.SSS}</TimeStamp>" + "</IHEYr4>";

    private ServerFactory sf = ServerFactory.getInstance();

    private UDPServer udpsrv = sf.newUDPServer(sf.newSyslogHandler(this));

    private StoreAuditRecordLocalHome storeHome = null;

    private String actorName = "ARR";

    public final String getActorName() {
        return actorName;
    }

    public final void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public final int getPort() {
        return udpsrv.getPort();
    }

    public final void setPort(int port) {
        udpsrv.setPort(port);
    }

    public String toString() {
        return udpsrv.toString();
    }

    public void process(Date date, String host, String content) {
        store(content);
    }

    private void store(String content) {
        StoreAuditRecordLocal bean;
        try {
            bean = getStoreAuditRecordHome().create();
        } catch (CreateException e) {
            throw new EJBException(e);
        }
        try {
            bean.store(content);
        } finally {
            try {
                bean.remove();
            } catch (Exception ignore) {
            }
        }
    }

    public void startService() throws Exception {
        storeHome = null;
        udpsrv.start();
        store(buildActorStartStopAuditMessage(START));
    }

    public String getCurrentPrincipalName() {
        Principal p = SecurityAssociation.getPrincipal();
        return p != null ? p.getName() : System.getProperty("user.name");
    }

    private String buildActorStartStopAuditMessage(String action) {
        Object[] arguments = { actorName, action, getCurrentPrincipalName(), HostNameUtils.getLocalHostName(), new Date() };
        return MessageFormat.format(ACTOR_START_STOP, arguments);
    }

    public void stopService() throws Exception {
        udpsrv.stop();
        store(buildActorStartStopAuditMessage(STOP));
    }

    private StoreAuditRecordLocalHome getStoreAuditRecordHome() {
        if (storeHome != null) {
            return storeHome;
        }
        Context jndiCtx = null;
        try {
            jndiCtx = new InitialContext();
            StoreAuditRecordLocalHome home = (StoreAuditRecordLocalHome) jndiCtx.lookup(StoreAuditRecordLocalHome.JNDI_NAME);
            return home;
        } catch (NamingException e) {
            throw new EJBException(e);
        } finally {
            if (jndiCtx != null) {
                try {
                    jndiCtx.close();
                } catch (NamingException ignore) {
                }
            }
        }
    }

    public String getLocalAddress() {
        return udpsrv.getLocalAddress();
    }

    public void setLocalAddress(String laddrStr) {
        udpsrv.setLocalAddress(laddrStr);
    }

    public int getMaxPacketSize() {
        return udpsrv.getMaxPacketSize();
    }

    public void setMaxPacketSize(int maxPacketSize) {
        udpsrv.setMaxPacketSize(maxPacketSize);
    }

    public int getReceiveBufferSize() {
        return udpsrv.getReceiveBufferSize();
    }

    public void setReceiveBufferSize(int receiveBufferSize) {
        udpsrv.setReceiveBufferSize(receiveBufferSize);
    }

    public boolean isRunning() {
        return udpsrv.isRunning();
    }

    public Date getLastStartedAt() {
        return udpsrv.getLastStartedAt();
    }

    public Date getLastStoppedAt() {
        return udpsrv.getLastStoppedAt();
    }
}
