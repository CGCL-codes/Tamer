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
 * Represents VTBL for COM interface IHTMLFrameElement.
 */
public class IHTMLFrameElementVTBL extends IDispatchVTBL {

    public IHTMLFrameElementVTBL(CoClassMetaInfo classMetaInfo) {
        super(classMetaInfo);
        addMembers(new VirtualMethodCallback[] { new VirtualMethodCallback("setBorderColor", new HResult(), new Parameter[] { new Variant() }), new VirtualMethodCallback("getBorderColor", new HResult(), new Parameter[] { new Pointer(new Variant()) }, 0) });
    }
}
