package edu.univalle.lingweb.agent.model;

/**
 * Contiene las constantes para la identificaci�n de las
 * distintas conversaciones de los agentes y de los tipo de anuncios generados en sistema.
 * @author Julio
 */
public class Constant {

    public static final String CHAT_MANAGER_NAME = "manager";

    public static final String CHAT_ID = "__chat__";

    public static final String STATUS_ID = "__status__";

    public static final String CHANGE_STATUS_ID = "__changeStatus__";

    public static final String NOTIFY_STATUS_ID = "__notifyStatus__";

    public static final String MESSAGE_ID = "__message__";

    public static final String NOTIFY_MESSAGE_ID = "__notify__";

    public static final String NOTIFY_PARTICIPANTS_ID = "__participants__";

    public static final String ANNOUNCEMENT_ID = "__announcement__";

    public static final Long SCORED_EXERCISE = new Long(1);

    public static final Long SCORED_UPDATE_EXERCISE = new Long(2);

    public static final Long IN_PROCESS = new Long(3);

    public static final Long PENDING = new Long(4);

    public static final Long ALERT_SCORED_EXERCISES = new Long(5);

    public static final Long ALERT_PENDED_EXERCISES = new Long(6);

    public static final Long STRENGTHENING_EXERCISE = new Long(7);

    public static final Long EXERCISE_OF_DEEPENING = new Long(8);

    public static final Long ALL_ANNOUNCEMENT = new Long(9);

    public static final Long NEW_AND_OLD_USER = new Long(10);
}
