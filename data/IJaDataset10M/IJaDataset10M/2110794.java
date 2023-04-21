package alice.tucson.examples.basic;

import alice.tucson.api.*;
import alice.logictuple.*;

public class SendMsg {

    public static void main(String[] args) throws Exception {
        TupleCentreId tid = new TupleCentreId("box");
        Value dest = new Value("gastone");
        Value msg = new Value("ciao bello!");
        TucsonContextSynch cnt = new TucsonContextSynch(Tucson.getContext(new AgentId("pippo")));
        cnt.out(tid, new LogicTuple("msg", dest, msg));
        System.out.println("Message posted.");
    }
}
