package com.incendiaryblue.appcomponents;

import com.incendiaryblue.appframework.AppComponentBase;
import com.incendiaryblue.appframework.ServerConfig;
import com.incendiaryblue.appframework.UserAppComponent;
import com.incendiaryblue.config.XMLConfigurable;
import com.incendiaryblue.config.XMLConfigurationException;
import com.incendiaryblue.config.XMLContext;
import org.w3c.dom.*;

/**
 * Represents the details of an FTP account.
 *
 * <p>Can be used in conjunciton with com.incendiaryblue.util.net.FTPFileSender to
 * send files around.</p>
 */
public class FTPAccount extends AppComponentBase implements UserAppComponent, XMLConfigurable {

    private String host;

    private String username;

    private String password;

    public String getHost() {
        return host;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public FTPAccount() {
        super(FTPAccount.class);
    }

    public FTPAccount(String host, String username, String password) {
        super(FTPAccount.class);
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public Object configure(Element element, XMLContext context) throws XMLConfigurationException {
        setName(element.getAttribute("name"));
        String hostProp = element.getAttribute("host");
        host = ServerConfig.get(hostProp);
        String userProp = element.getAttribute("username");
        username = ServerConfig.get(userProp);
        String passProp = element.getAttribute("password");
        password = ServerConfig.get(passProp);
        return this;
    }

    public void registerChild(Object child) {
    }
}
