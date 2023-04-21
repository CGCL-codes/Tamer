package org.jhrh.seamless.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SW_GROUP")
public class SwGroupEB {

    @Id
    private Long id;

    private String groupName;

    private String mailAddress;

    private String descriptionMsgKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getDescriptionMsgKey() {
        return descriptionMsgKey;
    }

    public void setDescriptionMsgKey(String descriptionMsgKey) {
        this.descriptionMsgKey = descriptionMsgKey;
    }
}
