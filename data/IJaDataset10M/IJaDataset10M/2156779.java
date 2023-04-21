package net.infordata.ifw2.web.tags;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import net.infordata.ifw2.web.bnds.IField;
import net.infordata.ifw2.web.bnds.IFieldDecorator;
import net.infordata.ifw2.web.bnds.IForm;
import net.infordata.ifw2.web.bnds.ISingleValueField;
import net.infordata.ifw2.web.util.CharArrayWriter;
import net.infordata.ifw2.web.view.ContentPart;
import net.infordata.ifw2.web.view.RendererContext;
import net.infordata.ifw2.web.view.WrapTag;

public abstract class AInputTag extends WrapTag implements IFormComponent {

    private static final long serialVersionUID = 1L;

    protected String ivStyle;

    protected String ivClass;

    protected String ivValue;

    protected Integer ivTabIndex;

    private boolean ivEnabled = true;

    private boolean ivReadOnly = false;

    private boolean ivChainEnabled = ivEnabled;

    private Integer ivFocused;

    private FormTag ivFormTag;

    private String ivBind;

    private ISingleValueField ivBindedField;

    protected String ivTitle;

    private String ivUniqueId;

    /** */
    public AInputTag() {
    }

    @Override
    public void release() {
        super.release();
        ivStyle = null;
        ivClass = null;
        ivValue = null;
        ivTabIndex = null;
        ivEnabled = true;
        ivReadOnly = false;
        ivChainEnabled = ivEnabled;
        ivFocused = null;
        ivTitle = null;
        ivFormTag = null;
        ivBind = null;
        ivBindedField = null;
        ivUniqueId = null;
    }

    /** 
   * By default name and id are the same.
   * @param name
   */
    public void setName(String name) {
        setId(name);
    }

    @Override
    public final String getName() {
        return getId();
    }

    @Override
    public final String getMangledName() {
        return ivFormTag.createMangledName(this);
    }

    @Override
    public final String getUniqueId() {
        return ivUniqueId;
    }

    @Override
    public final String getClientScriptAccessCode() {
        return "document.getElementById('" + RendererContext.get().idToExtId(getUniqueId()) + "')";
    }

    public final void setStyle(String style) {
        ivStyle = style;
    }

    public final void setCssClass(String style) {
        ivClass = style;
    }

    public final void setValue(String value) {
        ivValue = value;
    }

    public final void setTabIndex(int tabIndex) {
        ivTabIndex = tabIndex;
    }

    @Override
    public final boolean isEnabled() {
        return ivEnabled;
    }

    public final boolean isReadOnly() {
        return ivReadOnly;
    }

    public final void setEnabled(boolean enabled) {
        ivEnabled = enabled;
    }

    public final void setBind(String fieldName) {
        ivBind = fieldName;
    }

    public final void setTitle(String title) {
        ivTitle = title;
    }

    protected final FormTag getFormTag() {
        return ivFormTag;
    }

    public final IForm getBindedForm() {
        return ivFormTag.getBindedForm();
    }

    public final ISingleValueField getBindedField() {
        return ivBindedField;
    }

    /**
   * @return true if all the chain is enabled, form included.
   */
    @Override
    public final boolean isChainEnabled() {
        return ivChainEnabled;
    }

    public final void setFocused(Integer focused) {
        ivFocused = focused;
    }

    @Override
    public final Integer isFocused() {
        return ivFocused;
    }

    protected abstract void processBind(ISingleValueField bindedField);

    /**
   * Subclasses should redefine the {@link IForm#bindField(Class, String)} method
   * to request the wanted {@link IField} type.
   * @param form
   * @param fieldId
   * @return
   */
    protected abstract ISingleValueField bindField(IForm form, String id);

    /** */
    @Override
    public final int doStartTag() throws JspException {
        if (!isIf()) return SKIP_BODY;
        RendererContext ctx = RendererContext.get();
        {
            Tag parent = getParent();
            while (parent != null && !(parent instanceof FormTag)) parent = parent.getParent();
            if (parent == null) throw new IllegalStateException("Not in a FormTag: " + getId());
            ivFormTag = (FormTag) parent;
        }
        if (ivBind != null) {
            setName(ivBind);
            ivBindedField = bindField(getBindedForm(), ivBind);
            if (ivBindedField == null) throw new IllegalStateException("Field " + ivBind + " not found in form " + getBindedForm() + " tag " + getClass().getName());
            ivEnabled = ivEnabled && ivBindedField.isEnabled();
            ivReadOnly = ivBindedField.isReadOnly();
            if (ivTitle == null) ivTitle = ivBindedField.getTitle();
            if (ivClass == null) ivClass = ivBindedField.getCssClass();
            if (ivStyle == null) ivStyle = ivBindedField.getStyle();
            processBind(ivBindedField);
        }
        ivChainEnabled = ivEnabled && !ivFormTag.isReadOnly();
        if (ivChainEnabled) {
            Tag parent = getParent();
            while (parent != null && !(parent instanceof IComponent)) parent = parent.getParent();
            if (parent != null) {
                IComponent pcomp = (IComponent) parent;
                ivChainEnabled = ivChainEnabled && pcomp.isChainEnabled();
            }
        }
        ivUniqueId = ctx.getUniqueId(getName() + ENDS_WITH);
        ivFormTag.processFormComponent(this);
        ContentPart cp = wrapBegin(getId());
        setClientScriptAccessCode(cp, getClientScriptAccessCode(), ivChainEnabled);
        CharArrayWriter wt = new CharArrayWriter();
        try {
            render(wt);
            wt.write("<script>");
            wt.write("{");
            wt.write("var el=document.getElementById('" + ctx.idToExtId(cp.getUniqueId()) + "');");
            wt.write("$ifw.setRefreshByCopy(el, true);");
            wt.write("}");
            wt.write("{");
            wt.write("var form=" + ivFormTag.getHtmlFormScriptAccessCode() + ";");
            wt.write("var subFormName='" + ivFormTag.getMangledName() + "';");
            wt.write("var comp=" + getClientScriptAccessCode() + ";");
            wt.write("{");
            script(wt);
            wt.write("}");
            if (ivBindedField != null) {
                IFieldDecorator fd = ctx.getFieldDecorator(ivBindedField);
                if (fd != null) {
                    wt.write("{");
                    fd.script(ivBindedField, wt);
                    wt.write("}");
                }
            }
            wt.write("}");
            wt.write("</script>");
        } catch (IOException ex) {
            throw new JspException(ex);
        }
        wrapEnd(pageContext.getOut(), wt.toCharArray());
        return SKIP_BODY;
    }

    protected abstract void render(Writer out) throws IOException;

    protected abstract void script(Writer out) throws IOException;
}
