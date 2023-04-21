package watij.mshtml;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import watij.mshtml.impl.*;

/**
 * Represents COM coclass HTMLStyleSheet.
 */
public class HTMLStyleSheet extends CoClass {

    public static final CLSID CLASS_ID = CLSID.create("{3050F2E4-98B5-11CF-BB82-00AA00BDCE0B}");

    public HTMLStyleSheet() {
    }

    public HTMLStyleSheet(HTMLStyleSheet that) {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static DispHTMLStyleSheetImpl create(ClsCtx dwClsContext) throws ComException {
        final DispHTMLStyleSheetImpl intf = new DispHTMLStyleSheetImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>DispHTMLStyleSheetImpl</code> interface from IUnknown instance.
     */
    public static DispHTMLStyleSheetImpl queryInterface(IUnknown unknown) throws ComException {
        final DispHTMLStyleSheetImpl result = new DispHTMLStyleSheetImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID() {
        return CLASS_ID;
    }

    public Object clone() {
        return new HTMLStyleSheet(this);
    }
}
