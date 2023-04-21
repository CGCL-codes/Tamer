package org.knopflerfish.bundle.httpconsole;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import org.osgi.framework.*;

public class UpdateCommand extends IconCommand {

    public UpdateCommand() {
        super("cmd_update", "Update", "Update selected bundles", Activator.RES_ALIAS + "/update.gif");
    }

    public StringBuffer run(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        long[] bids = Util.getBundleIds(request);
        sb.append("<div class=\"shadow\">" + getName() + "</div>");
        if (bids.length == 0) {
            sb.append("No bundles selected");
        }
        for (int i = 0; i < bids.length; i++) {
            try {
                Bundle b = Activator.bc.getBundle(bids[i]);
                b.update();
                sb.append("Updated " + Util.getName(b) + "<br/>");
            } catch (Exception e) {
                sb.append(Util.toHTML(e));
            }
        }
        return sb;
    }
}
