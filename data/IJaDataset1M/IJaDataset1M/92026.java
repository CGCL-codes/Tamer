package com.aimluck.eip.schedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.commons.field.ALDateTimeField;
import com.aimluck.eip.cayenne.om.portlet.EipTTodo;
import com.aimluck.eip.cayenne.om.portlet.VEipTScheduleList;
import com.aimluck.eip.cayenne.om.security.TurbineUser;
import com.aimluck.eip.common.ALDBErrorException;
import com.aimluck.eip.common.ALEipUser;
import com.aimluck.eip.common.ALPageNotFoundException;
import com.aimluck.eip.modules.actions.common.ALAction;
import com.aimluck.eip.orm.Database;
import com.aimluck.eip.orm.query.SelectQuery;
import com.aimluck.eip.schedule.util.ScheduleUtils;
import com.aimluck.eip.todo.util.ToDoUtils;
import com.aimluck.eip.util.ALEipUtils;

/**
 * スケジュール1日表示の検索結果を管理するクラスです。
 * 
 */
public class CellScheduleOnedaySelectByMemberData extends CellScheduleOnedaySelectData {

    /** <code>logger</code> logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(CellScheduleOnedaySelectByMemberData.class.getName());

    /** <code>login_user</code> 表示対象ユーザー */
    private ALEipUser targerUser;

    /** <code>todoList</code> ToDo リスト */
    private List<ScheduleToDoResultData> todoList;

    /** ポートレット ID */
    private String portletId;

    /** ログインユーザID */
    private int userid;

    @Override
    public void init(ALAction action, RunData rundata, Context context) throws ALPageNotFoundException, ALDBErrorException {
        super.init(action, rundata, context);
        userid = ALEipUtils.getUserId(rundata);
        String s = rundata.getParameters().getString("selectedmember");
        if (s != null) {
            targerUser = ALEipUtils.getALEipUser(Integer.parseInt(s));
        } else {
            s = ALEipUtils.getTemp(rundata, context, "target_otheruser_id");
            targerUser = ALEipUtils.getALEipUser(Integer.parseInt(s));
        }
    }

