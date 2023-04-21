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
 * Represents Java interface for COM interface IHTMLBodyElement.
 */
public interface IHTMLBodyElement extends IDispatch {

    public static final String INTERFACE_IDENTIFIER = "{3050F1D8-98B5-11CF-BB82-00AA00BDCE0B}";

    void setBackground(BStr p) throws ComException;

    BStr getBackground() throws ComException;

    void setBgProperties(BStr p) throws ComException;

    BStr getBgProperties() throws ComException;

    void setLeftMargin(Variant p) throws ComException;

    Variant getLeftMargin() throws ComException;

    void setTopMargin(Variant p) throws ComException;

    Variant getTopMargin() throws ComException;

    void setRightMargin(Variant p) throws ComException;

    Variant getRightMargin() throws ComException;

    void setBottomMargin(Variant p) throws ComException;

    Variant getBottomMargin() throws ComException;

    void setNoWrap(VariantBool p) throws ComException;

    VariantBool getNoWrap() throws ComException;

    void setBgColor(Variant p) throws ComException;

    Variant getBgColor() throws ComException;

    void setText(Variant p) throws ComException;

    Variant getText() throws ComException;

    void setLink(Variant p) throws ComException;

    Variant getLink() throws ComException;

    void setVLink(Variant p) throws ComException;

    Variant getVLink() throws ComException;

    void setALink(Variant p) throws ComException;

    Variant getALink() throws ComException;

    void setOnload(Variant p) throws ComException;

    Variant getOnload() throws ComException;

    void setOnunload(Variant p) throws ComException;

    Variant getOnunload() throws ComException;

    void setScroll(BStr p) throws ComException;

    BStr getScroll() throws ComException;

    void setOnselect(Variant p) throws ComException;

    Variant getOnselect() throws ComException;

    void setOnbeforeunload(Variant p) throws ComException;

    Variant getOnbeforeunload() throws ComException;

    IHTMLTxtRange createTextRange() throws ComException;
}
