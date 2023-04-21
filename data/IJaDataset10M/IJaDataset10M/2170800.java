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
 * Represents Java interface for COM interface IHTMLLinkElement.
 */
public interface IHTMLLinkElement extends IDispatch {

    public static final String INTERFACE_IDENTIFIER = "{3050F205-98B5-11CF-BB82-00AA00BDCE0B}";

    void setHref(BStr p) throws ComException;

    BStr getHref() throws ComException;

    void setRel(BStr p) throws ComException;

    BStr getRel() throws ComException;

    void setRev(BStr p) throws ComException;

    BStr getRev() throws ComException;

    void setType(BStr p) throws ComException;

    BStr getType() throws ComException;

    BStr getReadyState() throws ComException;

    void setOnreadystatechange(Variant p) throws ComException;

    Variant getOnreadystatechange() throws ComException;

    void setOnload(Variant p) throws ComException;

    Variant getOnload() throws ComException;

    void setOnerror(Variant p) throws ComException;

    Variant getOnerror() throws ComException;

    IHTMLStyleSheet getStyleSheet() throws ComException;

    void setDisabled(VariantBool p) throws ComException;

    VariantBool getDisabled() throws ComException;

    void setMedia(BStr p) throws ComException;

    BStr getMedia() throws ComException;
}
