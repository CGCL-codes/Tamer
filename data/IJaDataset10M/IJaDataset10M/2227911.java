package org.antdepo.tasks;

import org.antdepo.Constants;
import org.antdepo.utils.JARVerifier;
import org.apache.log4j.Category;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.jar.JarFile;

/**
 * VerifyJar task verifies a JAR file given the proper configuration for the keystore.
 * Attributes:
 * jar - the jar file path
 * resultProperty - property to set with success status
 * failOnError - true if failure should be an error
 * frameworkProperties - (optional) path to properties file with required properties.
 * <p/>
 * If frameworkProperties is not defined, the ${antdepo.base}/etc/framework.properties is used.
 * <p/>
 * The properties file should have these properties:
 * framework.crypto.keystore = (path to keystore file)
 * framework.crypto.keystore.password = (password for keystore file verification, optional)
 * framework.crypto.jarSigning.keystoreAlias = (alias name of the certificate or cert chain to verify with)
 *
 * @author Greg Schueler <a href="mailto:greg@controltier.com">greg@controltier.com</a>
 * @version $Revision: 984 $
 * @ant.task name="verify-jar"
 */
public class VerifyJar extends Task {

    public static Category logger = Category.getInstance(VerifyJar.class.toString());

    private static final String KEYSTORE_PROPERTY_NAME = "framework.crypto.keystore";

    private static final String KEYSTORE_PASSWD_PROPERTY_NAME = "framework.crypto.keystore.password";

    private static final String JARSIGNING_ALIAS_PROPERTY_NAME = "framework.crypto.jarSigning.keystoreAlias";

    private File jar;

    private String resultProperty;

    private boolean failOnError = true;

    private String frameworkProperties = null;

    public File getJar() {
        return jar;
    }

    public void setJar(File jar) {
        this.jar = jar;
    }

    public void validateAttributes() {
        if (null == getJar()) {
            throw new BuildException("jar attribute required");
        }
        if (!getJar().exists()) {
            throw new BuildException("jar attribute file does not exist: " + getJar().toString());
        }
        if (null == getResultProperty()) {
            throw new BuildException("resultProperty attribute required");
        }
        if (null == getFrameworkProperties()) {
            if (null == getProject().getProperty("antdepo.base")) {
                throw new BuildException("antdepo.base property is not present");
            }
            setFrameworkProperties(Constants.getFrameworkProperties(getProject().getProperty("antdepo.base")));
        }
    }

    public void execute() throws BuildException {
        validateAttributes();
        File fwk = new File(getFrameworkProperties());
        if (!fwk.exists()) {
            throw new BuildException("Couldn't load framework.properties: does not exist");
        }
        Properties p = new Properties();
        try {
            FileInputStream fis = new FileInputStream(fwk);
            try {
                p.load(fis);
            } finally {
                if (null != fis) {
                    fis.close();
                }
            }
        } catch (IOException e) {
            throw new BuildException("Unable to load framework.properties: " + e.getMessage(), e);
        }
        String keystore = p.getProperty(KEYSTORE_PROPERTY_NAME);
        String passwd = p.getProperty(KEYSTORE_PASSWD_PROPERTY_NAME);
        String alias = p.getProperty(JARSIGNING_ALIAS_PROPERTY_NAME);
        if (null == keystore) {
            throw new BuildException("Couldn't verify jar: " + KEYSTORE_PROPERTY_NAME + " property not found");
        }
        if (null == alias) {
            throw new BuildException("Couldn't verify jar: " + JARSIGNING_ALIAS_PROPERTY_NAME + " property not found");
        }
        if (null != passwd && "".equals(passwd)) {
            passwd = null;
        }
        boolean verified = false;
        try {
            JARVerifier verifier = JARVerifier.create(keystore, alias, passwd == null ? null : passwd.toCharArray());
            verifier.verifySingleJarFile(new JarFile(getJar()));
            verified = true;
        } catch (JARVerifier.VerifierException e) {
            if (isFailOnError()) {
                throw new BuildException("Jar file failed to verify: " + e.getMessage(), e);
            } else {
                logger.error("Jar file failed to verify: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            if (isFailOnError()) {
                throw new BuildException("Error trying to verify jar file: " + e.getMessage(), e);
            } else {
                logger.error("Error trying to verify jar file: " + e.getMessage(), e);
            }
        }
        getProject().setProperty(getResultProperty(), verified ? "0" : "1");
    }

    public String getResultProperty() {
        return resultProperty;
    }

    public void setResultProperty(String resultProperty) {
        this.resultProperty = resultProperty;
    }

    public boolean isFailOnError() {
        return failOnError;
    }

    public void setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }

    public String getFrameworkProperties() {
        return frameworkProperties;
    }

    public void setFrameworkProperties(String frameworkProperties) {
        this.frameworkProperties = frameworkProperties;
    }
}
