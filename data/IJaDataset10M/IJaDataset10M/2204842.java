package com.aptana.ide.debug.internal.ui;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author Ingo Muschenetz
 *
 */
public final class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.aptana.ide.debug.internal.ui.messages";

    private Messages() {
    }

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    public static String LaunchDebuggerPromptStatusHandler_CloseActiveSession;

    /**
	 * WorkbenchCloseListener_ConfirmDebuggerExit
	 */
    public static String WorkbenchCloseListener_ConfirmDebuggerExit;

    public static String WorkbenchCloseListener_AptanaDebuggerIsActive_DoYouWantToExit;

    public static String WorkbenchCloseListener_AlwaysExitDebuggerWithoutPrompt;

    public static String Startup_Notification;

    public static String Startup_AptanaRequiresFirefox;

    public static String Startup_Download;

    public static String Startup_CheckAgain;

    public static String Startup_ExecutableFiles;

    /**
	 * LaunchBrowserPromptStatusHandler_BrowserLaunching
	 */
    public static String LaunchBrowserPromptStatusHandler_BrowserLaunching;

    /**
	 * LaunchBrowserPromptStatusHandler_BrowserIsAlreadyRunning
	 */
    public static String LaunchBrowserPromptStatusHandler_BrowserIsAlreadyRunning;

    /**
	 * LaunchDebuggerPromptStatusHandler_Title
	 */
    public static String LaunchDebuggerPromptStatusHandler_Title;

    /**
	 * LaunchDebuggerPromptStatusHandler_DebuggerSessionIsActive
	 */
    public static String LaunchDebuggerPromptStatusHandler_DebuggerSessionIsActive;

    /**
	 * JSDebugModelPresentation_line
	 */
    public static String JSDebugModelPresentation_line;

    public static String JSDebugModelPresentation_notavailable;

    public static String JSDebugModelPresentation_Terminated;

    public static String JSDebugModelPresentation_Suspended;

    public static String JSDebugModelPresentation_lineIn_0_1_2;

    public static String JSDebugModelPresentation_keywordAtLine_0_1_2;

    public static String JSDebugModelPresentation_atStartLine_0_1_2;

    public static String JSDebugModelPresentation_runToLine_0_1_2;

    public static String JSDebugModelPresentation_breakpointAtLine_0_1_2;

    public static String JSDebugModelPresentation_Stepping;

    public static String JSDebugModelPresentation_Running;

    public static String JSDebugModelPresentation_Exception_0;

    public static String JSDebugModelPresentation_UnknownName;

    public static String JSDebugModelPresentation_UnknownType;

    public static String JSDebugModelPresentation_UnknownValue;

    public static String JSDebugModelPresentation_DetailsComputing;

    public static String InstallDebuggerPromptStatusHandler_InstallDebuggerExtension;

    public static String InstallDebuggerPromptStatusHandler_WaitbrowserLaunches_AcceptExtensionInstallation_Quit;

    public static String InstallDebuggerPromptStatusHandler_WaitbrowserLaunches_Quit;

    public static String InstallDebuggerPromptStatusHandler_FirefoxIsRunning;

    public static String InstallDebuggerPromptStatusHandler_ExtensionInstalled;

    public static String InstallDebuggerPromptStatusHandler_ExtensionNotInstalled;
}
