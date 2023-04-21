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
 * Represents Java interface for COM interface IHTMLFrameBase2.
 */
public interface IHTMLFrameBase2 extends IDispatch {

    public static final String INTERFACE_IDENTIFIER = "{3050F6DB-98B5-11CF-BB82-00AA00BDCE0B}";

    IHTMLWindow2 getContentWindow() throws ComException;

    void setOnload(Variant p) throws ComException;

    Variant getOnload() throws ComException;

    void setOnreadystatechange(Variant p) throws ComException;

    Variant getOnreadystatechange() throws ComException;

    BStr getReadyState() throws ComException;

    void setAllowTransparency(VariantBool p) throws ComException;

    VariantBool getAllowTransparency() throws ComException;
}
