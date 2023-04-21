package ces.coffice.dutymanage.ui.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import ces.coffice.common.base.Const;
import ces.coffice.dutymanage.facade.PlanFac;
import ces.coffice.dutymanage.vo.Plan;
import ces.coffice.dutymanage.vo.PlanList;
import ces.coral.lang.StringUtil;
import ces.coral.log.Logger;
import ces.platform.system.common.Constant;
import ces.platform.system.common.IdGenerator;
import ces.platform.system.common.Translate;
import ces.platform.system.common.XmlInfo;
import ces.platform.system.dbaccess.User;
import ces.platform.system.util.SystemUtil;

public class PlanAction extends org.apache.struts.action.Action {

    static Logger logger = new Logger(PlanAction.class);

    /**
     * operator=list ȡ�б����
     * operator=delete ɾ��ѡ�е����
     * operator=display ���ֵ�հ���
     * operator=add ����ֵ�հ���
     * operator=edit �޸�ֵ�հ���
     * operator=issue ����
     * operator=withDrawIssue ȡ��
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String target = "coffice_alert";
        String operator = request.getParameter("operator");
        User user = (User) request.getSession().getAttribute("user");
        String userId = String.valueOf(user.getUserID());
        String userName = user.getUserName();
        PlanFac fac = new PlanFac();
        if ("list".equals(operator)) {
            String planName = request.getParameter("planName") == null ? "" : Translate.translate(request.getParameter("planName"), Constant.PARAM_POST);
            String beginDate = request.getParameter("beginTime") == null ? "" : request.getParameter("beginTime");
            String endDate = request.getParameter("endTime") == null ? "" : request.getParameter("endTime");
            String docId = request.getParameter("docId") == null ? "" : Translate.translate(request.getParameter("docId"), Constant.PARAM_POST);
            String planerName = request.getParameter("planerName") == null ? "" : Translate.translate(request.getParameter("planerName"), Constant.PARAM_POST);
            ;
            try {
                String condition = "";
                if (!beginDate.equals("")) {
                    if (XmlInfo.getInstance().getSysDataBaseType().equalsIgnoreCase(Constant.SQLSERVER) || XmlInfo.getInstance().getSysDataBaseType().equalsIgnoreCase(Constant.MYSQL)) {
                        condition += " and begin_date >= '" + beginDate + "' ";
                    } else {
                        condition += " and begin_date >= to_date('" + beginDate + "','yyyy-mm-dd') ";
                    }
                }
                if (!endDate.equals("")) {
                    if (XmlInfo.getInstance().getSysDataBaseType().equalsIgnoreCase(Constant.SQLSERVER) || XmlInfo.getInstance().getSysDataBaseType().equalsIgnoreCase(Constant.MYSQL)) {
                        condition += " and begin_date <= '" + endDate + " 23:59:59' ";
                    } else {
                        condition += " and begin_date <= to_date('" + endDate + " 23:59:59','yyyy-mm-dd hh24:mi:ss') ";
                    }
                }
                if (!planName.equals("")) {
                    condition += " and duty_name like '%" + SystemUtil.strEscape(planName);
                }
                if (!docId.equals("") && !docId.equals("����")) {
                    if (docId.equals("δ����")) {
                        condition += " and doc_id = '-1' ";
                    } else {
                        condition += " and doc_id != '-1' ";
                    }
                }
                condition += " order by begin_date desc";
                Vector rs = fac.getPlans(condition);
                if (!planerName.equals("")) {
                    Vector tmp = new Vector();
                    for (int i = 0; i < rs.size(); i++) {
                        Plan plan = (Plan) rs.get(i);
                        if (plan.getPlanerName().indexOf(planerName) != -1) {
                            tmp.add(plan);
                        }
                    }
                    request.setAttribute("rs", tmp);
                } else request.setAttribute("rs", rs);
                target = "list";
                request.setAttribute("planName", planName);
                request.setAttribute("beginTime", beginDate);
                request.setAttribute("endTime", endDate);
                request.setAttribute("docId", docId);
                request.setAttribute("planerName", planerName);
            } catch (Exception ex) {
                logger.error(ex);
                ex.printStackTrace();
                request.setAttribute("status", Const.FAILED_TO_SELF);
                request.setAttribute("message", "���?" + ex.getMessage());
            }
        } else if ("add".equals(operator)) {
            target = "add";
        } else if ("save".equals(operator)) {
            String planName = Translate.translate(request.getParameter("planName"), Constant.PARAM_POST);
            ;
            String beginTime = request.getParameter("beginTime");
            String endTime = request.getParameter("endTime");
            String[] planersId = request.getParameterValues("planersId");
            String[] planersName = request.getParameterValues("planersName");
            String[] time1 = request.getParameterValues("dateTime1");
            String[] time2 = request.getParameterValues("dateTime2");
            try {
                String fatherId = String.valueOf(IdGenerator.getInstance().getId(IdGenerator.GEN_ID_COFFICE_DUTY_PLAN));
                Vector list = new Vector();
                for (int i = 0; i < time1.length; i++) {
                    String childId = String.valueOf(IdGenerator.getInstance().getId(IdGenerator.GEN_ID_COFFICE_DUTY_PLANLIST));
                    PlanList pl = new PlanList(Integer.parseInt(childId));
                    pl.setDateTime1(time1[i]);
                    pl.setDateTime2(time2[i]);
                    pl.setPlanId(Integer.parseInt(fatherId));
                    pl.setPlaners(Translate.translate(planersName[i], Constant.PARAM_POST));
                    pl.setPlanersId(planersId[i]);
                    list.add(pl);
                }
                Plan plan = new Plan(Integer.parseInt(fatherId));
                plan.setBeginTime(beginTime);
                plan.setEndTime(endTime);
                plan.setName(planName);
                plan.setPlanerId(Integer.parseInt(userId));
                plan.setPlaner(userName);
                plan.setDocId("-1");
                plan.setPlanerDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                fac.addPlan(plan, list);
                plan = fac.getPlan(fatherId);
                Vector planLists = fac.getPlanListByPlan(plan);
                request.setAttribute("plan", plan);
                request.setAttribute("planLists", planLists);
                target = "modify";
            } catch (Exception ex) {
                logger.error(ex);
                ex.printStackTrace();
                request.setAttribute("status", Const.FAILED_TO_CLOSE);
                request.setAttribute("message", ex.getMessage());
            }
        } else if ("delete".equals(operator)) {
            String ids = request.getParameter("ids");
            String[] planIds = StringUtil.split(ids, ",");
            try {
                for (int i = 0; i < planIds.length; i++) {
                    Plan plan = fac.getPlan(planIds[i]);
                    fac.deletePlan(plan);
                }
                request.setAttribute("status", Const.SUCCESS_TO_UP);
                request.setAttribute("message", "ɾ��ɹ���");
            } catch (Exception ex) {
                logger.error(ex);
                ex.printStackTrace();
                request.setAttribute("status", Const.FAILED_TO_UP);
                request.setAttribute("message", "���?" + ex.getMessage());
            }
        } else if ("modify".equals(operator)) {
            String planId = request.getParameter("planId");
            try {
                Plan plan = fac.getPlan(planId);
                Vector planLists = fac.getPlanListByPlan(plan);
                request.setAttribute("plan", plan);
                request.setAttribute("planLists", planLists);
                target = "modify";
            } catch (Exception ex) {
                logger.error(ex);
                ex.printStackTrace();
                request.setAttribute("status", Const.FAILED_TO_SELF);
                request.setAttribute("message", ex.getMessage());
            }
        } else if ("saveModify".equals(operator)) {
            String planId = request.getParameter("planId");
            String planName = Translate.translate(request.getParameter("planName"), Constant.PARAM_POST);
            ;
            String beginTime = request.getParameter("beginTime");
            String endTime = request.getParameter("endTime");
            String[] planersId = request.getParameterValues("planersId");
            String[] planersName = request.getParameterValues("planersName");
            String[] time1 = request.getParameterValues("dateTime1");
            String[] time2 = request.getParameterValues("dateTime2");
            try {
                Vector list = new Vector();
                for (int i = 0; i < time1.length; i++) {
                    String childId = String.valueOf(IdGenerator.getInstance().getId(IdGenerator.GEN_ID_COFFICE_DUTY_PLANLIST));
                    PlanList pl = new PlanList(Integer.parseInt(childId));
                    pl.setDateTime1(time1[i]);
                    pl.setDateTime2(time2[i]);
                    pl.setPlanId(Integer.parseInt(planId));
                    pl.setPlaners(Translate.translate(planersName[i], Constant.PARAM_POST));
                    pl.setPlanersId(planersId[i]);
                    list.add(pl);
                }
                Plan plan = fac.getPlan(planId);
                plan.setBeginTime(beginTime);
                plan.setEndTime(endTime);
                plan.setName(planName);
                plan.setPlanerId(Integer.parseInt(userId));
                plan.setPlaner(userName);
                plan.setPlanerDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                fac.updatePlan(plan, list);
                plan = fac.getPlan(planId);
                Vector planLists = fac.getPlanListByPlan(plan);
                request.setAttribute("plan", plan);
                request.setAttribute("planLists", planLists);
                target = "modify";
            } catch (Exception ex) {
                logger.error(ex);
                ex.printStackTrace();
                request.setAttribute("status", Const.FAILED_TO_CLOSE);
                request.setAttribute("message", ex.getMessage());
            }
        }
        return mapping.findForward(target);
    }
}
