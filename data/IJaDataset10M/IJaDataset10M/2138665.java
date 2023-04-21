package org.fest.swing.driver;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.fest.swing.annotation.RunsInEDT;
import org.fest.swing.core.Robot;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.exception.ComponentLookupException;
import static javax.swing.JOptionPane.*;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.core.BasicRobot.robotWithNewAwtHierarchy;
import static org.fest.swing.driver.AbstractButtonTextQuery.textOf;
import static org.fest.swing.edt.GuiActionRunner.execute;
import static org.fest.swing.test.core.CommonAssertions.failWhenExpectingException;
import static org.fest.swing.test.core.TestGroups.GUI;
import static org.fest.swing.test.swing.JOptionPaneLauncher.launch;
import static org.fest.util.Arrays.array;
import static org.fest.util.Strings.concat;

/**
 * Tests for <code>{@link JOptionPaneDriver}</code>.
 * 
 * @author Alex Ruiz
 */
@Test(groups = GUI)
public class JOptionPaneDriverTest {

    private Robot robot;

    private JOptionPaneDriver driver;

    private static final Icon ICON = null;

    private static final String MESSAGE = "Message";

    private static final String TITLE = JOptionPaneDriverTest.class.getSimpleName();

    @BeforeClass
    public void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeMethod
    public void setUp() {
        robot = robotWithNewAwtHierarchy();
        driver = new JOptionPaneDriver(robot);
    }

    @AfterMethod
    public void tearDown() {
        robot.cleanUp();
    }

    public void shouldFindButtonWithGivenTextInOptionPane() {
        JOptionPane optionPane = messageWithOptions("First", "Second");
        launch(optionPane, TITLE);
        JButton button = driver.buttonWithText(optionPane, "Second");
        assertThat(textOf(button)).isEqualTo("Second");
    }

    public void shouldFindOKButton() {
        JOptionPane optionPane = informationMessage();
        launch(optionPane, TITLE);
        JButton button = driver.okButton(optionPane);
        assertThatButtonHasTextFromUIManager(button, "OptionPane.okButtonText");
    }

    public void shouldFindCancelButton() {
        JOptionPane optionPane = inputMessage();
        launch(optionPane, TITLE);
        JButton button = driver.cancelButton(optionPane);
        assertThatButtonHasTextFromUIManager(button, "OptionPane.cancelButtonText");
    }

    public void shouldFindYesButton() {
        JOptionPane optionPane = confirmMessage();
        launch(optionPane, TITLE);
        JButton button = driver.yesButton(optionPane);
        assertThatButtonHasTextFromUIManager(button, "OptionPane.yesButtonText");
    }

    public void shouldFindNoButton() {
        JOptionPane optionPane = confirmMessage();
        launch(optionPane, TITLE);
        JButton button = driver.noButton(optionPane);
        assertThatButtonHasTextFromUIManager(button, "OptionPane.noButtonText");
    }

    @RunsInEDT
    private static JOptionPane confirmMessage() {
        return execute(new GuiQuery<JOptionPane>() {

            protected JOptionPane executeInEDT() {
                return new JOptionPane(MESSAGE, QUESTION_MESSAGE, YES_NO_CANCEL_OPTION);
            }
        });
    }

    private void assertThatButtonHasTextFromUIManager(JButton button, String textKey) {
        String expected = UIManager.getString(textKey);
        assertThat(textOf(button)).isEqualTo(expected);
    }

    public void shouldFindTextComponentInOptionPane() {
        JOptionPane optionPane = inputMessage();
        launch(optionPane, TITLE);
        JTextComponent textBox = driver.textBox(optionPane);
        assertThat(textBox).isNotNull();
    }

    @RunsInEDT
    private static JOptionPane inputMessage() {
        return execute(new GuiQuery<JOptionPane>() {

            protected JOptionPane executeInEDT() {
                JOptionPane optionPane = new JOptionPane(MESSAGE, QUESTION_MESSAGE, OK_CANCEL_OPTION);
                optionPane.setWantsInput(true);
                return optionPane;
            }
        });
    }

    @Test(groups = GUI, expectedExceptions = ComponentLookupException.class)
    public void shouldNotFindTextComponentInOptionPaneIfNotInputMessage() {
        JOptionPane optionPane = errorMessage();
        launch(optionPane, TITLE);
        driver.textBox(optionPane);
    }

    public void shouldPassIfMatchingTitle() {
        JOptionPane optionPane = informationMessage();
        launch(optionPane, TITLE);
        driver.requireTitle(optionPane, TITLE);
    }

    public void shouldFailIfNotMatchingTitle() {
        JOptionPane optionPane = informationMessage();
        launch(optionPane, TITLE);
        try {
            driver.requireTitle(optionPane, "Yoda");
            failWhenExpectingException();
        } catch (AssertionError e) {
            assertThat(e.getMessage()).contains("property:'title'").contains(concat("expected:<'Yoda'> but was:<'", TITLE, "'>"));
        }
    }

    public void shouldPassIfMatchingOptions() {
        JOptionPane optionPane = messageWithOptions("First", "Second");
        launch(optionPane, TITLE);
        driver.requireOptions(optionPane, array("First", "Second"));
    }

    public void shouldFailIfNotMatchingOptions() {
        JOptionPane optionPane = messageWithOptions("First", "Second");
        launch(optionPane, TITLE);
        try {
            driver.requireOptions(optionPane, array("Third"));
            failWhenExpectingException();
        } catch (AssertionError e) {
            assertThat(e.getMessage()).contains("property:'options'").contains("expected:<['Third']> but was:<['First', 'Second']>");
        }
    }

