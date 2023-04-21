package org.apache.harmony.jpda.tests.jdwp.ReferenceType;

import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;
import org.apache.harmony.jpda.tests.share.SyncDebuggee;

public class GetValues005Debuggee extends SyncDebuggee {

    static int intArrayField[];

    static GetValues005Debuggee objectArrayField[];

    static GetValues005Debuggee objectField;

    static String stringField;

    static Thread threadField;

    static ThreadGroup threadGroupField;

    static Class classField;

    static ClassLoader classLoaderField;

    public void run() {
        logWriter.println("--> Debuggee: GetValues005Debuggee: START");
        intArrayField = new int[1];
        intArrayField[0] = 999;
        objectArrayField = new GetValues005Debuggee[1];
        objectArrayField[0] = new GetValues005Debuggee();
        objectField = new GetValues005Debuggee();
        stringField = "stringField";
        threadField = new GetValues005DebuggeeThread();
        threadGroupField = new ThreadGroup("ThreadGroupName");
        classField = GetValues005Debuggee.class;
        classLoaderField = classField.getClassLoader();
        intArrayField = null;
        objectArrayField = null;
        objectField = null;
        stringField = null;
        threadField = null;
        threadGroupField = null;
        classField = null;
        classLoaderField = null;
        logWriter.println("\n--> Debuggee: GetValues005Debuggee: Before ReferenceType::GetValues command:");
        logWriter.println("--> intArrayField value = " + intArrayField);
        logWriter.println("--> objectArrayField value = " + objectArrayField);
        logWriter.println("--> objectField value = " + objectField);
        logWriter.println("--> stringField value = " + stringField);
        logWriter.println("--> threadField value = " + threadField);
        logWriter.println("--> threadGroupField value = " + threadGroupField);
        logWriter.println("--> classField value = " + classField);
        logWriter.println("--> classLoaderField value = " + classLoaderField);
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_READY);
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
        logWriter.println("--> Debuggee: GetValues005Debuggee: FINISH");
    }

    public static void main(String[] args) {
        runDebuggee(GetValues005Debuggee.class);
    }
}

class GetValues005DebuggeeThread extends Thread {

    public void myMethod() {
    }
}
