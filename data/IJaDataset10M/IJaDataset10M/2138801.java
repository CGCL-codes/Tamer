package com.od.jtimeseries.net.udp.message.properties;

import com.od.jtimeseries.net.udp.message.AnnouncementMessage;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: nick
 * Date: 24-Jun-2009
 * Time: 12:45:00
 * To change this template use File | Settings | File Templates.
 */
abstract class PropertiesAnnouncementMessage extends AbstractPropertiesUdpMessage implements AnnouncementMessage {

    public static final String PORT_KEY = "PORT";

    public static final String DESCRIPTION_KEY = "DESCRIPTION";

    PropertiesAnnouncementMessage(String messageType) {
        super(messageType);
    }

    PropertiesAnnouncementMessage(Properties p) {
        super(p);
    }

    public int getPort() {
        return Integer.parseInt(getProperty(PORT_KEY));
    }

    public String getDescription() {
        return getProperty(DESCRIPTION_KEY);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof AnnouncementMessage)) return false;
        if (!super.equals(o)) return false;
        AnnouncementMessage that = (AnnouncementMessage) o;
        if (getPort() != that.getPort()) return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null) return false;
        return true;
    }

    public int hashCode() {
        int result = getPort();
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        return result;
    }
}
