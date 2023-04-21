package net.sf.daro.swing;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Test cases for {@link SwingWorkerExt}.
 * 
 * @author daniel
 */
public class SwingWorkerExtTest {

    /**
	 * Verifies the creation of a worker.
	 */
    @Test
    public void testCreationWithBlockingScope() {
        SampleWorker worker = new SampleWorker(this, BlockingScope.COMPONENT);
        Assert.assertEquals(this, worker.getBlockingTarget());
        Assert.assertEquals(BlockingScope.COMPONENT, worker.getBlockingScope());
    }

    /**
	 * Verifies the creation of a worker without blocking scope.
	 */
    @Test
    public void testCreationWithoutBlockingScope() {
        SampleWorker worker = new SampleWorker(this, null);
        Assert.assertEquals(this, worker.getBlockingTarget());
        Assert.assertEquals(BlockingScope.NONE, worker.getBlockingScope());
    }

    private class SampleWorker extends SwingWorkerExt<Integer, Void> {

        /**
		 * Creates a new SampleWorker.
		 * 
		 * @param blockingTarget
		 *            the blocking target
		 * @param blockingScope
		 *            the blocking scope
		 */
        public SampleWorker(Object blockingTarget, BlockingScope blockingScope) {
            super(blockingTarget, blockingScope);
        }

        @Override
        protected Integer doInBackground() throws Exception {
            return Integer.valueOf(5);
        }
    }
}
