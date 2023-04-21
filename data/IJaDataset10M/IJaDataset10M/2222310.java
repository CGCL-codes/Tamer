package watij.mshtml.impl;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import watij.mshtml.*;

/**
 * Represents COM dispinterface HTMLControlElementEvents.
 */
public class HTMLControlElementEventsImpl extends IDispatchImpl implements HTMLControlElementEvents {

    public static final String INTERFACE_IDENTIFIER = "{3050F4EA-98B5-11CF-BB82-00AA00BDCE0B}";

    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public HTMLControlElementEventsImpl() {
    }

    protected HTMLControlElementEventsImpl(IUnknownImpl that) throws ComException {
        super(that);
    }

    public HTMLControlElementEventsImpl(IUnknown that) throws ComException {
        super(that);
    }

    public HTMLControlElementEventsImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException {
        super(clsid, dwClsContext);
    }

    public HTMLControlElementEventsImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public VariantBool onhelp() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "onhelp", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public VariantBool onclick() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "onclick", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public VariantBool ondblclick() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "ondblclick", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public VariantBool onkeypress() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "onkeypress", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public void onkeydown() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onkeydown", parameters, void.class);
    }

    public void onkeyup() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onkeyup", parameters, void.class);
    }

    public void onmouseout() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onmouseout", parameters, void.class);
    }

    public void onmouseover() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onmouseover", parameters, void.class);
    }

    public void onmousemove() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onmousemove", parameters, void.class);
    }

    public void onmousedown() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onmousedown", parameters, void.class);
    }

    public void onmouseup() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onmouseup", parameters, void.class);
    }

    public VariantBool onselectstart() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "onselectstart", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public void onfilterchange() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onfilterchange", parameters, void.class);
    }

    public VariantBool ondragstart() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "ondragstart", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public VariantBool onbeforeupdate() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "onbeforeupdate", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public void onafterupdate() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onafterupdate", parameters, void.class);
    }

    public VariantBool onerrorupdate() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "onerrorupdate", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public VariantBool onrowexit() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "onrowexit", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public void onrowenter() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onrowenter", parameters, void.class);
    }

    public void ondatasetchanged() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "ondatasetchanged", parameters, void.class);
    }

    public void ondataavailable() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "ondataavailable", parameters, void.class);
    }

    public void ondatasetcomplete() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "ondatasetcomplete", parameters, void.class);
    }

    public void onlosecapture() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onlosecapture", parameters, void.class);
    }

    public void onpropertychange() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onpropertychange", parameters, void.class);
    }

    public void onscroll() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onscroll", parameters, void.class);
    }

    public void onfocus() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onfocus", parameters, void.class);
    }

    public void onblur() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onblur", parameters, void.class);
    }

    public void onresize() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onresize", parameters, void.class);
    }

    public VariantBool ondrag() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "ondrag", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public void ondragend() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "ondragend", parameters, void.class);
    }

    public VariantBool ondragenter() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "ondragenter", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public VariantBool ondragover() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "ondragover", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public void ondragleave() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "ondragleave", parameters, void.class);
    }

    public VariantBool ondrop() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "ondrop", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public VariantBool onbeforecut() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "onbeforecut", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public VariantBool oncut() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "oncut", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public VariantBool onbeforecopy() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "onbeforecopy", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public VariantBool oncopy() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "oncopy", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public VariantBool onbeforepaste() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "onbeforepaste", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public VariantBool onpaste() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "onpaste", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public VariantBool oncontextmenu() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "oncontextmenu", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public void onrowsdelete() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onrowsdelete", parameters, void.class);
    }

    public void onrowsinserted() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onrowsinserted", parameters, void.class);
    }

    public void oncellchange() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "oncellchange", parameters, void.class);
    }

    public void onreadystatechange() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onreadystatechange", parameters, void.class);
    }

    public void onbeforeeditfocus() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onbeforeeditfocus", parameters, void.class);
    }

    public void onlayoutcomplete() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onlayoutcomplete", parameters, void.class);
    }

    public void onpage() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onpage", parameters, void.class);
    }

    public VariantBool onbeforedeactivate() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "onbeforedeactivate", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public VariantBool onbeforeactivate() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "onbeforeactivate", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public void onmove() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onmove", parameters, void.class);
    }

    public VariantBool oncontrolselect() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "oncontrolselect", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public VariantBool onmovestart() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "onmovestart", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public void onmoveend() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onmoveend", parameters, void.class);
    }

    public VariantBool onresizestart() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "onresizestart", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public void onresizeend() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onresizeend", parameters, void.class);
    }

    public void onmouseenter() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onmouseenter", parameters, void.class);
    }

    public void onmouseleave() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onmouseleave", parameters, void.class);
    }

    public VariantBool onmousewheel() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "onmousewheel", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public void onactivate() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onactivate", parameters, void.class);
    }

    public void ondeactivate() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "ondeactivate", parameters, void.class);
    }

    public void onfocusin() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onfocusin", parameters, void.class);
    }

    public void onfocusout() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "onfocusout", parameters, void.class);
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new HTMLControlElementEventsImpl(this);
    }
}
