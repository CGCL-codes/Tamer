package watij.mshtml;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import watij.mshtml.impl.*;

/**
 * Represents Java interface for COM interface IHTMLTableSection2.
 */
public interface IHTMLTableSection2 extends IDispatch {

    public static final String INTERFACE_IDENTIFIER = "{3050F5C7-98B5-11CF-BB82-00AA00BDCE0B}";

    IDispatch moveRow(Int32 indexFrom, Int32 indexTo) throws ComException;
}
