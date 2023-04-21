package gu.client.model;

import java.util.List;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Story implements BaseObject {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    protected Long id;

    public String getId() {
        if (id == null) return null;
        return id.toString();
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Persistent
    private String title;

    @Persistent
    private String url;

    @Persistent
    private String description;

    @Persistent
    private String user_id;

    @Persistent
    private List<Long> digKeys;

    @NotPersistent
    private List<User> digs;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getDigs() {
        return digs;
    }

    public void setDigs(List<User> digs) {
        this.digs = digs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<Long> getDigKeys() {
        return digKeys;
    }

    public void setDigKeys(List<Long> digKeys) {
        this.digKeys = digKeys;
    }
}
