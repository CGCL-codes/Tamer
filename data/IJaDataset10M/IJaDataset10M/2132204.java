package it.xtypes.generator.ui.tests;

import it.xtypes.tests.common.TypeSystemAbstractTests;
import it.xtypes.typesystem.TypeSystemDefinition;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Lorenzo Bettini
 * 
 */
public class PluginJavaExtensionsTest extends TypeSystemAbstractTests {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testRegisterEPackage() throws Exception {
        TypeSystemDefinition model = getTypeSystemModel(programCreator.createFJTypeSystemProgram(programCreator.createRule("Field", " var List[Field] F1 ", "G ||-- var Class C : 'ok'")));
        EPackage ePackage = model.getEPackage();
        Map<String, URI> ePackageNsURIToGenModelLocationMap = EcorePlugin.getEPackageNsURIToGenModelLocationMap();
        String nsURI = ePackage.getNsURI();
        System.out.println("package URI: " + nsURI);
        URI uri = ePackageNsURIToGenModelLocationMap.get(nsURI);
        System.out.println(uri);
        assertTrue(uri != null);
    }

    @Test
    public void testRegisterEPackage2() throws Exception {
        TypeSystemDefinition model = getTypeSystemModel(programCreator.createFJTypeSystemProgram(programCreator.createRule("Field", " var List[Field] F1 ", "G ||-- var Class C : 'ok'")));
        EPackage ePackage = model.getEPackage();
        Map<String, URI> ePackageNsURIToGenModelLocationMap = EcorePlugin.getEPackageNsURIToGenModelLocationMap();
        String nsURI = ePackage.getNsURI();
        System.out.println("package URI: " + nsURI);
        URI uri = ePackageNsURIToGenModelLocationMap.get(nsURI);
        System.out.println(uri);
        assertTrue(uri != null);
    }

    @Test
    public void testRegisterEPackageEcore() throws Exception {
        TypeSystemDefinition model = getTypeSystemModel("grammar \"http://www.eclipse.org/emf/2002/Ecore\" " + "axiom EClass " + "G ||-- var EClass C : $C");
        EPackage ePackage = model.getEPackage();
        Map<String, URI> ePackageNsURIToGenModelLocationMap = EcorePlugin.getEPackageNsURIToGenModelLocationMap();
        String nsURI = ePackage.getNsURI();
        System.out.println("package URI: " + nsURI);
        URI uri = ePackageNsURIToGenModelLocationMap.get(nsURI);
        System.out.println(uri);
        assertTrue(uri != null);
    }
}
