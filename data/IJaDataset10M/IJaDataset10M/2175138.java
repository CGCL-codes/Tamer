package org.dbwiki.web.ui;

import java.util.Vector;
import org.dbwiki.web.html.HtmlPage;
import org.dbwiki.web.request.ServerRequest;

/** Handles server responses.  Called from WikiServer.
 * @jcheney
 * */
public class ServerResponseHandler extends HtmlContentGenerator {

    private String _title;

    public ServerResponseHandler(ServerRequest<?> request, String title) {
        super(request);
        _title = title;
    }

    public void print(String key, Vector<String> args, HtmlPage page, String indention) throws org.dbwiki.exception.WikiException {
        if (key.equals(ContentTitle)) {
            if (args != null) {
                page.add(args.get(0));
            } else {
                page.add(_title);
            }
        } else {
            super.print(key, args, page, indention);
        }
    }
}
