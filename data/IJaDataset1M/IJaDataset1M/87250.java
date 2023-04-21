package slim3.controller.attend.inputAttendance;

import java.util.ArrayList;
import java.util.List;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.util.DateUtil;
import slim3.constants.Constants;
import slim3.model.Attendance;
import slim3.model.Member;
import slim3.model.Practice;
import slim3.service.AttendanceService;
import slim3.service.PracticeService;
import slim3.util.AttendDateUtil;

public class IndexController extends Controller {

    private final String DATE_PATTERN = "yyyyMM";

    private AttendanceService attendanceSvc = new AttendanceService();

    private PracticeService practiceSvc = new PracticeService();

    @Override
    public Navigation run() throws Exception {
        if (sessionScope(Constants.SESSION_KEY_LOGIN_USER) == null) {
            requestScope(Constants.ATTRKEY_FROM_PATH, request.getRequestURL());
            return forward(Constants.PATH_LOGIN_ERROR);
        }
        String currentDateStr = getBaseYearAndMonthStr();
        if (currentDateStr == null || currentDateStr.isEmpty() || currentDateStr.equals("null")) {
            return forward("index.jsp");
        }
        int year = Integer.valueOf(currentDateStr.substring(0, 4));
        int month = Integer.valueOf(currentDateStr.substring(4, 6));
        request.setAttribute("currentYear", year);
        request.setAttribute("currentMonth", month);
        request.setAttribute("nextDate", getNextYearAndMonthStr(currentDateStr));
        request.setAttribute("currentDate", currentDateStr);
        request.setAttribute("beforeDate", getBeforeYearAndMonthStr(currentDateStr));
        request.setAttribute("attendanceList", ConstructAttendanceList(year, month));
        return forward("index.jsp");
    }

    /**
     * requestParameters �����̔N�����擾���܂�
     * requestParameters �ɑ��݂��Ȃ��ꍇ�̓V�X�e����t����擾���܂��B�iJST�j
     *
     * @return
     */
    private String getBaseYearAndMonthStr() {
        String yearAndDateStr = asString("date");
        if (yearAndDateStr == null) {
            yearAndDateStr = DateUtil.toString(AttendDateUtil.getCurrentDate(), DATE_PATTERN);
        }
        return yearAndDateStr;
    }

    /**
     * ��̔N�����痂���̔N�����擾���܂��B
     *
     * @param baseDateStr
     * @return
     */
    private String getNextYearAndMonthStr(String baseDateStr) {
        if (baseDateStr == null) {
            return null;
        }
        return DateUtil.toString(AttendDateUtil.getNextMonth(DateUtil.toDate(baseDateStr, DATE_PATTERN)), DATE_PATTERN);
    }

    /**
     * ��̔N������挎�̔N�����擾���܂��B
     *
     * @param baseDateStr
     * @return
     */
    private String getBeforeYearAndMonthStr(String baseDateStr) {
        if (baseDateStr == null) {
            return null;
        }
        return DateUtil.toString(AttendDateUtil.getBeforeMonth(DateUtil.toDate(baseDateStr, DATE_PATTERN)), DATE_PATTERN);
    }

    /**
     * �w�肳�ꂽ�N���ɂ��āA���O�C�����[�U�[�̏o�����X�g���쐬���܂��B
     * 
     * @param year
     * @param month
     * @return
     */
    private List<Attendance> ConstructAttendanceList(int year, int month) {
        Member loginUser = sessionScope(Constants.SESSION_KEY_LOGIN_USER);
        if (loginUser == null) {
        }
        List<Attendance> attendanceList = null;
        List<Practice> practiceList = practiceSvc.searchFromYearAndMonth(year, month);
        if (practiceList != null && practiceList.size() > 0) {
            attendanceList = new ArrayList<Attendance>();
            for (Practice practice : practiceList) {
                Attendance attendance = attendanceSvc.searchFromMemberIdAndPracticeDate(loginUser.getId(), practice.getStartDate());
                if (attendance == null) {
                    attendance = attendanceSvc.getInitalizedAttendance(loginUser, practice);
                }
                attendanceList.add(attendance);
            }
        }
        return attendanceList;
    }
}
