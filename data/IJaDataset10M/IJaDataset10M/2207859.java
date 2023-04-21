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
 * Represents Java interface for COM interface IHTMLRect.
 */
public interface IHTMLRect extends IDispatch {

    public static final String INTERFACE_IDENTIFIER = "{3050F4A3-98B5-11CF-BB82-00AA00BDCE0B}";

    void setLeft(Int32 p) throws ComException;

    Int32 getLeft() throws ComException;

    void setTop(Int32 p) throws ComException;

    Int32 getTop() throws ComException;

    void setRight(Int32 p) throws ComException;

    Int32 getRight() throws ComException;

    void setBottom(Int32 p) throws ComException;

    Int32 getBottom() throws ComException;
}
