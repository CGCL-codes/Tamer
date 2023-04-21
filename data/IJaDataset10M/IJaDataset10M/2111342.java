package org.neblipedia.test.integracion.mediawiki;

import java.io.File;
import java.util.LinkedList;
import org.junit.runners.Parameterized.Parameters;
import org.neblipedia.test.integracion.BaseTest;

public class ExpresionesTest extends BaseTest {

    private static BaseDb miTest;

    @Parameters
    public static LinkedList<String[]> datos() {
        miTest = new BaseDb(new File("tests/expresiones.test"));
        return miTest.getTests();
    }

    public ExpresionesTest(String titulo, String options, String dato, String resultado) {
        super(titulo, options, dato, resultado);
    }

    @Override
    protected BaseDb getDB() {
        return miTest;
    }
}
