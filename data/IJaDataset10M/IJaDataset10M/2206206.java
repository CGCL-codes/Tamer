package splitjoindup;

import java.io.InputStream;
import java.util.HashMap;
import com.ibm.realtime.flexotask.FlexotaskGraph;
import com.ibm.realtime.flexotask.scheduling.FlexotaskGlobalSchedulingElement;
import com.ibm.realtime.flexotask.scheduling.FlexotaskSchedulingData;
import com.ibm.realtime.flexotask.template.FlexotaskTemplate;
import com.ibm.realtime.flexotask.template.FlexotaskValidationException;
import com.ibm.realtime.flexotask.tools.FlexotaskXMLParser;

public class SplitJoinDupUniMain {

    private static final int HEAP_SIZE = 8 * 1024 * 1024;

    private static final int NUM_CORES = 1;

    public static void main(String[] argv) throws Exception {
        InputStream in = SplitJoinDupUniMain.class.getResourceAsStream("flexotaskGraphSJ.ftg");
        FlexotaskTemplate spec = FlexotaskXMLParser.parseStream(in);
        FlexotaskGlobalSchedulingElement globalData = new FlexotaskGlobalSchedulingElement(HEAP_SIZE, HEAP_SIZE, NUM_CORES);
        FlexotaskSchedulingData systemData = new FlexotaskSchedulingData(spec, HEAP_SIZE / 10, globalData);
        try {
            FlexotaskGraph graph = spec.validate("StreamScheduler", systemData, new HashMap());
            graph.getRunner().start();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Thread.sleep(10000 * 10000);
    }
}