    @RunsInEDT
    private static JOptionPane messageWithOptions(final Object... options) {
        return execute(new GuiQuery<JOptionPane>() {

            protected JOptionPane executeInEDT() {
                Object initialValue = options[0];
                JOptionPane optionPane = new JOptionPane(MESSAGE, QUESTION_MESSAGE, YES_NO_OPTION, ICON, options, initialValue);
                optionPane.setInitialValue(initialValue);
                return optionPane;
            }
        });
    }

    public void shouldPassIfMatchingMessage() {
        JOptionPane optionPane = messageWithText("Leia");
        launch(optionPane, TITLE);
        driver.requireMessage(optionPane, "Leia");
    }

    public void shouldFailIfNotMatchingMessage() {
        JOptionPane optionPane = messageWithText("Palpatine");
        launch(optionPane, TITLE);
        try {
            driver.requireMessage(optionPane, "Anakin");
            failWhenExpectingException();
        } catch (AssertionError e) {
            assertThat(e.getMessage()).contains("property:'message'").contains("expected:<'Anakin'> but was:<'Palpatine'>");
        }
    }

    @RunsInEDT
    private static JOptionPane messageWithText(final String text) {
        return execute(new GuiQuery<JOptionPane>() {

            protected JOptionPane executeInEDT() {
                return new JOptionPane(text);
            }
        });
    }

    public void shouldPassIfExpectedAndActualMessageTypeIsError() {
        JOptionPane optionPane = errorMessage();
        launch(optionPane, TITLE);
        driver.requireErrorMessage(optionPane);
    }

    public void shouldFailIfExpectedMessageTypeIsErrorAndActualIsNot() {
        JOptionPane optionPane = informationMessage();
        launch(optionPane, TITLE);
        try {
            driver.requireErrorMessage(optionPane);
            failWhenExpectingException();
        } catch (AssertionError e) {
            assertThat(e.getMessage()).contains("property:'messageType'").contains("expected:<'Error Message'> but was:<'Information Message'>");
        }
    }

    public void shouldPassIfExpectedAndActualMessageTypeIsInformation() {
        JOptionPane optionPane = informationMessage();
        launch(optionPane, TITLE);
        driver.requireInformationMessage(optionPane);
    }

    @RunsInEDT
    private static JOptionPane informationMessage() {
        return messageOfType(INFORMATION_MESSAGE);
    }

    public void shouldFailIfExpectedMessageTypeIsInformationAndActualIsNot() {
        JOptionPane optionPane = errorMessage();
        launch(optionPane, TITLE);
        try {
            driver.requireInformationMessage(optionPane);
            failWhenExpectingException();
        } catch (AssertionError e) {
            assertThat(e.getMessage()).contains("property:'messageType'").contains("expected:<'Information Message'> but was:<'Error Message'>");
        }
    }

    public void shouldPassIfExpectedAndActualMessageTypeIsWarning() {
        JOptionPane optionPane = warningMessage();
        launch(optionPane, TITLE);
        driver.requireWarningMessage(optionPane);
    }

    @RunsInEDT
    private static JOptionPane warningMessage() {
        return messageOfType(WARNING_MESSAGE);
    }

    public void shouldFailIfExpectedMessageTypeIsWarningAndActualIsNot() {
        JOptionPane optionPane = errorMessage();
        launch(optionPane, TITLE);
        try {
            driver.requireWarningMessage(optionPane);
            failWhenExpectingException();
        } catch (AssertionError e) {
            assertThat(e.getMessage()).contains("property:'messageType'").contains("expected:<'Warning Message'> but was:<'Error Message'>");
        }
    }

    public void shouldPassIfExpectedAndActualMessageTypeIsQuestion() {
        JOptionPane optionPane = questionMessage();
        launch(optionPane, TITLE);
        driver.requireQuestionMessage(optionPane);
    }

    @RunsInEDT
    private static JOptionPane questionMessage() {
        return messageOfType(QUESTION_MESSAGE);
    }

    public void shouldFailIfExpectedMessageTypeIsQuestionAndActualIsNot() {
        JOptionPane optionPane = errorMessage();
        launch(optionPane, TITLE);
        try {
            driver.requireQuestionMessage(optionPane);
            failWhenExpectingException();
        } catch (AssertionError e) {
            assertThat(e.getMessage()).contains("property:'messageType'").contains("expected:<'Question Message'> but was:<'Error Message'>");
        }
    }

    public void shouldPassIfExpectedAndActualMessageTypeIsPlain() {
        JOptionPane optionPane = plainMessage();
        launch(optionPane, TITLE);
        driver.requirePlainMessage(optionPane);
    }

    @RunsInEDT
    private static JOptionPane plainMessage() {
        return messageOfType(PLAIN_MESSAGE);
    }

    public void shouldFailIfExpectedMessageTypeIsPlainAndActualIsNot() {
        JOptionPane optionPane = errorMessage();
        launch(optionPane, TITLE);
        try {
            driver.requirePlainMessage(optionPane);
            failWhenExpectingException();
        } catch (AssertionError e) {
            assertThat(e.getMessage()).contains("property:'messageType'").contains("expected:<'Plain Message'> but was:<'Error Message'>");
        }
    }

    @RunsInEDT
    private static JOptionPane errorMessage() {
        return messageOfType(ERROR_MESSAGE);
    }

    @RunsInEDT
    private static JOptionPane messageOfType(final int type) {
        return execute(new GuiQuery<JOptionPane>() {

            protected JOptionPane executeInEDT() {
                return new JOptionPane(MESSAGE, type);
            }
        });
    }
}
