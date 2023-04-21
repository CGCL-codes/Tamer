package weibo4j.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import weibo4j.http.Response;
import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

/**
 * A data class representing Basic user information element
 */
public class User extends WeiboResponse implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -332738032648843482L;

    private String id;

    private String screenName;

    private String name;

    private int province;

    private int city;

    private String location;

    private String description;

    private String url;

    private String profileImageUrl;

    private String userDomain;

    private String gender;

    private int followersCount;

    private int friendsCount;

    private int statusesCount;

    private int favouritesCount;

    private Date createdAt;

    private boolean following;

    private boolean verified;

    private int verifiedType;

    private boolean allowAllActMsg;

    private boolean allowAllComment;

    private boolean followMe;

    private String avatarLarge;

    private int onlineStatus;

    private Status status = null;

    private int biFollowersCount;

    private String remark;

    private String lang;

    private String verifiedReason;

    public String getVerified_reason() {
        return verifiedReason;
    }

    public void setVerified_reason(String verifiedReason) {
        this.verifiedReason = verifiedReason;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setUserDomain(String userDomain) {
        this.userDomain = userDomain;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }

    public void setStatusesCount(int statusesCount) {
        this.statusesCount = statusesCount;
    }

    public void setFavouritesCount(int favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setVerifiedType(int verifiedType) {
        this.verifiedType = verifiedType;
    }

    public void setAllowAllActMsg(boolean allowAllActMsg) {
        this.allowAllActMsg = allowAllActMsg;
    }

    public void setAllowAllComment(boolean allowAllComment) {
        this.allowAllComment = allowAllComment;
    }

    public void setFollowMe(boolean followMe) {
        this.followMe = followMe;
    }

    public void setAvatarLarge(String avatarLarge) {
        this.avatarLarge = avatarLarge;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setBiFollowersCount(int biFollowersCount) {
        this.biFollowersCount = biFollowersCount;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public User() {
    }

    public User(JSONObject json) throws WeiboException {
        super();
        init(json);
    }

    private void init(JSONObject json) throws WeiboException {
        if (json != null) {
            try {
                id = json.getString("id");
                screenName = json.getString("screen_name");
                name = json.getString("name");
                province = json.getInt("province");
                city = json.getInt("city");
                location = json.getString("location");
                description = json.getString("description");
                url = json.getString("url");
                profileImageUrl = json.getString("profile_image_url");
                userDomain = json.getString("domain");
                gender = json.getString("gender");
                followersCount = json.getInt("followers_count");
                friendsCount = json.getInt("friends_count");
                favouritesCount = json.getInt("favourites_count");
                statusesCount = json.getInt("statuses_count");
                createdAt = parseDate(json.getString("created_at"), "EEE MMM dd HH:mm:ss z yyyy");
                following = getBoolean("following", json);
                verified = getBoolean("verified", json);
                verifiedType = json.getInt("verifiedType");
                allowAllActMsg = json.getBoolean("allowAllActMsg");
                allowAllComment = json.getBoolean("allowAllComment");
                followMe = json.getBoolean("followMe");
                avatarLarge = json.getString("avatarLarge");
                onlineStatus = json.getInt("onlineStatus");
                biFollowersCount = json.getInt("biFollowersCount");
                remark = json.getString("remark");
                lang = json.getString("lang");
                verifiedReason = json.getString("verified_reason");
                if (!json.isNull("status")) {
                    status = new Status(json.getJSONObject("status"));
                }
            } catch (JSONException jsone) {
                throw new WeiboException(jsone.getMessage() + ":" + json.toString(), jsone);
            }
        }
    }

    public static List<User> constructUser(Response res) throws WeiboException {
        JSONObject json = res.asJSONObject();
        JSONArray list = null;
        try {
            if (!json.isNull("users")) {
                list = json.getJSONArray("users");
            } else {
                list = res.asJSONArray();
            }
            int size = list.length();
            List<User> users = new ArrayList<User>(size);
            for (int i = 0; i < size; i++) {
                users.add(new User(list.getJSONObject(i)));
            }
            return users;
        } catch (JSONException je) {
            throw new WeiboException(je);
        }
    }

    public static String[] constructIds(Response res) throws WeiboException {
        try {
            JSONArray list = res.asJSONObject().getJSONArray("ids");
            String temp = list.toString().substring(1, list.toString().length() - 1);
            String[] ids = temp.split(",");
            return ids;
        } catch (JSONException jsone) {
            throw new WeiboException(jsone);
        } catch (WeiboException te) {
            throw te;
        }
    }

    public static List<User> constructUsers(Response res) throws WeiboException {
        try {
            JSONArray list = res.asJSONArray();
            int size = list.length();
            List<User> users = new ArrayList<User>(size);
            for (int i = 0; i < size; i++) {
                users.add(new User(list.getJSONObject(i)));
            }
            return users;
        } catch (JSONException jsone) {
            throw new WeiboException(jsone);
        } catch (WeiboException te) {
            throw te;
        }
    }

    /**
	 * 
	 * @param res
	 * @return
	 * @throws WeiboException
	 */
    public static UserWapper constructWapperUsers(Response res) throws WeiboException {
        JSONObject jsonUsers = res.asJSONObject();
        try {
            JSONArray user = jsonUsers.getJSONArray("users");
            int size = user.length();
            List<User> users = new ArrayList<User>(size);
            for (int i = 0; i < size; i++) {
                users.add(new User(user.getJSONObject(i)));
            }
            long previousCursor = jsonUsers.getLong("previous_curosr");
            long nextCursor = jsonUsers.getLong("next_cursor");
            if (nextCursor == -1) {
                nextCursor = jsonUsers.getLong("nextCursor");
            }
            return new UserWapper(users, previousCursor, nextCursor);
        } catch (JSONException jsone) {
            throw new WeiboException(jsone);
        }
    }

    /**
	 * @param res 
	 * @return 
	 * @throws WeiboException
	 */
    static List<User> constructResult(Response res) throws WeiboException {
        JSONArray list = res.asJSONArray();
        try {
            int size = list.length();
            List<User> users = new ArrayList<User>(size);
            for (int i = 0; i < size; i++) {
                users.add(new User(list.getJSONObject(i)));
            }
            return users;
        } catch (JSONException e) {
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getName() {
        return name;
    }

    public int getProvince() {
        return province;
    }

    public int getCity() {
        return city;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public URL getProfileImageURL() {
        try {
            return new URL(profileImageUrl);
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    public URL getURL() {
        try {
            return new URL(url);
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    public String getUserDomain() {
        return userDomain;
    }

    public String getGender() {
        return gender;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public boolean isFollowing() {
        return following;
    }

    public boolean isVerified() {
        return verified;
    }

    public int getverifiedType() {
        return verifiedType;
    }

    public boolean isallowAllActMsg() {
        return allowAllActMsg;
    }

    public boolean isallowAllComment() {
        return allowAllComment;
    }

    public boolean isfollowMe() {
        return followMe;
    }

    public String getavatarLarge() {
        return avatarLarge;
    }

    public int getonlineStatus() {
        return onlineStatus;
    }

    public Status getStatus() {
        return status;
    }

    public int getbiFollowersCount() {
        return biFollowersCount;
    }

    public String getRemark() {
        return remark;
    }

    public String getLang() {
        return lang;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "User [" + "id=" + id + ", screenName=" + screenName + ", name=" + name + ", province=" + province + ", city=" + city + ", location=" + location + ", description=" + description + ", url=" + url + ", profileImageUrl=" + profileImageUrl + ", userDomain=" + userDomain + ", gender=" + gender + ", followersCount=" + followersCount + ", friendsCount=" + friendsCount + ", statusesCount=" + statusesCount + ", favouritesCount=" + favouritesCount + ", createdAt=" + createdAt + ", following=" + following + ", verified=" + verified + ", verifiedType=" + verifiedType + ", allowAllActMsg=" + allowAllActMsg + ", allowAllComment=" + allowAllComment + ", followMe=" + followMe + ", avatarLarge=" + avatarLarge + ", onlineStatus=" + onlineStatus + ", status=" + status + ", biFollowersCount=" + biFollowersCount + ", remark=" + remark + ", lang=" + lang + ", verifiedReason=" + verifiedReason + "]";
    }
}
