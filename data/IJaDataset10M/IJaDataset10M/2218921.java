package org.fest.swing.driver;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import javax.swing.AbstractButton;
import org.fest.mocks.EasyMockTemplate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests for <code>{@link ClickButtonTask}</code>.
 *
 * @author Yvonne Wang
 */
public class ClickButtonTaskTest {

    private AbstractButton button;

    private ClickButtonTask task;

    @BeforeClass
    public void setUp() {
        button = createMock(AbstractButton.class);
        task = new ClickButtonTask(button);
    }

    @Test
    public void shouldClickButton() {
        new EasyMockTemplate(button) {

            protected void expectations() {
                button.doClick();
                expectLastCall().once();
            }

            protected void codeToTest() {
                task.run();
            }
        }.run();
    }
}
