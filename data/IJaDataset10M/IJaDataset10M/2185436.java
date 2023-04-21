package com.sun.midp.automation;

import java.util.*;
import com.sun.midp.main.*;
import com.sun.midp.events.*;
import com.sun.midp.configurator.Constants;
import com.sun.cldc.isolate.*;

/**
 * Class controlling states of the MIDlets
 */
final class AutoMIDletStateController implements MIDletProxyListListener {

    /** MIDlet proxy list reference. */
    private MIDletProxyList midletProxyList;

    /** List of AutoMIDletInfo for MIDlet's we are interested in */
    private AutoMIDletInfoList midletsInfo;

    /** The one and only AutoMIDletStateController instance */
    private static AutoMIDletStateController stateController = null;

    /**
     * Private constructor to prevent direct creation of instances.
     */
    private AutoMIDletStateController() {
        midletProxyList = MIDletProxyList.getMIDletProxyList();
        midletProxyList.addListener(this);
        midletsInfo = AutoMIDletInfoList.getMIDletInfoList();
    }

    /**
     * Starts MIDlet.
     *
     * @param midletDescriptor descriptor of MIDlet to start
     * @param args MIDlet's arguments
     * @throws throws RuntimeException if MIDlet couldn't be started
     * @return AutoMIDletImpl instance representing started MIDlet or
     *         null if there was a problem starting MIDlet
     */
    AutoMIDletImpl startMIDlet(AutoMIDletDescriptorImpl midletDescriptor, String[] args) {
        AutoMIDletImpl midlet = null;
        synchronized (midletsInfo) {
            AutoSuiteDescriptorImpl suite;
            suite = (AutoSuiteDescriptorImpl) midletDescriptor.getSuiteDescriptor();
            int suiteID = suite.getSuiteID();
            String midletClassName = midletDescriptor.getMIDletClassName();
            String midletName = midletDescriptor.getMIDletName();
            String arg1 = null;
            String arg2 = null;
            String arg3 = null;
            if (args != null) {
                if (args.length > 0) {
                    arg1 = args[0];
                }
                if (args.length > 1) {
                    arg2 = args[1];
                }
                if (args.length > 2) {
                    arg3 = args[2];
                }
            }
            AutoMIDletInfo info = midletsInfo.addToList(suiteID, midletClassName);
            Isolate isolate = AmsUtil.startMidletInNewIsolate(suiteID, midletClassName, midletName, arg1, arg2, arg3);
            while (info.midletProxy == null && info.startError == false) {
                try {
                    midletsInfo.wait();
                } catch (InterruptedException e) {
                }
            }
            if (info.startError) {
                midletsInfo.removeFromList(info);
                String errorMsg = "";
                switch(info.startErrorCode) {
                    case Constants.MIDLET_ISOLATE_CONSTRUCTOR_FAILED:
                        errorMsg = "MIDlet Isolate constructor failed";
                        break;
                    case Constants.MIDLET_RESOURCE_LIMIT:
                        errorMsg = "Not enough resources to start the MIDlet";
                        break;
                    case Constants.MIDLET_ISOLATE_RESOURCE_LIMIT:
                        errorMsg = "Maximum Isolate count is exceeded";
                        break;
                    case Constants.MIDLET_OUT_OF_MEM_ERROR:
                        errorMsg = "Not enough memory to start the MIDlet";
                        break;
                    case Constants.MIDLET_SUITE_NOT_FOUND:
                        errorMsg = "MIDlet suite is not found";
                        break;
                    case Constants.MIDLET_SUITE_DISABLED:
                        errorMsg = "MIDlet suite is disabled";
                        break;
                    case Constants.MIDLET_CLASS_NOT_FOUND:
                        errorMsg = "MIDlet class is not found";
                        break;
                    case Constants.MIDLET_INSTANTIATION_EXCEPTION:
                        errorMsg = "MIDlet instantiation exception";
                        break;
                    case Constants.MIDLET_ILLEGAL_ACCESS_EXCEPTION:
                        errorMsg = "MIDlet illegal access exception";
                        break;
                    case Constants.MIDLET_CONSTRUCTOR_FAILED:
                        errorMsg = "MIDlet constructor failed";
                        break;
                    default:
                        errorMsg = "Error starting MIDlet: " + info.startErrorCode;
                        break;
                }
                String details = info.startErrorDetails;
                if (details != null) {
                    errorMsg += " (" + details + ")";
                }
                throw new RuntimeException(errorMsg);
            } else {
                midlet = new AutoMIDletImpl(midletDescriptor);
                info.midlet = midlet;
                info.midletIsolate = isolate;
            }
        }
        return midlet;
    }

