package org.kablink.teaming.gwt.client.util;

import java.util.Date;
import java.util.List;
import org.kablink.teaming.gwt.client.GwtMainPage;
import org.kablink.teaming.gwt.client.GwtTeaming;
import org.kablink.teaming.gwt.client.GwtTeamingException;
import org.kablink.teaming.gwt.client.GwtTeamingMessages;
import org.kablink.teaming.gwt.client.RequestInfo;
import org.kablink.teaming.gwt.client.event.TeamingEvents;
import org.kablink.teaming.gwt.client.lpe.LandingPageEditor;
import org.kablink.teaming.gwt.client.profile.widgets.GwtProfilePage;
import org.kablink.teaming.gwt.client.rpc.shared.VibeRpcCmd;
import org.kablink.teaming.gwt.client.rpc.shared.VibeRpcResponse;
import org.kablink.teaming.gwt.client.tasklisting.TaskListing;
import org.kablink.teaming.gwt.client.widgets.MultiErrorAlertDlg;
import org.kablink.teaming.gwt.client.widgets.MultiErrorAlertDlg.MultiErrorAlertDlgClient;
import org.kablink.teaming.gwt.client.widgets.WidgetStyles;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TeamingPopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * Helper methods for the GWT UI client code.
 *
 * @author drfoster@novell.com
 */
public class GwtClientHelper {

    private static MultiErrorAlertDlg m_meaDlg;

    private static final String AMPERSAND_FORMAT_MARKER = "a/do?";

    private static final String PERMALINK_MARKER = "view_permalink";

    public static final long DAY_IN_MS = (24L * 60L * 60L * 1000L);

    public enum ScrollType {

        BOTH, HORIZONTAL, VERTICAL
    }

    private GwtClientHelper() {
    }

    /**
	 * Adds a Long to a List<Long> if it's not already there.
	 * 
	 * @param lList
	 * @param l
	 */
    public static void addLongToListLongIfUnique(List<Long> lList, Long l) {
        if (!(lList.contains(l))) {
            lList.add(l);
        }
    }

    /**
	 * Appends a <br /> to the HTML of a Widget.
	 * 
	 * @param w
	 */
    public static void appendBR(Widget w) {
        Element wE = w.getElement();
        String html = wE.getInnerHTML();
        wE.setInnerHTML(html + "<br />");
    }

    /**
	 * Appends a parameter to to a URL.
	 * 
	 * @param urlString
	 * @param pName
	 * @param pValue
	 * 
	 * @return
	 */
    public static String appendUrlParam(String urlString, String pName, String pValue) {
        String param;
        boolean useAmpersand = (0 < urlString.indexOf(AMPERSAND_FORMAT_MARKER));
        if (useAmpersand) param = ("&" + pName + "=" + pValue); else param = ("/" + pName + "/" + pValue);
        if (0 > urlString.indexOf(param)) {
            urlString += param;
        }
        return urlString;
    }

    /**
	 * Returns the boolean value stored in a string.
	 * 
	 * @param s
	 * @param def
	 * 
	 * @return
	 */
    public static boolean bFromS(String s, boolean def) {
        return (hasString(s) ? Boolean.parseBoolean(s) : def);
    }

    public static boolean bFromS(String s) {
        return bFromS(s, false);
    }

    /**
	 * Returns a base Image widget.
	 * 
	 * @param res
	 * @param title
	 */
    public static Image buildImage(ImageResource res, String title) {
        Image reply = new Image(res);
        reply.getElement().setAttribute("align", "absmiddle");
        if (hasString(title)) {
            reply.setTitle(title);
        }
        return reply;
    }

    public static Image buildImage(ImageResource res) {
        return buildImage(res, null);
    }

    /**
	 * If we're in debug UI mode, displays an alert.
	 * 
	 * @param msg
	 */
    public static void debugAlert(String msg) {
        if (isDebugUI()) {
            deferredAlert(msg);
        }
    }

