package net.sourceforge.openconferencer.client.jabber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Calendar;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import net.sourceforge.openconferencer.client.ApplicationProperties;
import net.sourceforge.openconferencer.client.util.LogHelper;
import org.eclipse.swt.widgets.Combo;

/**
 * @author aleksandar
 * 
 */
public class JabberUpdateTask implements Runnable {

    private static final String CACHE_FILENAME = "jabber_server.cache";

    private static final long EXPIRATION_PERIOD = 24 * 3600 * 1000;

    private static final String DEFAULT_SERVER_URL = "http://www.jabberes.org/servers/servers.xml";

    @XmlRootElement(name = "servers")
    public static class ServerList {

        private Server[] servers;

        /**
		 * @return the servers
		 */
        @XmlElement(name = "server")
        public Server[] getServers() {
            return servers;
        }

        /**
		 * @param servers
		 *            the servers to set
		 */
        public void setServers(Server[] servers) {
            this.servers = servers;
        }
    }

    @XmlRootElement(name = "server")
    public static class Server {

        private String jid;

        private boolean offline;

        /**
		 * @return the jid
		 */
        @XmlAttribute
        public String getJid() {
            return jid;
        }

        /**
		 * @param jid
		 *            the jid to set
		 */
        public void setJid(String jid) {
            this.jid = jid;
        }

        /**
		 * @return the offline
		 */
        @XmlAttribute
        public boolean isOffline() {
            return offline;
        }

        /**
		 * @param offline
		 *            the offline to set
		 */
        public void setOffline(boolean offline) {
            this.offline = offline;
        }
    }

    private Combo serverList;

    /**
	 * 
	 */
    public JabberUpdateTask(Combo serverList) {
        this.serverList = serverList;
    }

    @Override
    public void run() {
        File tmp = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tmp, CACHE_FILENAME);
        try {
            if (!file.exists() || Calendar.getInstance().getTimeInMillis() - file.lastModified() > EXPIRATION_PERIOD) downloadCacheFile(file);
            JAXBContext ctx = JAXBContext.newInstance(ServerList.class, Server.class);
            ServerList serverList = (ServerList) ctx.createUnmarshaller().unmarshal(file);
            for (Server server : serverList.getServers()) {
                if (!server.isOffline()) {
                    this.serverList.add(server.jid);
                }
            }
        } catch (Exception ex) {
            LogHelper.warn("Failed to update jabber server list.", ex);
        }
    }

    /**
	 * 
	 * @param file
	 * @throws Exception
	 */
    protected void downloadCacheFile(File file) throws Exception {
        ApplicationProperties app = ApplicationProperties.getInstance();
        String address = app.getProperty(JabberConstants.PROPERTY_JABBER_SERVERLIST, DEFAULT_SERVER_URL);
        URL url = new URL(address);
        file.createNewFile();
        OutputStream cache = new FileOutputStream(file);
        InputStream input = url.openStream();
        byte buffer[] = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) >= 0) cache.write(buffer, 0, bytesRead);
        input.close();
        cache.close();
    }
}
