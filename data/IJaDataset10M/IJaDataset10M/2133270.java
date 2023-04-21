package simtools.util;

import simtools.util.CircularBufferManager.InvalidIndex;
import junit.framework.TestCase;

public class CircularBufferManagerTest extends TestCase {

    public void testGetBufferIndex() {
        TestProvider provider = new TestProvider();
        CircularBufferManager cbm = new CircularBufferManager(provider, new TestPolicy());
        try {
            cbm.adjustBuffersSize();
            assertEquals(0, cbm.getStartIndex());
            assertEquals(0, cbm.getLastIndex());
            assertEquals(0, cbm.getBufferIndex(cbm.getStartIndex()));
            cbm.adjustBuffersSize();
            assertEquals(1, cbm.getStartIndex());
            assertEquals(1, cbm.getLastIndex());
            assertEquals(0, cbm.getBufferIndex(cbm.getStartIndex()));
            provider.requestedSize = 2;
            cbm.adjustBuffersSize();
            assertEquals(1, cbm.getStartIndex());
            assertEquals(2, cbm.getLastIndex());
            assertEquals(0, cbm.getBufferIndex(cbm.getStartIndex()));
            assertEquals(1, cbm.getBufferIndex(cbm.getLastIndex()));
            cbm.adjustBuffersSize();
            cbm.adjustBuffersSize();
            assertEquals(3, cbm.getStartIndex());
            assertEquals(4, cbm.getLastIndex());
            assertEquals(0, cbm.getBufferIndex(cbm.getStartIndex()));
            assertEquals(1, cbm.getBufferIndex(cbm.getLastIndex()));
            provider.requestedSize = 0;
            int k = 0;
            for (int i = 0; i < 10; i++) {
                k = cbm.adjustBuffersSize();
            }
            assertEquals(14 - 8 + 1, cbm.getStartIndex());
            assertEquals(14, cbm.getLastIndex());
            assertEquals(k + 1, cbm.getBufferIndex(cbm.getStartIndex()));
            assertEquals(k, cbm.getBufferIndex(cbm.getLastIndex()));
            provider.requestedSize = 1;
            k = cbm.adjustBuffersSize();
            assertEquals(14 - 1 + 1 + 1, cbm.getStartIndex());
            assertEquals(cbm.getStartIndex(), cbm.getLastIndex());
            assertEquals(k, cbm.getBufferIndex(cbm.getStartIndex()));
            assertEquals(k, cbm.getBufferIndex(cbm.getLastIndex()));
        } catch (InvalidIndex e) {
            fail("Unforeseen exception : " + e);
        }
    }

    static class TestPolicy implements CircularBufferManagerPolicy {

        public boolean getEnableSizeDicrease() {
            return true;
        }

        public int getIncreasedBufferSize(int currentSize) {
            return currentSize + 2;
        }

        public int getMaxSize() {
            return 8;
        }

        public double getMinDuration() {
            return 100;
        }

        public int getMinSize() {
            return 1;
        }
    }

    static class TestProvider implements CircularBufferProvider {

        int requestedSize = 1;

        public void changeBuffersSize(int newSize) {
        }

        public double getCurrentBufferDuration() {
            return 10;
        }

        public int getRequestedSize() {
            return requestedSize;
        }
    }
}