    /**
	 * Displays a messages in an 'deferred' alert box.
	 * 
	 * @param msg
	 * @param delay
	 */
    public static void deferredAlert(final String msg, int delay) {
        if (hasString(msg)) {
            if (0 >= delay) {
                ScheduledCommand cmd = new ScheduledCommand() {

                    @Override
                    public void execute() {
                        Window.alert(msg);
                    }
                };
                Scheduler.get().scheduleDeferred(cmd);
            } else {
                Timer timer = new Timer() {

                    @Override
                    public void run() {
                        Window.alert(msg);
                    }
                };
                timer.schedule(delay);
            }
        }
    }

    public static void deferredAlert(final String msg) {
        deferredAlert(msg, 0);
    }

    /**
	 * Displays a message to the user regarding possibly multiple
	 * errors.
	 * 
	 * @param baseError
	 * @param multiErrors
	 * @param delay
	 */
    public static void displayMultipleErrors(final String baseError, final List<String> multiErrors, final int delay) {
        if (null == m_meaDlg) {
            MultiErrorAlertDlg.createAsync(new MultiErrorAlertDlgClient() {

                @Override
                public void onUnavailable() {
                }

                @Override
                public void onSuccess(MultiErrorAlertDlg meaDlg) {
                    m_meaDlg = meaDlg;
                    displayMultipleErrorsAsync(baseError, multiErrors, delay);
                }
            });
        } else {
            displayMultipleErrorsAsync(baseError, multiErrors, delay);
        }
    }

    public static void displayMultipleErrors(String baseError, List<String> multiErrors) {
        displayMultipleErrors(baseError, multiErrors, 0);
    }

    private static void displayMultipleErrorsAsync(final String baseError, final List<String> multiErrors, final int delay) {
        if (hasString(baseError) && (null != multiErrors) && (!(multiErrors.isEmpty()))) {
            if (0 >= delay) {
                ScheduledCommand cmd = new ScheduledCommand() {

                    @Override
                    public void execute() {
                        displayMultipleErrorsNow(baseError, multiErrors);
                    }
                };
                Scheduler.get().scheduleDeferred(cmd);
            } else {
                Timer timer = new Timer() {

                    @Override
                    public void run() {
                        displayMultipleErrorsNow(baseError, multiErrors);
                    }
                };
                timer.schedule(delay);
            }
        }
    }

    private static void displayMultipleErrorsNow(String baseError, List<String> multiErrors) {
        MultiErrorAlertDlg.initAndShow(m_meaDlg, baseError, multiErrors);
    }

    /**
	 * Execute the given command via GWT's rpc mechanism
	 */
    public static void executeCommand(VibeRpcCmd cmd, AsyncCallback<VibeRpcResponse> callback) {
        GwtTeaming.getRpcService().executeCommand(HttpRequestInfo.createHttpRequestInfo(), cmd, callback);
    }

    /**
	 * Returns the RequestInfo object from whatever component we're
	 * running as.
	 *
	 * Returns null if a RequestInfo cannot be determined.
	 * 
	 * @return
	 */
    public static RequestInfo getRequestInfo() {
        RequestInfo reply;
        if (null != GwtMainPage.m_requestInfo) reply = GwtMainPage.m_requestInfo; else if (null != GwtProfilePage.profileRequestInfo) reply = GwtProfilePage.profileRequestInfo; else if (null != TaskListing.m_requestInfo) reply = TaskListing.m_requestInfo; else if (null != LandingPageEditor.m_requestInfo) reply = LandingPageEditor.m_requestInfo; else reply = null;
        if (null == reply) {
            deferredAlert(GwtTeaming.getMessages().missingRequestInfo());
        }
        return reply;
    }

