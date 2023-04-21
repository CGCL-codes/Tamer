package com.ecmdeveloper.plugin.core.model;

/**
 * @author ricardo.belfor
 *
 */
public interface IAction extends IObjectStoreItem {

    void setCodeModule(ICodeModule codeModule);

    String getCodeModuleVersion();
}
