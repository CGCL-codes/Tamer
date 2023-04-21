package net.sourceforge.obexftpfrontend.obexftp;

import java.io.BufferedReader;
import java.io.FileReader;
import net.sourceforge.obexftpfrontend.model.DevInfo;
import net.sourceforge.obexftpfrontend.persistence.MetaConfigurationReader;
import net.sourceforge.obexftpfrontend.persistence.DefaultMetaConfigurationReader;
import org.apache.log4j.Logger;

/**
 * Default implementation of DevInfoFileParser interface.
 * @author Daniel F. Martins
 */
public class DefaultDevInfoFileParser implements DevInfoFileParser {

    /** Logger. */
    private static final Logger log = Logger.getLogger(DefaultDevInfoFileParser.class);

    /** Meta configuration. */
    private MetaConfigurationReader configProperties;

    /**
     * Create a new instance of DefaultDevInfoFileParser.
     */
    public DefaultDevInfoFileParser() {
        this(DefaultMetaConfigurationReader.getInstance());
    }

    /**
     * Create a new instance of DefaultDevInfoFileParser.
     * @param configProperties Meta configuration.
     */
    public DefaultDevInfoFileParser(MetaConfigurationReader configProperties) {
        super();
        this.configProperties = configProperties;
    }

    @Override
    public DevInfo parseFile() throws OBEXFTPException {
        DevInfo info = new DevInfo();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(configProperties.getDevInfoFile()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                parseLine(line, info);
            }
            reader.close();
        } catch (Exception exc) {
            throw new OBEXFTPException("Error while trying to parse the devinfo.txt file", exc);
        }
        return info;
    }

    /**
     * Parse a string that represent a line of the device info file.
     * @param line Line to parse.
     * @param info Object to be filled with the parsed information.
     */
    private void parseLine(String line, DevInfo info) {
        String[] array = line.split(":");
        if ("MANU".equals(array[0])) {
            log.debug("devinfo.txt MANU element found");
            info.setManufacturer(array[1]);
        } else if ("MOD".equals(array[0])) {
            log.debug("devinfo.txt MOD element found");
            info.setModel(array[1]);
        } else if ("SW-VERSION".equals(array[0])) {
            log.debug("devinfo.txt SW-VERSION element found");
            info.setFirmwareVersion(array[1]);
        } else if ("SW-DATE".equals(array[0])) {
            log.debug("devinfo.txt SW-DATE element found");
            info.setFirmwareDate(array[1]);
        } else if ("HW-VERSION".equals(array[0])) {
            log.debug("devinfo.txt HW-VERSION element found");
            info.setHardwareVersion(array[1]);
        } else if ("SN".equals(array[0])) {
            log.debug("devinfo.txt SN element found");
            info.setSerialNumber(array[1]);
        }
    }
}
