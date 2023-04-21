package com.jpeterson.littles3.bo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Static implementation of <code>UserDirectory</code> that will return the same
 * AWSSecretAccessKey for any AWSAccessKeyId.
 * 
 * @author Jesse Peterson
 */
public class FileUserDirectory implements UserDirectory {

    public static final String CONFIG_USER_FILE = "user.file";

    public static final int INDEX_AWS_ACCESS_KEY_ID = 0;

    public static final int INDEX_AWS_SECRET_ACCESS_KEY = 1;

    public static final int INDEX_USERNAME = 2;

    public static final int INDEX_DISPLAY_NAME = 3;

    private Configuration configuration;

    private Log logger;

    public FileUserDirectory() {
        logger = LogFactory.getLog(this.getClass());
    }

    public String getAwsSecretAccessKey(String awsAccessKeyId) {
        String[] components;
        components = findComponents(awsAccessKeyId);
        if (components == null) {
            return null;
        }
        return components[INDEX_AWS_SECRET_ACCESS_KEY];
    }

    public CanonicalUser getCanonicalUser(String awsAccessKeyId) {
        CanonicalUser user;
        String[] components;
        components = findComponents(awsAccessKeyId);
        if (components == null) {
            return null;
        }
        user = new CanonicalUser(components[INDEX_USERNAME]);
        user.setDisplayName(components[INDEX_DISPLAY_NAME]);
        return user;
    }

    private String[] findComponents(String awsAccessKeyId) {
        BufferedReader in = null;
        String userFile = configuration.getString(CONFIG_USER_FILE);
        File file = new File(userFile);
        try {
            in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#")) {
                } else {
                    String[] components = line.split(",");
                    if (components[INDEX_AWS_ACCESS_KEY_ID].equals(awsAccessKeyId)) {
                        return components;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("Could not open configured user file: " + userFile, e);
        } catch (IOException e) {
            logger.error("Error reading user file: " + userFile, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
