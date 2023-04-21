package watij.mshtml;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import watij.mshtml.impl.*;

/**
 * Represents COM coclass HTMLOptionButtonElement.
 */
public class HTMLOptionButtonElement extends CoClass {

    public static final CLSID CLASS_ID = CLSID.create("{3050F2BE-98B5-11CF-BB82-00AA00BDCE0B}");

    public HTMLOptionButtonElement() {
    }

    public HTMLOptionButtonElement(HTMLOptionButtonElement that) {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static DispIHTMLOptionButtonElementImpl create(ClsCtx dwClsContext) throws ComException {
        final DispIHTMLOptionButtonElementImpl intf = new DispIHTMLOptionButtonElementImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>DispIHTMLOptionButtonElementImpl</code> interface from IUnknown instance.
     */
    public static DispIHTMLOptionButtonElementImpl queryInterface(IUnknown unknown) throws ComException {
        final DispIHTMLOptionButtonElementImpl result = new DispIHTMLOptionButtonElementImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID() {
        return CLASS_ID;
    }

    public Object clone() {
        return new HTMLOptionButtonElement(this);
    }
}
