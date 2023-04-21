package watij.mshtml.server;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.server.*;
import com.jniwrapper.win32.com.types.*;
import watij.mshtml.*;
import watij.mshtml.impl.*;

/**
 * Represents VTBL for COM interface IElementNamespaceFactoryCallback.
 */
public class IElementNamespaceFactoryCallbackVTBL extends IUnknownVTBL {

    public IElementNamespaceFactoryCallbackVTBL(CoClassMetaInfo classMetaInfo) {
        super(classMetaInfo);
        addMembers(new VirtualMethodCallback[] { new VirtualMethodCallback("resolve", new HResult(), new Parameter[] { new BStr(), new BStr(), new BStr(), new IElementNamespaceImpl() }) });
    }
}
