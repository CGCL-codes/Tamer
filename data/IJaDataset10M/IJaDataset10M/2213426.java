package kite.sample02.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @version $Id: Message.java 85 2010-08-02 06:33:16Z williewheeler $
 * @author Willie Wheeler
 * @since 1.0
 */
@Entity
@Table(name = "message")
@NamedQueries({ @NamedQuery(name = "findMotd", query = "from Message where type = 'motd'"), @NamedQuery(name = "findImportantMessages", query = "from Message where type = 'important' order by dateCreated") })
public class Message {

    private Long id;

    private String type;

    private Date dateCreated;

    private String text;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }

    @Column(name = "message_type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "date_created")
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Column
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
