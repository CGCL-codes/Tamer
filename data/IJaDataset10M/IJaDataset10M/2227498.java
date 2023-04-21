package com.bluemarsh.jswat.core;

import com.sun.jdi.request.AccessWatchpointRequest;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.ClassUnloadRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ExceptionRequest;
import com.sun.jdi.request.MethodEntryRequest;
import com.sun.jdi.request.MethodExitRequest;
import com.sun.jdi.request.ModificationWatchpointRequest;
import com.sun.jdi.request.StepRequest;
import com.sun.jdi.request.ThreadDeathRequest;
import com.sun.jdi.request.ThreadStartRequest;
import com.sun.jdi.request.VMDeathRequest;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Nathan Fiedler
 */
public class EventRequests {

    /**
     * Creates a new instance of EventRequests.
     */
    private EventRequests() {
    }

    /**
     * Generate a detailed report of all the event requests in the debuggee.
     *
     * @param  erm  event request manager.
     * @return  a string containing request descriptions.
     */
    public static String describeRequests(EventRequestManager erm) {
        StringBuilder sb = new StringBuilder(512);
        List requests = erm.accessWatchpointRequests();
        Iterator iter = requests.iterator();
        sb.append("Access watchpoint requests:\n");
        while (iter.hasNext()) {
            AccessWatchpointRequest awr = (AccessWatchpointRequest) iter.next();
            printBasics(awr, sb);
            sb.append("\tField: ");
            sb.append(awr.field().toString());
            sb.append("\n");
        }
        sb.append("\n");
        requests = erm.breakpointRequests();
        iter = requests.iterator();
        sb.append("Breakpoint requests:\n");
        while (iter.hasNext()) {
            BreakpointRequest br = (BreakpointRequest) iter.next();
            printBasics(br, sb);
            sb.append("\tLocation: ");
            sb.append(br.location().toString());
            sb.append("\n");
        }
        sb.append("\n");
        requests = erm.classPrepareRequests();
        iter = requests.iterator();
        sb.append("Class prepare requests:\n");
        while (iter.hasNext()) {
            ClassPrepareRequest cpr = (ClassPrepareRequest) iter.next();
            printBasics(cpr, sb);
            sb.append("\n");
        }
        sb.append("\n");
        requests = erm.classUnloadRequests();
        iter = requests.iterator();
        sb.append("Class unload requests:\n");
        while (iter.hasNext()) {
            ClassUnloadRequest cur = (ClassUnloadRequest) iter.next();
            printBasics(cur, sb);
            sb.append("\n");
        }
        sb.append("\n");
        requests = erm.exceptionRequests();
        iter = requests.iterator();
        sb.append("Exception requests:\n");
        while (iter.hasNext()) {
            ExceptionRequest er = (ExceptionRequest) iter.next();
            printBasics(er, sb);
            sb.append("\tException: ");
            sb.append(String.valueOf(er.exception()));
            sb.append("\n\tNotify caught: ");
            sb.append(String.valueOf(er.notifyCaught()));
            sb.append("\n\tNotify uncaught: ");
            sb.append(String.valueOf(er.notifyUncaught()));
            sb.append("\n");
        }
        sb.append("\n");
        requests = erm.methodEntryRequests();
        iter = requests.iterator();
        sb.append("Method entry requests:\n");
        while (iter.hasNext()) {
            MethodEntryRequest mer = (MethodEntryRequest) iter.next();
            printBasics(mer, sb);
            sb.append("\n");
        }
        sb.append("\n");
        requests = erm.methodExitRequests();
        iter = requests.iterator();
        sb.append("Method exit requests:\n");
        while (iter.hasNext()) {
            MethodExitRequest mer = (MethodExitRequest) iter.next();
            printBasics(mer, sb);
            sb.append("\n");
        }
        sb.append("\n");
        requests = erm.modificationWatchpointRequests();
        iter = requests.iterator();
        sb.append("Modification watchpoint requests:\n");
        while (iter.hasNext()) {
            ModificationWatchpointRequest mwr = (ModificationWatchpointRequest) iter.next();
            printBasics(mwr, sb);
            sb.append("\tField: ");
            sb.append(mwr.field().toString());
            sb.append("\n");
        }
        sb.append("\n");
        requests = erm.stepRequests();
        iter = requests.iterator();
        sb.append("Step requests:\n");
        while (iter.hasNext()) {
            StepRequest sr = (StepRequest) iter.next();
            printBasics(sr, sb);
            sb.append("\tThread: ");
            if (sr.thread() != null) {
                sb.append(sr.thread().toString());
            }
            int depth = sr.depth();
            sb.append("\n\tDepth: ");
            if (depth == StepRequest.STEP_INTO) {
                sb.append("into");
            } else if (depth == StepRequest.STEP_OUT) {
                sb.append("out");
            } else if (depth == StepRequest.STEP_OVER) {
                sb.append("over");
            } else {
                sb.append("unknown");
            }
            int size = sr.size();
            sb.append("\n\tSize: ");
            if (size == StepRequest.STEP_MIN) {
                sb.append("instruction");
            } else if (size == StepRequest.STEP_LINE) {
                sb.append("line");
            } else {
                sb.append("unknown");
            }
            sb.append("\n");
        }
        sb.append("\n");
        requests = erm.threadDeathRequests();
        iter = requests.iterator();
        sb.append("Thread death requests:\n");
        while (iter.hasNext()) {
            ThreadDeathRequest tdr = (ThreadDeathRequest) iter.next();
            printBasics(tdr, sb);
            sb.append("\n");
        }
        sb.append("\n");
        requests = erm.threadStartRequests();
        iter = requests.iterator();
        sb.append("Thread start requests:\n");
        while (iter.hasNext()) {
            ThreadStartRequest tsr = (ThreadStartRequest) iter.next();
            printBasics(tsr, sb);
            sb.append("\n");
        }
        sb.append("\n");
        requests = erm.vmDeathRequests();
        iter = requests.iterator();
        sb.append("VM death requests:\n");
        while (iter.hasNext()) {
            VMDeathRequest vmdr = (VMDeathRequest) iter.next();
            printBasics(vmdr, sb);
            sb.append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Prints the basic information about an event request.
     *
     * @param  er  event request.
     * @param  sb  string builder.
     */
    private static void printBasics(EventRequest er, StringBuilder sb) {
        sb.append("\tEnabled: ");
        sb.append(String.valueOf(er.isEnabled()));
        sb.append("\n\tSuspend policy: ");
        int policy = er.suspendPolicy();
        if (policy == EventRequest.SUSPEND_ALL) {
            sb.append("all");
        } else if (policy == EventRequest.SUSPEND_EVENT_THREAD) {
            sb.append("thread");
        } else if (policy == EventRequest.SUSPEND_NONE) {
            sb.append("none");
        } else {
            sb.append("unknown");
        }
    }
}
