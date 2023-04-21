package org.powerstone.workflow.model;

import java.util.*;
import org.apache.commons.lang.builder.*;
import org.apache.commons.logging.*;

/**
 * @hibernate.class table="WF_FLOW_PROC"
 * @hibernate.query name="ActiveFlowProcsByDeploy"
 *  query="select theFP from FlowProc theFP where theFP.flowProcID in
 *  (select fp.flowProcID from FlowTask ft join ft.flowProcTransaction.flowProc fp
 *  where fp.flowDeploy.flowDeployID = ? and ft.taskState not in (?)
 *  group by fp having count(ft) >0)"

 * ���������
 * <p>Title: PowerStone</p>
 */
public class FlowProc extends BaseObject {

    private static Log log = LogFactory.getLog(FlowProc.class);

    private Long flowProcID = new Long(-1);

    private FlowDeploy flowDeploy;

    private String startTime;

    private FlowProc linkFlowProc;

    private String starterUserID;

    private java.util.List flowProcRelativeDatas = new ArrayList();

    private java.util.List linkedFlowProcs = new ArrayList();

    private java.util.List flowProcTransactions = new ArrayList();

    private String previewText;

    /**
   * @hibernate.id column="PK_FLOW_PROC_ID"
   * 		   unsaved-value="-1"
   *               generator-class="native"
   * @return Long
   */
    public Long getFlowProcID() {
        return flowProcID;
    }

    public void setFlowProcID(Long flowProcID) {
        this.flowProcID = flowProcID;
    }

    /**
   * @hibernate.many-to-one
   * column="FK_FLOW_DEPLOY_ID"
   * class="org.powerstone.workflow.model.FlowDeploy"
   * @return FlowDeploy
   */
    public FlowDeploy getFlowDeploy() {
        return flowDeploy;
    }

    public void setFlowDeploy(FlowDeploy flowDeploy) {
        this.flowDeploy = flowDeploy;
    }

    /**
   * @hibernate.property
   * 		column="VC_START_TIME"
   * 		length="255"
   * 		type="string"
   *            not-null="true"
   * @return String
   */
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
   * @hibernate.many-to-one
   * column="FK_LINK_FLOW_PROC_ID"
   * class="org.powerstone.workflow.model.FlowProc"
   * @return FlowProc
   */
    public FlowProc getLinkFlowProc() {
        return linkFlowProc;
    }

    public void setLinkFlowProc(FlowProc linkFlowProc) {
        this.linkFlowProc = linkFlowProc;
    }

    /**
   * @hibernate.property
   * 		column="VC_STARTER_USER_ID"
   * 		length="255"
   * 		type="string"
   *            not-null="true"
   * @return String
   */
    public String getStarterUserID() {
        return starterUserID;
    }

    public void setStarterUserID(String starterUserID) {
        this.starterUserID = starterUserID;
    }

    /**
   * ���ؽ���Ѿ���ͨ��·��
   * @return List
   */
    public List getFlowProcTransitions() {
        List result = new ArrayList();
        for (Iterator it = this.getFlowProcTransactions().iterator(); it.hasNext(); ) {
            FlowProcTransaction fpt = (FlowProcTransaction) it.next();
            for (Iterator it2 = fpt.getFlowProcTransitions().iterator(); it2.hasNext(); ) {
                FlowProcTransition ft = (FlowProcTransition) it2.next();
                result.add(ft);
            }
        }
        return result;
    }

    /**
   * ���ؽ���Ѳ��������
   * @return List
   */
    public List getFlowTasks() {
        List result = new ArrayList();
        for (Iterator it = this.getFlowProcTransactions().iterator(); it.hasNext(); ) {
            FlowProcTransaction fpt = (FlowProcTransaction) it.next();
            for (Iterator it2 = fpt.getFlowTasks().iterator(); it2.hasNext(); ) {
                FlowTask ft = (FlowTask) it2.next();
                result.add(ft);
            }
        }
        return result;
    }

    /**
   * getTaskByNode
   *
   * @param nodeID String
   * @return FlowTask
   */
    public FlowTask getTaskByNode(String nodeID) {
        for (Iterator it = getFlowTasks().iterator(); it.hasNext(); ) {
            FlowTask ft = (FlowTask) it.next();
            if (ft.getFlowNodeBinding().getFlowNodeID().equals(nodeID)) {
                return ft;
            }
        }
        log.warn("WorkflowNode[" + nodeID + "]has no Task in FlowProc[" + this.getFlowProcID() + "]");
        return null;
    }

