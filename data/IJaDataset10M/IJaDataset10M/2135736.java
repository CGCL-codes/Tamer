package musicSales;

import java.util.LinkedList;
import java.util.Date;
import java.io.Serializable;

public class Artist implements Serializable {

    private String name;

    private String nationality;

    private String recordCo;

    private String artistId;

    private String type;

    private LinkedList<Single> sReleases;

    private LinkedList<Album> aReleases;

    public Artist(String name, String nationality, String recordCo, String artistId, String type) {
        this.name = name;
        this.nationality = nationality;
        this.recordCo = recordCo;
        this.artistId = artistId;
        this.type = type;
    }

    public Artist() {
        name = null;
        nationality = null;
        recordCo = null;
        artistId = null;
        type = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getRecordCo() {
        return recordCo;
    }

    public void setRecordCo(String recordCo) {
        this.recordCo = recordCo;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addSRelease(Single release) {
        sReleases.add(release);
    }

    public Single getSRelease(int index) {
        return sReleases.get(index);
    }

    public void addARelease(Album release) {
        aReleases.add(release);
    }

    public Album getARelease(int index) {
        return aReleases.get(index);
    }
}
