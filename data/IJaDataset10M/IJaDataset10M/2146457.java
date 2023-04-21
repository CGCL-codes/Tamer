package com.deimos;

import org.vast.ows.sps.StatusReport;
import org.vast.ows.sps.StatusReport.RequestStatus;
import org.vast.ows.sps.StatusReport.TaskStatus;
import com.deimos.dataBase.Event;
import com.deimos.sps.GetFeasibility.Operation;
import com.spotimage.eosps.wsn.EOSPSNotificationSystem;
import com.spotimage.wsn.InMemorySubscriptionDB;

public class Notification extends EOSPSNotificationSystem {

    private static Notification INSTANCE = null;

    public static String address = null;

    private Notification() {
        super(address, new InMemorySubscriptionDB());
    }

    private static synchronized void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Notification();
        }
    }

    public static Notification getInstance() {
        if (INSTANCE == null && address != null) createInstance();
        return INSTANCE;
    }

    public static void notifyReport(StatusReport report, Operation op) {
        if (op == Operation.SUBMITTED && report.getRequestStatus() == RequestStatus.Accepted && Notification.getInstance() != null) {
            Notification.getInstance().notifyTaskSubmitted(report);
        } else if (op == Operation.SUBMITTED && report.getRequestStatus() == RequestStatus.Rejected && Notification.getInstance() != null) Notification.getInstance().notifyRequestRejected(report); else if (op == Operation.RESERVED && report.getRequestStatus() == RequestStatus.Accepted && Notification.getInstance() != null) Notification.getInstance().notifyTaskReservation(report); else if (op == Operation.RESERVED && report.getRequestStatus() == RequestStatus.Rejected && Notification.getInstance() != null) Notification.getInstance().notifyRequestRejected(report); else if (op == Operation.CONFIRMED && report.getRequestStatus() == RequestStatus.Accepted && Notification.getInstance() != null) Notification.getInstance().notifyTaskConfirmation(report); else if (op == Operation.CONFIRMED && report.getRequestStatus() == RequestStatus.Rejected && Notification.getInstance() != null) Notification.getInstance().notifyRequestRejected(report); else if (op == Operation.UPDATED && report.getRequestStatus() == RequestStatus.Accepted && Notification.getInstance() != null) Notification.getInstance().notifyTaskUpdated(report); else if (op == Operation.UPDATED && report.getRequestStatus() == RequestStatus.Rejected && Notification.getInstance() != null) Notification.getInstance().notifyRequestRejected(report); else if (report.getTaskStatus() == TaskStatus.Completed && Notification.getInstance() != null) Notification.getInstance().notifyTaskCompleted(report); else if (report.getTaskStatus() == TaskStatus.Cancelled && Notification.getInstance() != null) Notification.getInstance().notifyTaskCancelled(report); else if (report.getTaskStatus() == TaskStatus.Failed && Notification.getInstance() != null) Notification.getInstance().notifyTaskFailed(report); else if (report.getTaskStatus() == TaskStatus.Expired && Notification.getInstance() != null) Notification.getInstance().notifyReservationExpired(report); else if (report.getRequestStatus() == RequestStatus.Accepted && Notification.getInstance() != null) Notification.getInstance().notifyRequestAccepted(report); else if (report.getRequestStatus() == RequestStatus.Rejected && Notification.getInstance() != null) Notification.getInstance().notifyRequestRejected(report);
    }

    public static void notifyReport(Event event) {
        Notification.notifyReport(UtilsDeimos.generateReport(event), event.getOperation());
    }
}
