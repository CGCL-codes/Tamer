package watij.mshtml;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import watij.mshtml.impl.*;

/**
 * Represents COM coclass HTMLAreasCollection.
 */
public class HTMLAreasCollection extends CoClass {

    public static final CLSID CLASS_ID = CLSID.create("{3050F4CA-98B5-11CF-BB82-00AA00BDCE0B}");

    public HTMLAreasCollection() {
    }

    public HTMLAreasCollection(HTMLAreasCollection that) {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static DispHTMLAreasCollectionImpl create(ClsCtx dwClsContext) throws ComException {
        final DispHTMLAreasCollectionImpl intf = new DispHTMLAreasCollectionImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>DispHTMLAreasCollectionImpl</code> interface from IUnknown instance.
     */
    public static DispHTMLAreasCollectionImpl queryInterface(IUnknown unknown) throws ComException {
        final DispHTMLAreasCollectionImpl result = new DispHTMLAreasCollectionImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID() {
        return CLASS_ID;
    }

    public Object clone() {
        return new HTMLAreasCollection(this);
    }
}
