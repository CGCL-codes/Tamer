package com.icesoft.faces.component.inputfile;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.component.style.OutputStyle;
import com.icesoft.faces.context.BridgeFacesContext;
import com.icesoft.faces.context.DOMResponseWriter;
import com.icesoft.faces.env.CommonEnvironmentRequest;
import com.icesoft.faces.util.CoreUtils;
import com.icesoft.faces.util.DOMUtils;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.faces.application.FacesMessage;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.NamingContainer;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.FacesEvent;
import javax.faces.event.AbortProcessingException;
import javax.faces.FacesException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * InputFile is a JSF component class representing an ICEfaces inputFile.
 */
public class InputFile extends UICommand implements Serializable {

    private static final Log log = LogFactory.getLog(InputFile.class);

    public static final int DEFAULT = FileInfo.DEFAULT;

    public static final int UPLOADING = FileInfo.UPLOADING;

    public static final int SAVED = FileInfo.SAVED;

    public static final int INVALID = FileInfo.INVALID;

    public static final int SIZE_LIMIT_EXCEEDED = FileInfo.SIZE_LIMIT_EXCEEDED;

    public static final int UNKNOWN_SIZE = FileInfo.UNKNOWN_SIZE;

    public static final int INVALID_NAME_PATTERN = FileInfo.INVALID_NAME_PATTERN;

    public static final int UNSPECIFIED_NAME = FileInfo.UNSPECIFIED_NAME;

    public static final int INVALID_CONTENT_TYPE = FileInfo.INVALID_CONTENT_TYPE;

    public static final String INVALID_FILE_MESSAGE_ID = "com.icesoft.faces.component.inputfile.INVALID_FILE";

    public static final String SIZE_LIMIT_EXCEEDED_MESSAGE_ID = "com.icesoft.faces.component.inputfile.SIZE_LIMIT_EXCEEDED";

    public static final String UNKNOWN_SIZE_MESSAGE_ID = "com.icesoft.faces.component.inputfile.UNKNOWN_SIZE";

    public static final String INVALID_NAME_PATTERN_MESSAGE_ID = "com.icesoft.faces.component.inputfile.INVALID_NAME_PATTERN";

    public static final String UNSPECIFIED_NAME_MESSAGE_ID = "com.icesoft.faces.component.inputfile.UNSPECIFIED_NAME";

    public static final String INVALID_CONTENT_TYPE_MESSAGE_ID = "com.icesoft.faces.component.inputfile.INVALID_CONTENT_TYPE";

    public static final String FILE_UPLOAD_PREFIX = "fileUpload";

    public static final String SUBMIT_NONE = "none";

    public static final String SUBMIT_PRE_UPLOAD = "preUpload";

    public static final String SUBMIT_POST_UPLOAD = "postUpload";

    public static final String SUBMIT_PRE_POST_UPLOAD = "preAndPostUpload";

    private Boolean disabled;

    private String style;

    private String styleClass;

    private String label;

    private String enabledOnUserRole;

    private String renderedOnUserRole;

    private String title;

    private int height = 30;

    private int width = 500;

    private int inputTextSize = 35;

    private String inputTextClass;

    private String fileNamePattern;

    private boolean uniqueFolder = true;

    private boolean uniqueFolderSet = false;

    private String uploadDirectory;

    private Boolean uploadDirectoryAbsolute;

    private FileInfo fileInfo = new FileInfo();

    private long sizeMax;

    private MethodBinding progressListener;

    private Boolean progressRender;

    private String submitOnUpload;

    private Boolean failOnEmptyFile;

    /**
     * <p>Return the value of the <code>COMPONENT_TYPE</code> of this
     * component.</p>
     */
    public String getComponentType() {
        return "com.icesoft.faces.File";
    }

    /**
     * <p>Return the value of the <code>RENDERER_TYPE</code> of this
     * component.</p>
     */
    public String getRendererType() {
        return "com.icesoft.faces.Upload";
    }

    /**
     * <p>Return the value of the <code>COMPONENT_FAMILY</code> of this
     * component.</p>
     */
    public String getFamily() {
        return "com.icesoft.faces.File";
    }

