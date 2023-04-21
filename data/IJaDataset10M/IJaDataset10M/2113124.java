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
 * Represents VTBL for COM interface IEnumPrivacyRecords.
 */
public class IEnumPrivacyRecordsVTBL extends IUnknownVTBL {

    public IEnumPrivacyRecordsVTBL(CoClassMetaInfo classMetaInfo) {
        super(classMetaInfo);
        addMembers(new VirtualMethodCallback[] { new VirtualMethodCallback("reset", new HResult(), new Parameter[] {}), new VirtualMethodCallback("getSize", new HResult(), new Parameter[] { new Pointer(new UInt32()) }), new VirtualMethodCallback("getPrivacyImpacted", new HResult(), new Parameter[] { new Pointer(new Int32()) }), new VirtualMethodCallback("next", new HResult(), new Parameter[] { new Pointer(new BStr()), new Pointer(new BStr()), new Pointer(new Int32()), new Pointer(new UInt32()) }) });
    }
}
