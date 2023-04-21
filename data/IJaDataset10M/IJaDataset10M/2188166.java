package cn.chengdu.in.parser;

import org.json.JSONException;
import org.json.JSONObject;
import cn.chengdu.in.type.User;

/**
 * @author Declan.Z(declan.zhang@gmail.com)
 * @date 2011-2-16
 */
public class UserParser extends AbstractParser<User> {

    @Override
    public User parse(JSONObject json) throws JSONException {
        if (json.has("user")) {
            return parse(json.getJSONObject("user"));
        }
        User obj = new User();
        if (json.has("userId")) {
            obj.setId(json.getString("userId"));
        }
        if (json.has("uid")) {
            obj.setId(json.getString("uid"));
        }
        if (json.has("id")) {
            obj.setId(json.getString("id"));
        }
        if (json.has("username")) {
            obj.setUsername(json.getString("username"));
        }
        if (json.has("avatarUri")) {
            obj.setAvatarUri(json.getString("avatarUri"));
        }
        if (json.has("isFollowing")) {
            obj.setFollowing(json.getInt("isFollowing") == 1);
        }
        if (json.has("weeklyRank")) {
            obj.setWeeklyRank(json.getInt("weeklyRank"));
        }
        if (json.has("checkinCount")) {
            obj.setCheckinCount(json.getInt("checkinCount"));
        }
        if (json.has("badgeCount")) {
            obj.setBadgeCount(json.getInt("badgeCount"));
        }
        if (json.has("mayorshipCount")) {
            obj.setMayotshipCount(json.getInt("mayorshipCount"));
        }
        if (json.has("fanCount")) {
            obj.setFanCount(json.getInt("fanCount"));
        }
        if (json.has("followingCount")) {
            obj.setFollowingCount(json.getInt("followingCount"));
        }
        if (json.has("tipCount")) {
            obj.setTipCount(json.getInt("tipCount"));
        }
        if (json.has("likedCount")) {
            obj.setLikedCount(json.getInt("likedCount"));
        }
        if (json.has("point")) {
            obj.setPoint(json.getInt("point"));
        }
        if (json.has("lastRank")) {
            obj.setRank(json.getString("lastRank"));
        }
        if (json.has("statusMsg")) {
            obj.setStatusMsg(json.getString("statusMsg"));
        }
        if (json.has("gender")) {
            obj.setGender(json.getInt("gender"));
        }
        if (json.has("photoCount")) {
            obj.setPhotoCount(json.getInt("photoCount"));
        }
        if (json.has("photos")) {
            obj.setPhotos(new PhotosParser().parse(json.getJSONObject("photos")));
        }
        if (json.has("beenCount")) {
            obj.setBeenCount(json.getInt("beenCount"));
        }
        if (json.has("isRead")) {
            obj.setRead(json.getInt("isRead") == 1);
        }
        if (json.has("status")) {
            obj.setStatus(json.getInt("status"));
        }
        if (json.has("statusMsg")) {
            obj.setStatusMsg(json.getString("statusMsg"));
        }
        if (json.has("captureCount")) {
            obj.setCaptureCount(json.getInt("captureCount"));
        }
        if (json.has("lastSign")) {
            obj.setLastSign(json.getString("lastSign"));
        }
        if (json.has("tel")) {
            obj.setCsPhone(json.getString("tel"));
        }
        if (json.has("phone")) {
            obj.setPhone(json.getString("phone"));
        }
        if (json.has("email")) {
            obj.setEmail(json.getString("email"));
        }
        if (json.has("desc")) {
            obj.setStatusMsg(json.getString("desc"));
        }
        return obj;
    }
}
