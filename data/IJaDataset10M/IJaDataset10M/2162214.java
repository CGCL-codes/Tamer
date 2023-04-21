package com.eastidea.qaforum.util;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityController;

@Name("sessionBean")
@Scope(ScopeType.SESSION)
public class SessionBean extends EntityController {

    private static final long serialVersionUID = 1L;

    protected boolean projectChanged = true;

    protected Long projectIdOld;

    protected Long projectId;

    protected Long testEnvironmentId;

    protected Long testRegionId;

    protected Long testCaseId;

    protected Long testRoundId;

    public boolean isProjectChanged() {
        return projectChanged;
    }

    public void setProjectChanged(boolean projectChanged) {
        this.projectChanged = projectChanged;
    }

    public Long getProjectIdOld() {
        return projectIdOld;
    }

    public void setProjectIdOld(Long projectIdOld) {
        this.projectIdOld = projectIdOld;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getTestEnvironmentId() {
        return testEnvironmentId;
    }

    public void setTestEnvironmentId(Long testEnvironmentId) {
        this.testEnvironmentId = testEnvironmentId;
    }

    public Long getTestRegionId() {
        return testRegionId;
    }

    public void setTestRegionId(Long testRegionId) {
        this.testRegionId = testRegionId;
    }

    public Long getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(Long testCaseId) {
        this.testCaseId = testCaseId;
    }

    public Long getTestRoundId() {
        return testRoundId;
    }

    public void setTestRoundId(Long testRoundId) {
        this.testRoundId = testRoundId;
    }
}
