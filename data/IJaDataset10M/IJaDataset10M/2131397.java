package jtk.project4.fleet.task;

import jtk.project4.fleet.domain.Vendor;
import jtk.project4.fleet.event.VendorEvent;
import jtk.project4.fleet.event.VendorEvent.VendorEventType;
import jtk.project4.fleet.service.DatabaseService;
import java.util.List;
import nl.coderight.jazz.Task;
import nl.coderight.jazz.event.MessageEvent;
import nl.coderight.jazz.event.ProgressEvent;
import nl.coderight.jazz.event.MessageEvent.MessageEventType;
import nl.coderight.jazz.event.ProgressEvent.ProgressEventType;

public class LoadVendorTask extends Task<List<Vendor>> {

    @Override
    public List<Vendor> process() throws Exception {
        return DatabaseService.getInstance().getVendors();
    }

    @Override
    protected void onStart() {
        ProgressEvent progressEvent = new ProgressEvent(ProgressEventType.START);
        progressEvent.setMessage("Loading Contacts...");
        progressEvent.setIndeterminate(true);
        postEvent(progressEvent);
    }

    @Override
    protected void onFinish() {
        postEvent(new ProgressEvent(ProgressEventType.STOP));
        VendorEvent vendorEvent = new VendorEvent(VendorEventType.LOAD_SUCCESS);
        vendorEvent.setVendors(getResult());
        postEvent(vendorEvent);
    }

    @Override
    protected void onError() {
        MessageEvent error = new MessageEvent(MessageEventType.ERROR);
        error.setMessage(getError().getMessage());
        postEvent(error);
    }
}
