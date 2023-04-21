package name.fraser.neil.plaintext;

import java.util.LinkedList;
import java.util.List;
import name.fraser.neil.plaintext.Diff.Operation;
import junit.framework.TestCase;

public class PatchProducerTestCase extends TestCase {

    private Operation DELETE = Operation.DELETE;

    private Operation EQUAL = Operation.EQUAL;

    private Operation INSERT = Operation.INSERT;

    private PatchProducer patchProducer;

    @Override
    protected void setUp() throws Exception {
        patchProducer = new PatchProducer();
    }

    public void testPatchObj() {
        Patch p = new Patch();
        p.start1 = 20;
        p.start2 = 21;
        p.length1 = 18;
        p.length2 = 17;
        p.diffs = TestUtil.diffList(new Diff(EQUAL, "jump"), new Diff(DELETE, "s"), new Diff(INSERT, "ed"), new Diff(EQUAL, " over "), new Diff(DELETE, "the"), new Diff(INSERT, "a"), new Diff(EQUAL, "\nlaz"));
        String strp = "@@ -21,18 +22,17 @@\n jump\n-s\n+ed\n  over \n-the\n+a\n %0Alaz\n";
        assertEquals("Patch: toString.", strp, p.toString());
    }

    public void testPatchFromText() {
        assertTrue("patch_fromText: #0.", patchProducer.patch_fromText("").isEmpty());
        String strp = "@@ -21,18 +22,17 @@\n jump\n-s\n+ed\n  over \n-the\n+a\n %0Alaz\n";
        assertEquals("patch_fromText: #1.", strp, patchProducer.patch_fromText(strp).get(0).toString());
        assertEquals("patch_fromText: #2.", "@@ -1 +1 @@\n-a\n+b\n", patchProducer.patch_fromText("@@ -1 +1 @@\n-a\n+b\n").get(0).toString());
        assertEquals("patch_fromText: #3.", "@@ -1,3 +0,0 @@\n-abc\n", patchProducer.patch_fromText("@@ -1,3 +0,0 @@\n-abc\n").get(0).toString());
        assertEquals("patch_fromText: #4.", "@@ -0,0 +1,3 @@\n+abc\n", patchProducer.patch_fromText("@@ -0,0 +1,3 @@\n+abc\n").get(0).toString());
        try {
            patchProducer.patch_fromText("Bad\nPatch\n");
            fail("patch_fromText: #5.");
        } catch (IllegalArgumentException ex) {
        }
    }

    public void testPatchToText() {
        String strp = "@@ -21,18 +22,17 @@\n jump\n-s\n+ed\n  over \n-the\n+a\n  laz\n";
        List<Patch> patches;
        patches = patchProducer.patch_fromText(strp);
        assertEquals("patch_toText: Single.", strp, patchProducer.patch_toText(patches));
        strp = "@@ -1,9 +1,9 @@\n-f\n+F\n oo+fooba\n@@ -7,9 +7,9 @@\n obar\n-,\n+.\n  tes\n";
        patches = patchProducer.patch_fromText(strp);
        assertEquals("patch_toText: Dual.", strp, patchProducer.patch_toText(patches));
    }

    public void testPatchAddContext() {
        patchProducer.Patch_Margin = 4;
        Patch p;
        p = patchProducer.patch_fromText("@@ -21,4 +21,10 @@\n-jump\n+somersault\n").get(0);
        patchProducer.patch_addContext(p, "The quick brown fox jumps over the lazy dog.");
        assertEquals("patch_addContext: Simple case.", "@@ -17,12 +17,18 @@\n fox \n-jump\n+somersault\n s ov\n", p.toString());
        p = patchProducer.patch_fromText("@@ -21,4 +21,10 @@\n-jump\n+somersault\n").get(0);
        patchProducer.patch_addContext(p, "The quick brown fox jumps.");
        assertEquals("patch_addContext: Not enough trailing context.", "@@ -17,10 +17,16 @@\n fox \n-jump\n+somersault\n s.\n", p.toString());
        p = patchProducer.patch_fromText("@@ -3 +3,2 @@\n-e\n+at\n").get(0);
        patchProducer.patch_addContext(p, "The quick brown fox jumps.");
        assertEquals("patch_addContext: Not enough leading context.", "@@ -1,7 +1,8 @@\n Th\n-e\n+at\n  qui\n", p.toString());
        p = patchProducer.patch_fromText("@@ -3 +3,2 @@\n-e\n+at\n").get(0);
        patchProducer.patch_addContext(p, "The quick brown fox jumps.  The quick brown fox crashes.");
        assertEquals("patch_addContext: Ambiguity.", "@@ -1,27 +1,28 @@\n Th\n-e\n+at\n  quick brown fox jumps. \n", p.toString());
    }

