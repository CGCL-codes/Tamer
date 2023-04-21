package org.goodoldai.jeff.explanation.builder;

import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.builder.internationalization.InternationalizationManager;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Locale;
import junit.framework.TestCase;

/**
 * @author Bojan Tomic
 */
public class DefaultImageExplanationChunkBuilderTest extends TestCase {

    ImageData imagedata = null;

    int context = 0;

    String group = null;

    String rule = null;

    String[] tags = null;

    File unit = null;

    File dimensionNames = null;

    File imageCaptions = null;

    InternationalizationManager im = null;

    DefaultImageExplanationChunkBuilder instance = null;

    @Override
    protected void setUp() throws Exception {
        context = ExplanationChunk.ERROR;
        group = "group 1";
        rule = "rule 1";
        tags = new String[2];
        tags[0] = "tag1";
        tags[1] = "tag2";
        imagedata = new ImageData("URL1", "Whale photo");
        unit = new File("test" + File.separator + "units_srb_RS.properties");
        PrintWriter unitpw = new PrintWriter(new FileWriter(unit));
        unitpw.println("EUR = RSD");
        unitpw.close();
        dimensionNames = new File("test" + File.separator + "dimension_names_srb_RS.properties");
        PrintWriter dimnamespw = new PrintWriter(new FileWriter(dimensionNames));
        dimnamespw.println("distance = razdaljina");
        dimnamespw.println("money = novac");
        dimnamespw.println("profit = dobit (profit)");
        dimnamespw.close();
        imageCaptions = new File("test" + File.separator + "image_captions_srb_RS.properties");
        PrintWriter imagecaptpw = new PrintWriter(new FileWriter(imageCaptions));
        imagecaptpw.println("Whale\\ photo = Fotografija kita");
        imagecaptpw.println("Image\\ 1 = Slika 1");
        imagecaptpw.close();
        im = new InternationalizationManager(new Locale("srb", "RS"));
        instance = new DefaultImageExplanationChunkBuilder(im);
    }

    @Override
    protected void tearDown() throws Exception {
        unit.delete();
        dimensionNames.delete();
        imageCaptions.delete();
    }

    /**
     * Test of constructor, of class DefaultImageExplanationChunkBuilder.
     * Test case: unsuccessfull execution - i18nManager is null
     */
    public void testConstructori18nManagerNull() {
        try {
            instance = new DefaultImageExplanationChunkBuilder(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The entered i18nManager instance must not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildChunk method, of class DefaultImageExplanationChunkBuilder.
     * Test case: unsuccessfull execution - content is null
     */
    public void testBuildChunkNullContent() {
        try {
            instance.buildChunk(context, group, rule, tags, null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You must enter image data as content";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildChunk method, of class DefaultImageExplanationChunkBuilder.
     * Test case: unsuccessfull execution - wrong type content
     */
    public void testBuildChunkWrongTypeContent() {
        try {
            instance.buildChunk(context, group, rule, tags, "content");
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You must enter image data as content";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildChunk method, of class DefaultImageExplanationChunkBuilder.
     * Test case: successfull execution - caption is null
     */
    public void testBuildChunkSuccessfull1() {
        imagedata.setCaption(null);
        ImageExplanationChunk imc = (ImageExplanationChunk) (instance.buildChunk(context, group, rule, tags, imagedata));
        assertEquals(context, imc.getContext());
        assertEquals(group, imc.getGroup());
        assertEquals(rule, imc.getRule());
        assertEquals(tags, imc.getTags());
        assertEquals(imagedata, imc.getContent());
        assertEquals(null, ((ImageData) (imc.getContent())).getCaption());
        assertEquals("URL1", ((ImageData) (imc.getContent())).getURL());
    }

    /**
     * Test of buildChunk method, of class DefaultImageExplanationChunkBuilder.
     * Test case: successfull execution - translation is performed
     */
    public void testBuildChunkSuccessfull2() {
        ImageExplanationChunk imc = (ImageExplanationChunk) (instance.buildChunk(context, group, rule, tags, imagedata));
        assertEquals(context, imc.getContext());
        assertEquals(group, imc.getGroup());
        assertEquals(rule, imc.getRule());
        assertEquals(tags, imc.getTags());
        assertEquals(imagedata, imc.getContent());
        assertEquals("Fotografija kita", ((ImageData) (imc.getContent())).getCaption());
        assertEquals("URL1", ((ImageData) (imc.getContent())).getURL());
    }

    /**
     * Test of buildChunk method, of class DefaultImageExplanationChunkBuilder.
     * Test case: successfull execution - translation does not exist
     */
    public void testBuildChunkSuccessfull3() {
        imagedata.setCaption("Unknown picture");
        ImageExplanationChunk imc = (ImageExplanationChunk) (instance.buildChunk(context, group, rule, tags, imagedata));
        assertEquals(context, imc.getContext());
        assertEquals(group, imc.getGroup());
        assertEquals(rule, imc.getRule());
        assertEquals(tags, imc.getTags());
        assertEquals(imagedata, imc.getContent());
        assertEquals("Unknown picture", ((ImageData) (imc.getContent())).getCaption());
        assertEquals("URL1", ((ImageData) (imc.getContent())).getURL());
    }
}
