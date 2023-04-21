package crawler.dto;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySpaceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String profileType;

    private String id;

    private String city;

    private String country;

    private String state;

    private String lastLogin;

    private String profileName;

    private String countFriends;

    private String countComments;

    private String headline;

    private List<CommentDTO> comments;

    private List<String> friends;

    private ProfileDTO profile;

    private BandDTO band;

    public MySpaceDTO() {
        countFriends = "0";
        countComments = "0";
    }

    public MySpaceDTO(String profileType, String id, String city, String country, String state, String lastLogin, String profileName, String url, String countFriends, String countComments, String headline) {
        super();
        this.profileType = profileType;
        this.id = id;
        this.city = city;
        this.country = country;
        this.state = state;
        this.lastLogin = lastLogin;
        this.profileName = profileName;
        this.countFriends = countFriends;
        this.countComments = countComments;
        this.headline = headline;
    }

    public MySpaceDTO(ResultSet rset) {
        try {
            this.setCity(rset.getString("city"));
            this.setCountFriends(rset.getString("countFriends"));
            this.setCountFriends(rset.getString("countComments"));
            this.setCountry(rset.getString("country"));
            this.setId(rset.getString("id"));
            this.setLastLogin(rset.getString("lastLogin"));
            this.setProfileName(rset.getString("profileName"));
            this.setProfileType(rset.getString("profileType"));
            this.setState(rset.getString("state"));
            this.setHeadline(rset.getString("headline"));
        } catch (SQLException e) {
            System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
        }
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public ProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileDTO profile) {
        this.profile = profile;
    }

    public BandDTO getBand() {
        return band;
    }

    public void setBand(BandDTO band) {
        this.band = band;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public String getCountFriends() {
        return countFriends;
    }

    public void setCountFriends(String countFriends) {
        this.countFriends = countFriends;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getCountComments() {
        return countComments;
    }

    public void setCountComments(String countComments) {
        this.countComments = countComments;
    }
}
