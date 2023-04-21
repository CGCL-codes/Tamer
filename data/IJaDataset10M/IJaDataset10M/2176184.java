package cn.myapps.core.workflow.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.log4j.Logger;
import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.constants.MobileConstant;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.FlowState;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.element.AbortNode;
import cn.myapps.core.workflow.element.AutoNode;
import cn.myapps.core.workflow.element.CompleteNode;
import cn.myapps.core.workflow.element.FlowDiagram;
import cn.myapps.core.workflow.element.ManualNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.element.SuspendNode;
import cn.myapps.core.workflow.element.TerminateNode;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.dao.NodeRTDAO;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowHistory;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHISProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.DefaultProperty;

/**
 * 
 * @author Marky
 * 
 */
public class StateMachineHelper {

    private static Logger LOG = Logger.getLogger(StateMachineHelper.class);

    public boolean isDisplySubmit = false;

    public boolean isDisplyFlow = true;

    private int flowState;

    private String contextPath;

    private static NodeRTProcess getNodeRTProcess(String applicationId) throws Exception {
        return StateMachine.getNodeRTProcess(applicationId);
    }

    static BillDefiProcess getBillDefiProcess() throws Exception {
        return StateMachine.getBillDefiProcess();
    }

    private static RelationHISProcess getRelationHISProcess(String applicationId) throws Exception {
        return StateMachine.getRelationHISProcess(applicationId);
    }

    public StateMachineHelper() {
    }

