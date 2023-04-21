package maltcms.db;

import java.io.File;
import java.net.URL;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import cross.Logging;

public class QueryCallable<T> implements Callable<ObjectSet<T>> {

    private Logger log = Logging.getLogger(this);

    protected Predicate<T> llap;

    protected ObjectSet<T> los;

    protected String lloc;

    protected boolean done = false;

    protected ObjectContainer oc = null;

    public QueryCallable(String location, Predicate<T> ap) {
        lloc = location;
        this.log.debug("QueryCallable for {}", location);
        if (ap == null) {
            throw new IllegalArgumentException("Predicate must not be null!");
        } else {
            llap = ap;
        }
    }

    public ObjectSet<T> call() throws Exception {
        if (new File(this.lloc).exists()) {
            this.log.debug("Opening DB locally as file!");
            this.oc = Db4o.openFile(this.lloc);
            return oc.query(llap);
        } else {
            URL url = new URL(this.lloc);
            this.log.debug("Opening DB via Client!");
            this.oc = Db4o.openClient(url.getHost(), url.getPort(), url.getUserInfo(), "default");
            return oc.query(llap);
        }
    }

    public void terminate() {
        if (this.oc != null) {
            this.log.debug("Closing DB connection!");
            this.oc.close();
        }
    }
}
