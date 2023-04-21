package net.infordata.ifw2.web.tags;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import net.infordata.ifw2.web.bnds.IForm;
import net.infordata.ifw2.web.bnds.IFormDecorator;
import net.infordata.ifw2.web.bnds.IFormSet;
import net.infordata.ifw2.web.ctrl.FlowContext;
import net.infordata.ifw2.web.ctrl.IFlow;
import net.infordata.ifw2.web.tags.KeyActionTag.Key;
import net.infordata.ifw2.web.util.CharArrayWriter;
import net.infordata.ifw2.web.util.WEBUtil;
import net.infordata.ifw2.web.view.ContentPart;
import net.infordata.ifw2.web.view.ECSAdapter;
import net.infordata.ifw2.web.view.RendererContext;
import net.infordata.ifw2.web.view.WrapBodyTag;

/**
 * A form is enabled if it is in an active-flow (ie it is part of the chain of current states and its 
 * current child state is not a modal one).<br>
 * If it is binded to an {@link IForm} then it is enabled only if the binded object is enabled. 
 * When binded to an {@link IForm} a state can be specified, if so it is enabled if and only if the 
 * state is the current one.   
 * @author valentino.proietti
 */
public class FormTag extends WrapBodyTag implements IComponent {

    private static final long serialVersionUID = 1L;

    public static final String FORMS_ATTR = FormTag.class.getName() + ".forms";

    private String ivUniqueId;

    private String ivUniqueExtId;

    private String ivStyle;

    private String ivClass;

    private boolean ivEnabled = true;

    private boolean ivChainEnabled = ivEnabled;

    private String ivBindState;

    private String ivBind;

    private IForm ivBindedForm;

    private boolean ivReadOnly = false;

    private FormTag ivParentFormTag;

    private boolean ivParentOfTheCurrentForm;

    private Map<String, IFormComponent> ivComponents = new HashMap<String, IFormComponent>();

    String ivDefaultFocusedCSAC;

    Integer ivDefaultFocusedCSACPriority;

    private String ivLastFocusedCSAC;

    private IComponent ivParentComponent;

    private boolean ivSkipFocus;

    private Map<String, Object> ivFormScope = new HashMap<String, Object>();

    private boolean ivParentFromTagStack = false;

    private WrapBodyTag ivTagFromStack = null;

    @Override
    public void release() {
        super.release();
        ivUniqueId = null;
        ivUniqueExtId = null;
        ivStyle = null;
        ivClass = null;
        ivEnabled = true;
        ivChainEnabled = ivEnabled;
        ivBindState = null;
        ivBind = null;
        ivBindedForm = null;
        ivDefaultFocusedCSAC = null;
        ivDefaultFocusedCSACPriority = null;
        ivLastFocusedCSAC = null;
        ivComponents.clear();
        ivParentComponent = null;
        ivReadOnly = false;
        ivParentFormTag = null;
        ivParentOfTheCurrentForm = false;
        ivSkipFocus = false;
        ivFormScope.clear();
        ivParentFromTagStack = false;
        ivTagFromStack = null;
    }

    public final Map<String, Object> getFormScope() {
        return ivFormScope;
    }

    private void setParentOfTheCurrentForm() {
        ivParentOfTheCurrentForm = true;
        if (ivParentFormTag != null) ivParentFormTag.setParentOfTheCurrentForm();
    }

    void updateParentKeys() {
        if (ivParentOfTheCurrentForm) {
            Set<Key> myKeys = _cast(ivFormScope.get(KeyActionTag.PARENT_KEYS_ATTR));
            if (myKeys != null && myKeys.size() > 0) {
                RendererContext ctx = RendererContext.get();
                Map<String, Object> modalScope = ctx.getCurrentModalScope();
                Set<Key> parentKeys = _cast(modalScope.get(KeyActionTag.PARENT_KEYS_ATTR));
                if (parentKeys == null) {
                    parentKeys = new LinkedHashSet<Key>();
                    modalScope.put(KeyActionTag.PARENT_KEYS_ATTR, parentKeys);
                }
                for (Key myKey : myKeys) {
                    if (!parentKeys.contains(myKey)) parentKeys.add(myKey);
                }
            }
        }
    }

    public final void setName(String name) {
        setId(name);
    }

    public final String getName() {
        return getId();
    }

    public final String getMangledName() {
        return ivUniqueExtId + ContentPart.UID_SEPARATOR + getName();
    }

    public final String createMangledName(IFormComponent comp) {
        return createMangledName(comp.getName());
    }