    /**
     * Returns the client's timezone offset.
	 * 
	 * Note:
	 *    We use deprecated APIs here since GWT's client side has no
	 *    GregorianCalendar equivalent.  This is the only way to
	 *    manipulate a date and time.
     * 
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getTimeZoneOffset() {
        final Date now = new Date();
        final int tzo = now.getTimezoneOffset();
        return tzo;
    }

    /**
	 * Returns the client's timezone offset in milliseconds.
	 * 
	 * @return
	 */
    public static long getTimeZoneOffsetMillis() {
        return (((long) getTimeZoneOffset()) * 60L * 1000L);
    }

    /**
     * Converts a time in GMT to a GWT Date object which is in the
     * timezone of the browser.
     * 
     * @param time
     * 
     * @return
     */
    public static final Date gmtToLocal(Date date) {
        return new Date(date.getTime() + getTimeZoneOffsetMillis());
    }

    /**
     * Converts a GWT Date in the timezone of the browser to a time in
     * GMT.
     * 
     * @param date
     * 
     * @return
     */
    public static final Date localToGmt(Date date) {
        return new Date(date.getTime() - getTimeZoneOffsetMillis());
    }

    /**
	 * Applies patches to a message string.
	 * 
	 * @param msg
	 * @param patches
	 * 
	 * @return
	 */
    public static String patchMessage(String msg, String[] patches) {
        int count = ((null == patches) ? 0 : patches.length);
        for (int i = 0; i < count; i += 1) {
            String delimiter = ("[" + i + "]");
            String patch = patches[i];
            while (msg.contains(delimiter)) {
                msg = msg.replace(delimiter, patch);
            }
        }
        return msg;
    }

    public static String patchMessage(String msg, String patch) {
        return patchMessage(msg, new String[] { patch });
    }

    /**
	 * Handles Throwable's received by GWT RPC onFailure() methods.
	 * 
	 * Notes:
	 * 1) Passing no error message string here allows for the proper
	 *    exception handling to occur but will NOT display an error to
	 *    the user.
	 * 2) On 20100803 I (Dennis) discussed this with Jay and we agreed
	 *    that in the Durango release, we'll only display errors here
	 *    for those exceptions that we really know something about.
	 *    The others will be ignored.
	 * 
	 * @param t
	 * @param errorMessage
	 * @param patches
	 */
    public static void handleGwtRPCFailure(Throwable t, String errorMessage, String[] patches) {
        boolean displayAlert = false;
        if (null != t) {
            GwtTeamingMessages messages = GwtTeaming.getMessages();
            String cause;
            if (t instanceof GwtTeamingException) {
                switch(((GwtTeamingException) t).getExceptionType()) {
                    case ACCESS_CONTROL_EXCEPTION:
                        displayAlert = true;
                        cause = patchMessage(messages.rpcFailure_AccessToFolderDenied(), patches);
                        break;
                    case FAVORITES_LIMIT_EXCEEDED:
                        errorMessage = messages.rpcFailure_AddFavoriteLimitExceeded();
                        cause = "";
                        displayAlert = true;
                        break;
                    case NO_BINDER_BY_THE_ID_EXCEPTION:
                        displayAlert = true;
                        cause = patchMessage(messages.rpcFailure_FolderDoesNotExist(), patches);
                        break;
                    case USER_NOT_LOGGED_IN:
                        cause = null;
                        GwtTeaming.getMainPage().handleSessionExpired();
                        break;
                    default:
                        cause = patchMessage(messages.rpcFailure_UnknownException(), patches);
                        break;
                }
            } else {
                cause = t.getLocalizedMessage();
                if (!(hasString(cause))) {
                    cause = t.toString();
                }
            }
            if (!(hasString(cause))) {
                cause = messages.rpcFailure_UnknownCause();
            }
            patches = new String[] { cause };
        }
        if (hasString(errorMessage) && (displayAlert || isDebugUI())) {
            errorMessage = patchMessage(errorMessage, patches);
            deferredAlert(errorMessage);
        }
    }

    public static void handleGwtRPCFailure(Throwable t, String errorMessage, String patch) {
        handleGwtRPCFailure(t, errorMessage, new String[] { patch });
    }