    @Override
    protected List<VEipTScheduleList> getScheduleList(RunData rundata, Context context) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getViewDate().getValue());
        cal.add(Calendar.DATE, 1);
        cal.add(Calendar.MILLISECOND, -1);
        ALDateTimeField field = new ALDateTimeField();
        field.setValue(cal.getTime());
        return ScheduleUtils.getScheduleList(userid, getViewDate().getValue(), field.getValue(), Arrays.asList((int) targerUser.getUserId().getValue()), null);
    }

    @Override
    public void loadToDo(RunData rundata, Context context) {
        todoList = new ArrayList<ScheduleToDoResultData>();
        try {
            SelectQuery<EipTTodo> query = getSelectQueryForTodo(rundata, context);
            List<EipTTodo> todos = query.fetchList();
            int todosize = todos.size();
            for (int i = 0; i < todosize; i++) {
                EipTTodo record = todos.get(i);
                ScheduleToDoResultData rd = new ScheduleToDoResultData();
                rd.initField();
                String todo_url = getPortletURItoTodo(rundata, record.getTodoId().longValue(), portletId);
                rd.setTodoId(record.getTodoId().intValue());
                rd.setTodoName(record.getTodoName());
                rd.setUserId(record.getTurbineUser().getUserId().intValue());
                rd.setStartDate(record.getStartDate());
                rd.setEndDate(record.getEndDate());
                rd.setTodoUrl(todo_url);
                rd.setPublicFlag("T".equals(record.getPublicFlag()));
                todoList.add(rd);
            }
        } catch (Exception ex) {
            logger.error("Exception", ex);
            return;
        }
    }

    private SelectQuery<EipTTodo> getSelectQueryForTodo(RunData rundata, Context context) {
        Integer uid = (int) targerUser.getUserId().getValue();
        SelectQuery<EipTTodo> query = Database.query(EipTTodo.class);
        Expression exp1 = ExpressionFactory.noMatchExp(EipTTodo.STATE_PROPERTY, Short.valueOf((short) 100));
        query.setQualifier(exp1);
        Expression exp2 = ExpressionFactory.matchExp(EipTTodo.ADDON_SCHEDULE_FLG_PROPERTY, "T");
        query.andQualifier(exp2);
        Expression exp3 = ExpressionFactory.matchDbExp(TurbineUser.USER_ID_PK_COLUMN, uid);
        query.andQualifier(exp3);
        Expression exp11 = ExpressionFactory.greaterOrEqualExp(EipTTodo.END_DATE_PROPERTY, getViewDate().getValue());
        Expression exp12 = ExpressionFactory.lessOrEqualExp(EipTTodo.START_DATE_PROPERTY, getViewDate().getValue());
        Expression exp21 = ExpressionFactory.lessOrEqualExp(EipTTodo.START_DATE_PROPERTY, getViewDate().getValue());
        Expression exp22 = ExpressionFactory.matchExp(EipTTodo.END_DATE_PROPERTY, ToDoUtils.getEmptyDate());
        Expression exp31 = ExpressionFactory.greaterOrEqualExp(EipTTodo.END_DATE_PROPERTY, getViewDate().getValue());
        Expression exp32 = ExpressionFactory.matchExp(EipTTodo.START_DATE_PROPERTY, ToDoUtils.getEmptyDate());
        query.andQualifier((exp11.andExp(exp12)).orExp(exp21.andExp(exp22)).orExp(exp31.andExp(exp32)));
        return query;
    }

    /**
   * 
   * @param record
   * @return
   * @throws ALPageNotFoundException
   * @throws ALDBErrorException
   */
    @Override
    protected Object getResultData(VEipTScheduleList record) throws ALPageNotFoundException, ALDBErrorException {
        CellScheduleResultData rd = new CellScheduleResultData();
        CellScheduleResultData rd2 = new CellScheduleResultData();
        rd.initField();
        rd2.setFormat("yyyy-MM-dd-HH-mm");
        rd2.initField();
        try {
            if ("R".equals(record.getStatus())) {
                return rd;
            }
            if (!ScheduleUtils.isView(getViewDate(), record.getRepeatPattern(), record.getStartDate(), record.getEndDate())) {
                return rd;
            }
            rd.setScheduleId(record.getScheduleId().intValue());
            rd.setParentId(record.getParentId().intValue());
            rd.setName(record.getName());
            rd.setStartDate(record.getStartDate());
            rd.setEndDate(record.getEndDate());
            rd.setTmpreserve("T".equals(record.getStatus()));
            rd.setPublic("O".equals(record.getPublicFlag()));
            rd.setHidden("P".equals(record.getPublicFlag()));
            rd.setPattern(record.getRepeatPattern());
            boolean is_member = record.isMember();
            boolean publicable = record.getPublicFlag().equals("O");
            if (!publicable && !is_member) {
                rd.setName("非公開");
            }
            boolean hidden = record.getPublicFlag().equals("P");
            if (hidden && !is_member) {
                return null;
            }
            if (rd.getPattern().equals("S")) {
                rd.setSpan(true);
                return rd;
            }
            if (!rd.getPattern().equals("N")) {
                if (!ScheduleUtils.isView(getViewDate(), rd.getPattern(), rd.getStartDate().getValue(), rd.getEndDate().getValue())) {
                    return rd;
                }
                rd.setRepeat(true);
            }
        } catch (Exception e) {
            logger.error("Exception", e);
            return null;
        }
        return rd;
    }

    public ALEipUser getTargerUser() {
        return targerUser;
    }

    public void setTargerUser(ALEipUser targerUser) {
        this.targerUser = targerUser;
    }

    @Override
    public List<ScheduleToDoResultData> getToDoResultDataList() {
        return todoList;
    }

    @Override
    public void setPortletId(String id) {
        portletId = id;
    }
}
