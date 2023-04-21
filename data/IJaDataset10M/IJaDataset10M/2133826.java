package fr.biojee.discographie.album.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import fr.biojee.discographie.artist.entity.Artist;
import fr.biojee.discographie.style.entity.Style;

@Entity
@Table(name = "ALBUM")
public class Album implements Serializable {

    private Integer idAlbum;

    private Artist artist;

    private String title;

    private int CreationDate;

    private Style style;

    @Id
    @GeneratedValue(generator = "increment")
    @Column(name = "ID_ALBUM")
    @OneToMany(mappedBy = "song")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Integer getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(Integer idAlbum) {
        this.idAlbum = idAlbum;
    }

    @Basic(fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name = "ID_ARTIST", nullable = true)
    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    @Column(name = "TITLE")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "CREATION_DATE")
    public int getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(int creationDate) {
        CreationDate = creationDate;
    }

    @Basic(fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name = "STYLE_ID", nullable = true)
    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }
}
