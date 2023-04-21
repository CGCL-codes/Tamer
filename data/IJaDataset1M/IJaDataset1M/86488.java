package watij.shdocvw.server;

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
import watij.shdocvw.*;
import watij.shdocvw.impl.*;

/**
 * Represents VTBL for COM interface IWebBrowser.
 */
public class IWebBrowserVTBL extends IDispatchVTBL {

    public IWebBrowserVTBL(CoClassMetaInfo classMetaInfo) {
        super(classMetaInfo);
        addMembers(new VirtualMethodCallback[] { new VirtualMethodCallback("goBack", new HResult(), new Parameter[] {}), new VirtualMethodCallback("goForward", new HResult(), new Parameter[] {}), new VirtualMethodCallback("goHome", new HResult(), new Parameter[] {}), new VirtualMethodCallback("goSearch", new HResult(), new Parameter[] {}), new VirtualMethodCallback("navigate", new HResult(), new Parameter[] { new BStr(), new Pointer.Const(new Variant()), new Pointer.Const(new Variant()), new Pointer.Const(new Variant()), new Pointer.Const(new Variant()) }), new VirtualMethodCallback("refresh", new HResult(), new Parameter[] {}), new VirtualMethodCallback("refresh2", new HResult(), new Parameter[] { new Pointer.Const(new Variant()) }), new VirtualMethodCallback("stop", new HResult(), new Parameter[] {}), new VirtualMethodCallback("getApplication", new HResult(), new Parameter[] { new Pointer(new IDispatchImpl()) }, 0), new VirtualMethodCallback("getParent", new HResult(), new Parameter[] { new Pointer(new IDispatchImpl()) }, 0), new VirtualMethodCallback("getContainer", new HResult(), new Parameter[] { new Pointer(new IDispatchImpl()) }, 0), new VirtualMethodCallback("getDocument", new HResult(), new Parameter[] { new Pointer(new IDispatchImpl()) }, 0), new VirtualMethodCallback("getTopLevelContainer", new HResult(), new Parameter[] { new Pointer(new VariantBool()) }, 0), new VirtualMethodCallback("getType", new HResult(), new Parameter[] { new Pointer(new BStr()) }, 0), new VirtualMethodCallback("getLeft", new HResult(), new Parameter[] { new Pointer(new Int32()) }, 0), new VirtualMethodCallback("setLeft", new HResult(), new Parameter[] { new Int32() }), new VirtualMethodCallback("getTop", new HResult(), new Parameter[] { new Pointer(new Int32()) }, 0), new VirtualMethodCallback("setTop", new HResult(), new Parameter[] { new Int32() }), new VirtualMethodCallback("getWidth", new HResult(), new Parameter[] { new Pointer(new Int32()) }, 0), new VirtualMethodCallback("setWidth", new HResult(), new Parameter[] { new Int32() }), new VirtualMethodCallback("getHeight", new HResult(), new Parameter[] { new Pointer(new Int32()) }, 0), new VirtualMethodCallback("setHeight", new HResult(), new Parameter[] { new Int32() }), new VirtualMethodCallback("getLocationName", new HResult(), new Parameter[] { new Pointer(new BStr()) }, 0), new VirtualMethodCallback("getLocationURL", new HResult(), new Parameter[] { new Pointer(new BStr()) }, 0), new VirtualMethodCallback("getBusy", new HResult(), new Parameter[] { new Pointer(new VariantBool()) }, 0) });
    }
}
