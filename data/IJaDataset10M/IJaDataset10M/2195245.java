package org.parallelj.internal.kernel.procedure;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.parallelj.internal.kernel.KCall;
import org.parallelj.internal.kernel.callback.Entry;
import org.parallelj.internal.kernel.callback.Exit;
import org.parallelj.internal.kernel.procedure.SubProcessProcedure;

public class SubProgramTest extends ProcedureTest<SubProcessProcedure> {

    static Logger logger = Logger.getRootLogger();

    RunnableTest test = new RunnableTest();

    @Before
    public void setup() {
        this.test.setup();
        super.setup();
    }

    @Override
    public void setupProcedure() {
        procedure = new SubProcessProcedure(program);
        procedure.setSubProgram(test.program);
        procedure.setEntry(new Entry() {

            @Override
            public void enter(KCall execution) {
                logger.info("main enter");
            }
        });
        procedure.setExit(new Exit() {

            @Override
            public void exit(KCall execution) {
                logger.info("main exit");
            }
        });
    }
}
