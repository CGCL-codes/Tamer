package sjtu.llgx.web.action.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import sjtu.llgx.util.Common;
import sjtu.llgx.util.JavaCenterHome;
import sjtu.llgx.web.action.BaseAction;

public class PostAction extends BaseAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> sGlobal = (Map<String, Object>) request.getAttribute("sGlobal");
        Map<String, Object> sConfig = (Map<String, Object>) request.getAttribute("sConfig");
        int supe_uid = (Integer) sGlobal.get("supe_uid");
        int tagid = Common.intval(request.getParameter("tagid"));
        try {
            if (submitCheck(request, "deletesubmit")) {
                Object[] ids = request.getParameterValues("ids");
                if (ids != null && adminDeleteService.deletePosts(request, response, supe_uid, tagid, ids) != null) {
                    return cpMessage(request, mapping, "do_success", request.getParameter("mpurl"));
                } else {
                    return cpMessage(request, mapping, "cp_choose_to_delete_the_topic");
                }
            }
        } catch (Exception e) {
            return showMessage(request, response, e.getMessage());
        }
        boolean managebatch = Common.checkPerm(request, response, "managebatch");
        boolean allowbatch = true;
        boolean allowmanage = false;
        if (Common.checkPerm(request, response, "managethread")) {
            allowmanage = true;
        } else {
            if (tagid > 0) {
                Map<String, Object> whereArr = new HashMap<String, Object>();
                whereArr.put("tagid", tagid);
                whereArr.put("uid", supe_uid);
                int grade = Common.intval(Common.getCount("tagspace", whereArr, "grade"));
                if (grade >= 8) {
                    managebatch = true;
                    allowmanage = true;
                }
            }
        }
        Map<String, String[]> paramMap = request.getParameterMap();
        if (!allowmanage) {
            paramMap.put("uid", new String[] { String.valueOf(supe_uid) });
            paramMap.put("username", null);
        }
        StringBuffer mpurl = new StringBuffer("admincp.jsp?ac=post");
        String timeoffset = Common.getTimeOffset(sGlobal, sConfig);
        String[] intkeys = new String[] { "uid", "pid", "tagid", "tid", "isthread" };
        String[] strkeys = new String[] { "username", "ip" };
        List<String[]> randkeys = new ArrayList<String[]>();
        randkeys.add(new String[] { "sstrtotime", "dateline" });
        String[] likekeys = new String[] { "message" };
        Map<String, String> wheres = getWheres(intkeys, strkeys, randkeys, likekeys, "", paramMap, timeoffset);
        String whereSQL = wheres.get("sql") == null ? "1" : wheres.get("sql");
        mpurl.append(wheres.get("url"));
        Map<String, String> orders = getOrders(new String[] { "dateline" }, "pid", null, paramMap);
        String ordersql = orders.get("sql");
        mpurl.append(orders.get("url"));
        request.setAttribute("orderby_" + request.getParameter("orderby"), " selected");
        request.setAttribute("ordersc_" + request.getParameter("ordersc"), " selected");
        int perpage = Common.intval(request.getParameter("perpage"));
        if (!Common.in_array(new Integer[] { 20, 50, 100, 1000 }, perpage)) {
            perpage = 20;
        }
        int page = Math.max(Common.intval(request.getParameter("page")), 1);
        int start = (page - 1) * perpage;
        int maxPage = (Integer) sConfig.get("maxpage");
        String result = Common.ckStart(start, perpage, maxPage);
        if (result != null) {
            return showMessage(request, response, result);
        }
        int count = 1;
        String selectsql = null;
        if (perpage > 100) {
            selectsql = "pid";
        } else {
            count = dataBaseService.findRows("SELECT COUNT(*) FROM " + JavaCenterHome.getTableName("post") + " WHERE " + whereSQL);
            selectsql = "*";
        }
        mpurl.append("&perpage=" + perpage);
        request.setAttribute("perpage_" + perpage, " selected");
        if (count > 0) {
            List<Map<String, Object>> list = dataBaseService.executeQuery("SELECT " + selectsql + " FROM " + JavaCenterHome.getTableName("post") + " WHERE " + whereSQL + " " + ordersql + " LIMIT " + start + "," + perpage);
            if (perpage > 100) {
                count = list.size();
            } else {
                SimpleDateFormat postSDF = Common.getSimpleDateFormat("yyyy-MM-dd HH:mm", timeoffset);
                List<Integer> tids = new ArrayList<Integer>();
                for (Map<String, Object> value : list) {
                    if (!Common.empty(value.get("message")) && Common.empty(request.getParameter("pid"))) {
                        try {
                            value.put("message", Common.getStr((String) value.get("message"), 150, false, false, false, 0, 0, request, response));
                        } catch (Exception e) {
                            return showMessage(request, response, e.getMessage());
                        }
                    }
                    if (!managebatch && (Integer) value.get("uid") != supe_uid) {
                        allowbatch = false;
                    }
                    int tid = (Integer) value.get("tid");
                    if (tid > 0) {
                        tids.add(tid);
                    }
                    value.put("dateline", Common.gmdate(postSDF, (Integer) value.get("dateline")));
                }
                if (tids.size() > 0) {
                    Map<Object, Object> threads = new HashMap<Object, Object>();
                    List<Map<String, Object>> threadList = dataBaseService.executeQuery("SELECT tid, subject FROM " + JavaCenterHome.getTableName("thread") + " WHERE tid IN (" + Common.sImplode(tids) + ")");
                    for (Map<String, Object> value : threadList) {
                        threads.put(value.get("tid"), value.get("subject"));
                    }
                    request.setAttribute("threads", threads);
                }
            }
            request.setAttribute("multi", Common.multi(request, count, perpage, page, maxPage, mpurl.toString(), null, null));
            request.setAttribute("list", list);
            if (list.size() % perpage == 1) {
                mpurl.append("&page=" + (page - 1));
            } else {
                mpurl.append("&page=" + page);
            }
        }
        request.setAttribute("FORMHASH", formHash(request));
        request.setAttribute("count", count);
        request.setAttribute("mpurl", mpurl);
        request.setAttribute("allowmanage", allowmanage);
        request.setAttribute("allowbatch", allowbatch);
        request.setAttribute("perpage", perpage);
        request.setAttribute("wheresql", whereSQL);
        request.setAttribute("tagid", tagid);
        return mapping.findForward("post");
    }
}
