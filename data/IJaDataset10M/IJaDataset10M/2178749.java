package com.creawor.hz_market.resource.guanxian_resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.hibernate.HibernateException;
import com.creawor.hz_market.bean.UserDetails;
import com.creawor.hz_market.t_basestation.t_basestation_QueryMap;
import com.creawor.hz_market.t_information.t_information;
import com.creawor.hz_market.t_role.t_role;
import com.creawor.hz_market.t_user.t_user;
import com.creawor.hz_market.util.LoginUtils;
import com.creawor.imei.base.BaseAction;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.creawor.imei.util.*;
import com.creawor.km.util.CatcheUtil;
import com.creawor.km.util.RoleCtl;

public class GuanxianResource_Manager extends BaseAction {

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
        GuanxianResource_QueryMap query_map = new GuanxianResource_QueryMap();
        query_map.pageno = this.pageno;
        query_map.pagesize = this.pagesize;
        java.util.Iterator sl = null;
        List list = null;
        String userCounty = user.getDeptCode();
        if (sql_string == null || sql_string.equals("") || sql_string.equalsIgnoreCase("null")) {
            list = (List) CatcheUtil.getInstance().getSYSObject("guanxian_resourceList");
            if (!RoleCtl.lavel2(request, list)) {
                list = query_map.findAllByCounty(userCounty);
                CatcheUtil.getInstance().cacheSYSObj("guanxian_resourceList", list);
            } else {
                list = query_map.findAllList();
                CatcheUtil.getInstance().cacheSYSObj("guanxian_resourceList", list);
            }
        } else {
            sl = query_map.findAll(this.sql_string, this.sql_param);
            request.setAttribute("sql_string", this.sql_string);
            request.setAttribute("sql_param", this.sql_param_str);
        }
        List newList = RoleCtl.groupFilter(request, list);
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
        GuanxianResource_QueryMap sqm = new GuanxianResource_QueryMap();
        String uid = request.getParameter("id");
        GuanxianResource_Form sf = null;
        sf = sqm.getByID(uid);
        request.setAttribute("GuanxianResource_Form", sf);
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
        request.setAttribute("GuanxianResource_Form", new GuanxianResource_Form());
        return "addpage";
    }

    /**��������ļ��?**************/
    public String doAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GuanxianResource_EditMap editMap = new GuanxianResource_EditMap();
        try {
            GuanxianResource_Form vo = null;
            vo = (GuanxianResource_Form) form;
            editMap.add(vo);
        } catch (HibernateException e) {
            ActionErrors errors = new ActionErrors();
            errors.add("org.apache.struts.action.GLOBAL_ERROR", new ActionError("error.database.save", e.toString()));
            saveErrors(request, errors);
            e.printStackTrace();
            request.setAttribute("GuanxianResource_Form", form);
            return "addpage";
        }
        return "aftersave";
    }

    /**�����ѯҳ��?*/
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
        GuanxianResource_QueryMap query_map = new GuanxianResource_QueryMap();
        String uid = request.getParameter("id");
        GuanxianResource_Form vo = null;
        query_map.session.flush();
        vo = query_map.getByID(uid);
        request.setAttribute("GuanxianResource_Form", vo);
        return "editpage";
    }

    /**�����޸ĵļ�¼**************/
    public String doUpdate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            GuanxianResource_EditMap edit_map = new GuanxianResource_EditMap();
            GuanxianResource_Form vo = null;
            vo = (GuanxianResource_Form) form;
            edit_map.update(vo);
        } catch (HibernateException e) {
            ActionErrors errors = new ActionErrors();
            errors.add("org.apache.struts.action.GLOBAL_ERROR", new ActionError("error.database.save", e.toString()));
            saveErrors(request, errors);
            e.printStackTrace();
            request.setAttribute("GuanxianResource_Form", form);
            return "editpage";
        }
        return "aftersave";
    }

    /**ɾ����?**************/
    public String doDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GuanxianResource_EditMap edit_map = new GuanxianResource_EditMap();
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
