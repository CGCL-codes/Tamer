package jspx.user.online;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User:chenYuan (mail:cayurain@21cn.com)
 * Date: 2006-8-31
 * Time: 17:07:17
 *
 */
public interface InspectPage {

    boolean canOpen(HttpServletRequest request, HttpServletResponse response);
}