    public static void handleGwtRPCFailure(Throwable t, String errorMessage, Long patch) {
        handleGwtRPCFailure(t, errorMessage, new String[] { String.valueOf(patch) });
    }

    public static void handleGwtRPCFailure(Throwable t, String errorMessage) {
        handleGwtRPCFailure(t, errorMessage, ((String[]) null));
    }

    public static void handleGwtRPCFailure(Throwable t) {
        handleGwtRPCFailure(t, null, ((String[]) null));
    }

    /**
	 * Returns true is s refers to a non null, non 0 length String and
	 * false otherwise.
	 * 
	 * @param s
	 * @return
	 */
    public static boolean hasString(String s) {
        return ((null != s) && (0 < s.length()));
    }

    /**
	 * Returns the integer value stored in a string.
	 * 
	 * @param s
	 * @param def
	 * 
	 * @return
	 */
    public static int iFromS(String s, int def) {
        return (hasString(s) ? Integer.parseInt(s) : def);
    }

    public static int iFromS(String s) {
        return iFromS(s, (-1));
    }

    /**
	 * Open a window with the url that points to the appropriate help
	 * documentation.
	 */
    public static void invokeHelp(HelpData helpData) {
        if (null != helpData) {
            String url = helpData.getUrl();
            if (hasString(url)) {
                Window.open(url, "teaming_help_window", "resizeable,scrollbars");
            }
        }
    }

    /**
	 * Returns true if the UI is in debug mode for the landing page
	 * 
	 * @return
	 */
    public static boolean isDebugLP() {
        RequestInfo ri = getRequestInfo();
        if (null == ri) {
            return false;
        }
        return ri.isDebugLP();
    }

    /**
	 * Returns true if the UI is in debug mode and false otherwise.
	 * 
	 * @return
	 */
    public static boolean isDebugUI() {
        RequestInfo ri = getRequestInfo();
        if (null == ri) {
            return false;
        }
        return ri.isDebugUI();
    }

    /**
	 * Return whether the Granite GWT enhancements should enabled or
	 * not.
	 * 
	 * @return
	 */
    public boolean isGraniteGwtEnabled() {
        RequestInfo ri = getRequestInfo();
        return ((null == ri) ? false : ri.isGraniteGwtEnabled());
    }

    /**
	 * Returns true if a URL is a permalink URL and false otherwise.
	 * 
	 * @param url
	 * @return
	 */
    public static boolean isPermalinkUrl(String url) {
        boolean reply = hasString(url);
        if (reply) {
            reply = (0 < url.indexOf(PERMALINK_MARKER));
        }
        return reply;
    }

    /**
	 * Invokes the simple profile dialog off an HTML Element.
	 * 
	 * @param htmlElement
	 * @param binderId
	 * @param userName
	 */
    public static native void invokeSimpleProfile(Element htmlElement, String binderId, String userName);

    /**
	 * Appends an HTML element to the top document.
	 * 
	 * @param htmlElement
	 */
    public static native void jsAppendDocumentElement(Element htmlElement);

    public static native String jsBuildTagSearchUrl(String tag);

    /**
	 * Uses JavaScript native method to URI encode a string.
	 * 
	 * @param s
	 * 
	 * @return
	 */
    public static native String jsEncodeURIComponent(String s);

    /**
	 * Evaluates a JavaScript string containing embedded
	 * JavaScript. 
	 * 
	 * Note:  The code that's now in GwtMainPage.jsEvalStringImpl() was
	 *    originally inside this method but GWT's obfuscation used for
	 *    our production compiles broke it.
	 * 
	 * @param url
	 * @param jsString
	 */
    public static native void jsEvalString(String url, String jsString);

    private static native void jsFixFirstCol(Element eTD);

    /**
	 * Search for <script type="text/javascript"> elements found in the given html element
	 * and execute the javascript.
	 * 
	 * @param htmlElement
	 */
    public static native void jsExecuteJavaScript(Element htmlElement);

