package org.ikasan.common.component;

import java.io.Serializable;

/**
 * ScheduleInfo class.
 * @deprecated - This has been deprecated TODO replace with?
 * 
 * @author Jeff Mitchell
 */
@Deprecated
public class ScheduleInfo implements Serializable {

    /** Serialize ID */
    private static final long serialVersionUID = 1L;

    /** scheduled by a rollback/retry operation */
    public static final ScheduleInfo UNDEFINED_TASK = new ScheduleInfo(new Integer(0), "Undefined schedule info.");

    /** scheduled by a rollback/retry operation */
    public static final ScheduleInfo ROLLBACK_RETRY_TASK = new ScheduleInfo(new Integer(1), "Scheduled within a rollback/retry.");

    /** scheduled by normal onTimeout operation */
    public static final ScheduleInfo ONTIMEOUT_TASK = new ScheduleInfo(new Integer(2), "Scheduled within an onTimeout.");

    /** schedule cause code */
    private Integer id;

    /** schedule description */
    private String description;

    /** count */
    private int count;

    /**
     * Default constructor
     * Creates a new instance of <code>ScheduleInfo</code>.
     * 
     * @param id 
     * @param description 
     */
    public ScheduleInfo(final Integer id, final String description) {
        this.id = id;
        this.description = description;
    }
}
