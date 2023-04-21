package solowiki.internal;

import info.bliki.api.Page;
import info.bliki.api.User;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Giuseppe Profiti
 */
public class RemoteOperations {

    String username;

    String pass;

    String actionLink;

    User user;

    public RemoteOperations(String api) {
        this("", "", api);
    }

    public RemoteOperations(String user, String password, String api) {
        username = user;
        pass = password;
        actionLink = api;
        this.user = new User(username, pass, actionLink);
    }

    public Vector<String> getPageId(Vector<String> names) {
        user.login();
        List<Page> listOfPages = user.queryInfo(names);
        Vector<String> v = new Vector(names.size());
        v.addAll(names);
        for (Page page : listOfPages) {
            int pos = names.indexOf(page.getTitle());
            if (pos == -1) {
                String title = String.valueOf(page.getTitle().charAt(0)).toLowerCase() + page.getTitle().substring(1);
                pos = names.indexOf(title);
            }
            v.setElementAt(page.getPageid(), pos);
        }
        return v;
    }

    public String getPageContent(String pagename) {
        user.login();
        Vector<String> v = new Vector(1);
        v.add(pagename);
        List<Page> results = user.queryContent(v);
        Page page = results.get(0);
        String res = null;
        if ((page.getPageid() != null) && (Integer.parseInt(page.getPageid()) > 0)) res = page.getCurrentContent();
        return res;
    }
}
