package com.busfm.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.busfm.model.ChannelEntity;
import com.busfm.model.ChannelList;
import com.busfm.model.PlayList;
import com.busfm.model.SongEntity;
import com.busfm.model.UserEntity;

/**
 * @Description Parse JSON
 * 
 * @author DJ
 * @version 1.0
 * @Date 2011/08/21
 */
public class JsonParser {

    public static UserEntity parserUserData(String body) throws JSONException {
        UserEntity userEntity = new UserEntity();
        try {
            JSONObject jsonObject = new JSONObject(body);
            JSONArray jarr = jsonObject.getJSONArray("userinfo");
            JSONObject tmp;
            for (int i = 0; i < jarr.length(); i++) {
                tmp = jarr.getJSONObject(i);
                userEntity.setMemberID(tmp.getString("member_id"));
                userEntity.setMemberEmail(tmp.getString("member_mail"));
                userEntity.setMemberNickName(tmp.getString("member_nickname"));
            }
        } catch (Exception e) {
            JSONObject jsonObject = new JSONObject(body);
            String errorMsg = jsonObject.getString("error");
            userEntity.setErrorMsg(errorMsg);
        }
        return userEntity;
    }

    public static ChannelList parserChannels(String body) {
        ChannelList channelList = new ChannelList();
        ChannelEntity channelEntity = new ChannelEntity();
        try {
            JSONObject jsonObject = new JSONObject(body);
            JSONArray jarr = jsonObject.getJSONArray("Channels");
            JSONObject tmp;
            for (int i = 0; i < jarr.length(); i++) {
                tmp = jarr.getJSONObject(i);
                channelEntity = new ChannelEntity();
                channelEntity.setCID(tmp.getInt("cid"));
                channelEntity.setCNAME(tmp.getString("cname"));
                channelList.addList(channelEntity);
            }
        } catch (Exception e) {
        }
        return channelList;
    }

    public static PlayList parserSongs(String body, boolean isFavoriate) {
        PlayList playList = new PlayList();
        SongEntity songEntity = new SongEntity();
        try {
            JSONObject jsonObject = new JSONObject(body);
            JSONArray jarr = jsonObject.getJSONArray("Tracks");
            JSONObject tmp;
            for (int i = 0; i < jarr.length(); i++) {
                tmp = jarr.getJSONObject(i);
                songEntity = new SongEntity();
                songEntity.setSongId(tmp.getString("id"));
                songEntity.setTitle(tmp.getString("title"));
                songEntity.setLocation(tmp.getString("url"));
                songEntity.setArtist(tmp.getString("artist"));
                songEntity.setAlbum(tmp.getString("album"));
                songEntity.setThumb(tmp.getString("thumb"));
                songEntity.setIsFavoriate(isFavoriate);
                playList.addList(songEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playList;
    }
}
