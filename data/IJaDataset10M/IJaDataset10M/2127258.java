package photospace.service;

import org.springframework.remoting.jaxrpc.*;
import photospace.*;
import photospace.space.*;
import photospace.meta.*;

/**
 * Photospace web service service interface.
 * 
 * @link http://sourceforge.net/mailarchive/message.php?msg_id=7709205
 */
public class Photospace extends ServletEndpointSupport {

    public Meta browse(String path, int sortOrder, int start, int end) throws Exception {
        return Application.searcher().browse(path, sortOrder, start, end);
    }

    public SearchResult search(String query, int sortOrder, int start, int end) throws Exception {
        return Application.searcher().search(query, sortOrder, start, end);
    }

    public Meta get(String path) throws Exception {
        return Application.persister().getMeta(path);
    }

    public UTMPoint[] convertToUtm(Position[] positions) throws Exception {
        return Calculator.toUTMPoint(positions);
    }
}
