package org.openthinclient.server;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;

/**
 * @author J?rg Henne
 */
public class ServerConfiguration implements Serializable {

    private static final long serialVersionUID = 3834308444944412977L;

    private static transient Mapping mapping;

    /**
   * Load a server configuration from the given path.
   * 
   * @param path
   * @return
   * @throws IOException
   * @throws MappingException
   * @throws MarshalException
   * @throws ValidationException
   */
    public static ServerConfiguration load(File path) throws IOException, MappingException, MarshalException, ValidationException {
        FileReader reader = new FileReader(path);
        Unmarshaller unmarshaller = new Unmarshaller();
        unmarshaller.setMapping(getMapping());
        ServerConfiguration unmarshal = (ServerConfiguration) unmarshaller.unmarshal(reader);
        unmarshal.location = path;
        return unmarshal;
    }

    /**
   * Save to configuration into the directory it came from. Saving the
   * configuration will done carefully, so that the configration is not lost
   * even in the case of a system failure. The process is as follows:
   * <ul>
   * <li>Save the configuration into a temporary file in the directory from
   * which the configuration was loaded.
   * <li>Move the existing configuration to a backup file (the previous name
   * with a trailing "~")
   * <li>Move the temporary file to the final file (i.e. rename it).
   * <ul>
   * 
   * @throws IOException
   * @throws MappingException
   * @throws MarshalException
   * @throws ValidationException
   */
    public void save() throws IOException, MappingException, MarshalException, ValidationException {
        if (null == location) throw new IOException("Can't save configuration: location not known");
        File parentDir = location.getParentFile();
        File tmpFile = File.createTempFile("config", ".tmp", parentDir);
        FileWriter writer = new FileWriter(tmpFile);
        Marshaller marshaller = new Marshaller(writer);
        marshaller.setMapping(getMapping());
        marshaller.marshal(this);
        writer.close();
        File backup = new File(location.getCanonicalPath() + "~");
        backup.delete();
        location.renameTo(backup);
        tmpFile.renameTo(location);
    }

    private static Mapping getMapping() throws IOException, MappingException {
        if (null == mapping) {
            mapping = new Mapping();
            mapping.loadMapping(new InputSource(ServerConfiguration.class.getResourceAsStream("mapping.xml")));
        }
        return mapping;
    }

    /**
   * The path from which this configuration was loaded.
   */
    private transient File location;

    /**
   * The path to a directory where the client configuration databases (and
   * versions) are stored.
   */
    private File clientConfigurationBase;

    /**
   * The path to a directory where the client configuration databases (and
   * versions) are stored.
   */
    private File defaultLocation;

    private int tftpPort;

    private int nfsPort;

    private int nfsProgramNumber;

    private int mountdPort;

    private int mountdProgramNumber;

    private boolean tftpEnabled = true;

    private boolean nfsEnabled = true;

    public File getClientConfigurationBase() {
        return clientConfigurationBase;
    }

    public void setClientConfigurationBase(File clientConfigurationBase) {
        this.clientConfigurationBase = clientConfigurationBase;
    }

    public int getMountdProgramNumber() {
        return mountdProgramNumber;
    }

    public void setMountdProgramNumber(int mountdProgramNumber) {
        this.mountdProgramNumber = mountdProgramNumber;
    }

    public int getNfsPort() {
        return nfsPort;
    }

    public void setNfsPort(int nfsPort) {
        this.nfsPort = nfsPort;
    }

    public int getNfsProgramNumber() {
        return nfsProgramNumber;
    }

    public void setNfsProgramNumber(int nfsProgramNumber) {
        this.nfsProgramNumber = nfsProgramNumber;
    }

    public int getTftpPort() {
        return tftpPort;
    }

    public void setTftpPort(int tftpPort) {
        this.tftpPort = tftpPort;
    }

    public boolean isNfsEnabled() {
        return nfsEnabled;
    }

    public void setNfsEnabled(boolean nfsEnabled) {
        this.nfsEnabled = nfsEnabled;
    }

    public boolean isTftpEnabled() {
        return tftpEnabled;
    }

    public void setTftpEnabled(boolean tftpEnabled) {
        this.tftpEnabled = tftpEnabled;
    }

    public int getMountdPort() {
        return mountdPort;
    }

    public void setMountdPort(int mountdPort) {
        this.mountdPort = mountdPort;
    }

    public File getDefaultLocation() {
        return defaultLocation;
    }

    public void setDefaultLocation(File defaultLocation) {
        this.defaultLocation = defaultLocation;
    }
}