    @SuppressWarnings("deprecation")
    public void testPatchMake() {
        LinkedList<Patch> patches;
        patches = patchProducer.patch_make("", "");
        assertEquals("patch_make: Null case.", "", patchProducer.patch_toText(patches));
        String text1 = "The quick brown fox jumps over the lazy dog.";
        String text2 = "That quick brown fox jumped over a lazy dog.";
        String expectedPatch = "@@ -1,8 +1,7 @@\n Th\n-at\n+e\n  qui\n@@ -21,17 +21,18 @@\n jump\n-ed\n+s\n  over \n-a\n+the\n  laz\n";
        patches = patchProducer.patch_make(text2, text1);
        assertEquals("patch_make: Text2+Text1 inputs.", expectedPatch, patchProducer.patch_toText(patches));
        expectedPatch = "@@ -1,11 +1,12 @@\n Th\n-e\n+at\n  quick b\n@@ -22,18 +22,17 @@\n jump\n-s\n+ed\n  over \n-the\n+a\n  laz\n";
        patches = patchProducer.patch_make(text1, text2);
        assertEquals("patch_make: Text1+Text2 inputs.", expectedPatch, patchProducer.patch_toText(patches));
        LinkedList<Diff> diffs = patchProducer.diffProducer.produceTextDiffList(text1, text2, false);
        patches = patchProducer.patch_make(diffs);
        assertEquals("patch_make: Diff input.", expectedPatch, patchProducer.patch_toText(patches));
        patches = patchProducer.patch_make(text1, diffs);
        assertEquals("patch_make: Text1+Diff inputs.", expectedPatch, patchProducer.patch_toText(patches));
        patches = patchProducer.patch_make(text1, text2, diffs);
        assertEquals("patch_make: Text1+Text2+Diff inputs (deprecated).", expectedPatch, patchProducer.patch_toText(patches));
        patches = patchProducer.patch_make("`1234567890-=[]\\;',./", "~!@#$%^&*()_+{}|:\"<>?");
        assertEquals("patch_toText: Character encoding.", "@@ -1,21 +1,21 @@\n-%601234567890-=%5B%5D%5C;',./\n+~!@#$%25%5E&*()_+%7B%7D%7C:%22%3C%3E?\n", patchProducer.patch_toText(patches));
        diffs = TestUtil.diffList(new Diff(DELETE, "`1234567890-=[]\\;',./"), new Diff(INSERT, "~!@#$%^&*()_+{}|:\"<>?"));
        assertEquals("patch_fromText: Character decoding.", diffs, patchProducer.patch_fromText("@@ -1,21 +1,21 @@\n-%601234567890-=%5B%5D%5C;',./\n+~!@#$%25%5E&*()_+%7B%7D%7C:%22%3C%3E?\n").get(0).diffs);
        text1 = "";
        for (int x = 0; x < 100; x++) {
            text1 += "abcdef";
        }
        text2 = text1 + "123";
        expectedPatch = "@@ -573,28 +573,31 @@\n cdefabcdefabcdefabcdefabcdef\n+123\n";
        patches = patchProducer.patch_make(text1, text2);
        assertEquals("patch_make: Long string with repeats.", expectedPatch, patchProducer.patch_toText(patches));
        try {
            patchProducer.patch_make(null);
            fail("patch_make: Null inputs.");
        } catch (IllegalArgumentException ex) {
        }
    }

