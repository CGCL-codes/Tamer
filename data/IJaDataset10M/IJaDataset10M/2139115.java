package net.sourceforge.grass.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.grass.InstallConfig;
import net.sourceforge.grass.model.ConfigException;

/**
 * ConfigInstaller replcaes tokens with install value for it defined from the .grass file in the
 * user home dir.
 * 
 * @author Saravana Aravind R
 * @version $Id: ConfigInstaller.java,v 1.1 2010/08/30 04:49:37 rsaravind Exp $
 */
public class ConfigInstaller {

    private static Logger logger;

    static {
        ConfigInstaller.logger = Logger.getLogger(ConfigInstaller.class.getName());
    }

    public boolean process(String inputDir) throws ConfigException {
        ConfigInstaller.logger.fine("Install config in " + inputDir);
        boolean installed = false;
        String homeDir = System.getProperty("user.home");
        homeDir += (homeDir.endsWith(File.separator)) ? "" : File.separator;
        File grassFile = new File(homeDir + ".grass");
        if (!grassFile.exists()) {
            throw new ConfigException("Define install config values in a .grass file in home directory");
        }
        InstallConfig installConfig = InstallConfig.build(grassFile);
        installed = this.processDir(new File(inputDir), installConfig);
        return installed;
    }

    private boolean processDir(File dir, InstallConfig installConfig) {
        boolean installed = false;
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; ++i) {
            if (files[i].isFile()) {
                boolean hasInstallToken = ConfigParser.hasInstallToken(files[i]);
                if (hasInstallToken) {
                    try {
                        installed = this.processFile(files[i], installConfig);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                installed = this.processDir(files[i], installConfig);
            }
        }
        return installed;
    }

    /**
     * Processes the given dynamic config file for the given target and generates the config file
     * appropriate for the target.
     * 
     * @param srcPath the dynamic config file
     * @param targetPath the generated config file path relative to output dir
     * @param target the target environment
     * @throws IOException if any IO errors
     */
    public boolean processFile(File in, InstallConfig installConfig) throws IOException {
        FileReader input = null;
        FileWriter output = null;
        StringBuffer buffer = new StringBuffer();
        boolean installed = false;
        try {
            input = new FileReader(in);
            BufferedReader reader = new BufferedReader(input);
            String line = null;
            StringBuffer temp = null;
            while ((line = reader.readLine()) != null) {
                Pattern pattern = Pattern.compile("\\[~[^(\\[!)]*~\\]");
                Matcher matcher = pattern.matcher(line);
                temp = new StringBuffer();
                while (matcher.find()) {
                    String tokenName = matcher.group();
                    tokenName = tokenName.substring(2, tokenName.length() - 2);
                    ConfigInstaller.logger.fine(installConfig.toString());
                    String value = installConfig.getValue(tokenName);
                    ConfigInstaller.logger.fine("Install Token value for " + tokenName + " : " + value);
                    if ((value == null) || value.equals("")) {
                        throw new ConfigException("Install value not defined for token " + tokenName);
                    }
                    matcher.appendReplacement(temp, value);
                }
                matcher.appendTail(temp);
                buffer.append(temp.toString() + "\n");
            }
            input.close();
            output = new FileWriter(in);
            output.write(buffer.toString());
            ConfigInstaller.logger.info("Installed Config File : " + in.getName());
            installed = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            output.close();
        }
        return installed;
    }
}
