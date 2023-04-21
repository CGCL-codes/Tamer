package org.tanso.fountain.admin.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.StringTokenizer;
import org.tanso.fountain.admin.resource.tomcat6.SigTomcat6;
import org.tanso.fountain.admin.resource.tomcat6.Tomcat6ManagerProxy;
import org.tanso.fountain.admin.resource.tomcat6.Tomcat6ManagerStub;
import org.tanso.fountain.interfaces.func.admin.IASMgmt;
import org.tanso.fountain.interfaces.func.admin.IAppDeploy;
import org.tanso.fountain.interfaces.func.admin.INodeInfo;
import org.tanso.fountain.interfaces.func.admin.IResourceMgmt;
import org.tanso.fountain.interfaces.func.comm.IMessage;
import org.tanso.fountain.interfaces.func.comm.INotificationService;
import org.tanso.fountain.interfaces.func.comm.ISubscriber;
import org.tanso.fountain.interfaces.platform.ComponentBase;
import org.tanso.fountain.interfaces.platform.ComponentContext;
import org.tanso.fountain.util.rmi.PlatformRMI;

/**
 * This is resource manager which manages the application servers and node
 * resources.
 * 
 * @author Haiping Huang
 * 
 */
public class ResourceManager extends ComponentBase implements IResourceMgmt, ISubscriber {

    private static final String SERVER_CONFIG_PROPS = "/resourcedefs.properties";

    private Properties serverProp;

    private INotificationService pubsub;

    private String managementTopic;

    private int serverIdCount = 0;

    private int proxyIdCount = 0;

    private String localNodeId;

    private org.apache.log4j.Logger logger;

    public void setPubSub(INotificationService pubsub) {
        this.pubsub = pubsub;
    }

    public void unsetPubSub(String providerName) {
        this.pubsub.unsubscribe(managementTopic, this);
        this.pubsub = null;
    }

    /**
	 * Get properties from the config file
	 * 
	 */
    private void getProperties() {
        if (serverProp != null) {
            return;
        }
        InputStream is;
        try {
            is = this.getClass().getResourceAsStream(SERVER_CONFIG_PROPS);
            serverProp = new Properties();
            serverProp.load(is);
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public IASMgmt getLocalASMgmt(String ASType) {
        if (ASType.equals(IASMgmt.TOMCAT_6)) {
            if (null == serverProp) {
                logger.info("No properties for AS specified!");
                return null;
            }
            Tomcat6ManagerStub tomcat6Instance;
            try {
                tomcat6Instance = new Tomcat6ManagerStub(localNodeId);
            } catch (RemoteException e) {
                e.printStackTrace();
                return null;
            }
            if (tomcat6Instance.init("tomcat6@" + serverIdCount++ + "@" + this.getComponentContext().getLocalNodeId(), super.platformLog, super.platformPortManager) && tomcat6Instance.config(serverProp)) {
                if (null != tomcat6Instance.bindToRmiRegistry(PlatformRMI.getPlatformRMIPort())) {
                    return tomcat6Instance;
                } else {
                    logger.info("Bind RmiRegiestry failed!");
                    return null;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public IASMgmt getRemoteASMgmt(INodeInfo node, String ASType) {
        if (ASType.equals(IASMgmt.TOMCAT_6)) {
            Tomcat6ManagerProxy tomcat6Instance = new Tomcat6ManagerProxy(localNodeId);
            if (tomcat6Instance.init("tomcat6proxy." + proxyIdCount++ + "@" + this.getComponentContext().getLocalNodeId(), node.getNodeId(), super.platformLog, super.platformPortManager, this.pubsub) && tomcat6Instance.bindToServer()) {
                return tomcat6Instance;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean activate(ComponentContext componentContext) {
        this.logger = super.platformLog.getLogger(this.getClass());
        localNodeId = componentContext.getLocalNodeId();
        getProperties();
        managementTopic = componentContext.getLocalNodeId() + ".ResourceManager";
        this.pubsub.subscribe(managementTopic, this);
        return true;
    }

    @Override
    public boolean deactivate() {
        if (pubsub != null) {
            this.pubsub.unsubscribe(managementTopic, this);
        }
        localNodeId = null;
        return true;
    }

    @Override
    public Object getService(Object requestor, String interfaceName) {
        if (interfaceName.equals(IResourceMgmt.class.getCanonicalName())) {
            return this;
        } else {
            return null;
        }
    }

    public void msgNotify(String topic, IMessage msg) {
        if (topic.equals(this.managementTopic)) {
            if (msg instanceof SigTomcat6) {
                try {
                    SigTomcat6 sig = (SigTomcat6) msg;
                    if (sig.getSig() == SigTomcat6.ACT_GETSERVER) {
                        String contactTopic = sig.getContactTopic();
                        String replyContent = sig.getContent();
                        StringTokenizer token = new StringTokenizer(sig.getContent(), SigTomcat6.SIG_SEPARATOR);
                        int tid = Integer.parseInt(token.nextToken());
                        Tomcat6ManagerStub newServer = (Tomcat6ManagerStub) this.getLocalASMgmt(IASMgmt.TOMCAT_6);
                        String bindName = null;
                        if (newServer == null) {
                            logger.info("Get Local AS Mgmt failed!");
                        } else {
                            bindName = newServer.getServerBindName();
                        }
                        sig.setSig(SigTomcat6.ACT_GETSERVER_RESPONSE);
                        sig.setContactTopic(this.managementTopic);
                        if (null != bindName) {
                            replyContent = tid + SigTomcat6.SIG_SEPARATOR + bindName;
                            sig.setContent(replyContent);
                            sig.setReturnCode(1);
                        } else {
                            sig.setReturnCode(0);
                        }
                        this.pubsub.publish(contactTopic, sig);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                logger.error("Unknown message type: " + msg.getClass().getCanonicalName());
            }
        } else {
            logger.error("Unhandled topic: " + topic);
        }
    }

    public IAppDeploy createAppDeploy(String tenantId, String appName, String welcomePage, String repoInfo) {
        return new TansoAppDeploy(tenantId, appName, welcomePage, repoInfo);
    }
}
