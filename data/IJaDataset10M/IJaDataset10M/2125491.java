package jnewsgate.http.phpbbgate;

import jnewsgate.http.*;
import java.util.*;

/**
 * Group source for phpBB groups.
 */
public class PhpBBGroupSource extends HTTPGroupSource {

    public PhpBBGroupSource(Properties settings, String prefix) {
        this(settings.getProperty(prefix + "name"), settings.getProperty(prefix + "url"), settings.getProperty(prefix + "forum"), settings.getProperty(prefix + "maxposts"), settings.getProperty(prefix + "username"), settings.getProperty(prefix + "password"));
    }

    public PhpBBGroupSource(String groupname, String url, String forum, String maxposts, String username, String password) {
        super(new HTTPGroup[] { new HTTPGroup(new PhpBBGroupLoader(groupname, url, forum, maxposts, username, password)) }, groupname);
    }
}
