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
 * Represents Java interface for COM interface IHTMLFrameElement2.
 */
public interface IHTMLFrameElement2 extends IDispatch {

    public static final String INTERFACE_IDENTIFIER = "{3050F7F5-98B5-11CF-BB82-00AA00BDCE0B}";

    void setHeight(Variant p) throws ComException;

    Variant getHeight() throws ComException;

    void setWidth(Variant p) throws ComException;

    Variant getWidth() throws ComException;
}
