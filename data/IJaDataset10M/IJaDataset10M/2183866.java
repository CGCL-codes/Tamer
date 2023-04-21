package org.fest.swing.driver;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import org.fest.swing.annotation.RunsInEDT;
import org.fest.swing.core.Robot;
import org.fest.swing.core.matcher.JButtonMatcher;
import org.fest.swing.exception.ComponentLookupException;
import static javax.swing.JOptionPane.*;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.driver.JOptionPaneMessageQuery.messageOf;
import static org.fest.swing.driver.JOptionPaneMessageTypeQuery.messageTypeOf;
import static org.fest.swing.driver.JOptionPaneMessageTypes.messageTypeAsText;
import static org.fest.swing.driver.JOptionPaneOptionsQuery.optionsOf;
import static org.fest.swing.driver.JOptionPaneTitleQuery.titleOf;

/**
 * Understands simulation of user input on a <code>{@link JOptionPane}</code>. Unlike <code>JOptionPaneFixture</code>,
 * this driver only focuses on behavior present only in <code>{@link JOptionPane}</code>s. This class is intended for
 * internal use only.
 * 
 * @author Alex Ruiz
 */
public class JOptionPaneDriver extends JComponentDriver {

    private static final String MESSAGE_PROPERTY = "message";

    private static final String MESSAGE_TYPE_PROPERTY = "messageType";

    private static final String OPTIONS_PROPERTY = "options";

    private static final String TITLE_PROPERTY = "title";

    /**
   * Creates a new </code>{@link JOptionPaneDriver}</code>.
   * @param robot the robot to use to simulate user input.
   */
    public JOptionPaneDriver(Robot robot) {
        super(robot);
    }

    /**
   * Asserts that the <code>{@link JOptionPane}</code> has the given title.
   * @param optionPane the target <code>JOptionPane</code>.
   * @param title the title to match.
   * @throws AssertionError if the <code>JOptionPane</code> does not have the given title.
   */
    @RunsInEDT
    public void requireTitle(JOptionPane optionPane, String title) {
        assertThat(titleOf(optionPane)).as(propertyName(optionPane, TITLE_PROPERTY)).isEqualTo(title);
    }

    /**
   * Asserts that the <code>{@link JOptionPane}</code> shows the given message.
   * @param optionPane the target <code>JOptionPane</code>.
   * @param message the message to verify.
   * @throws AssertionError if the <code>JOptionPane</code> does not show the given message.
   */
    @RunsInEDT
    public void requireMessage(JOptionPane optionPane, Object message) {
        assertThat(messageOf(optionPane)).as(propertyName(optionPane, MESSAGE_PROPERTY)).isEqualTo(message);
    }

    /**
   * Asserts that the <code>{@link JOptionPane}</code> has the given options.
   * @param optionPane the target <code>JOptionPane</code>.
   * @param options the options to verify.
   * @throws AssertionError if the <code>JOptionPane</code> does not have the given options.
   */
    @RunsInEDT
    public void requireOptions(JOptionPane optionPane, Object[] options) {
        assertThat(optionsOf(optionPane)).as(propertyName(optionPane, OPTIONS_PROPERTY)).isEqualTo(options);
    }

    /**
   * Finds the "OK" button in the <code>{@link JOptionPane}</code>. This method is independent of locale and platform.
   * @param optionPane the target <code>JOptionPane</code>.
   * @return the "OK" button.
   * @throws ComponentLookupException if the a "OK" button cannot be found.
   */
    @RunsInEDT
    public JButton okButton(JOptionPane optionPane) {
        return buttonWithTextFromUIManager(optionPane, "OptionPane.okButtonText");
    }

    /**
   * Finds the "Cancel" button in the <code>{@link JOptionPane}</code>. This method is independent of locale and 
   * platform.
   * @param optionPane the target <code>JOptionPane</code>.
   * @return the "Cancel" button.
   * @throws ComponentLookupException if the a "Cancel" button cannot be found.
   */
    @RunsInEDT
    public JButton cancelButton(JOptionPane optionPane) {
        return buttonWithTextFromUIManager(optionPane, "OptionPane.cancelButtonText");
    }

    /**
   * Finds the "Yes" button in the <code>{@link JOptionPane}</code>. This method is independent of locale and platform.
   * @param optionPane the target <code>JOptionPane</code>.
   * @return the "Yes" button.
   * @throws ComponentLookupException if the a "Yes" button cannot be found.
   */
    @RunsInEDT
    public JButton yesButton(JOptionPane optionPane) {
        return buttonWithTextFromUIManager(optionPane, "OptionPane.yesButtonText");
    }

