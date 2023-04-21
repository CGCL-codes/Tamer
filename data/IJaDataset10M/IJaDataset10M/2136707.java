package com.nubotech.gwt.oss.client.auth;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.nubotech.gwt.oss.client.AccessControlPolicy;
import com.nubotech.gwt.oss.client.Bucket;
import com.nubotech.gwt.oss.client.ObjectHolder;
import com.nubotech.gwt.oss.client.OnlineStorageService;
import com.nubotech.gwt.oss.client.OnlineStorageServiceFactory;
import com.nubotech.gwt.oss.client.util.AesCipher;
import com.nubotech.gwt.oss.client.util.LogUtil;
import com.nubotech.gwt.oss.client.util.Sha1;

/**
 * 
 * manager used to store and get credentials. Credentials can be stored locally using Google Gears or remotely.
 * @author jonnakkerud
 */
public class AuthenticationManager {

    public enum Storage {

        NONE, LOCAL, REMOTE
    }

    private static AuthenticationManager instance = new AuthenticationManager();

    private Credential login;

    private String prefix = "oss";

    private AuthenticationManager() {
    }

    public static AuthenticationManager instance() {
        return instance;
    }

    public void update(String email, String pass, String account, String key) {
        Credential c = new Credential();
        c.setEmail(email);
        c.setPass(pass);
        c.setAccount(account);
        c.setSecretKey(key);
        update(email, pass, account, key, Storage.NONE);
    }

    public void update(String email, String pass, String account, String key, Storage storageType) {
        Credential c = new Credential();
        c.setEmail(email);
        c.setPass(pass);
        c.setAccount(account);
        c.setSecretKey(key);
        update(c, storageType);
    }

    public void update(Credential c, Storage storageType) {
        switch(storageType) {
            case NONE:
                updateImpl(c);
                break;
            case REMOTE:
                updateRemoteImpl(c);
                break;
        }
    }

    public Credential getLogin() {
        return login;
    }

    public boolean isLogin() {
        return (login != null);
    }

    public void login(String email, String pass, AuthenticationListener listener) {
        login(email, pass, (credentialIsLocal() ? Storage.LOCAL : Storage.REMOTE), listener);
    }

    public void login(String email, String pass, Storage storageType, AuthenticationListener listener) {
        Credential c = getLogin();
        if (c != null) {
            listener.loginSuccess(c);
            return;
        }
        switch(storageType) {
            case NONE:
                break;
            case REMOTE:
                processCredentialRemote(email, pass, listener);
                break;
        }
    }

    public void loginRemote(String email, String pass, String bucket, String credential, AuthenticationListener listener) {
        Credential c = getLogin();
        if (c != null) {
            listener.loginSuccess(c);
            return;
        }
        processCredentialRemote(email, pass, listener, bucket, credential);
    }

    public void logout() {
        login = null;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private void updateImpl(Credential c) {
        this.login = c;
    }

    private void updateRemoteImpl(Credential c) {
        OnlineStorageService service = OnlineStorageServiceFactory.getService(c);
        service.beginBatch();
        String bucketName = prefix + "-" + Sha1.hex_sha1(Window.Location.getHostName());
        service.createBucket(bucketName);
        String keyName = Sha1.hex_sha1(c.getEmail() + c.getPass()) + "/" + getPrefix() + ".credentials";
        String content = c.getAccount() + "," + c.getSecretKey();
        String key = c.getEmail() + c.getPass();
        ObjectHolder holder = new ObjectHolder();
        holder.setBucket(new Bucket(bucketName));
        holder.setKey(keyName);
        holder.setAccessPolicy(AccessControlPolicy.PUBLIC_READ);
        AesCipher cipher = new AesCipher(key);
        String iv = cipher.generateSharedKey();
        String cipherText = cipher.encrypt(content);
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"a\":").append('"').append(cipherText).append('"').append(",");
        sb.append("\"b\":").append('"').append(iv).append('"').append(",");
        sb.append("\"c\":").append(cipher.getOriginalSize());
        sb.append("}");
        holder.setData(sb.toString());
        service.putObject(holder, null);
        service.commitBatch();
        Cookies.setCookie("oss-storage", "2");
    }

    private String encrypt(String key, String clearText) {
        AesCipher cipher = new AesCipher(key, AesCipher.Mode.OFB);
        return cipher.encrypt(clearText);
    }

    private String decrypt(String key, String cipherText) {
        AesCipher cipher = new AesCipher(key, AesCipher.Mode.OFB);
        return cipher.decrypt(cipherText);
    }

    private void processCredentialRemote(final String email, final String pass, final AuthenticationListener listener) {
        processCredentialRemote(email, pass, listener, null, null);
    }

    private void processCredentialRemote(final String email, final String pass, final AuthenticationListener listener, String bucket, String credential) {
        Credential c = new Credential();
        c.setAnonymous(true);
        OnlineStorageService service = OnlineStorageServiceFactory.getService(c);
        String bucketName = null;
        if (bucket != null) {
            bucketName = bucket;
        } else {
            bucketName = getPrefix() + "-" + Sha1.hex_sha1(Window.Location.getHostName());
        }
        String credentialKey = null;
        if (credential != null) {
            credentialKey = credential;
        } else {
            credentialKey = Sha1.hex_sha1(email + pass) + "/" + getPrefix() + ".credentials";
        }
        StringBuilder url = new StringBuilder();
        url.append("http://").append(service.getHost()).append("/").append(bucketName);
        url.append("/").append(credentialKey);
        RequestBuilder request = new RequestBuilder(RequestBuilder.GET, url.toString());
        request.setCallback(new RequestCallback() {

            public void onResponseReceived(Request req, Response res) {
                try {
                    Credential credential = createCredentialFromJson(email, pass, res.getText());
                    validateLogin(email, pass, credential);
                } catch (Exception ex) {
                    login = null;
                    listener.loginFail(ex.getMessage());
                }
                if (login != null) {
                    listener.loginSuccess(login);
                }
            }

            public void onError(Request req, Throwable th) {
                login = null;
                listener.loginFail(th.getMessage());
            }
        });
        try {
            request.send();
        } catch (RequestException ex) {
            login = null;
            listener.loginFail(ex.getMessage());
        }
    }

    public Credential createCredentialFromJson(String email, String pass, String json) {
        JSONObject jo = (JSONObject) JSONParser.parse(json);
        JSONString content = (JSONString) jo.get("a");
        JSONString iv = (JSONString) jo.get("b");
        JSONNumber origLen = (JSONNumber) jo.get("c");
        String key = email + pass;
        AesCipher cipher = new AesCipher(key);
        cipher.setHexIv(iv.stringValue());
        cipher.setOriginalSize(new Double(origLen.doubleValue()).intValue());
        String s = cipher.decrypt(content.stringValue());
        String[] arS = s.split(",");
        Credential cred = new Credential();
        cred.setEmail(email);
        cred.setAccount(arS[0]);
        cred.setSecretKey(arS[1]);
        cred.setPass(Sha1.hex_sha1(cred.getAccount() + pass));
        return cred;
    }

    private void validateLogin(String email, String pass, Credential c) throws AuthenticationException {
        if (c == null) throw new AuthenticationException("Email or password not found");
        String hash = Sha1.hex_sha1(c.getAccount() + pass);
        if (hash.equals(c.getPass()) == false) {
            throw new AuthenticationException("Password is incorrect");
        }
        login = c;
    }

    private boolean credentialIsLocal() {
        boolean retval = true;
        String s = Cookies.getCookie("oss-storage");
        if (s != null && Integer.parseInt(s) != 1) {
            retval = false;
        }
        return retval;
    }
}
