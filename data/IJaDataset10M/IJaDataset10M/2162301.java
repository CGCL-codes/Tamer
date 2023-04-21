package com.columboid.protocol.syncml;

import java.security.NoSuchAlgorithmException;
import com.columboid.protocol.syncml.helper.EncryptionHelper;

public class ServerAuthentication implements Authentication {

    private CredentialType credentialType = CredentialType.MD5;

    public Boolean checkCredential(Credential credential, String hashCheckValue) throws NoSuchAlgorithmException {
        String hashResult = "";
        hashResult = EncryptionHelper.GetHashString(credential.getUserName(), credential.getHashPassword(), credential.getNonceToken(), credentialType);
        return hashCheckValue.equals(hashResult);
    }

    public void setCredentialType(CredentialType credentialType) {
        this.credentialType = credentialType;
    }

    public CredentialType getCredentialType() {
        return credentialType;
    }
}
