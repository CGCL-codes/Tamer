package evolaris.platform.smssvc.web.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author richard.hable
 * @date 2006-12-19
 *
 */
@SuppressWarnings("serial")
public class TimerEventListForm extends ActionForm {

    private Boolean allGroups;

    private Long userId;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if (request.getParameter("method") == null && request.getParameter("allGroups") == null) {
            allGroups = null;
        }
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getAllGroups() {
        return allGroups;
    }

    public void setAllGroups(Boolean allGroups) {
        this.allGroups = allGroups;
    }

    /**
	 * Class required for list display
	 */
    public static class DisplayListEntry {

        private long timerEventId;

        private String pointInTime;

        private String groupname;

        private String username;

        private String description;

        private String repeat;

        private String status;

        private String applyTo;

        /**
		 * @return the description
		 */
        public String getDescription() {
            return description;
        }

        /**
		 * @param description the description to set
		 */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
		 * @return the repeat
		 */
        public String getRepeat() {
            return repeat;
        }

        /**
		 * @param repeat the repeat to set
		 */
        public void setRepeat(String parameter) {
            this.repeat = parameter;
        }

        /**
		 * @return the pointInTime
		 */
        public String getPointInTime() {
            return pointInTime;
        }

        /**
		 * @param pointInTime the pointInTime to set
		 */
        public void setPointInTime(String pointInTime) {
            this.pointInTime = pointInTime;
        }

        /**
		 * @return the timerEventId
		 */
        public long getTimerEventId() {
            return timerEventId;
        }

        /**
		 * @param timerEventId the timerEventId to set
		 */
        public void setTimerEventId(long timerEventId) {
            this.timerEventId = timerEventId;
        }

        /**
		 * @return the username
		 */
        public String getUsername() {
            return username;
        }

        /**
		 * @param username the username to set
		 */
        public void setUsername(String username) {
            this.username = username;
        }

        /**
		 * @return the status
		 */
        public String getStatus() {
            return status;
        }

        /**
		 * @param status the status to set
		 */
        public void setStatus(String status) {
            this.status = status;
        }

        /**
		 * @return the applyTo
		 */
        public String getApplyTo() {
            return applyTo;
        }

        /**
		 * @param applyTo the applyTo to set
		 */
        public void setApplyTo(String applyTo) {
            this.applyTo = applyTo;
        }

        /**
		 * @return the groupname
		 */
        public String getGroupname() {
            return groupname;
        }

        /**
		 * @param groupname the groupname to set
		 */
        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }
    }
}