    /**
     * @see javax.faces.component.UIComponentBase#broadcast(javax.faces.event.FacesEvent)
     */
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);
        if (event instanceof InputFileProgressEvent) {
            if (progressListener != null) {
                progressListener.invoke(FacesContext.getCurrentInstance(), new Object[] { event });
            }
            setPreUpload(false);
            setPostUpload(false);
        } else if (event instanceof InputFileSetFileEvent) {
            updateFileValueBinding();
        }
    }

    /**
     * When we're rendering after the actionListener has fired, in the
     * last lifecycle for a file upload, capture the iframeContent,
     * so we can send that back in the passed-in UploadStateHolder.
     * 
     * We leave the InputFile's most up-to-date configuration in the
     * session, so that when a file upload starts, we'll already have what
     * we need to begin processing it.
     * 
     * @return The UploadConfig that was put into the session
     */
    protected UploadConfig storeContentAndConfig(BridgeFacesContext facesContext, String clientId, String iframeContent) {
        Map parameterMap = facesContext.getExternalContext().getRequestParameterMap();
        UploadStateHolder uploadState = (UploadStateHolder) parameterMap.get(clientId);
        if (uploadState != null && uploadState.getFileInfo().isFinished()) {
            uploadState.setIframeContent(iframeContent);
        }
        UploadConfig uploadConfig = getComponentUploadConfig();
        Object sessionObj = facesContext.getExternalContext().getSession(false);
        if (sessionObj != null) {
            synchronized (sessionObj) {
                Map map = (Map) facesContext.getExternalContext().getSessionMap();
                String key = facesContext.getViewNumber() + " " + clientId;
                if (log.isDebugEnabled()) log.debug("  session map key: " + key);
                map.put(key, uploadConfig);
            }
        }
        if (log.isDebugEnabled()) log.debug("  uploadConfig: " + uploadConfig);
        return uploadConfig;
    }

    protected void renderIFrame(Writer writer, BridgeFacesContext context) throws IOException {
        writer.write("<html style=\"overflow:hidden;\">");
        ArrayList outputStyleComponents = findOutputStyleComponents(context.getViewRoot());
        if (outputStyleComponents != null) {
            writer.write("<head>");
            for (int i = 0; i < outputStyleComponents.size(); i++) {
                OutputStyle outputStyle = (OutputStyle) outputStyleComponents.get(i);
                String href = outputStyle.getHref();
                if ((href != null) && (href.length() > 0)) {
                    href = CoreUtils.resolveResourceURL(context, href);
                    writer.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + href + "\">");
                }
            }
            writer.write("</head>");
        }
        String srv = getUploadServletPath(context);
        writer.write("<body style=\"background-color:transparent; overflow:hidden\"><form method=\"post\" action=\"" + srv + "\" enctype=\"multipart/form-data\" id=\"fileUploadForm\">");
        writer.write("<input type=\"hidden\" name=\"ice.component\" value=\"");
        writer.write(this.getClientId(context));
        writer.write("\"/>");
        writer.write("<input type=\"hidden\" name=\"ice.view\"");
        writer.write(" value=\"" + context.getViewNumber() + "\"/>");
        writer.write("<input type=\"file\" name=\"upload\"");
        writer.write(" size=\"" + getInputTextSize() + "\"");
        String inputTextClass = getInputTextClass();
        if (inputTextClass != null) writer.write(" class=\"" + inputTextClass + "\"");
        String title = getTitle();
        if (title != null) writer.write(" title=\"" + title + "\"");
        writer.write("/>");
        writer.write("<input type=\"submit\" value=\"" + getLabel() + "\"");
        String buttonClass = getButtonClass();
        if (buttonClass != null) writer.write(" class=\"" + buttonClass + "\"");
        if (isDisabled()) writer.write(" disabled=\"disabled\"");
        writer.write("/>");
        ResponseWriter responseWriter = context.getResponseWriter();
        Node node = ((DOMResponseWriter) responseWriter).getSavedNode();
        Node copy;
        if (node != null) {
            copy = node.cloneNode(true);
            ((Element) copy).setAttribute("id", "fileUploadForm" + NamingContainer.SEPARATOR_CHAR + ((Element) copy).getAttribute("id"));
            String divAsString = DOMUtils.nodeToString(copy);
            if (log.isDebugEnabled()) {
                log.debug("State saving view contents: " + divAsString);
            }
            writer.write(divAsString);
        }
        writer.write("</form>");
        writer.write("</body></html>");
    }

    private String getUploadServletPath(BridgeFacesContext context) {
        String requestContextPath = null;
        if (context != null) {
            ExternalContext externalContext = context.getExternalContext();
            if (externalContext != null) {
                requestContextPath = externalContext.getRequestContextPath();
            }
        }
        if (requestContextPath == null || requestContextPath.length() == 0) {
            CommonEnvironmentRequest request = ((CommonEnvironmentRequest) context.getExternalContext().getRequest());
            return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/uploadHtml";
        } else return requestContextPath + "/uploadHtml";
    }

    /**
     * @deprecated use getFileInfo().getException() instead. 
     */
    public Throwable getUploadException() {
        return fileInfo.getException();
    }

    /**
     * <p/>
     * Set the value of the <code>label</code> property. </p>
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * <p/>
     * Set the value of the <code>uniqueFolder</code> property. </p>
     */
    public void setUniqueFolder(boolean uniqueFolder) {
        if (uniqueFolder != this.uniqueFolder) {
            this.uniqueFolder = uniqueFolder;
        }
        this.uniqueFolderSet = true;
    }

    /**
     * <p/>
     * Return the value of the <code>uniqueFolder</code> property. </p>
     */
    public boolean isUniqueFolder() {
        if (this.uniqueFolderSet) {
            return (this.uniqueFolder);
        }
        ValueBinding vb = getValueBinding("uniqueFolder");
        if (vb != null) {
            return (Boolean.TRUE.equals(vb.getValue(getFacesContext())));
        }
        return true;
    }

    /**
     * This is for internal use, so we can tell when this property has not been
     * set on the component.
     */
    protected Boolean getUniqueFolder() {
        if (this.uniqueFolderSet) {
            return Boolean.valueOf(this.uniqueFolder);
        }
        ValueBinding vb = getValueBinding("uniqueFolder");
        if (vb != null) {
            return (Boolean) vb.getValue(getFacesContext());
        }
        return null;
    }

    /**
     * <p/>
     * Return the value of the <code>label</code> property. </p>
     */
    public String getLabel() {
        if (label != null) {
            return label;
        }
        ValueBinding vb = getValueBinding("label");
        return vb != null ? (String) vb.getValue(getFacesContext()) : "Upload";
    }

    /**
     * <p>Return the value of the <code>disabled</code> property.</p>
     */
    public boolean isDisabled() {
        if (!Util.isEnabledOnUserRole(this)) {
            return true;
        } else {
            if (disabled != null) {
                return disabled.booleanValue();
            }
            ValueBinding vb = getValueBinding("disabled");
            Boolean boolVal = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
            return boolVal != null ? boolVal.booleanValue() : false;
        }
    }

    /**
     * <p>Set the value of the <code>disabled</code> property.</p>
     */
    public void setDisabled(boolean disabled) {
        this.disabled = Boolean.valueOf(disabled);
    }

    /**
     * <p>Set the value of the <code>enabledOnUserRole</code> property.</p>
     */
    public void setEnabledOnUserRole(String enabledOnUserRole) {
        this.enabledOnUserRole = enabledOnUserRole;
    }

    /**
     * <p>Return the value of the <code>enabledOnUserRole</code> property.</p>
     */
    public String getEnabledOnUserRole() {
        if (enabledOnUserRole != null) {
            return enabledOnUserRole;
        }
        ValueBinding vb = getValueBinding("enabledOnUserRole");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public void setUploadDirectory(String uploadDirectory) {
        this.uploadDirectory = uploadDirectory;
    }

    public String getUploadDirectory() {
        if (uploadDirectory != null) {
            return uploadDirectory;
        }
        ValueBinding vb = getValueBinding("uploadDirectory");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public void setUploadDirectoryAbsolute(Boolean uploadDirectoryAbsolute) {
        this.uploadDirectoryAbsolute = uploadDirectoryAbsolute;
    }

    public Boolean getUploadDirectoryAbsolute() {
        if (uploadDirectoryAbsolute != null) {
            return uploadDirectoryAbsolute;
        }
        ValueBinding vb = getValueBinding("uploadDirectoryAbsolute");
        return vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
    }

    public void setProgressRender(Boolean progressRender) {
        this.progressRender = progressRender;
    }

    public Boolean getProgressRender() {
        if (progressRender != null) {
            return progressRender;
        }
        ValueBinding vb = getValueBinding("progressRender");
        return vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
    }

    public boolean renderOnProgress() {
        Boolean progRend = getProgressRender();
        return (progRend != null) ? progRend.booleanValue() : true;
    }

    public void setSubmitOnUpload(String submitOnUpload) {
        this.submitOnUpload = submitOnUpload;
    }

    public String getSubmitOnUpload() {
        if (submitOnUpload != null) {
            return submitOnUpload;
        }
        ValueBinding vb = getValueBinding("submitOnUpload");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public String getValidatedSubmitOnUpload() {
        String sol = getSubmitOnUpload();
        if (sol == null || sol.length() == 0) {
            sol = SUBMIT_NONE;
        } else {
            if (!sol.equals(SUBMIT_NONE) && !sol.equals(SUBMIT_PRE_UPLOAD) && !sol.equals(SUBMIT_POST_UPLOAD) && !sol.equals(SUBMIT_PRE_POST_UPLOAD)) {
                throw new FacesException(new IllegalArgumentException("Invalid value for ice:inputFile's submitOnUpload property: " + sol));
            }
        }
        return sol;
    }

    /**
     * <p>Set the value of the <code>renderedOnUserRole</code> property.</p>
     */
    public void setRenderedOnUserRole(String renderedOnUserRole) {
        this.renderedOnUserRole = renderedOnUserRole;
    }

    /**
     * <p>Return the value of the <code>renderedOnUserRole</code> property.</p>
     */
    public String getRenderedOnUserRole() {
        if (renderedOnUserRole != null) {
            return renderedOnUserRole;
        }
        ValueBinding vb = getValueBinding("renderedOnUserRole");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Return the value of the <code>rendered</code> property.</p>
     */
    public boolean isRendered() {
        if (!Util.isRenderedOnUserRole(this)) {
            return false;
        }
        return super.isRendered();
    }

    /**
     * <p>Set the value of the <code>style</code> property.</p>
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * <p>Return the value of the <code>style</code> property.</p>
     */
    public String getStyle() {
        if (style != null) {
            return style;
        }
        ValueBinding vb = getValueBinding("style");
        return vb != null ? (String) vb.getValue(getFacesContext()) : "border-collapse:collapse; border-spacing:0px; padding:0px;";
    }

    /**
     * <p>Set the value of the <code>styleClass</code> property.</p>
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * <p>Return the value of the <code>styleClass</code> property.</p>
     */
    public String getStyleClass() {
        return Util.getQualifiedStyleClass(this, styleClass, CSS_DEFAULT.ICE_FILE_UPLOAD_BASE_CLASS, "styleClass", isDisabled());
    }

    /**
     * <p>Gets the state of the instance as a <code>Serializable</code>
     * Object.</p>
     */
    public Object saveState(FacesContext context) {
        Object values[] = new Object[31];
        values[0] = super.saveState(context);
        values[1] = disabled;
        values[2] = style;
        values[3] = styleClass;
        values[4] = label;
        values[5] = enabledOnUserRole;
        values[6] = renderedOnUserRole;
        values[7] = title;
        values[8] = new Integer(height);
        values[9] = new Integer(width);
        values[10] = new Integer(inputTextSize);
        values[11] = inputTextClass;
        values[12] = fileNamePattern;
        values[13] = uniqueFolder ? Boolean.TRUE : Boolean.FALSE;
        values[14] = uniqueFolderSet ? Boolean.TRUE : Boolean.FALSE;
        values[15] = uploadDirectory;
        values[16] = uploadDirectoryAbsolute;
        if (log.isDebugEnabled()) log.debug("#### InputFile.saveState()  fileInfo: " + fileInfo);
        values[17] = fileInfo;
        values[18] = new Long(sizeMax);
        values[19] = saveAttachedState(context, progressListener);
        values[20] = progressRender;
        values[21] = submitOnUpload;
        values[22] = tabindex;
        values[23] = onchange;
        values[24] = accesskey;
        values[25] = onfocus;
        values[26] = accept;
        values[27] = onblur;
        values[28] = buttonClass;
        values[29] = size;
        values[30] = failOnEmptyFile;
        return ((Object) (values));
    }

    /**
     * <p>Perform any processing required to restore the state from the entries
     * in the state Object.</p>
     */
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        disabled = (Boolean) values[1];
        style = (String) values[2];
        styleClass = (String) values[3];
        label = (String) values[4];
        enabledOnUserRole = (String) values[5];
        renderedOnUserRole = (String) values[6];
        title = (String) values[7];
        height = ((Integer) values[8]).intValue();
        width = ((Integer) values[9]).intValue();
        inputTextSize = ((Integer) values[10]).intValue();
        inputTextClass = (String) values[11];
        fileNamePattern = (String) values[12];
        uniqueFolder = ((Boolean) values[13]).booleanValue();
        uniqueFolderSet = ((Boolean) values[14]).booleanValue();
        uploadDirectory = (String) values[15];
        uploadDirectoryAbsolute = (Boolean) values[16];
        fileInfo = (FileInfo) values[17];
        if (log.isDebugEnabled()) log.debug("#### InputFile.restoreState()  fileInfo: " + fileInfo);
        sizeMax = ((Long) values[18]).longValue();
        progressListener = (MethodBinding) restoreAttachedState(context, values[19]);
        progressRender = (Boolean) values[20];
        submitOnUpload = (String) values[21];
        tabindex = (String) values[22];
        onchange = (String) values[23];
        accesskey = (String) values[24];
        onfocus = (String) values[25];
        accept = (String) values[26];
        onblur = (String) values[27];
        buttonClass = (String) values[28];
        size = (String) values[29];
        failOnEmptyFile = (Boolean) values[30];
    }

    /**
     * <p>Set the value of the <code>title</code> property.</p>
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * <p>Return the value of the <code>title</code> property.</p>
     */
    public String getTitle() {
        if (title != null) {
            return title;
        }
        ValueBinding vb = getValueBinding("title");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    private String size = null;

    /**
     * <p>Set the value of the <code>size</code> property.</p>
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * <p>Return the value of the <code>size</code> property.</p>
     */
    public String getSize() {
        if (size != null) {
            return size;
        }
        ValueBinding vb = getValueBinding("size");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public int getHeight() {
        ValueBinding vb = getValueBinding("height");
        if (vb != null) {
            Integer value = (Integer) vb.getValue(getFacesContext());
            if (null == value) {
                return height;
            }
            return (value.intValue());
        } else {
            return (this.height);
        }
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        ValueBinding vb = getValueBinding("width");
        if (vb != null) {
            Integer value = (Integer) vb.getValue(getFacesContext());
            if (null == value) {
                return width;
            }
            return (value.intValue());
        } else {
            return (this.width);
        }
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getInputTextSize() {
        ValueBinding vb = getValueBinding("inputTextSize");
        if (vb != null) {
            Integer value = (Integer) vb.getValue(getFacesContext());
            if (null == value) {
                return inputTextSize;
            }
            return (value.intValue());
        } else {
            return (this.inputTextSize);
        }
    }

    public void setInputTextSize(int inputTextSize) {
        this.inputTextSize = inputTextSize;
    }

    public String getFileNamePattern() {
        if (fileNamePattern != null) {
            return fileNamePattern;
        }
        ValueBinding vb = getValueBinding("fileNamePattern");
        return vb != null ? (String) vb.getValue(getFacesContext()) : ".+";
    }

    public void setFileNamePattern(String fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
    }

    public void setInputTextClass(String inputTextClass) {
        this.inputTextClass = inputTextClass;
    }

    public String getInputTextClass() {
        return Util.getQualifiedStyleClass(this, inputTextClass, CSS_DEFAULT.ICE_FILE_UPLOAD_DEFAULT_INPUT_TEXT_CLASS, "inputTextClass", isDisabled());
    }

    private String buttonClass = null;

    public void setButtonClass(String buttonClass) {
        this.buttonClass = buttonClass;
    }

    public String getButtonClass() {
        return Util.getQualifiedStyleClass(this, buttonClass, CSS_DEFAULT.ICE_FILE_UPLOAD_DEFAULT_BUTTON_CLASS, "buttonClass", isDisabled());
    }

    boolean isRegister() {
        return false;
    }

    public void setRegister(FacesContext facesContext) {
    }

    /**
     * <p/>
     * Return the value of the <code>fileInfo</code> property. </p>
     */
    public FileInfo getFileInfo() {
        return (FileInfo) fileInfo.clone();
    }

    /**
     * Not to be called by applications. Used internally between the
     * UploadServer, InputFile and FileUploadPhaseListener.
     */
    public void setFileInfo(FileInfo fileInfo) {
        if (log.isDebugEnabled()) log.debug("#### InputFile.setFileInfo()  fileInfo: " + fileInfo);
        this.fileInfo = fileInfo;
    }

    /**
     * This is the configuration, as represented by the values specified on
     * this InputFile component. Some of the values take precedence over
     * context-params, if they're non-null here, the combination of which
     * will produce a final configuration for the UploadServer to use.
     * 
     * Not intended for use by application use.
     */
    public UploadConfig getComponentUploadConfig() {
        FacesContext context = FacesContext.getCurrentInstance();
        String clientId = getClientId(context);
        UIComponent form = DomBasicRenderer.findForm(this);
        String formClientId = (form != null) ? form.getClientId(context) : null;
        Long sizeMax = null;
        String fileNamePattern = getFileNamePattern();
        Boolean uniqueFolder = getUniqueFolder();
        String uploadDirectory = getUploadDirectory();
        Boolean uploadDirectoryAbsolute = getUploadDirectoryAbsolute();
        boolean progressRender = renderOnProgress();
        boolean progressListener = (getProgressListener() != null);
        boolean failOnEmptyFile = isFailOnEmptyFile();
        UploadConfig uploadConfig = new UploadConfig(clientId, formClientId, sizeMax, fileNamePattern, uniqueFolder, uploadDirectory, uploadDirectoryAbsolute, progressRender, progressListener, failOnEmptyFile);
        return uploadConfig;
    }

    /**
     * <p/>
     * Return the value of the <code>file</code> property. </p>
     * @deprecated use getFileInfo().getFile() instead.
     */
    public File getFile() {
        return fileInfo.getFile();
    }

    /**
     * <p/>
     * Set the value of the <code>file<code> property. </p>
     * @deprecated This should be a read-only property
     */
    public void setFile(File file) {
    }

    /**
     * In the 1.5.3 codebase, there was a writeable ValueBinding named "file"
     * that would be updated when a new file was saved. This provides
     * backwards compatibility with that.
     */
    protected void updateFileValueBinding() {
        try {
            ValueBinding vb = getValueBinding("file");
            if (vb != null) vb.setValue(FacesContext.getCurrentInstance(), getFile());
        } catch (Exception e) {
            log.warn("The InputFile's file attribute has a ValueBinding, whose value could not be set", e);
        }
    }

    /**
     * @return DEFAULT, UPLOADING, SAVED, or one of the error statuses.
     * @deprecated use getFileInfo().getStatus() instead.
     */
    public int getStatus() {
        return fileInfo.getStatus();
    }

    /**
     * <p>Return the value of the <code>fileName</code> property.</p>
     *
     * @deprecated use getFileInfo().getFileName() instead.
     */
    public String getFilename() {
        return fileInfo.getFileName();
    }

    /**
     * <p>Set the value of the <code>fileName</code> property.</p>
     *
     * @deprecated This should be a read-only property
     */
    public void setFilename(String filename) {
        fileInfo.setFileName(filename);
    }

    /**
     * <p>Return the value of the <code>size</code> property.</p>
     *
     * @deprecated use getFileInfo().getSize() instead.
     */
    public long getFilesize() {
        return fileInfo.getSize();
    }

    /**
     * <p>Set the value of the <code>size</code> property.</p>
     * @deprecated This should be a read-only property
     */
    public void setFilesize(long filesize) {
        fileInfo.setSize(filesize);
    }

    public long getSizeMax() {
        return sizeMax;
    }

    public MethodBinding getProgressListener() {
        return progressListener;
    }

    public void setProgressListener(MethodBinding binding) {
        progressListener = binding;
    }

    public int getProgress() {
        return fileInfo.getPercent();
    }

    /**
     * @deprecated This is not used internally any more, and it's hard to
     * imagine a valid scenario for the application to call this method.
     * Potentially applications, written before the addition of the
     * reset() method, may use this?
     */
    public void setProgress(int i) {
        fileInfo.setPercent(i);
        if (getProgressListener() != null) getProgressListener().invoke(FacesContext.getCurrentInstance(), new Object[] { new InputFileProgressEvent(this) });
    }

    void setPreUpload(boolean p) {
        fileInfo.setPreUpload(p);
    }

    void setPostUpload(boolean p) {
        fileInfo.setPostUpload(p);
    }

    public String getCssFile() {
        return null;
    }

    private String getDisabled() {
        return null;
    }

    private String getStyleClassString() {
        return null;
    }

    private String getStyleString() {
        return null;
    }

    private String getStyleInfo() {
        return null;
    }

    private String getInputTextClassString() {
        return null;
    }

    private String getButtonClassString() {
        return null;
    }

    private String getTitleAsString() {
        return null;
    }

    private static ArrayList findOutputStyleComponents(UIComponent parent) {
        ArrayList returnValue = null;
        if (parent.getChildCount() == 0) return returnValue;
        Iterator children = parent.getChildren().iterator();
        UIComponent childComponent = null;
        while (children.hasNext()) {
            childComponent = (UIComponent) children.next();
            if (childComponent instanceof OutputStyle) {
                if (returnValue == null) {
                    returnValue = new ArrayList();
                }
                returnValue.add(childComponent);
            } else {
                ArrayList outputStyleComponents = findOutputStyleComponents(childComponent);
                if (outputStyleComponents != null) {
                    if (returnValue == null) {
                        returnValue = outputStyleComponents;
                    } else {
                        returnValue.addAll(outputStyleComponents);
                    }
                }
            }
        }
        return returnValue;
    }

    private String onfocus;

    public void setOnfocus(String onfocus) {
        this.onfocus = onfocus;
    }

    public String getOnfocus() {
        if (onfocus != null) {
            return onfocus;
        }
        ValueBinding vb = getValueBinding("onfocus");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    private String onchange;

    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }

    public String getOnchange() {
        if (onchange != null) {
            return onchange;
        }
        ValueBinding vb = getValueBinding("onchange");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    private String accept;

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getAccept() {
        if (accept != null) {
            return accept;
        }
        ValueBinding vb = getValueBinding("accept");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    private String accesskey;

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public String getAccesskey() {
        if (accesskey != null) {
            return accesskey;
        }
        ValueBinding vb = getValueBinding("accesskey");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    private String onblur;

    public void setOnblur(String onblur) {
        this.onblur = onblur;
    }

    public String getOnblur() {
        if (onblur != null) {
            return onblur;
        }
        ValueBinding vb = getValueBinding("onblur");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    private String tabindex;

    public void setTabindex(String tabindex) {
        this.tabindex = tabindex;
    }

    public String getTabindex() {
        if (tabindex != null) {
            return tabindex;
        }
        ValueBinding vb = getValueBinding("tabindex");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public void reset() {
        fileInfo = new FileInfo();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            String clientId = getClientId(facesContext);
            Iterator msgs = facesContext.getMessages(clientId);
            if (msgs != null) {
                while (msgs.hasNext()) {
                    FacesMessage msg = (FacesMessage) msgs.next();
                    msgs.remove();
                }
            }
        }
    }

    public void setFailOnEmptyFile(boolean failOnEmptyFile) {
        this.failOnEmptyFile = new Boolean(failOnEmptyFile);
    }

    public boolean isFailOnEmptyFile() {
        if (failOnEmptyFile != null) {
            return failOnEmptyFile.booleanValue();
        }
        ValueBinding vb = getValueBinding("failOnEmptyFile");
        return vb != null ? ((Boolean) vb.getValue(getFacesContext())).booleanValue() : true;
    }
}
