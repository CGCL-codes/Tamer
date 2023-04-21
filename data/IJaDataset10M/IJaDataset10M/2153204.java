package fi.hip.gb.disk.transport.jgroups;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgroups.Address;
import org.jgroups.Message;
import org.jgroups.stack.IpAddress;
import fi.hip.gb.disk.conf.Config;
import fi.hip.gb.disk.transport.Transport;

/**
 * Implementation of {@link Transport} interface for JGroups
 * groups communication protocol. 
 * 
 * @author Juho Karppinen
 */
public class JGroupsTransport implements Transport {

    /** destination address, or null if the message should be broadcasted for all */
    private Address destination;

    private JGroupsServer server;

    private Log log = LogFactory.getLog(JGroupsTransport.class);

    /**
     * Creates a new connection to the given endpoint. The address
     * can be given either as plain ip address, ip:port, or it can be
     * null (multicast for all group members). If port is omitted 
     * a random member from that ip address is used. In addition, if there is
     * no service running in given port number we fallback
     * to some other port instead.
     * <p>
     * The endpoint URL can be given with jgroups:// prefix or without.
     * 
     * @param address address of the endpoint 
     * @throws IOException if the server could not be found
     */
    public JGroupsTransport(String address) throws IOException {
        this.server = (JGroupsServer) Config.getGroupInfosys();
        if (address != null) {
            if (address.startsWith("jgroups://")) {
                address = address.substring(10);
            }
            URL url = new URL("http://" + address);
            Vector<String> ports = new Vector<String>();
            String[] members = this.server.getServers();
            for (int i = 0; i < members.length; i++) {
                if (members[i].equals(url.getHost() + ":" + url.getPort())) {
                    this.destination = new IpAddress(url.getHost(), url.getPort());
                    break;
                } else if (members[i].startsWith(url.getHost() + ":")) {
                    ports.add(members[i].substring(address.indexOf(":") + 1));
                }
            }
            if (this.destination == null) {
                if (ports.size() > 0) this.destination = new IpAddress(url.getHost(), Integer.parseInt(ports.elementAt(new Random().nextInt(ports.size() - 1)))); else throw new IOException("No online server found from ip " + url);
            }
        } else {
            log.info("No destination set, multicasting to all");
        }
    }

    public void put(String path, File file) throws IOException {
        log.debug("Sending " + file.getAbsolutePath() + " " + file.length() + " bytes to " + this.destination);
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] line = new byte[FileMessage.MAXSIZE];
            int bytes = -1;
            final long startTime = System.currentTimeMillis();
            while ((bytes = in.read(line)) != -1) {
                Message msg = new Message(this.destination, null, new FileMessage(path, line, bytes, file.length()));
                server.sendSyncMessage(msg);
            }
            log.debug("file " + path + " uploaded in " + (System.currentTimeMillis() - startTime) / 1000 + " seconds to " + this.destination);
        } catch (FileNotFoundException fnfe) {
            log.error("File " + file.getAbsolutePath() + " not found", fnfe);
            throw fnfe;
        } catch (IOException ioe) {
            log.error("Error reading file " + file.getAbsolutePath(), ioe);
            throw ioe;
        } catch (Exception e) {
            log.error("Failed to put file " + file.getAbsolutePath(), e);
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException ioe) {
                log.error("Failed to close inputstream for file " + file.getAbsolutePath(), ioe);
            }
        }
    }

    public void get(String path, File file) throws IOException {
        if (file.exists()) {
            log.debug("File " + file.getAbsolutePath() + " already exists, skipping downloading from " + this.destination);
        } else {
            log.debug("Starting receiving file from " + this.destination + " into " + file.getAbsolutePath());
            FileOutputStream out = null;
            Message req = new Message(this.destination, null, new FileMessage(path, FileMessage.GET));
            try {
                out = new FileOutputStream(file);
                final long startTime = System.currentTimeMillis();
                while (true) {
                    FileMessage msg = (FileMessage) this.server.sendSyncMessage(req);
                    out.write(msg.getBytes(), 0, (int) msg.getLength());
                    out.flush();
                    if (msg.getOffset() + msg.getLength() >= msg.getFileSize()) {
                        break;
                    } else if (file.exists() == false) {
                        log.debug(file.getAbsolutePath() + " canceled");
                        break;
                    } else {
                        FileMessage fm = (FileMessage) req.getObject();
                        fm.setOffset(msg.getOffset() + msg.getLength());
                        req.setObject(fm);
                        log.debug(file.getAbsolutePath() + " got " + msg.getLength() + " bytes " + " asking new set from " + fm.getOffset() + "/" + msg.getFileSize());
                    }
                }
                log.debug(file.getAbsolutePath() + " completed in " + (System.currentTimeMillis() - startTime) / 1000 + " seconds from " + this.destination + " " + file.length() + " bytes");
            } catch (Exception e) {
                throw new IOException("Failed to download file from " + this.destination + " into " + file.getAbsolutePath() + " : " + e.getMessage());
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        log.error("Failed to close stream for file " + file.getAbsolutePath());
                    }
                }
            }
        }
    }

    public void delete(String path) throws IOException {
        Message msg = new Message(this.destination, null, new FileMessage(path, FileMessage.DELETE));
        try {
            this.server.sendSyncMessage(msg);
            log.info("file " + path + " deleted");
        } catch (Exception e) {
            throw new IOException("Failed to delete file " + path);
        }
    }

    public int exists(String fileName) throws IOException {
        return -1;
    }
}
