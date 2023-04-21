package org.isource.admin;

import java.io.IOException;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.isource.common.PMF;
import org.isource.pojo.ThirdParty;

@SuppressWarnings("serial")
public class UpdateThirdParty extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String key = req.getParameter("key");
        String name = req.getParameter("name");
        String version = req.getParameter("version");
        String category = req.getParameter("category");
        if (key == null) {
            resp.getWriter().println("No key provided.");
            return;
        }
        if (name == null) {
            resp.getWriter().println("No name provided.");
            return;
        }
        if (version == null) {
            resp.getWriter().println("No version provided.");
            return;
        }
        if (category == null) {
            resp.getWriter().println("No category provided.");
            return;
        }
        doPostJDO(Long.valueOf(key), name, version, category);
        resp.sendRedirect("/admin");
    }

    private void doPostJDO(long key, String name, String version, String category) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            ThirdParty tp = pm.getObjectById(ThirdParty.class, key);
            tp.setName(name);
            tp.setVersion(version);
            tp.setCategory(category);
        } finally {
            pm.close();
        }
    }
}