    final String createMangledName(String name) {
        return getMangledName() + ContentPart.UID_SEPARATOR + name;
    }

    /**
   * @return the script code to access the html form if it is non a nested form,
   *   otherwise the returned element is a div. 
   */
    @Override
    public final String getClientScriptAccessCode() {
        if (ivParentFormTag != null) {
            return "document.getElementById('" + ivUniqueExtId + "')";
        }
        return "document.forms[\"" + ivUniqueExtId + "\"]";
    }

    /**
   * @return always return the client side script code to reach the html form object.
   */
    public final String getHtmlFormScriptAccessCode() {
        if (ivParentFormTag != null) {
            return ivParentFormTag.getHtmlFormScriptAccessCode();
        }
        return "document.forms[\"" + ivUniqueExtId + "\"]";
    }

    public final boolean isSubForm() {
        return ivParentFormTag != null;
    }

    public void setStyle(String style) {
        ivStyle = style;
    }

    public void setCssClass(String cssClass) {
        ivClass = cssClass;
    }

    @Override
    public final boolean isEnabled() {
        return ivEnabled;
    }

    public final void setEnabled(boolean enabled) {
        ivEnabled = enabled;
    }

    @Override
    public final boolean isChainEnabled() {
        return ivChainEnabled;
    }

    public final boolean isReadOnly() {
        return ivReadOnly;
    }

    public final void setReadOnly(boolean readOnly) {
        ivReadOnly = readOnly;
    }

    /**
   * @param stateId - the state which owns the binded form, otherwise the
   *   current flow is used.
   */
    public final void setBindState(String stateId) {
        ivBindState = stateId;
    }

    public final void setBind(String formName) {
        ivBind = formName;
    }

    public final void setSkipFocus(boolean skipFocus) {
        ivSkipFocus = skipFocus;
    }

    /**
   * @return the form name, which is univoque in the current flow.
   */
    public final String getFormName() {
        return getName();
    }

    public final IForm getBindedForm() {
        return ivBindedForm;
    }

    boolean isMultiPart() {
        return false;
    }

    public IFormComponent getComponent(String name) {
        return ivComponents.get(name);
    }

    public final IComponent getParentComponent() {
        return ivParentComponent;
    }

    /**
   * @param value - if true {@link #getParent()} uses 
   *   {@link RendererContext#getTagStack()} to access to the parent tag, which makes 
   *   possible to nest, for example, a {@link FormTag} with a 
   *   {@link DivTag} and a child {@link TextTag} declared in an included jsp.<br>
   *   Works only if it is the outermost tag of the included jsp.  
   */
    public void setParentFromTagStack(boolean value) {
        ivParentFromTagStack = value;
    }

    @Override
    public Tag getParent() {
        Tag parent = super.getParent();
        if (!ivParentFromTagStack || parent != null) return parent;
        return ivTagFromStack;
    }

    /** 
   * Must be called by all {@link IFormComponent}s just before rendering themselfs. 
   * @param fc
   */
    void processFormComponent(IFormComponent fc) {
        if (fc.isChainEnabled()) {
            final IFlow flow = RendererContext.get().getCurrentFlow();
            final String lastForm = flow.getLastActiveFormName();
            final String lastField = flow.getLastActiveFieldName();
            if (getFormName().equals(lastForm) && lastField != null && lastField.equals(fc.getName())) {
                ivLastFocusedCSAC = fc.getClientScriptAccessCode();
            }
            Integer focusPriority = fc.isFocused();
            if (focusPriority != null && (ivDefaultFocusedCSACPriority == null || focusPriority.intValue() > ivDefaultFocusedCSACPriority.intValue())) {
                ivDefaultFocusedCSAC = fc.getClientScriptAccessCode();
                ivDefaultFocusedCSACPriority = focusPriority;
            }
        }
        ivComponents.put(fc.getName(), new FormComponent(fc));
    }

