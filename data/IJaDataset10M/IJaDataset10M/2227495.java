package ces.sf.oa.insend.action;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import ces.arch.form.BaseForm;
import ces.arch.form.EditForm;
import ces.coffice.common.affix.util.AffixUtil;
import ces.platform.system.dbaccess.Organize;
import ces.platform.system.dbaccess.User;
import ces.sf.oa.coflowlog.util.CoflowLogUtil;
import ces.sf.oa.insend.form.InsendForm;
import ces.sf.oa.insend.pojo.InsendPojo;
import ces.sf.oa.util.OAConstants;
import ces.sf.oa.util.PojoToMap;
import ces.sf.oa.util.StringUtil;
import ces.sf.oa.util.WorkflowUtil;
import ces.workflow.wapi.AttributeInstance;
import ces.workflow.wapi.ClientAPI;
import ces.workflow.wapi.Coflow;
import ces.workflow.wapi.Workitem;
import ces.workflow.wapi.define.Activity;
import ces.workflow.wapi.define.DefineService;
import ces.workflow.wapi.status.WFStatus;

public class DocSaveAction extends ces.arch.action.SaveAction {

    private Log log = LogFactory.getLog(this.getClass());

    protected ActionForward process(ActionMapping mapping, BaseForm form, HttpServletRequest request, HttpServletResponse response, ActionErrors errors) throws Exception {
        User user = (User) request.getSession().getAttribute(ces.sf.oa.util.OAConstants.ATTRIBUTE_USER);
        int userId = user.getUserID();
        String userName = user.getUserName();
        Vector deptlist = user.getParentOrg();
        String departName = "";
        if (deptlist.size() > 0) {
            Organize org = (Organize) deptlist.get(0);
            departName = org.getOrganizeName();
        }
        try {
            String reason = "";
            String activityId = "";
            long workitemId = 0;
            long processInstanceId = 0;
            boolean isNewDoc = false;
            boolean isStart = false;
            Workitem workitem = null;
            ClientAPI client = Coflow.getClientAPI(String.valueOf(userId));
            InsendForm insendForm = (InsendForm) form;
            InsendPojo insendPojo = (InsendPojo) insendForm.getBean();
            reason = (insendPojo.getReason()).replaceAll("&#8226;", "��");
            reason = (insendPojo.getReason()).replaceAll("&lt;", "<");
            reason = (insendPojo.getReason()).replaceAll("&gt;", ">");
            if (insendPojo.getId() == 0) {
                insendPojo.setCreateId(userId);
                insendPojo.setCreateName(userName);
                insendPojo.setCreateDate(new java.util.Date());
                insendPojo.setReason(reason);
                isNewDoc = true;
            }
            if ("true".equals(insendForm.getStartFlow()) && insendPojo.getProcessInstanceId() == 0) {
                String packageId = OAConstants.OA_INSEND_PACKAGE_ID;
                String processId = OAConstants.OA_INSEND_PROCESS_ID;
                workitem = client.startProcessInstanceAndGetWorkitem(packageId, processId, "");
                isStart = true;
                if (workitem != null) {
                    if (workitem.getStatus() == WFStatus.WORKITEM_INITIALIZED.getValue()) {
                        client.checkoutWorkitem(workitem.getId());
                    }
                    insendForm.setWorkitemId(String.valueOf(workitem.getId()));
                    insendPojo.setProcessInstanceId((workitem.getProcessInstanceId()));
                    processInstanceId = workitem.getProcessInstanceId();
                }
                activityId = workitem.getActivityId();
                workitemId = workitem.getId();
                insendForm.setStartFlow("next");
            }
            if ("".equals(activityId) && insendForm.getWorkitemId() != null && !"".equals(insendForm.getWorkitemId())) {
                workitem = client.getWorkitem(Long.parseLong(insendForm.getWorkitemId()));
                activityId = workitem.getActivityId();
                processInstanceId = workitem.getProcessInstanceId();
                workitemId = workitem.getId();
            } else {
                activityId = "BEGIN";
            }
            if (processInstanceId != 0) {
                client.refreshAttribute(processInstanceId, new AttributeInstance("titletype", insendPojo.getFileDraftType()));
                client.refreshAttribute(processInstanceId, new AttributeInstance("meetingid", insendPojo.getInsendId()));
                client.refreshAttribute(processInstanceId, new AttributeInstance("makeupunit", insendPojo.getMakeupUnit()));
                client.refreshAttribute(processInstanceId, new AttributeInstance("username", insendPojo.getCreateName()));
                client.refreshAttribute(processInstanceId, new AttributeInstance("deptname", insendPojo.getDepartName()));
                client.refreshAttribute(processInstanceId, new AttributeInstance("draftdate", insendPojo.getCreateDate()));
                client.refreshAttribute(processInstanceId, new AttributeInstance("title", insendPojo.getFileName()));
                client.refreshAttribute(processInstanceId, new AttributeInstance("senduserdept", departName));
                client.refreshAttribute(processInstanceId, new AttributeInstance("secret", insendPojo.getSecretLevel()));
                client.refreshAttribute(processInstanceId, new AttributeInstance("infotype", insendPojo.getInfotype()));
                client.refreshAttribute(processInstanceId, new AttributeInstance("isdb", insendPojo.getOversee()));
                client.refreshAttribute(processInstanceId, new AttributeInstance("count", insendPojo.getTotalFilecount()));
                client.refreshAttribute(processInstanceId, new AttributeInstance("insendno", insendPojo.getFileId()));
                client.refreshAttribute(processInstanceId, new AttributeInstance("opentype", insendPojo.getOpenType()));
                client.refreshAttribute(processInstanceId, new AttributeInstance("sendtype", insendPojo.getSendType()));
                client.refreshAttribute(processInstanceId, new AttributeInstance("majorsend", insendPojo.getMajorsender()));
                client.refreshAttribute(processInstanceId, new AttributeInstance("othersend", insendPojo.getOthersender()));
                client.refreshAttribute(processInstanceId, new AttributeInstance("sendinfo", "0"));
                client.refreshAttribute(processInstanceId, new AttributeInstance("idiograhp", insendPojo.getIdiograph()));
                client.refreshAttribute(processInstanceId, new AttributeInstance("reason", insendPojo.getReason()));
            }
            DefineService defineService = Coflow.getDefineService();
            Activity activty = defineService.getActivity(OAConstants.OA_INSEND_PACKAGE_ID, OAConstants.OA_INSEND_PROCESS_ID, activityId);
            Map oldValues = new HashMap();
            oldValues.put("secretLevel", new String[] { "�ܼ�", StringUtil.getParameter("beforeSecretLevel", request) });
            oldValues.put("openType", new String[] { "��������", StringUtil.getParameter("beforeOpenType", request) });
            if (activty != null) {
                try {
                    String activityName = processInstanceId == 0 ? "���" : activty.getName();
                    setChangeMark(userName, activityName, insendPojo, oldValues);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            this.doSave(mapping, (EditForm) form, request, response, errors);
            if (request.getParameter("from") != null) {
                request.setAttribute("from", StringUtil.getParameter("from", request));
            }
            if (isNewDoc) {
                String lastFile = insendPojo.getLastFile();
                if (!StringUtil.isEmpty(lastFile)) {
                    String oldOrgId = lastFile.split("_")[1].split("\\.")[0];
                    AffixUtil.changeAffixOrgId(oldOrgId, String.valueOf(insendPojo.getId()));
                }
            }
            if (isStart && workitem != null) {
                CoflowLogUtil.writeLogForDraft(workitem, processInstanceId, userId, insendPojo.getCreateDate(), "����");
            }
            if (workitem != null) {
                WorkflowUtil.setRedirect(request, workitem);
            }
            request.setAttribute("saved", "true");
            insendForm.setActivity(activityId);
            if ("next".equals(insendForm.getStartFlow())) {
                request.setAttribute("workitemId", "" + workitemId);
                return mapping.findForward("SEND_NEXT");
            }
            return mapping.findForward("BEGIN");
        } catch (Exception ex) {
            log.error(ex);
            request.setAttribute("Exception", ex);
            return mapping.findForward("fail");
        }
    }

    /**
	 * �����ֶ��޸ļ�¼��ͨ���෴�����POJO�����set��get����ʵ��
	 * @author yhl
	 * @param operationer	������
	 * @param activityName	��ǰ�ڵ�
	 * @param srcObject		POJO����(�ж�Ӧ��setXXX��getXXX����)
	 * @param oldValues		�޸�֮ǰ��ֵ����
	 * @throws Throwable	��@srcObjectת����MAP��ʽʱ
	 */
    private void setChangeMark(String operationer, String activityName, Object srcObject, Map oldValues) throws Throwable {
        Map newValues = PojoToMap.getProperties(srcObject);
        Iterator keys = oldValues.keySet().iterator();
        while (keys.hasNext()) {
            Object nextKey = keys.next();
            String newValue = StringUtil.getValue(newValues.get(nextKey.toString()));
            String[] oldValueArray = (String[]) oldValues.get(nextKey.toString());
            String oldValue = oldValueArray[1];
            if (StringUtil.isEmpty(newValue) || StringUtil.isEmpty(oldValue)) {
                continue;
            } else if (!newValue.equals(oldValue)) {
                String setMethodName = "set" + nextKey.toString().substring(0, 1).toUpperCase() + nextKey.toString().substring(1) + "Mark";
                String getMethodName = "get" + nextKey.toString().substring(0, 1).toUpperCase() + nextKey.toString().substring(1) + "Mark";
                Class c = Class.forName(srcObject.getClass().getName());
                Method methods[] = c.getDeclaredMethods();
                Object inhereValue = null;
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().equals(getMethodName)) {
                        inhereValue = methods[i].invoke(srcObject, new Object[0]);
                    }
                }
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().equals(setMethodName)) {
                        String recordContent = operationer + "��" + StringUtil.getValue(new Date(), "datetime") + "����[" + activityName + "]�ڵ㽫<" + oldValueArray[0] + ">��\"" + oldValue + "\"�޸�Ϊ\"" + newValue + "\"";
                        if (inhereValue != null) {
                            recordContent += ";" + inhereValue;
                        }
                        Object[] values = { recordContent };
                        methods[i].invoke(srcObject, values);
                    }
                }
            }
        }
    }
}
