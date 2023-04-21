package org.humboldt.cassia.core.jdo;

import java.io.Serializable;
import java.util.Date;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class OAIPMH implements Serializable {

    @PrimaryKey
    @Persistent
    Long id;

    @Persistent
    @Column(length = 20480000)
    String title;

    @Persistent
    @Column(length = 20480000)
    String creator;

    @Persistent
    @Column(length = 20480000)
    String subject;

    @Persistent
    @Column(length = 20480000)
    String description;

    @Persistent
    @Column(length = 20480000)
    String publisher;

    @Persistent
    @Column(length = 20480000)
    String contributor;

    @Persistent
    @Column(length = 20480000)
    String date;

    @Persistent
    @Column(length = 20480000)
    String type;

    @Persistent
    @Column(length = 20480000)
    String format;

    @Persistent
    String identifier;

    @Persistent
    @Column(length = 20480000)
    String source;

    @Persistent
    String languaje;

    @Persistent
    @Column(length = 20480000)
    String relation;

    @Persistent
    @Column(length = 20480000)
    String coverage;

    @Persistent
    @Column(length = 20480000)
    String rights;

    @Persistent
    Date timestamp = new Date();

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLanguaje() {
        return languaje;
    }

    public void setLanguaje(String languaje) {
        this.languaje = languaje;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    @Override
    public String toString() {
        return "OAIPMH [id=" + id + "\n title=" + title + "\n creator=" + creator + "\n subject=" + subject + "\n description=" + description + "\n publisher=" + publisher + "\n contributor=" + contributor + "\n date=" + date + "\n type=" + type + "\n format=" + format + "\n identifier=" + identifier + "\n source=" + source + "\n languaje=" + languaje + "\n relation=" + relation + "\n coverage=" + coverage + "\n rights=" + rights + "]";
    }
}
