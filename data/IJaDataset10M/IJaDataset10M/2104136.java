package iwork.apps.media.uigen;

import iwork.icrafter.uigen.*;
import iwork.icrafter.im.*;
import java.util.*;
import iwork.icrafter.system.*;
import iwork.icrafter.remote.*;
import iwork.eheap2.*;
import javax.swing.*;
import java.awt.*;

public class RealTimeMediaPlayerUIGenerator implements ServiceUIGenerator {

    String m_description = "RealTime MediaPlayer GUI";

    String m_shortDescription = "RealTime MediaPlayer GUI";

    public RealTimeMediaPlayerUIGenerator() {
    }

    public UISpec generate(Vector matchingServices, ApplianceSpec apps, UserSpec prefs, EventHeap eh, Properties configs) throws ICrafterException {
        String playerServiceName = (String) ((Vector) matchingServices.get(0)).get(0);
        String producerServiceName = (String) ((Vector) matchingServices.get(1)).get(0);
        ServiceDiscoveryUtils discoveryUtils = new ServiceDiscoveryUtils(eh);
        DiscoveredService playerService = discoveryUtils.locateServiceNamed(playerServiceName);
        DiscoveredService producerService = discoveryUtils.locateServiceNamed(producerServiceName);
        RealTimeMediaPlayerUIPanel panel = new RealTimeMediaPlayerUIPanel();
        panel.setServices(eh, playerServiceName, playerService.getShortDescription(), playerService.getMachineName(), producerServiceName, producerService.getShortDescription());
        return new JavaUI(panel, panel.getPreferredSize());
    }

    public String getOutputLanguage() {
        return "java";
    }

    public String getExecPlatform() {
        return "java";
    }

    public String getDescription() {
        return m_description;
    }

    public void setDescription(String desc) {
        m_description = desc;
    }

    public String getShortDescription() {
        return m_shortDescription;
    }

    public void setShortDescription(String desc) {
        m_shortDescription = desc;
    }
}
