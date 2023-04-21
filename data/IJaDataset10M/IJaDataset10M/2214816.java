package com.ekeyman.service.impl;

import com.ekeyman.service.EkeymanService;
import com.ekeymanlib.business.KeyManager;
import com.ekeymanlib.domain.User;
import com.ekeymanlib.domain.Vendor;
import com.ekeymanlib.dto.AppDeviceApiKeys;
import com.ekeymanlib.dto.EncryptionKeys;
import com.ekeymanlib.dto.VendorProfile;

public class EkeymanServiceImpl implements EkeymanService {

    public EkeymanServiceImpl() {
    }

    private KeyManager keyManager;

    public EncryptionKeys createEncryptionKeys(String resourceUri, String publicKey, int keyBytesLength, int ivBytesLength, int saltBytesLength, int iterationCountMax, String location, String digitalSignature) {
        return getKeyManager().createEncryptionKeys(resourceUri, publicKey, keyBytesLength, ivBytesLength, saltBytesLength, iterationCountMax, location, digitalSignature);
    }

    public EncryptionKeys getEncryptionKeys(String resourceUri, String publicKey, String location, String digitalSignature) {
        return getKeyManager().getEncryptionKeys(resourceUri, publicKey, location, digitalSignature);
    }

    public void deleteEncryptionKeys(String resourceUri, String publicKey, String location, String digitalSignature) {
        getKeyManager().deleteEncryptionKeys(resourceUri, publicKey, location, digitalSignature);
    }

    public AppDeviceApiKeys registerAppDevice(String apiKey, String application, String device) {
        return getKeyManager().registerAppDevice(apiKey, application, device);
    }

    public AppDeviceApiKeys generateNewPrivateKey(String publicKey, String apiKey) {
        return getKeyManager().generateNewPrivateKey(publicKey, apiKey);
    }

    public void updateVendor(String apiKey, String name, String address, String city, String state, String zip, String phone, String emailAddress) {
        getKeyManager().updateVendor(apiKey, name, address, city, state, zip, phone, emailAddress);
    }

    public void deleteVendor(String username) {
        getKeyManager().deleteVendor(username);
    }

    public VendorProfile getVendorProfile(String apiKey) {
        return getKeyManager().getVendorProfile(apiKey);
    }

    public void setKeyManager(KeyManager keyManager) {
        this.keyManager = keyManager;
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }

    public void deleteUser(String openidurl) {
        getKeyManager().deleteUser(openidurl);
    }

    public String getUser(String openidurl) {
        String apiKey = "";
        User user = getKeyManager().getUser(openidurl);
        if (user != null) {
            Vendor vendor = user.getVendor();
            if (vendor != null) {
                apiKey = vendor.getApiKey();
            }
        }
        return apiKey;
    }

    public void registerUser(String apiKey, String openidurl) {
        getKeyManager().registerUser(apiKey, openidurl);
    }

    public String registerVendor(String name, String address, String city, String state, String zip, String phone, String emailAddress) {
        return getKeyManager().registerVendor(name, address, city, state, zip, phone, emailAddress);
    }
}
