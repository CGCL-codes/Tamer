package org.powerstone.workflow.flowdriver;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.HashMap;
import java.util.List;
import org.powerstone.workflow.model.WorkflowDriver;
import javax.servlet.http.Cookie;
import org.powerstone.workflow.model.FlowTask;
import java.util.Iterator;
import org.powerstone.workflow.flowdriver.IFlowDriver;

public abstract class WorkflowDriverReadAction extends Action {

    private static Log log = LogFactory.getLog(WorkflowDriverReadAction.class);

    private static String flowTaskParamName = IFlowDriver.DEFAULT_FLOW_TASK_PARAM_NAME;

    public void setFlowTaskParamName(String flowTaskParamName) {
        this.flowTaskParamName = flowTaskParamName;
    }

    public final ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ActionForward forward = doExecute(actionMapping, actionForm, httpServletRequest, httpServletResponse);
        HashMap InputParameters = getDriverInputParameters(httpServletRequest, httpServletResponse);
        if (log.isDebugEnabled()) {
            log.debug("���������" + InputParameters);
        }
        return forward;
    }

    /**
   * ʵ�����ҵ���߼�
   * @param mapping ActionMapping
   * @param form ActionForm
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @throws Exception
   * @return ActionForward
   */
    public abstract ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
   * �������͵��û��ע��Ĳ���:�˴�Ӧ�ú�ע������������һ��
   * @return HashMap
   */
    protected HashMap getDriverInputParameters(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getServletPath();
        if (log.isDebugEnabled()) {
            log.debug(uri);
        }
        HashMap driverInputParameters = new HashMap();
        String flowTaskID = request.getParameter(flowTaskParamName);
        if (flowTaskID == null) {
            log.warn("ReadDO[" + request.getContextPath() + "|" + uri + "]����ȱ�ٹؼ����[" + flowTaskParamName + "]��");
            return driverInputParameters;
        } else {
            List readDrivers = WorkflowFactoryPlugin.getDriverManager().findDriverByReadDO(request.getContextPath(), uri);
            WorkflowDriver flowDriver = null;
            if (readDrivers.size() > 0) {
                flowDriver = (WorkflowDriver) readDrivers.get(0);
                if (readDrivers.size() > 1) {
                    log.warn("����[" + request.getContextPath() + "|" + uri + "]��ע��Ϊ�����");
                }
                this.processCookie(flowTaskID, flowDriver.getWriteURL(), response);
            } else {
                log.warn("����[" + request.getContextPath() + "|" + uri + "]û�б�ע��Ϊ�κ���");
                return driverInputParameters;
            }
            FlowTask flowTask = WorkflowFactoryPlugin.getTaskManager().getFlowTask(flowTaskID);
            HashMap procState = flowTask.getFlowProc().generateProcStateForDriver(flowTask.getFlowNodeBinding());
            if (procState != null) {
                for (Iterator it = procState.keySet().iterator(); it.hasNext(); ) {
                    String paraName = (String) it.next();
                    String paraValue = (String) procState.get(paraName);
                    driverInputParameters.put(paraName, paraValue);
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("���״̬{" + procState + "}\n����ע�����{" + driverInputParameters + "}");
            }
            return driverInputParameters;
        }
    }

    /**
   * ����cookie��WriteDOʹ��
   * @param flowTaskID String
   * @param writeURL String
   * @param response HttpServletResponse
   */
    private void processCookie(String flowTaskID, String writeURL, HttpServletResponse response) {
        String cookieValue = flowTaskID + IFlowDriver.COOKIE_DIVIDER + writeURL;
        Cookie ck = new Cookie(IFlowDriver.COOKIE_NAME, cookieValue);
        ck.setPath("/");
        response.addCookie(ck);
        if (log.isDebugEnabled()) {
            log.debug("����Cookie:" + cookieValue);
        }
    }

    public static String getFlowTaskParamName() {
        return flowTaskParamName;
    }
}
