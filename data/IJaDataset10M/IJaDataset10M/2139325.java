package ces.research.oa.document.incept.action;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.SimpleFormatter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import ces.arch.action.ListAction;
import ces.arch.bo.BusinessException;
import ces.arch.form.BaseForm;
import ces.arch.query.ListQury;
import ces.arch.query.QueryParser;
import ces.platform.bcm.sui.objtype.DateTimeText;
import ces.platform.system.dbaccess.Organize;
import ces.platform.system.dbaccess.User;
import ces.research.oa.document.incept.form.DocListQueryForm;
import ces.research.oa.document.util.OAConstants;
import ces.research.oa.entity.InceptPojo;

/**
 * 
 * <p>
 * Title: �Ǽ����б�
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class DocListAction extends ListAction {

    public DocListAction() {
    }

    private Date getDate(String time) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        try {
            date = fmt.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date(date.getTime());
    }

    public ActionForward process(ActionMapping mapping, BaseForm form, HttpServletRequest request, HttpServletResponse response, ActionErrors errors) throws BusinessException {
        User user = (User) request.getSession().getAttribute(OAConstants.ATTRIBUTE_USER);
        if (user == null) {
            return mapping.findForward(OAConstants.FORWARD_LOGIN);
        }
        String doaction = request.getParameter("doAction");
        String userId = String.valueOf(user.getUserID());
        Organize org = null;
        try {
            org = user.getOrgOfUser();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String orgName = "";
        String sqltemp2 = "";
        if (org != null) {
            orgName = String.valueOf(org.getOrganizeID());
        }
        DocListQueryForm queryForm = (DocListQueryForm) form;
        if (doaction != null) {
            queryForm.setDoAction(doaction);
        }
        ListQury filter = new ListQury();
        filter.setCursor(queryForm.getCurrentPage() - 1);
        filter.setNumPerPage(queryForm.getNumPerPage());
        if ("true".equals(request.getParameter("del"))) {
            String[] ids = request.getParameterValues("chk");
            for (int i = 0; i < ids.length; i++) {
                getBo().delete(InceptPojo.class, new Long(ids[i]));
            }
        }
        if ("registerList".equals(queryForm.getDoAction())) {
            queryForm.setUserId(String.valueOf(user.getUserID()));
            queryForm.setProcessInstanceId("0");
        } else if ("allDocList".equals(queryForm.getDoAction())) {
            queryForm.setProcessInstanceId(null);
            queryForm.setExceptPid("0");
            queryForm.setUserId(null);
        } else if ("docList".equals(queryForm.getDoAction())) {
            queryForm.setProcessInstanceId(null);
            queryForm.setExceptPid(null);
            queryForm.setUserId(null);
        } else if ("docListPrint".equals(queryForm.getDoAction())) {
            queryForm.setProcessInstanceId(null);
            queryForm.setExceptPid("0");
            queryForm.setUserId(null);
        } else if ("qiandocList".equals(queryForm.getDoAction())) {
            queryForm.setProcessInstanceId(null);
            queryForm.setExceptPid("0");
            queryForm.setUserId(null);
            queryForm.setPage("48");
        } else if ("docSecretSearch".equals(queryForm.getDoAction())) {
            queryForm.setProcessInstanceId(null);
            queryForm.setExceptPid(null);
            queryForm.setUserId(null);
            queryForm.setNumPerPage(99999);
        } else if ("secretList".equals(queryForm.getDoAction())) {
            String secret = request.getParameter("secret") == null ? null : request.getParameter("secret");
            String begin = request.getParameter("comeDate") == null ? null : request.getParameter("comeDate");
            String end = request.getParameter("leaveDate") == null ? null : request.getParameter("leaveDate");
            if (begin != null && !"".equals(begin)) {
                queryForm.setTimeBegin(getDate(begin) == null ? null : getDate(begin));
            }
            if (end != null && !"".equals(end)) {
                queryForm.setTimeEnd(getDate(end) == null ? null : getDate(end));
            }
            queryForm.setProcessInstanceId(null);
            queryForm.setExceptPid(null);
            queryForm.setUserId(null);
            queryForm.setSecret(secret);
        } else {
            request.setAttribute("Exception", new Exception("δԤ�ϵĲ�ѯ����:" + queryForm.getDoAction()));
            return mapping.findForward("fail");
        }
        queryForm.setOrderBy("id");
        queryForm.setOrderType("desc");
        QueryParser parser = new QueryParser(queryForm);
        filter = parser.parse();
        filter.setOrderby("id");
        filter.setOrderType("desc");
        String oldsql = filter.getQuery();
        int whereIndex = oldsql.indexOf(" where ");
        int oindex = oldsql.indexOf(" order by ");
        if (whereIndex == -1) {
            if (oindex == -1) {
                filter.setQuery(oldsql + " where 0 = 0 ");
            } else {
                String presql = oldsql.substring(0, oindex);
                String osql = oldsql.substring(oindex);
                filter.setQuery(presql + " where 0 = 0 " + osql);
            }
        }
        oldsql = filter.getQuery();
        oindex = oldsql.indexOf(" order by ");
        if (oindex != -1) {
            String presql = oldsql.substring(0, oindex);
            String osql = oldsql.substring(oindex);
            filter.setQuery(presql + sqltemp2 + osql);
        } else {
            filter.setQuery(oldsql + " " + sqltemp2);
        }
        List voList = null;
        try {
            voList = doFind(mapping, queryForm, request, response, errors, filter);
        } catch (Exception e) {
            voList = new ArrayList();
            log.error("����Ĳ�ѯ���������⣬���²�ѯ����" + e.getMessage());
        }
        queryForm.setTotalNum(filter.getTotalNum());
        request.setAttribute("voList", voList);
        request.setAttribute("form", queryForm);
        if ("1".equals(request.getParameter("printList"))) {
            return mapping.findForward("listPrints");
        }
        return mapping.findForward(queryForm.getDoAction());
    }
}
