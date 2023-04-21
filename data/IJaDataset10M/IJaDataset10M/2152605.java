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
 * Represents Java interface for COM interface HTMLStyleElementEvents2.
 */
public interface HTMLStyleElementEvents2 extends IDispatch {

    public static final String INTERFACE_IDENTIFIER = "{3050F615-98B5-11CF-BB82-00AA00BDCE0B}";

    static final int DISPID_onhelp = -2147418102;

    static final int DISPID_onclick = -600;

    static final int DISPID_ondblclick = -601;

    static final int DISPID_onkeypress = -603;

    static final int DISPID_onkeydown = -602;

    static final int DISPID_onkeyup = -604;

    static final int DISPID_onmouseout = -2147418103;

    static final int DISPID_onmouseover = -2147418104;

    static final int DISPID_onmousemove = -606;

    static final int DISPID_onmousedown = -605;

    static final int DISPID_onmouseup = -607;

    static final int DISPID_onselectstart = -2147418100;

    static final int DISPID_onfilterchange = -2147418095;

    static final int DISPID_ondragstart = -2147418101;

    static final int DISPID_onbeforeupdate = -2147418108;

    static final int DISPID_onafterupdate = -2147418107;

    static final int DISPID_onerrorupdate = -2147418099;

    static final int DISPID_onrowexit = -2147418106;

    static final int DISPID_onrowenter = -2147418105;

    static final int DISPID_ondatasetchanged = -2147418098;

    static final int DISPID_ondataavailable = -2147418097;

    static final int DISPID_ondatasetcomplete = -2147418096;

    static final int DISPID_onlosecapture = -2147418094;

    static final int DISPID_onpropertychange = -2147418093;

    static final int DISPID_onscroll = 1014;

    static final int DISPID_onfocus = -2147418111;

    static final int DISPID_onblur = -2147418112;

    static final int DISPID_onresize = 1016;

    static final int DISPID_ondrag = -2147418092;

    static final int DISPID_ondragend = -2147418091;

    static final int DISPID_ondragenter = -2147418090;

    static final int DISPID_ondragover = -2147418089;

    static final int DISPID_ondragleave = -2147418088;

    static final int DISPID_ondrop = -2147418087;

    static final int DISPID_onbeforecut = -2147418083;

    static final int DISPID_oncut = -2147418086;

    static final int DISPID_onbeforecopy = -2147418082;

    static final int DISPID_oncopy = -2147418085;

    static final int DISPID_onbeforepaste = -2147418081;

    static final int DISPID_onpaste = -2147418084;

    static final int DISPID_oncontextmenu = 1023;

    static final int DISPID_onrowsdelete = -2147418080;

    static final int DISPID_onrowsinserted = -2147418079;

    static final int DISPID_oncellchange = -2147418078;

    static final int DISPID_onreadystatechange = -609;

    static final int DISPID_onlayoutcomplete = 1030;

    static final int DISPID_onpage = 1031;

    static final int DISPID_onmouseenter = 1042;

    static final int DISPID_onmouseleave = 1043;

    static final int DISPID_onactivate = 1044;

    static final int DISPID_ondeactivate = 1045;

    static final int DISPID_onbeforedeactivate = 1034;

    static final int DISPID_onbeforeactivate = 1047;

    static final int DISPID_onfocusin = 1048;

    static final int DISPID_onfocusout = 1049;

    static final int DISPID_onmove = 1035;

    static final int DISPID_oncontrolselect = 1036;

    static final int DISPID_onmovestart = 1038;

    static final int DISPID_onmoveend = 1039;

    static final int DISPID_onresizestart = 1040;

    static final int DISPID_onresizeend = 1041;

    static final int DISPID_onmousewheel = 1033;

    static final int DISPID_onload = 1003;

    static final int DISPID_onerror = 1002;

    VariantBool onhelp(IHTMLEventObj pEvtObj);

    VariantBool onclick(IHTMLEventObj pEvtObj);

    VariantBool ondblclick(IHTMLEventObj pEvtObj);

    VariantBool onkeypress(IHTMLEventObj pEvtObj);

