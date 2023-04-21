package csiebug.web.taglib.form.upload;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;
import csiebug.web.html.form.upload.HtmlUploadify;

public class UploadifyTag extends BodyTagSupport implements TryCatchFinally {

    private static final long serialVersionUID = 1L;

    private String uploadId;

    private String auto;

    private String buttonImg;

    private String buttonText;

    private String cancelImg;

    private String checkScript;

    private String displayData;

    private String version;

    private String expressInstall;

    private String fileExt;

    private String folder;

    private String height;

    private String hideButton;

    private String method;

    private String multi;

    private String queueID;

    private String queueSizeLimit;

    private String removeCompleted;

    private String rollover;

    private String script;

    private String scriptData;

    private String simUploadLimit;

    private String sizeLimit;

    private String width;

    private String onAllComplete;

    private String onCancel;

    private String onCheck;

    private String onClearQueue;

    private String onComplete;

    private String onError;

    private String onInit;

    private String onOpen;

    private String onProgress;

    private String onQueueFull;

    private String onSelect;

    private String onSelectOnce;

    private String onSWFReady;

    public int doStartTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();
            HtmlUploadify htmlUploadify = new HtmlUploadify(version, uploadId, auto, buttonImg, buttonText, cancelImg, checkScript, displayData, expressInstall, fileExt, folder, height, hideButton, method, multi, queueID, queueSizeLimit, removeCompleted, rollover, script, scriptData, simUploadLimit, sizeLimit, width, onAllComplete, onCancel, onCheck, onClearQueue, onComplete, onError, onInit, onOpen, onProgress, onQueueFull, onSelect, onSelectOnce, onSWFReady);
            out.println(htmlUploadify.render());
        } catch (Exception e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }

    public void doCatch(Throwable e) throws Throwable {
        throw new JspException("UploadifyTag Problem: " + e.getMessage());
    }

    public void doFinally() {
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setCheckScript(String checkScript) {
        this.checkScript = checkScript;
    }

    public String getCheckScript() {
        return checkScript;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    public String getAuto() {
        return auto;
    }

    public void setButtonImg(String buttonImg) {
        this.buttonImg = buttonImg;
    }

    public String getButtonImg() {
        return buttonImg;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setDisplayData(String displayData) {
        this.displayData = displayData;
    }

    public String getDisplayData() {
        return displayData;
    }

    public void setExpressInstall(String expressInstall) {
        this.expressInstall = expressInstall;
    }

    public String getExpressInstall() {
        return expressInstall;
    }

    public void setCancelImg(String cancelImg) {
        this.cancelImg = cancelImg;
    }

    public String getCancelImg() {
        return cancelImg;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHeight() {
        return height;
    }

    public void setHideButton(String hideButton) {
        this.hideButton = hideButton;
    }

    public String getHideButton() {
        return hideButton;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setMulti(String multi) {
        this.multi = multi;
    }

    public String getMulti() {
        return multi;
    }

    public void setQueueSizeLimit(String queueSizeLimit) {
        this.queueSizeLimit = queueSizeLimit;
    }

    public String getQueueSizeLimit() {
        return queueSizeLimit;
    }

    public void setRemoveCompleted(String removeCompleted) {
        this.removeCompleted = removeCompleted;
    }

    public String getRemoveCompleted() {
        return removeCompleted;
    }

    public void setRollover(String rollover) {
        this.rollover = rollover;
    }

    public String getRollover() {
        return rollover;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getScript() {
        return script;
    }

    public void setQueueID(String queueID) {
        this.queueID = queueID;
    }

    public String getQueueID() {
        return queueID;
    }

    public void setScriptData(String scriptData) {
        this.scriptData = scriptData;
    }

    public String getScriptData() {
        return scriptData;
    }

    public void setSimUploadLimit(String simUploadLimit) {
        this.simUploadLimit = simUploadLimit;
    }

    public String getSimUploadLimit() {
        return simUploadLimit;
    }

    public void setSizeLimit(String sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    public String getSizeLimit() {
        return sizeLimit;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getWidth() {
        return width;
    }

    public void setOnAllComplete(String onAllComplete) {
        this.onAllComplete = onAllComplete;
    }

    public String getOnAllComplete() {
        return onAllComplete;
    }

    public void setOnCancel(String onCancel) {
        this.onCancel = onCancel;
    }

    public String getOnCancel() {
        return onCancel;
    }

    public void setOnCheck(String onCheck) {
        this.onCheck = onCheck;
    }

    public String getOnCheck() {
        return onCheck;
    }

    public void setOnClearQueue(String onClearQueue) {
        this.onClearQueue = onClearQueue;
    }

    public String getOnClearQueue() {
        return onClearQueue;
    }

    public void setOnComplete(String onComplete) {
        this.onComplete = onComplete;
    }

    public String getOnComplete() {
        return onComplete;
    }

    public void setOnError(String onError) {
        this.onError = onError;
    }

    public String getOnError() {
        return onError;
    }

    public void setOnInit(String onInit) {
        this.onInit = onInit;
    }

    public String getOnInit() {
        return onInit;
    }

    public void setOnOpen(String onOpen) {
        this.onOpen = onOpen;
    }

    public String getOnOpen() {
        return onOpen;
    }

    public void setOnProgress(String onProgress) {
        this.onProgress = onProgress;
    }

    public String getOnProgress() {
        return onProgress;
    }

    public void setOnQueueFull(String onQueueFull) {
        this.onQueueFull = onQueueFull;
    }

    public String getOnQueueFull() {
        return onQueueFull;
    }

    public void setOnSelect(String onSelect) {
        this.onSelect = onSelect;
    }

    public String getOnSelect() {
        return onSelect;
    }

    public void setOnSelectOnce(String onSelectOnce) {
        this.onSelectOnce = onSelectOnce;
    }

    public String getOnSelectOnce() {
        return onSelectOnce;
    }

    public void setOnSWFReady(String onSWFReady) {
        this.onSWFReady = onSWFReady;
    }

    public String getOnSWFReady() {
        return onSWFReady;
    }
}
