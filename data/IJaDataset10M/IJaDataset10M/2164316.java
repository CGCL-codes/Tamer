package org.bpmsuite.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import org.bpmsuite.constants.ProcessVariables;
import org.bpmsuite.constants.ResourceBundles;
import org.bpmsuite.dto.DataTransferObjectException;
import org.bpmsuite.dto.bpm.BpmTask;
import org.bpmsuite.frontend.session.SessionResource;
import org.bpmsuite.frontend.session.SessionResourceException;
import org.bpmsuite.service.ServiceException;
import org.bpmsuite.viewhelper.ProtocolEntryViewHelper;
import org.bpmsuite.vo.platform.PlatformUser;
import org.bpmsuite.vo.protocol.ProtocolEntry;

public class TasklistSessionResource implements SessionResource {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3888309866584914906L;

    private SessionRessourceHandler _sessionRessourceHandler;

    private ResourceBundle _resourceBundle;

    private Map _tasklist;

    private Map _selectBoxTasklist;

    private BpmTask _currentTask;

    private Map _pooledTasklist;

    private Map _selectBoxPooledTasklist;

    private TasklistSessionResource() {
    }

    public TasklistSessionResource(SessionRessourceHandler sessionRessourceHandler) {
        super();
        _sessionRessourceHandler = sessionRessourceHandler;
    }

    public void init() throws SessionResourceException {
        _resourceBundle = ResourceBundle.getBundle(ResourceBundles.TASKLIST);
        _tasklist = new LinkedHashMap();
        _selectBoxTasklist = new LinkedHashMap();
        _pooledTasklist = new LinkedHashMap();
        _selectBoxPooledTasklist = new LinkedHashMap();
    }

    public void reset() throws SessionResourceException {
        _tasklist.clear();
        _selectBoxTasklist.clear();
        _pooledTasklist.clear();
        _selectBoxPooledTasklist.clear();
    }

    public void refresh(int type) throws SessionResourceException {
        switch(type) {
            case REMOTE_REFRESH:
                retrieveTasklists();
                break;
        }
    }

    public void destroy() throws SessionResourceException {
        _resourceBundle = null;
        _selectBoxTasklist = null;
        _tasklist = null;
        _sessionRessourceHandler = null;
    }

    public BpmTask getDefaultTask() {
        BpmTask result = null;
        if (getTaskList().keySet().iterator().hasNext()) {
            Long id = (Long) getTaskList().keySet().iterator().next();
            result = getTask(id);
        }
        return result;
    }

    public BpmTask getTask(Long taskId) {
        BpmTask result = null;
        if (_tasklist == null || _tasklist.size() == 0) {
            retrieveTasklists();
        }
        Object task = _tasklist.get(taskId);
        if (task != null) {
            result = (BpmTask) task;
            if (!result.isModelLoaded()) {
                try {
                    result.loadModel(_sessionRessourceHandler.getServiceSessionResource().getServices());
                } catch (DataTransferObjectException e) {
                    e.printStackTrace();
                }
            }
        }
        setCurrentTask(result);
        return result;
    }

    public Map getSelectboxLeavingTransitions() throws SessionResourceException {
        Map result = null;
        BpmTask bpmTask = getCurrentTask();
        if (bpmTask != null && bpmTask.getLeavingTransitions() != null && bpmTask.getLeavingTransitions().size() > 1) {
            result = new LinkedHashMap();
            String key;
            for (Iterator iterator = bpmTask.getLeavingTransitions().iterator(); iterator.hasNext(); ) {
                key = (String) iterator.next();
                result.put(key, _resourceBundle.getString(key));
            }
        }
        return result;
    }

    public Map getSelectBoxTasklist() {
        if (_tasklist.size() == 0) {
            retrieveTasklists();
        }
        return _selectBoxTasklist;
    }

    public Map getTaskList() {
        if (_tasklist.size() == 0) {
            retrieveTasklists();
        }
        return _tasklist;
    }

    public boolean isTasklistEmpty() {
        boolean result = true;
        if (_tasklist.size() > 0) {
            result = false;
        }
        return result;
    }

    public Map getSelectBoxPooledTasklist() {
        if (_pooledTasklist.size() == 0) {
            retrieveTasklists();
        }
        return _selectBoxPooledTasklist;
    }

    public Map getPooledTaskList() {
        if (_pooledTasklist.size() == 0) {
            retrieveTasklists();
        }
        return _pooledTasklist;
    }

    public boolean isPooledTasklistEmpty() {
        boolean result = true;
        if (_pooledTasklist.size() > 0) {
            result = false;
        }
        return result;
    }