    /**
	 * Returns the binder ID from the content IFRAME.
	 * 
	 * @return
	 */
    public static native String jsGetContentBinderId();

    /**
	 * Returns the contributor IDs from the content IFRAME.
	 * 
	 * @return
	 */
    public static native String jsGetContentContributorIds();

    /**
	 * Returns the left position of the content <IFRAME>'s <DIV>.
	 * 
	 * @return
	 */
    public static native int jsGetContentIFrameLeft();

    /**
	 * Returns the top position of the content <IFRAME>'s <DIV>.
	 * 
	 * @return
	 */
    public static native int jsGetContentIFrameTop();

    /**
	 * Returns the view type of what's being viewed in the content
	 * panel.
	 * 
	 * @return
	 */
    public static native String jsGetViewType();

    /**
	 * Hides the popup entry iframe div if one exists.
	 */
    public static native void jsHideEntryPopupDiv();

    /**
	 * Called to hide any open entry view DIV that's in new page mode.
	 */
    public static native void jsHideNewPageEntryViewDIV();

    /**
	 * Invoke the "define editor overrides" dialog.
	 */
    public static native void jsInvokeDefineEditorOverridesDlg();

    /**
	 * Returns true if we're running in any flavor of IE and false
	 * otherwise.
	 * 
	 * @return
	 */
    public static native boolean jsIsIE();

    /**
	 * Uses Teaming's existing ss_common JavaScript to launch a toolbar
	 * popup URL.
	 * 
	 * @param url
	 */
    public static native void jsLaunchToolbarPopupUrl(String url, String w, String h);

    public static void jsLaunchToolbarPopupUrl(String url, int w, int h) {
        jsLaunchToolbarPopupUrl(url, String.valueOf(w), String.valueOf(h));
    }

    public static void jsLaunchToolbarPopupUrl(String url) {
        jsLaunchToolbarPopupUrl(url, "", "");
    }

    /**
	 * Uses Teaming's existing ss_common JavaScript to launch a URL in
	 * a new window.
	 * 
	 * @param url
	 * @param windowName
	 * @param windowHeight
	 * @param windowWidth
	 */
    public static native void jsLaunchUrlInWindow(String url, String windowName, int windowHeight, int windowWidth);

    public static native void jsLaunchUrlInWindow(String url, String windowName);

    /**
	 * Loads a URL into the current window.
	 * 
	 * @param url
	 */
    public static native void jsLoadUrlInCurrentWindow(String url);

    /**
	 * Loads a URL into the top window.
	 * 
	 * @param url
	 */
    public static native void jsLoadUrlInTopWindow(String url);

    /**
	 * Use Teaming's existing JavaScript to logout of Teaming.
	 */
    public static native void jsLogout();

    /**
	 * Used to fire a simple Vibe event to the outer most GwtMainpage's
	 * event bus from anywhere within the application.
	 * 
	 * @param event
	 */
    public static native void jsFireVibeEventOnMainEventBus(TeamingEvents eventEnum);

    /**
	 * Called to force the GWT UI content area to resize itself based
	 * on its current content.
	 * 
	 * @param reason
	 */
    public static native void jsResizeGwtContent(String reason);

    /**
	 * Set the JavaScript variable, ss_userDisplayStyle, to the given
	 * value.
	 */
    public static native void jsSetEntryDisplayStyle(String style);

    /**
	 * Call ss_setEntryPopupIframeSize() to set the size and position of the view entry popup div.
	 */
    public static native void jsSetEntryPopupIframeSize();

    /**
	 * Sets the text on the main GWT page's <title>.
	 */
    public static native void jsSetMainTitle(String title);

    /**
	 * Runs an entry view URL in the content frame.
	 * 
	 * @param url
	 */
    public static native void jsShowForumEntry(String entryUrl);

    /**
	 * Compares two strings by collation.
	 * 
	 * Returns:
	 *    -1 if s1 <  s2;
	 *     0 if s1 == s2; and
	 *     1 if s1 >  s2.
	 *     
	 * @param s1
	 * @param s2
	 * 
	 * @return
	 */
    public static native int jsStringCompare(String s1, String s2);