    /**
   * ���ҽڵ�nodeID��ǰ������
   * @param nodeID String
   * @param transitionIgnore FlowProcTransition�������ڴ˽�̵�·��(��ΪҪ�Ͳ���������)
   * @return FlowProcTransaction
   */
    public FlowProcTransaction getPreTransactionOfNode(String nodeID, FlowProcTransaction transactionIgnore) {
        for (Iterator it = this.getFlowProcTransitions().iterator(); it.hasNext(); ) {
            FlowProcTransition ft = (FlowProcTransition) it.next();
            if (!ft.getFlowProcTransaction().equals(transactionIgnore) && ft.getToNodeID().equals(nodeID)) {
                FlowProcTransaction result = ft.getFlowProcTransaction();
                if (log.isDebugEnabled()) {
                    log.debug("FlowProcTransaction[" + result.getTransactionID() + "]contains tasks[" + result.getFlowTasks().size() + "] and transitions[" + result.getFlowProcTransitions().size() + "]");
                }
                return result;
            }
        }
        log.warn("WorkflowNode[" + nodeID + "]has no PreTransaction in FlowProc[" + this.getFlowProcID() + "]");
        return null;
    }

    /**
   * ���ҽڵ�nodeID�ĺ�������
   * @param nodeID String
   * @return FlowProcTransaction
   */
    public FlowProcTransaction getPostTransactionOfNode(String nodeID) {
        for (Iterator it = this.getFlowProcTransitions().iterator(); it.hasNext(); ) {
            FlowProcTransition ft = (FlowProcTransition) it.next();
            if (ft.getFromNodeID().equals(nodeID)) {
                return ft.getFlowProcTransaction();
            }
        }
        log.warn("WorkflowNode[" + nodeID + "]has no PostTransaction in FlowProc[" + this.getFlowProcID() + "]");
        return null;
    }

    /**
   * @hibernate.bag name="flowProcRelativeDatas"
   * cascade="all-delete-orphan"
   * lazy="true"
   * inverse="true"
   * @hibernate.collection-key
   * column="FK_FLOW_PROC_ID"
   * @hibernate.collection-one-to-many
   * class="org.powerstone.workflow.model.FlowProcRelativeData"
   * @return List
   */
    public List getFlowProcRelativeDatas() {
        return flowProcRelativeDatas;
    }

