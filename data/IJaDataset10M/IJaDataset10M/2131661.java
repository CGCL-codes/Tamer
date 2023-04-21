package com.mockturtlesolutions.snifflib.reposconfig.database;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import com.mockturtlesolutions.snifflib.guitools.components.DomainNameFilter;
import com.mockturtlesolutions.snifflib.guitools.components.LeastPartDomainNameFilter;

/**
Instantiation of this class requires successful aquisition of the current
system configuration.  A class instance can be accessed to provide infomation
about the configuration.
*/
public class ReposConfig implements ReposConfiguration {

    public String SysConfigFile;

    public String UsrConfigFile;

    public String ConfigEnvironmentVariable;

    protected LinkedHashMap ReposMap;

    public String SplitString;

    /**
	Get the configuration information from the usual places.
	This is 
	*/
    public ReposConfig() {
        this.ConfigEnvironmentVariable = "REPOSCONFIG";
        this.SysConfigFile = System.getenv(this.ConfigEnvironmentVariable);
        this.SplitString = " ";
        this.UsrConfigFile = (String) System.getProperty("user.home").concat(File.separator).concat(".myreposconfig");
        this.ReposMap = new LinkedHashMap();
    }

    public ReposConfig(String S) {
        this.SysConfigFile = null;
        this.UsrConfigFile = (String) System.getProperty("user.home").concat(File.separator).concat(S);
        this.ReposMap = new LinkedHashMap();
        this.SplitString = " ";
    }

    public static Class getPreferencesClass() {
        Class out = RepositoryPrefs.class;
        return (out);
    }

    /**
	Get a HashMap containing the default configuration.  This may be overridden for
	more detailed configuration classes.
	*/
    public LinkedHashMap getDefaultConfig() {
        LinkedHashMap configmap = new LinkedHashMap();
        configmap.put("name", "Name");
        return (configmap);
    }

    public HashSet getDomainNameFilterConfigs() {
        HashSet out = new HashSet();
        return (out);
    }

    public HashSet getButtonTextConfigs() {
        HashSet out = new HashSet();
        return (out);
    }

    public HashSet getIntegerConfigs() {
        HashSet out = new HashSet();
        return (out);
    }

    public HashSet getColorConfigs() {
        HashSet out = new HashSet();
        return (out);
    }

    public HashSet getYesNoConfigs() {
        HashSet out = new HashSet();
        return (out);
    }

    public HashSet getTrueFalseConfigs() {
        HashSet out = new HashSet();
        return (out);
    }

    public HashSet getFontConfigs() {
        HashSet out = new HashSet();
        return (out);
    }

    public HashSet getFileChooserConfigs() {
        HashSet out = new HashSet();
        return (out);
    }

    public HashSet getDirectoryChooserConfigs() {
        HashSet out = new HashSet();
        return (out);
    }

    public HashSet getOnOffConfigs() {
        HashSet out = new HashSet();
        return (out);
    }

    public int size() {
        return (this.ReposMap.size());
    }

    /**Return true if a configuration exists for the named
	 repository.
	 */
    public boolean hasRepository(String repos) {
        return (this.ReposMap.containsKey(repos));
    }

    public void addRepository(String repos) {
        this.ReposMap.put(repos, this.getDefaultConfig());
    }

    public void removeRepository(String repos) {
        if (this.ReposMap.remove(repos) == null) {
            System.out.println("Unable to remove repository " + repos + ".");
        }
    }

    public String getUsrConfigFile() {
        return (this.UsrConfigFile);
    }

    /**
	This may be overridden so that a class will use a different user configuration file.
	*/
    public String getSysConfigFile() {
        return (this.SysConfigFile);
    }

    /**
	*/
    public String[] getIllegalChars() {
        String[] out = null;
        return (out);
    }

    /**
	This may be overridden so that a class will use a different user configuration file.
	*/
    public String getConfigEnvironmentVariable() {
        return (this.ConfigEnvironmentVariable);
    }

