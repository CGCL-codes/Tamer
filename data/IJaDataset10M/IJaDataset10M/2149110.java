package org.javagroup.process;

import java.io.*;
import java.net.*;
import java.awt.Frame;
import org.javagroup.util.*;
import org.apache.log4j.*;

/**
 * SecurityManager facilitating process identification.
 * @author Luke Gorrie
 */
public class ProcessSecurityManager extends SecurityManager {

    protected ProcessManager _manager;

    /** Log4J category */
    private static Category log = Category.getInstance(ProcessSecurityManager.class.getName());

    public ProcessSecurityManager(ProcessManager manager) {
        _manager = manager;
    }

    ProcessSecurityManager() {
        super();
    }

    void setProcessManager(ProcessManager manager) {
        _manager = manager;
    }

    public JProcess getCurrentProcess() {
        ThreadGroup group = getThreadGroup();
        Thread curThread = Thread.currentThread();
        JProcess match = null;
        match = _manager.getProcessFor(group);
        if (match != null) {
            return match;
        }
        Class[] class_context = getClassContext();
        for (int i = 0; i < class_context.length; i++) {
            ClassLoader loader = class_context[i].getClassLoader();
            if (loader != null) {
                match = _manager.getProcessFor(loader);
                if (match != null) return match;
            }
        }
        return null;
    }

    public void checkCreateClassLoader() {
    }

    public void checkAccess(Thread g) {
    }

    public void checkAccess(ThreadGroup g) {
    }

    public void checkExit(int status) {
        JProcess process = getCurrentProcess();
        if (process != null) {
            log.debug(process.getPid() + ":Security.checkExit");
            _manager.kill(process.getPid());
        }
    }

    public void checkExec(String cmd) {
    }

    public void checkLink(String lib) {
    }

    public void checkRead(FileDescriptor fd) {
    }

    public void checkRead(String file) {
    }

    public void checkRead(String file, Object context) {
    }

    public void checkWrite(FileDescriptor fd) {
    }

    public void checkWrite(String file) {
    }

    public void checkDelete(String file) {
    }

    public void checkConnect(String host, int port) {
    }

    public void checkConnect(String host, int port, Object context) {
    }

    public void checkListen(int port) {
    }

    public void checkAccept(String host, int port) {
    }

    public void checkMulticast(InetAddress maddr) {
    }

    public void checkMulticast(InetAddress maddr, byte ttl) {
    }

    public void checkPropertiesAccess() {
    }

    public void checkPropertyAccess(String key) {
    }

    public void checkPropertyAccess(String key, String def) {
    }

    public boolean checkTopLevelWindow(Object obj) {
        Frame window = null;
        if (obj instanceof Frame) window = (Frame) obj;
        JProcess process = _manager.getCurrentProcess();
        if (process != null) log.debug(process.getPid() + ":Security.checkTopLevelWindow");
        if ((process != null) && (window != null)) {
            process.registerAndBindToResource(new WindowResource(window));
        } else if ((process == null) && (window != null)) {
            log.error("Security.checkTopLevelWindow: Window not attached to a process:" + window.getTitle() + ":" + window.getName() + ":" + Thread.currentThread().getName());
            System.err.println("Security.checkTopLevelWindow: Window not attached to a process:" + window.getTitle() + ":" + window.getName() + ":" + Thread.currentThread().getName());
        }
        return true;
    }

    public void checkPrintJobAccess() {
    }

    public void checkSystemClipboardAccess() {
    }

    public void checkAwtEventQueueAccess() {
    }

    public void checkPackageAccess(String pkg) {
    }

    public void checkPackageDefinition(String pkg) {
    }

    public void checkSetFactory() {
    }

    public void checkMemberAccess(Class clazz, int which) {
    }

    public void checkSecurityAccess(String provider) {
    }

    public void checkPermission(java.security.Permission perm) {
    }

    public void checkPermission(java.security.Permission perm, Object context) {
    }
}
