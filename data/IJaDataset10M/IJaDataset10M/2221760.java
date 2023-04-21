package com.inorout.android.gae.model.musicFromServerDB;

import java.io.Serializable;

public class MusicTop implements Serializable {

    private static final long serialVersionUID = 7390103290165670089L;

    private Long id;

    private Integer classement;

    private String artiste;

    private String album;

    private String titre;

    public MusicTop() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getClassement() {
        return classement;
    }

    public void setClassement(Integer classement) {
        this.classement = classement;
    }

    public String getArtiste() {
        return artiste;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
}
