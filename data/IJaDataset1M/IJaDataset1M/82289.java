package org.bd.banglasms.ui;

/**
 * This view shows a message box to user, typically containing an information note, or information query.
 * <h2> Events </h2>
 * <UL> It must support the following events.
 * <LI> {@link #EVENT_MESSAGE_CANCEL}
 * <LI> {@link #EVENT_MESSAGE_OK}
 * <LI> {@link #EVENT_MESSAGE_DISSMISS}
 * </UL>
 */
public interface MessageBox extends View {

    /**
	 * Option style when user is shown no option to select or a platform
	 * specific dismiss option. Message will disappear after a timeout
	 * without user interaction. When the message will be dismissed
	 * {@link #EVENT_MESSAGE_DISSMISS} will be notified.
	 */
    public static final int NO_OPTION = 0;

    /**
	 *Option style when user is shown an information with only OK option.
	 * Message will be shown on screen until user selects OK, upon which
	 * {@link #EVENT_MESSAGE_DISSMISS} will be notified.
	 */
    public static final int OPTION_OK = 1;

    /**
	 * Option style when user is asked a query with an OK and Cancel option.
	 * Depending on user interaction either {@link #EVENT_MESSAGE_OK} or
	 * {@link #EVENT_MESSAGE_CANCEL} will be notified.
	 */
    public static final int OPTION_OK_CANCEL = 2;

    public static final String EVENT_MESSAGE_DISSMISS = "event_message_dismissed";

    public static final String EVENT_MESSAGE_OK = "event_message_ok";

    public static final String EVENT_MESSAGE_CANCEL = "event_message_cancel";

    public void setText(String text);

    public void setOptionStyle(int optionStyle);
}
