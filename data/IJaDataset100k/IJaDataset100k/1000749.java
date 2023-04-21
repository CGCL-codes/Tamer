package net.sf.dvorcode;

import net.sf.dvorcode.Dvorcode.Codec;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mike
 */
public class DvorcodeTest {

    private static String Dvorak_UPPER_keys, Dvorak_lower_keys, QWERTY_UPPER_keys, QWERTY_lower_keys, unmapped_chars, simple_aoeu, simple_asdf;

    private static Codec DECODE, ENCODE;

    public DvorcodeTest() {
    }

    /**
     * Sets up a simple fixture for testing Dvorak<->QWERTY conversions
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        Dvorak_UPPER_keys = "~!@#$%^&*(){}\"<>PYFGCRL?+|AOEUIDHTNS_:QJKXBMWVZ";
        Dvorak_lower_keys = "`1234567890[]',.pyfgcrl/=\\aoeuidhtns-;qjkxbmwvz";
        QWERTY_UPPER_keys = "~!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL:\"ZXCVBNM<>?";
        QWERTY_lower_keys = "`1234567890-=qwertyuiop[]\\asdfghjkl;'zxcvbnm,./";
        unmapped_chars = "© amAM`1234567890~!@#$%^&*()\\|";
        simple_aoeu = "aoeuiDHTNS1";
        simple_asdf = "asdfgHJKL:1";
        DECODE = Dvorcode.Codec.DECODE;
        ENCODE = Dvorcode.Codec.ENCODE;
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of transcode method, of class Dvorcode called with blank cypher
     */
    @Test
    public void testTranscode_String_DvorcodeCodec_blankCypher() {
        System.out.println("transcode (blank cypher)");
        String cypher = "";
        Codec direction = DECODE;
        String expResult = "";
        String result = Dvorcode.transcode(cypher, direction);
        assertEquals(expResult, result);
    }

    /**
     * Test of transcode method, of class Dvorcode called with blank cypher
     */
    @Test
    public void testTranscode_String_DvorcodeCodec_blankArgs() {
        System.out.println("transcode (blank args)");
        String cypher = "";
        Codec direction = null;
        String expResult = null;
        String result = Dvorcode.transcode(cypher, direction);
        assertEquals(expResult, result);
    }

    /**
     * Test of transcode method, of class Dvorcode called with null arguments
     */
    @Test
    public void testTransCode_String_DvorcodeCodec_nullArgs() {
        System.out.println("transcode (null args)");
        String cypher = null;
        Codec direction = null;
        String expResult = null;
        String result = Dvorcode.transcode(cypher, direction);
        assertEquals(expResult, result);
    }

    /**
     * Test of transcode method, of class Dvorcode called with null direction
     */
    @Test
    public void testTransCode_String_DvorcodeCodec_nullDirection() {
        System.out.println("transcode (null direction)");
        String cypher = simple_aoeu;
        Codec direction = null;
        String expResult = null;
        String result = Dvorcode.transcode(cypher, direction);
        assertEquals(expResult, result);
    }

    /**
     * Test of transcode method, of class Dvorcode decoding shifted key values
     */
    @Test
    public void testTransCode_String_DvorcodeCodec_decode_shiftedKeys() {
        System.out.println("transcode - decoding (shifted keys)");
        String cypher = QWERTY_UPPER_keys;
        Codec direction = Codec.DECODE;
        String expResult = Dvorak_UPPER_keys;
        String result = Dvorcode.transcode(cypher, direction);
        assertEquals(expResult, result);
    }

    /**
     * Test of transcode method, of class Dvorcode decoding un-shifted key values
     */
    @Test
    public void testTransCode_String_DvorcodeCodec_decode_unshiftedKeys() {
        System.out.println("transcode - decoding (unshifted keys)");
        String cypher = QWERTY_lower_keys;
        Codec direction = DECODE;
        String expResult = Dvorak_lower_keys;
        String result = Dvorcode.transcode(cypher, direction);
        assertEquals(expResult, result);
    }

    /**
     * Test of transcode method, of class Dvorcode endocding shifted key caps
     */
    @Test
    public void testTransCode_String_DvorcodeCodec_encode_shiftedKeys() {
        System.out.println("transcode - encoding (shifted keys)");
        String cypher = Dvorak_UPPER_keys;
        Codec direction = ENCODE;
        String expResult = QWERTY_UPPER_keys;
        String result = Dvorcode.transcode(cypher, direction);
        assertEquals(expResult, result);
    }

    /**
     * Test of transcode method, of class Dvorcode encoding unshifted key caps
     */
    @Test
    public void testTransCode_String_DvorcodeCodec_encode_unshiftedKeys() {
        System.out.println("transcode - encoding (unshifted keys)");
        String cypher = Dvorak_lower_keys;
        Codec direction = ENCODE;
        String expResult = QWERTY_lower_keys;
        String result = Dvorcode.transcode(cypher, direction);
        assertEquals(expResult, result);
    }

    /**
     * Test of transcode method, of class Dvorcode decoding un-mapped characters
     */
    @Test
    public void testTransCode_String_DvorcodeCodec_decode_unmappedChars() {
        System.out.println("transcode - decoding unmapped chars");
        String cypher = unmapped_chars;
        Codec direction = DECODE;
        String expResult = cypher;
        String result = Dvorcode.transcode(cypher, direction);
        assertEquals(expResult, result);
    }

    /**
     * Test of transcode method, of class Dvorcode encoding un-mapped characters
     */
    @Test
    public void testTransCode_String_DvorcodeCodec_encode_unmappedChars() {
        System.out.println("transcode - encoding unmapped chars");
        String cypher = unmapped_chars;
        Codec direction = Codec.ENCODE;
        String expResult = cypher;
        String result = Dvorcode.transcode(cypher, direction);
        assertEquals(expResult, result);
    }

    /**
     * Test of decode method, of class Dvorcode.
     * 
     * Note: not repeating test cases tested in transcode(String,Dvorcode.Codec)
     */
    @Test
    public void testDecode() {
        System.out.println("decode");
        String cypher = simple_asdf;
        String expResult = simple_aoeu;
        String result = Dvorcode.decode(cypher);
        assertEquals(expResult, result);
    }

    /**
     * Test of encode method, of class Dvorcode.
     * 
     * Note: not repeating test cases tested in transcode(String,Dvorcode.Codec)
     */
    @Test
    public void testEncode() {
        System.out.println("encode");
        String message = simple_aoeu;
        String expResult = simple_asdf;
        String result = Dvorcode.encode(message);
        assertEquals(expResult, result);
    }

    /**
     * Test of transcode method, of class Dvorcode.
     * 
     * Note: not repeating test cases tested in transcode(String,Dvorcode.Codec)
     */
    @Test
    public void testTranscode_String() {
        System.out.println("transcode");
        String cypher = simple_asdf;
        String expResult = simple_aoeu;
        String result = Dvorcode.transcode(cypher);
        assertEquals(expResult, result);
    }
}
