package sjtu.llgx.web.action.admin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import sjtu.llgx.util.Common;
import sjtu.llgx.util.JavaCenterHome;
import sjtu.llgx.util.Serializer;
import sjtu.llgx.web.action.BaseAction;

public class MagicAction extends BaseAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        if (!Common.checkPerm(request, response, "managemagic")) {
            return cpMessage(request, mapping, "cp_no_authority_management_operation");
        }
        String view = request.getParameter("view");
        view = "disabled".equals(view) ? view : "enabled";
        request.setAttribute("view", view);
        request.setAttribute("actives_" + view, " class='active'");
        try {
            if (submitCheck(request, "editsubmit")) {
                String custom = null;
                String customName = request.getParameter("customName");
                if (customName != null) {
                    String customValue = request.getParameter("custom_" + customName);
                    if (!Common.empty(customValue)) {
                        Map<String, String> customs = new HashMap<String, String>();
                        customs.put(customName, customValue);
                        custom = Serializer.serialize(customs);
                    }
                }
                String description = Common.getStr(request.getParameter("description"), 0, true, true, false, 0, 0, request, response);
                StringBuilder setSql = new StringBuilder();
                setSql.append("description='" + description + "',");
                setSql.append("charge='" + Common.range(request.getParameter("charge"), 65535, 0) + "',");
                setSql.append("forbiddengid='" + Common.implode(request.getParameterValues("forbiddengid"), ",") + "',");
                setSql.append("experience='" + Common.range(request.getParameter("experience"), 65535, 0) + "',");
                setSql.append("provideperoid='" + Common.intval(request.getParameter("provideperoid")) + "',");
                setSql.append("providecount='" + Common.range(request.getParameter("providecount"), 65535, 0) + "',");
                setSql.append("useperoid='" + Common.intval(request.getParameter("useperoid")) + "',");
                setSql.append("usecount='" + Common.range(request.getParameter("usecount"), 65535, 0) + "',");
                setSql.append("displayorder='" + Common.intval(request.getParameter("displayorder")) + "',");
                setSql.append("close='" + (Common.intval(request.getParameter("close")) != 0 ? 1 : 0) + "',");
                setSql.append("custom='" + (custom == null ? "" : custom) + "'");
                String mid = request.getParameter("mid");
                String sql = "UPDATE " + JavaCenterHome.getTableName("magic") + " SET " + setSql + " WHERE mid='" + mid + "'";
                dataBaseService.executeUpdate(sql);
                int storage = Common.intval(request.getParameter("storage"));
                sql = "UPDATE " + JavaCenterHome.getTableName("magicstore") + " SET storage='" + storage + "' WHERE mid='" + mid + "'";
                dataBaseService.executeUpdate(sql);
                cacheService.magic_cache();
                return cpMessage(request, mapping, "do_success", "admincp.jsp?ac=magic&view=" + view, 2);
            } else if (submitCheck(request, "ordersubmit")) {
                String[] mids = request.getParameterValues("mid");
                if (mids != null) {
                    Map<String, Integer> orders = new HashMap<String, Integer>();
                    Map<String, Integer> charges = new HashMap<String, Integer>();
                    String sql = "SELECT mid, charge, displayorder FROM " + JavaCenterHome.getTableName("magic") + " WHERE close=0";
                    List<Map<String, Object>> magics = dataBaseService.executeQuery(sql);
                    for (Map<String, Object> magic : magics) {
                        orders.put((String) magic.get("mid"), (Integer) magic.get("displayorder"));
                        charges.put((String) magic.get("mid"), (Integer) magic.get("charge"));
                    }
                    int orderVal = 0, chargeVal = 0;
                    for (String mid : mids) {
                        orderVal = (int) Common.range(request.getParameter("displayorder_" + mid), 65535, 0);
                        chargeVal = (int) Common.range(request.getParameter("charge_" + mid), 65535, 0);
                        if (orderVal != orders.get(mid) || chargeVal != charges.get(mid)) {
                            sql = "UPDATE " + JavaCenterHome.getTableName("magic") + " SET displayorder=" + orderVal + ",charge=" + chargeVal + " WHERE mid='" + mid + "'";
                            dataBaseService.executeUpdate(sql);
                        }
                    }
                }
                return cpMessage(request, mapping, "do_success", "admincp.jsp?ac=magic&view=" + view, 2);
            }
        } catch (Exception e) {
            return showMessage(request, response, e.getMessage());
        }
        if ("edit".equals(request.getParameter("op"))) {
            String mid = request.getParameter("mid");
            String sql = "SELECT m.*,ms.storage FROM " + JavaCenterHome.getTableName("magic") + " m LEFT JOIN " + JavaCenterHome.getTableName("magicstore") + " ms ON m.mid=ms.mid WHERE m.mid='" + mid + "'";
            List<Map<String, Object>> magics = dataBaseService.executeQuery(sql);
            if (magics.isEmpty()) {
                return cpMessage(request, mapping, "magic_does_not_exist");
            }
            Map<String, Object> magic = magics.get(0);
            sql = "SELECT gid,grouptitle,system FROM " + JavaCenterHome.getTableName("usergroup");
            List<Map<String, Object>> query = dataBaseService.executeQuery(sql);
            Map<Object, Map<Integer, String>> userGroups = new LinkedHashMap<Object, Map<Integer, String>>();
            userGroups.put(-1, null);
            userGroups.put(1, null);
            userGroups.put(0, null);
            for (Map<String, Object> value : query) {
                Map<Integer, String> userGroup = userGroups.get(value.get("system"));
                if (userGroup == null) {
                    userGroup = new HashMap<Integer, String>();
                    userGroups.put(value.get("system"), userGroup);
                }
                userGroup.put((Integer) value.get("gid"), (String) value.get("grouptitle"));
            }
            request.setAttribute("userGroups", userGroups);
            magic.put("forbiddengid", ((String) magic.get("forbiddengid")).split(","));
            magic.put("custom", Serializer.unserialize((String) magic.get("custom"), false));
            request.setAttribute("magic", magic);
            request.setAttribute("custom", getCustom(mid));
        } else {
            int close = "disabled".equals(view) ? 1 : 0;
            String sql = "SELECT mid,name,description,charge,displayorder FROM " + JavaCenterHome.getTableName("magic") + " WHERE close=" + close + " ORDER BY displayorder";
            List<Map<String, Object>> magics = dataBaseService.executeQuery(sql);
            request.setAttribute("magics", magics);
        }
        return mapping.findForward("magic");
    }

    private Map<String, Object> getCustom(String mid) {
        Map<String, Object> custom = new HashMap<String, Object>();
        Map<Integer, String> option = new TreeMap<Integer, String>();
        if ("invisible".equals(mid)) {
            option.put(86400, "24Сʱ");
            option.put(259200, "3��");
            option.put(432000, "5��");
            option.put(604800, "7��");
            initCustom(custom, "����ʱ��", "effectivetime", "24Сʱ", option);
        } else if ("superstar".equals(mid)) {
            option.put(86400, "24Сʱ");
            option.put(259200, "3��");
            option.put(432000, "5��");
            option.put(604800, "7��");
            initCustom(custom, "����ʱ��", "effectivetime", "7��", option);
        } else if ("friendnum".equals(mid)) {
            option.put(5, "5");
            option.put(10, "10");
            option.put(20, "20");
            option.put(50, "50");
            option.put(100, "100");
            initCustom(custom, "���Ӻ�����", "addnum", "10", option);
        } else if ("attachsize".equals(mid)) {
            option.put(5, "5M");
            option.put(10, "10M");
            option.put(20, "20M");
            option.put(50, "50M");
            option.put(100, "100M");
            initCustom(custom, "��������", "addsize", "10M", option);
        } else if ("visit".equals(mid)) {
            option.put(5, "5");
            option.put(10, "10");
            option.put(20, "20");
            option.put(50, "50");
            initCustom(custom, "���ʺ�����", "maxvisit", "10", option);
        } else if ("gift".equals(mid)) {
            option.put(5, "5");
            option.put(10, "10");
            option.put(20, "20");
            option.put(50, "50");
            option.put(100, "100");
            option.put(999, "����");
            initCustom(custom, "ÿ�ݻ�����ֵ", "maxchunk", "20", option);
        } else if ("viewmagic".equals(mid)) {
            option.put(5, "5");
            option.put(10, "10");
            option.put(20, "20");
            option.put(50, "50");
            option.put(999, "����");
            initCustom(custom, "���鿴��", "maxview", "10", option);
        } else if ("viewvisitor".equals(mid)) {
            option.put(5, "5");
            option.put(10, "10");
            option.put(20, "20");
            option.put(50, "50");
            initCustom(custom, "���鿴��", "maxview", "10", option);
        } else if ("detector".equals(mid)) {
            option.put(5, "5");
            option.put(10, "10");
            option.put(20, "20");
            option.put(50, "50");
            initCustom(custom, "���̽����", "maxdetect", "10", option);
        } else if ("call".equals(mid)) {
            option.put(5, "5");
            option.put(10, "10");
            option.put(20, "20");
            option.put(50, "50");
            initCustom(custom, "��������", "maxcall", "10", option);
        }
        return custom;
    }

    private void initCustom(Map<String, Object> custom, String title, String name, String desc, Map<Integer, String> option) {
        custom.put("title", title);
        custom.put("name", name);
        custom.put("desc", desc);
        custom.put("option", option);
    }
}
