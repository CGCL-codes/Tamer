package cn.jsprun.struts.action;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import cn.jsprun.service.system.DataBaseService;
import cn.jsprun.utils.BeanFactory;
import cn.jsprun.utils.Common;
import cn.jsprun.utils.FormDataCheck;

public class PmpruneAction extends DispatchAction {

    private DataBaseService dataBaseService = (DataBaseService) BeanFactory.getBean("dataBaseService");

    public ActionForward batchPmprune(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String prunesubmit = request.getParameter("prunesubmit");
        if (prunesubmit == null) {
            Common.requestforward(response, "admincp.jsp?action=pmprune");
            return null;
        }
        String ignorenew = request.getParameter("ignorenew");
        String days = request.getParameter("days");
        String cins = request.getParameter("cins");
        String users = request.getParameter("users");
        String srchtype = request.getParameter("srchtype");
        String srchtxt = request.getParameter("srchtxt");
        HttpSession session = request.getSession();
        int timestamp = (Integer) (request.getAttribute("timestamp"));
        if (!FormDataCheck.isValueString(days)) {
            String info = "��û������Ҫɾ�����Ϣ��ʱ�䷶Χ���뷵���޸ġ�";
            request.setAttribute("errorInfo", info);
            return mapping.findForward("error");
        }
        StringBuffer sqlbuffer = new StringBuffer("select pmid from jrun_pms");
        String where = " where ";
        String and = " ";
        if (ignorenew != null) {
            sqlbuffer.append(where);
            where = " ";
            sqlbuffer.append(and);
            and = " and ";
            sqlbuffer.append("new=0");
        }
        if (FormDataCheck.isNum(days) && !days.equals("0")) {
            sqlbuffer.append(where);
            where = " ";
            sqlbuffer.append(and);
            and = " and ";
            int day = Common.toDigit(days);
            sqlbuffer.append("dateline <=" + (timestamp - day * 86400));
        }
        if (users != null && !users.equals("")) {
            String uid = "0";
            String biner = cins == null ? "" : " BINARY ";
            List<Map<String, String>> members = dataBaseService.executeQuery("select uid from jrun_members where " + biner + " username in ('" + users + "')");
            for (Map<String, String> member : members) {
                uid += "," + member.get("uid");
            }
            sqlbuffer.append(where);
            where = " ";
            sqlbuffer.append(and);
            and = " and ";
            sqlbuffer.append("(msgfromid IN (" + uid + ") AND folder='outbox') OR (msgtoid IN (" + uid + ") AND folder='inbox')");
        }
        if (srchtxt != null && !srchtxt.equals("")) {
            srchtxt = srchtxt.replaceAll("\\*", "%");
            srchtxt = srchtxt.toLowerCase();
            sqlbuffer.append(where);
            where = " ";
            sqlbuffer.append(and);
            and = " and ";
            if (Common.matches(srchtxt, "[and|\\+|&|\\s+]") && !Common.matches(srchtxt, "[or|\\|]")) {
                srchtxt = srchtxt.replaceAll("( and |&| )", "+");
                String[] keyword = srchtxt.split("\\+");
                if (srchtype.equals("title")) {
                    for (int i = 0; i < keyword.length; i++) {
                        sqlbuffer.append("subject like '%" + keyword[i].trim() + "%'");
                        sqlbuffer.append(and);
                    }
                } else {
                    for (int i = 0; i < keyword.length; i++) {
                        sqlbuffer.append("message like '%" + keyword[i].trim() + "%'");
                        sqlbuffer.append(and);
                    }
                }
            } else {
                srchtxt = srchtxt.replaceAll("( or |\\|)", "+");
                String[] keyword = srchtxt.split("\\+");
                if (srchtype.equals("title")) {
                    sqlbuffer.append("(");
                    for (int i = 0; i < keyword.length; i++) {
                        sqlbuffer.append("subject like '%" + keyword[i].trim() + "%'");
                        sqlbuffer.append(" or ");
                    }
                } else {
                    sqlbuffer.append("(");
                    for (int i = 0; i < keyword.length; i++) {
                        sqlbuffer.append("message like '%" + keyword[i].trim() + "%'");
                        sqlbuffer.append(" or ");
                    }
                }
                int length = sqlbuffer.length();
                sqlbuffer.delete(length - 4, length);
                sqlbuffer.append(")");
            }
        }
        List<Map<String, String>> pmslist = dataBaseService.executeQuery(sqlbuffer.toString());
        int countIDs = 0;
        StringBuffer pmidbuffer = new StringBuffer("0");
        if (pmslist != null && pmslist.size() > 0) {
            countIDs = pmslist.size();
            for (Map<String, String> pms : pmslist) {
                pmidbuffer.append("," + pms.get("pmid"));
            }
        }
        pmslist = null;
        String confirmInfo = "���������ɻָ�����ȷ��Ҫɾ���������� " + countIDs + " ������Ϣ��";
        String commitPath = request.getContextPath() + "/pmprune.do?pmpruneaction=deletePmprun";
        request.setAttribute("commitPath", commitPath);
        request.setAttribute("confirmInfo", confirmInfo);
        session.setAttribute("pmidbuffer", pmidbuffer.toString());
        session.setAttribute("countIds", countIDs);
        return mapping.findForward("confirm");
    }

    public ActionForward deletePmprun(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String ids = (String) session.getAttribute("pmidbuffer");
        if (ids == null) {
            Common.requestforward(response, "admincp.jsp?action=pmprune");
            return null;
        }
        session.removeAttribute("pmidbuffer");
        dataBaseService.runQuery("delete from jrun_pms where pmid in (" + ids + ")");
        int countIds = (Integer) session.getAttribute("countIds");
        String info = "��������� " + countIds + " ������Ϣ�ɹ�ɾ��";
        request.setAttribute("resultInfo", info);
        return mapping.findForward("result");
    }
}
