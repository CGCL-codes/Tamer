package com.ivis.xprocess.ui.managementbar;

public interface IManagementBarContainer extends IManagementElement {

    /**
     * Add a child to the management bar container.
     *
     * @param managementElement
     */
    void addChildXProcessWidget(IManagementElement managementElement);
}
