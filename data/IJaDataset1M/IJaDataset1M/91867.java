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
 * Represents VTBL for COM interface IShellWindows.
 */
public class IShellWindowsVTBL extends IDispatchVTBL {

    public IShellWindowsVTBL(CoClassMetaInfo classMetaInfo) {
        super(classMetaInfo);
        addMembers(new VirtualMethodCallback[] { new VirtualMethodCallback("getCount", new HResult(), new Parameter[] { new Pointer(new Int32()) }, 0), new VirtualMethodCallback("item", new HResult(), new Parameter[] { new Variant(), new Pointer(new IDispatchImpl()) }, 1), new VirtualMethodCallback("_NewEnum", new HResult(), new Parameter[] { new Pointer(new IUnknownImpl()) }, 0), new VirtualMethodCallback("register", new HResult(), new Parameter[] { new IDispatchImpl(), new Int32(), new Int(), new Pointer(new Int32()) }), new VirtualMethodCallback("registerPending", new HResult(), new Parameter[] { new Int32(), new Pointer.Const(new Variant()), new Pointer.Const(new Variant()), new Int(), new Pointer(new Int32()) }), new VirtualMethodCallback("revoke", new HResult(), new Parameter[] { new Int32() }), new VirtualMethodCallback("onNavigate", new HResult(), new Parameter[] { new Int32(), new Pointer.Const(new Variant()) }), new VirtualMethodCallback("onActivated", new HResult(), new Parameter[] { new Int32(), new VariantBool() }), new VirtualMethodCallback("findWindowSW", new HResult(), new Parameter[] { new Pointer.Const(new Variant()), new Pointer.Const(new Variant()), new Int(), new Pointer(new Int32()), new Int(), new Pointer(new IDispatchImpl()) }, 5), new VirtualMethodCallback("onCreated", new HResult(), new Parameter[] { new Int32(), new IUnknownImpl() }), new VirtualMethodCallback("processAttachDetach", new HResult(), new Parameter[] { new VariantBool() }) });
    }
}
