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
 * Represents VTBL for COM interface IHTMLAreasCollection.
 */
public class IHTMLAreasCollectionVTBL extends IDispatchVTBL {

    public IHTMLAreasCollectionVTBL(CoClassMetaInfo classMetaInfo) {
        super(classMetaInfo);
        addMembers(new VirtualMethodCallback[] { new VirtualMethodCallback("invokeSetLength", new HResult(), new Parameter[] { new Int32() }), new VirtualMethodCallback("invokeGetLength", new HResult(), new Parameter[] { new Pointer(new Int32()) }, 0), new VirtualMethodCallback("get_newEnum", new HResult(), new Parameter[] { new Pointer(new IUnknownImpl()) }, 0), new VirtualMethodCallback("item", new HResult(), new Parameter[] { new Variant(), new Variant(), new Pointer(new IDispatchImpl()) }, 2), new VirtualMethodCallback("tags", new HResult(), new Parameter[] { new Variant(), new Pointer(new IDispatchImpl()) }, 1), new VirtualMethodCallback("add", new HResult(), new Parameter[] { new IHTMLElementImpl(), new Variant() }), new VirtualMethodCallback("remove", new HResult(), new Parameter[] { new Int32() }) });
    }
}
