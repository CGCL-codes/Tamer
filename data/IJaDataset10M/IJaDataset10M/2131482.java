package com.aimluck.eip.schedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.commons.field.ALDateTimeField;
import com.aimluck.eip.cayenne.om.portlet.VEipTScheduleList;
import com.aimluck.eip.common.ALDBErrorException;
import com.aimluck.eip.common.ALPageNotFoundException;
import com.aimluck.eip.modules.actions.common.ALAction;
import com.aimluck.eip.orm.query.ResultList;
import com.aimluck.eip.schedule.util.ScheduleUtils;
import com.aimluck.eip.util.ALEipUtils;

/**
 *
 */
public class ScheduleListSelectData extends ScheduleMonthlySelectData {

    /** <code>logger</code> logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(ScheduleListSelectData.class.getName());

    /** <code>prevDate</code> 前の日 */
    private ALDateTimeField prevDate;

    /** <code>nextDate</code> 次の日 */
    private ALDateTimeField nextDate;

    /** <code>prevWeek</code> 前の週 */
    private ALDateTimeField prevWeek;

    /** <code>nextWeek</code> 次の週 */
    private ALDateTimeField nextWeek;

    /** <code>viewStart</code> 表示開始日時 */
    private ALDateTimeField viewStart;

    /** <code>viewEnd</code> 表示終了日時 */
    private ALDateTimeField viewEnd;

    protected String viewtype;

    private ScheduleListContainer con;

    private int userid;

