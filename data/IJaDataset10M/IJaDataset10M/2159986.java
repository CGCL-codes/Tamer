package de.fuh.xpairtise.tests.common.replication;

import junit.framework.TestCase;
import de.fuh.xpairtise.common.replication.elements.ReplicatedWhiteboardPencilUpdate;

public class ReplicatedWhiteboardPencilUpdateTest extends TestCase {

    public void testPerformCopyDataFromValidatedElement() {
        byte[] color = { 0, 0, 0 };
        int points1[] = { 1, 3 };
        int points2[] = { 2, 4 };
        ReplicatedWhiteboardPencilUpdate e1 = new ReplicatedWhiteboardPencilUpdate("id1", points1, color, 1);
        ReplicatedWhiteboardPencilUpdate e2 = new ReplicatedWhiteboardPencilUpdate("id2", points2, color, 2);
        assertFalse(e1.getClientId().equals(e2.getClientId()));
        assertFalse(e1.getPoints()[0] == e2.getPoints()[0]);
        assertFalse(e1.getPoints()[1] == e2.getPoints()[1]);
        e2.setSequenceId(e1.getSequenceId() + 1);
        e1.copyDataFromElement(e2);
        assertTrue(e1.getClientId().equals(e2.getClientId()));
        assertTrue(e1.getPoints()[0] == e2.getPoints()[0]);
        assertTrue(e1.getPoints()[1] == e2.getPoints()[1]);
    }

    public void testReplicatedWhiteboardPencilUpdate() {
        byte[] color = { 0, 0, 0 };
        int points[] = { 17, 23 };
        ReplicatedWhiteboardPencilUpdate e = new ReplicatedWhiteboardPencilUpdate("id", points, color, 1);
        assertEquals("id", e.getClientId());
        assertEquals(17, e.getPoints()[0]);
        assertEquals(23, e.getPoints()[1]);
    }
}
