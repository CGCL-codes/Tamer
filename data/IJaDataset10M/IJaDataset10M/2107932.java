package com.web.music.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Unused
 */
@Entity
public class TrackContent {

    @Id
    @GeneratedValue
    public long id;

    @Column(columnDefinition = "LONGBLOB")
    private byte[] content;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
