package org.tanso.fountain.admin.manager.adminserver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.tanso.fountain.admin.manager.AdminMessage;
import org.tanso.fountain.admin.manager.AdminMessageBaseControlContent;
import org.tanso.fountain.admin.manager.TenantBean;
import org.tanso.fountain.admin.manager.hosts.AdminHosts;
import org.tanso.fountain.interfaces.func.comm.IMessage;
import org.tanso.fountain.interfaces.func.comm.INotificationService;
import org.tanso.fountain.interfaces.func.comm.ISubscriber;
import org.tanso.ts.mts.MTSMessage;
import com.google.gson.Gson;

/**
 * This class defines all adminserver's functions' implementations
 * @author Lingwan Zeng
 *
 */
public class AdminServer implements ISubscriber {

    private AdminMessage message;

    private INotificationService ns;

    private String dirPath;

    public static String myStringSeparator1 = "*";

    public static String myStringSeparator2 = "_";

    public static String myStringSeparator3 = "*";

    public Logger log;

    /**
	 * Set INotificationService instance
	 * 
	 * @param ns
	 *            INotificationService instance
	 */
    public void setNS(INotificationService ns) {
        this.ns = ns;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public AdminMessage getMessage() {
        return message;
    }

    public AdminServer() {
    }

    /**
	 * Set filePath where to store admin response from managers
	 * 
	 * @param dirpath
	 */
    public void setFilePath(String dirpath) {
        this.dirPath = dirpath;
    }

    /**
	 * Subscribe subjects
	 * 
	 */
    public void initPubSubService() {
        ns.subscribe("tanso_response_orm_tansoadmin", this);
        ns.subscribe("tanso_response_arm_tansoadmin", this);
        ns.subscribe("tanso_response_trm_tansoadmin", this);
        ns.subscribe("tanso_response_arm_tenantadmin", this);
    }

    public void initLocalResources() {
        initPubSubService();
    }

    /**
	 * receive mts message from web server and send them to managers by pub/sub
	 * services
	 */
    public void messageHandler(Object msg) {
        String msgContent = ((MTSMessage) msg).getContent();
        log.debug(msgContent);
        log.debug(((MTSMessage) msg).getSourceIP() + "--" + ((MTSMessage) msg).getSourcePort());
        int firstPointIndex = msgContent.indexOf(myStringSeparator1);
        String msgType = msgContent.substring(0, firstPointIndex);
        if (msgType.equalsIgnoreCase("control")) {
            message = constructAdminMessage(msgContent);
            log.debug("Message Published, Topic is:" + "tanso_request_" + message.getMessageSourceRole() + "_" + message.getMessageDestRole());
            log.debug("Content is:" + message.toString());
            ns.publish("tanso_request_" + message.getMessageSourceRole() + "_" + message.getMessageDestRole(), message);
        }
    }

    /**
	 * Construct a AdminMessage according to mts msg content, this AdminMessage
	 * would be used to transfer admin information among managers
	 * 
	 * @param msgContent
	 * @return
	 */
    public AdminMessage constructAdminMessage(String msgContent) {
        String msgType = null;
        String instruction = null;
        String role = null;
        String from = null;
        String to = null;
        String taskId = null;
        StringTokenizer token = new StringTokenizer(msgContent, myStringSeparator1);
        int i = 0;
        while (token.hasMoreTokens()) {
            try {
                String s = token.nextToken();
                switch(i) {
                    case 0:
                        msgType = s;
                        break;
                    case 1:
                        instruction = s;
                        break;
                    case 2:
                        role = s;
                        break;
                    case 3:
                        from = s;
                        break;
                    case 4:
                        to = s;
                        break;
                    case 5:
                        taskId = s;
                        break;
                    default:
                        break;
                }
                i++;
            } catch (NoSuchElementException e) {
                break;
            }
        }
        log.debug("***" + "msgType -- " + msgType + "***");
        log.debug("***" + "instruction -- " + instruction + "***");
        log.debug("***" + "role -- " + role + "***");
        log.debug("***" + "from -- " + from + "***");
        log.debug("***" + "to -- " + to + "***");
        log.debug("***" + "taskId -- " + taskId + "***");
        AdminMessage adminmessage = new AdminMessage();
        adminmessage.setMessageType(msgType);
        adminmessage.setMessageSourceRole(from);
        adminmessage.setMessageId(taskId);
        if (to.contains("trm")) {
            adminmessage.setMessageDestRole("trm");
            adminmessage.setMessageDestId(to.substring(to.indexOf("_") + 1));
        } else if (to.contains("adrm")) {
            adminmessage.setMessageDestRole("adrm");
            adminmessage.setMessageDestId(to.substring(to.indexOf("_") + 1));
        } else {
            adminmessage.setMessageDestRole(to);
        }
        if (msgType.equalsIgnoreCase("control")) {
            AdminMessageBaseControlContent controlcontent = new AdminMessageBaseControlContent();
            controlcontent.setControlInstruction(instruction);
            if (instruction.equalsIgnoreCase("uploadfile")) {
                controlcontent = setUploadFileRole(role);
            } else if (instruction.equalsIgnoreCase("undeployapp")) {
                controlcontent.setControlObjectRole("app");
                controlcontent.setControlObjectId(role);
            } else if (instruction.equalsIgnoreCase("showservers")) {
                controlcontent.setControlObjectRole("ASD");
                controlcontent.setControlObjectId(role);
            } else if (instruction.equalsIgnoreCase("createtenant")) {
                String tenantid = role.substring(0, role.indexOf("_"));
                int maxNodeNum = Integer.parseInt(role.substring(role.indexOf("_") + 1));
                log.debug("***********new tenant id is **********" + tenantid + "***");
                log.debug("***********max node num is **********" + maxNodeNum + "***");
                TenantBean newTenant = new TenantBean(role.substring(0, role.indexOf("_")), Integer.parseInt(role.substring(role.indexOf("_") + 1)));
                Gson gson = new Gson();
                controlcontent.setControlObjectId(gson.toJson(newTenant));
            } else {
                if (role.contains("node")) {
                    controlcontent.setControlObjectRole("node");
                    log.debug("***" + "nodeId -- " + role.substring(role.indexOf("_") + 1) + "***");
                    controlcontent.setControlObjectId(role.substring(role.indexOf("_") + 1));
                } else if (role.contains("tenant")) {
                    controlcontent.setControlObjectRole("tenant");
                    log.debug("***" + "tenantId -- " + role.substring(role.indexOf("_") + 1) + "***");
                    controlcontent.setControlObjectId(role.substring(role.indexOf("_") + 1));
                } else if (role.contains("server")) {
                    controlcontent.setControlObjectRole("server");
                    controlcontent.setControlObjectId(role.substring(role.indexOf("_") + 1));
                }
            }
            adminmessage.setControlContent(controlcontent);
        } else if (msgType.equalsIgnoreCase("data")) {
        }
        return adminmessage;
    }

    /**
	 * Set elements o f AdminMessageBaseControlContent according to content of element "role"
	 * @param role
	 * @return
	 */
    public AdminMessageBaseControlContent setUploadFileRole(String role) {
        AdminMessageBaseControlContent controlContent = new AdminMessageBaseControlContent();
        String tenantId = null;
        String appName = null;
        String instanceNum = null;
        String filePath = null;
        String welcomePage = null;
        StringTokenizer token = new StringTokenizer(role, myStringSeparator2);
        int i = 0;
        while (token.hasMoreTokens()) {
            try {
                String s = token.nextToken();
                switch(i) {
                    case 0:
                        tenantId = s;
                        break;
                    case 1:
                        appName = s;
                        break;
                    case 2:
                        instanceNum = s;
                        break;
                    case 3:
                        filePath = s;
                        break;
                    case 4:
                        welcomePage = s;
                    default:
                        break;
                }
                i++;
            } catch (NoSuchElementException e) {
                break;
            }
        }
        log.debug("***" + "tenantId -- " + tenantId + "***");
        log.debug("***" + "appName -- " + appName + "***");
        log.debug("***" + "instanceNum -- " + instanceNum + "***");
        log.debug("***" + "filePath -- " + filePath + "***");
        log.debug("***" + "welcomePage -- " + welcomePage + "***");
        StringBuilder controlId = new StringBuilder(20);
        String fileFtpPath = String.format("ftp:%s:21:%s:%s:%s", AdminHosts.HOST_FTPSERVER, AdminHosts.FTP_USERNAME, AdminHosts.FTP_PWD, filePath);
        controlId.append(tenantId).append(myStringSeparator3).append(appName).append(myStringSeparator3).append(fileFtpPath).append(myStringSeparator3).append(instanceNum).append(myStringSeparator3).append(welcomePage);
        log.debug("Object id of uploadedfile is : " + controlId.toString());
        controlContent.setControlInstruction("uploadfile");
        controlContent.setControlObjectRole("app");
        controlContent.setControlObjectId(controlId.toString());
        return controlContent;
    }

    public void messageReceiver() {
        messageReceiver();
    }

    public void responseSender() {
        responseSender();
    }

    public void msgNotify(String topic, IMessage msg) {
        log.info("topic is " + topic);
        log.info("content is " + ((AdminMessage) msg).toString());
        if (topic.equalsIgnoreCase("tanso_response_orm_tansoadmin")) {
            responseHandlerOfTansoAdmin(msg);
        } else if (topic.equalsIgnoreCase("tanso_response_arm_tansoadmin")) {
            responseHandlerOfTansoAdmin(msg);
        } else if (topic.equalsIgnoreCase("tanso_response_trm_tansoadmin")) {
            responseHandlerOfTansoAdmin(msg);
        } else if (topic.equalsIgnoreCase("tanso_response_arm_tenantadmin")) {
            responseHandlerOfTansoAdmin(msg);
        }
    }

    /**
	 * Handle admin response from managers.Here admin is tanso admin,and
	 * managers are orm and arm.It saves response result into a file whose name
	 * consisit of admin request id.WebServer will got this result file later on
	 * using the admin request id it knows.
	 * 
	 * @param msg
	 *            msg got from pub/sub system,the response result
	 */
    public void responseHandlerOfTansoAdmin(Object msg) {
        AdminMessage adminmessage = (AdminMessage) msg;
        log.debug("adminmessage is : " + adminmessage.toString());
        try {
            writeToFile(adminmessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Write result into File
	 * 
	 * @param adminmessage
	 *            Message got from pub/sub system, the response result
	 * @throws IOException
	 *             Caused by file io
	 */
    public void writeToFile(AdminMessage adminmessage) throws IOException {
        String resultContent;
        if (adminmessage.getMessageType().equalsIgnoreCase("control")) {
            resultContent = adminmessage.getControlContent().getControlStatus();
        } else {
            resultContent = adminmessage.getDataContent().getDataContentInStringFormat();
        }
        String taskId = adminmessage.getMessageId();
        String filePath = dirPath + taskId + ".txt";
        log.debug("filePath is " + filePath);
        File f = new File(filePath);
        f.createNewFile();
        FileWriter fw = new FileWriter(f);
        fw.write(resultContent);
        fw.close();
    }
}
