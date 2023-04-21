package watij.mshtml;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import watij.mshtml.impl.*;

/**
 * Represents COM coclass HTMLHtmlElement.
 */
public class HTMLHtmlElement extends CoClass {

    public static final CLSID CLASS_ID = CLSID.create("{3050F491-98B5-11CF-BB82-00AA00BDCE0B}");

    public HTMLHtmlElement() {
    }

    public HTMLHtmlElement(HTMLHtmlElement that) {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static DispHTMLHtmlElementImpl create(ClsCtx dwClsContext) throws ComException {
        final DispHTMLHtmlElementImpl intf = new DispHTMLHtmlElementImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>DispHTMLHtmlElementImpl</code> interface from IUnknown instance.
     */
    public static DispHTMLHtmlElementImpl queryInterface(IUnknown unknown) throws ComException {
        final DispHTMLHtmlElementImpl result = new DispHTMLHtmlElementImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID() {
        return CLASS_ID;
    }

    public Object clone() {
        return new HTMLHtmlElement(this);
    }
}