    @SuppressWarnings("unchecked")
    @Override
    public int doStartTag() throws JspException {
        if (!isIf()) return SKIP_BODY;
        final RendererContext ctx = RendererContext.get();
        final IFlow flow = ctx.getCurrentFlow();
        if (ivBind != null) {
            IFormSet formSet;
            try {
                formSet = (IFormSet) ((ivBindState == null) ? flow : flow.getState(ivBindState));
            } catch (ClassCastException ex) {
                throw new IllegalStateException("Form " + ivBind + " not found in the context of flow " + flow + " with bindState:" + ivBindState, ex);
            }
            if (formSet == null) throw new IllegalStateException("Form " + ivBind + " not found in the context of flow " + flow + " with bindState:" + ivBindState);
            setName(ivBind);
            ivBindedForm = formSet.getForm(ivBind);
            if (ivBindedForm == null) throw new IllegalStateException("Form " + ivBind + " not found in the context of flow " + flow + " with bindState:" + ivBindState);
            ivEnabled = ivEnabled && ivBindedForm.isEnabled() && (ivBindState == null || (flow.getState(ivBindState) == flow.getCurrentState()));
            ivReadOnly = ivReadOnly || ivBindedForm.isReadOnly();
            ivBindedForm.binded(ctx.getId());
            {
                Map<String, Object> modalScope = ctx.getCurrentModalScope();
                Set<IForm> forms = (Set<IForm>) modalScope.get(FORMS_ATTR);
                if (forms == null) {
                    forms = new LinkedHashSet<IForm>();
                    modalScope.put(FORMS_ATTR, forms);
                }
                forms.add(ivBindedForm);
            }
        }
        {
            ivChainEnabled = ivEnabled && ctx.isCurrentFlowActive();
        }
        {
            List<WrapBodyTag> tagStack = ctx.getTagStack();
            for (int i = tagStack.size() - 1; i >= 0; i--) {
                WrapBodyTag parent = tagStack.get(i);
                if (parent instanceof FormTag) {
                    if (isMultiPart()) throw new IllegalStateException("Multipart FormTag cannot be nested: " + getId());
                    FormTag parentFormTag = (FormTag) parent;
                    if (parentFormTag.isMultiPart()) throw new IllegalStateException("Cannot be nested in a multipart FormTag: " + getId());
                    ivParentFormTag = parentFormTag;
                    break;
                }
            }
        }
        if (ivParentFromTagStack) {
            List<WrapBodyTag> stack = ctx.getTagStack();
            int len = stack.size();
            ivTagFromStack = len <= 0 ? null : stack.get(len - 1);
        }
        {
            Tag parent = getParent();
            while (parent != null) {
                if (ivParentComponent == null && parent instanceof IComponent) ivParentComponent = (IComponent) parent;
                parent = parent.getParent();
            }
            if (ivParentComponent != null) ivChainEnabled = ivChainEnabled && ivParentComponent.isChainEnabled();
        }
        ContentPart cp = wrapBegin(getId(), false);
        ivUniqueId = cp.getUniqueId() + UID_SEPARATOR + "form";
        ivUniqueExtId = ctx.idToExtId(ivUniqueId);
        setClientScriptAccessCode(cp, getClientScriptAccessCode(), ivChainEnabled);
        wrapBegin("inner");
        pushTag();
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doEndTag() throws JspException {
        if (!isIf()) return EVAL_PAGE;
        popTag();
        RendererContext ctx = RendererContext.get();
        char[] content;
        {
            String cnt = (bodyContent == null) ? "" : bodyContent.getString();
            CharArrayWriter wt = new CharArrayWriter();
            wrapEnd("div", null, wt, cnt.toCharArray());
            content = wt.toCharArray();
        }
        try {
            String currentPage = WEBUtil.getCurrentPage((HttpServletRequest) pageContext.getRequest());
            CharArrayWriter wt = new CharArrayWriter();
            if (ivParentFormTag == null) {
                wt.write("<form method='POST' name='" + ivUniqueExtId + "' action='" + currentPage + "' " + (ivStyle == null ? "" : " style='" + ivStyle + "' ") + (ivClass == null ? "" : " class='" + ivClass + "' "));
                if (isMultiPart()) wt.write("enctype='multipart/form-data' target='$IFW$UPLOADFRAME'");
                wt.write(">\n");
                if (isMultiPart()) wt.write("<input type='hidden' name='$ifw$multipart' value='true'/>");
                wt.write("<input type='hidden' name='$ifw$form' value='" + getMangledName() + "'/>");
                wt.write("<input type='hidden' name='$ifw$action' value=''/>");
                wt.write("<input type='hidden' name='$ifw$actionParam' value=''/>");
            } else {
                wt.write("<div id='" + ivUniqueExtId + "'" + (ivStyle == null ? "" : " style='" + ivStyle + "' ") + (ivClass == null ? "" : " class='" + ivClass + "' "));
                wt.write(">\n");
            }
            String stateFieldName = null;
            String flowFieldName = null;
            if (ivBindedForm != null && ivBindState != null) {
                stateFieldName = createMangledName("$ifw$state");
                wt.write("<input type='hidden' name='" + stateFieldName + "' value='" + ivBindState + "'/>");
            }
            {
                final String cuid = ctx.getCurrentFlowId();
                if (cuid != null) {
                    flowFieldName = createMangledName("$ifw$flow");
                    wt.write("<input type='hidden' name='" + flowFieldName + "' value='" + ctx.idToExtId(cuid) + "'/>");
                }
            }
            wt.write(content);
            if (ivParentFormTag == null) {
                wt.write("</form>\n");
            } else {
                wt.write("</div>\n");
            }
            wt.write("<script>");
            wt.write("{");
            wt.write("var form=" + getHtmlFormScriptAccessCode() + ";");
            wt.write("var subFormName='" + getMangledName() + "';");
            if (stateFieldName != null) {
                wt.write("form['" + stateFieldName + "'].$ifw$subFormName=subFormName;");
            }
            if (flowFieldName != null) {
                wt.write("form['" + flowFieldName + "'].$ifw$subFormName=subFormName;");
            }
            wt.write("{");
            script(wt);
            wt.write("}");
            if (ivBindedForm != null) {
                IFormDecorator fd = ctx.getFormDecorator(ivBindedForm);
                if (fd != null) {
                    wt.write("{");
                    fd.script(ivBindedForm, wt);
                    wt.write("}");
                }
            }
            wt.write("}");
            wt.write("</script>");
            content = wt.toCharArray();
        } catch (IOException ex) {
            throw new JspException(ex);
        }
        wrapEnd("div", null, pageContext.getOut(), content);
        boolean isCurrentForm = false;
        {
            final FlowContext fctx = FlowContext.get();
            IFlow thisFlow = ctx.getCurrentFlow();
            IFlow flow = fctx.getLastActiveFlow();
            if (thisFlow == flow) {
                String formName = flow.getLastActiveFormName();
                if (formName != null && formName.equals(getFormName())) {
                    isCurrentForm = true;
                }
            }
            if (isCurrentForm && ivParentFormTag != null) {
                ivParentFormTag.setParentOfTheCurrentForm();
            }
        }
        updateParentKeys();
        content = null;
        wrapBegin(getId() + INTERNAL + "xtra", false, true);
        CharArrayWriter wt = new CharArrayWriter();
        try {
            boolean placedFakeSpan = false;
            if (ctx.enhanceCurrentFormVisibility()) {
                if (!placedFakeSpan) {
                    placedFakeSpan = true;
                    if (RendererContext.get().needsFakeSpan()) wt.write("<span class='IFW_HIDDEN'>X</span>");
                }
                wt.write("<script>" + "{" + "var form=" + getClientScriptAccessCode() + ";" + "$ifw.removeClass(form,'uncurrentForm');" + "$ifw.removeClass(form,'currentForm');" + "$ifw.addClass(form,'" + (isCurrentForm ? "currentForm" : "uncurrentForm") + "');" + "}" + "</script>");
            }
            if (!ivSkipFocus && (ivDefaultFocusedCSAC != null || ivLastFocusedCSAC != null)) {
                if (!placedFakeSpan) {
                    placedFakeSpan = true;
                    if (RendererContext.get().needsFakeSpan()) wt.write("<span class='IFW_HIDDEN'>X</span>");
                }
                wt.write("<script>" + "{" + "$ifw.setDefaultFocusedField(" + getHtmlFormScriptAccessCode() + ",'" + getMangledName() + "'," + (ivLastFocusedCSAC != null ? ivLastFocusedCSAC : ivDefaultFocusedCSAC) + "," + (isChainEnabled() && ctx.checkAndResetRequiringFocusIndicator()) + ");" + "}" + "</script>");
            }
        } catch (IOException ex) {
            throw new JspException(ex);
        }
        content = wt.toCharArray();
        wrapEnd("span", "class='IFW_HIDDEN'", pageContext.getOut(), content);
        return EVAL_PAGE;
    }

    protected void script(Writer out) throws IOException {
        if (ivParentFormTag == null) {
            out.write("$ifw.registerForm(form);");
        }
    }

    @SuppressWarnings("unchecked")
    private final Set<Key> _cast(Object obj) {
        return (Set<Key>) obj;
    }

    public static class ECSBaseAdapter extends ECSAdapter<FormTag> {

        private static final long serialVersionUID = 1L;

        private final FormTag ivMyTag = new FormTag();

        private final String ivName;

        private Boolean ivEnabled, ivReadOnly, ivSkipFocus, ivParentFromTagStack;

        private String ivStyle, ivCssClass;

        public ECSBaseAdapter(String name) {
            if (name == null) throw new NullPointerException();
            ivName = name;
        }

        @Override
        public ECSBaseAdapter setStyle(String value) {
            ivStyle = value;
            return this;
        }

        public ECSBaseAdapter setCssClass(String value) {
            ivCssClass = value;
            return this;
        }

        public ECSBaseAdapter setEnabled(boolean value) {
            ivEnabled = value;
            return this;
        }

        /**
     * In a read only form actions are enabled but input fields are not
     * @return itself     
     */
        public ECSBaseAdapter setReadOnly(boolean value) {
            ivReadOnly = value;
            return this;
        }

        /**
     * @param value - If true, the form receives the focus only if the user gives it 
     *   explicitly, ie. it is not considered when the system has to choose a form to 
     *   be focused.
     * @return itself
     */
        public ECSBaseAdapter setSkipFocus(boolean value) {
            ivSkipFocus = value;
            return this;
        }

        /**
     * @param value - If true and it is the outermost element of an included jsp, its child
     *   ifw2 tags are able to be wired with ifw2 tags in the including jsp.
     * @return itself
     */
        public ECSBaseAdapter setParentFromTagStack(boolean value) {
            ivParentFromTagStack = value;
            return this;
        }

        @Override
        public FormTag getTag() {
            return ivMyTag;
        }

        @Override
        protected void initTag(FormTag tag) {
            tag.setName(ivName);
            if (ivEnabled != null) tag.setEnabled(ivEnabled);
            tag.setCssClass(ivCssClass);
            tag.setStyle(ivStyle);
            if (ivReadOnly != null) tag.setReadOnly(ivReadOnly);
            if (ivSkipFocus != null) tag.setSkipFocus(ivSkipFocus);
            if (ivParentFromTagStack != null) tag.setParentFromTagStack(ivParentFromTagStack);
        }
    }

    public static class ECSBndsAdapter extends ECSAdapter<FormTag> {

        private static final long serialVersionUID = 1L;

        private final FormTag ivMyTag = new FormTag();

        private final String ivBind;

        private Boolean ivEnabled, ivReadOnly, ivSkipFocus, ivParentFromTagStack;

        private String ivStyle, ivCssClass, ivBindState;

        public ECSBndsAdapter(String bind) {
            if (bind == null) throw new NullPointerException();
            ivBind = bind;
        }

        @Override
        public ECSBndsAdapter setStyle(String value) {
            ivStyle = value;
            return this;
        }

        public ECSBndsAdapter setCssClass(String value) {
            ivCssClass = value;
            return this;
        }

        public ECSBndsAdapter setEnabled(boolean value) {
            ivEnabled = value;
            return this;
        }

        /**
     * In a read only form actions are enabled but input fields are not
     * @return itself     
     */
        public ECSBndsAdapter setReadOnly(boolean value) {
            ivReadOnly = value;
            return this;
        }

        /**
     * @param value - If true, the form receives the focus only if the user gives it 
     *   explicitly, ie. it is not considered when the system has to choose a form to 
     *   be focused.
     * @return itself
     */
        public ECSBndsAdapter setSkipFocus(boolean value) {
            ivSkipFocus = value;
            return this;
        }

        public ECSBndsAdapter setBindState(String value) {
            ivBindState = value;
            return this;
        }

        /**
     * @param value - If true and it is the outermost element of an included jsp, its child
     *   ifw2 tags are able to be wired with ifw2 tags in the including jsp.
     * @return itself
     */
        public ECSBndsAdapter setParentFromTagStack(boolean value) {
            ivParentFromTagStack = value;
            return this;
        }

        @Override
        public FormTag getTag() {
            return ivMyTag;
        }

        @Override
        protected void initTag(FormTag tag) {
            tag.setBind(ivBind);
            tag.setBindState(ivBindState);
            if (ivEnabled != null) tag.setEnabled(ivEnabled);
            tag.setCssClass(ivCssClass);
            tag.setStyle(ivStyle);
            if (ivReadOnly != null) tag.setReadOnly(ivReadOnly);
            if (ivSkipFocus != null) tag.setSkipFocus(ivSkipFocus);
            if (ivParentFromTagStack != null) tag.setParentFromTagStack(ivParentFromTagStack);
        }
    }
}
