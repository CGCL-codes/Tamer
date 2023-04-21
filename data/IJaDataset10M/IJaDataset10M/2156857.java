package net.sf.jradius.session;

import java.util.Map;
import net.sf.jradius.exception.RadiusException;
import net.sf.jradius.log.RadiusLogEntry;
import net.sf.jradius.server.JRadiusRequest;
import net.sf.jradius.server.config.ConfigurationItem;
import net.sf.jradius.server.config.XMLConfiguration;
import net.sf.jradius.util.RadiusRandom;
import org.apache.commons.configuration.HierarchicalConfiguration;

/**
 * The Default SessionFactory.
 * @author Gert Jan Verhoog
 * @author David Bird
 */
public class RadiusSessionFactory implements SessionFactory {

    private Map configMap = null;

    public JRadiusSession getSession(JRadiusRequest request, Object key) throws RadiusException {
        return null;
    }

    public JRadiusSession newSession(JRadiusRequest request) {
        return new RadiusSession(createNewSessionID());
    }

    public RadiusLogEntry newSessionLogEntry(JRadiusSession session, String packetId) {
        return new RadiusLogEntry(session, packetId);
    }

    protected String createNewSessionID() {
        return RadiusRandom.getRandomString(16);
    }

    public String getConfigValue(String name) {
        if (configMap == null) return null;
        return (String) configMap.get(name);
    }

    public void setConfig(XMLConfiguration config, HierarchicalConfiguration.Node root) {
        this.configMap = ConfigurationItem.getPropertiesFromConfig(config, root);
    }
}
