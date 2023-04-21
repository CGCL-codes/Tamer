package org.apache.tika.listener;

public interface DublinCoreListener {

    void setContributor(String contributor);

    void setCoverage(String coverage);

    void setCreator(String creator);

    void setDate(String data);

    void setDescription(String description);

    void setFormat(String format);

    void setIdentifier(String identifier);

    void setLanguage(String language);

    void setPublisher(String publisher);

    void setRelation(String relation);

    void setRights(String rights);

    void setSource(String source);

    void setSubject(String subject);

    void setTitle(String title);

    void setType(String type);
}
