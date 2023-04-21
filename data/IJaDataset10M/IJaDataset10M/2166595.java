package watij.shdocvw.server;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.server.*;
import com.jniwrapper.win32.com.types.*;
import watij.shdocvw.*;
import watij.shdocvw.impl.*;

/**
 * Adapter for server implementation of DWebBrowserEvents2
 */
public class DWebBrowserEvents2Server extends IDispatchServer implements DWebBrowserEvents2 {

    public DWebBrowserEvents2Server(CoClassMetaInfo classImpl) {
        super(classImpl);
        registerMethods(DWebBrowserEvents2.class);
    }

    public void statusTextChange(BStr Text) {
    }

    public void progressChange(Int32 Progress, Int32 ProgressMax) {
    }

    public void commandStateChange(Int32 Command, VariantBool Enable) {
    }

    public void downloadBegin() {
    }

    public void downloadComplete() {
    }

    public void titleChange(BStr Text) {
    }

    public void propertyChange(BStr szProperty) {
    }

    public void beforeNavigate2(IDispatch pDisp, Variant URL, Variant Flags, Variant TargetFrameName, Variant PostData, Variant Headers, VariantBool Cancel) {
    }

    public void newWindow2(IDispatch ppDisp, VariantBool Cancel) {
    }

    public void navigateComplete2(IDispatch pDisp, Variant URL) {
    }

    public void documentComplete(IDispatch pDisp, Variant URL) {
    }

    public void onQuit() {
    }

    public void onVisible(VariantBool Visible) {
    }

    public void onToolBar(VariantBool ToolBar) {
    }

    public void onMenuBar(VariantBool MenuBar) {
    }

    public void onStatusBar(VariantBool StatusBar) {
    }

    public void onFullScreen(VariantBool FullScreen) {
    }

    public void onTheaterMode(VariantBool TheaterMode) {
    }

    public void windowSetResizable(VariantBool Resizable) {
    }

    public void windowSetLeft(Int32 Left) {
    }

    public void windowSetTop(Int32 Top) {
    }

    public void windowSetWidth(Int32 Width) {
    }

    public void windowSetHeight(Int32 Height) {
    }

    public void windowClosing(VariantBool IsChildWindow, VariantBool Cancel) {
    }

    public void clientToHostWindow(Int32 CX, Int32 CY) {
    }

    public void setSecureLockIcon(Int32 SecureLockIcon) {
    }

    public void fileDownload(VariantBool Cancel) {
    }

    public void navigateError(IDispatch pDisp, Variant URL, Variant Frame, Variant StatusCode, VariantBool Cancel) {
    }

    public void printTemplateInstantiation(IDispatch pDisp) {
    }

    public void printTemplateTeardown(IDispatch pDisp) {
    }

    public void updatePageStatus(IDispatch pDisp, Variant nPage, Variant fDone) {
    }

    public void privacyImpactedStateChange(VariantBool bImpacted) {
    }

    public void newWindow3(IDispatch ppDisp, VariantBool Cancel, UInt32 dwFlags, BStr bstrUrlContext, BStr bstrUrl) {
    }
}