    /**
     * Initiates switching MIDlet to specified state.
     *
     * @param midlet AutoMIDletImpl instance representing MIDlet to switch
     * @param state state to switch to
     */
    void switchTo(AutoMIDletImpl midlet, AutoMIDletLifeCycleState state) {
        MIDletProxy midletProxy = midletsInfo.findMIDletProxy(midlet);
        if (midletProxy != null) {
            if (state == AutoMIDletLifeCycleState.ACTIVE) {
                midletProxy.activateMidlet();
            } else if (state == AutoMIDletLifeCycleState.PAUSED) {
                midletProxy.pauseMidlet();
            } else if (state == AutoMIDletLifeCycleState.DESTROYED) {
                midletProxy.destroyMidlet();
            }
        }
    }

    /**
     * Gets AutoMIDletStateController instance.
     *
     * @return AutoMIDletStateController instance
     */
    static synchronized AutoMIDletStateController getMIDletStateController() {
        if (stateController == null) {
            stateController = new AutoMIDletStateController();
        }
        return stateController;
    }

    /**
     * Called when a MIDlet Isolate has exited.
     *
     * @param midletProxy The proxy of the MIDlet being added
     */
    void isolateExited(MIDletProxy midletProxy) {
        synchronized (midletsInfo) {
            AutoMIDletInfo info = midletsInfo.findMIDletInfo(midletProxy);
            if (info != null) {
                AutoMIDletImpl midlet = info.midlet;
                if (midlet != null) {
                    midlet.stateChanged(AutoMIDletLifeCycleState.DESTROYED);
                }
                midletsInfo.removeFromList(info);
            }
        }
    }

    /**
     * Start notifier thread that will wait for the MIDlet's isolate to exit.
     *
     * @param proxy proxy of MIDlet
     */
    private void startNotificationThread(AutoMIDletInfo info) {
        IsolateExitNotifier notifier = new IsolateExitNotifier();
        notifier.midletProxy = info.midletProxy;
        notifier.midletIsolate = info.midletIsolate;
        notifier.parent = this;
        new Thread(notifier).start();
    }

    /**
     * Called when a MIDlet is added to the list.
     *
     * @param midletProxy The proxy of the MIDlet being added
     */
    public void midletAdded(MIDletProxy midletProxy) {
        synchronized (midletsInfo) {
            AutoMIDletInfo info = midletsInfo.findMIDletInfo(midletProxy);
            if (info != null) {
                info.midletProxy = midletProxy;
                midletsInfo.notify();
            }
        }
    }

    /**
     * Called when the state of a MIDlet in the list is updated.
     *
     * @param midletProxy The proxy of the MIDlet that was updated
     * @param fieldID code for which field of the proxy was updated
     */
    public void midletUpdated(MIDletProxy midletProxy, int fieldID) {
        AutoMIDletImpl midlet = midletsInfo.findMIDlet(midletProxy);
        if (midlet != null && fieldID == MIDletProxyListListener.MIDLET_STATE) {
            switch(midletProxy.getMidletState()) {
                case MIDletProxy.MIDLET_ACTIVE:
                    midlet.stateChanged(AutoMIDletLifeCycleState.ACTIVE);
                    break;
                case MIDletProxy.MIDLET_PAUSED:
                    midlet.stateChanged(AutoMIDletLifeCycleState.PAUSED);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Called when a MIDlet is removed from the list.
     *
     * @param midletProxy The proxy of the removed MIDlet
     */
    public void midletRemoved(MIDletProxy midletProxy) {
        synchronized (midletsInfo) {
            AutoMIDletInfo info = midletsInfo.findMIDletInfo(midletProxy);
            if (info != null) {
                startNotificationThread(info);
            }
        }
    }

    /**
     * Called when error occurred while starting a MIDlet object.
     *
     * @param externalAppID ID assigned by the external application manager
     * @param suiteID Suite ID of the MIDlet
     * @param className Class name of the MIDlet
     * @param errorCode start error code
     * @param errorDetails start error details
     */
    public void midletStartError(int externalAppID, int suiteID, String className, int errorCode, String errorDetails) {
        synchronized (midletsInfo) {
            AutoMIDletInfo info = midletsInfo.findMIDletInfo(suiteID, className);
            if (info != null) {
                info.startError = true;
                info.startErrorCode = errorCode;
                info.startErrorDetails = errorDetails;
                midletsInfo.notify();
            }
        }
    }
}

/**
 * Waits for an isolate to terminate and then notifies the native app
 * manager.
 */
final class IsolateExitNotifier implements Runnable {

    /** MIDlet information. */
    MIDletProxy midletProxy;

    /** Isolate of the MIDlet. */
    Isolate midletIsolate;

    /** Parent to notify */
    AutoMIDletStateController parent;

    /** Performs this classes function. */
    public void run() {
        midletIsolate.waitForExit();
        parent.isolateExited(midletProxy);
    }
}
