package org.dcm4chee.dashboard.service.webcfg;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.jboss.system.ServiceMBeanSupport;
import org.jboss.system.server.ServerConfigLocator;

/**
 * @author franz.willer@gmail.com
 * @author Robert David <robert.david@agfa.com>
 * @version $Revision$ $Date$
 * @since July 26, 2010
 */
public class WebCfgService extends ServiceMBeanSupport {

    protected static final long serialVersionUID = 1L;

    protected boolean manageUsers;

    protected String webConfigPath;

    protected Map<String, int[]> windowsizeMap = new LinkedHashMap<String, int[]>();

    protected static final String NONE = "NONE";

    protected final String NEWLINE = System.getProperty("line.separator", "\n");

    public void setManageUsers(boolean manageUsers) {
        this.manageUsers = manageUsers;
    }

    public boolean isManageUsers() {
        return manageUsers;
    }

    public String getWebConfigPath() {
        return System.getProperty("dcm4chee-web3.cfg.path", NONE);
    }

    public void setWebConfigPath(String webConfigPath) {
        if (NONE.equals(webConfigPath)) {
            System.getProperties().remove("dcm4chee-web3.cfg.path");
        } else {
            String old = System.getProperty("dcm4chee-web3.cfg.path");
            if (!webConfigPath.endsWith("/")) webConfigPath += "/";
            System.setProperty("dcm4chee-web3.cfg.path", webConfigPath);
            if (old == null) {
                initDefaultRolesFile();
            }
        }
    }

    protected void initDefaultRolesFile() {
        String webConfigPath = System.getProperty("dcm4chee-web3.cfg.path", "conf/dcm4chee-web3");
        File mappingFile = new File(webConfigPath + "roles.json");
        if (!mappingFile.isAbsolute()) mappingFile = new File(ServerConfigLocator.locate().getServerHomeDir(), mappingFile.getPath());
        if (mappingFile.exists()) return;
        log.info("Init default Role Mapping file! mappingFile:" + mappingFile);
        if (mappingFile.getParentFile().mkdirs()) log.info("M-WRITE dir:" + mappingFile.getParent());
        FileChannel fos = null;
        InputStream is = null;
        try {
            URL url = getClass().getResource("/META-INF/roles-default.json");
            log.info("Use default Mapping File content of url:" + url);
            is = url.openStream();
            ReadableByteChannel inCh = Channels.newChannel(is);
            fos = new FileOutputStream(mappingFile).getChannel();
            int pos = 0;
            while (is.available() > 0) pos += fos.transferFrom(inCh, pos, is.available());
        } catch (Exception e) {
            log.error("Roles file doesn't exist and the default can't be created!", e);
        } finally {
            close(is);
            close(fos);
        }
    }

    protected void close(Closeable toClose) {
        if (toClose != null) {
            try {
                toClose.close();
            } catch (IOException ignore) {
                log.debug("Error closing : " + toClose.getClass().getName(), ignore);
            }
        }
    }

    public String getWindowSizeConfig() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, int[]> e : windowsizeMap.entrySet()) {
            sb.append(e.getKey()).append(':').append(e.getValue()[0]).append('x').append(e.getValue()[1]).append(NEWLINE);
        }
        return sb.toString();
    }

    public void setWindowSizeConfig(String s) {
        windowsizeMap.clear();
        StringTokenizer st = new StringTokenizer(s, " \t\r\n;");
        String t;
        int pos;
        while (st.hasMoreTokens()) {
            t = st.nextToken();
            if ((pos = t.indexOf(':')) == -1) {
                throw new IllegalArgumentException("Format must be:<name>:<width>x<height>! " + t);
            } else {
                windowsizeMap.put(t.substring(0, pos), parseSize(t.substring(++pos)));
            }
        }
    }

    public int[] getWindowSize(String name) {
        int[] size = windowsizeMap.get(name);
        if (size == null) size = windowsizeMap.get("default");
        if (size == null) {
            log.warn("No default window size is configured! use 800x600 as default!");
            return new int[] { 800, 600 };
        }
        return size;
    }

    protected int[] parseSize(String s) {
        int pos = s.indexOf('x');
        if (pos == -1) throw new IllegalArgumentException("Windowsize must be <width>x<height>! " + s);
        return new int[] { Integer.parseInt(s.substring(0, pos).trim()), Integer.parseInt(s.substring(++pos).trim()) };
    }
}
