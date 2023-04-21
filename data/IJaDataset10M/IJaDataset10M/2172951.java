package watij.mshtml;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import watij.mshtml.impl.*;

/**
 * Represents Java interface for COM interface IElementBehaviorCategory.
 */
public interface IElementBehaviorCategory extends IUnknown {

    public static final String INTERFACE_IDENTIFIER = "{3050F4ED-98B5-11CF-BB82-00AA00BDCE0B}";

    Pointer getCategory() throws ComException;
}
