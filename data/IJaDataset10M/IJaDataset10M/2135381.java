package edu.sdsc.rtdsm.drivers.turbine;

import com.rbnb.sapi.*;
import java.util.*;
import edu.sdsc.rtdsm.drivers.turbine.util.*;
import edu.sdsc.rtdsm.framework.sink.*;
import edu.sdsc.rtdsm.framework.util.*;
import edu.sdsc.rtdsm.framework.feedback.SinkFeedbackAgent;

/**
 * Class TurbineSink
 * 
 */
public class TurbineSink {

    Vector<TurbineSink> sink;

    TurbineSinkConfig sinkConfig;

    TurbineSinkFetchHandler handler;

    Vector<TurbineServer> serverVec = new Vector<TurbineServer>();

    Vector<TurbineRawSink> sinkVec = new Vector<TurbineRawSink>();

    Vector<Thread> sinkThreadVec = new Vector<Thread>();

    ThreadGroup threadGroup = new ThreadGroup("TurbineSinks");

    boolean connected;

    public TurbineSink(TurbineSinkConfig sinkConfig) {
        if (!(sinkConfig instanceof TurbineSinkConfig)) {
            throw new IllegalArgumentException("The config parameter has to be " + "an instance of TurbineSinkConfig");
        }
        this.sinkConfig = (TurbineSinkConfig) sinkConfig;
        serverVec = sinkConfig.getTurbineServerVec();
        for (int i = 0; i < serverVec.size(); i++) {
            TurbineServer server = serverVec.elementAt(i);
            Debugger.debug(Debugger.TRACE, "While creating raw sink: Callback listener=" + sinkConfig.getCallBackListener());
            TurbineRawSink sink = new TurbineRawSink(server, sinkConfig.getName(), sinkConfig.getCallBackListener());
            sinkVec.addElement(sink);
        }
    }

    public void connectAndWait() {
        for (int i = 0; i < sinkVec.size(); i++) {
            TurbineRawSink sink = sinkVec.elementAt(i);
            sink.connectNoHangup();
        }
        for (int i = 0; i < sinkVec.size(); i++) {
            TurbineSinkThread sThread = new TurbineSinkThread(sinkVec.elementAt(i));
            Thread sinkThread = new Thread(threadGroup, sThread, "RST:" + getName() + ":" + i);
            Debugger.debug(Debugger.TRACE, "Created Raw sink thread: " + sinkThread);
            sinkThreadVec.addElement(sinkThread);
        }
        for (int i = 0; i < sinkThreadVec.size(); i++) {
            sinkThreadVec.elementAt(i).start();
        }
        for (int i = 0; i < sinkThreadVec.size(); i++) {
            try {
                sinkThreadVec.elementAt(i).join();
            } catch (InterruptedException ie) {
                throw new IllegalStateException("The sink terminated abruptly (Sink number " + i);
            }
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public Object pullData(int channelId) {
        return null;
    }

    public String getName() {
        return sinkConfig.getName();
    }

    public void disconnect() {
        for (int i = 0; i < sinkVec.size(); i++) {
            sinkVec.elementAt(i).terminate();
        }
        for (int i = 0; i < sinkThreadVec.size(); i++) {
            Debugger.debug(Debugger.TRACE, "Interrupting thread " + sinkThreadVec.elementAt(i));
            sinkThreadVec.elementAt(i).interrupt();
        }
    }
}
