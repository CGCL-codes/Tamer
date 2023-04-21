package com.apps.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import org.hibernate.annotations.Index;

@Entity
public class CrawlerURL extends Identity {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Column(length = 512)
    private String url;

    @Index(name = "CrawlerURL_md5", columnNames = { "md5" })
    private String md5;

    private int level;

    private int status;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
