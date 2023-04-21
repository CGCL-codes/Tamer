package org.apache.rat.pd.heuristic.functions;

import org.apache.rat.pd.heuristic.HeuristicCheckerResult;
import junit.framework.TestCase;

public class CFunctionHeuristicCheckerTest extends TestCase {

    private CFunctionHeuristicChecker checker;

    private static final String SIMPLE_FUNCTION_1 = "void\n& name(){}";

    private static final String SIMPLE_FUNCTION_2 = "type name   (){}";

    private static final String SIMPLE_FUNCTION_3 = "void nameOfAFunction(pom\n, pom2 pom3)\n{\n something;  \n something2;\n}";

    private static final String SIMPLE_FUNCTION_4 = "void nameOfAFunction () {something {soemthing2}}";

    private static final String SIMPLE_FUNCTION_5 = "something\nvoid name() {}";

    private static final String SIMPLE_FUNCTION_5_SUGGESTED = "void name() {}";

    private static final String SIMPLE_FUNCTION_6 = "gchar *\n" + "gimp_plug_in_directory (void)\n" + " {\n" + "		\n" + "		\n" + " if (! gimp_plug_in_dir)\n" + " {\n" + "	g_free (tmp);\n" + " }\n" + "}";

    private static final String SIMPLE_MACRO_FUNCTION_1 = "#define SWAP(a, b)\n{a ^= b;b ^= a;a ^= b;}";

    private static final String SIMPLE_MACRO_FUNCTION_2 = "#define ADD_FIVE(a) ((a) + 5)\n";

    private static final String SIMPLE_MACRO_FUNCTION_3 = "#define max(a,b) \n" + "       {\n typeof (a) _a = (a); \\n" + "           typeof (b) _b = (b); \\n" + "         _a > _b ? _a : _b; }";

    private static final String SIMPLE_MACRO_FUNCTION_4 = "#define SWAP(a, b)\n \n  {                   \n" + "                        a ^= b;         \n" + "                        b ^= a;         \n" + "                        a ^= b;         \n" + "                     }";

    private static final String SIMPLE_INLINE_FUNCTION_1 = "inline\n void name(){}";

    private static final String SIMPLE_INLINE_FUNCTION_2 = "inline void hello()\n" + "{ \n" + "  cout<<\"hello\";\n" + "}";

    private static final String SIMPLE_INLINE_FUNCTION_3 = "inline\n  NTL_HDR_strChattr\n { NTL_SRC_strChattr }";

    private static final String SIMPLE_INLINE_FUNCTION_4 = "inline void operator<(nsChangeHint s1, nsChangeHint s2) {}";

    private static final String SIMPLE_INLINE_FUNCTION_5 = "inline nsChangeHint NS_CombineHint(nsChangeHint aH1, nsChangeHint aH2) {\n" + "  return (nsChangeHint)(aH1 | aH2);\n" + "}";

    private static final String INVALID_FUNCTION_2 = "type name   (){} something";

    private static final String INVALID_FUNCTION_3 = "void name(){   something{}";

    private static final String SIMPLE_MACRO_FUNCTION_5 = "something #define SWAP(a, b){a ^= b;b ^= a;a ^= b;}";

    private static final String SIMPLE_MACRO_FUNCTION_5_SUGGESTED = "#define SWAP(a, b){a ^= b;b ^= a;a ^= b;}";

    private static final String INVALID_MACRO_FUNCTION_2 = "#define SWAP(a, b){a ^= b;b ^= a;a ^= b;}something";

    private static final String INVALID_MACRO_FUNCTION_3 = "#define SWAP(a, b){a ^= b;b ^= a;{a ^= b;}";

    private static final String SIMPLE_INLINE_FUNCTION_6 = "something inline void name(){}";

    private static final String SIMPLE_INLINE_FUNCTION_6_SUGGESTED = "inline void name(){}";

    private static final String INVALID_INLINE_FUNCTION_2 = "inline void name () { {}";

    protected void setUp() throws Exception {
        checker = new CFunctionHeuristicChecker(0, System.out);
    }

