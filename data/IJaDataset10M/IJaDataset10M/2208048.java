package org.siberia.trans.xml;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import junit.framework.*;
import org.siberia.xml.schema.pluginarch.Repository;
import org.siberia.xml.schema.pluginarch.ObjectFactory;
import org.siberia.xml.schema.pluginarch.ModuleDeclaration;
import org.siberia.xml.schema.pluginarch.ModuleCategory;
import org.siberia.xml.schema.pluginarch.Modules;
import org.siberia.xml.schema.pluginarch.Module;
import org.siberia.xml.schema.pluginarch.ModuleBuild;
import org.siberia.xml.schema.pluginarch.ModuleDependency;

/**
 *
 * @author alexis
 */
public class JAXBLoaderTest extends TestCase {

    public JAXBLoaderTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(JAXBLoaderTest.class);
        return suite;
    }

    /**
     * Test of location methods of PluginLink
     */
    public void testUnmarshal() throws Exception {
        String prefix = "src/test/java";
        JAXBLoader jaxb = new JAXBLoader();
        Object o = null;
        try {
            o = jaxb.loadRepository(new FileInputStream(prefix + "/org/siberia/trans/xml/repository_example.xml"));
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
        assertNotNull("unable to unmarshal repository", o);
        o = null;
        try {
            o = jaxb.loadRepository(new FileInputStream(prefix + "/org/siberia/trans/xml/repository_example2.xml"));
            if (o instanceof Repository) {
                assertNotNull(((Repository) o).getModules());
            } else {
                assertFalse(true);
            }
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
        assertNotNull("unable to unmarshal repository", o);
        o = null;
        try {
            o = jaxb.loadModule(new FileInputStream(prefix + "/org/siberia/trans/xml/module_example.xml"));
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
        assertNotNull("unable to unmarshal module", o);
        assertEquals(3, ((Module) o).getBuild().size());
        for (int i = 0; i < ((Module) o).getBuild().size(); i++) {
            ModuleBuild build = ((Module) o).getBuild().get(i);
            if (build.getVersion().equals("0.0.1")) {
                assertEquals(0, build.getDependency().size());
            } else if (build.getVersion().equals("0.0.2")) {
                assertEquals(1, build.getDependency().size());
            } else if (build.getVersion().equals("0.0.3")) {
                assertEquals(2, build.getDependency().size());
                for (int j = 0; j < build.getDependency().size(); j++) {
                    ModuleDependency depn = build.getDependency().get(j);
                    if (j == 0) {
                        assertEquals("siberia-lang", depn.getName());
                        assertEquals("0.0.2", depn.getVersionConstraint());
                    } else if (j == 1) {
                        assertEquals("siberia-utilities", depn.getName());
                        assertEquals("0.0.1", depn.getVersionConstraint());
                    }
                }
            }
        }
        o = null;
        try {
            o = jaxb.loadRepositories(new FileInputStream(prefix + "/org/siberia/trans/xml/repositories_example.xml"));
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
        assertNotNull("unable to unmarshal repositories", o);
    }

    /**
     * Test of location methods of PluginLink
     */
    public void testmarshal() throws Exception {
        String prefix = "src/test/java";
        ObjectFactory factory = new ObjectFactory();
        Repository rep = factory.createRepository();
        rep.setName("alexis's repository");
        rep.setAdminmail("toto@sdjkf.fr");
        Modules modules = factory.createModules();
        ModuleDeclaration module = factory.createModuleDeclaration();
        module.setName("toto");
        module.setCategory(ModuleCategory.SYSTEM);
        module.setActive(false);
        module.setShortDescription("short desc");
        module.setLongDescription("long desc");
        modules.getModuleDeclaration().add(module);
        rep.setModules(modules);
        JAXBLoader jaxb = new JAXBLoader();
        jaxb.saveRepository(rep, new File(prefix + "/org/siberia/trans/xml/toto.xml"));
        Module module1 = factory.createModule();
        ModuleBuild build1 = factory.createModuleBuild();
        build1.setVersion("0.0.1");
        build1.setLicense("GPL");
        GregorianCalendar cal = new GregorianCalendar(2007, Calendar.NOVEMBER, 11, 20, 30, 52);
        XMLGregorianCalendar calendar = javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        build1.setReleaseDate(calendar);
        ModuleDependency depen1 = factory.createModuleDependency();
        depen1.setName("a");
        depen1.setVersionConstraint("toto");
        ModuleDependency depen2 = factory.createModuleDependency();
        depen2.setName("b");
        depen2.setVersionConstraint("tata");
        build1.getDependency().add(depen1);
        build1.getDependency().add(depen2);
        module1.getBuild().add(build1);
        jaxb.saveModule(module1, new File(prefix + "/org/siberia/trans/xml/tata.xml"));
    }
}
