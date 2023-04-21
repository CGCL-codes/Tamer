package org.scohen.juploadr.upload;

import java.util.ArrayList;
import java.util.List;
import org.scohen.juploadr.event.UploadCompleteEvent;
import org.scohen.juploadr.event.UploadEvent;
import org.scohen.juploadr.event.UploadStatusMonitor;

public class MultiMonitorFacade implements UploadStatusMonitor {

    private List<UploadStatusMonitor> monitors;

    public MultiMonitorFacade() {
        monitors = new ArrayList<UploadStatusMonitor>();
    }

    public void addMonitor(UploadStatusMonitor monitor) {
        monitors.add(monitor);
    }

    public void uploadStarted(UploadEvent e) {
        for (int i = 0; i < monitors.size(); i++) {
            UploadStatusMonitor monitor = (UploadStatusMonitor) monitors.get(i);
            monitor.uploadStarted(e);
        }
    }

    public void uploadStatusChanged(UploadEvent e) {
        for (int i = 0; i < monitors.size(); i++) {
            UploadStatusMonitor monitor = (UploadStatusMonitor) monitors.get(i);
            monitor.uploadStatusChanged(e);
        }
    }

    public void uploadFinished(UploadCompleteEvent e) {
        for (int i = 0; i < monitors.size(); i++) {
            UploadStatusMonitor monitor = (UploadStatusMonitor) monitors.get(i);
            monitor.uploadFinished(e);
        }
    }

    public void allUploadsComplete() {
        for (int i = 0; i < monitors.size(); i++) {
            UploadStatusMonitor monitor = (UploadStatusMonitor) monitors.get(i);
            monitor.allUploadsComplete();
        }
    }

    public void uploadFailed(UploadEvent e) {
        for (int i = 0; i < monitors.size(); i++) {
            UploadStatusMonitor monitor = (UploadStatusMonitor) monitors.get(i);
            monitor.uploadFailed(e);
        }
    }

    public void uploadCancelled() {
        for (int i = 0; i < monitors.size(); i++) {
            UploadStatusMonitor monitor = (UploadStatusMonitor) monitors.get(i);
            monitor.uploadCancelled();
        }
    }
}
