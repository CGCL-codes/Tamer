package engine.operators.permutation;

import org.easymock.classextension.EasyMock;
import org.easymock.classextension.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.Test;
import engine.individuals.Permutation;
import engine.utils.WevoRandom;

/**
 * Tests for InversionMutation.
 * @author Karol Stosiek (karol.stosiek@gmail.com)
 * @author Michal Anglart (anglart.michal@gmail.com)
 */
public class InversionMutationTest {

    /** Mock control. */
    private final IMocksControl mockControl = EasyMock.createControl();

    /** Random number generator mock. */
    private WevoRandom generatorMock;

    /** Tests if single mutation works as expected. */
    @Test
    public void testSingleMutation() {
        final int chromosomeLength = 8;
        final Permutation originalIndividual = new Permutation(new int[] { 0, 1, 2, 3, 4, 5, 6, 7 });
        final Permutation expectedIndividual = new Permutation(new int[] { 0, 1, 2, 7, 6, 5, 4, 3 });
        mockControl.reset();
        generatorMock = mockControl.createMock(WevoRandom.class);
        EasyMock.expect(generatorMock.nextInt(0, chromosomeLength)).andReturn(3);
        EasyMock.expect(generatorMock.nextInt(0, chromosomeLength)).andReturn(7);
        InversionMutation mutation = new InversionMutation(generatorMock, 0.0);
        mockControl.replay();
        Permutation mutatedIndividual = mutation.mutate(originalIndividual);
        mockControl.verify();
        for (int i = 0; i < expectedIndividual.getSize(); i++) {
            Assert.assertEquals(mutatedIndividual.getValue(i), expectedIndividual.getValue(i));
        }
    }
}
