package org.jomc.ant.test;

import org.jomc.ant.ResourceFileProcessorTask;

/**
 * Test cases for class {@code org.jomc.ant.ResourceFileProcessorTask}.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JOMC$
 */
public class ResourceFileProcessorTaskTest extends JomcToolTaskTest {

    /** Creates a new {@code ResourceFileProcessorTaskTest} instance. */
    public ResourceFileProcessorTaskTest() {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public ResourceFileProcessorTask getJomcTask() {
        return (ResourceFileProcessorTask) super.getJomcTask();
    }

    /** {@inheritDoc} */
    @Override
    protected ResourceFileProcessorTask newJomcTask() {
        return new ResourceFileProcessorTask();
    }

    /** {@inheritDoc} */
    @Override
    protected String getBuildFileName() {
        return "resource-file-processor-task-test.xml";
    }
}
