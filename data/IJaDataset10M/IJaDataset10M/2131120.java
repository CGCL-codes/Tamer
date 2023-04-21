package net.sipviplinks.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GirlsPhonesServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(GirlsPhonesServlet.class.getName());

    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Start GirlsPhonesServlet");
        JSONArray girlsphonesjarray = new JSONArray();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        int k = 0;
        Query q = pm.newQuery(GirlsPhones.class);
        List<GirlsPhones> results = (List<GirlsPhones>) q.execute();
        k = results.size();
        if (k > 0) {
            Random randGen = new Random();
            int first = randGen.nextInt(k);
            int second = randGen.nextInt(k);
            while (first == second) {
                second = randGen.nextInt(k);
            }
            int count = 0;
            if (results.iterator().hasNext()) {
                for (GirlsPhones e : results) {
                    if (count == first || count == second) {
                        JSONObject obj = new JSONObject();
                        obj.put("name", e.getName());
                        obj.put("phone", e.getPhone());
                        obj.put("imagefile", e.getImagefile());
                        girlsphonesjarray.add(obj);
                    }
                    count = count + 1;
                }
            }
            String output = req.getParameter("callback") + "(" + girlsphonesjarray + ");";
            resp.setContentType("text/javascript");
            PrintWriter out = resp.getWriter();
            out.println(output);
        } else {
            log.severe("result size = 0 ??");
        }
        pm.close();
    }
}
