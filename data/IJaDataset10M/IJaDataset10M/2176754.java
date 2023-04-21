package watij.iwshruntimelibrary;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import watij.iwshruntimelibrary.impl.*;

/**
 * Represents Java interface for COM interface IFileSystem3.
 */
public interface IFileSystem3 extends IFileSystem {

    public static final String INTERFACE_IDENTIFIER = "{2A0B9D10-4B87-11D3-A97A-00104B365C9F}";

    ITextStream getStandardStream(StandardStreamTypes StandardStreamType, VariantBool Unicode) throws ComException;

    BStr getFileVersion(BStr FileName) throws ComException;
}