    public HashMap getProcState() {
        HashMap result = new HashMap();
        if (getFlowProcRelativeDatas().size() > 0) {
            for (Iterator it = getFlowProcRelativeDatas().iterator(); it.hasNext(); ) {
                FlowProcRelativeData procRelativeData = (FlowProcRelativeData) it.next();
                if (procRelativeData.getFlowNodeOutputParamBinding() != null) {
                    String nodeParamID = procRelativeData.getFlowNodeOutputParamBinding().getFlowNodeParamID();
                    String nodeParamValue = procRelativeData.getCorrespondingNodeParamValue();
                    result.put(nodeParamID, nodeParamValue);
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("+++++++++++++ProcState[" + result + "]");
        }
        return result;
    }

    /**
   * ���ض�Ӧ���������������read_doע�����
   * @return HashMap
   */
    public HashMap generateProcStateForDriver(FlowNodeBinding targetNode) {
        HashMap result = new HashMap();
        if (getFlowProcRelativeDatas().size() > 0) {
            for (Iterator it = getFlowProcRelativeDatas().iterator(); it.hasNext(); ) {
                FlowProcRelativeData procRelativeData = (FlowProcRelativeData) it.next();
                FlowNodeOutputParamBinding nodeOutputParamBinding = procRelativeData.getFlowNodeOutputParamBinding();
                if (nodeOutputParamBinding == null) {
                    log.warn("����һ���Ƿ���(û�ж�Ӧ��FlowNodeOutputParamBinding)ProcRelativeData(" + procRelativeData + ")");
                    continue;
                }
                if (log.isDebugEnabled()) {
                    log.debug("procRelativeData[" + procRelativeData + "]��Ӧ��FlowNodeParamID---->" + nodeOutputParamBinding.getFlowNodeParamID());
                }
                WFDriverInputParam driverInputParam = targetNode.findDriverInputParamByNodeParamID(nodeOutputParamBinding.getFlowNodeParamID());
                if (log.isDebugEnabled()) {
                    log.debug("ProcRelativeData[" + procRelativeData + "]��ӦFlowNodeParamID[" + nodeOutputParamBinding.getFlowNodeParamID() + "]->��Ӧ�˽ڵ�[" + targetNode.getFlowNodeID() + "]��WFDriverInputParam[" + driverInputParam + "]");
                }
                String driverParamValue = procRelativeData.getDriverParamValue();
                if (driverInputParam != null) {
                    result.put(driverInputParam.getParamName(), driverParamValue);
                } else {
                    result.put(nodeOutputParamBinding.getWfDriverOutputParam().getParamName(), driverParamValue);
                }
            }
        }
        return result;
    }

    public void addFlowProcRelativeData(FlowProcRelativeData procRelativeData) {
        procRelativeData.setFlowProc(this);
        getFlowProcRelativeDatas().add(procRelativeData);
    }

    public FlowProcRelativeData findProcRelativeDataByDriverParamName(String driverParamName) {
        for (Iterator it = getFlowProcRelativeDatas().iterator(); it.hasNext(); ) {
            FlowProcRelativeData procRelativeData = (FlowProcRelativeData) it.next();
            if (procRelativeData.getFlowNodeOutputParamBinding() != null && procRelativeData.getFlowNodeOutputParamBinding().getWfDriverOutputParam().getParamName().equals(driverParamName)) {
                return procRelativeData;
            }
        }
        return null;
    }

    public void setFlowProcRelativeDatas(List flowProcRelativeDatas) {
        this.flowProcRelativeDatas = flowProcRelativeDatas;
    }

    /**
   * @hibernate.bag name="linkedFlowProcs"
   * cascade="save-update"
   * lazy="true"
   * inverse="true"
   * @hibernate.collection-key
   * column="FK_LINK_FLOW_PROC_ID"
   * @hibernate.collection-one-to-many
   * class="org.powerstone.workflow.model.FlowProc"
   * @return List
   */
    public java.util.List getLinkedFlowProcs() {
        return linkedFlowProcs;
    }

    public void removeLinkedFlowProc(FlowProc flowProc) {
        flowProc.setLinkFlowProc(null);
        this.getLinkedFlowProcs().remove(flowProc);
    }

    public void addLinkedFlowProc(FlowProc flowProc) {
        flowProc.setLinkFlowProc(this);
        getLinkedFlowProcs().add(flowProc);
    }

    public void setLinkedFlowProcs(List linkedFlowProcs) {
        this.linkedFlowProcs = linkedFlowProcs;
    }

    /**
   * @hibernate.bag name="flowProcTransactions"
   * cascade="all-delete-orphan"
   * lazy="true"
   * inverse="true"
   * @hibernate.collection-key
   * column="FK_FLOW_PROC_ID"
   * @hibernate.collection-one-to-many
   * class="org.powerstone.workflow.model.FlowProcTransaction"
   * @return List
   */
    public List getFlowProcTransactions() {
        return flowProcTransactions;
    }

    public void addProcTransaction(FlowProcTransaction procTransaction) {
        procTransaction.setFlowProc(this);
        getFlowProcTransactions().add(procTransaction);
    }

    public void removeProcTransaction(FlowProcTransaction procTransaction) {
        procTransaction.setFlowProc(null);
        getFlowProcTransactions().remove(procTransaction);
    }

    public void setFlowProcTransactions(List flowProcTransactions) {
        this.flowProcTransactions = flowProcTransactions;
    }

    /**
   * ��ɹ¶��Ա���ɾ��
   */
    public void toOrphan() {
        if (getFlowDeploy() != null) {
            getFlowDeploy().removeFlowProc(this);
        }
        if (getLinkFlowProc() != null) {
            getLinkFlowProc().removeLinkedFlowProc(this);
        }
    }

    public void clear() {
        if (this.getFlowProcRelativeDatas().size() > 0) {
            for (Iterator it = getFlowProcRelativeDatas().iterator(); it.hasNext(); ) {
                FlowProcRelativeData relativeData = (FlowProcRelativeData) it.next();
                relativeData.setFlowProc(null);
            }
            getFlowProcRelativeDatas().clear();
        }
        if (this.getFlowProcTransactions().size() > 0) {
            for (Iterator it = getFlowProcTransactions().iterator(); it.hasNext(); ) {
                FlowProcTransaction procTransaction = (FlowProcTransaction) it.next();
                procTransaction.setFlowProc(null);
                procTransaction.clear();
            }
            getFlowProcTransactions().clear();
        }
    }

    public String getPreviewText() {
        return previewText;
    }

    public void setPreviewText(String previewText) {
        this.previewText = previewText;
    }

    public boolean equals(Object object) {
        if (!(object instanceof FlowProc)) {
            return false;
        }
        FlowProc fp = (FlowProc) object;
        return new EqualsBuilder().append(this.getFlowProcID().toString(), fp.getFlowProcID().toString()).append(this.getStartTime(), fp.getStartTime()).append(this.getStarterUserID(), fp.starterUserID).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(286335803, 287569255).append(this.getFlowProcID().toString()).append(this.getStartTime()).append(this.getStarterUserID()).toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("flowProcID", this.getFlowProcID().toString()).append("startTime", this.getStartTime()).append("starterUserID", this.getStarterUserID()).toString();
    }
}
