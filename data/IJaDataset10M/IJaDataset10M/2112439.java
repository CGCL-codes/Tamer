package org.esprit.ocm.model;

import java.io.Serializable;
import net.sf.gilead.pojo.java5.LightEntity;

public class AbstractAwsCredentials extends LightEntity implements Serializable {

    /**
	 *   
	 */
    private static final long serialVersionUID = 1196595979410834877L;

    private int idAwsCredentials;

    private String awsAccessKeyId;

    private String secretAccessKey;

    public AbstractAwsCredentials() {
    }

    public int getIdAwsCredentials() {
        return idAwsCredentials;
    }

    public void setIdAwsCredentials(int idAwsCredentials) {
        this.idAwsCredentials = idAwsCredentials;
    }

    public String getAwsAccessKeyId() {
        return awsAccessKeyId;
    }

    public void setAwsAccessKeyId(String awsAccessKeyId) {
        this.awsAccessKeyId = awsAccessKeyId;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }
}