    public void testPatchSplitMax() {
        LinkedList<Patch> patches;
        patches = patchProducer.patch_make("abcdefghijklmnopqrstuvwxyz01234567890", "XabXcdXefXghXijXklXmnXopXqrXstXuvXwxXyzX01X23X45X67X89X0");
        patchProducer.patch_splitMax(patches);
        assertEquals("patch_splitMax: #1.", "@@ -1,32 +1,46 @@\n+X\n ab\n+X\n cd\n+X\n ef\n+X\n gh\n+X\n ij\n+X\n kl\n+X\n mn\n+X\n op\n+X\n qr\n+X\n st\n+X\n uv\n+X\n wx\n+X\n yz\n+X\n 012345\n@@ -25,13 +39,18 @@\n zX01\n+X\n 23\n+X\n 45\n+X\n 67\n+X\n 89\n+X\n 0\n", patchProducer.patch_toText(patches));
        patches = patchProducer.patch_make("abcdef1234567890123456789012345678901234567890123456789012345678901234567890uvwxyz", "abcdefuvwxyz");
        String oldToText = patchProducer.patch_toText(patches);
        patchProducer.patch_splitMax(patches);
        assertEquals("patch_splitMax: #2.", oldToText, patchProducer.patch_toText(patches));
        patches = patchProducer.patch_make("1234567890123456789012345678901234567890123456789012345678901234567890", "abc");
        patchProducer.patch_splitMax(patches);
        assertEquals("patch_splitMax: #3.", "@@ -1,32 +1,4 @@\n-1234567890123456789012345678\n 9012\n@@ -29,32 +1,4 @@\n-9012345678901234567890123456\n 7890\n@@ -57,14 +1,3 @@\n-78901234567890\n+abc\n", patchProducer.patch_toText(patches));
        patches = patchProducer.patch_make("abcdefghij , h : 0 , t : 1 abcdefghij , h : 0 , t : 1 abcdefghij , h : 0 , t : 1", "abcdefghij , h : 1 , t : 1 abcdefghij , h : 1 , t : 1 abcdefghij , h : 0 , t : 1");
        patchProducer.patch_splitMax(patches);
        assertEquals("patch_splitMax: #4.", "@@ -2,32 +2,32 @@\n bcdefghij , h : \n-0\n+1\n  , t : 1 abcdef\n@@ -29,32 +29,32 @@\n bcdefghij , h : \n-0\n+1\n  , t : 1 abcdef\n", patchProducer.patch_toText(patches));
    }

    public void testPatchAddPadding() {
        LinkedList<Patch> patches;
        patches = patchProducer.patch_make("", "test");
        assertEquals("patch_addPadding: Both edges full.", "@@ -0,0 +1,4 @@\n+test\n", patchProducer.patch_toText(patches));
        patchProducer.patch_addPadding(patches);
        assertEquals("patch_addPadding: Both edges full.", "@@ -1,8 +1,12 @@\n %01%02%03%04\n+test\n %01%02%03%04\n", patchProducer.patch_toText(patches));
        patches = patchProducer.patch_make("XY", "XtestY");
        assertEquals("patch_addPadding: Both edges partial.", "@@ -1,2 +1,6 @@\n X\n+test\n Y\n", patchProducer.patch_toText(patches));
        patchProducer.patch_addPadding(patches);
        assertEquals("patch_addPadding: Both edges partial.", "@@ -2,8 +2,12 @@\n %02%03%04X\n+test\n Y%01%02%03\n", patchProducer.patch_toText(patches));
        patches = patchProducer.patch_make("XXXXYYYY", "XXXXtestYYYY");
        assertEquals("patch_addPadding: Both edges none.", "@@ -1,8 +1,12 @@\n XXXX\n+test\n YYYY\n", patchProducer.patch_toText(patches));
        patchProducer.patch_addPadding(patches);
        assertEquals("patch_addPadding: Both edges none.", "@@ -5,8 +5,12 @@\n XXXX\n+test\n YYYY\n", patchProducer.patch_toText(patches));
    }

