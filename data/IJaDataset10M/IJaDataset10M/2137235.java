package net.kortsoft.gameportlet.model.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import org.springframework.beans.factory.annotation.Configurable;
import net.kortsoft.gameportlet.model.GameInvoker;
import net.kortsoft.gameportlet.model.GameType;
import net.kortsoft.gameportlet.model.Room;

public class PortletGameInvoker implements GameInvoker {

    private static final String PORTLET_NAME_SETTING = "portletName";

    private static final String ACTION_NAME_SETTING = "action";

    private String portletName;

    private PortletUrlFactory portletUrlFactory;

    private PortletRequest portletRequest;

    private Map<String, String> params;

    public PortletGameInvoker(GameType gameType, PortletRequest portletRequest, PortletUrlFactory portletUrlFactory) {
        super();
        this.portletRequest = portletRequest;
        this.portletName = gameType.getSettings().get(PORTLET_NAME_SETTING);
        this.portletUrlFactory = portletUrlFactory;
        this.params = new HashMap<String, String>(gameType.getSettings());
        this.params.remove(PORTLET_NAME_SETTING);
    }

    @Override
    public String getGameUrl(Room room) {
        PortletURL gamePortletUrl = portletUrlFactory.createUrlTo(portletName, portletRequest);
        gamePortletUrl.setParameter("roomId", room.getId().toString());
        for (Entry<String, String> paramEntry : params.entrySet()) gamePortletUrl.setParameter(paramEntry.getKey(), paramEntry.getValue());
        return gamePortletUrl.toString();
    }

    public static Map<String, String> createSettings(String portletName, String actionName) {
        Map<String, String> settings = new HashMap<String, String>();
        settings.put(PORTLET_NAME_SETTING, portletName);
        settings.put(ACTION_NAME_SETTING, actionName);
        return settings;
    }
}
