package jtk.project4.fleet.task;

import java.util.List;
import jtk.project4.fleet.domain.Parts;
import jtk.project4.fleet.domain.Vendor;
import jtk.project4.fleet.event.InventoryEvent;
import jtk.project4.fleet.event.VendorEvent;
import jtk.project4.fleet.event.InventoryEvent.InventoryEventType;
import jtk.project4.fleet.event.VendorEvent.VendorEventType;
import jtk.project4.fleet.service.DatabaseService;
import nl.coderight.jazz.Task;
import nl.coderight.jazz.event.MessageEvent;
import nl.coderight.jazz.event.ProgressEvent;
import nl.coderight.jazz.event.MessageEvent.MessageEventType;
import nl.coderight.jazz.event.ProgressEvent.ProgressEventType;

public class LoadInventoryTask extends Task<List<Parts>> {

    public List<Parts> process() throws Exception {
        return DatabaseService.getInstance().getParts();
    }

    protected void onStart() {
        ProgressEvent progressEvent = new ProgressEvent(ProgressEventType.START);
        progressEvent.setMessage("Loading contacts...");
        progressEvent.setIndeterminate(true);
        postEvent(progressEvent);
    }

    protected void onFinish() {
        postEvent(new ProgressEvent(ProgressEventType.STOP));
        InventoryEvent inventoryEvent = new InventoryEvent(InventoryEventType.LOAD_SUCCESS);
        inventoryEvent.setParts(getResult());
        postEvent(inventoryEvent);
    }

    protected void onError() {
        MessageEvent error = new MessageEvent(MessageEventType.ERROR);
        error.setMessage(getError().getMessage());
        postEvent(error);
    }
}
