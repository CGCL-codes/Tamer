package com.ibm.tuningfork.core.sharing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.AbstractList;
import java.util.ArrayList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import com.ibm.tuningfork.infra.util.REF;

/**
 * A TuningFork sharing session represents an ongoing relationship with another running TuningFork process. When Alice
 * shares her first figure with Bob, sharing session objects are created in each TF. If Alice subsequently shares
 * another figure, the same session objects are reused. Likewise if Bob then shares a figure back to Alice. When the
 * last figure ceases to be shared between them, the session objects should be destroyed.
 * 
 * These objects also allow ConvertedObjectTrackers to be shared among all figures shared in the same session.
 * 
 * Also, these provide a way to shortcut entering hostnames and ports into the sharing dialog when sharing subsequent
 * figures in a session.
 * 
 * I expect that this will allow trace file fetching, but this is not done yet.
 * 
 * 
 */
public class ActiveSharingSession {

    private final SharingDestination otherEnd;

    private final ConvertedObjectTracker tracker;

    private AbstractList<SharingConduit> requestSessions;

    public static ActiveSharingSession forInitiatingOrResponding(BufferedReader in, PrintWriter out, InetAddress otherAddress, boolean amIForInitiating) throws Exception {
        ActiveSharingSession session;
        if (amIForInitiating) {
            sendMyInfo(out);
            session = createOrReuse(in, otherAddress, amIForInitiating);
        } else {
            session = createOrReuse(in, otherAddress, amIForInitiating);
            try {
                sendMyInfo(out);
            } catch (Exception e) {
                session.close();
            }
        }
        return session;
    }

    private static ActiveSharingSession createOrReuse(BufferedReader in, InetAddress otherAddress, boolean amIForInitiating) throws Exception {
        final SharingDestination otherEnd = SharingDestination.read(in);
        ActiveSharingSession existing = SessionRegistry.get(otherEnd.getPID());
        return existing != null ? existing : new ActiveSharingSession(amIForInitiating, otherEnd);
    }

    private ActiveSharingSession(boolean amIForInitiating, SharingDestination otherEnd) throws Exception {
        this.otherEnd = otherEnd;
        if (amIForInitiating || askIfSharingOK(otherEnd.toString())) ; else throw new Exception("Sharing request refused");
        requestSessions = new ArrayList<SharingConduit>();
        tracker = new ConvertedObjectTracker(amIForInitiating);
        SessionRegistry.put(this);
    }

    private static void sendMyInfo(PrintWriter out) throws IOException {
        SharingDestination.Me.write(out);
    }

    ActiveSharingSession addRequestSession(SharingConduit srs) {
        requestSessions.add(srs);
        return this;
    }

    public ConvertedObjectTracker getTracker() {
        return isAlive() ? tracker : null;
    }

    public boolean isAlive() {
        final ArrayList<SharingConduit> liveSessions = new ArrayList<SharingConduit>();
        for (SharingConduit s : requestSessions) if (s.isAlive()) liveSessions.add(s);
        requestSessions = liveSessions;
        return !requestSessions.isEmpty();
    }

    public void close() {
        for (SharingConduit s : requestSessions) s.close();
        requestSessions = new ArrayList<SharingConduit>();
    }

    private static boolean askIfSharingOK(final String whoIsAsking) {
        final Display display = PlatformUI.getWorkbench().getDisplay();
        final REF<Boolean> answerHolder = new REF<Boolean>();
        display.syncExec(new Runnable() {

            public void run() {
                answerHolder.ref = MessageDialog.openConfirm(display.getShells()[0], "\"" + whoIsAsking + "\" wants to share a figure with you", "Proceed?");
            }
        });
        return answerHolder.ref;
    }

    public ExpressionTreeBasedConverter newExpressionTreeBasedConverter(SharingConduit s) {
        return new ExpressionTreeBasedConverter(getTracker(), s);
    }

    public boolean amITheInitiator() {
        return getTracker().amITheInitiator();
    }

    public SharingDestination getOtherEnd() {
        return otherEnd;
    }
}
