package fitlibrary.suite;

import java.util.concurrent.BlockingQueue;
import fitlibrary.flow.actor.DoFlowActor;
import fitlibrary.runResults.TestResults;
import fitlibrary.suite.Reporter.ReportAction;

public class BatchFitLibrarySingleStep extends BatchFitLibrary {

    public DoFlowActor actor(BlockingQueue<ReportAction> reportQueue, TestResults testResults) {
        DoFlowActor actor = new DoFlowActor(doFlow, reportQueue, testResults);
        new Thread(actor).start();
        return actor;
    }
}
