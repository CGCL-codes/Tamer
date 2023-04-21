package tudresden.ocl20.pivot.metamodels.uml2.test.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import org.eclipse.core.runtime.FileLocator;
import org.junit.Test;
import tudresden.ocl20.pivot.facade.Ocl2ForEclipseFacade;
import tudresden.ocl20.pivot.metamodels.uml2.test.UML2MetaModelTestPlugin;
import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.model.ModelConstants;
import tudresden.ocl20.pivot.pivotmodel.Type;

/**
 * <p>
 * Contains test cases that test the adaptation of Classes contained in a
 * reference Jar archive.
 * </p>
 * 
 * @author Claas Wilke
 */
public class TestModelImport {

    /**
	 * <p>
	 * Returns the file object for a given path relative to the plug-in's
	 * directory.
	 * </p>
	 * 
	 * @param path
	 *            The path of the resource.
	 * @return The found {@link File} object.
	 * @throws Exception
	 *             Thrown, if the opening fails.
	 */
    protected static File getFile(String path) throws Exception {
        URL fileLocation;
        fileLocation = UML2MetaModelTestPlugin.getDefault().getBundle().getResource(path);
        fileLocation = FileLocator.resolve(fileLocation);
        File file;
        file = new File(fileLocation.getFile());
        assertTrue(file.exists());
        return file;
    }

    /**
	 * <p>
	 * Tests the adaptation of a provider class referencing a Jar archive.
	 * </p>
	 * 
	 * @throws Exception
	 */
    @Test
    public void testReferencedModel01() throws Exception, ModelAccessException {
        String msg;
        msg = "The adaptation of referenced UML models seems to be wrong. ";
        File modelFile;
        modelFile = TestModelImport.getFile("model/referenceTest/model02.uml");
        IModel model;
        model = Ocl2ForEclipseFacade.getModel(modelFile, Ocl2ForEclipseFacade.UML2_MetaModel);
        assertNotNull(msg, model);
        Type referredType;
        referredType = model.findType(Arrays.asList(new String[] { ModelConstants.ROOT_PACKAGE_NAME, "package1", "ReferredType" }));
        assertNotNull(msg, referredType);
        modelFile = TestModelImport.getFile("model/referenceTest/model01.uml");
        model = Ocl2ForEclipseFacade.getModel(modelFile, Ocl2ForEclipseFacade.UML2_MetaModel);
        assertNotNull(msg, model);
        Type testReferredType;
        testReferredType = model.findType(Arrays.asList(new String[] { ModelConstants.ROOT_PACKAGE_NAME, "package2", "ReferencingType" }));
        assertNull(msg, testReferredType);
    }

    /**
	 * <p>
	 * Tests the adaptation of a provider class referencing a Jar archive.
	 * </p>
	 * 
	 * @throws Exception
	 */
    @Test
    public void testReferencedModel02() throws Exception {
        String msg;
        msg = "The adaptation of referenced UML models seems to be wrong. ";
        File modelFile;
        modelFile = TestModelImport.getFile("model/referenceTest/model04.uml");
        IModel model;
        model = Ocl2ForEclipseFacade.getModel(modelFile, Ocl2ForEclipseFacade.UML2_MetaModel);
        assertNotNull(msg, model);
        Type referredType;
        referredType = model.findType(Arrays.asList(new String[] { ModelConstants.ROOT_PACKAGE_NAME, "ReferredType" }));
        assertNotNull(msg, referredType);
        modelFile = TestModelImport.getFile("model/referenceTest/model03.uml");
        model = Ocl2ForEclipseFacade.getModel(modelFile, Ocl2ForEclipseFacade.UML2_MetaModel);
        assertNotNull(msg, model);
        Type testReferredType;
        testReferredType = model.findType(Arrays.asList(new String[] { ModelConstants.ROOT_PACKAGE_NAME, "package2", "ReferencingType" }));
        assertNull(msg, testReferredType);
    }
}
