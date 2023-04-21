package watij.mshtml;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import watij.mshtml.impl.*;

/**
 * Represents COM coclass HTCEventBehavior.
 */
public class HTCEventBehavior extends CoClass {

    public static final CLSID CLASS_ID = CLSID.create("{3050F4FE-98B5-11CF-BB82-00AA00BDCE0B}");

    public HTCEventBehavior() {
    }

    public HTCEventBehavior(HTCEventBehavior that) {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static DispHTCEventBehaviorImpl create(ClsCtx dwClsContext) throws ComException {
        final DispHTCEventBehaviorImpl intf = new DispHTCEventBehaviorImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>DispHTCEventBehaviorImpl</code> interface from IUnknown instance.
     */
    public static DispHTCEventBehaviorImpl queryInterface(IUnknown unknown) throws ComException {
        final DispHTCEventBehaviorImpl result = new DispHTCEventBehaviorImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID() {
        return CLASS_ID;
    }

    public Object clone() {
        return new HTCEventBehavior(this);
    }
}
