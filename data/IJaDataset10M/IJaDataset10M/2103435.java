package eu.planets_project.services.datatypes;

import static org.junit.Assert.assertTrue;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import eu.planets_project.services.identify.Identify;

/**
 * @author <a href="mailto:Andrew.Jackson@bl.uk">Andy Jackson</a>, <a
 *         href="mailto:fabian.steeg@uni-koeln.de">Fabian Steeg</a>
 */
public class ServiceDescriptionTest {

    /**
     * A short overview of using the ServiceDescription API:
     */
    @Test
    public void sampleUsage() {
        ServiceDescription description1 = new ServiceDescription.Builder("Test Service", Identify.class.getName()).author("Some One").build();
        Assert.assertEquals("Some One", description1.getAuthor());
        description1 = ServiceDescription.create("Test Service", Identify.class.getName()).build();
        Assert.assertEquals("Test Service", description1.getName());
        ServiceDescription copy = ServiceDescription.copy(description1).build();
        Assert.assertEquals(description1, copy);
        ServiceDescription description2 = new ServiceDescription.Builder(description1).author("Another One").build();
        Assert.assertEquals("Another One", description2.getAuthor());
        Assert.assertEquals("Test Service", description2.getName());
        ServiceDescription description3 = ServiceDescription.of(description2.toXml());
        Assert.assertEquals(description2, description3);
    }

    ServiceDescription sd = null;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        ServiceDescription.Builder builder = new ServiceDescription.Builder("A Test Service", "eu.planets_project.services.identify.Identify");
        builder.author("Andrew N. Jackson <Andrew.Jackson@bl.uk>");
        builder.description("This is just a simple test service description, used to unit test the Service Description code.");
        builder.classname(ServiceDescriptionTest.class.getCanonicalName());
        builder.furtherInfo(new URI("http://www.planets-project.eu/"));
        builder.inputFormats(new URI("planets:fmt/ext/jpg"), new URI("planets:fmt/ext/jpeg"));
        builder.instructions("There are not special instructions for this service.");
        List<Parameter> pars = new ArrayList<Parameter>();
        pars.add(new Parameter("planets:srv/par/test", "true"));
        builder.parameters(pars);
        sd = builder.build();
    }

    /**
     * Test method for
     * {@link eu.planets_project.services.datatypes.ServiceDescription#toXml()}.
     */
    @Test
    public void testToXml() {
        String sdXml = sd.toXmlFormatted();
        System.out.println(sdXml);
        try {
            String sdfname = "serviceDescription.xml";
            BufferedWriter out = new BufferedWriter(new FileWriter(sdfname));
            out.write(sdXml);
            out.close();
            System.out.println("Wrote service description to file: " + sdfname);
        } catch (IOException e) {
        }
        ServiceDescription nsd = ServiceDescription.of(sdXml);
        assertTrue("Re-serialised ServiceDescription does not match the original. ", sd.equals(nsd));
    }

    /**
     * Test method for
     * {@link eu.planets_project.services.datatypes.ServiceDescription#equals(Object)}
     * .
     */
    @Test
    public void equalsWithIdentifier() {
        String name = "name";
        String type = "type";
        String id = "id1";
        ServiceDescription original = new ServiceDescription.Builder(name, type).identifier(id).build();
        ServiceDescription copy = ServiceDescription.of(original.toXml());
        Assert.assertEquals(id, original.getIdentifier());
        Assert.assertEquals(name, original.getName());
        Assert.assertEquals(type, original.getType());
        Assert.assertEquals(original, copy);
        Assert.assertNotSame(original, new ServiceDescription.Builder(original).identifier("id2").build());
    }

    /**
     * Test method for
     * {@link eu.planets_project.services.datatypes.ServiceDescription#equals(Object)}
     * .
     */
    @Test
    public void equalsWithoutIdentifier() {
        ServiceDescription original = new ServiceDescription.Builder("name", "type").build();
        ServiceDescription copy = ServiceDescription.of(original.toXml());
        Assert.assertEquals(original, copy);
        Assert.assertNotSame(original, new ServiceDescription.Builder(original).author("me").build());
    }

    /**
     * Test method for
     * {@link eu.planets_project.services.datatypes.ServiceDescription#equals(Object)}
     * .
     */
    @Test
    public void equalsCornerCases() {
        Assert.assertFalse(sd.equals("Some string"));
        Assert.assertFalse(sd.equals(1));
        Assert.assertFalse(sd.equals(null));
    }

    /**
     * Test method for
     * {@link eu.planets_project.services.datatypes.ServiceDescription#hashCode()}
     * .
     */
    @Test
    public void hashCodeUsage() {
        Set<ServiceDescription> set = new HashSet<ServiceDescription>();
        set.add(sd);
        set.add(ServiceDescription.of(sd.toXml()));
        set.add(ServiceDescription.of(sd.toXml()));
        set.add(new ServiceDescription.Builder(sd).author("me").build());
        Assert.assertEquals(2, set.size());
    }

    @Test
    public void schemaGeneration() {
        ServiceDescription.main(null);
    }
}
