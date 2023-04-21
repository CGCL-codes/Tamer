package ws.prova.test;

import ws.prova.Communicator;
import ws.prova.kernel.ProvaResultSet;
import ws.prova.parser.ParsingException;
import ws.prova.util.NullWriter;
import java.util.Iterator;
import java.util.List;
import org.mandarax.kernel.Result;

/**
 * Standalone test of consulting and querying a Prova rulebase from Java
 * <p>Title: Prova </p>
 * <p>Description: Standalone test of consulting and querying a Prova rulebase from Java</p>
 * <p>Copyright: Copyright (c) 2002-2005</p>
 * <p>Company: City University, London, United Kingdom</p>
 *
 * @author <A HREF="mailto:a.kozlenkov@city.ac.uk">Alex Kozlenkov</A>
 * @version 1.8.0 <15 April 2005>
 */
public class ProvaRunner {

    static final String kAgent = "prova";

    static final String kPort = null;

    Communicator comm;

    int key = 0;

    public ProvaRunner(String rulebase) {
        comm = new Communicator(kAgent, kPort, rulebase, 0, Communicator.SYNC);
    }

    /**
	 * Test ws.prova.reagent.consultSync by running an example of Karsten Kuhla.
	 * We run the goal twice and each run returns two results with one instantiated variable in each.
	 */
    public void testConsultSync() {
        System.out.println("testConsultSync >>>");
        comm.setPrintWriter(NullWriter.getPrintWriter());
        String input = ":- solve(happy(Person)).:- solve(Happy(Person)).";
        try {
            List resultSets = comm.consultSync(input, Integer.toString(key++), new Object[] {});
            for (Iterator rsit = resultSets.iterator(); rsit.hasNext(); ) {
                ProvaResultSet rs = (ProvaResultSet) rsit.next();
                if (rs.getException() != null) {
                    System.out.println("Test fails with " + rs.getException());
                    continue;
                } else if (rs.numberOfResults() == 0) {
                    continue;
                }
                for (Iterator itr = rs.iterator(); itr.hasNext(); ) {
                    Result r = (Result) itr.next();
                    Object o = r.getResult(Object.class, "Person");
                    System.out.println("result=" + o);
                }
            }
        } catch (Exception e) {
            System.out.println("Test fails with " + e);
        } finally {
        }
    }

    /**
	 * Test ws.prova.reagent.consultSync by running an example with Prova embedded in a string.
	 * This particular test demonstrates passing Java objects to Prova in such a way that, e.g.,
	 * Prova can call their methods.
	 * The method also demonstrates how Communicator.unconsultSync works (see below).
	 */
    public void testConsultSyncWithJavaObjects() {
        System.out.println("testConsultSyncWithJavaObjects >>>");
        comm.setPrintWriter(NullWriter.getPrintWriter());
        String input = "a():-_0.print(\"%%%% \"),_0.println(_1). b(_0):-_0.print(\"%%%% \"),_0.println(_2). :- eval(a()). :- solve(b(X)).";
        try {
            List resultSets = comm.consultSync(input, Integer.toString(key), new Object[] { System.out, "test a OK", "test b OK" });
            for (Iterator rsit = resultSets.iterator(); rsit.hasNext(); ) {
                ProvaResultSet rs = (ProvaResultSet) rsit.next();
                if (rs.getException() != null) {
                    System.out.println("Test fails with " + rs.getException());
                    continue;
                } else if (rs.numberOfResults() == 0) {
                    continue;
                }
                for (Iterator itr = rs.iterator(); itr.hasNext(); ) {
                    Result r = (Result) itr.next();
                    Object o = rs.getResult(Object.class, "X");
                    System.out.println("result=" + o);
                }
            }
            comm.unconsultSync(Integer.toString(key));
            resultSets = comm.consultSync(":- solve(b(X)).", Integer.toString(key), new Object[] {});
            for (Iterator rsit = resultSets.iterator(); rsit.hasNext(); ) {
                ProvaResultSet rs = (ProvaResultSet) rsit.next();
                if (rs.getException() != null) {
                    System.out.println("Test fails with " + rs.getException());
                    continue;
                } else if (rs.numberOfResults() == 0) {
                    continue;
                }
                for (Iterator itr = rs.iterator(); itr.hasNext(); ) {
                    Result r = (Result) itr.next();
                    Object o = rs.getResult(Object.class, "X");
                    System.out.println("result=" + o);
                }
            }
        } catch (Exception e) {
            System.out.println("Test fails with " + e);
        }
    }

    /**
	 * Test ws.prova.reagent.consultSync by running an additional example that contains a syntax error 'hidden' in
	 * a file included inside a consulted file.
	 */
    public void testConsultSync2() {
        System.out.println("testConsultSync2 >>>");
        comm.setPrintWriter(NullWriter.getPrintWriter());
        String input = ":- eval(consult(\"prova/prova-examples/kk_rules003.prova\")).:- solve(holds_at(X,T)).";
        try {
            List resultSets = comm.consultSync(input, Integer.toString(key++), new Object[] {});
            for (Iterator rsit = resultSets.iterator(); rsit.hasNext(); ) {
                ProvaResultSet rs = (ProvaResultSet) rsit.next();
                final Exception e = rs.getException();
                if (e != null) {
                    System.out.println("Test fails with " + e);
                    if (e instanceof ParsingException) System.out.println(((ParsingException) e).errorsToString());
                    continue;
                }
                for (Iterator itr = rs.iterator(); itr.hasNext(); ) {
                    Result r = (Result) itr.next();
                    Object o = rs.getResult(Object.class, "X");
                    System.out.println("result=" + o);
                }
            }
        } catch (Exception e) {
            System.out.println("Test fails with " + e);
            if (e instanceof ParsingException) System.out.println(((ParsingException) e).errorsToString());
        }
    }

    public static void main(String[] args) {
        ProvaRunner runner = new ProvaRunner("prova/prova-examples/kk_rules001.prova");
        runner.testConsultSyncWithJavaObjects();
        runner.testConsultSync();
        runner.testConsultSync2();
    }
}
