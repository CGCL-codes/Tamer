package org.simpleframework.tool.cp;

import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "host", strict = false)
class Host implements Searchable {

    @Attribute(required = false)
    String name;

    @Attribute
    String hostname;

    @Attribute
    String type;

    @Attribute(required = false)
    String location;

    @Attribute(required = false)
    String network;

    @Attribute(name = "relay-hostname", required = false)
    String relay_hostname;

    @ElementList(inline = true, required = false, empty = false)
    List<Host.Server> list;

    String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getName() {
        return hostname;
    }

    public boolean matches(String regex, boolean deep, List<String> bagOfTokens) {
        int count = 0;
        if (list != null) {
            for (Host.Server server : list) {
                if (server.name != null && server.name.toLowerCase().matches(regex)) {
                    bagOfTokens.add(server.name);
                    count++;
                }
            }
        }
        if (name != null && name.toLowerCase().matches(regex)) {
            bagOfTokens.add(name);
            count++;
        }
        if (hostname != null && hostname.toLowerCase().matches(regex)) {
            bagOfTokens.add(hostname);
            count++;
        }
        return count > 0;
    }

    @Root(name = "server", strict = false)
    private static class Server {

        @Attribute
        String name;

        @Attribute(required = false)
        String disabled;

        @ElementList(inline = true, required = false)
        List<HostAttribute> list;
    }
}
