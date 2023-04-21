package org.manaty.seam_jbpm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.model.SelectItem;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.log.Log;
import org.jbpm.file.def.FileDefinition;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.logging.log.ProcessLog;
import org.jbpm.util.XmlUtil;
import org.manaty.seam_jbpm.util.Variable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Name("processInsDetailAction")
@Scope(ScopeType.CONVERSATION)
public class ProcessInsDetail {

    @Logger
    private Log log;

    @RequestParameter
    private Long processInstanceId;

    @In
    private JbpmConsoleService jbpmConsoleService;

    @Out(required = false)
    private ProcessInstance currProcessInstance;

    @Out(required = false)
    private List<ProcessLog> logs;

    private String selectedTransition;

    private List<Variable> variables;

    private Map<String, Object> variableMap;

    private Map<String, String> formMap;

    private List<SelectItem> transitions;

    public void detail() {
        this.currProcessInstance = jbpmConsoleService.getProcessInstance(processInstanceId);
        variables = jbpmConsoleService.getVariables(currProcessInstance);
        transitions = createSelectItems(currProcessInstance);
    }

    public void signal() {
        jbpmConsoleService.signal(currProcessInstance, selectedTransition, variableMap);
        this.currProcessInstance = jbpmConsoleService.getProcessInstance(currProcessInstance.getId());
        variables = jbpmConsoleService.getVariables(currProcessInstance);
        transitions = createSelectItems(currProcessInstance);
    }

    public void updateVariables() {
        jbpmConsoleService.updateVariables(currProcessInstance, variables);
        this.currProcessInstance = jbpmConsoleService.getProcessInstance(currProcessInstance.getId());
    }

    private List<SelectItem> createSelectItems(ProcessInstance ins) {
        List<SelectItem> list = new ArrayList<SelectItem>();
        SelectItem item0 = new SelectItem();
        item0.setLabel("---");
        item0.setValue("");
        list.add(item0);
        for (Object tr : currProcessInstance.getRootToken().getNode().getLeavingTransitionsList()) {
            Transition transition = (Transition) tr;
            SelectItem item = new SelectItem();
            if (transition.getName() != null && transition.getName().trim().length() > 0) {
                item.setLabel(transition.getName());
                item.setValue(transition.getName());
                list.add(item);
            }
        }
        return list;
    }

    public void showLogs() {
        logs = jbpmConsoleService.getLogs(currProcessInstance);
    }

    public String getSelectedTransition() {
        return selectedTransition;
    }

    public void setSelectedTransition(String selectedTransition) {
        this.selectedTransition = selectedTransition;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public Map<String, Object> getVariableMap() {
        return variableMap;
    }

    public void setVariableMap(Map<String, Object> map) {
        variableMap = map;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }

    public List<SelectItem> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<SelectItem> transitions) {
        this.transitions = transitions;
    }

    public Map<String, String> getFormMap() {
        if (formMap == null) {
            formMap = new HashMap();
            FileDefinition fileDefinition = currProcessInstance.getProcessDefinition().getFileDefinition();
            if (fileDefinition != null) {
                java.io.InputStream inputStream = fileDefinition.getInputStream("forms.xml");
                if (inputStream != null) {
                    Document document = XmlUtil.parseXmlInputStream(inputStream);
                    Element documentElement = document.getDocumentElement();
                    NodeList nodeList = documentElement.getElementsByTagName("form");
                    int length = nodeList.getLength();
                    for (int i = 0; i < length; i++) {
                        Element element = (Element) nodeList.item(i);
                        String itemTaskName = element.getAttribute("task");
                        String itemFormName = element.getAttribute("form");
                        if (itemTaskName != null && itemFormName != null) {
                            formMap.put(itemTaskName, itemFormName);
                        }
                    }
                }
            }
        }
        return formMap;
    }

    public void setFormMap(Map<String, String> formMap) {
        this.formMap = formMap;
    }

    public void moveToNode(Node node) {
        jbpmConsoleService.moveToNode(currProcessInstance, node);
        this.currProcessInstance = jbpmConsoleService.getProcessInstance(currProcessInstance.getId());
    }
}
