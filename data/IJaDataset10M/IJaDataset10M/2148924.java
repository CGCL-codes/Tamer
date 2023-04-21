package bookmarks;

import static org.yuzz.xml.NodeStatics.a;
import static org.yuzz.xml.NodeStatics.n;
import static org.yuzz.xml.NodeStatics.t;
import org.yuzz.xml.Node;
import org.yuzz.web.Noder;

public class BookmarkForm implements Noder<Node> {

    public Node node() throws Throwable {
        Node addNewForm = n("div", n("form", a("action", "add"), a("method", "post"), t("name"), n("input", a("name", "name")), n("br"), t("url"), n("input", a("name", "url")), n("input", a("type", "submit"))));
        return addNewForm;
    }
}
