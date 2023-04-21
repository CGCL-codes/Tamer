package de.linwave.music;

import java.util.ArrayList;
import java.util.List;

public class Band extends Artist {

    List<Artist> artists = new ArrayList<Artist>();

    int founded = 1960;

    public Band() {
    }

    public Band(String name) {
        super(name);
    }

    public List<Artist> getAritsts() {
        return artists;
    }

    public long getOID() {
        return OID;
    }

    public String _toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" [Band]").append(super.toString());
        for (Artist artist : artists) {
            sb.append(" artist=").append(artist);
        }
        return sb.toString();
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public int getFounded() {
        return founded;
    }

    public void setFounded(int founded) {
        this.founded = founded;
    }
}
