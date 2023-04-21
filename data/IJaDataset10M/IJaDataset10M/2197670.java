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
 * Represents VTBL for COM interface IHTMLEditHost.
 */
public class IHTMLEditHostVTBL extends IUnknownVTBL {

    public IHTMLEditHostVTBL(CoClassMetaInfo classMetaInfo) {
        super(classMetaInfo);
        addMembers(new VirtualMethodCallback[] { new VirtualMethodCallback("snapRect", new HResult(), new Parameter[] { new IHTMLElementImpl(), new Pointer(new Rect()), new _ELEMENT_CORNER() }) });
    }
}
