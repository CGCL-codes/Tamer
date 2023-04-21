package guestbook;

import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import javax.jdo.PersistenceManager;
import totoCharts.globalWaveID;

public abstract class abstractRequestServlet<T> extends HttpServlet {

    /**
	 * 
	 */
    private PersistenceManager pm = PMF.get().getPersistenceManager();

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    protected final List<T> getData(Class<T> class1) {
        try {
            if (pm.isClosed()) pm = PMF.get().getPersistenceManager();
            javax.jdo.Query query = pm.newQuery(class1);
            if (class1 != globalWaveID.class) query.setOrdering("date desc");
            query.setRange(0, 1);
            List<T> list = (List<T>) pm.newQuery(query).execute();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void writeToServlet(HttpServletResponse resp, Object result) {
        resp.setContentType("text/plain;charset=UTF-8");
        try {
            resp.getWriter().println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void pmClose() {
        if ((pm != null) && (!pm.isClosed())) pm.close();
    }
}
