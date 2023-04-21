package samples.junit4.resetmock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import samples.singleton.StaticService;
import static org.powermock.api.easymock.PowerMock.*;

/**
 * Asserts that it works to reset mocks for static methods even after verify has
 * been invoked.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(StaticService.class)
public class ResetForStaticMethodsTest {

    @Test
    public void assertThatResetWorksForStaticMethods() throws InterruptedException {
        mockStatic(StaticService.class);
        StaticService.sayHello();
        expectLastCall().once();
        replay(StaticService.class);
        StaticService.sayHello();
        verify(StaticService.class);
        reset(StaticService.class);
        StaticService.sayHello();
        expectLastCall().once();
    }
}
