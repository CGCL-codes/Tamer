package logger.sd.server.service;

import logger.sd.protocol.LRP;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * The class <code>EchoServiceTest</code> contains tests for the class
 * <code>{@link EchoService}</code>.
 * 
 * @generatedBy CodePro at 12/10/10 10:41
 * @author Wendell
 * @version $Revision: 1.0 $
 */
public class EchoServiceTest {

    /**
	 * Run the EchoService(Socket) constructor test.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testEchoService() throws Exception {
        Service result = new EchoService("Teste");
        assertNotNull(result);
    }

    /**
	 * Run the void trataRequisicao() method test.
	 * 
	 * @throws Exception
	 */
    @Test
    public synchronized void testTrataRequisicao() throws Exception {
        String sentenca = "Echo teste";
        Service fixture = new EchoService(sentenca);
        String result = fixture.trataRequisicao();
        assertEquals(sentenca.toUpperCase(), result);
    }

    /**
	 * Run the String getCode() method test.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testGetCode() throws Exception {
        String sentenca = "Echo teste";
        Service fixture = new EchoService(sentenca);
        String result = fixture.getCode();
        assertEquals(LRP.ECHO.getMessage(), result);
    }

    /**
	 * Launch the test.
	 * 
	 * @param args
	 *            the command line arguments
	 */
    public static void main(String[] args) {
        new org.junit.runner.JUnitCore().run(EchoServiceTest.class);
    }
}
