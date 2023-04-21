package cc.carnero.ctwee;

import java.util.Date;
import java.util.HashMap;
import android.util.Log;

/**
 * manipulating and saving user data
 */
public class ctUser {

    private double id = 0;

    private String name = "";

    private String screenname = "";

    private String avatar = "";

    private String url = "";

    private String description = "";

    private String location = "";

    private Date created;

    private int tweets = 0;

    private int following = 0;

    private int followers = 0;

    private int friends = 0;

    private int verified = 0;

    public Double getId() {
        return this.id;
    }

    public HashMap get() {
        HashMap data = new HashMap();
        data.put("uid", this.id);
        data.put("name", this.name);
        data.put("screenname", this.screenname);
        data.put("avatar", this.avatar);
        data.put("url", this.url);
        data.put("description", this.description);
        data.put("location", this.location);
        data.put("created", this.created);
        data.put("tweets", this.tweets);
        data.put("following", this.following);
        data.put("followers", this.followers);
        data.put("friends", this.friends);
        data.put("verified", this.verified);
        return data;
    }

    public Boolean saveData(HashMap data) {
        try {
            if (data.size() > 0) {
                Object[] keys = data.keySet().toArray();
                for (int i = 0; i < keys.length; i++) {
                    String key = keys[i].toString();
                    Object value = data.get(key);
                    if (key.equalsIgnoreCase("uid") == true) {
                        this.id = (Double) value;
                        continue;
                    }
                    if (key.equalsIgnoreCase("name") == true) {
                        this.name = value.toString();
                        continue;
                    }
                    if (key.equalsIgnoreCase("screenname") == true) {
                        this.screenname = value.toString();
                        continue;
                    }
                    if (key.equalsIgnoreCase("avatar") == true) {
                        this.avatar = value.toString();
                        continue;
                    }
                    if (key.equalsIgnoreCase("url") == true) {
                        this.url = value.toString();
                        continue;
                    }
                    if (key.equalsIgnoreCase("description") == true) {
                        this.description = value.toString();
                        continue;
                    }
                    if (key.equalsIgnoreCase("location") == true) {
                        this.location = value.toString();
                        continue;
                    }
                    if (key.equalsIgnoreCase("created") == true) {
                        this.created = (Date) value;
                        continue;
                    }
                    if (key.equalsIgnoreCase("tweets") == true) {
                        this.tweets = (Integer) value;
                        continue;
                    }
                    if (key.equalsIgnoreCase("following") == true) {
                        this.following = (Integer) value;
                        continue;
                    }
                    if (key.equalsIgnoreCase("followers") == true) {
                        this.followers = (Integer) value;
                        continue;
                    }
                    if (key.equalsIgnoreCase("friends") == true) {
                        this.friends = (Integer) value;
                        continue;
                    }
                    if (key.equalsIgnoreCase("verified") == true) {
                        this.verified = (Integer) value;
                        continue;
                    }
                }
                if (this.screenname.length() == 0 && this.name.length() > 0) {
                    this.screenname = this.name;
                }
                if (this.id > 0 && this.name.length() > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            Log.e(ctGlobal.tag, "ctUser.saveData: " + e.toString());
        }
        return false;
    }
}
