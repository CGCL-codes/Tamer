package org.waveprotocol.wave.model.operation.wave;

import org.waveprotocol.wave.model.testing.ModelTestUtils;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for BlipContentOperation.
 *
 * @author anorth@google.com (Alex North)
 */
public class OperationEqualityTest extends OperationTestBase {

    public void testEquality() {
        List<Object> operations1 = createOperations();
        List<Object> operations2 = createOperations();
        int size = operations1.size();
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                assertEquals("failed pair's equals [i:" + i + ", " + operations1.get(i) + "] [j:" + j + ", " + operations2.get(j) + "]", i == j, operations1.get(i).equals(operations2.get(j)));
            }
        }
    }

    private List<Object> createOperations() {
        return Arrays.<Object>asList(new AddParticipant(context, fred), new AddParticipant(context, jane), new BlipContentOperation(context, ModelTestUtils.createContent("Hello")), new BlipContentOperation(context, ModelTestUtils.createContent("World")), new NoOp(context), new RemoveParticipant(context, fred), new RemoveParticipant(context, jane), new SubmitBlip(context), new VersionUpdateOp(fred, 1, null, "id1"), new VersionUpdateOp(fred, 1, null, "id2"));
    }
}
