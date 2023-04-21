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
 * Represents Java interface for COM interface IHTMLAppBehavior2.
 */
public interface IHTMLAppBehavior2 extends IDispatch {

    public static final String INTERFACE_IDENTIFIER = "{3050F5C9-98B5-11CF-BB82-00AA00BDCE0B}";

    void setContextMenu(BStr p) throws ComException;

    BStr getContextMenu() throws ComException;

    void setInnerBorder(BStr p) throws ComException;

    BStr getInnerBorder() throws ComException;

    void setScroll(BStr p) throws ComException;

    BStr getScroll() throws ComException;

    void setScrollFlat(BStr p) throws ComException;

    BStr getScrollFlat() throws ComException;

    void setSelection(BStr p) throws ComException;

    BStr getSelection() throws ComException;
}