    public Collection getProtocolVH() throws SessionResourceException {
        Collection result = null;
        BpmTask currentTask = getCurrentTask();
        if (currentTask.getProtocolEntry() != null) {
            Map services = _sessionRessourceHandler.getServiceSessionResource().getServices();
            result = new ArrayList();
            for (Iterator iterator = currentTask.getProtocolEntry().getFirstEntry().getProtocolDesc().iterator(); iterator.hasNext(); ) {
                result.add(new ProtocolEntryViewHelper((ProtocolEntry) iterator.next(), services));
            }
        }
        return result;
    }

    public void finishCurrentTask(String leavingTransition, String protocolText) throws ServiceException, SessionResourceException {
        _currentTask.setLeavingTransition(leavingTransition);
        _sessionRessourceHandler.getServiceSessionResource().getBpmService().finishTask(_currentTask, protocolText);
        _currentTask = null;
    }

    private void retrieveTasklists() {
        try {
            Collection taskList = _sessionRessourceHandler.getServiceSessionResource().getBpmService().retrieveTaskList(_sessionRessourceHandler.getEmployee());
            Collection pooledTasklist = _sessionRessourceHandler.getServiceSessionResource().getBpmService().retrievePooledTaskList(_sessionRessourceHandler.getEmployee());
            fillTasklist(taskList);
            fillPooledTasklist(pooledTasklist);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    private synchronized void fillTasklist(Collection taskList) throws ServiceException {
        _tasklist.clear();
        _selectBoxTasklist.clear();
        if (taskList != null && taskList.size() > 0) {
            BpmTask bpmTask;
            String taskName;
            for (Iterator iterator = taskList.iterator(); iterator.hasNext(); ) {
                bpmTask = (BpmTask) iterator.next();
                _tasklist.put(bpmTask.getTaskInstanceId(), bpmTask);
                taskName = buildGeneralTaskName(bpmTask);
                _selectBoxTasklist.put(bpmTask.getTaskInstanceId(), taskName);
            }
        }
    }

    private synchronized void fillPooledTasklist(Collection taskList) throws ServiceException {
        _pooledTasklist.clear();
        _selectBoxPooledTasklist.clear();
        if (taskList != null && taskList.size() > 0) {
            BpmTask bpmTask;
            String taskName;
            for (Iterator iterator = taskList.iterator(); iterator.hasNext(); ) {
                bpmTask = (BpmTask) iterator.next();
                _pooledTasklist.put(bpmTask.getTaskInstanceId(), bpmTask);
                taskName = buildGeneralTaskName(bpmTask);
                _selectBoxPooledTasklist.put(bpmTask.getTaskInstanceId(), taskName);
            }
        }
    }

    public BpmTask getCurrentTask() throws SessionResourceException {
        if (_currentTask == null) {
            setCurrentTask(getDefaultTask());
        }
        return _currentTask;
    }

    public void setCurrentTask(BpmTask task) {
        _currentTask = task;
        if (_currentTask != null && _currentTask.hasModel() && !_currentTask.isModelLoaded()) {
            try {
                _currentTask.loadModel(_sessionRessourceHandler.getServiceSessionResource().getServices());
            } catch (DataTransferObjectException e) {
                e.printStackTrace();
            }
        }
        try {
            reset();
        } catch (SessionResourceException e) {
            e.printStackTrace();
        }
    }

    private synchronized String buildGeneralTaskName(BpmTask bpmTask) throws ServiceException {
        StringBuffer stringBuffer = new StringBuffer();
        String additionalTasklistInformation = bpmTask.getAdditionalTasklistInformation(_sessionRessourceHandler.getLocale());
        String systemId = (String) bpmTask.getProcessVariables().get(ProcessVariables.SYSTEM_ID);
        String taskname = stringBuffer.append(bpmTask.getProcessDefinitionName()).append(".").append(bpmTask.getTaskName()).toString();
        stringBuffer.delete(0, stringBuffer.length());
        if (bpmTask.isPooledTask()) {
            stringBuffer.append("* ");
        }
        stringBuffer.append(_resourceBundle.getString(bpmTask.getProcessDefinitionName()));
        stringBuffer.append(" - ");
        stringBuffer.append(_resourceBundle.getString(taskname));
        if (additionalTasklistInformation != null) {
            stringBuffer.append(" ").append(additionalTasklistInformation);
        }
        if (!systemId.toLowerCase().equals(_sessionRessourceHandler.getSystemId().toLowerCase())) {
            PlatformUser platformUser = _sessionRessourceHandler.getServiceSessionResource().getPlatformService().retrievePlatformUserBySystemId(systemId);
            stringBuffer.append(", ").append(platformUser.getFullName());
        }
        return stringBuffer.toString();
    }
}
