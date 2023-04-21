package jtk.project4.fleet.screen.addEditPartInventory;

import java.sql.SQLException;
import nl.coderight.jazz.Controller;
import jtk.project4.fleet.FleetModel;
import jtk.project4.fleet.domain.Parts;
import jtk.project4.fleet.event.InventoryEvent;
import jtk.project4.fleet.task.LoadInventoryTask;

public class AddEditPartInventoryController extends Controller {

    private FleetModel fleetModel;

    private AddEditPartInventoryView partinventoryView;

    public void handleEvent(InventoryEvent evt) throws SQLException {
        Parts part = fleetModel.getPart();
        switch(evt.getType()) {
            case SUBMIT:
                fleetModel.insertOrUpdatePart(part);
                System.out.print("asddd");
                executeTask(new LoadInventoryTask());
                break;
            default:
                propagateEvent(evt);
                break;
        }
    }

    @Override
    public void execute() {
        fleetModel = (FleetModel) getModel();
        partinventoryView = new AddEditPartInventoryView();
        setView(partinventoryView);
        showView();
    }
}
