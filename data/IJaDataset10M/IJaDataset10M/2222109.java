package watij.mshtml;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import watij.mshtml.impl.*;

/**
 * Represents COM coclass HTMLHistory.
 */
public class HTMLHistory extends CoClass {

    public static final CLSID CLASS_ID = CLSID.create("{FECEAAA3-8405-11CF-8BA1-00AA00476DA6}");

    public HTMLHistory() {
    }

    public HTMLHistory(HTMLHistory that) {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static IOmHistory create(ClsCtx dwClsContext) throws ComException {
        final IOmHistoryImpl intf = new IOmHistoryImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>IOmHistory</code> interface from IUnknown instance.
     */
    public static IOmHistory queryInterface(IUnknown unknown) throws ComException {
        final IOmHistoryImpl result = new IOmHistoryImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID() {
        return CLASS_ID;
    }

    public Object clone() {
        return new HTMLHistory(this);
    }
}
