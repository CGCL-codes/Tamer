package totoCharts;

import guestbook.abstractRequestServlet;
import javax.servlet.http.*;
import java.util.List;

public class gadgetRequestWaveID<glovalWaveID> extends abstractRequestServlet<globalWaveID> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int result = -1;
            int update;
            List<globalWaveID> list = getData(globalWaveID.class);
            if (list.size() > 0) {
                for (globalWaveID ID : list) {
                    result = ID.getWaveID();
                    update = result + 1;
                    ID.setWaveID(update);
                }
            } else {
                globalWaveID ID = new globalWaveID();
                guestbook.DBMethods.saveInDB(ID, true);
                result = 0;
                update = 1;
                ID.setWaveID(update);
            }
            resp.setContentType("text/plain;charset=UTF-8");
            resp.getWriter().print(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pmClose();
        }
    }
}
