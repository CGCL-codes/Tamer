package eu.redseeds.scl.model.impl.rsl.rsldomainelements.terms;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import eu.redseeds.scl.model.AbstractModelTest;
import eu.redseeds.scl.model.rsl.rsldomainelements.terms.DeterminerDTO;
import eu.redseeds.scl.model.rsl.rsldomainelements.terms.ModifierDTO;
import eu.redseeds.scl.model.rsl.rsldomainelements.terms.NounDTO;
import eu.redseeds.scl.model.rsl.rsldomainelements.terms.PrepositionDTO;
import eu.redseeds.scl.model.rsl.rsldomainelements.terms.VerbDTO;

public class TermsDTOsTest extends AbstractModelTest {

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
    }

    /**
	 * Test method for {@link eu.redseeds.scl.impl.rsl.rslrequirementrepresentationsentences.svosentences.SVOSentenceImpl#getUid()}.
	 */
    @Test
    public void testCreateTerm() {
        NounDTO n = facade.createNounDTO();
        Assert.assertNotNull("Creating Noun failed with null", n);
        Assert.assertEquals("Creating Noun failed with null", "", n.toString());
        VerbDTO v = facade.createVerbDTO();
        Assert.assertNotNull("Creating Verb failed with null", v);
        Assert.assertEquals("Creating Verb failed with null", "", v.toString());
        ModifierDTO m = facade.createModifierDTO();
        Assert.assertNotNull("Creating Modifier failed with null", m);
        Assert.assertEquals("Creating Modifier failed with null", "", m.toString());
        DeterminerDTO d = facade.createDeterminerDTO();
        Assert.assertNotNull("Creating Determiner failed with null", d);
        Assert.assertEquals("Creating Determiner failed with null", "", d.toString());
        PrepositionDTO p = facade.createPrepostitionDTO();
        Assert.assertNotNull("Creating Prepostition failed with null", p);
        Assert.assertEquals("Creating Prepostition failed with null", "", p.toString());
    }

    /**
	 * Test method for {@link eu.redseeds.scl.impl.rsl.rslrequirementrepresentationsentences.svosentences.SVOSentenceImpl#getUid()}.
	 */
    @Test
    public void testCreateNamedTerm() {
        String nname = "noun";
        String vname = "verb";
        String pname = "preposition";
        String mname = "modifier";
        String dname = "determiner";
        NounDTO n = facade.createNounDTO(nname);
        Assert.assertNotNull("Creating named Noun failed with null", n);
        Assert.assertEquals("Creating Noun failed with different names", n.getName(), nname);
        VerbDTO v = facade.createVerbDTO(vname);
        Assert.assertNotNull("Creating named Verb failed with null", v);
        Assert.assertEquals("Creating Noun failed with different names", v.getName(), vname);
        ModifierDTO m = facade.createModifierDTO(mname);
        Assert.assertNotNull("Creating named Modifier failed with null", m);
        Assert.assertEquals("Creating Noun failed with different names", m.getName(), mname);
        DeterminerDTO d = facade.createDeterminerDTO(dname);
        Assert.assertNotNull("Creating named Determiner failed with null", d);
        Assert.assertEquals("Creating Noun failed with different names", d.getName(), dname);
        PrepositionDTO p = facade.createPrepostitionDTO(pname);
        Assert.assertNotNull("Creating named Verb failed with null", p);
        Assert.assertEquals("Creating Noun failed with different names", p.getName(), pname);
    }

    /**
	 * Test method for {@link eu.redseeds.scl.impl.rsl.rslrequirementrepresentationsentences.svosentences.SVOSentenceImpl#getUid()}.
	 */
    @Test
    public void testNounEqulasFalse() {
        String nname = "noun";
        String nname1 = "notnoun";
        NounDTO n = facade.createNounDTO(nname);
        Assert.assertNotNull("Creating named Noun failed with null", n);
        Assert.assertEquals("Creating Noun failed with different names", n.getName(), nname);
        NounDTO n1 = facade.createNounDTO(nname1);
        Assert.assertNotNull("Creating named Noun failed with null", n1);
        Assert.assertEquals("Creating Noun failed with different names", n1.getName(), nname1);
        NounDTO n2 = facade.createNounDTO();
        Assert.assertNotNull("Creating named Noun failed with null", n2);
        Assert.assertNull("Creating Noun failed with different names", n2.getName());
        Assert.assertFalse("testNounEqulasFalse fails with true", n.equals(n1));
        Assert.assertFalse("testNounEqulasFalse fails with true", n.equals(n2));
    }

    /**
	 * Test method for {@link eu.redseeds.scl.impl.rsl.rslrequirementrepresentationsentences.svosentences.SVOSentenceImpl#getUid()}.
	 */
    @Test
    public void testNounEqulasTrue() {
        String nname = "noun";
        String nname1 = "nouns";
        NounDTO n = facade.createNounDTO(nname);
        Assert.assertNotNull("Creating named Noun failed with null", n);
        Assert.assertEquals("Creating Noun failed with different names", n.getName(), nname);
        NounDTO n1 = facade.createNounDTO(nname1);
        Assert.assertNotNull("Creating named Noun failed with null", n1);
        Assert.assertEquals("Creating Noun failed with different names", n1.getName(), nname1);
        NounDTO n2 = facade.createNounDTO(nname);
        Assert.assertNotNull("Creating named Noun failed with null", n2);
        Assert.assertEquals("Creating Noun failed with different names", n2.getName(), nname);
        Assert.assertTrue("testNounEqulasTrue fails with false", n.equals(n1));
        Assert.assertTrue("testNounEqulasTrue fails with false", n.equals(n2));
    }
}
