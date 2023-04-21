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
 * Represents Java interface for COM interface IHTMLInputFileElement.
 */
public interface IHTMLInputFileElement extends IDispatch {

    public static final String INTERFACE_IDENTIFIER = "{3050F2AD-98B5-11CF-BB82-00AA00BDCE0B}";

    BStr getType() throws ComException;

    void setName(BStr p) throws ComException;

    BStr getName() throws ComException;

    void setStatus(Variant p) throws ComException;

    Variant getStatus() throws ComException;

    void setDisabled(VariantBool p) throws ComException;

    VariantBool getDisabled() throws ComException;

    IHTMLFormElement getForm() throws ComException;

    void setSize(Int32 p) throws ComException;

    Int32 getSize() throws ComException;

    void setMaxLength(Int32 p) throws ComException;

    Int32 getMaxLength() throws ComException;

    void select() throws ComException;

    void setOnchange(Variant p) throws ComException;

    Variant getOnchange() throws ComException;

    void setOnselect(Variant p) throws ComException;

    Variant getOnselect() throws ComException;

    void setValue(BStr p) throws ComException;

    BStr getValue() throws ComException;
}
