package com.liferay.saw.service;

import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.util.ClassLoaderProxy;

/**
 * <a href="SAWWorkflowLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class SAWWorkflowLocalServiceUtil {

    public static com.sun.saw.vo.OutputVO checkoutTasks(com.sun.saw.vo.CheckoutTaskVO checkoutTaskVO) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        return getService().checkoutTasks(checkoutTaskVO);
    }

    public static com.sun.saw.vo.OutputVO completeTasks(com.sun.saw.vo.CompleteTaskVO completeTaskVO) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        return getService().completeTasks(completeTaskVO);
    }

    public static java.lang.String deploy(java.lang.String xml) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        return getService().deploy(xml);
    }

    public static java.lang.String getCurrentTasksXml(long instanceId, long tokenId, java.lang.String userId) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        return getService().getCurrentTasksXml(instanceId, tokenId, userId);
    }

    public static java.lang.String getDefinitionsCountXml(long definitionId, java.lang.String name, java.lang.String userId) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        return getService().getDefinitionsCountXml(definitionId, name, userId);
    }

    public static java.lang.String getDefinitionsXml(long definitionId, java.lang.String name, java.lang.String userId, int begin, int end) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        return getService().getDefinitionsXml(definitionId, name, userId, begin, end);
    }

    public static java.lang.String getDefinitionXml(long definitionId) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        return getService().getDefinitionXml(definitionId);
    }

    public static java.lang.String getInstancesCountXml(long definitionId, long instanceId, java.lang.String definitionName, java.lang.String definitionVersion, java.lang.String startDateGT, java.lang.String startDateLT, java.lang.String endDateGT, java.lang.String endDateLT, java.lang.String userId, boolean hideEndedTasks, boolean retrieveUserInstances, boolean andOperator) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        return getService().getInstancesCountXml(definitionId, instanceId, definitionName, definitionVersion, startDateGT, startDateLT, endDateGT, endDateLT, userId, hideEndedTasks, retrieveUserInstances, andOperator);
    }

    public static java.lang.String getInstancesXml(long definitionId, long instanceId, java.lang.String definitionName, java.lang.String definitionVersion, java.lang.String startDateGT, java.lang.String startDateLT, java.lang.String endDateGT, java.lang.String endDateLT, java.lang.String userId, boolean hideEndedTasks, boolean retrieveUserInstances, boolean andOperator, int start, int end) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        return getService().getInstancesXml(definitionId, instanceId, definitionName, definitionVersion, startDateGT, startDateLT, endDateGT, endDateLT, userId, hideEndedTasks, retrieveUserInstances, andOperator, start, end);
    }

    public static java.lang.String getTaskFormElementsXml(long taskId) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        return getService().getTaskFormElementsXml(taskId);
    }

    public static com.sun.saw.vo.OutputVO getTasks(com.sun.saw.vo.FilterTaskVO filterTaskVO) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        return getService().getTasks(filterTaskVO);
    }

    public static java.lang.String getTaskTransitionsXml(long taskId) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        return getService().getTaskTransitionsXml(taskId);
    }

    public static java.lang.String getTaskXml(long taskId, java.lang.String userId) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        return getService().getTaskXml(taskId, userId);
    }

    public static java.lang.String getUserTasksCountXml(long instanceId, java.lang.String taskName, java.lang.String definitionName, java.lang.String assignedTo, java.lang.String userId, java.lang.String createDateGT, java.lang.String createDateLT, java.lang.String startDateGT, java.lang.String startDateLT, java.lang.String endDateGT, java.lang.String endDateLT, boolean hideEndedTasks, boolean andOperator) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        return getService().getUserTasksCountXml(instanceId, taskName, definitionName, assignedTo, userId, createDateGT, createDateLT, startDateGT, startDateLT, endDateGT, endDateLT, hideEndedTasks, andOperator);
    }

    public static java.lang.String getUserTasksXml(long instanceId, java.lang.String taskName, java.lang.String definitionName, java.lang.String assignedTo, java.lang.String createDateGT, java.lang.String createDateLT, java.lang.String startDateGT, java.lang.String startDateLT, java.lang.String endDateGT, java.lang.String endDateLT, java.lang.String userId, boolean hideEndedTasks, boolean andOperator, int begin, int end) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        return getService().getUserTasksXml(instanceId, taskName, definitionName, assignedTo, createDateGT, createDateLT, startDateGT, startDateLT, endDateGT, endDateLT, userId, hideEndedTasks, andOperator, begin, end);
    }

    public static com.sun.saw.vo.OutputVO saveTasks(com.sun.saw.vo.SaveTaskVO saveTaskVO) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        return getService().saveTasks(saveTaskVO);
    }

    public static void signalInstance(long instanceId) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        getService().signalInstance(instanceId);
    }

    public static void signalToken(long instanceId, long tokenId) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        getService().signalToken(instanceId, tokenId);
    }

    public static java.lang.String startWorkflow(long definitionId, java.lang.String userId) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        return getService().startWorkflow(definitionId, userId);
    }

    public static java.util.Map updateTask(long taskId, java.lang.String transition, java.lang.String userId, java.util.Map parameterMap) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        return getService().updateTask(taskId, transition, userId, parameterMap);
    }

    public static java.lang.String updateTaskXml(long taskId, java.lang.String transition, java.lang.String userId, java.util.Map parameterMap) throws com.liferay.portal.kernel.jbi.WorkflowComponentException {
        return getService().updateTaskXml(taskId, transition, userId, parameterMap);
    }

    public static SAWWorkflowLocalService getService() {
        if (_service == null) {
            Object obj = PortletBeanLocatorUtil.locate("saw-web", SAWWorkflowLocalServiceUtil.class.getName());
            ClassLoader portletClassLoader = (ClassLoader) PortletBeanLocatorUtil.locate("saw-web", "portletClassLoader");
            ClassLoaderProxy classLoaderProxy = new ClassLoaderProxy(obj, portletClassLoader);
            _service = new SAWWorkflowLocalServiceClp(classLoaderProxy);
            ClpSerializer.setClassLoader(portletClassLoader);
        }
        return _service;
    }

    public void setService(SAWWorkflowLocalService service) {
        _service = service;
    }

    private static SAWWorkflowLocalService _service;
}
