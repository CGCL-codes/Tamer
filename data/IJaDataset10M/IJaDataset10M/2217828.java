package com.xdatasystem.contactsimporter.sohu;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import com.xdatasystem.contactsimporter.AuthenticationException;
import com.xdatasystem.contactsimporter.C;
import com.xdatasystem.contactsimporter.ContactImpl;
import com.xdatasystem.contactsimporter.ContactListImporterException;
import com.xdatasystem.contactsimporter.ContactListImporterImpl;
import com.xdatasystem.user.Contact;

public class SohuImporter extends ContactListImporterImpl {

    public SohuImporter(String username, String password) {
        super(username, password);
    }

    @Override
    public String getContactListURL() {
        return "http://mail.sohu.com/address/list";
    }

    @Override
    public String getLoginURL() {
        return C.SOHU_LOGIN_ADDRESS;
    }

    @Override
    protected void login(DefaultHttpClient client) throws Exception {
        long time = System.currentTimeMillis();
        BasicClientCookie cookie = new BasicClientCookie("sohu_LOGIN", (new StringBuilder("T")).append(time).append("/").append(time - 16L).append("/").append(time).toString());
        client.getCookieStore().addCookie(cookie);
        String loginPageUrl = getLoginURL().toString();
        getLogger().info("Requesting login page");
        NameValuePair data[] = { new BasicNameValuePair("userid", this.getUsername()), new BasicNameValuePair("password", MD5_Simple.MD5(this.getPassword())), new BasicNameValuePair("appid", "1000"), new BasicNameValuePair("persistentcookie", "0"), new BasicNameValuePair("w", "1280"), new BasicNameValuePair("pwdtype", "1") };
        String content = readInputStream(doPost(client, loginPageUrl, data, ""), C.SOHU_ENCODE);
        if (!content.contains("success")) {
            getLogger().info("Login failed, username or  password do not match please check your name&password");
            throw new AuthenticationException("Login failed");
        }
    }

    @Override
    protected List parseContacts(InputStream inputstream) throws Exception {
        List<Contact> contactList = new ArrayList();
        String source = readInputStream(inputstream, C.SOHU_ENCODE);
        JSONObject object = new JSONObject(source);
        JSONArray contacts = object.getJSONArray("contact");
        for (int i = 0; i < contacts.length(); i++) {
            JSONObject contactInfo = contacts.getJSONObject(i);
            String name = contactInfo.getString("nickname");
            String email = contactInfo.getString("email");
            contactList.add(new ContactImpl(name, email));
        }
        return contactList;
    }

    public static boolean isSohu(String email) {
        return email.indexOf("@sohu.") != -1;
    }
}
