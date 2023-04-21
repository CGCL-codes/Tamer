package edu.miami.cs.research.apg.agregator.ontology;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import com.sleepycat.persist.model.DeleteAction;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;
import edu.miami.cs.research.apg.generator.search.representations.FieldType;

/**
 * @author Darrius Serrant
 *
 */
@Entity
public class MusicArtist extends MusicData {

    /**
	 * @author Darrius Serrant
	 *
	 */
    public enum FieldNames {

        LASTFMTAGS("LastFMTags", FieldType.NOMINAL), MUSICARTISTID("MusicArtistID", FieldType.NUMERICAL), ARTIST("Artist", FieldType.NUMERICAL), NAME("Name", FieldType.NOMINAL), SONGTRACKS("SongTracks", FieldType.NOMINAL), ECHONESTID("EchoNestID", FieldType.NOMINAL), FAMILIARITY("Familiarity", FieldType.NUMERICAL), HOTTTNESSS("Hotttnesss", FieldType.NUMERICAL), LOCATION("Location", FieldType.NOMINAL), MUSICALBUMS("MusicAlbums", FieldType.NUMERICAL), SIMILARARTISTS("SimilarArtists", FieldType.NUMERICAL), STYLE("Style", FieldType.NOMINAL), MOOD("Mood", FieldType.NOMINAL), YEARSACTIVE("YearsActive", FieldType.NUMERICAL), TOPFIFTEENSONGS("TopFifteenSongs", FieldType.NOMINAL);

        private FieldNames(String fieldName, FieldType type) {
            this.fieldName = fieldName;
            this.fieldType = type;
        }

        public String toString() {
            return fieldName;
        }

        /**
	 * @param fieldType the fieldType to set
	 */
        public FieldType getFieldType() {
            return fieldType;
        }

        private String fieldName;

        private FieldType fieldType;
    }

    @PrimaryKey
    private long musicArtistID;

    @SecondaryKey(relate = Relationship.ONE_TO_ONE)
    private String echoNestID;

    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private String name;

    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private String location;

    private double latitude;

    private double longtitude;

    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private double familiarity;

    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private double hotttness;

    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private int[] yearsActive;

    @SecondaryKey(relate = Relationship.MANY_TO_MANY)
    private ArrayList<String> topFifteenSongs;

    @SecondaryKey(relate = Relationship.MANY_TO_MANY)
    private ArrayList<String> similarArtists;

    @SecondaryKey(relate = Relationship.MANY_TO_MANY, relatedEntity = SongTrack.class, onRelatedEntityDelete = DeleteAction.NULLIFY)
    private ArrayList<String> songTracks;

    @SecondaryKey(relate = Relationship.ONE_TO_MANY, relatedEntity = MusicAlbum.class, onRelatedEntityDelete = DeleteAction.NULLIFY)
    private ArrayList<Long> musicAlbums;

    @SecondaryKey(relate = Relationship.MANY_TO_MANY)
    private ArrayList<String> mood;

    @SecondaryKey(relate = Relationship.MANY_TO_MANY)
    private ArrayList<String> style;

    @SecondaryKey(relate = Relationship.MANY_TO_MANY)
    private ArrayList<String> lastFMTags;

    /**
	 * 
	 */
    public MusicArtist() {
        setName(null);
        setLocation(null);
        setFamiliarity(0);
        setHotttness(0);
        setYearsActive(null);
        setTopFifteenSongs(new ArrayList<String>());
        setSimilarArtists(new ArrayList<String>());
    }

    public MusicArtist(String name, String location, int familiarity, int hotttness, int[] yearsActive, ArrayList<String> topFifteenSongs, ArrayList<String> similiarArtists, ArrayList<String> songTracks) {
        this.setName(name);
        this.setLocation(location);
        this.setFamiliarity(familiarity);
        this.setHotttness(hotttness);
        this.setYearsActive(yearsActive);
        this.setTopFifteenSongs(topFifteenSongs);
        this.setSimilarArtists(similiarArtists);
        this.setSongTracks(songTracks);
    }

    public long getMusicArtistID() {
        return musicArtistID;
    }

    public void setMusicArtistID(long musicArtistID) {
        this.musicArtistID = musicArtistID;
    }

    public String getEchoNestID() {
        return echoNestID;
    }

    public void setEchoNestID(String echoNestID) {
        this.echoNestID = echoNestID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getFamiliarity() {
        return familiarity;
    }

    public void setFamiliarity(double d) {
        this.familiarity = d;
    }

    public double getHotttness() {
        return hotttness;
    }

    public void setHotttness(double hotttness) {
        this.hotttness = hotttness;
    }

    public int[] getYearsActive() {
        return yearsActive;
    }

    public void setYearsActive(int[] yearsActive) {
        this.yearsActive = yearsActive;
    }

    public ArrayList<String> getTopFifteenSongs() {
        return topFifteenSongs;
    }

    public void setTopFifteenSongs(ArrayList<String> topFifteenSongs) {
        this.topFifteenSongs = topFifteenSongs;
    }

    public ArrayList<String> getSimilarArtists() {
        return similarArtists;
    }

    public void setSimilarArtists(ArrayList<String> similarArtists) {
        this.similarArtists = similarArtists;
    }

    public ArrayList<String> getSongTracks() {
        return songTracks;
    }

    public ArrayList<Long> getMusicAlbums() {
        return musicAlbums;
    }

    public void setMusicAlbums(ArrayList<Long> musicAlbums) {
        this.musicAlbums = musicAlbums;
    }

    public void setSongTracks(ArrayList<String> songTracks) {
        this.songTracks = songTracks;
    }

    public ArrayList<String> getMood() {
        return mood;
    }

    public void setMood(ArrayList<String> mood) {
        this.mood = mood;
    }

    public ArrayList<String> getLastFMTags() {
        return lastFMTags;
    }

    public ArrayList<String> getStyle() {
        return style;
    }

    public void setStyle(ArrayList<String> style) {
        this.style = style;
    }

    @Override
    public void setLastFMTags(Collection<String> socialTags) {
        lastFMTags = (ArrayList<String>) socialTags;
    }

    public void setLastFMTags(ArrayList<String> lastFMTags) {
        this.lastFMTags = lastFMTags;
    }

    public Properties getDifferences(MusicArtist artist) {
        Properties differences = new Properties();
        if (artist.getFamiliarity() != familiarity) {
            differences.put("familiarity", familiarity);
        }
        if (artist.getHotttness() != hotttness) {
            differences.put("hotttness", hotttness);
        }
        if (artist.getLocation().equals(location)) {
            differences.put("location", location);
        }
        if (artist.getMusicArtistID() != musicArtistID) {
            differences.put("musicArtistID", musicArtistID);
        }
        if (!artist.getName().equals(name)) {
            differences.put("name", name);
        }
        if (!artist.getSimilarArtists().equals(similarArtists)) {
            differences.put("similarArtists", similarArtists);
        }
        if (!artist.getSongTracks().equals(songTracks)) {
            differences.put("songTracks", songTracks);
        }
        if (!artist.getTopFifteenSongs().equals(topFifteenSongs)) {
            differences.put("topFifteenSongs", topFifteenSongs);
        }
        if (!artist.getYearsActive().equals(yearsActive)) {
            differences.put("yearsActive", yearsActive);
        }
        return differences;
    }
}
