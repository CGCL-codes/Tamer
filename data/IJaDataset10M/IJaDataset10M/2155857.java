package org.peaseplate.templateengine.showcase;

import org.peaseplate.utils.resource.ResourceKey;
import org.testng.annotations.Test;

@Test(groups = { "template-engine", "showcase" })
public class WhileLoopShowcase extends AbstractShowcase {

    public static class WorkingObject {

        private int index = 0;

        public int incIndex() {
            return index++;
        }

        public int getIndex() {
            return index;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected ResourceKey[] getResourceKeys() {
        return new ResourceKey[] { new ResourceKey("org/peaseplate/templateengine/showcase/whileLoopShowcase.template") };
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected Object createWorkingObject(final ResourceKey key) {
        return new WorkingObject();
    }
}
