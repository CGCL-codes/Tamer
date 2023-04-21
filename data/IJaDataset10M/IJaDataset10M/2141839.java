package testng;

import java.util.List;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import app.data.Bug;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import com.avaje.ebean.control.ServerControl;
import com.avaje.ebean.meta.MetaQueryStatistic;

public class BugAndDetailsTester {

    protected EbeanServer server;

    @BeforeSuite
    public void setup() {
        server = Ebean.getServer(null);
    }

    @Test(groups = "standalone")
    public void testBugAndDetails() throws Exception {
        ServerControl serverControl = server.getServerControl();
        serverControl.getLogControl().setDebugGeneratedSql(true);
        withAllProperties();
        Reporter.log(" GAP ", true);
        withPartials();
        System.out.println(" ");
        System.out.println(" GAP ");
        System.out.println(" ");
        withPartialsAsQuery();
        Query<MetaQueryStatistic> query = Ebean.createQuery(MetaQueryStatistic.class);
        List<MetaQueryStatistic> list = query.findList();
        for (MetaQueryStatistic metaQueryStatistic : list) {
            System.out.println(metaQueryStatistic);
        }
    }

    /**
	 * With the "*" all the properties of the associated beans are fetched...
	 */
    public static void withAllProperties() {
        Query<Bug> query = Ebean.createQuery(Bug.class);
        query.setAutoFetch(false);
        query.join("details");
        query.join("details.user", "*");
        query.join("product", "*");
        List<Bug> list = query.findList();
        Assert.assertEquals(list.size(), 6, "list from " + query.getGeneratedSql());
        System.out.println(list.size());
        System.out.println(list);
    }

    /**
	 * Only get the user name and email address... the user objects are
	 * "partially" populated.
	 */
    public static void withPartials() {
        Query<Bug> query = Ebean.createQuery(Bug.class);
        query.setAutoFetch(false);
        query.join("details", "*");
        query.join("details.user", "name, email");
        query.join("product", "name");
        query.where().eq("id", 300);
        query.where().ilike("product.name", "code%");
        List<Bug> list = query.findList();
        System.out.println(list.size());
        System.out.println(list);
    }

    public static void withPartialsAsQuery() {
        String oql = "find bug " + " join details " + " join details.user (name, email) " + " join product (name) " + " where id > :minId and lower(product.name) like :prodName ";
        Query<Bug> query = Ebean.createQuery(Bug.class);
        query.setAutoFetch(false);
        query.setQuery(oql);
        query.set("minId", 1);
        query.set("prodName", "code%");
        List<Bug> list = query.findList();
        System.out.println(list.size());
        System.out.println(list);
    }
}