    /**
   * 
   * @param action
   * @param rundata
   * @param context
   * @throws ALPageNotFoundException
   * @throws ALDBErrorException
   */
    @Override
    public void init(ALAction action, RunData rundata, Context context) throws ALPageNotFoundException, ALDBErrorException {
        super.init(action, rundata, context);
        viewtype = "list";
        prevDate = new ALDateTimeField("yyyy-MM-dd");
        nextDate = new ALDateTimeField("yyyy-MM-dd");
        prevWeek = new ALDateTimeField("yyyy-MM-dd");
        nextWeek = new ALDateTimeField("yyyy-MM-dd");
        viewStart = new ALDateTimeField("yyyy-MM-dd");
        viewStart.setNotNull(true);
        viewEnd = new ALDateTimeField("yyyy-MM-dd");
        if (ALEipUtils.isMatch(rundata, context)) {
            if (rundata.getParameters().containsKey("view_start")) {
                ALEipUtils.setTemp(rundata, context, "view_start", rundata.getParameters().getString("view_start"));
            }
        }
        String tmpViewStart = ALEipUtils.getTemp(rundata, context, "view_start");
        if (tmpViewStart == null || tmpViewStart.equals("")) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            viewStart.setValue(cal.getTime());
        } else {
            viewStart.setValue(tmpViewStart);
            if (!viewStart.validate(new ArrayList<String>())) {
                ALEipUtils.removeTemp(rundata, context, "view_start");
                throw new ALPageNotFoundException();
            }
        }
        this.setMonthlyCalendarViewMonth(viewStart.getYear(), viewStart.getMonth());
        this.setMonthlyCalendar(rundata, context);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(viewStart.getValue());
        cal2.add(Calendar.DATE, 1);
        nextDate.setValue(cal2.getTime());
        cal2.add(Calendar.DATE, 6);
        nextWeek.setValue(cal2.getTime());
        cal2.add(Calendar.DATE, -8);
        prevDate.setValue(cal2.getTime());
        cal2.add(Calendar.DATE, -6);
        prevWeek.setValue(cal2.getTime());
        cal2.add(Calendar.DATE, 7);
        cal2.add(Calendar.DATE, -1);
        viewEnd.setValue(cal2.getTime());
        ALEipUtils.setTemp(rundata, context, "tmpStart", viewStart.toString() + "-00-00");
        ALEipUtils.setTemp(rundata, context, "tmpEnd", viewStart.toString() + "-00-00");
        Calendar cal4 = Calendar.getInstance();
        cal4.setTime(viewStart.getValue());
        cal4.add(Calendar.DATE, 7);
        viewEnd.setValue(cal4.getTime());
        userid = ALEipUtils.getUserId(rundata);
        con = new ScheduleListContainer();
        con.initField();
        Calendar cal5 = Calendar.getInstance();
        cal5.setTime(viewStart.getValue());
        con.setViewStartDate(cal5);
    }

    @Override
    protected ResultList<VEipTScheduleList> selectList(RunData rundata, Context context) throws ALPageNotFoundException, ALDBErrorException {
        try {
            setupLists(rundata, context);
            List<VEipTScheduleList> resultBaseList = getScheduleList(rundata, context);
            List<VEipTScheduleList> resultList = ScheduleUtils.sortByDummySchedule(resultBaseList);
            return new ResultList<VEipTScheduleList>(resultList);
        } catch (Exception e) {
            logger.error("[ScheduleListSelectData]", e);
            throw new ALDBErrorException();
        }
    }

    protected List<VEipTScheduleList> getScheduleList(RunData rundata, Context context) {
        Integer targetId = null;
        boolean isFacility = false;
        if ((target_user_id != null) && (!target_user_id.equals(""))) {
            if (target_user_id.startsWith(ScheduleUtils.TARGET_FACILITY_ID)) {
                String fid = target_user_id.substring(ScheduleUtils.TARGET_FACILITY_ID.length(), target_user_id.length());
                targetId = Integer.valueOf(fid);
                isFacility = true;
            } else {
                targetId = Integer.valueOf(target_user_id);
            }
        } else {
            return new ArrayList<VEipTScheduleList>();
        }
        return ScheduleUtils.getScheduleList(Integer.valueOf(userid), viewStart.getValue(), viewEnd.getValue(), isFacility ? null : Arrays.asList(targetId), isFacility ? Arrays.asList(targetId) : null);
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
        ScheduleSearchResultData rd = new ScheduleSearchResultData();
        rd.initField();
        try {
            boolean is_member = record.isMember();
            if ((!"D".equals(record.getStatus())) && "P".equals(record.getPublicFlag()) && (userid != record.getUserId().intValue()) && (userid != record.getOwnerId().intValue()) && !is_member) {
                return null;
            }
            if ("C".equals(record.getPublicFlag()) && (userid != record.getUserId().intValue()) && (userid != record.getOwnerId().intValue()) && !is_member) {
                rd.setName("非公開");
                rd.setTmpreserve(false);
            } else {
                rd.setName(record.getName());
                rd.setTmpreserve("T".equals(record.getStatus()));
            }
            rd.setScheduleId(record.getScheduleId().intValue());
            rd.setParentId(record.getParentId().intValue());
            rd.setStartDate(record.getStartDate());
            rd.setEndDate(record.getEndDate());
            rd.setPublic("O".equals(record.getPublicFlag()));
            rd.setHidden("P".equals(record.getPublicFlag()));
            rd.setDummy("D".equals(record.getStatus()));
            rd.setLoginuser(record.getUserId().intValue() == userid);
            rd.setOwner(record.getOwnerId().intValue() == userid);
            rd.setMember(is_member);
            rd.setPattern(record.getRepeatPattern());
            rd.setCreateUser(ALEipUtils.getALEipUser(record.getCreateUserId()));
            if (!rd.getPattern().equals("N")) {
                rd.setRepeat(true);
            }
            con.addResultData(rd);
        } catch (Exception e) {
            logger.error("[ScheduleListSelectData]", e);
            return null;
        }
        return rd;
    }

    /**
   * 表示開始日時を取得します。
   * 
   * @return
   */
    @Override
    public ALDateTimeField getViewStart() {
        return viewStart;
    }

    /**
   * 表示終了日時を取得します。
   * 
   * @return
   */
    @Override
    public ALDateTimeField getViewEnd() {
        return viewEnd;
    }

    /**
   * 表示タイプを取得します。
   * 
   * @return
   */
    @Override
    public String getViewtype() {
        return viewtype;
    }

    /**
   * 前の日を取得します。
   * 
   * @return
   */
    public ALDateTimeField getPrevDate() {
        return prevDate;
    }

    /**
   * 前の週を取得します。
   * 
   * @return
   */
    public ALDateTimeField getPrevWeek() {
        return prevWeek;
    }

    /**
   * 次の日を取得します。
   * 
   * @return
   */
    public ALDateTimeField getNextDate() {
        return nextDate;
    }

    /**
   * 次の週を取得します。
   * 
   * @return
   */
    public ALDateTimeField getNextWeek() {
        return nextWeek;
    }

    public List<ScheduleResultData> getScheduleList() {
        return con.getScheduleList();
    }
}
