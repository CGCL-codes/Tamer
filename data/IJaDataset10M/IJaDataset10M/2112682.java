package net.sourceforge.nrl.parser.model.xsd;

import java.io.File;
import junit.framework.TestCase;
import net.sourceforge.nrl.parser.model.IClassifier;
import net.sourceforge.nrl.parser.model.IPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;

/**
 * Test loading of substitution groups.
 * 
 * @author Christian Nentwich
 */
public class SubstitutionGroupTest extends TestCase {

    private static boolean isInitialised = false;

    private static IPackage model = null;

    public void setUp() throws Exception {
        if (!isInitialised) {
            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xsd", new XSDResourceFactoryImpl());
            isInitialised = true;
            XSDModelLoader loader = new XSDModelLoader();
            model = loader.load(new File("testdata/schema/substitution.xsd"));
        }
    }

    public void testLoading() throws Exception {
        assertNotNull(model);
    }

    public void testSubstitution() throws Exception {
        assertNotNull(model.getElementByName("TheBase", true));
        assertNotNull(model.getElementByName("TheAlternativeA", true));
        assertNotNull(model.getElementByName("TheAlternativeB", true));
        assertNull(model.getElementByName("base", true));
        assertNull(model.getElementByName("alternativeA", true));
        assertNull(model.getElementByName("alternativeB", true));
        IClassifier obj = (IClassifier) model.getElementByName("Usage", true);
        assertNotNull(obj);
        assertEquals(3, obj.getAttributes(false).size());
        assertTrue(((Boolean) obj.getAttributeByName("base", false).getUserData(IXSDUserData.SUBSTITUTABLE)).booleanValue());
        assertEquals("TheBase", obj.getAttributeByName("base", false).getType().getName());
        assertEquals("TheAlternativeA", obj.getAttributeByName("alternativeA", false).getType().getName());
        assertEquals("base", ((XSDAttribute) obj.getAttributeByName("alternativeA", false).getUserData(IXSDUserData.SUBSTITUTION_FOR)).getName());
        assertEquals("TheAlternativeB", obj.getAttributeByName("alternativeB", false).getType().getName());
        assertEquals("base", ((XSDAttribute) obj.getAttributeByName("alternativeB", false).getUserData(IXSDUserData.SUBSTITUTION_FOR)).getName());
    }
}
