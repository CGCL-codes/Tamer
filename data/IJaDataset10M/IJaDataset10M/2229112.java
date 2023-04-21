package net.sf.kfgodel.dgarcia.lang.reflection;

import java.util.regex.Matcher;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Esta clase solo controla los casos conocidos de nombres de variable (por si aparecen nuevos)
 * 
 * @author D. Garcia
 */
public class ChainProperyNameTest {

    @Test
    public void testProperyChains() {
        checkValidChain("enigma");
        checkValidChain("enigma.resuelto");
        checkValidChain("_$nombreMedio.raro_y_conCarac.a");
        checkValidChain("$$");
        checkValidChain("ASDASD_ASDAS");
        checkValidChain("getEnvoltorio");
        checkValidChain("a.b.c.d.e.f.g.h.y");
        checkValidChain("_._._._");
        checkValidChain("_1._2._3._4");
        checkInvalidChain("enigma.sin.resolver.todavía");
        checkInvalidChain("_.");
        checkInvalidChain("");
        checkInvalidChain("getEnvoltorio()");
        checkInvalidChain("_1.2._3._4");
    }

    /**
	 * Prueba si la cadean de propiedades pasadas es valida
	 */
    private void checkValidChain(String testChain) {
        checkExpression(testChain, true);
    }

    /**
	 * Chequea y muestra la expresion
	 */
    private void checkExpression(String testChain, boolean expected) {
        Matcher matcher = ReflectionUtils.propertyChainPattern.matcher(testChain);
        boolean found = false;
        while (matcher.find()) {
            System.out.format("Matched:\"%s\" %d-%d.%n", matcher.group(), matcher.start(), matcher.end());
            found = expected;
        }
        if (!found) {
            System.out.format("Sin match.%n");
        }
        String mensaje = (expected ? "debería dar OK" : "debería fallar");
        Assert.assertEquals("[" + testChain + "] " + mensaje, expected, matcher.matches());
    }

    /**
	 * Prueba si la cadean de propiedades pasadas es valida
	 */
    private void checkInvalidChain(String testChain) {
        checkExpression(testChain, false);
    }
}
