package jgroups.jgroups;

import jgroups.html_parser.*;
import java.io.*;
import jgroups.http_client.*;
import java.util.*;

/**

The User class serves to represent users of the Yahoo! Groups system. A user has a login name and a password.

Notes: It is currently not possible to make use of more than one user at the same time.
The current version of this library does not have support for retrieving the list of groups a user is
subscribed to, this is a planned extension.

 */
public class User {

    private String login;

    private String passwd;

    private HashMap groups = new HashMap();

    public User(String login_, String passwd_) {
        login = login_;
        passwd = passwd_;
    }

    /** The login method makes a connection to yahoo to log in the user. The login message must be sent
	before doing any other operations on a user.
	*/
    public void login() throws IOException, ModuleException, ParseException {
        HTTPConnection connection = new HTTPConnection("login.yahoo.com");
        NVPair[] headers = new NVPair[1];
        headers[0] = new NVPair("accept", "*/*");
        connection.setDefaultHeaders(headers);
        HTTPResponse response = connection.Get("/");
        Logger.logDebug("login page: " + response.getText());
        HTMLTree tree = new HTMLTree(response.getData());
        HTMLNode formNode = tree.findInAll("form");
        FormExtracter form = new FormExtracter(formNode);
        form.set("login", login);
        form.set("passwd", passwd);
        response = form.submit();
        Logger.logDebug("login response: " + response.getText());
        connection.stop();
    }

    /**
The getGroupNamed method gets a Group object representing a Yahoo! group. Note that the user doesn't have to be
 subscribed to the group, though in that case if the group is private the Group object won't be able to access its
 messages. The group doesn't even have to exist for this method to work (though this might be changed in a future version). 
*/
    public Group getGroupNamed(String name) {
        Object groupNotCast = groups.get(name);
        if (groupNotCast == null) {
            Group group = new Group(this, name);
            groups.put(name, group);
            return group;
        } else {
            return (Group) groupNotCast;
        }
    }
}
