package org.javenue.util.process;

import org.javenue.util.process.Process;
import org.javenue.util.process.Transition;
import org.javenue.util.process.TransitionException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static java.lang.System.out;

/**
 * Prequisites:
 * <p>
 * This test class assumes that the database is up and running, that hibernate can connect to it
 * and that a table named "person_process_state" exists. A file has been provided in the "ddl" directory for creating
 * this table in a MySQL database.
 * <p>
 * This test class persists instances of PersonProcessState to the database.
 * 
 * @author Benjamin Possolo
 * <p>Created on Sep 1, 2008
 */
public class PersonProcessTest extends AbstractDerbyBackedTest {

    private PersonProcess myProc;

    @Before
    public void setUp() throws Exception {
        myProc = new PersonProcess();
        myProc.setId(new Integer(1));
        myProc.setTransitionManager(transitionManager);
    }

    @Test
    public void testProcessWithSuccessfulTransition() {
        myProc.setTransitionToTest(1);
        myProc.run();
    }

    @Test
    public void testProcessWithFailedTransition() {
        myProc.setTransitionToTest(2);
        myProc.run();
    }

    private class PersonProcess extends Process<PersonProcessState> {

        private int tries = 0;

        private int transitionToTest;

        public void setTransitionToTest(int transitionToTest) {
            this.transitionToTest = transitionToTest;
        }

        @Override
        public String getTerminationCodeAsString(int terminationCode) {
            return null;
        }

        @Override
        public void stop() {
            abortIdempotentTransition();
        }

        public void run() {
            switch(transitionToTest) {
                case 1:
                    out.println("========transition 1========");
                    performTransition(new Transition() {

                        public void doTransition() {
                            tries++;
                            out.println("In doTransition 1, try number " + tries);
                            getProcessState().setFirstname("patrick");
                            getProcessState().setLastname("bateman");
                            setState(PersonProcessState.NAME_OK_STATE);
                        }
                    });
                    assertEquals(1, tries);
                    setProcessState(null);
                    performTransition(new Transition() {

                        public void doTransition() {
                            assertEquals("patrick", getProcessState().getFirstname());
                            assertEquals("bateman", getProcessState().getLastname());
                            assertEquals(PersonProcessState.NAME_OK_STATE, getState());
                        }
                    });
                    break;
                case 2:
                    out.println("========transition 2========");
                    try {
                        performTransition(new Transition() {

                            public void doTransition() {
                                tries++;
                                getProcessState().setFirstname("sean");
                                throw new IllegalArgumentException("empty message");
                            }
                        });
                        fail();
                    } catch (TransitionException e) {
                        assertTrue(e.getCause() instanceof IllegalArgumentException);
                    }
                    assertEquals(1, tries);
                    assertEquals("sean", getProcessState().getFirstname());
                    performTransition(new Transition() {

                        public void doTransition() {
                            assertEquals("patrick", getProcessState().getFirstname());
                        }
                    });
                    break;
            }
        }
    }
}
