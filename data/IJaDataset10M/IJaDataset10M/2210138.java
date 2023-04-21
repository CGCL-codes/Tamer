package Protocol.utility;

import java.io.Serializable;

public class SysHobby implements Serializable {

    int hobby_id;

    String hobby_name;

    String hobby_description;

    int hobby_popularity;

    public int getHobby_popularity() {
        return hobby_popularity;
    }

    public void setHobby_popularity(int hobby_popularity) {
        this.hobby_popularity = hobby_popularity;
    }

    public int getHobby_id() {
        return hobby_id;
    }

    public void setHobby_id(int hobby_id) {
        this.hobby_id = hobby_id;
    }

    public String getHobby_name() {
        return hobby_name;
    }

    public void setHobby_name(String hobby_name) {
        this.hobby_name = hobby_name;
    }

    public String getHobby_description() {
        return hobby_description;
    }

    public void setHobby_description(String hobby_description) {
        this.hobby_description = hobby_description;
    }
}
