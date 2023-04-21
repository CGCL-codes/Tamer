package com.leemba.monitor.server.objects.state;

import com.leemba.monitor.server.exceptions.ValidateException;
import java.util.ArrayList;
import java.util.List;
import javax.management.ObjectName;
import javax.management.remote.JMXServiceURL;
import org.terracotta.modules.annotations.AutolockRead;
import org.terracotta.modules.annotations.AutolockWrite;
import org.terracotta.modules.annotations.InstrumentedClass;

/**
 *
 * @author mrjohnson
 */
@InstrumentedClass
public class JMXService extends Service<JMXService> {

    private String url = null;

    private String objectName = null;

    private String username = null;

    private String password = null;

    private List<JMXAttribute> attributes = new ArrayList<JMXAttribute>();

    JMXService() {
        super.setAgentType(AgentType.JMX);
    }

    public static JMXService create() {
        return (JMXService) Service.createService(AgentType.JMX);
    }

    @AutolockWrite
    @Override
    public synchronized void update(JMXService other) throws ValidateException {
        super.update(other);
        this.url = other.url;
        this.objectName = other.objectName;
        this.username = other.username;
        this.password = other.password;
        this.attributes = other.attributes;
    }

    @AutolockRead
    @Override
    public synchronized JMXService validate() throws ValidateException {
        super.validate();
        if (this.url == null || this.url.trim().equals("")) throw new ValidateException("URL is required");
        if (this.objectName == null || this.objectName.trim().equals("")) throw new ValidateException("Object name is required");
        if (this.attributes == null) throw new ValidateException("Attributes list should not be null");
        try {
            new JMXServiceURL(this.url);
        } catch (Exception e) {
            throw new ValidateException("Invalid JMX URL", e);
        }
        try {
            new ObjectName(this.objectName);
        } catch (Exception e) {
            throw new ValidateException("Invalid object name", e);
        }
        return this;
    }

    @AutolockRead
    public synchronized List<JMXAttribute> getAttributes() {
        return new ArrayList<JMXAttribute>(attributes);
    }

    @AutolockWrite
    public synchronized void setAttributes(List<JMXAttribute> attributes) {
        this.attributes = attributes;
    }

    @AutolockRead
    public synchronized String getObjectName() {
        return objectName;
    }

    @AutolockWrite
    public synchronized void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    @AutolockRead
    public synchronized String getPassword() {
        return password;
    }

    @AutolockWrite
    public synchronized void setPassword(String password) {
        this.password = password;
    }

    @AutolockRead
    public synchronized String getUrl() {
        return url;
    }

    @AutolockWrite
    public synchronized void setUrl(String url) {
        this.url = url;
    }

    @AutolockRead
    public synchronized String getUsername() {
        return username;
    }

    @AutolockWrite
    public synchronized void setUsername(String username) {
        this.username = username;
    }
}
