package watij.mshtml.server;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.server.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.server.*;
import com.jniwrapper.win32.com.types.*;
import watij.mshtml.*;
import watij.mshtml.impl.*;

/**
 * Represents VTBL for COM interface IHTMLFrameBase.
 */
public class IHTMLFrameBaseVTBL extends IDispatchVTBL {

    public IHTMLFrameBaseVTBL(CoClassMetaInfo classMetaInfo) {
        super(classMetaInfo);
        addMembers(new VirtualMethodCallback[] { new VirtualMethodCallback("setSrc", new HResult(), new Parameter[] { new BStr() }), new VirtualMethodCallback("getSrc", new HResult(), new Parameter[] { new Pointer(new BStr()) }, 0), new VirtualMethodCallback("setName", new HResult(), new Parameter[] { new BStr() }), new VirtualMethodCallback("getName", new HResult(), new Parameter[] { new Pointer(new BStr()) }, 0), new VirtualMethodCallback("setBorder", new HResult(), new Parameter[] { new Variant() }), new VirtualMethodCallback("getBorder", new HResult(), new Parameter[] { new Pointer(new Variant()) }, 0), new VirtualMethodCallback("setFrameBorder", new HResult(), new Parameter[] { new BStr() }), new VirtualMethodCallback("getFrameBorder", new HResult(), new Parameter[] { new Pointer(new BStr()) }, 0), new VirtualMethodCallback("setFrameSpacing", new HResult(), new Parameter[] { new Variant() }), new VirtualMethodCallback("getFrameSpacing", new HResult(), new Parameter[] { new Pointer(new Variant()) }, 0), new VirtualMethodCallback("setMarginWidth", new HResult(), new Parameter[] { new Variant() }), new VirtualMethodCallback("getMarginWidth", new HResult(), new Parameter[] { new Pointer(new Variant()) }, 0), new VirtualMethodCallback("setMarginHeight", new HResult(), new Parameter[] { new Variant() }), new VirtualMethodCallback("getMarginHeight", new HResult(), new Parameter[] { new Pointer(new Variant()) }, 0), new VirtualMethodCallback("setNoResize", new HResult(), new Parameter[] { new VariantBool() }), new VirtualMethodCallback("getNoResize", new HResult(), new Parameter[] { new Pointer(new VariantBool()) }, 0), new VirtualMethodCallback("setScrolling", new HResult(), new Parameter[] { new BStr() }), new VirtualMethodCallback("getScrolling", new HResult(), new Parameter[] { new Pointer(new BStr()) }, 0) });
    }
}
