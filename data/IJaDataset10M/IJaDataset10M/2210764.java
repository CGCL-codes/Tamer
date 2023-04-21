package cn.myapps.core.workflow.notification.ejb;

public interface NotificationConstant {

    /**
	 * 到达通知
	 */
    public static final String STRTAGERY_ARRIVE = "arrive";

    /**
	 * 过期的通知
	 */
    public static final String STRTAGERY_OVERDUE = "overdue";

    /**
	 * 退回通知
	 */
    public static final String STRTAGERY_REJECT = "reject";

    /**
	 * 发送通知
	 * 
	 */
    public static final String STRTAGERY_SEND = "send";

    /**
	 * 邮件发送方式
	 */
    public static final int SEND_MODE_EMAIL = 0;

    /**
	 * 手机短信发送方式
	 */
    public static final int SEND_MODE_SMS = 1;

    /**
	 * 站内短信发送方式
	 */
    public static final int SEND_MODE_PERSONALMESSAGE = 2;

    /**
	 * 已时间计时
	 */
    public static final int TIME_UNIT_HOUR = 0;

    /**
	 * 已天数计时
	 */
    public static final int TIME_UNIT_DAY = 1;

    /**
	 * 提交人
	 */
    public static final int RESPONSIBLE_TYPE_SUBMITTER = 0x00000001;

    /**
	 * 作者
	 */
    public static final int RESPONSIBLE_TYPE_AUTHOR = 0x00000010;

    /**
	 * 所有当前审批人
	 */
    public static final int RESPONSIBLE_TYPE_ALL = 0x00000100;

    /**
	 * 30分钟
	 */
    public static final int JOB_PEIROD = 30 * 60 * 1000;
}
