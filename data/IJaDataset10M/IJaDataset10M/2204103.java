package cn.myapps.core.workflow.notification.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.document.ejb.DocumentProcessBean;
import cn.myapps.core.dynaform.pending.ejb.PendingVO;
import cn.myapps.core.homepage.ejb.ReminderProcess;
import cn.myapps.core.homepage.ejb.ReminderProcessBean;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.notification.dao.NotificationDAO;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRT;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRTProcessBean;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHISProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHISProcessBean;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.RuntimeDaoManager;

public class NotificationProcessBean extends AbstractRunTimeProcessBean implements NotificationProcess {

    private BillDefiProcess billDefiProcess;

    private ActorRTProcess actorRTProcess;

    private DocumentProcess documentProcess;

    private ReminderProcess reminderProcess;

    private Map flowTemp;

    public NotificationProcessBean(String applicationId) {
        super(applicationId);
        try {
            flowTemp = new HashMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected IRuntimeDAO getDAO() throws Exception {
        return new RuntimeDaoManager().getNotificationDAO(getConnection(), getApplicationId());
    }

    /**
	 * 通知流程送出人
	 * 
	 * @param doc
	 * @param pending
	 * @param flow
	 * @param user
	 * @throws Exception
	 */
    public void notifySender(Document doc, PendingVO pending, BillDefiVO flow, WebUser user) throws Exception {
        FlowStateRT stateRT = doc.getState();
        if (stateRT != null) {
            Collection nodeRTs = stateRT.getNoderts();
            for (Iterator iterator = nodeRTs.iterator(); iterator.hasNext(); ) {
                NodeRT nodeRT = (NodeRT) iterator.next();
                Collection userList = nodeRT.getUserList();
                if (userList.isEmpty()) {
                    continue;
                }
                Node el = flow.toFlowDiagram().getNodeByID(nodeRT.getNodeid());
                NotificationCreator creator = new NotificationCreator(el);
                Notification notification = creator.createSendNotification(user, getApplicationId());
                notification.setResponsibles(userList);
                notification.setDocument(doc);
                notification.send();
            }
        }
    }

    /**
	 * 流程审批后通知当前审批者
	 */
    public void notifyCurrentAuditors(Document doc, PendingVO pending, BillDefiVO flow) throws Exception {
        FlowStateRT stateRT = doc.getState();
        if (stateRT != null && pending != null) {
            Collection nodeRTs = stateRT.getNoderts();
            for (Iterator iterator = nodeRTs.iterator(); iterator.hasNext(); ) {
                NodeRT nodeRT = (NodeRT) iterator.next();
                Collection userList = nodeRT.getUserList();
                if (userList.isEmpty()) {
                    continue;
                }
                Node el = flow.toFlowDiagram().getNodeByID(nodeRT.getNodeid());
                NotificationCreator creator = new NotificationCreator(el);
                Notification notification = creator.createArriveNotification(getApplicationId());
                notification.setResponsibles(userList);
                notification.setDocument(doc);
                notification.send();
            }
        }
    }

    /**
	 * 通知被回退者
	 * 
	 * @param doc
	 * @param flow
	 * @throws Exception
	 */
    public void notifyRejectees(Document doc, PendingVO pending, BillDefiVO flow) throws Exception {
        FlowStateRT stateRT = doc.getState();
        if (stateRT != null) {
            Collection nodeRTs = stateRT.getNoderts();
            for (Iterator iterator = nodeRTs.iterator(); iterator.hasNext(); ) {
                NodeRT nodeRT = (NodeRT) iterator.next();
                Collection userList = new ArrayList();
                for (Iterator iterator2 = nodeRT.getPendingActorRTList().iterator(); iterator2.hasNext(); ) {
                    ActorRT actorRT = (ActorRT) iterator2.next();
                    userList.addAll(actorRT.getAllUser());
                }
                if (userList.isEmpty()) {
                    continue;
                }
                Node el = flow.toFlowDiagram().getNodeByID(nodeRT.getNodeid());
                NotificationCreator creator = new NotificationCreator(el);
                Notification notification = creator.createRejectNotification((UserVO) doc.getAuthor(), getApplicationId());
                notification.setResponsibles(userList);
                notification.setDocument(doc);
                notification.send();
            }
        }
    }

    public void notifyOverDueAuditors() throws Exception {
        HashSet unique = new HashSet();
        Collection notifications = getOverDueNotifications();
        for (Iterator iterator = notifications.iterator(); iterator.hasNext(); ) {
            Notification notification = (Notification) iterator.next();
            notification.send();
            if (notification.getDocument() != null) {
                String docId = notification.getDocument().getId();
                String flowId = notification.getDocument().getFlowid();
                String key = docId + flowId;
                if (notification.isSended() && unique.add(key)) {
                    addReminderCount(docId, flowId);
                }
            }
        }
    }

    /**
	 * 在最后一条历史记录中增加提醒次数，以标记当前节点超时提醒次数
	 * 
	 * @throws Exception
	 */
    public void addReminderCount(String docid, String flowid) throws Exception {
        RelationHISProcess relationProcess = new RelationHISProcessBean(getApplicationId());
        RelationHIS relationHIS = relationProcess.doViewLast(docid, flowid);
        relationHIS.setReminderCount(relationHIS.getReminderCount() + 1);
        relationProcess.doUpdate(relationHIS);
    }

    /**
	 * 获取超时的通知
	 * 
	 * @return 超时通知集合
	 * @throws Exception
	 */
    private Collection getOverDueNotifications() throws Exception {
        List notifications = new ArrayList();
        Date curDate = new Date();
        Collection pendingInfo = ((NotificationDAO) getDAO()).queryOverDuePending(curDate, getApplicationId());
        for (Iterator iterator = pendingInfo.iterator(); iterator.hasNext(); ) {
            Map info = (Map) iterator.next();
            List userList = getUserList(info);
            Notification notification = createOverDueNotification(info, curDate);
            notification.setResponsibles(userList);
            notifications.add(notification);
        }
        return notifications;
    }

    /**
	 * 获取当前审批用户列表
	 * 
	 * @param info
	 *            待办信息
	 * @return 文档
	 * @throws Exception
	 */
    private List getUserList(Map info) throws Exception {
        String id = (String) info.get("actorrtid");
        ActorRT actorrt = (ActorRT) getActorRTProcess().doView(id);
        if (actorrt != null) {
            return actorrt.getAllUser();
        }
        return new ArrayList();
    }

    /**
	 * 创建超时通知
	 * 
	 * @param info
	 *            待办信息
	 * @param curDate
	 *            当前日期
	 * @return
	 * @throws Exception
	 */
    private Notification createOverDueNotification(Map info, Date curDate) throws Exception {
        String nodeid = (String) info.get("nodeid");
        String flowid = (String) info.get("flowid");
        String docid = (String) info.get("id");
        Date deadline = (Date) info.get("deadline");
        BillDefiVO flow = (BillDefiVO) flowTemp.get(flowid);
        if (!flowTemp.containsKey(flowid)) {
            flow = (BillDefiVO) getBillDefiProcess().doView(flowid);
        }
        return createOverDueNotification(flow, docid, nodeid, curDate, deadline);
    }

    /**
	 * 创建超期通知
	 * 
	 * @param flow
	 *            流程
	 * @param docid
	 *            文档ID
	 * @param nodeid
	 *            节点ID
	 * @param curDate
	 *            当前日期
	 * @param deadline
	 *            最后限期
	 * @return
	 * @throws Exception
	 */
    private Notification createOverDueNotification(BillDefiVO flow, String docid, String nodeid, Date curDate, Date deadline) throws Exception {
        DocumentProcess dp = getDocumentProcess();
        Node el = flow.toFlowDiagram().getNodeByID(nodeid);
        NotificationCreator creator = new NotificationCreator(el);
        Notification notification = creator.createOverDueNotification(curDate, deadline, getApplicationId());
        Document document = (Document) dp.doView(docid);
        notification.setDocument(document);
        return notification;
    }

    public DocumentProcess getDocumentProcess() {
        if (documentProcess == null) {
            documentProcess = new DocumentProcessBean(getApplicationId());
        }
        return documentProcess;
    }

    public ReminderProcess getReminderProcess() {
        if (reminderProcess == null) {
            reminderProcess = new ReminderProcessBean();
        }
        return reminderProcess;
    }

    public BillDefiProcess getBillDefiProcess() throws Exception {
        if (billDefiProcess == null) {
            billDefiProcess = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);
        }
        return billDefiProcess;
    }

    public ActorRTProcess getActorRTProcess() {
        if (actorRTProcess == null) {
            actorRTProcess = new ActorRTProcessBean(getApplicationId());
        }
        return actorRTProcess;
    }
}