    /**
	(Re)reads the configuration file information.
	*/
    public void initialize() {
        this.ReposMap.clear();
        this.ReposMap.put("default", this.getDefaultConfig());
        if (this.SysConfigFile != null) {
            File sysconfigfile = new File(this.SysConfigFile);
            if (sysconfigfile.exists()) {
                this.processConfig(sysconfigfile);
            } else {
            }
        }
        File usrconfigfile = new File(this.UsrConfigFile);
        if (usrconfigfile.exists()) {
            this.processConfig(usrconfigfile);
        } else {
            int s = JOptionPane.showConfirmDialog(null, "Create configuration file " + usrconfigfile.getName() + "?", "Configuration file does not exist!", JOptionPane.YES_NO_CANCEL_OPTION);
            if (s == JOptionPane.YES_OPTION) {
                try {
                    usrconfigfile.createNewFile();
                    this.saveConfig();
                } catch (IOException err) {
                    throw new RuntimeException("Problem creating and saving new configuration file.", err);
                }
            } else {
                return;
            }
        }
    }

    /**
	Print the configuration on standard output.
	*/
    public void show() {
        Set reposkeys = this.ReposMap.keySet();
        Set configkeys;
        Iterator iter = reposkeys.iterator();
        Iterator iter2;
        String repos, config, value;
        System.out.println("");
        System.out.println("Current repository configuration:");
        HashMap configMap;
        while (iter.hasNext()) {
            repos = (String) iter.next();
            System.out.println("\n\nRepository:" + repos);
            configMap = (HashMap) this.ReposMap.get(repos);
            configkeys = configMap.keySet();
            iter2 = configkeys.iterator();
            while (iter2.hasNext()) {
                config = (String) iter2.next();
                value = (String) (configMap.get(config));
                System.out.println(config + ":" + value);
            }
        }
        System.out.println("");
    }

    /**
	Get a string array of repository names.
	*/
    public String[] getRepositories() {
        Set reposkeys = this.ReposMap.keySet();
        if (reposkeys == null) {
            throw new RuntimeException("No repositories are configured.");
        }
        String[] repos = new String[reposkeys.size()];
        Iterator iter = reposkeys.iterator();
        int j = 0;
        while (iter.hasNext()) {
            repos[j] = (String) iter.next();
            j++;
        }
        return (repos);
    }

    public LinkedHashMap getConfig() {
        return (this.ReposMap);
    }

    public HashMap getConfig(String repository) {
        return ((HashMap) this.ReposMap.get(repository));
    }

    /**
	Return the keys of configuration for a particular repository as a Set.
	*/
    public Set getConfigKeys() {
        HashMap configMap = (HashMap) this.getDefaultConfig();
        return ((Set) configMap.keySet());
    }

    public void setSplitString(String splitter) {
        this.SplitString = splitter;
    }

    public String getSplitString() {
        return (this.SplitString);
    }

    /**
	Returns a String[] of of values delimited by the currently set SplitString.
	
	For example if a configuration recentDatabases had as it's value
	
	"main,remote,home"
	
	and the SplitString was set to "," then getSplitConfigValue would return the
	String[]{"main","remote","home"}
	
	*/
    public String[] getSplitConfigValue(String repository, String config) {
        String[] inputs = (String[]) this.getConfigValue(repository, config).split(this.SplitString);
        return (inputs);
    }

    /**
	Returns the first configured value when the configuration is treated as a list with items separated
	by the split string.
	*/
    public String getFirstSplitConfigValue(String repository, String config) {
        String out = null;
        String[] inputs = (String[]) this.getConfigValue(repository, config).split(this.SplitString);
        if (inputs != null) {
            if (inputs.length > 0) {
                out = inputs[0];
            }
        }
        return (out);
    }

    /**
	Returns the last configured value when the configuration is treated as a list with items separated
	by the split string.  
	*/
    public String getLastSplitConfigValue(String repository, String config) {
        String out = null;
        String[] inputs = (String[]) this.getConfigValue(repository, config).split(this.SplitString);
        if (inputs != null) {
            if (inputs.length > 0) {
                out = inputs[inputs.length - 1];
            }
        }
        return (out);
    }

