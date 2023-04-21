package org.fao.fenix.communication.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CommunicationResource {

    @Id
    @GeneratedValue
    private long id;

    private String type;

    private String title;

    private String digest;

    private String host;

    private String hostLabel;

    private String oGroup;

    private String privilege;

    public CommunicationResource() {
    }

    public CommunicationResource(long id, String title, String digest, String host, String group, String privilege) {
        super();
        this.id = id;
        this.title = title;
        this.digest = digest;
        this.host = host;
        this.oGroup = group;
        this.privilege = privilege;
    }

    public CommunicationResource(long id, String title, String digest, String host, String hostLabel, String group, String privilege) {
        super();
        this.id = id;
        this.title = title;
        this.digest = digest;
        this.host = host;
        this.hostLabel = hostLabel;
        this.oGroup = group;
        this.privilege = privilege;
    }

    public CommunicationResource(long id, String type, String title, String digest, String host, String hostLabel, String group, String privilege) {
        super();
        this.id = id;
        this.type = type;
        this.title = title;
        this.digest = digest;
        this.host = host;
        this.hostLabel = hostLabel;
        this.oGroup = group;
        this.privilege = privilege;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getOGroup() {
        return oGroup;
    }

    public void setOGroup(String ogroup) {
        this.oGroup = ogroup;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHostLabel() {
        return hostLabel;
    }

    public void setHostLabel(String hostLabel) {
        this.hostLabel = hostLabel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
