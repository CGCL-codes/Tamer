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
 * Represents VTBL for COM interface IHTMLTable2.
 */
public class IHTMLTable2VTBL extends IDispatchVTBL {

    public IHTMLTable2VTBL(CoClassMetaInfo classMetaInfo) {
        super(classMetaInfo);
        addMembers(new VirtualMethodCallback[] { new VirtualMethodCallback("firstPage", new HResult(), new Parameter[] {}), new VirtualMethodCallback("lastPage", new HResult(), new Parameter[] {}), new VirtualMethodCallback("getCells", new HResult(), new Parameter[] { new Pointer(new IHTMLElementCollectionImpl()) }, 0), new VirtualMethodCallback("moveRow", new HResult(), new Parameter[] { new Int32(), new Int32(), new Pointer(new IDispatchImpl()) }, 2) });
    }
}
