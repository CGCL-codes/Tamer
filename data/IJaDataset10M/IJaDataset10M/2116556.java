package com.germinus.portlet.scriblog.model;

public class ScriblogEntry {

    public static enum ScriblogFormAction {

        CANCEL, SAVEDRAFT, PUBLISHENTRY, DELETEDRAFT
    }

    private String title;

    private String body;

    private ScriblogFormAction formAction;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setSaveDraft(String saveDraft) {
        this.formAction = ScriblogFormAction.SAVEDRAFT;
    }

    public void setPublishEntry(String publishEntry) {
        this.formAction = ScriblogFormAction.PUBLISHENTRY;
    }

    public void setCancel(String cancel) {
        this.formAction = ScriblogFormAction.CANCEL;
    }

    public void setDeleteDraft(String deleteDraft) {
        this.formAction = ScriblogFormAction.DELETEDRAFT;
    }

    public ScriblogFormAction getFormAction() {
        return formAction;
    }
}
