package net.sf.jiproute.server;

import simple.http.serve.Context;
import simple.http.serve.Resource;
import simple.http.Request;
import simple.http.Response;
import org.apache.log4j.Logger;
import net.sf.jiproute.util.NodeUtils;

public class DelRootRedirect extends SecureRedirect {

    private static Logger log = Logger.getLogger(DelRootRedirect.class.getName());

    public DelRootRedirect(Context context) {
        super(context);
    }

    public Resource redirect(Request req, Response resp) throws Exception {
        log.info("User " + req.getSession().get("userId").toString() + " requested to drop the root qdisc");
        NodeUtils.deleteQdisc(req.getParameter("dev"));
        return resolve("home.vm");
    }
}