    /**
	 * Sets a TeamingPopupPanel to use one-way-corner animation to
	 * open.
	 * 
	 * @param popup
	 */
    public static void oneWayCornerPopup(TeamingPopupPanel popup) {
        popup.setAnimationEnabled(true);
        popup.setAnimationTypeToOneWayCorner();
    }

    /**
	 * Removes all the child Node's from a DOM Element.
	 *  
	 * @param e
	 */
    public static void removeAllChildren(Element e) {
        if (null != e) {
            Node child = e.getFirstChild();
            while (null != child) {
                e.removeChild(child);
                child = e.getFirstChild();
            }
        }
    }

    /**
	 * Sets a TeamingPopupPanel to use roll-down animation to open.
	 * 
	 * @param popup
	 */
    public static void rollDownPopup(TeamingPopupPanel popup) {
        popup.setAnimationEnabled(true);
        popup.setAnimationTypeToRollDown();
    }

    /**
	 * Performs a collated compare on two strings without generating any
	 * exceptions.
	 * 
	 * Returns:
	 *    -1 if s1 <  s2;
	 *     0 if s1 == s2; and
	 *     1 if s1 >  s2.
	 *     
	 * @param s1
	 * @param s2
	 * 
	 * @return
	 */
    public static int safeSColatedCompare(String s1, String s2) {
        return jsStringCompare(((null == s1) ? "" : s1), ((null == s2) ? "" : s2));
    }

    /**
	 * For the given list box, select the item in the list box that has
	 * the given value.
	 * 
	 * @param listbox
	 * @param value
	 */
    public static int selectListboxItemByValue(ListBox listbox, String value) {
        int i;
        for (i = 0; i < listbox.getItemCount(); i += 1) {
            String tmp = listbox.getValue(i);
            if (tmp != null && tmp.equalsIgnoreCase(value)) {
                listbox.setSelectedIndex(i);
                return i;
            }
        }
        return -1;
    }

    /**
	 * Sets up a colspan="span" that spans the cells of a row in a
	 * Grid beginning at index start in a way that works on IE, FF, ...
	 *
	 * Note:  This is a hack to get around the inability to natively
	 *        set a colspan on cells of GWT Grid.
	 *
	 * @param grid
	 * @param row
	 * @param cell
	 */
    public static void setGridColSpan(Grid grid, int row, int start, int span) {
        jsFixFirstCol(grid.getCellFormatter().getElement(row, start));
        for (int i = 1; i < span; i += 1) {
            start += 1;
            grid.getCellFormatter().getElement(row, start).addClassName("grid_HideCell");
        }
    }

    /**
	 * Set the background color if specified in the WidgetStyles.
	 */
    public static void setElementBackgroundColor(Element element, String color) {
        if (element != null && color != null && color.length() > 0) {
            Style style;
            style = element.getStyle();
            style.setBackgroundColor(color);
        }
    }

    /**
	 * Set the border width and color if specified in the WidgetStyles
	 */
    public static void setElementBorderStyles(Element element, WidgetStyles widgetStyles) {
        if (element != null) {
            String width;
            String color;
            Style style;
            style = element.getStyle();
            width = widgetStyles.getBorderWidth();
            if (width != null && width.length() > 0) {
                style.setBorderWidth(Double.valueOf(width), Unit.PX);
            }
            color = widgetStyles.getBorderColor();
            if (color != null && color.length() > 0) {
                style.setBorderColor(color);
            }
        }
    }

    /**
	 * Set the text color if one is specified in the WidgetStyles.
	 */
    public static void setElementTextColor(Element element, String color) {
        if (element != null && color != null && color.length() > 0) {
            Style style;
            style = element.getStyle();
            style.setColor(color);
        }
    }

