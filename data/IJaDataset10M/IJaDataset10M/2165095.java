package compiler;

import java.io.File;
import junit.framework.TestCase;

public class CompilerTest extends TestCase {

    private String sep = File.separator;

    public CompilerTest(String name) {
        super(name);
    }

    public void testGatewayGood() {
        final int MAX_GOOD = 19;
        String path = "testsuite" + sep + "examples" + sep + "good" + sep;
        String fillingZero = "";
        for (int i = 1; i < MAX_GOOD; i++) {
            if (("" + i).length() == 1) fillingZero = "0"; else fillingZero = "";
            System.out.print("good" + sep + "core0" + fillingZero + i + ".jl" + ": ");
            assertEquals(true, compiler.Compiler.gatewayGood(path + "core0" + fillingZero + i + ".jl"));
        }
    }

    public void testGatewayBad() {
        final int MAX_BAD = 27;
        String path = "testsuite" + sep + "examples" + sep + "bad" + sep;
        String fillingZero = "";
        for (int i = 1; i < MAX_BAD; i++) {
            if (("" + i).length() == 1) fillingZero = "0"; else fillingZero = "";
            System.out.print("bad" + sep + "bad0" + fillingZero + i + ".jl" + ": ");
            assertEquals(false, compiler.Compiler.gatewayBad(path + "bad0" + fillingZero + i + ".jl"));
        }
    }

    public void testGatewayCustom() {
        String path = "tests" + sep + "extratests" + sep;
        assertEquals(true, compiler.Compiler.gatewayBad(path + "should_pass1.jl"));
        assertEquals(false, compiler.Compiler.gatewayBad(path + "should_fail1.jl"));
        assertEquals(false, compiler.Compiler.gatewayBad(path + "should_fail2.jl"));
        assertEquals(false, compiler.Compiler.gatewayBad(path + "should_fail3.jl"));
        assertEquals(false, compiler.Compiler.gatewayBad(path + "should_fail4.jl"));
    }
}
