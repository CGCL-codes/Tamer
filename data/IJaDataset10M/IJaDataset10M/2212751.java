package org.vtlabs.ec2backup.config;

import org.vtlabs.ec2backup.volume.BackupPolicy;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.vtlabs.ec2backup.account.AWSAccount;
import org.vtlabs.ec2backup.volume.Volume;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.log4j.Logger;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
class ConfigReader {

    private static Logger log = Logger.getLogger(ConfigReader.class);

    private XMLConfiguration config;

    private List<AWSAccount> accounts;

    private Map<AWSAccount, List<Volume>> accountVolumesMap;

    private Map<Volume, List<BackupPolicy>> volumeBackupPolicyMap;

    public ConfigReader(File configFile) throws ConfigException {
        try {
            config = new XMLConfiguration(configFile);
            accounts = new ArrayList<AWSAccount>();
            accountVolumesMap = new HashMap<AWSAccount, List<Volume>>();
            loadConfig();
        } catch (Exception e) {
            throw new ConfigException("Error reading config file", e);
        }
    }

    public List<AWSAccount> getAccounts() {
        return accounts;
    }

    public List<Volume> getVolumes(AWSAccount a) {
        return accountVolumesMap.get(a);
    }

    private void loadConfig() {
        AWSAccount newAccount = null;
        int index = 0;
        do {
            newAccount = createAccount(getAccountAttribute("accessKeyID", config, index), getAccountAttribute("secretKeyID", config, index));
            if (newAccount != null && addAccount(newAccount)) {
                loadAccountVolumes(newAccount, getAccountConfig(index));
                index++;
            }
        } while (newAccount != null);
    }

    private void loadAccountVolumes(AWSAccount account, HierarchicalConfiguration accountConfig) {
        List<Volume> volumes = new ArrayList<Volume>();
        Volume newVolume = null;
        int index = 0;
        do {
            newVolume = createVolume(getVolumeAttribute("id", accountConfig, index), getVolumeAttribute("description", accountConfig, index));
            if (newVolume != null) {
                volumes.add(newVolume);
                index++;
            }
        } while (newVolume != null);
        addAccountVolumes(account, volumes);
    }

    private AWSAccount createAccount(String accessKeyID, String secretKeyID) {
        if (accessKeyID != null && secretKeyID != null) {
            return new AWSAccount(accessKeyID, secretKeyID);
        } else {
            return null;
        }
    }

    private Volume createVolume(String id, String description) {
        if (id != null && description != null) {
            return new Volume(id, description);
        } else {
            return null;
        }
    }

    private boolean addAccount(AWSAccount account) {
        if (accounts.add(account)) {
            log.debug(new StringBuilder("Account loaded ").append(account));
            return true;
        } else {
            log.warn(new StringBuilder("Account couldn't be added ").append(account));
            return false;
        }
    }

    private boolean addAccountVolumes(AWSAccount account, List<Volume> volumes) {
        accountVolumesMap.put(account, volumes);
        if (log.isDebugEnabled()) {
            StringBuilder sbDebug = new StringBuilder("Volumes ");
            sbDebug.append(volumes);
            sbDebug.append(" added to account ").append(account);
            log.debug(sbDebug);
        }
        return true;
    }

    private String getAccountAttribute(String attributeName, HierarchicalConfiguration config, int index) {
        return getAttribute("accounts.account", attributeName, config, index);
    }

    private String getVolumeAttribute(String attributeName, HierarchicalConfiguration config, int index) {
        return getAttribute("volumes.volume", attributeName, config, index);
    }

    private String getAttribute(String nodeName, String attributeName, HierarchicalConfiguration config, int index) {
        StringBuilder sbAttr = new StringBuilder(nodeName).append("(");
        sbAttr.append(index).append(")[@").append(attributeName).append("]");
        return config.getString(sbAttr.toString());
    }

    private HierarchicalConfiguration getAccountConfig(int index) {
        StringBuilder sbElement = new StringBuilder("accounts.account(");
        sbElement.append(index).append(")");
        return config.configurationAt(sbElement.toString());
    }
}