    /**
     * Test method for
     * {@link org.apache.rat.pd.heuristic.functions.FunctionHeuristicChecker#checkByHeuristic(java.lang.String)}
     * .
     */
    public void testCheckByHeuristic() {
        assertTrue("This is a function", checker.checkByHeuristic(SIMPLE_FUNCTION_1).isCheckOnInternetNeaded());
        assertTrue("This is a function", checker.checkByHeuristic(SIMPLE_FUNCTION_2).isCheckOnInternetNeaded());
        assertTrue("This is a function", checker.checkByHeuristic(SIMPLE_FUNCTION_3).isCheckOnInternetNeaded());
        assertTrue("This is a function", checker.checkByHeuristic(SIMPLE_FUNCTION_4).isCheckOnInternetNeaded());
        HeuristicCheckerResult result = checker.checkByHeuristic(SIMPLE_FUNCTION_5);
        assertTrue("This string consist function", result.isCheckOnInternetNeaded());
        assertEquals("Function must look like this:", SIMPLE_FUNCTION_5_SUGGESTED, result.getCodeSuggestedToBeChecked());
        assertTrue("This is a function", checker.checkByHeuristic(SIMPLE_FUNCTION_6).isCheckOnInternetNeaded());
        assertFalse("False mast be returned because there is something after function", checker.checkByHeuristic(INVALID_FUNCTION_2).isCheckOnInternetNeaded());
        assertFalse("False mast be returned because the numbers of opened and closed brackets are not equal", checker.checkByHeuristic(INVALID_FUNCTION_3).isCheckOnInternetNeaded());
    }

    public void testCheckByHeuristicsMacro() {
        assertTrue("This is macro function", checker.checkByHeuristic(SIMPLE_MACRO_FUNCTION_1).isCheckOnInternetNeaded());
        assertTrue("This is macro function", checker.checkByHeuristic(SIMPLE_MACRO_FUNCTION_2).isCheckOnInternetNeaded());
        assertTrue("This is macro function", checker.checkByHeuristic(SIMPLE_MACRO_FUNCTION_3).isCheckOnInternetNeaded());
        assertTrue("This is macro function", checker.checkByHeuristic(SIMPLE_MACRO_FUNCTION_4).isCheckOnInternetNeaded());
        HeuristicCheckerResult result = checker.checkByHeuristic(SIMPLE_MACRO_FUNCTION_5);
        assertTrue("This string consist macro", result.isCheckOnInternetNeaded());
        assertEquals("Function must look like this:", SIMPLE_MACRO_FUNCTION_5_SUGGESTED, result.getCodeSuggestedToBeChecked());
        assertFalse("There must be false return because there is something after macro", checker.checkByHeuristic(INVALID_MACRO_FUNCTION_2).isCheckOnInternetNeaded());
        assertFalse("There must be false return because the numbers of open and closed brackets are not equal", checker.checkByHeuristic(INVALID_MACRO_FUNCTION_3).isCheckOnInternetNeaded());
    }

    public void testCheckByHeuristicsInline() {
        assertTrue("This is valid inline function-basic one", checker.checkByHeuristic(SIMPLE_INLINE_FUNCTION_1).isCheckOnInternetNeaded());
        assertTrue("This is valid inline function", checker.checkByHeuristic(SIMPLE_INLINE_FUNCTION_2).isCheckOnInternetNeaded());
        assertTrue("This is valid inline function", checker.checkByHeuristic(SIMPLE_INLINE_FUNCTION_3).isCheckOnInternetNeaded());
        assertTrue("This is valid inline function", checker.checkByHeuristic(SIMPLE_INLINE_FUNCTION_4).isCheckOnInternetNeaded());
        assertTrue("This is valid inline function", checker.checkByHeuristic(SIMPLE_INLINE_FUNCTION_5).isCheckOnInternetNeaded());
        HeuristicCheckerResult result = checker.checkByHeuristic(SIMPLE_INLINE_FUNCTION_6);
        assertTrue("This string consist inline funstion", result.isCheckOnInternetNeaded());
        assertEquals("Function must look like this:", SIMPLE_INLINE_FUNCTION_6_SUGGESTED, result.getCodeSuggestedToBeChecked());
        assertFalse("There must be false return because the numbers of open and closed brackets are not equal", checker.checkByHeuristic(INVALID_INLINE_FUNCTION_2).isCheckOnInternetNeaded());
    }
}