    public void setSplitConfigValue(String repository, String config, String[] V) {
        if (this.hasIllegalChars(V[0])) {
            throw new IllegalArgumentException("Configuration value`" + V[0] + "' has illegal characters.");
        }
        String value = V[0];
        for (int j = 0; j < V.length; j++) {
            if (this.hasIllegalChars(V[j])) {
                throw new IllegalArgumentException("Configuration value`" + V[j] + "' has illegal characters.");
            }
        }
        for (int j = 1; j < V.length; j++) {
            value = value.concat(this.SplitString);
            value = value.concat(V[j]);
        }
        this.setConfigValue(repository, config, value);
    }

    public String getConfigValue(String config) {
        return ((String) this.getConfigValue("default", config));
    }

    /**
	Return the value of a configuration for a particular repository.
	*/
    public String getConfigValue(String repository, String config) {
        HashMap configMap;
        if (this.hasRepository(repository)) {
            configMap = (HashMap) this.ReposMap.get(repository);
        } else {
            this.show();
            throw new IllegalArgumentException("Configuration does not have a listing for repository `" + repository + "'.");
        }
        String OUT = (String) configMap.get(config);
        if (OUT == null) {
            throw new IllegalArgumentException("Configuration does not maintain a mapping for key `" + config + "'.");
        }
        String[] inputs = OUT.split(this.SplitString);
        for (int j = 0; j < inputs.length; j++) {
            if (this.hasIllegalChars(inputs[j])) {
                throw new IllegalArgumentException("Configuration value`" + inputs[j] + "' has illegal characters.");
            }
        }
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formatted_date = formatter.format(date);
        OUT = OUT.replaceAll("\\$TODAY", formatted_date);
        return (OUT);
    }

    public void setConfigValue(String config, Object value) {
        this.setConfigValue("default", config, value.toString());
    }

    /**
	Set the value of a configuration for a particular repository.
	*/
    public void setConfigValue(String repository, String config, String value) {
        String[] inputs = value.split(this.SplitString);
        for (int j = 0; j < inputs.length; j++) {
            if (this.hasIllegalChars(inputs[j])) {
                throw new IllegalArgumentException("Configuration value`" + inputs[j] + "' has illegal characters.");
            }
        }
        HashMap configMap = (HashMap) this.ReposMap.get(repository);
        configMap.put(config, value);
    }

    public boolean hasIllegalChars(String value) {
        boolean check = false;
        String[] illegals = this.getIllegalChars();
        if (illegals != null) {
            for (int j = 0; j < illegals.length; j++) {
                if (value.indexOf(illegals[j]) != -1) {
                    check = true;
                    break;
                }
            }
        }
        return (check);
    }

