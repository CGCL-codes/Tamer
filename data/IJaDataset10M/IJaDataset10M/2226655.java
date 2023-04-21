package de.xirp.io.logging;

import java.util.HashMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.RGB;
import de.xirp.ui.util.SWTUtil;
import de.xirp.ui.util.ressource.ColorManager;

/**
 * Log4j appender for text fields (StyledText).<br>
 * Messages are logged in special colors for each level.
 * 
 * @author Rabea Gransberger
 */
public class XirpTextFieldAppender extends AppenderSkeleton {

    /**
	 * The texts for the different robots
	 */
    private static HashMap<String, StyledText> texts = new HashMap<String, StyledText>();

    /**
	 * The Text to write the log to
	 */
    private static StyledText text;

    /**
	 * Color for Logging Type ERROR
	 */
    private static final RGB ERROR = new RGB(255, 0, 0);

    /**
	 * Color for Logging Type WARN
	 */
    private static final RGB WARN = new RGB(255, 127, 0);

    /**
	 * Color for Logging Type FATAL
	 */
    private static final RGB FATAL = new RGB(175, 0, 0);

    /**
	 * Color for Logging Type DEBUG
	 */
    private static final RGB DEBUG = new RGB(0, 0, 255);

    /**
	 * Color for Logging Type INFO
	 */
    private static final RGB INFO = new RGB(0, 0, 0);

    /**
	 * Sets the text field to which this appender should append the
	 * log events for a specific robot.
	 * 
	 * @param robotName
	 *            the name of the robot or <code>null</code> if this
	 *            should be a log text field for all robots
	 * @param textField
	 *            TextField to append the log text to
	 */
    public static void addTextField(String robotName, StyledText textField) {
        if (robotName == null) {
            text = textField;
        } else {
            texts.put(robotName, textField);
        }
    }

    /**
	 * Removes the text field for the given robot name.
	 * 
	 * @param robotName
	 *            the name of the robot or <code>null</code> if the
	 *            text field for all robots should be removed
	 */
    public static void removeTextField(String robotName) {
        if (robotName == null) {
            text = null;
        } else {
            texts.remove(robotName);
        }
    }

    /**
	 * Extracts the robot name and the original message from the given
	 * message.
	 * 
	 * @param message
	 *            the original message to extract the robot name and
	 *            original message from
	 * @return a string array with the robot name and original message
	 */
    private String[] extractRobot(String message) {
        String robot = StringUtils.substringBetween(message, RobotLogger.ROBOT_IDENTIFIER, RobotLogger.ROBOT_DELIMITER);
        if (StringUtils.isEmpty(robot)) {
            return null;
        }
        String origMessage = StringUtils.substringBefore(message, RobotLogger.ROBOT_IDENTIFIER);
        origMessage += StringUtils.substringAfterLast(message, RobotLogger.ROBOT_DELIMITER);
        return new String[] { robot, origMessage };
    }

    /**
	 * Writes the log message of the event to the styled Text (of a
	 * robot if a robot is available) and shows it in different colors
	 * for the different log types. For example error messages are
	 * shown in Red. If the current Thread is not the UI Thread, a
	 * asyncExec is called to display the message.
	 * 
	 * @param logEvent
	 *            The event holding the type and message
	 * @see org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent)
	 */
    @Override
    protected void append(final LoggingEvent logEvent) {
        String robotName = null;
        String renderedMessage = logEvent.getRenderedMessage();
        String[] mess = extractRobot(renderedMessage);
        if (mess != null) {
            robotName = mess[0];
            renderedMessage = robotName + ": " + mess[1];
        }
        Level level = logEvent.getLevel();
        final StyleRange range = new StyleRange();
        range.length = renderedMessage.length();
        if (level == Level.DEBUG) {
            range.foreground = ColorManager.getColor(DEBUG);
        } else if (level == Level.WARN) {
            range.foreground = ColorManager.getColor(WARN);
        } else if (level == Level.ERROR) {
            range.foreground = ColorManager.getColor(ERROR);
        } else if (level == Level.FATAL) {
            range.foreground = ColorManager.getColor(FATAL);
        } else {
            range.foreground = ColorManager.getColor(INFO);
        }
        if (robotName != null) {
            StyledText robotText = texts.get(robotName);
            log(robotText, range, renderedMessage);
        } else {
            for (StyledText text : texts.values()) {
                log(text, range, renderedMessage);
            }
            log(text, range, renderedMessage);
        }
    }

    /**
	 * Writes the log message with the given style to the given text
	 * field using an asyncExec.
	 * 
	 * @param textField
	 *            the text field the log message to
	 * @param range
	 *            the range used for formatting
	 * @param rendered
	 *            the rendered text
	 */
    private void log(final StyledText textField, final StyleRange range, final String rendered) {
        if (SWTUtil.swtAssert(textField)) {
            SWTUtil.asyncExec(new Runnable() {

                public void run() {
                    if (SWTUtil.swtAssert(textField)) {
                        int start = textField.getText().length();
                        range.start = start;
                        textField.append(rendered);
                        textField.setStyleRange(range);
                        textField.setCaretOffset(textField.getText().length());
                        textField.showSelection();
                    }
                }
            });
        }
    }

    /**
	 * @see org.apache.log4j.AppenderSkeleton#requiresLayout()
	 */
    @Override
    public boolean requiresLayout() {
        return false;
    }

    /**
	 * @see org.apache.log4j.AppenderSkeleton#close()
	 */
    @Override
    public void close() {
    }
}
