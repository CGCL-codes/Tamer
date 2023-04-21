package com.ammatsun.checkstyle4nb.actions;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import org.openide.cookies.LineCookie;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.text.Line;
import org.openide.windows.OutputEvent;
import org.openide.windows.OutputListener;
import org.openide.windows.OutputWriter;
import java.io.IOException;

/**
 * The listener for Netbeans to capture any audit events and send
 * it to output tab. Also redirects the user to the corresponding
 * file that generated the event.
 *
 * @version    1.0, 2006/Jul/12
 * @author     Andrea Matsunaga
 */
public class NbListener implements AuditListener, OutputListener {

    /** Writer (output tab) to send this to. */
    private OutputWriter myOutputWriter = null;

    /** File currently being processed. */
    private int myCurrId = 0;

    /** List of nodes selected by the user. */
    private Node[] myActivatedNodes = null;

    /**
     * Creates a new instance of NbListener.
     * @param outputWriter the writer to output any errors to
     * @param activatedNodes list of nodes that were selected when action was
     *        invoked
     */
    public NbListener(OutputWriter outputWriter, Node[] activatedNodes) {
        this.myOutputWriter = outputWriter;
        this.myActivatedNodes = activatedNodes;
    }

    /**
     * Outputs the event details into Netbeans output window.
     * @param auditEvent the event details
     */
    public void addError(AuditEvent auditEvent) {
        try {
            if (0 == auditEvent.getLine()) {
                myOutputWriter.println(auditEventToString(auditEvent));
            } else {
                myOutputWriter.println(auditEventToString(auditEvent), this);
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }

    /**
     * Notifies that an exception happened while performing audit.
     * @param auditEvent the event details
     * @param throwable the exception details
     */
    public void addException(AuditEvent auditEvent, Throwable throwable) {
        myOutputWriter.println("Error occurred. Audit: " + auditEvent.toString() + " Exception: " + throwable);
    }

    /**
     * Sets the ID of the file currently being processed.
     * @param currId the ID of the file being processed (the index in the list
     *        of active nodes)
     */
    public void setCurrId(int currId) {
        this.myCurrId = currId;
    }

    /**
     * Method to get a printable string from an audit event.
     * @param auditEvent event to display
     * @return String to display
     */
    public String auditEventToString(AuditEvent auditEvent) {
        StringBuffer toDisplay = new StringBuffer();
        toDisplay.append("[" + myCurrId + "] ");
        toDisplay.append(auditEvent.getFileName());
        toDisplay.append("[");
        toDisplay.append(auditEvent.getLine());
        toDisplay.append("]: ");
        toDisplay.append(auditEvent.getMessage());
        return toDisplay.toString();
    }

    /**
     * Notifies that the audit is finished.
     * @param auditEvent the error that occurred
     */
    public void auditFinished(AuditEvent auditEvent) {
        myOutputWriter.println("Checkstyle audit complete.");
    }

    /**
     * Notifies that the audit is about to start.
     * @param auditEvent the error that occurred.
     */
    public void auditStarted(AuditEvent auditEvent) {
        myOutputWriter.println("Starting checkstyle audit.");
    }

    /**
     * Notifies that audit is finished on a specific file.
     * @param auditEvent the error that occurred.
     */
    public void fileFinished(AuditEvent auditEvent) {
        System.out.println("Completed file " + auditEvent.getFileName());
    }

    /**
     * Notifies that audit is about to start on a specific file.
     * @param auditEvent the error that occurred.
     */
    public void fileStarted(AuditEvent auditEvent) {
        System.out.println("Starting file " + auditEvent.getFileName());
    }

    /**
     * Called when a line is cleared from the buffer of known lines.
     * @param event the event describing the line
     */
    public void outputLineCleared(OutputEvent event) {
    }

    /**
     * Called when some sort of action is performed on a line. In this case,
     * will redirect the focus to the file and line that generated the
     * checkstyle event.
     * @param event the event describing the line
     */
    public void outputLineAction(OutputEvent event) {
        String fullLine = event.getLine();
        if (null == fullLine) {
            return;
        }
        String[] parts = fullLine.split("\\[|\\]");
        if ((null == parts) || (4 > parts.length) || (null == parts[1]) || (null == parts[3]) || (0 == parts[1].length()) || (0 == parts[3].length())) {
            return;
        }
        int fileId = 0;
        int lineNum = 0;
        try {
            fileId = Integer.parseInt(parts[1]);
            lineNum = Integer.parseInt(parts[3]);
        } catch (NumberFormatException e) {
            System.err.println("Failed to parse file ID or line number.");
        }
        DataObject dataObj = myActivatedNodes[fileId].getLookup().lookup(DataObject.class);
        LineCookie cookie = dataObj.getCookie(LineCookie.class);
        if (null == cookie) {
            return;
        }
        Line line = cookie.getLineSet().getOriginal(lineNum - 1);
        line.show(Line.SHOW_GOTO);
    }

    /**
     * Called when a line is selected. Nothing done in this case.
     * @param event the event describing the line
     */
    public void outputLineSelected(OutputEvent event) {
    }
}
