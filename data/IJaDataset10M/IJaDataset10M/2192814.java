package jblip.gui;

import java.util.ArrayList;
import java.util.List;
import jblip.base.HttpClient;
import jblip.gui.data.DatabaseClient;
import jblip.resources.User;
import jblip.resources.UserPicture;

public class JBlipClient extends HttpClient {

    private static final UserPicture AV_EMPTY = new UserPicture() {

        private static final long serialVersionUID = 1L;

        @Override
        public String getURL(PictureSize size) {
            return "";
        }

        @Override
        public String getURL() {
            return "";
        }

        @Override
        public Integer getID() {
            return -1;
        }
    };

    private DatabaseClient cache;

    public JBlipClient(String username, String password) {
        super(username, password);
    }

    public JBlipClient(String host, String username, String password) {
        super(host, username, password);
    }

    public synchronized List<String> getTrackedTags() {
        List<String> tags = new ArrayList<String>();
        return tags;
    }

    @Override
    public synchronized UserPicture getAvatar(final Integer user_id) {
        User user = cache.getUser(user_id);
        if (user == null) {
            user = getUser(user_id);
        }
        return getAvatar(user.getLogin());
    }

    @Override
    public synchronized UserPicture getAvatar(final String user) {
        UserPicture pic = cache.getAvatar(user);
        if (pic == null) {
            pic = super.getAvatar(user);
            if (pic != null) {
                cache.setAvatar(user, pic);
            } else {
                cache.setAvatar(user, AV_EMPTY);
            }
        } else if (pic.getURL().equals("")) {
            return null;
        }
        return pic;
    }

    public void setCache(final DatabaseClient db) {
        this.cache = db;
    }
}
