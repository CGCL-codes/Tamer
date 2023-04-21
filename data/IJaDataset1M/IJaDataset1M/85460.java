package com.itstherules.stream.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.blinkenlights.jid3.ID3Exception;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.v1.ID3V1Tag;
import org.blinkenlights.jid3.v1.ID3V1_0Tag;
import org.blinkenlights.jid3.v1.ID3V1Tag.Genre;

public class ParameterBasedMp3 implements IMp3 {

    private final Map<String, Object> parameters;

    public ParameterBasedMp3(Map<String, Object> parameters) {
        if (parameters == null) {
            parameters = new HashMap<String, Object>();
        }
        this.parameters = parameters;
    }

    public String getRootDirectory() {
        return oneUp(getSubDirectory());
    }

    public String getSubDirectory() {
        return oneUp(getFull());
    }

    private String oneUp(String path) {
        return new File(new File(path).getParent()).getName();
    }

    private String nullGuard(Object object) {
        if (object == null) return "";
        return (String) object;
    }

    public String getName() {
        return nullGuard(parameters.get("name"));
    }

    public String getAlbum() {
        return nullGuard(parameters.get("album"));
    }

    public String getArtist() {
        return nullGuard(parameters.get("artist"));
    }

    public String getComment() {
        return nullGuard(parameters.get("comment"));
    }

    public String getGenre() {
        return nullGuard(parameters.get("genre"));
    }

    public String getTitle() {
        return nullGuard(parameters.get("title"));
    }

    public String getYear() {
        return nullGuard(parameters.get("year"));
    }

    public String getFull() {
        return nullGuard(parameters.get("full"));
    }

    public void persist(String directory) {
        MP3File mp3File = new MP3File(new File(directory + getFull()));
        ID3V1Tag tag1 = null;
        try {
            tag1 = mp3File.getID3V1Tag();
            if (tag1 == null) {
                tag1 = new ID3V1_0Tag();
            }
        } catch (ID3Exception e) {
            tag1 = new ID3V1_0Tag();
        }
        if (!"".equals(getTitle())) tag1.setTitle(getTitle());
        tag1.setAlbum(getAlbum());
        tag1.setArtist(getArtist());
        tag1.setComment(getComment());
        try {
            tag1.setGenre(Genre.lookupGenre(getGenre()));
        } catch (ID3Exception e) {
        }
        tag1.setYear(getYear());
        mp3File.setID3Tag(tag1);
        try {
            mp3File.sync();
        } catch (ID3Exception e) {
        }
    }
}