    public StateMachineHelper(Document doc) {
        Environment evt = Environment.getInstance();
        contextPath = evt.getContextPath();
        initFlowImage(doc.getFlowid(), doc.getId(), evt);
        try {
            if (doc.getState() != null) {
                flowState = doc.getState().getState();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * 初始化流程图
	 * 
	 * @param flowid
	 * @param docid
	 * @param request
	 */
    private void initFlowImage(String flowid, String docid, Environment evt) {
        try {
            String path = DefaultProperty.getProperty("BILLFLOW_DIAGRAMPATH");
            String imgPath = evt.getRealPath(path + "/" + docid + ".jpg");
            File file = new File(imgPath);
            if (!file.exists()) {
                BillDefiVO flowVO = (BillDefiVO) getBillDefiProcess().doView(flowid);
                if (flowVO != null) {
                    FlowDiagram fd = flowVO.toFlowDiagram();
                    StateMachine.toJpegImage(docid, fd, evt);
                    StateMachine.toFlowImage(docid, fd, evt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * 根据当前用户获取当前流程所有开始节点的下一个手动节点(第一个节点)的列表
	 * 
	 * @param flowid
	 * @param user
	 * 
	 * @return
	 * @throws Exception
	 */
    public Collection getFirstNodeList(String flowid, WebUser user) throws Exception {
        BillDefiVO flowVO = (BillDefiVO) getBillDefiProcess().doView(flowid);
        return StateMachine.getFirstNodeList(flowVO, user);
    }

    /**
	 * 根据第一个节点获取开始节点
	 * 
	 * @param flowid
	 * @param firstNode
	 * @return
	 * @throws Exception
	 */
    public Collection getStartNodeListByFirstNode(String flowid, Node firstNode) throws Exception {
        BillDefiVO flowVO = (BillDefiVO) getBillDefiProcess().doView(flowid);
        return StateMachine.getStartNodeListByFirstNode(flowVO, firstNode);
    }

    /**
	 * 根据当前结点id获取下一结点列表
	 * 
	 * @param docid
	 *            当前文档 id
	 * @param flowid
	 *            流程 Id
	 * @param currid
	 *            当前结点id
	 * @param user
	 * @return 当前节点id获取下一节点列表
	 * @throws Exception
	 */
    public Collection getNextToNodeList(String docid, String flowid, String currid, WebUser user) throws Exception {
        BillDefiVO flowVO = null;
        flowVO = (BillDefiVO) getBillDefiProcess().doView(flowid);
        return StateMachine.getNextToNodeList(flowVO, currid);
    }

    /**
	 * 根据当前节点id获取回退节点列表
	 * 
	 * @param doc
	 *            TODO
	 * @param currid
	 * @param user
	 * 
	 * @return
	 * @throws Exception
	 */
    public Collection getBackToNodeList(Document doc, String currid, WebUser user) throws Exception {
        return getBackToNodeList(doc, currid, user, 0);
    }

    public Collection getBackToNodeList(Document doc, String currid, WebUser user, int flowState) throws Exception {
        BillDefiVO flowVO = null;
        flowVO = (BillDefiVO) getBillDefiProcess().doView(doc.getFlowid());
        return StateMachine.getBackToNodeList(doc, flowVO, currid, user, flowState);
    }

    /**
	 * 获取当前文档流程状态
	 * 
	 * @param docid
	 *            Document id
	 * @param flowid
	 *            文档流程 id
	 * @return 当前文档流程状态
	 * @throws Exception
	 */
    public static int getCurrFlowState(String docid, String flowid, String applicationId) throws Exception {
        return StateMachine.getCurrFlowState(docid, flowid, applicationId);
    }

    /**
	 * 获取当前结点
	 * 
	 * @param flowid
	 * @param nodeid
	 * @return
	 * @throws Exception
	 */
    public static Node getCurrNode(String flowid, String nodeid) throws Exception {
        BillDefiVO flowVO = null;
        BillDefiProcess process = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);
        flowVO = (BillDefiVO) process.doView(flowid);
        return StateMachine.getCurrNode(flowVO, nodeid);
    }

    /**
	 * 获取所有运行时节点
	 * 
	 * @param doc
	 *            TODO
	 * 
	 * @return
	 * @throws Exception
	 */
    public static Collection getAllNodeRT(Document doc, String applicationId) throws Exception {
        if (doc != null && doc.getState() != null) {
            return doc.getState().getNoderts();
        }
        return new ArrayList();
    }

    /**
	 * 获取当前流程所有历史记录
	 * 
	 * @param docid
	 * @param flowid
	 * @return
	 * @throws Exception
	 */
    public static Collection getAllRelationHIS(String docid, String flowid, String applicationId) throws Exception {
        return getRelationHISProcess(applicationId).doQuery(docid, flowid);
    }

    /**
	 * 根据进行挂起操作的用户获取运行时节点
	 * 
	 * @param doc
	 *            TODO
	 * 
	 * @return 根据进行挂起操作的用户,运行时的节点
	 * @throws Exception
	 */
    public NodeRT getSuspendNodeRT(Document doc, String applicationId) throws Exception {
        RelationHIS relationHIS = getRelationHISProcess(applicationId).doViewLast(doc.getId(), doc.getFlowid());
        Collection actorhissList = relationHIS.getActorhiss();
        ActorHIS actorhis = (ActorHIS) actorhissList.iterator().next();
        Collection nodertList = getAllNodeRT(doc, applicationId);
        Collection newNodertList = new ArrayList();
        for (Iterator iter = nodertList.iterator(); iter.hasNext(); ) {
            NodeRT nodert = (NodeRT) iter.next();
            Collection actorrtList = nodert.getActorrts();
            if (actorrtList.size() == 1) {
                newNodertList.add(nodert);
            }
        }
        if (newNodertList != null) {
            for (Iterator iter = newNodertList.iterator(); iter.hasNext(); ) {
                NodeRT nodert = (NodeRT) iter.next();
                ActorRT actort = (ActorRT) nodert.getActorrts().iterator().next();
                if ((actorhis.getActorid()).equals(actort.getActorid())) {
                    return nodert;
                }
            }
        }
        return null;
    }

    /**
	 * 判断当前用户是否可编辑文档. 此实现为通过当前用户、此Document与相应的流程获取当前用户节点是否为空 或根据此Document id
	 * 与相应的流程id来获取当前流程状态是否为0(FlowState.START)作为判断. 若返回true代表用户可以对文档编辑,否则不可以.
	 * 
	 * @param doc
	 * 
	 * @param webUser
	 *            当前用户
	 * 
	 * @return 如果是流程处理者则返回true,否则返回false
	 * @throws Exception
	 */
    public static boolean isDocEditUser(final Document doc, WebUser webUser) throws Exception {
        Document flowDoc = doc;
        if (doc == null) return false;
        if (doc.getParent() != null) {
            flowDoc = doc.getParent();
        }
        return isRunning(StateMachine.getCurrUserNodeRT(flowDoc, webUser), flowDoc.getStateInt());
    }

    private static boolean isRunning(NodeRT nodert, int flowState) {
        return nodert != null || flowState == FlowState.START;
    }

    /**
	 * 返回的字符串为重定义后的XML，表达显示当前用户运行时节点
	 * 
	 * @param flowid
	 *            flow id
	 * @param docid
	 *            Document id
	 * @param webUser
	 *            webuser
	 * @return 字符串为显示当前用户运行时节点
	 * @throws Exception
	 */
    public String toFlowXMLText(Document doc, WebUser webUser) throws Exception {
        StringBuffer buffer = new StringBuffer();
        BillDefiVO flowVO = null;
        String docid = doc.getId();
        String flowid = doc.getFlowid();
        flowVO = (BillDefiVO) getBillDefiProcess().doView(flowid);
        NodeRT nodert = null;
        FlowDiagram fd = null;
        if (flowVO != null) {
            fd = flowVO.toFlowDiagram();
            nodert = StateMachine.getCurrUserNodeRT(doc, webUser);
        }
        Node currnode = null;
        if (nodert != null) {
            String currnodeid = nodert.getNodeid();
            if (currnodeid != null) {
                currnode = (Node) fd.getElementByID(currnodeid);
            }
            buffer.append("<").append(MobileConstant.TAG_FORM).append(" ");
            buffer.append(" ").append(MobileConstant.ATT_TITLE).append("='").append("{*[Workflow]*}").append("'>");
            buffer.append("<").append(MobileConstant.TAG_WORKFLOW).append(">");
            buffer.append("<").append(MobileConstant.TAG_HIDDENFIELD).append(" ").append(MobileConstant.ATT_NAME).append("='_currid' >" + currnodeid + "</").append(MobileConstant.TAG_HIDDENFIELD).append(">");
            if (flowState == FlowState.RUNNING) {
                isDisplySubmit = true;
                Collection nextNodeList = this.getNextToNodeList(docid, flowid, currnode.id, webUser);
                if (nextNodeList != null && nextNodeList.size() > 0) {
                    Iterator it3 = nextNodeList.iterator();
                    StringBuffer buf1 = new StringBuffer();
                    StringBuffer buf2 = new StringBuffer();
                    while (it3.hasNext()) {
                        Node nextNode = (Node) it3.next();
                        Node node = getCurrNode(flowid, nodert.getNodeid());
                        boolean issplit = false;
                        boolean isgather = false;
                        if (node != null && node instanceof ManualNode) {
                            issplit = ((ManualNode) node).issplit;
                            isgather = ((ManualNode) node).isgather;
                        }
                        boolean flag = false;
                        if (isgather) {
                            Collection nodertList = getAllNodeRT(doc, webUser.getApplicationid());
                            if (nodertList != null) {
                                for (Iterator iter = nodertList.iterator(); iter.hasNext(); ) {
                                    NodeRT nrt = (NodeRT) iter.next();
                                    if (!nrt.getNodeid().equals(nodert.getNodeid())) {
                                        Node nd = (Node) fd.getElementByID(nrt.getNodeid());
                                        Node currNode = (Node) fd.getElementByID(nodert.getNodeid());
                                        Collection followTo = fd.getAllFollowNodeOnPath(nd.id);
                                        if (followTo != null && followTo.contains(currNode)) {
                                            flag = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (!flag) {
                            boolean isOthers = false;
                            String id = "next";
                            String flowOperation = FlowType.RUNNING2RUNNING_NEXT;
                            if (!(nextNode instanceof ManualNode)) {
                                isOthers = true;
                                id = "other";
                                if (nextNode instanceof SuspendNode) {
                                    flowOperation = FlowType.RUNNING2SUSPEND;
                                } else if (nextNode instanceof AbortNode) {
                                    flowOperation = FlowType.RUNNING2ABORT;
                                } else if (nextNode instanceof TerminateNode) {
                                    flowOperation = FlowType.RUNNING2TERMIATE;
                                } else if (nextNode instanceof CompleteNode) {
                                    flowOperation = FlowType.RUNNING2COMPLETE;
                                }
                            }
                            if (issplit) {
                                buf1.append("<").append(MobileConstant.TAG_OPTION).append("");
                                buf1.append(" ").append(MobileConstant.ATT_VALUE).append("='" + nextNode.id + "' >");
                                buf1.append(nextNode.name);
                                buf1.append("</").append(MobileConstant.TAG_OPTION).append(">");
                            } else {
                                buf2.append("<").append(MobileConstant.TAG_OPTION).append("");
                                buf2.append(" ").append(MobileConstant.ATT_VALUE).append("='" + nextNode.id + "' >");
                                buf2.append(nextNode.name);
                                buf2.append("</").append(MobileConstant.TAG_OPTION).append(">");
                            }
                        }
                    }
                    if (buf1.toString().trim().length() > 0) {
                        buffer.append("<").append(MobileConstant.TAG_CHECKBOXFIELD).append("  ").append(MobileConstant.ATT_NAME).append("='_nextids'>" + buf1.toString() + "</").append(MobileConstant.TAG_CHECKBOXFIELD).append(">");
                    }
                    if (buf2.toString().trim().length() > 0) {
                        buffer.append("<").append(MobileConstant.TAG_RADIOFIELD).append("  ").append(MobileConstant.ATT_NAME).append("='_nextids'>");
                        buffer.append(buf2);
                        buffer.append("</").append(MobileConstant.TAG_RADIOFIELD).append(">");
                    }
                }
                Collection backNodeList = getBackToNodeList(doc, currnode.id, webUser);
                if (backNodeList != null && backNodeList.size() > 0) {
                    buffer.append("<").append(MobileConstant.TAG_SELECTFIELD).append(" ").append(MobileConstant.ATT_NAME).append("='_nextids'>");
                    buffer.append("<").append(MobileConstant.TAG_OPTION).append(" ").append(MobileConstant.ATT_VALUE).append("=''>");
                    buffer.append("{*[Please]*} {*[Choose]*}");
                    buffer.append("</").append(MobileConstant.TAG_OPTION).append(">");
                    for (Iterator iter = backNodeList.iterator(); iter.hasNext(); ) {
                        Node backNode = (Node) iter.next();
                        buffer.append("<").append(MobileConstant.TAG_OPTION).append(" ").append(MobileConstant.ATT_VALUE).append("='" + backNode.id + "'>");
                        buffer.append(backNode.name);
                        buffer.append("</").append(MobileConstant.TAG_OPTION).append(">");
                    }
                    buffer.append("</").append(MobileConstant.TAG_SELECTFIELD).append(">");
                }
            } else if (flowState == FlowState.SUSPEND && currnode instanceof SuspendNode) {
                Collection backNodeList = this.getBackToNodeList(doc, currnode.id, webUser, FlowState.SUSPEND);
                backNodeList = StateMachine.removeDuplicateNode(backNodeList);
                isDisplySubmit = true;
                if (backNodeList != null) {
                    Iterator it4 = backNodeList.iterator();
                    buffer.append("<").append(MobileConstant.TAG_RADIOFIELD).append(" ").append(MobileConstant.ATT_LABEL).append(" = '{*[Resume]*} {*[Flow]*}:'  ").append(MobileConstant.ATT_NAME).append("='_nextids'>");
                    while (it4.hasNext()) {
                        Node backNode = (Node) it4.next();
                        buffer.append("<").append(MobileConstant.TAG_OPTION).append(" ").append(MobileConstant.ATT_VALUE).append("='" + backNode.id + "' ");
                        buffer.append(">");
                        buffer.append(backNode.name);
                        buffer.append("</").append(MobileConstant.TAG_OPTION).append(">");
                    }
                    buffer.append("</").append(MobileConstant.TAG_RADIOFIELD).append(">");
                }
            }
            if (doc.getState() != null) {
                buffer.append("<").append(MobileConstant.TAG_TEXTAREAFIELD).append(" ").append(MobileConstant.ATT_LABEL).append(" = '{*[Approve]*}{*[Remarks]*}:' ").append(MobileConstant.ATT_NAME).append("='_attitude'></").append(MobileConstant.TAG_TEXTAREAFIELD).append(">");
            }
            buffer.append("</").append(MobileConstant.TAG_WORKFLOW).append(">");
            buffer.append("</").append(MobileConstant.TAG_FORM).append(">");
        }
        return buffer.toString();
    }

    /**
	 * 返回的字符串为重定义后的HTML，表达显示当前用户运行时节点
	 * 
	 * @param flowid
	 *            flow id
	 * @param docid
	 *            Document id
	 * @param webUser
	 *            webuser
	 * @return 字符串为显示当前用户运行时节点
	 * @throws Exception
	 */
    public String toFlowHtmlText(Document doc, WebUser webUser) throws Exception {
        StringBuffer buffer = new StringBuffer();
        BillDefiVO flowVO = null;
        String docid = doc.getId();
        String flowid = doc.getFlowid();
        flowVO = (BillDefiVO) getBillDefiProcess().doView(flowid);
        NodeRT nodert = null;
        FlowDiagram fd = null;
        if (flowVO != null) {
            fd = flowVO.toFlowDiagram();
            nodert = StateMachine.getCurrUserNodeRT(doc, webUser);
        }
        Node currnode = null;
        if (nodert != null) {
            String currnodeid = nodert.getNodeid();
            if (currnodeid != null) {
                currnode = (Node) fd.getElementByID(currnodeid);
            }
            buffer.append("<input type='hidden' name='_currid' value='" + currnodeid + "'>");
        }
        buffer.append("");
        if (flowState == FlowState.RUNNING && currnode != null) {
            isDisplySubmit = true;
            Collection nextNodeList = this.getNextToNodeList(docid, flowid, currnode.id, webUser);
            boolean flag = false;
            Node node = (Node) fd.getElementByID(nodert.getNodeid());
            boolean issplit = false;
            if (node != null && node instanceof ManualNode) {
                issplit = ((ManualNode) node).issplit;
            }
            buffer.append("<td class='flow-next' style='white-space:nowrap;word-break:keep-all'>{*[Commit]*}{*[To]*}:</td><td class='flow-next'>");
            if (nextNodeList != null && nextNodeList.size() > 0) {
                Iterator it3 = nextNodeList.iterator();
                while (it3.hasNext()) {
                    Node nextNode = (Node) it3.next();
                    boolean isOthers = false;
                    String id = "next";
                    String flowOperation = FlowType.RUNNING2RUNNING_NEXT;
                    if (!(nextNode instanceof ManualNode)) {
                        isOthers = true;
                        id = "other";
                        if (nextNode instanceof SuspendNode) {
                            flowOperation = FlowType.RUNNING2SUSPEND;
                        } else if (nextNode instanceof AbortNode) {
                            flowOperation = FlowType.RUNNING2ABORT;
                        } else if (nextNode instanceof TerminateNode) {
                            flowOperation = FlowType.RUNNING2TERMIATE;
                        } else if (nextNode instanceof CompleteNode) {
                            flowOperation = FlowType.RUNNING2COMPLETE;
                        } else if (nextNode instanceof AutoNode) {
                            flowOperation = FlowType.RUNNING2AUTO;
                        }
                    }
                    buffer.append("<input id='" + id + "' type='");
                    buffer.append(issplit ? "checkbox" : "radio");
                    buffer.append("' name='_nextids' value='" + nextNode.id);
                    buffer.append("' onclick='ev_setFlowType(" + isOthers);
                    buffer.append(", this, " + flowOperation + ")' />" + nextNode.name);
                }
                buffer.append("&nbsp;&nbsp;</td>");
            }
            Collection backNodeList = getBackToNodeList(doc, currnode.id, webUser);
            if (backNodeList != null && backNodeList.size() > 0) {
                buffer.append("<td style='white-space:nowrap;word-break:keep-all' class='commFont flow-back'>{*[Return]*}{*[To]*}:");
                buffer.append("</td>").append("<td>");
                buffer.append("<select class='flow-back' id='back' name='_nextids'");
                buffer.append(" onchange='ev_setFlowType(false, this, " + FlowType.RUNNING2RUNNING_BACK + ")'>");
                buffer.append("<option value=''>");
                buffer.append("{*[Please]*}{*[Choose]*}");
                buffer.append("</option>");
                for (Iterator iter = backNodeList.iterator(); iter.hasNext(); ) {
                    Node backNode = (Node) iter.next();
                    buffer.append("<option value='" + backNode.id + "'>");
                    buffer.append(backNode.name);
                    buffer.append("(").append(backNode.statelabel).append(")");
                    buffer.append("</option>");
                }
                buffer.append("</select>");
                buffer.append("</td>");
            }
        } else if (flowState == FlowState.SUSPEND && currnode != null && currnode instanceof SuspendNode) {
            Collection backNodeList = this.getBackToNodeList(doc, currnode.id, webUser, FlowState.SUSPEND);
            backNodeList = StateMachine.removeDuplicateNode(backNodeList);
            isDisplySubmit = true;
            if (backNodeList != null) {
                Iterator it4 = backNodeList.iterator();
                buffer.append("<td class='flow-next'>{*[Resume]*} {*[Flow]*}:");
                while (it4.hasNext()) {
                    Node backNode = (Node) it4.next();
                    buffer.append("<input id='suspend' type='radio' name='_nextids' value='" + backNode.id + "' ");
                    buffer.append("' onclick='ev_setFlowType(false, this, ");
                    buffer.append(FlowType.SUSPEND2RUNNING + ")' />");
                    buffer.append(backNode.name);
                }
                buffer.append("</td>");
            }
        } else {
            isDisplySubmit = false;
            if (flowState != FlowState.RUNNING) {
                if (flowState == FlowState.START) {
                    isDisplyFlow = false;
                } else {
                    buffer.append("<td style='font-size:12px;font-family: Arial, Helvetica;color:#FF0000'>" + doc.getStateLabel() + "</td>");
                }
            } else {
                buffer.append("<td style='font-size:12px;font-family: Arial, Helvetica;'>");
                buffer.append(doc.getStateLabel());
                buffer.append("</td>");
            }
        }
        return buffer.toString();
    }

    /**
	 * 返回字符串内容为显示当前处理人。
	 * 
	 * @param doc
	 *            TODO
	 * 
	 * @return 字符串内容为显示当前处理人
	 * @throws Exception
	 */
    public String toCurrProcessorHtml(Document doc, String applicationId) throws Exception {
        StringBuffer buffer = new StringBuffer();
        Collection nameList = toCurrProcessorList(doc, applicationId);
        if (nameList != null && !nameList.isEmpty()) {
            buffer.append("(");
            for (Iterator iterator = nameList.iterator(); iterator.hasNext(); ) {
                String name = (String) iterator.next();
                buffer.append(name + ",");
            }
            if (buffer.lastIndexOf(",") != -1) {
                buffer.deleteCharAt(buffer.lastIndexOf(","));
            }
            buffer.append(")");
        }
        return buffer.toString();
    }

    public Collection toCurrProcessorList(Document doc, String applicationId) throws Exception {
        Collection nodertList = new ArrayList();
        Collection processorList = new ArrayList();
        if (flowState == FlowState.SUSPEND) {
            NodeRT suspendRT = getSuspendNodeRT(doc, applicationId);
            if (suspendRT != null) {
                nodertList.add(suspendRT);
            }
        } else {
            nodertList = getAllNodeRT(doc, applicationId);
        }
        for (Iterator iter = nodertList.iterator(); iter.hasNext(); ) {
            NodeRT nodert = (NodeRT) iter.next();
            Collection colls = nodert.getPendingActorRTList();
            Object[] actorrts = colls.toArray();
            if (actorrts.length > 0) {
                for (int i = 0; i < actorrts.length; i++) {
                    String actorrtName = ((ActorRT) actorrts[i]).getName();
                    processorList.add(actorrtName);
                }
            }
        }
        return processorList;
    }

    public static String toHistoryHtml(Document doc, int cellCount) throws Exception {
        Collection colls = getAllRelationHIS(doc.getId(), doc.getFlowid(), doc.getApplicationid());
        FlowHistory his = new FlowHistory();
        his.addAllHis(colls);
        return his.toTextHtml();
    }

    public static String toHistoryXml(Document doc, int cellCount) throws Exception {
        Collection colls = getAllRelationHIS(doc.getId(), doc.getFlowid(), doc.getApplicationid());
        FlowHistory his = new FlowHistory();
        his.addAllHis(colls);
        return his.toTextXml();
    }

    /**
	 * is shows history or not
	 * 
	 * @param flowid
	 * @param docid
	 * @return
	 * @throws Exception
	 */
    public boolean isShowHis(String flowid, String docid, String applicationId) throws Exception {
        Collection colls = getAllRelationHIS(docid, flowid, applicationId);
        if (colls.size() > 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * 返回字符串为显示的流程状态标识。 此实现为通过Document id 与流程(flow) id
	 * 查询当前Document流程状态(flowStateRT)，并通过流程状态获取当前节点的状态标识（State label)
	 * 
	 * @returnf 字符串为显示的流程状态。
	 * @param doc
	 * @throws Exception
	 */
    public static String toFlowStateHtml(Document doc) throws Exception {
        StringBuffer buffer = new StringBuffer();
        FlowStateRT flowStateRT = doc.getState();
        if (flowStateRT != null) {
            buffer.append("{*[Flow]*}{*[State]*}:(");
            if (!StringUtil.isBlank(doc.getStateLabel())) {
                buffer.append(doc.getStateLabel());
            } else if (isDraft(doc)) {
                buffer.append(FlowState.getName(FlowState.DRAFT));
            } else {
                buffer.append(FlowState.getName(flowStateRT.getState()));
            }
            buffer.append(")");
        }
        LOG.debug("FlowState HTML: " + buffer.toString());
        return buffer.toString();
    }

    /**
	 * 判断是否为草稿状态
	 * 
	 * @param doc
	 * @param webUser
	 * 
	 * @return
	 * @throws Exception
	 */
    private static boolean isDraft(Document doc) throws Exception {
        Collection firstNodelist = StateMachine.getFirstNodeList(doc.getId(), doc.getFlowid());
        if (firstNodelist != null && firstNodelist.size() > 0) {
            Collection nodertlist = StateMachine.getAllNodeRT(doc.getId(), doc.getFlowid(), doc.getApplicationid());
            if (nodertlist != null && nodertlist.size() > 0) {
                for (Iterator iter = nodertlist.iterator(); iter.hasNext(); ) {
                    NodeRT nodert = (NodeRT) iter.next();
                    if (isContains(firstNodelist, nodert)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isContains(Collection firstNodes, NodeRT nodert) {
        for (Iterator iter = firstNodes.iterator(); iter.hasNext(); ) {
            Node firstNode = (Node) iter.next();
            if (firstNode.id.equals(nodert.getNodeid())) {
                return true;
            }
        }
        return false;
    }
}