    /**
   * Finds the "No" button in the <code>{@link JOptionPane}</code>.  This method is independent of locale and platform.
   * @param optionPane the target <code>JOptionPane</code>.
   * @return the "No" button.
   * @throws ComponentLookupException if the a "No" button cannot be found.
   */
    @RunsInEDT
    public JButton noButton(JOptionPane optionPane) {
        return buttonWithTextFromUIManager(optionPane, "OptionPane.noButtonText");
    }

    @RunsInEDT
    private JButton buttonWithTextFromUIManager(JOptionPane optionPane, String key) {
        return buttonWithText(optionPane, UIManager.getString(key));
    }

    /**
   * Finds a button in the <code>{@link JOptionPane}</code> containing the given text.
   * @param optionPane the target <code>JOptionPane</code>.
   * @param text the text of the button to find and return.
   * @return a button containing the given text.
   * @throws ComponentLookupException if the a button with the given text cannot be found.
   */
    @RunsInEDT
    public JButton buttonWithText(JOptionPane optionPane, String text) {
        return robot.finder().find(optionPane, JButtonMatcher.withText(text).andShowing());
    }

    /**
   * Finds a <code>{@link JButton}</code> in the <code>{@link JOptionPane}</code> (assuming it has only one button.)
   * @param optionPane the target <code>JOptionPane</code>.
   * @return the only <code>JButton</code> contained in the <code>JOptionPane</code>.
   * @throws ComponentLookupException if a matching component could not be found.
   * @throws ComponentLookupException if more than one matching component is found.
   */
    @RunsInEDT
    public JButton button(JOptionPane optionPane) {
        return robot.finder().findByType(optionPane, JButton.class);
    }

    /**
   * Returns the <code>{@link JTextComponent}</code> in the given message only if the message is of type input.
   * @param optionPane the target <code>JOptionPane</code>.
   * @return the text component in the given message.
   * @throws ComponentLookupException if the message type is not input and therefore it does not contain a text component.
   */
    @RunsInEDT
    public JTextComponent textBox(JOptionPane optionPane) {
        return robot.finder().findByType(optionPane, JTextComponent.class);
    }

    /**
   * Asserts that the <code>{@link JOptionPane}</code> is displaying an error message.
   * @param optionPane the target <code>JOptionPane</code>.
   */
    @RunsInEDT
    public void requireErrorMessage(JOptionPane optionPane) {
        assertEqualMessageType(optionPane, ERROR_MESSAGE);
    }

    /**
   * Asserts that the <code>{@link JOptionPane}</code> is displaying an information message.
   * @param optionPane the target <code>JOptionPane</code>.
   */
    @RunsInEDT
    public void requireInformationMessage(JOptionPane optionPane) {
        assertEqualMessageType(optionPane, INFORMATION_MESSAGE);
    }

    /**
   * Asserts that the <code>{@link JOptionPane}</code> is displaying a warning message.
   * @param optionPane the target <code>JOptionPane</code>.
   */
    @RunsInEDT
    public void requireWarningMessage(JOptionPane optionPane) {
        assertEqualMessageType(optionPane, WARNING_MESSAGE);
    }

    /**
   * Asserts that the <code>{@link JOptionPane}</code> is displaying a question.
   * @param optionPane the target <code>JOptionPane</code>.
   */
    @RunsInEDT
    public void requireQuestionMessage(JOptionPane optionPane) {
        assertEqualMessageType(optionPane, QUESTION_MESSAGE);
    }

    /**
   * Asserts that the <code>{@link JOptionPane}</code> is displaying a plain message.
   * @param optionPane the target <code>JOptionPane</code>.
   */
    @RunsInEDT
    public void requirePlainMessage(JOptionPane optionPane) {
        assertEqualMessageType(optionPane, PLAIN_MESSAGE);
    }

    @RunsInEDT
    private void assertEqualMessageType(JOptionPane optionPane, int expected) {
        String actualType = actualMessageTypeAsText(optionPane);
        assertThat(actualType).as(propertyName(optionPane, MESSAGE_TYPE_PROPERTY)).isEqualTo(messageTypeAsText(expected));
    }

    @RunsInEDT
    private String actualMessageTypeAsText(final JOptionPane optionPane) {
        return messageTypeAsText(messageTypeOf(optionPane));
    }
}
