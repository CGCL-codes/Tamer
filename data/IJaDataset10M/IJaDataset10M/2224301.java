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
 * Represents Java interface for COM interface DispHTMLAppBehavior.
 */
public interface DispHTMLAppBehavior extends IDispatch {

    public static final String INTERFACE_IDENTIFIER = "{3050F57C-98B5-11CF-BB82-00AA00BDCE0B}";

    static final int DISPID_setApplicationName = 5000;

    static final int DISPID_getApplicationName = 5000;

    static final int DISPID_setVersion = 5001;

    static final int DISPID_getVersion = 5001;

    static final int DISPID_setIcon = 5002;

    static final int DISPID_getIcon = 5002;

    static final int DISPID_setSingleInstance = 5003;

    static final int DISPID_getSingleInstance = 5003;

    static final int DISPID_setMinimizeButton = 5005;

    static final int DISPID_getMinimizeButton = 5005;

    static final int DISPID_setMaximizeButton = 5006;

    static final int DISPID_getMaximizeButton = 5006;

    static final int DISPID_setBorder = 5007;

    static final int DISPID_getBorder = 5007;

    static final int DISPID_setBorderStyle = 5008;

    static final int DISPID_getBorderStyle = 5008;

    static final int DISPID_setSysMenu = 5009;

    static final int DISPID_getSysMenu = 5009;

    static final int DISPID_setCaption = 5010;

    static final int DISPID_getCaption = 5010;

    static final int DISPID_setWindowState = 5011;

    static final int DISPID_getWindowState = 5011;

    static final int DISPID_setShowInTaskBar = 5012;

    static final int DISPID_getShowInTaskBar = 5012;

    static final int DISPID_getCommandLine = 5013;

    static final int DISPID_setContextMenu = 5014;

    static final int DISPID_getContextMenu = 5014;

    static final int DISPID_setInnerBorder = 5015;

    static final int DISPID_getInnerBorder = 5015;

    static final int DISPID_setScroll = 5016;

    static final int DISPID_getScroll = 5016;

    static final int DISPID_setScrollFlat = 5017;

    static final int DISPID_getScrollFlat = 5017;

    static final int DISPID_setSelection = 5018;

    static final int DISPID_getSelection = 5018;

    void setApplicationName(BStr param1);

    BStr getApplicationName();

    void setVersion(BStr param1);

    BStr getVersion();

    void setIcon(BStr param1);

    BStr getIcon();

    void setSingleInstance(BStr param1);

    BStr getSingleInstance();

    void setMinimizeButton(BStr param1);

    BStr getMinimizeButton();

    void setMaximizeButton(BStr param1);

    BStr getMaximizeButton();

    void setBorder(BStr param1);

    BStr getBorder();

    void setBorderStyle(BStr param1);

    BStr getBorderStyle();

    void setSysMenu(BStr param1);

    BStr getSysMenu();

    void setCaption(BStr param1);

    BStr getCaption();

    void setWindowState(BStr param1);

    BStr getWindowState();

    void setShowInTaskBar(BStr param1);

    BStr getShowInTaskBar();

    BStr getCommandLine();

    void setContextMenu(BStr param1);

    BStr getContextMenu();

    void setInnerBorder(BStr param1);

    BStr getInnerBorder();

    void setScroll(BStr param1);

    BStr getScroll();

    void setScrollFlat(BStr param1);

    BStr getScrollFlat();

    void setSelection(BStr param1);

    BStr getSelection();
}
