package br.ufmg.ubicomp.decs.client.action.emergency;

import java.util.Calendar;
import br.ufmg.ubicomp.decs.client.DECSContext;
import br.ufmg.ubicomp.decs.client.eventservice.service.EventService;
import br.ufmg.ubicomp.decs.client.eventservice.xml.EntityProperties;
import br.ufmg.ubicomp.decs.client.service.EmergencyService;
import br.ufmg.ubicomp.decs.client.utils.DebugUtils;

public class UpdateEmergencyAtLandmarkAction extends AbstractEmergencyAction {

    public UpdateEmergencyAtLandmarkAction(DECSContext context, EntityProperties emergency, EntityProperties user, EntityProperties landmark) {
        super(context, emergency, user, landmark);
    }

    @Override
    public String getActionName() {
        return "UpdateEmergencyAtLandmarkAction";
    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess(getActionName(), result);
        context.updateUserBytesReceived(getUser().getName(), result);
        DebugUtils.debug(4, "UpdateEmergencyAtLandmarkAction.onSuccess");
    }

    @Override
    public String execute() {
        super.execute();
        DebugUtils.debug(4, "UpdateEmergencyAtLandmarkAction.execute");
        context.updateBytesSent(getEmergency().getName(), EmergencyService.STATE_LRE, getLandmark().getValue(EventService.PARAM_LAT), getLandmark().getValue(EventService.PARAM_LON), "Arrived at Landmark");
        context.updateUserBytesSent(getUser().getName(), getEmergency().getName(), EmergencyService.STATE_LRE, getLandmark().getValue(EventService.PARAM_LAT), getLandmark().getValue(EventService.PARAM_LON), "Arrived at Landmark");
        context.getProxy().getEmergencyService().updateEmergency(getEmergency().getName(), EmergencyService.STATE_LRE, getLandmark().getValue("lat"), getLandmark().getValue("lon"), "Arrived at Landmark", this);
        DebugUtils.debug(1, System.currentTimeMillis() + ": Process completed!");
        return null;
    }
}
