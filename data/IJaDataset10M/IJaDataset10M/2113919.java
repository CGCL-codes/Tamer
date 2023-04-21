package classifier.examples.operators;

import java.util.Arrays;
import java.util.List;
import org.easymock.classextension.EasyMock;
import org.easymock.classextension.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.Test;
import engine.utils.JavaRandom;

/**
 * Tests for {@link PhotoRulesIntervalAddMutation}.
 * @author Lukasz Krawiec (lmkrawiec@gmail.com)
 * @author Michal Anglart (anglart.michal@gmail.com)
 */
public class PhotoRulesIntervalAddMutationTest {

    /** Mock controller. */
    private IMocksControl mockControl = EasyMock.createControl();

    /** Random generator mock. */
    private JavaRandom randomMock;

    /** Test adding interval. */
    @Test
    public void testAddInterval() {
        List<Integer> set = Arrays.asList(10, 20, 30, 40);
        createMocks();
        EasyMock.expect(randomMock.nextInt(0, 256)).andReturn(50);
        mockControl.replay();
        PhotoRulesIntervalAddMutation mutation = new PhotoRulesIntervalAddMutation(randomMock, 40);
        List<Integer> mutatedSet = mutation.apply(set);
        mockControl.verify();
        Assert.assertEquals(set, Arrays.asList(10, 20, 30, 40));
        Assert.assertEquals(mutatedSet, Arrays.asList(10, 20, 30, 40, 30, 70));
    }

    /** Creates mock instances used in tests. */
    private void createMocks() {
        mockControl.reset();
        randomMock = mockControl.createMock(JavaRandom.class);
    }
}
