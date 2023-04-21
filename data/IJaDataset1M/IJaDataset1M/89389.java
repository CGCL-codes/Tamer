package com.qasystems.qstudio.java.gui;

import com.qasystems.qstudio.java.gui.ProjectMember;

/**
 * Skeleton to be able to compile a pro version.
 * The compliance view only makes sense in a enterprise
 * environment, so there is no shared context. Otherwise
 * we have to introduce edition specific classes for each
 * class that offers a compliance view.
 */
public class ComplianceView {

    /**
   * Enterprise functionality
   */
    public ComplianceView() {
    }

    /**
   * Enterprise functionality
   */
    public ComplianceView(ProjectMember m) {
    }

    /**
   * Enterprise functionality
   */
    public ComplianceView(ProjectMember m, Project p) {
    }

    /**
   * Enterprise functionality
   */
    public synchronized void setProjectMember(ProjectMember m) {
    }

    /**
   * Enterprise functionality
   */
    public synchronized void setProject(Project p) {
    }

    /**
   * Enterprise functionality
   */
    public ProjectMember getProjectMember() {
        return (null);
    }

    /**
   * Enterprise functionality
   */
    public void openComplianceView() {
    }

    /**
   * Enterprise functionality
   */
    public void openComplianceView(ProjectMember m) {
    }
}
