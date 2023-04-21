package com.gmail.huxiaowi.cblog.entity;

import java.io.Serializable;
import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;

/**
 * Class Comment
 * 
 *   留言评论POJO
 * 
 * @author  huxw
 * @version $Revision:1.0.0, $Date:2010-4-29 下午05:42:08$
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Comment implements Serializable {

    private static final long serialVersionUID = 2892716902611453351L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String name;

    @Persistent
    private String email;

    @Persistent
    private Link url;

    @Persistent
    private Date dateline;

    @Persistent
    private Text content;

    @Persistent
    private Long messageId;

    public Comment() {
        super();
    }

    public Comment(String name, String email, Link url, Date dateline, Text content) {
        super();
        this.name = name;
        this.email = email;
        this.url = url;
        this.dateline = dateline;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Link getUrl() {
        return url;
    }

    public void setUrl(Link url) {
        this.url = url;
    }

    public Date getDateline() {
        return dateline;
    }

    public void setDateline(Date dateline) {
        this.dateline = dateline;
    }

    public Text getContent() {
        return content;
    }

    public void setContent(Text content) {
        this.content = content;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
}
