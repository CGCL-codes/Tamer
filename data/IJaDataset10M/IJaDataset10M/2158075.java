package com.whereisnow.webservice.client;

import org.apache.log4j.Logger;

public abstract class WSClient {

    private static final String DEFAULT_WS_SECURITY_MODULE = "rampart";

    private static Logger logger = Logger.getLogger(WSClient.class);

    private String secureServiceEndPoint = null;

    private String freeServiceEndPoint = null;

    private String userEmail = null;

    private String userPassword = null;

    private String certPath = null;

    private String certPassword = null;

    private String repositoryPath = null;

    private String securityModuleName = null;

    private boolean stubCorrectlyInitialized = false;

    public WSClient() {
        this(null);
    }

    /**
     *
     * @param freeServiceEndPoint
     */
    public WSClient(String freeServiceEndPoint) {
        setSecurityModuleName(DEFAULT_WS_SECURITY_MODULE);
        setFreeServiceEndPoint(freeServiceEndPoint);
    }

    /**
     *
     * @param userEmail
     * @param userPassword
     */
    public WSClient(String userEmail, String userPassword) {
        this(userEmail, userPassword, null);
    }

    /**
     *
     * @param userEmail
     * @param userPassword
     * @param secureServiceEndPoint
     */
    public WSClient(String userEmail, String userPassword, String secureServiceEndPoint) {
        setSecurityModuleName(DEFAULT_WS_SECURITY_MODULE);
        setUserEmail(userEmail);
        setUserPassword(userPassword);
        setSecureServiceEndPoint(secureServiceEndPoint);
    }

    /**
     *
     * @throws java.lang.Exception
     */
    public abstract void initializeFreeStub() throws Exception;

    /**
     *
     * @throws java.lang.Exception
     */
    public abstract void initializeSecureStub() throws Exception;

    /**
     *
     * @return
     */
    protected boolean validateCredentials() {
        if (!validateStringField(userEmail) || !validateStringField(userPassword)) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param stringField
     * @return
     */
    protected boolean validateStringField(String stringField) {
        return (stringField != null && !stringField.isEmpty());
    }

    /**
     *
     * @param certPath
     * @param certPassword
     * @param repositoryPath
     */
    public void supplyClientParameters(String certPath, String certPassword, String repositoryPath) {
        setCertPath(certPath);
        setCertPassword(certPassword);
        setRepositoryPath(repositoryPath);
    }

    /**
     *
     * @return
     */
    public String getCertPassword() {
        if (!validateStringField(certPassword)) {
            throw new NullPointerException("Certificate Password cannot be NULL or VOID!");
        }
        return certPassword;
    }

    /**
     *
     * @param certPassword
     */
    public void setCertPassword(String certPassword) {
        this.certPassword = certPassword;
    }

    /**
     *
     * @return
     */
    public String getCertPath() {
        if (!validateStringField(certPath)) {
            throw new NullPointerException("Certificate Path cannot be NULL or VOID!");
        }
        return certPath;
    }

    /**
     *
     * @param certPath
     */
    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    /**
     *
     * @return
     */
    public String getFreeServiceEndPoint() {
        return freeServiceEndPoint;
    }

    /**
     *
     * @param freeServiceEndPoint
     */
    public void setFreeServiceEndPoint(String freeServiceEndPoint) {
        this.freeServiceEndPoint = freeServiceEndPoint;
    }

    /**
     *
     * @return
     */
    public String getRepositoryPath() {
        if (!validateStringField(repositoryPath)) {
            throw new NullPointerException("Repository Path cannot be NULL or VOID!");
        }
        return repositoryPath;
    }

    /**
     *
     * @param repositoryPath
     */
    public void setRepositoryPath(String repositoryPath) {
        this.repositoryPath = repositoryPath;
    }

    /**
     *
     * @return
     */
    public String getSecureServiceEndPoint() {
        return secureServiceEndPoint;
    }

    /**
     *
     * @param secureServiceEndPoint
     */
    public void setSecureServiceEndPoint(String secureServiceEndPoint) {
        this.secureServiceEndPoint = secureServiceEndPoint;
    }

    /**
     *
     * @return
     */
    public String getSecurityModuleName() {
        if (!validateStringField(securityModuleName)) {
            throw new NullPointerException("Security Module Name cannot be NULL or VOID!");
        }
        return securityModuleName;
    }

    /**
     *
     * @param securityModuleName
     */
    public void setSecurityModuleName(String securityModuleName) {
        this.securityModuleName = securityModuleName;
    }

    /**
     * 
     * @return
     */
    public boolean isStubCorrectlyInitialized() {
        return stubCorrectlyInitialized;
    }

    /**
     *
     * @param stubCorrectlyInitialized
     */
    public void setStubCorrectlyInitialized(boolean stubCorrectlyInitialized) {
        this.stubCorrectlyInitialized = stubCorrectlyInitialized;
    }

    /**
     *
     * @return
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     *
     * @param userEmail
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     *
     * @return
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     *
     * @param userPassword
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
