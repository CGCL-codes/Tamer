package org.fest.swing.fixture;

import javax.swing.JTable;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.fest.swing.annotation.RunsInEDT;
import org.fest.swing.core.Robot;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.test.swing.TableRenderDemo;
import org.fest.swing.test.swing.TestWindow;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.core.BasicRobot.robotWithNewAwtHierarchy;
import static org.fest.swing.edt.GuiActionRunner.execute;
import static org.fest.swing.test.core.CommonAssertions.failWhenExpectingException;
import static org.fest.swing.test.core.TestGroups.GUI;
import static org.fest.swing.util.Arrays.format;
import static org.fest.util.Strings.concat;

/**
 * Test case for <a href="http://code.google.com/p/fest/issues/detail?id=135">Bug 135</a>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
@Test(groups = GUI)
public class TableContentsTest {

    private Robot robot;

    private MyWindow window;

    private JTableFixture fixture;

    @BeforeClass
    public void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeMethod
    public void setUp() {
        robot = robotWithNewAwtHierarchy();
        window = MyWindow.createNew();
        robot.showWindow(window);
        fixture = new JTableFixture(robot, window.table);
    }

    @AfterMethod
    public void tearDown() {
        robot.cleanUp();
    }

    public void shouldReturnTableContents() {
        String[][] contents = fixture.contents();
        assertThat(contents.length).isEqualTo(5);
        assertThat(contents[0].length).isEqualTo(5);
        assertThat(contents[0][0]).isEqualTo("Mary");
        assertThat(contents[0][1]).isEqualTo("Campione");
        assertThat(contents[0][2]).isEqualTo("Snowboarding");
        assertThat(contents[0][3]).isEqualTo("5");
        assertThat(contents[0][4]).isEqualTo("false");
        assertThat(contents[1][0]).isEqualTo("Alison");
        assertThat(contents[1][1]).isEqualTo("Huml");
        assertThat(contents[1][2]).isEqualTo("Rowing");
        assertThat(contents[1][3]).isEqualTo("3");
        assertThat(contents[1][4]).isEqualTo("true");
        assertThat(contents[2][0]).isEqualTo("Kathy");
        assertThat(contents[2][1]).isEqualTo("Walrath");
        assertThat(contents[2][2]).isEqualTo("Knitting");
        assertThat(contents[2][3]).isEqualTo("2");
        assertThat(contents[2][4]).isEqualTo("false");
        assertThat(contents[3][0]).isEqualTo("Sharon");
        assertThat(contents[3][1]).isEqualTo("Zakhour");
        assertThat(contents[3][2]).isEqualTo("Speed reading");
        assertThat(contents[3][3]).isEqualTo("20");
        assertThat(contents[3][4]).isEqualTo("true");
        assertThat(contents[4][0]).isEqualTo("Philip");
        assertThat(contents[4][1]).isEqualTo("Milne");
        assertThat(contents[4][2]).isEqualTo("Pool");
        assertThat(contents[4][3]).isEqualTo("10");
        assertThat(contents[4][4]).isEqualTo("false");
    }

    public void shouldPassIfContentIsEqualToExpected() {
        String[][] contents = new String[][] { { "Mary", "Campione", "Snowboarding", "5", "false" }, { "Alison", "Huml", "Rowing", "3", "true" }, { "Kathy", "Walrath", "Knitting", "2", "false" }, { "Sharon", "Zakhour", "Speed reading", "20", "true" }, { "Philip", "Milne", "Pool", "10", "false" } };
        fixture.requireContents(contents);
    }

    @Test(groups = GUI, dependsOnMethods = "shouldReturnTableContents")
    public void shouldFailIfContentNotEqualToExpected() {
        try {
            fixture.requireContents(new String[][] { { "hello" } });
            failWhenExpectingException();
        } catch (AssertionError e) {
            assertThat(e.getMessage()).contains("property:'contents'").contains("expected:<[['hello']]>").contains(concat("but was:<", format(fixture.contents()), ">"));
        }
    }

    private static class MyWindow extends TestWindow {

        private static final long serialVersionUID = 1L;

        @RunsInEDT
        static MyWindow createNew() {
            return execute(new GuiQuery<MyWindow>() {

                protected MyWindow executeInEDT() {
                    return new MyWindow();
                }
            });
        }

        final JTable table;

        private MyWindow() {
            super(TableContentsTest.class);
            TableRenderDemo newContentPane = new TableRenderDemo();
            table = newContentPane.table;
            newContentPane.setOpaque(true);
            setContentPane(newContentPane);
        }
    }
}
