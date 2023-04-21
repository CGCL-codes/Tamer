package dataflow.example;

import org.apache.log4j.Logger;

public class Worker1 {

    static Logger log = Logger.getLogger(Worker1.class);

    public Integer run(Integer portSetNumber) {
        log.info("Running Worker 1");
        return 1;
    }

    public Object getresult() {
        return "Worker1";
    }

    public Object getsend2file() {
        return "Worker1 - send2file";
    }
}
