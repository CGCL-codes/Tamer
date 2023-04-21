package watij.mshtml.server;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.server.*;
import com.jniwrapper.win32.com.types.*;
import watij.mshtml.*;
import watij.mshtml.impl.*;

/**
 * Adapter for server implementation of HTMLFrameSiteEvents
 */
public class HTMLFrameSiteEventsServer extends IDispatchServer implements HTMLFrameSiteEvents {

    public HTMLFrameSiteEventsServer(CoClassMetaInfo classImpl) {
        super(classImpl);
        registerMethods(HTMLFrameSiteEvents.class);
    }

    public VariantBool onhelp() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool onclick() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool ondblclick() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool onkeypress() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void onkeydown() {
    }

    public void onkeyup() {
    }

    public void onmouseout() {
    }

    public void onmouseover() {
    }

    public void onmousemove() {
    }

    public void onmousedown() {
    }

    public void onmouseup() {
    }

    public VariantBool onselectstart() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void onfilterchange() {
    }

    public VariantBool ondragstart() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool onbeforeupdate() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void onafterupdate() {
    }

    public VariantBool onerrorupdate() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool onrowexit() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void onrowenter() {
    }

    public void ondatasetchanged() {
    }

    public void ondataavailable() {
    }

    public void ondatasetcomplete() {
    }

    public void onlosecapture() {
    }

    public void onpropertychange() {
    }

    public void onscroll() {
    }

    public void onfocus() {
    }

    public void onblur() {
    }

    public void onresize() {
    }

    public VariantBool ondrag() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void ondragend() {
    }

    public VariantBool ondragenter() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool ondragover() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void ondragleave() {
    }

    public VariantBool ondrop() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool onbeforecut() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool oncut() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool onbeforecopy() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool oncopy() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool onbeforepaste() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool onpaste() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool oncontextmenu() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void onrowsdelete() {
    }

    public void onrowsinserted() {
    }

    public void oncellchange() {
    }

    public void onreadystatechange() {
    }

    public void onbeforeeditfocus() {
    }

    public void onlayoutcomplete() {
    }

    public void onpage() {
    }

    public VariantBool onbeforedeactivate() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool onbeforeactivate() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void onmove() {
    }

    public VariantBool oncontrolselect() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool onmovestart() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void onmoveend() {
    }

    public VariantBool onresizestart() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void onresizeend() {
    }

    public void onmouseenter() {
    }

    public void onmouseleave() {
    }

    public VariantBool onmousewheel() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void onactivate() {
    }

    public void ondeactivate() {
    }

    public void onfocusin() {
    }

    public void onfocusout() {
    }

    public void onload() {
    }
}
