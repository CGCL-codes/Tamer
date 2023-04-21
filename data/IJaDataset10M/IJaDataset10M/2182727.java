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
 * Represents VTBL for COM interface IHTMLStyleFontFace.
 */
public class IHTMLStyleFontFaceVTBL extends IDispatchVTBL {

    public IHTMLStyleFontFaceVTBL(CoClassMetaInfo classMetaInfo) {
        super(classMetaInfo);
        addMembers(new VirtualMethodCallback[] { new VirtualMethodCallback("setFontsrc", new HResult(), new Parameter[] { new BStr() }), new VirtualMethodCallback("getFontsrc", new HResult(), new Parameter[] { new Pointer(new BStr()) }, 0) });
    }
}
