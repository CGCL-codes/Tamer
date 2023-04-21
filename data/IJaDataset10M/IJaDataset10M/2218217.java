package com.thornapple.setmanager;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Bill
 */
@Entity
@Table(name = "SONG")
public class Song implements java.io.Serializable, Comparable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    private String name;

    @Lob
    private String tablature;

    @ManyToOne
    private Artist artist;

    @Column(name = "CGR_ID")
    private String cgrID;

    public Song() {
    }

    public Song(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getTablature() {
        return tablature;
    }

    public void setTablature(String tablature) {
        this.tablature = tablature;
    }

    public String toString() {
        return this.name;
    }

    public void setCgrID(String cgrID) {
        this.cgrID = cgrID;
    }

    public String getCgrID() {
        return cgrID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int compareTo(Object o) {
        if (o instanceof Song) {
            Song a = (Song) o;
            if (getName() != null) {
                return getName().compareTo(a.getName());
            } else if (a.getName() != null) {
                return -1;
            } else return 0;
        } else return -1;
    }
}
