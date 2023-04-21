package org.jomc.model.test;

import java.util.logging.Level;
import javax.xml.transform.Source;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.jomc.model.ModelContext;
import org.jomc.model.ModelException;
import org.jomc.model.Modules;

/**
 * Test cases for {@code org.jomc.model.ModelContext} implementations.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a> 1.0
 * @version $Id: ModelContextTest.java 1659 2010-03-21 04:22:19Z schulte2005 $
 */
public class ModelContextTest extends TestCase {

    private ModelContext modelContext;

    public ModelContextTest() {
        this(null);
    }

    public ModelContextTest(final ModelContext modelContext) {
        super();
        this.modelContext = modelContext;
    }

    public ModelContext getModelContext() throws ModelException {
        if (this.modelContext == null) {
            this.modelContext = ModelContext.createModelContext(this.getClass().getClassLoader());
            this.modelContext.getListeners().add(new ModelContext.Listener() {

                @Override
                public void onLog(final Level level, final String message, final Throwable t) {
                    System.out.println("[" + level.getLocalizedName() + "] " + message);
                }
            });
        }
        return this.modelContext;
    }

    public void testFindClass() throws Exception {
        try {
            this.getModelContext().findClass(null);
            Assert.fail("Expected NullPointerException not thrown.");
        } catch (final NullPointerException e) {
            Assert.assertNotNull(e.getMessage());
            System.out.println(e.toString());
        }
        Assert.assertEquals(Object.class, this.getModelContext().findClass("java.lang.Object"));
        Assert.assertNull(this.getModelContext().findClass("DOES_NOT_EXIST"));
    }

    public void testFindResource() throws Exception {
        try {
            this.getModelContext().findResource(null);
            Assert.fail("Expected NullPointerException not thrown.");
        } catch (final NullPointerException e) {
            Assert.assertNotNull(e.getMessage());
            System.out.println(e.toString());
        }
        Assert.assertNotNull(this.getModelContext().findResource("META-INF/jomc.xsl"));
    }

    public void testFindResources() throws Exception {
        try {
            this.getModelContext().findResources(null);
            Assert.fail("Expected NullPointerException not thrown.");
        } catch (final NullPointerException e) {
            Assert.assertNotNull(e.getMessage());
            System.out.println(e.toString());
        }
        Assert.assertTrue(this.getModelContext().findResources("META-INF/jomc.xsl").hasMoreElements());
    }

    public void testFindModules() throws Exception {
        this.getModelContext().findModules();
    }

    public void testProcessModules() throws Exception {
        try {
            this.getModelContext().processModules(null);
            Assert.fail("Expected NullPointerException not thrown.");
        } catch (final NullPointerException e) {
            Assert.assertNotNull(e.getMessage());
            System.out.println(e);
        }
        this.getModelContext().processModules(new Modules());
    }

    public void testValidateModel() throws Exception {
        try {
            this.getModelContext().validateModel((Modules) null);
            Assert.fail("Expected NullPointerException not thrown.");
        } catch (final NullPointerException e) {
            Assert.assertNotNull(e.getMessage());
            System.out.println(e.toString());
        }
        try {
            this.getModelContext().validateModel((Source) null);
            Assert.fail("Expected NullPointerException not thrown.");
        } catch (final NullPointerException e) {
            Assert.assertNotNull(e.getMessage());
            System.out.println(e.toString());
        }
        this.getModelContext().validateModel(new Modules());
    }

    public void testCreateModelContext() throws Exception {
        ModelContext.setModelContextClassName(null);
        Assert.assertNotNull(ModelContext.createModelContext(null));
        Assert.assertNotNull(ModelContext.createModelContext(this.getClass().getClassLoader()));
        ModelContext.setModelContextClassName("DOES_NOT_EXIST");
        try {
            ModelContext.createModelContext(null);
            Assert.fail("Expected ModelException not thrown.");
        } catch (final ModelException e) {
            Assert.assertNotNull(e.getMessage());
            System.out.println(e);
        }
        try {
            ModelContext.createModelContext(this.getClass().getClassLoader());
            Assert.fail("Expected ModelException not thrown.");
        } catch (final ModelException e) {
            Assert.assertNotNull(e.getMessage());
            System.out.println(e);
        }
        ModelContext.setModelContextClassName(null);
    }

    public void testCreateContext() throws Exception {
        Assert.assertNotNull(this.getModelContext().createContext());
    }

    public void testCreateMarshaller() throws Exception {
        Assert.assertNotNull(this.getModelContext().createMarshaller());
    }

    public void testCreateUnmarshaller() throws Exception {
        Assert.assertNotNull(this.getModelContext().createUnmarshaller());
    }

    public void testCreateSchema() throws Exception {
        Assert.assertNotNull(this.getModelContext().createSchema());
    }

    public void testCreateEntityResolver() throws Exception {
        Assert.assertNotNull(this.getModelContext().createEntityResolver());
    }

    public void testCreateResourceResolver() throws Exception {
        Assert.assertNotNull(this.getModelContext().createResourceResolver());
    }
}
