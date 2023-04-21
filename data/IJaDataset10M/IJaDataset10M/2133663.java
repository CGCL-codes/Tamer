package com.strongauth.cloud.valueobject;

/**
 * This is a value object that represents a cloud account. It 
 * encapsulates credentials and cloud related information
 * specific to the cloud account.
 * 
 *  
 */
public class CloudAuth {

    private String cloudName = "";

    private String cloudStore = "";

    private String accessKey = "";

    private String secretKey = "";

    private String cloudContainer = "";

    private boolean prompt = true;

    public CloudAuth() {
    }

    public CloudAuth(boolean promptLogin) {
        this.prompt = promptLogin;
    }

    /**
     * Constructs a CloudAuth Bean to represent the cloud account with the
     * supplied accesskey and secret key.
     * @param accessKey The access key for the cloud instance.
     * @param secretKey The secret access key for the cloud instance.
     */
    public CloudAuth(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * Constructs a CloudAuth Bean to represent the cloud account with the
     * supplied accesskey and secret key.
     * @param cloudName The name of this cloud account.
     * @param accessKey The access key for the cloud instance.
     * @param secretKey The secret access key for the cloud instance.
     */
    public CloudAuth(String cloudName, String accessKey, String secretKey) {
        this.cloudName = cloudName;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * Constructs a CloudAuth Bean to represent the cloud account with the
     * supplied attributes.
     * @param cloudStore The type of the cloud (such as AWS, Eucalyptus, etc).
     * @param cloudName The name of the cloud account.
     * @param accessKey The access key for the cloud instance.
     * @param secretKey The secret access key for the cloud instance.
     * @param cloudContainer The name of the bucket that is active during any crypto operation.
     * @param promptLogin true if the attributes need to be cleared after use.
     */
    public CloudAuth(String cloudStore, String cloudName, String accessKey, String secretKey, String cloudContainer, boolean promptLogin) {
        this.cloudStore = cloudStore;
        this.cloudName = cloudName;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.cloudContainer = cloudContainer;
        this.prompt = promptLogin;
    }

    /**
     * Constructs a CloudAuth Bean to represent the cloud account with the
     * supplied attributes.
     * @param cloudStore The type of the cloud (such as AWS, Eucalyptus, etc).
     * @param accessKey The access key for the cloud instance.
     * @param secretKey The secret access key for the cloud instance.
     * @param cloudContainer The name of the bucket that is active during any crypto operation.
     * @param promptLogin true if the attributes need to be cleared after use.
     */
    public CloudAuth(String cloudStore, String accessKey, String secretKey, String cloudContainer, boolean promptLogin) {
        this.cloudStore = cloudStore;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.cloudContainer = cloudContainer;
        this.prompt = promptLogin;
    }

    public void setPromptLogin(boolean promptLogin) {
        this.prompt = promptLogin;
    }

    public void setCloudStore(String cloudStore) {
        this.cloudStore = cloudStore;
    }

    public void setCloudName(String cloudName) {
        this.cloudName = cloudName;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setContainer(String cloudContainer) {
        this.cloudContainer = cloudContainer;
    }

    public boolean promptLogin() {
        return prompt;
    }

    public String getCloudStore() {
        return cloudStore;
    }

    public String getCloudName() {
        return cloudName;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getContainer() {
        return cloudContainer;
    }
}
