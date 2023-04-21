package org.fest.swing.fixture;

import javax.swing.JPanel;
import org.testng.annotations.Test;
import org.fest.swing.driver.ComponentDriver;
import org.fest.swing.driver.JComponentDriver;
import static org.easymock.classextension.EasyMock.createMock;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.test.builder.JPanels.panel;

/**
 * Tests for <code>{@link JPanelFixture}</code>.
 *
 * @author Alex Ruiz
 */
public class JPanelFixtureTest extends CommonComponentFixtureTestCase<JPanel> {

    private JComponentDriver driver;

    private JPanel target;

    private JPanelFixture fixture;

    void onSetUp() {
        driver = createMock(JComponentDriver.class);
        target = panel().createNew();
        fixture = new JPanelFixture(robot(), target);
        fixture.updateDriver(driver);
    }

    @Test
    public void shouldCreateFixtureWithGivenComponentName() {
        String name = "panel";
        expectLookupByName(name, JPanel.class);
        verifyLookup(new JPanelFixture(robot(), name));
    }

    @Test
    public void shouldBeContainerFixture() {
        assertThat(fixture).isInstanceOf(ContainerFixture.class);
    }

    ComponentDriver driver() {
        return driver;
    }

    JPanel target() {
        return target;
    }

    JPanelFixture fixture() {
        return fixture;
    }
}
