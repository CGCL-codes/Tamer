package com.kbframework.workflow.operaction;

import java.io.File;
import com.kbframework.workflow.ProcessDefinition;

public interface ProcessDeployer {

    public void deployProcesses(File[] files);

    public ProcessDefinition getLastProcessDefinition(String processName);

    public ProcessDefinition findProcessDefinition(String processDefinitionId);
}