    /**
	 * Set the overflow style on the given UIObject
	 */
    public static void setOverflow(Style.Overflow overflow, UIObject uiObj) {
        Style style;
        style = uiObj.getElement().getStyle();
        if (style != null) style.setOverflow(overflow);
    }

    /**
	 * Set the height of the given UIObject
	 */
    public static void setHeight(int height, Unit unit, UIObject uiObj) {
        Style style;
        style = uiObj.getElement().getStyle();
        if (style != null) {
            if (height != 100 || unit != Unit.PCT) style.setHeight(height, unit);
        }
    }

    /**
	 * Set the width of the given UIObject
	 */
    public static void setWidth(int width, Unit unit, UIObject uiObj) {
        Style style;
        style = uiObj.getElement().getStyle();
        if (style != null) {
            if (width != 100 || unit != Unit.PCT) style.setWidth(width, unit);
        }
    }

    /**
	 * Replaces all occurrences of oldSub with newSub in s.
	 * 
	 * The implementation was copied from StringUtil.replace().
	 * 
	 * @param s
	 * @param oldSub
	 * @param newSub
	 * 
	 * @return
	 */
    public static String replace(String s, String oldSub, String newSub) {
        if ((s == null) || (oldSub == null) || (newSub == null)) {
            return null;
        }
        int y = s.indexOf(oldSub);
        if (y >= 0) {
            StringBuffer sb = new StringBuffer();
            int length = oldSub.length();
            int x = 0;
            while (x <= y) {
                sb.append(s.substring(x, y));
                sb.append(newSub);
                x = y + length;
                y = s.indexOf(oldSub, x);
            }
            sb.append(s.substring(x));
            return sb.toString();
        } else {
            return s;
        }
    }

    /**
	 * Adds scroll bars to the main content panel for the duration of a
	 * PopupPanel.
	 *  
	 * @param popup
	 * @param scrollType
	 */
    public static void scrollUIForPopup(PopupPanel popup, ScrollType scrollType) {
        final String scrollClass;
        switch(scrollType) {
            default:
            case VERTICAL:
                scrollClass = "gwtUI_ss_forceVerticalScroller";
                break;
            case BOTH:
                scrollClass = "gwtUI_ss_forceBothScrollers";
                break;
            case HORIZONTAL:
                scrollClass = "gwtUI_ss_forceHorizontalScroller";
                break;
        }
        final Element bodyElement = RootPanel.getBodyElement();
        String className = bodyElement.getClassName();
        if ((!(hasString(className))) || (0 > className.indexOf(scrollClass))) {
            bodyElement.addClassName(scrollClass);
            popup.addCloseHandler(new CloseHandler<PopupPanel>() {

                @Override
                public void onClose(CloseEvent<PopupPanel> event) {
                    bodyElement.removeClassName(scrollClass);
                }
            });
        }
    }

    public static void scrollUIForPopup(PopupPanel popup) {
        scrollUIForPopup(popup, ScrollType.VERTICAL);
    }

    /**
	 * Simulates a click event on the given element.
	 * 
	 * @param e
	 */
    public static void simulateElementClick(Element e) {
        NativeEvent clickEvent = Document.get().createClickEvent(1, 0, 0, 0, 0, false, false, false, false);
        e.dispatchEvent(clickEvent);
    }

    /**
	 * Validates we have a URL in an OnSelectBinderInfo object.
	 * 
	 * Optionally displays an error if there isn't and returns false.
	 * Otherwise, returns true.
	 * 
	 * @param osbi
	 * @param displayError
	 * 
	 * @return
	 */
    public static boolean validateOSBI(OnSelectBinderInfo osbi, boolean displayError) {
        if (!(hasString(osbi.getBinderUrl()))) {
            if (displayError) {
                deferredAlert(GwtTeaming.getMessages().cantAccessFolder());
            }
            return false;
        }
        return true;
    }

    public static boolean validateOSBI(OnSelectBinderInfo osbi) {
        return validateOSBI(osbi, true);
    }
}
