package tristero.www;

import org.webmacro.*;
import org.webmacro.servlet.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import tristero.*;
import java.net.*;
import java.util.*;
import tristero.*;
import java.net.*;
import tristero.util.*;
import tristero.ntriple.*;

public class Browse extends WMServlet {

    static final String rdfUrl = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    static final String dcUrl = "http://purl.org/dc/elements/1.1/";

    public Template handle(WebContext context) throws HandlerException {
        try {
            System.out.println("www search handle()");
            Config.init();
            HttpServletRequest req = context.getRequest();
            context.getResponse().setContentType("text/html");
            context.put("version", Config.version);
            context.put("address", Config.address);
            context.put("encoder", new MyURLEncoder());
            System.out.println("Browsing...");
            String dbName = req.getParameter("db");
            context.put("db", dbName);
            Schema schema = null;
            try {
                schema = Config.metaDb.loadSchema(dbName);
            } catch (Exception e) {
                e.printStackTrace();
                throw new HandlerException(e.toString());
            }
            String[] names = schema.names;
            String filter = req.getParameter("primary");
            context.put("propname", filter);
            if (filter != null) {
                Hashtable criteria = new Hashtable();
                MetadataDatabase db = Config.metaDb;
                System.out.println("loadMetadata: " + dbName);
                String model = db.loadMetadata(dbName);
                System.out.println("query model: " + model);
                model = db.queryModel(model, criteria);
                System.out.println("putting model " + model);
                context.put("model", Config.rdfStore.fetch(model));
                Set set = new HashSet();
                filter = "<" + dcUrl + filter + ">";
                model = Config.rdfStore.search("", filter, "", model);
                String objUri = Config.rdfStore.isolateObjects(model);
                List objects = Config.rdfStore.fetchEntities(objUri);
                System.out.println("object: " + objects);
                Iterator iterator = objects.iterator();
                while (iterator.hasNext()) {
                    Object o = iterator.next();
                    System.out.println("adding to result set: " + o);
                    set.add(o);
                }
                context.put("options", set);
                context.put("parser", new NTripleParser());
                context.put("decoder", new URLDecoder());
                return getTemplate("browse_results.wm");
            } else {
                Vector v = new Vector();
                for (int x = 0; x < schema.names.length; x++) {
                    v.addElement(schema.names[x]);
                }
                context.put("fields", v);
                return getTemplate("browse.wm");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new HandlerException("Exception: " + e);
        }
    }
}
