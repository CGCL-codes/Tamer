package com.bluestone.report;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import com.bluestone.IAction;
import com.bluestone.config.ConfigUtil;
import com.bluestone.scripts.Command;
import com.bluestone.scripts.TestCase;
import com.bluestone.scripts.TestSuite;
import com.bluestone.util.Util;

/**
 * All actions in the project are invoked by ActionProxy.
 * @author <a href="mailto:ming7655@gmail.com">Aaron</a>
 */
public class ActionProxy {

    private ArrayList actionHandlers = new ArrayList();

    private static ActionProxy INSTANCE;

    private IAction action;

    private static ProjectReportBean projectReportBean;

    private ActionReportBean currentReportBean;

    private static ArrayList actionReportBeans = new ArrayList();

    private ActionProxy(IAction action) {
        this.action = action;
    }

    /**
	 * Generate the ActionProxy from the given IAction.
	 * @param action
	 * @return ActionProxy
	 */
    public static ActionProxy getActionProxy(IAction action) {
        if (INSTANCE == null) {
            INSTANCE = new ActionProxy(action);
            init();
        }
        INSTANCE.setAction(action);
        return INSTANCE;
    }

    private void setAction(IAction action) {
        this.action = action;
    }

    /**
	 * Invoke the IAction.execute().
	 * @return true if the IAction exectue successful
	 */
    public boolean execute() {
        this.currentReportBean = this.generateReportBean();
        ActionInvocationHandler actionHandler = new ActionInvocationHandler(this.action, this);
        IAction actionProxy = (IAction) Proxy.newProxyInstance(IAction.class.getClassLoader(), new Class[] { IAction.class }, actionHandler);
        return true;
    }

    public void setActionHandlers(ArrayList actionHandlers) {
        this.actionHandlers = actionHandlers;
    }

    public ArrayList getActionHandlers() {
        return this.actionHandlers;
    }

    public void addActionHandler(IActionHandler actionHandler) {
        if (this.actionHandlers != null) {
            this.actionHandlers.add(actionHandler);
        }
    }

    public void addActionHandlers(ArrayList actionHandlers) {
        this.actionHandlers = actionHandlers;
    }

    public void removeActionHandler(IActionHandler actionHandler) {
        if (this.actionHandlers != null) {
            this.actionHandlers.remove(actionHandler);
        }
    }

    private static void init() {
        INSTANCE.addActionHandlers(ConfigUtil.getInstance().getRegisterActionHandlers());
        INSTANCE.initProjectReportBean();
    }

    private void initProjectReportBean() {
    }

    /**
	 * 
	 * @return ProjectReportBean
	 */
    public static ProjectReportBean getProjectReportBean() {
        return projectReportBean;
    }

    /**
	 * Get the ActionReportBean from the current IAction.
	 * @return ActionReportBean
	 */
    public ActionReportBean getCurrentReportBean() {
        return this.currentReportBean;
    }

    private ActionReportBean generateReportBean() {
        ActionReportBean tempReportBean = this.getTempReportBean();
        tempReportBean.setReportId(Util.generateReportBeanId());
        actionReportBeans.add(tempReportBean);
        return tempReportBean;
    }

    private ActionReportBean getTempReportBean() {
        ActionReportBean rb = (ActionReportBean) ReportBeanFactory.createAbstractReportBean(this.action);
        Object actionParent = this.action.getParent();
        ReportBeanFactory.createAbstractReportBean(actionParent).addChildren(rb);
        if (Command.class.isInstance(actionParent)) {
            Command command = (Command) actionParent;
            rb.setCommandId(command.getId());
            rb.setCommandName(command.getName());
            Object commandParent = command.getParent();
            ReportBeanFactory.createAbstractReportBean(commandParent).addChildren(ReportBeanFactory.createAbstractReportBean(command));
            if (TestCase.class.isInstance(commandParent)) {
                TestCase tc = (TestCase) commandParent;
                rb.setTestCaseId(tc.getTestCaseId());
                rb.setTestCaseAttributeId(tc.getId());
                rb.setTestCaseName(tc.getName());
                Object testCaseParent = tc.getParent();
                ReportBeanFactory.createAbstractReportBean(testCaseParent).addChildren(ReportBeanFactory.createAbstractReportBean(tc));
                if (TestSuite.class.isInstance(testCaseParent)) {
                    TestSuite ts = (TestSuite) testCaseParent;
                    rb.setTestSuiteName(ts.getName());
                    if (projectReportBean != null) {
                        projectReportBean.addChildren(ReportBeanFactory.createAbstractReportBean(ts));
                    }
                }
            }
        }
        if (projectReportBean != null) {
            rb.setProjectId(projectReportBean.getProjectId());
            rb.setProjectName(projectReportBean.getProjectName());
            rb.setProjectRetryCount(projectReportBean.getProjectRetryCount());
            rb.setProjectSpeedRate(projectReportBean.getProjectSpeedRate());
            rb.setProjectVersion(projectReportBean.getProjectVersion());
        }
        return rb;
    }

    /**
	 * Get all of the ActionReportBeans from the IActions which have been invoked.
	 * @return ArrayList
	 */
    public static ArrayList getActionReportBeans() {
        return actionReportBeans;
    }
}
