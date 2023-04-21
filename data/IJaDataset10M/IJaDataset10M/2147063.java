package ces.research.oa.document.util.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.context.ApplicationContext;
import net.sf.json.JsonConfig;
import ces.arch.action.ListAction;
import ces.arch.bo.BusinessException;
import ces.arch.bo.IBo;
import ces.arch.form.BaseForm;
import ces.arch.form.ListForm;
import ces.arch.query.ListQury;
import ces.arch.query.QueryParser;
import ces.coral.dbo.DBHandle;
import ces.platform.system.dbaccess.Organize;
import ces.platform.system.dbaccess.User;
import ces.research.oa.document.util.ApplicationContextFactory;
import ces.research.oa.document.util.DateUtil;
import ces.research.oa.document.util.LeaderUtil;
import ces.research.oa.document.util.OAConstants;
import ces.research.oa.document.util.form.UtilForm;
import ces.research.oa.entity.InspectPojo;
import ces.research.oa.entity.LookPojo;
import ces.research.oa.util.DateJsonValueProcessor;

public class UtilAction extends ListAction {

    public ActionForward process(ActionMapping mapping, BaseForm form, HttpServletRequest request, HttpServletResponse response, ActionErrors errors) throws BusinessException {
        response.setContentType("text/html;charset=GBK");
        String doAction = request.getParameter("doAction") == null ? "" : request.getParameter("doAction");
        PrintWriter pw = null;
        UtilForm uf = (UtilForm) form;
        ApplicationContext appContext = ApplicationContextFactory.getContext();
        IBo bo = (IBo) (appContext.getBean("DefaultBo"));
        User user = (User) request.getSession().getAttribute(OAConstants.ATTRIBUTE_USER);
        if (null == doAction || "".equals(doAction)) {
            doAction = uf.getDoAction();
        }
        if ("getDept".equals(doAction)) {
            String deptData = "";
            try {
                Organize org = user.getOrgOfUser();
                Vector allOrg = org.getAllOrg();
                Organize orgv = null;
                for (int i = 0; i < allOrg.size(); i++) {
                    orgv = (Organize) allOrg.get(i);
                    if (orgv.getOrganizeID() > 0) {
                        deptData += orgv.getOrganizeName() + "$=" + orgv.getOrganizeName() + ";";
                    }
                }
                pw = response.getWriter();
                pw.write(deptData);
                pw.close();
                return mapping.findForward(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("looked".equals(doAction)) {
            bo.insertOrUpdate(new LookPojo(String.valueOf(user.getUserID()), user.getUserName(), DateUtil.getSysdate(), DateUtil.getSysTime()));
            return null;
        } else if ("islooked".equals(doAction)) {
            List list = bo.getDao().getHibernateTemplate().find("from LookPojo where user_id='" + String.valueOf(user.getUserID()) + "'  and  look_date='" + DateUtil.getSysdate() + "'");
            try {
                pw = response.getWriter();
            } catch (IOException e) {
                System.out.println("get PrintWriter error:" + e.getMessage());
                e.printStackTrace();
            }
            if (null != list && list.size() > 0) {
                pw.write("yes");
            } else {
                pw.write("no");
            }
        }
        if ("numCode".equals(doAction)) {
            String sign = LeaderUtil.isok(uf.getModuleName(), uf.getWenhao(), uf.getYear());
            try {
                pw = response.getWriter();
                pw.write(sign);
                pw.close();
            } catch (IOException e) {
                System.out.println("get PrintWriter error:" + e.getMessage());
                e.printStackTrace();
            }
        }
        if ("duban".equals(doAction)) {
            List data = bo.getDao().getHibernateTemplate().find(" from InspectPojo where REGISTER_USER_ID='" + user.getUserID() + "' order by id ");
            data = doit(data);
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.registerJsonValueProcessor(java.sql.Date.class, new DateJsonValueProcessor());
            try {
                pw = response.getWriter();
                String realData = JSONArray.fromObject(data, jsonConfig).toString();
                pw.write(realData);
                pw.close();
            } catch (IOException e) {
                System.out.println("in duban method get PrintWriter error:" + e.getMessage());
                e.printStackTrace();
            }
        }
        if ("allQuery".equals(doAction)) {
            uf.setOrderBy("id");
            uf.setOrderType("desc");
            QueryParser qp = new QueryParser((ListForm) form);
            ListQury filter = qp.parse();
            filter.setNumPerPage(25);
            filter.setOrderby("id");
            filter.setOrderType("desc");
            List result = getBo().find(filter);
            if (null != result) {
                uf.setTotalNum(filter.getTotalNum());
            } else {
                uf.setTotalNum(0);
            }
            uf.setResult(result);
            uf.setNumPerPage(25);
            request.setAttribute("form", uf);
            return mapping.findForward("allQuery");
        }
        if ("showData".equals(doAction)) {
            String query = "<query id=\"0\">" + "<target pojo=\"ShouyePojo\" as=\"o\"/>" + "<compare target=\"o.userid\" name=\"userid\" operator=\"EQ\"/>" + "<compare target=\"o.regTime\" name=\"beginDate\" operator=\"GE\"/>" + "<compare target=\"o.regTime\" name=\"endDate\" operator=\"LE\"/>" + "<compare target=\"o.title\" name=\"title\" operator=\"LK\"/>" + "<compare target=\"o.model\" name=\"type\" operator=\"EQ\"/>" + "<compare target=\"o.regUser\" name=\"uname\" operator=\"EQ\"/>" + "<compare target=\"o.dept\" name=\"dept\" operator=\"LK\"/>" + "</query>";
            uf.setQuery(query);
            uf.setOrderBy("id");
            uf.setOrderType("desc");
            uf.setUserid(String.valueOf(user.getUserID()));
            QueryParser qp = new QueryParser((ListForm) form);
            ListQury filter = qp.parse();
            filter.setNumPerPage(25);
            filter.setOrderby("id");
            filter.setOrderType("desc");
            List result = getBo().find(filter);
            if (null != result) {
                uf.setTotalNum(filter.getTotalNum());
            } else {
                uf.setTotalNum(0);
            }
            uf.setResult(result);
            uf.setNumPerPage(25);
            request.setAttribute("form", uf);
            return mapping.findForward("showData");
        }
        if ("ldps".equals(doAction)) {
            String query = "<query id=\"0\">" + "<target pojo=\"LdpsPojo\" as=\"o\"/>" + "<compare target=\"o.moduleName\" name=\"moduleName\" operator=\"LK\"/>" + "<compare target=\"o.fileNumber\" name=\"wenhao\" operator=\"LK\"/>" + "<compare target=\"o.title\" name=\"title\" operator=\"LK\"/>" + "<compare target=\"o.creatorName\" name=\"postilUserName\" operator=\"LK\"/>" + "<compare target=\"o.content\" name=\"postilContent\" operator=\"LK\"/>" + "<compare target=\"o.registerTime\" name=\"fromDate\" operator=\"GE\"/>" + "<compare target=\"o.registerTime\" name=\"toDate\" operator=\"LE\"/>" + "<compare target=\"o.postilDate\" name=\"postilBeginDate\" operator=\"GE\"/>" + "<compare target=\"o.postilDate\" name=\"postilEndDate\" operator=\"LE\"/>" + "</query>";
            uf.setQuery(query);
            uf.setOrderBy("id");
            uf.setOrderType("desc");
            QueryParser qp = new QueryParser((ListForm) form);
            ListQury filter = qp.parse();
            filter.setNumPerPage(uf.getNumPerPage());
            filter.setOrderby("id");
            filter.setOrderType("desc");
            List result = getBo().find(filter);
            if (null != result) {
                uf.setTotalNum(filter.getTotalNum());
            } else {
                uf.setTotalNum(0);
            }
            uf.setResult(result);
            uf.setNumPerPage(filter.getNumPerPage());
            request.setAttribute("fromDate", uf.getFromDate());
            request.setAttribute("toDate", uf.getToDate());
            request.setAttribute("form", uf);
            return mapping.findForward("ldps");
        }
        return null;
    }

    public String CharsetUTF(String value) {
        if (null != value && !"".equals(value)) {
            try {
                return new String(value.getBytes(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                System.out.println("ת���ַ��������ˣ�");
                e.printStackTrace();
            }
        }
        return "";
    }

    public List doit(List data) {
        Collections.sort(data, new Comparator() {

            public int compare(Object obja, Object objb) {
                try {
                    if (null != obja && null != objb && obja instanceof InspectPojo && obja instanceof InspectPojo) {
                        InspectPojo a = (InspectPojo) obja;
                        InspectPojo b = (InspectPojo) objb;
                        if (a.getRegisterTime().compareTo(b.getRegisterTime()) == 0) {
                            String typea = a.getInspectType();
                            String typeb = b.getInspectType();
                            if (typea.equals(typeb)) {
                                return 0;
                            } else {
                                if (typea.equals("�ؼ�")) {
                                    return -1;
                                }
                                if (typeb.equals("�ؼ�")) {
                                    return 1;
                                }
                                if (typea.equals("����")) {
                                    return -1;
                                }
                                if (typeb.equals("����")) {
                                    return 1;
                                }
                                if (typea.equals("�Ӽ�")) {
                                    return -1;
                                }
                                if (typeb.equals("�Ӽ�")) {
                                    return 1;
                                }
                                if (typea.equals("��ʱ���")) {
                                    return -1;
                                }
                                if (typeb.equals("��ʱ���")) {
                                    return 1;
                                }
                                return 0;
                            }
                        } else {
                            return a.getRegisterTime().compareTo(b.getRegisterTime());
                        }
                    }
                    return 0;
                } catch (Exception e) {
                    System.out.println("====" + e.getMessage());
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        return data;
    }
}
