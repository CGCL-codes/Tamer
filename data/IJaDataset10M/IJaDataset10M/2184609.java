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
 * Represents Java interface for COM interface IHTMLTableCell2.
 */
public interface IHTMLTableCell2 extends IDispatch {

    public static final String INTERFACE_IDENTIFIER = "{3050F82D-98B5-11CF-BB82-00AA00BDCE0B}";

    void setAbbr(BStr p) throws ComException;

    BStr getAbbr() throws ComException;

    void setAxis(BStr p) throws ComException;

    BStr getAxis() throws ComException;

    void setCh(BStr p) throws ComException;

    BStr getCh() throws ComException;

    void setChOff(BStr p) throws ComException;

    BStr getChOff() throws ComException;

    void setHeaders(BStr p) throws ComException;

    BStr getHeaders() throws ComException;

    void setScope(BStr p) throws ComException;

    BStr getScope() throws ComException;
}