    public void testPatchApply() {
        patchProducer.matchProducer.Match_Distance = 1000;
        patchProducer.matchProducer.Match_Threshold = 0.5f;
        patchProducer.Patch_DeleteThreshold = 0.5f;
        LinkedList<Patch> patches;
        patches = patchProducer.patch_make("", "");
        Object[] results = patchProducer.patch_apply(patches, "Hello world.");
        boolean[] boolArray = (boolean[]) results[1];
        String resultStr = results[0] + "\t" + boolArray.length;
        assertEquals("patch_apply: Null case.", "Hello world.\t0", resultStr);
        patches = patchProducer.patch_make("The quick brown fox jumps over the lazy dog.", "That quick brown fox jumped over a lazy dog.");
        results = patchProducer.patch_apply(patches, "The quick brown fox jumps over the lazy dog.");
        boolArray = (boolean[]) results[1];
        resultStr = results[0] + "\t" + boolArray[0] + "\t" + boolArray[1];
        assertEquals("patch_apply: Exact match.", "That quick brown fox jumped over a lazy dog.\ttrue\ttrue", resultStr);
        results = patchProducer.patch_apply(patches, "The quick red rabbit jumps over the tired tiger.");
        boolArray = (boolean[]) results[1];
        resultStr = results[0] + "\t" + boolArray[0] + "\t" + boolArray[1];
        assertEquals("patch_apply: Partial match.", "That quick red rabbit jumped over a tired tiger.\ttrue\ttrue", resultStr);
        results = patchProducer.patch_apply(patches, "I am the very model of a modern major general.");
        boolArray = (boolean[]) results[1];
        resultStr = results[0] + "\t" + boolArray[0] + "\t" + boolArray[1];
        assertEquals("patch_apply: Failed match.", "I am the very model of a modern major general.\tfalse\tfalse", resultStr);
        patches = patchProducer.patch_make("x1234567890123456789012345678901234567890123456789012345678901234567890y", "xabcy");
        results = patchProducer.patch_apply(patches, "x123456789012345678901234567890-----++++++++++-----123456789012345678901234567890y");
        boolArray = (boolean[]) results[1];
        resultStr = results[0] + "\t" + boolArray[0] + "\t" + boolArray[1];
        assertEquals("patch_apply: Big delete, small change.", "xabcy\ttrue\ttrue", resultStr);
        patches = patchProducer.patch_make("x1234567890123456789012345678901234567890123456789012345678901234567890y", "xabcy");
        results = patchProducer.patch_apply(patches, "x12345678901234567890---------------++++++++++---------------12345678901234567890y");
        boolArray = (boolean[]) results[1];
        resultStr = results[0] + "\t" + boolArray[0] + "\t" + boolArray[1];
        assertEquals("patch_apply: Big delete, big change 1.", "xabc12345678901234567890---------------++++++++++---------------12345678901234567890y\tfalse\ttrue", resultStr);
        patchProducer.Patch_DeleteThreshold = 0.6f;
        patches = patchProducer.patch_make("x1234567890123456789012345678901234567890123456789012345678901234567890y", "xabcy");
        results = patchProducer.patch_apply(patches, "x12345678901234567890---------------++++++++++---------------12345678901234567890y");
        boolArray = (boolean[]) results[1];
        resultStr = results[0] + "\t" + boolArray[0] + "\t" + boolArray[1];
        assertEquals("patch_apply: Big delete, big change 2.", "xabcy\ttrue\ttrue", resultStr);
        patchProducer.Patch_DeleteThreshold = 0.5f;
        patchProducer.matchProducer.Match_Threshold = 0.0f;
        patchProducer.matchProducer.Match_Distance = 0;
        patches = patchProducer.patch_make("abcdefghijklmnopqrstuvwxyz--------------------1234567890", "abcXXXXXXXXXXdefghijklmnopqrstuvwxyz--------------------1234567YYYYYYYYYY890");
        results = patchProducer.patch_apply(patches, "ABCDEFGHIJKLMNOPQRSTUVWXYZ--------------------1234567890");
        boolArray = (boolean[]) results[1];
        resultStr = results[0] + "\t" + boolArray[0] + "\t" + boolArray[1];
        assertEquals("patch_apply: Compensate for failed patch.", "ABCDEFGHIJKLMNOPQRSTUVWXYZ--------------------1234567YYYYYYYYYY890\tfalse\ttrue", resultStr);
        patchProducer.matchProducer.Match_Threshold = 0.5f;
        patchProducer.matchProducer.Match_Distance = 1000;
        patches = patchProducer.patch_make("", "test");
        String patchStr = patchProducer.patch_toText(patches);
        patchProducer.patch_apply(patches, "");
        assertEquals("patch_apply: No side effects.", patchStr, patchProducer.patch_toText(patches));
        patches = patchProducer.patch_make("The quick brown fox jumps over the lazy dog.", "Woof");
        patchStr = patchProducer.patch_toText(patches);
        patchProducer.patch_apply(patches, "The quick brown fox jumps over the lazy dog.");
        assertEquals("patch_apply: No side effects with major delete.", patchStr, patchProducer.patch_toText(patches));
        patches = patchProducer.patch_make("", "test");
        results = patchProducer.patch_apply(patches, "");
        boolArray = (boolean[]) results[1];
        resultStr = results[0] + "\t" + boolArray[0];
        assertEquals("patch_apply: Edge exact match.", "test\ttrue", resultStr);
        patches = patchProducer.patch_make("XY", "XtestY");
        results = patchProducer.patch_apply(patches, "XY");
        boolArray = (boolean[]) results[1];
        resultStr = results[0] + "\t" + boolArray[0];
        assertEquals("patch_apply: Near edge exact match.", "XtestY\ttrue", resultStr);
        patches = patchProducer.patch_make("y", "y123");
        results = patchProducer.patch_apply(patches, "x");
        boolArray = (boolean[]) results[1];
        resultStr = results[0] + "\t" + boolArray[0];
        assertEquals("patch_apply: Edge partial match.", "x123\ttrue", resultStr);
    }
}
