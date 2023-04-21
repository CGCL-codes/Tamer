package br.ufmg.ubicomp.decs.client.action;

import java.util.Map;
import br.ufmg.ubicomp.decs.client.DECSContext;
import br.ufmg.ubicomp.decs.client.action.emergency.AbstractEmergencyAction;
import br.ufmg.ubicomp.decs.client.core.DECSConfig;
import br.ufmg.ubicomp.decs.client.core.DECSConstants;
import br.ufmg.ubicomp.decs.client.eventservice.xml.EntityProperties;
import br.ufmg.ubicomp.decs.client.utils.DebugUtils;
import br.ufmg.ubicomp.decs.client.xml.ClientXMLUtils;

public class UpdateMarkerLocationAction extends AbstractEmergencyAction {

    private static final String PROPERTY_DIST = "dist";

    private Map<String, String> params;

    private String userName;

    public UpdateMarkerLocationAction(DECSContext context, Map<String, String> params) {
        super(context, null, null, null);
        this.params = params;
    }

    private void updateLocation() {
        DebugUtils.debug(4, "UpdateMarkerLocationAction.updateLocation");
        userName = params.get("USER");
        context.updateBytesSent(userName, params.get("lat"), params.get("lon"));
        context.updateUserBytesSent(userName, params.get("lat"), params.get("lon"));
        context.getProxy().getUserService().updateMarkerLocation(userName, params.get("lat"), params.get("lon"), this);
    }

    @Override
    public String getActionName() {
        return "UpdateMarkerLocationAction";
    }

    @Override
    public String execute() {
        super.execute();
        DebugUtils.debug(4, "UpdateMarkerLocationAction.execute");
        updateLocation();
        return null;
    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess("UpdateMarkerLocationAction", result);
        DebugUtils.debug(4, "UpdateMarkerLocationAction.onSuccess");
        context.updateUserBytesReceived(userName, result);
        EntityProperties user = null;
        if (DECSConfig.DECS_SIMULATION_TYPE.equals(DECSConfig.DECS_SIMULATION_TYPE_EMERGENCY)) {
            user = ClientXMLUtils.getEntityFromXML(result, DECSConstants.UBI_CONTEXT_USER);
        } else if (DECSConfig.DECS_SIMULATION_TYPE.equals(DECSConfig.DECS_SIMULATION_TYPE_TOURISM)) {
            user = ClientXMLUtils.getEntityFromXML(result, DECSConstants.DROID_GUIDE_USER);
        }
        EntityProperties info = ClientXMLUtils.getEntityFromXML(result, "info");
        if (context.isStopAutoMode()) {
            context.updateStatsValue("User " + params.get("USER") + "change of loc.", 1);
            context.updateStatsValue("User change of loc.", 1);
        }
    }
}
