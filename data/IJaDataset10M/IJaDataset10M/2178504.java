package org.in4ama.documentengine.project;

import org.in4ama.datasourcemanager.cfg.DataConfigurationMgr;
import org.in4ama.documentautomator.DocumentMgrConfigurationMgr;
import org.in4ama.documentengine.mailshots.MailshotScriptMgr;
import org.in4ama.documentengine.project.cfg.ProjectConfigurationMgr;
import org.in4ama.documentengine.project.cfg.SettingsConfigurationMgr;
import org.in4ama.documentengine.project.cfg.TestDataConfigurationMgr;

/** Aggregate class for project configuration. */
public class Project {

    private String name;

    private DataConfigurationMgr dataConfigurationMgr;

    private DocumentMgrConfigurationMgr documentMgrConfigMgr;

    private TestDataConfigurationMgr testDataConfigurationMgr;

    private ProjectConfigurationMgr projectConfigurationMgr;

    private SettingsConfigurationMgr settingsConfigurationMgr;

    private MailshotScriptMgr mailshotScriptMgr;

    private ProjectReferenceMgr referenceMgr;

    /** Creates a new empty project. */
    public Project(String name) {
        this.name = name;
        this.dataConfigurationMgr = new DataConfigurationMgr();
        this.documentMgrConfigMgr = new DocumentMgrConfigurationMgr();
        this.testDataConfigurationMgr = new TestDataConfigurationMgr();
        this.projectConfigurationMgr = new ProjectConfigurationMgr();
        this.settingsConfigurationMgr = new SettingsConfigurationMgr();
        this.mailshotScriptMgr = new MailshotScriptMgr();
        this.referenceMgr = new ProjectReferenceMgr();
    }

    /** Returns the name of this project. */
    public String getName() {
        return name;
    }

    /** Sets the data source configuration manager. */
    public void setDataConfigurationMgr(DataConfigurationMgr dataConfigMgr) {
        this.dataConfigurationMgr = dataConfigMgr;
    }

    /** Returns the associated data source configuration manager. */
    public DataConfigurationMgr getDataConfigurationMgr() {
        return dataConfigurationMgr;
    }

    /** Returns the associated document configuration manager. */
    public DocumentMgrConfigurationMgr getDocumentMgrConfigurationMgr() {
        return documentMgrConfigMgr;
    }

    /** Sets the document configuration manager. */
    public void setDocumentMgrConfigurationMgr(DocumentMgrConfigurationMgr documentMgrConfigMgr) {
        this.documentMgrConfigMgr = documentMgrConfigMgr;
    }

    /** Gets the associated test data configuration manager. */
    public TestDataConfigurationMgr getTestDataConfigurationMgr() {
        return testDataConfigurationMgr;
    }

    /** Sets the data source configuration manager. */
    public void setTestDataConfigurationMgr(TestDataConfigurationMgr testDataConfigurationMgr) {
        this.testDataConfigurationMgr = testDataConfigurationMgr;
    }

    /** Returns the project configuration manager. */
    public ProjectConfigurationMgr getProjectConfigurationMgr() {
        return projectConfigurationMgr;
    }

    /** Sets the project configuration managers. */
    public void setProjectConfigurationMgr(ProjectConfigurationMgr projectConfigurationMgr) {
        this.projectConfigurationMgr = projectConfigurationMgr;
    }

    /** Returns the project's settings configuration manager. */
    public SettingsConfigurationMgr getSettingsConfigurationMgr() {
        return settingsConfigurationMgr;
    }

    /** Returns the project's settings configuration manager. */
    public ProjectReferenceMgr getReferenceMgr() {
        return referenceMgr;
    }

    /** Sets the references mgr. */
    public void setReferencesMgr(ProjectReferenceMgr referenceMgr) {
        this.referenceMgr = referenceMgr;
    }

    /** Sets the project's settings configuration manager. */
    public void setSettingsConfigurationMgr(SettingsConfigurationMgr settingsConfigurationMgr) {
        this.settingsConfigurationMgr = settingsConfigurationMgr;
    }

    /** Returns the mailshot script manager. */
    public MailshotScriptMgr getMailshotScriptMgr() {
        return mailshotScriptMgr;
    }

    /** Sets the mailshot script manager. */
    public void setMailshotScriptMgr(MailshotScriptMgr mailshotScriptMgr) {
        this.mailshotScriptMgr = mailshotScriptMgr;
    }
}