    /**
	Save the current configuration back to the configuration file.
	*/
    public void saveConfig() {
        try {
            File savefile = new File(this.getUsrConfigFile());
            FileWriter writer = new FileWriter(savefile);
            String[] repositories = (String[]) this.getRepositories();
            Iterator iter;
            Set keyset;
            String key;
            keyset = this.getConfigKeys();
            for (int j = 0; j < repositories.length; j++) {
                if (!repositories[j].equalsIgnoreCase("untitled")) {
                    writer.write("\n\n[" + repositories[j] + "]");
                    iter = keyset.iterator();
                    while (iter.hasNext()) {
                        key = (String) iter.next();
                        writer.write("\n" + key + "=" + this.getConfigValue(repositories[j], key));
                    }
                }
            }
            writer.close();
        } catch (java.io.IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void showError(String msg) {
        JOptionPane dialog = new JOptionPane();
        JTextArea area = new JTextArea(msg);
        JScrollPane jsp = new JScrollPane(area);
        jsp.setPreferredSize(new Dimension(500, 200));
        dialog.showMessageDialog(null, jsp, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void processConfig(File configfile) {
        this.ReposMap = new LinkedHashMap();
        BufferedReader input = null;
        String line = null;
        int lineno = 0;
        String key = null;
        String value = null;
        String Title;
        int j, k;
        String currentRepository = null;
        HashMap reposConfig = null;
        this.ReposMap.put("default", (HashMap) this.getDefaultConfig());
        try {
            input = new BufferedReader(new FileReader(configfile));
            while ((line = input.readLine()) != null) {
                lineno++;
                if (line.length() > 0) {
                    if (line.charAt(0) == '#') {
                    } else {
                        if (line.charAt(0) == ' ') {
                            for (k = 0; k < line.length(); k++) {
                                if (line.charAt(k) != ' ') {
                                    throw new IllegalArgumentException("Unable to parse configuration file " + configfile + " at line " + lineno + ".");
                                }
                            }
                        } else {
                            if (line.charAt(0) == '[') {
                                if (line.length() < 3) {
                                    throw new IllegalArgumentException("Unable to parse configuration file " + configfile + " at line " + lineno + ".");
                                }
                                for (k = 1; k < line.length(); k++) {
                                    if (line.charAt(k) == '[' | line.charAt(k) == '#') {
                                        throw new IllegalArgumentException("Unable to parse configuration file " + configfile + " at line " + lineno + ".");
                                    } else {
                                        if (line.charAt(k) == ']') {
                                            if (line.length() > k) {
                                                for (int l = k + 1; l < line.length(); l++) {
                                                    if (line.charAt(l) == '#') {
                                                        break;
                                                    }
                                                    if (line.charAt(l) == ' ') {
                                                    } else {
                                                        throw new IllegalArgumentException("Unable to parse configuration file " + configfile + " at line " + lineno + ".");
                                                    }
                                                }
                                            }
                                            if (reposConfig != null) {
                                                this.ReposMap.put(currentRepository, (HashMap) reposConfig);
                                            }
                                            currentRepository = line.substring(1, k);
                                            reposConfig = this.getDefaultConfig();
                                            break;
                                        }
                                    }
                                }
                            } else {
                                if (currentRepository == null) {
                                    throw new IllegalArgumentException("Unable to parse configuration file " + configfile + " at line " + lineno + ".");
                                }
                                key = null;
                                for (k = 0; k < line.length(); k++) {
                                    if (line.charAt(k) == '[' | line.charAt(k) == '#') {
                                        throw new IllegalArgumentException("Unable to parse configuration file " + configfile + " at line " + lineno + ".");
                                    } else {
                                        if (line.charAt(k) == ']') {
                                            throw new IllegalArgumentException("Unable to parse configuration file " + configfile + " at line " + lineno + ".");
                                        } else {
                                            if (line.charAt(k) == '=') {
                                                if (currentRepository == null | reposConfig == null) {
                                                    throw new IllegalArgumentException("Unable to parse configuration file " + configfile + " at line " + lineno + ".");
                                                }
                                                key = line.substring(0, k);
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (key == null) {
                                    throw new IllegalArgumentException("Unable to parse configuration file " + configfile + " at line " + lineno + ".");
                                } else {
                                    value = null;
                                    line = line.substring(k + 1, line.length());
                                    for (k = 0; k < line.length(); k++) {
                                        if (line.charAt(k) == '[' | line.charAt(k) == ']') {
                                            throw new IllegalArgumentException("Unable to parse configuration file " + configfile + " at line " + lineno + ".");
                                        } else {
                                            if (line.charAt(k) == '#') {
                                                value = line.substring(0, k - 1);
                                                break;
                                            }
                                        }
                                    }
                                    if (value == null) {
                                        value = line;
                                    }
                                    reposConfig.put((String) key, (String) value);
                                }
                            }
                        }
                    }
                }
            }
            if (reposConfig != null) {
                this.ReposMap.put((String) currentRepository, (HashMap) reposConfig);
            }
        } catch (IOException e) {
            throw new RuntimeException("Problem reading in repository configuration.", e);
        }
    }
}
