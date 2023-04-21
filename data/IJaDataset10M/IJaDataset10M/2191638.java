package eu.xtreemos.xconsole.command;

import java.util.ArrayList;
import eu.xtreemos.xconsole.blocks.XConsoleHandle;
import org.slasoi.adhoc.workload.AdhocJobManager;

/**
 * @author gregor.pipan@xlab.si
 *
 */
public class XConAdhocJobManager {

    public String servicename = "org.slasoi.adhoc.workload.AdhocJobManager";

    public ArrayList<XConsoleHandle> handles = new ArrayList<XConsoleHandle>();

    public void startAll() {
        System.out.println("XCONSOLECMD startAll called startAll");
        try {
            AdhocJobManager.startAll();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public XConsoleHandle registerstartAll() {
        return new XConsoleHandle(this, "startAll", "startAll", "", "");
    }

    public void startAllInstances(String __agent) {
        System.out.println("XCONSOLECMD startAllInstances called startAllInstances");
        try {
            AdhocJobManager.startAllInstances(__agent);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public XConsoleHandle registerstartAllInstances() {
        return new XConsoleHandle(this, "startAllInstances", "startAllInstances", "java.lang.String", "");
    }

    public void startInstance(String __agent, String __instance) {
        System.out.println("XCONSOLECMD startInstance called startInstance");
        try {
            AdhocJobManager.startInstance(__agent, __instance);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public XConsoleHandle registerstartInstance() {
        return new XConsoleHandle(this, "startInstance", "startInstance", "java.lang.String, java.lang.String", "");
    }

    public void pauseAll() {
        System.out.println("XCONSOLECMD pauseAll called pauseAll");
        try {
            AdhocJobManager.pauseAll();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public XConsoleHandle registerpauseAll() {
        return new XConsoleHandle(this, "pauseAll", "pauseAll", "", "");
    }

    public void pauseAllInstances(String __i) {
        System.out.println("XCONSOLECMD pauseAllInstances called pauseAllInstances");
        try {
            AdhocJobManager.pauseAllInstances(__i);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public XConsoleHandle registerpauseAllInstances() {
        return new XConsoleHandle(this, "pauseAllInstances", "pauseAllInstances", "java.lang.String", "");
    }

    public void pauseInstance(String __agent, String __instance) {
        System.out.println("XCONSOLECMD pauseInstance called pauseInstance");
        try {
            AdhocJobManager.pauseInstance(__agent, __instance);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public XConsoleHandle registerpauseInstance() {
        return new XConsoleHandle(this, "pauseInstance", "pauseInstance", "java.lang.String, java.lang.String", "");
    }

    public void resumeAll() {
        System.out.println("XCONSOLECMD resumeAll called resumeAll");
        try {
            AdhocJobManager.resumeAll();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public XConsoleHandle registerresumeAll() {
        return new XConsoleHandle(this, "resumeAll", "resumeAll", "", "");
    }

    public void resumeAllInstances(String __i) {
        System.out.println("XCONSOLECMD resumeAllInstances called resumeAllInstances");
        try {
            AdhocJobManager.resumeAllInstances(__i);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public XConsoleHandle registerresumeAllInstances() {
        return new XConsoleHandle(this, "resumeAllInstances", "resumeAllInstances", "java.lang.String", "");
    }

    public void resumeInstance(String __agent, String __instance) {
        System.out.println("XCONSOLECMD resumeInstance called resumeInstance");
        try {
            AdhocJobManager.resumeInstance(__agent, __instance);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public XConsoleHandle registerresumeInstance() {
        return new XConsoleHandle(this, "resumeInstance", "resumeInstance", "java.lang.String, java.lang.String", "");
    }

    public void stopAll() {
        System.out.println("XCONSOLECMD stopAll called stopAll");
        try {
            AdhocJobManager.stopAll();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public XConsoleHandle registerstopAll() {
        return new XConsoleHandle(this, "stopAll", "stopAll", "", "");
    }

    public void stopAllInstances(String __agent) {
        System.out.println("XCONSOLECMD stopAllInstances called stopAllInstances");
        try {
            AdhocJobManager.stopAllInstances(__agent);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public XConsoleHandle registerstopAllInstances() {
        return new XConsoleHandle(this, "stopAllInstances", "stopAllInstances", "java.lang.String", "");
    }

    public void stopInstance(String __agent, String __instance) {
        System.out.println("XCONSOLECMD stopInstance called stopInstance");
        try {
            AdhocJobManager.stopInstance(__agent, __instance);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public XConsoleHandle registerstopInstance() {
        return new XConsoleHandle(this, "stopInstance", "stopInstance", "java.lang.String, java.lang.String", "");
    }

    public void quit() {
        System.out.println("XCONSOLECMD quit called quit");
        try {
            AdhocJobManager.quit();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public XConsoleHandle registerquit() {
        return new XConsoleHandle(this, "quit", "quit", "", "");
    }

    public ArrayList<XConsoleHandle> register() {
        this.handles.add(registerstartAll());
        this.handles.add(registerstartAllInstances());
        this.handles.add(registerstartInstance());
        this.handles.add(registerstopAll());
        this.handles.add(registerstopAllInstances());
        this.handles.add(registerstopInstance());
        this.handles.add(registerpauseAll());
        this.handles.add(registerpauseAllInstances());
        this.handles.add(registerpauseInstance());
        this.handles.add(registerresumeAll());
        this.handles.add(registerresumeAllInstances());
        this.handles.add(registerresumeInstance());
        this.handles.add(registerquit());
        return this.handles;
    }
}
