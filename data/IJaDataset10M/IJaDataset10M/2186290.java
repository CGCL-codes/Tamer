package alice.tuprolog.test.theory;

import alice.tuprolog.*;
import junit.framework.*;

public class TestAdvUse extends TestCase {

    Prolog engine;

    Theory th1, th2, th3;

    /**
 	 *Impostazione suite dei test
 	 */
    public static Test suite() {
        return new TestSuite(TestAdvUse.class);
    }

    /**
 	 *Metodo di setUp richiamato ad ogni test per avere ambiente pulito
 	 */
    public void setUp() {
        engine = new Prolog();
        try {
            th1 = new Theory("fatt(0,1).\n" + "fatt(N,Y) :- N>0, N1 is N-1, fatt(N1,Y1), Y is N*Y1.", "Koremo");
            th2 = new Theory(new java.io.FileInputStream("fatty.pl"), "Are");
            th3 = new Theory("uni(X):- X is 2.", "Koremo");
            engine.addTheory(th1);
            engine.addTheory(th2);
            engine.addTheory(th3);
        } catch (Exception e) {
        }
    }

    /**
 	 *Metodo per testare rimozione con teorie multiple omonime, toglie l'ultima.
 	 */
    public void testMultiRemove() {
        try {
            engine.removeTheory("Koremo");
            Theory received = engine.getTheory("Koremo");
            assertTrue("Rimozione in ambiente multiplo", received.equals(th1));
            SolveInfo info = engine.solve("uni(3).");
            assertTrue("Risultato insesistente", !info.isSuccess());
        } catch (Exception e) {
        }
    }

    /**
 	 *metodo per testare il funzionamento standard della th default
 	 */
    public void testMultiDefault() {
        try {
            Theory defTh = new Theory("double(X,Y):- Y is 2*X.");
            engine.addTheory(defTh);
            Theory received = engine.getTheory("default");
            assertTrue("Gestione default", received.getName().equals("default"));
            SolveInfo info = engine.solve("double(5,X).");
            assertTrue("Risoluzione secondo default", (info.isSuccess()));
            System.out.println("Soluzione ottenuta: " + info.getSolution().toString());
        } catch (Exception e) {
        }
    }

    /**
 	 *Main per invocare l'esecuzione di JUNIT
 	 */
    public static void main(String args[]) {
        junit.swingui.TestRunner.run(TestAdvUse.class);
    }
}
