package org.imajie.server.web.imajiematch.matchsServers;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Carl Tremblay
 */
public class GetTasksCountJspBean implements Serializable {

    private static Logger logger = Logger.getLogger(GetTasksCountJspBean.class.getName());

    public String getCount(HttpServletRequest request) {
        String msg = "";
        HttpSession session = request.getSession(true);
        if (session.getAttribute("tasksList") != null) {
            String delimiter = "\\|!\\|";
            String[] temp = session.getAttribute("tasksList").toString().split(delimiter);
            if (temp.length == 0) {
                msg = "0";
            } else {
                msg = msg + (temp.length - 1);
            }
        } else {
            msg = "0";
        }
        return msg;
    }
}
