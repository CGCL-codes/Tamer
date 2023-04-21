package bookmarks;

import static org.yuzz.xml.NodeStatics.a;
import static org.yuzz.xml.NodeStatics.body;
import static org.yuzz.xml.NodeStatics.head;
import static org.yuzz.xml.NodeStatics.htmlXhtml1999;
import static org.yuzz.xml.NodeStatics.n;
import static org.yuzz.xml.NodeStatics.nlist;
import static org.yuzz.xml.NodeStatics.*;
import org.snuvy.DbTable;
import org.snuvy.Dbm;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.yuzz.xml.NodeFunctions;
import org.yuzz.xml.NodeOperators;
import org.yuzz.xml.NodeStatics;
import org.yuzz.xml.Node.NodeList;
import org.yuzz.xml.Xhtml.Body;
import org.yuzz.xml.Xhtml.HtmlTag;
import org.yuzz.xml.Xhtml.Table;
import org.yuzz.xml.Xhtml.Tr;
import org.yuzz.functor.Procedure.Proc2;
import org.yuzz.web.ListView;
import org.yuzz.web.MapReduce;
import org.yuzz.web.Page;

/**
 * have a list of actions<T>
 * have an object T which contains all possible resources
 * node<T> into list<Node>
 * combine list<Node>
 * @author sean
 *
 */
public class BookmarksPage implements Page {

    public HtmlTag node(Dbm dbm) throws Throwable {
        final BookmarkManager bookmarkManager = new BookmarkManager();
        final NodeList genericList = nlist(n("br"), n("h3", t("Generic List")));
        DbTable bookmarks = bookmarkManager.getTable(dbm);
        BlockingQueue<Object> q;
        BookmarkRowToTr rowToTr = new BookmarkRowToTr();
        BlockingQueue<Tr> rowQueue = new LinkedBlockingQueue<Tr>();
        bookmarks.queue(rowToTr, BookmarkManager.BOOKMARK_SCHEMA.byHost, rowQueue);
        final Table htmlTable = NodeStatics.table();
        MapReduce.reduce(htmlTable, rowQueue, new NodeOperators.AddNodesProc<Table, Tr>());
        genericList.add(new ListView(bookmarks).node());
        Body body = body();
        body.add(n("div", a("id", "urls"), n("h3", t("by hostname")), htmlTable, n("br")));
        body.add(table(tr(td(n("h3", t("test"))))));
        body.add(new BookmarkForm().node());
        body.add(genericList);
        return htmlXhtml1999(head(n("title", t("Bookmarks"))), body);
    }

    public HtmlTag process(HttpServletRequest req, HttpServletResponse res) throws Throwable {
        Dbm dbm = new Dbm();
        dbm.open();
        try {
            return node(dbm);
        } finally {
            dbm.close();
        }
    }
}
