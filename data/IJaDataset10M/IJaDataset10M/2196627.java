package com.luzan.app.map.daemon.search;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Documents")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Documents", propOrder = { "host", "count", "current", "lang", "type", "metaTags", "query", "equery", "docs" })
public class DocBeansList {

    @XmlElementRef
    List<DocumentBean> docs;

    @XmlElement(name = "host", required = false, nillable = false)
    String host;

    @XmlElement(name = "count", defaultValue = "0", required = true, nillable = false)
    String count;

    @XmlElement(name = "current", required = false, nillable = false)
    String current;

    @XmlElement(name = "lang", required = false, nillable = false)
    String lang;

    @XmlElement(name = "type", required = false, nillable = false)
    String type;

    @XmlElement(name = "metaTags", required = false, nillable = false)
    String metaTags;

    @XmlElement(name = "query", required = false, nillable = false)
    String query;

    @XmlElement(name = "equery", defaultValue = "", required = true, nillable = false)
    String equery;

    public DocBeansList() {
        count = "0";
    }

    public DocBeansList(final List<DocumentBean> docs, final long count) {
        this.docs = docs;
        this.count = String.valueOf(count);
        equery = "";
    }

    public List<DocumentBean> getDocs() {
        if (docs == null) return docs = new ArrayList<DocumentBean>();
        return docs;
    }

    public void setDocs(final List<DocumentBean> docs) {
        this.docs = docs;
    }

    public String getCount() {
        return count;
    }

    public void setCount(final String count) {
        this.count = count;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(final String lang) {
        this.lang = lang;
    }

    public String getMetaTags() {
        return metaTags;
    }

    public void setMetaTags(final String metaTags) {
        this.metaTags = metaTags;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(final String current) {
        this.current = current;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public String getEquery() {
        return equery;
    }

    public void setEquery(final String equery) {
        this.equery = equery;
    }
}