    void onkeydown(IHTMLEventObj pEvtObj);

    void onkeyup(IHTMLEventObj pEvtObj);

    void onmouseout(IHTMLEventObj pEvtObj);

    void onmouseover(IHTMLEventObj pEvtObj);

    void onmousemove(IHTMLEventObj pEvtObj);

    void onmousedown(IHTMLEventObj pEvtObj);

    void onmouseup(IHTMLEventObj pEvtObj);

    VariantBool onselectstart(IHTMLEventObj pEvtObj);

    void onfilterchange(IHTMLEventObj pEvtObj);

    VariantBool ondragstart(IHTMLEventObj pEvtObj);

    VariantBool onbeforeupdate(IHTMLEventObj pEvtObj);

    void onafterupdate(IHTMLEventObj pEvtObj);

    VariantBool onerrorupdate(IHTMLEventObj pEvtObj);

    VariantBool onrowexit(IHTMLEventObj pEvtObj);

    void onrowenter(IHTMLEventObj pEvtObj);

    void ondatasetchanged(IHTMLEventObj pEvtObj);

    void ondataavailable(IHTMLEventObj pEvtObj);

    void ondatasetcomplete(IHTMLEventObj pEvtObj);

    void onlosecapture(IHTMLEventObj pEvtObj);

    void onpropertychange(IHTMLEventObj pEvtObj);

    void onscroll(IHTMLEventObj pEvtObj);

    void onfocus(IHTMLEventObj pEvtObj);

    void onblur(IHTMLEventObj pEvtObj);

    void onresize(IHTMLEventObj pEvtObj);

    VariantBool ondrag(IHTMLEventObj pEvtObj);

    void ondragend(IHTMLEventObj pEvtObj);

    VariantBool ondragenter(IHTMLEventObj pEvtObj);

    VariantBool ondragover(IHTMLEventObj pEvtObj);

    void ondragleave(IHTMLEventObj pEvtObj);

    VariantBool ondrop(IHTMLEventObj pEvtObj);

    VariantBool onbeforecut(IHTMLEventObj pEvtObj);

    VariantBool oncut(IHTMLEventObj pEvtObj);

    VariantBool onbeforecopy(IHTMLEventObj pEvtObj);

    VariantBool oncopy(IHTMLEventObj pEvtObj);

    VariantBool onbeforepaste(IHTMLEventObj pEvtObj);

    VariantBool onpaste(IHTMLEventObj pEvtObj);

    VariantBool oncontextmenu(IHTMLEventObj pEvtObj);

    void onrowsdelete(IHTMLEventObj pEvtObj);

    void onrowsinserted(IHTMLEventObj pEvtObj);

    void oncellchange(IHTMLEventObj pEvtObj);

    void onreadystatechange(IHTMLEventObj pEvtObj);

    void onlayoutcomplete(IHTMLEventObj pEvtObj);

    void onpage(IHTMLEventObj pEvtObj);

    void onmouseenter(IHTMLEventObj pEvtObj);

    void onmouseleave(IHTMLEventObj pEvtObj);

    void onactivate(IHTMLEventObj pEvtObj);

    void ondeactivate(IHTMLEventObj pEvtObj);

    VariantBool onbeforedeactivate(IHTMLEventObj pEvtObj);

    VariantBool onbeforeactivate(IHTMLEventObj pEvtObj);

    void onfocusin(IHTMLEventObj pEvtObj);

    void onfocusout(IHTMLEventObj pEvtObj);

    void onmove(IHTMLEventObj pEvtObj);

    VariantBool oncontrolselect(IHTMLEventObj pEvtObj);

    VariantBool onmovestart(IHTMLEventObj pEvtObj);

    void onmoveend(IHTMLEventObj pEvtObj);

    VariantBool onresizestart(IHTMLEventObj pEvtObj);

    void onresizeend(IHTMLEventObj pEvtObj);

    VariantBool onmousewheel(IHTMLEventObj pEvtObj);

    void onload(IHTMLEventObj pEvtObj);

    void onerror(IHTMLEventObj pEvtObj);
}
