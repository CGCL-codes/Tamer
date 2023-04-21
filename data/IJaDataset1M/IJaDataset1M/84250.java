package com.creawor.hz_market.t_community;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.hibernate.HibernateException;
import com.creawor.hz_market.bean.UserDetails;
import com.creawor.hz_market.t_basestation.t_basestation_QueryMap;
import com.creawor.hz_market.util.LoginUtils;
import com.creawor.imei.base.BaseAction;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.creawor.imei.util.*;
import com.creawor.km.util.CatcheUtil;
import com.creawor.km.util.RoleCtl;

public class t_community_Manager extends BaseAction {

    /**��ʾlist.jsp�� ��ѯ����jspҳ��*/
    public String doList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pagenos = request.getParameter("pageno");
        String pagesizes = request.getParameter("pagesize");
        UserDetails user = LoginUtils.getLoginUser(request);
        try {
            this.pageno = Integer.valueOf(pagenos);
        } catch (NumberFormatException e) {
            this.pageno = new Integer(1);
        }
        if (pagesizes != null) this.pagesize = Integer.valueOf(pagesizes.trim());
        t_community_QueryMap query_map = new t_community_QueryMap();
        query_map.pageno = this.pageno;
        query_map.pagesize = this.pagesize;
        java.util.Iterator sl = null;
        List list = null;
        String userCounty = user.getDeptCode();
        if (sql_string == null || sql_string.equals("") || sql_string.equalsIgnoreCase("null")) {
            list = (List) CatcheUtil.getInstance().getSYSObject("t_communityList");
            if (!RoleCtl.lavel2(request, list)) {
                list = query_map.findAllByCounty(userCounty);
                CatcheUtil.getInstance().cacheSYSObj("t_communityList", list);
            } else {
                list = query_map.findAllList();
                CatcheUtil.getInstance().cacheSYSObj("t_communityList", list);
            }
        } else {
            list = query_map.findAllList(this.sql_string, this.sql_param);
            request.setAttribute("sql_string", this.sql_string);
            request.setAttribute("sql_param", this.sql_param_str);
        }
        List newList = RoleCtl.communityFilter(request, list);
        request.setAttribute("sl", newList.iterator());
        request.setAttribute("curpageno", pageno);
        if (pageno.intValue() > 1) request.setAttribute("prepage", new Integer(pageno.intValue() - 1)); else request.setAttribute("prepage", new Integer(1));
        int totalpage = 0;
        if ((query_map.totalrow % pagesize.intValue()) == 0) totalpage = query_map.totalrow / pagesize.intValue(); else totalpage = query_map.totalrow / pagesize.intValue() + 1;
        request.setAttribute("totalpage", new Integer(totalpage));
        if (pageno.intValue() < totalpage) request.setAttribute("nextpage", new Integer(pageno.intValue() + 1)); else request.setAttribute("nextpage", new Integer(totalpage));
        return "list";
    }

    /**������ϸ��Ϣ��ʾ����*/
    public String doDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pageno;
        try {
            request.setAttribute("sql_string", this.sql_string);
            request.setAttribute("sql_param", this.sql_param_str);
            pageno = request.getParameter("pageno");
            request.setAttribute("pageno", pageno);
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        }
        t_community_QueryMap sqm = new t_community_QueryMap();
        String uid = request.getParameter("id");
        t_community_Form sf = null;
        sf = sqm.getByID(uid);
        request.setAttribute("t_community_Form", sf);
        return "detail";
    }

    /**��������ҳ��*/
    public String doCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String pageno;
        try {
            request.setAttribute("sql_string", this.sql_string);
            request.setAttribute("sql_param", this.sql_param_str);
            pageno = request.getParameter("pageno");
            request.setAttribute("pageno", pageno);
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        }
        request.setAttribute("t_community_Form", new t_community_Form());
        return "addpage";
    }

    /**���������ļ�¼**************/
    public String doAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        t_community_EditMap editMap = new t_community_EditMap();
        try {
            t_community_Form vo = null;
            vo = (t_community_Form) form;
            editMap.add(vo);
        } catch (HibernateException e) {
            ActionErrors errors = new ActionErrors();
            errors.add("org.apache.struts.action.GLOBAL_ERROR", new ActionError("error.database.save", e.toString()));
            saveErrors(request, errors);
            e.printStackTrace();
            request.setAttribute("t_community_Form", form);
            return "addpage";
        }
        return "aftersave";
    }

    /**�����ѯҳ��*/
    public String doQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String pageno;
        try {
            pageno = request.getParameter("pageno");
            request.setAttribute("pageno", pageno);
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        }
        return "searchpage";
    }

    /**�����޸�ҳ��*/
    public String doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pageno;
        try {
            request.setAttribute("sql_string", this.sql_string);
            request.setAttribute("sql_param", this.sql_param_str);
            pageno = request.getParameter("pageno");
            request.setAttribute("pageno", pageno);
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        }
        t_community_QueryMap query_map = new t_community_QueryMap();
        String uid = request.getParameter("id");
        t_community_Form vo = null;
        query_map.session.flush();
        vo = query_map.getByID(uid);
        request.setAttribute("t_community_Form", vo);
        return "editpage";
    }

    /**�����޸ĵļ�¼**************/
    public String doUpdate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            t_community_EditMap edit_map = new t_community_EditMap();
            t_community_Form vo = null;
            vo = (t_community_Form) form;
            edit_map.update(vo);
        } catch (HibernateException e) {
            ActionErrors errors = new ActionErrors();
            errors.add("org.apache.struts.action.GLOBAL_ERROR", new ActionError("error.database.save", e.toString()));
            saveErrors(request, errors);
            e.printStackTrace();
            request.setAttribute("t_community_Form", form);
            return "editpage";
        }
        return "aftersave";
    }

    /**ɾ���¼**************/
    public String doDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        t_community_EditMap edit_map = new t_community_EditMap();
        String uid = request.getParameter("id");
        try {
            edit_map.delete(uid);
        } catch (HibernateException e) {
            ActionErrors errors = new ActionErrors();
            errors.add("org.apache.struts.action.GLOBAL_ERROR", new ActionError("error.database.save", e.toString()));
            saveErrors(request, errors);
            e.printStackTrace();
            return "list";
        }
        return "aftersave";
    }
}
