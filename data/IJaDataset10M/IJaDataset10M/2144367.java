package br.ufmg.ubicomp.decs.client.core;

import java.util.HashMap;
import java.util.Map;
import br.ufmg.ubicomp.decs.client.core.enums.EmergencyTopics;
import br.ufmg.ubicomp.decs.client.core.service.CoreEventService;
import br.ufmg.ubicomp.decs.client.eventservice.service.EventService;
import br.ufmg.ubicomp.decs.client.service.EmergencyService;
import br.ufmg.ubicomp.decs.server.eventservice.entity.EventMessage;
import br.ufmg.ubicomp.decs.server.eventservice.manager.EventManager;

public class EmergencyServiceMessages {

    public static Map<String, String> getCreateNewEmergencyParamsObject(double lat, double lon, String type, String severity, String description) {
        String topic = EmergencyService.TOPIC_NEW_EMERGENCY;
        Map<String, String> params = new HashMap<String, String>();
        params.put(EventService.PARAM_PUBLISHER, "system");
        params.put(EventService.PARAM_EMERGENCY_TYPE, type);
        params.put(EventService.PARAM_TOPIC, topic);
        params.put(EventService.PARAM_DESCRIPTION, description);
        params.put(EventService.PARAM_LAT, String.valueOf(lat));
        params.put(EventService.PARAM_LON, String.valueOf(lon));
        params.put(EventService.PARAM_SEVERITY, severity);
        return params;
    }

    public static Map<String, String> getCreateNewEmergencyParamsObject(Map<String, String> params2) {
        String topic = EmergencyService.TOPIC_NEW_EMERGENCY;
        Map<String, String> params = new HashMap<String, String>();
        params.putAll(params2);
        params.put(EventService.PARAM_TOPIC, topic);
        return params;
    }

    public static Map<String, String> getCreateNewEmergencyParamsObject(EventMessage em) {
        Map<String, String> params = getCreateNewEmergencyParamsObject(em.getLat(), em.getLon(), em.get("emergencyType"), em.get("severity"), em.get("message"));
        return params;
    }
}
