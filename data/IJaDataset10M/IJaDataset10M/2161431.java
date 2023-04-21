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
 * Represents VTBL for COM interface IHTMLPainterEventInfo.
 */
public class IHTMLPainterEventInfoVTBL extends IUnknownVTBL {

    public IHTMLPainterEventInfoVTBL(CoClassMetaInfo classMetaInfo) {
        super(classMetaInfo);
        addMembers(new VirtualMethodCallback[] { new VirtualMethodCallback("getEventInfoFlags", new HResult(), new Parameter[] { new Pointer(new Int32()) }), new VirtualMethodCallback("getEventTarget", new HResult(), new Parameter[] { new Pointer.Const(new IHTMLElementImpl()) }), new VirtualMethodCallback("setCursor", new HResult(), new Parameter[] { new Int32() }), new VirtualMethodCallback("stringFromPartID", new HResult(), new Parameter[] { new Int32(), new Pointer(new BStr()) }) });
    }
}
