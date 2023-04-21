package foo;

import org.apache.log4j.Logger;
import org.parallelj.AndJoin;
import org.parallelj.AndSplit;
import org.parallelj.Program;

@Program
public class SubProgramProgram {

    static Logger logger = Logger.getRootLogger();

    @AndJoin("begin")
    public CallableProgram a() {
        CallableProgram callable = new CallableProgram();
        logger.info("enter:" + callable);
        return callable;
    }

    @AndSplit("end")
    public void a(CallableProgram callable) {
        logger.info("exit:" + callable);
    }
}
