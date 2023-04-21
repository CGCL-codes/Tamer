package action;

import java.text.SimpleDateFormat;
import java.util.List;
import model.Vehicle;
import model.VehicleStatus;
import service.ScheduleService;
import service.VehicleService;
import util.BeanUtils;
import util.CollectionUtils;
import com.opensymphony.xwork2.ActionSupport;
import exception.ScheduleException;

public class VehicleAction extends ActionSupport implements CommonAction {

    private static final long serialVersionUID = 1L;

    private static final SimpleDateFormat FORMAT_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String[] VEHICLE_PROPERTIES_UPDATABLE = { "licensePlate", "brand", "status" };

    @Override
    public String setRedirectAction(String toAction) {
        redirectAction = toAction;
        return REDIRECT_ACTION;
    }

    @Override
    public String getRedirectAction() {
        return redirectAction;
    }

    @Override
    public String setCustomPage(String page) {
        customPage = page;
        return CUSTOM_PAGE;
    }

    @Override
    public String getCustomPage() {
        return customPage;
    }

    /**
	 * get vehicles
	 */
    public String listVehiclesAction() {
        if (vehicle == null) {
            vehicle = Vehicle.newInstance();
            vehicle.setStatus(VehicleStatus.ALL);
            vehicle.setLicensePlate("");
        }
        vechiles = vehicleService.getVehicles(vehicle.getStatus(), 0);
        return SUCCESS;
    }

    public String editVehicleAction() {
        vehicle = vehicleService.getVehicle(id);
        return SUCCESS;
    }

    public String updateVehicleAction() {
        try {
            Vehicle veh = vehicleService.getVehicle(vehicle.getEncryptedId());
            BeanUtils.copyTheseProperties(vehicle, veh, VEHICLE_PROPERTIES_UPDATABLE);
            try {
                veh.setBiddingStartOn(FORMAT_DATETIME.parse(vehicle.getBiddingStartOnString()));
            } catch (Exception e) {
                veh.setBiddingStartOn(null);
            }
            try {
                veh.setBiddingStopOn(FORMAT_DATETIME.parse(vehicle.getBiddingStopOnString()));
            } catch (Exception e) {
                veh.setBiddingStopOn(null);
            }
            scheduleService.removeAllSchedule(veh);
            if (veh.getStatus() == VehicleStatus.SCHEDULED) {
                if (veh.getBiddingStartOn() != null) scheduleService.addStartBiddingSchedule(veh);
                if (veh.getBiddingStopOn() != null) scheduleService.addStopBiddingSchedule(veh);
            }
            vehicleService.update(veh);
            return setRedirectAction("vehicle-listVehicles");
        } catch (ScheduleException e) {
            return ERROR;
        }
    }

    public String listBiddingVehiclesAction() {
        vechiles = vehicleService.getVehicles(VehicleStatus.BIDDING, 0);
        return SUCCESS;
    }

    public String viewVehicleAction() {
        vehicle = vehicleService.getVehicle(id);
        return SUCCESS;
    }

    private String redirectAction;

    private String customPage;

    private VehicleService vehicleService;

    private ScheduleService scheduleService;

    private List<Vehicle> vechiles;

    private Vehicle vehicle;

    private String id;

    public void setScheduleService(ScheduleService val) {
        scheduleService = val;
    }

    public void setVehicle(Vehicle val) {
        vehicle = val;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVehicleService(VehicleService val) {
        vehicleService = val;
    }

    public List<Vehicle> getVehicles() {
        if (vechiles == null) {
            vechiles = CollectionUtils.newArrayList(0);
        }
        return vechiles;
    }
}
