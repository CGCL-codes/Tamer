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
 * Represents VTBL for COM interface IHTMLFontNamesCollection.
 */
public class IHTMLFontNamesCollectionVTBL extends IDispatchVTBL {

    public IHTMLFontNamesCollectionVTBL(CoClassMetaInfo classMetaInfo) {
        super(classMetaInfo);
        addMembers(new VirtualMethodCallback[] { new VirtualMethodCallback("invokeGetLength", new HResult(), new Parameter[] { new Pointer(new Int32()) }, 0), new VirtualMethodCallback("get_newEnum", new HResult(), new Parameter[] { new Pointer(new IUnknownImpl()) }, 0), new VirtualMethodCallback("item", new HResult(), new Parameter[] { new Int32(), new Pointer(new BStr()) }, 1) });
    }
}
