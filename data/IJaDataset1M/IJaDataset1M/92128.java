package watij.mshtml;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import watij.mshtml.impl.*;

/**
 * Represents COM coclass BlockFormats.
 */
public class BlockFormats extends CoClass {

    public static final CLSID CLASS_ID = CLSID.create("{3050F831-98B5-11CF-BB82-00AA00BDCE0B}");

    public BlockFormats() {
    }

    public BlockFormats(BlockFormats that) {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static IBlockFormats create(ClsCtx dwClsContext) throws ComException {
        final IBlockFormatsImpl intf = new IBlockFormatsImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>IBlockFormats</code> interface from IUnknown instance.
     */
    public static IBlockFormats queryInterface(IUnknown unknown) throws ComException {
        final IBlockFormatsImpl result = new IBlockFormatsImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID() {
        return CLASS_ID;
    }

    public Object clone() {
        return new BlockFormats(this);
    }
}
