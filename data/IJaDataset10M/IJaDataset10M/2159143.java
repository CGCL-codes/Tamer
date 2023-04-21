package org.jomc.model.test;

import junit.framework.Assert;
import org.jomc.model.DefaultModelProcessor;
import org.jomc.model.ModelContext;
import org.jomc.model.ModelProcessor;
import org.jomc.model.Modules;

/**
 * Test cases for {@code org.jomc.model.ModelProcessor} implementations.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a> 1.0
 * @version $Id: ModelProcessorTest.java 1659 2010-03-21 04:22:19Z schulte2005 $
 */
public class ModelProcessorTest {

    private ModelProcessor modelProcessor;

    public ModelProcessorTest() {
        this(null);
    }

    public ModelProcessorTest(final ModelProcessor modelProcessor) {
        super();
        this.modelProcessor = modelProcessor;
    }

    public ModelProcessor getModelProcessor() {
        if (this.modelProcessor == null) {
            this.modelProcessor = new DefaultModelProcessor();
        }
        return this.modelProcessor;
    }

    public void testProcessModules() throws Exception {
        final ModelContext context = ModelContext.createModelContext(this.getClass().getClassLoader());
        try {
            this.getModelProcessor().processModules(null, null);
            Assert.fail("Expected NullPointerException not thrown.");
        } catch (final NullPointerException e) {
            Assert.assertNotNull(e.getMessage());
            System.out.println(e);
        }
        try {
            this.getModelProcessor().processModules(context, null);
            Assert.fail("Expected NullPointerException not thrown.");
        } catch (final NullPointerException e) {
            Assert.assertNotNull(e.getMessage());
            System.out.println(e);
        }
        try {
            this.getModelProcessor().processModules(null, new Modules());
            Assert.fail("Expected NullPointerException not thrown.");
        } catch (final NullPointerException e) {
            Assert.assertNotNull(e.getMessage());
            System.out.println(e);
        }
    }
}
