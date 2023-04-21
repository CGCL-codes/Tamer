package uk.ekiwi.messaging.mq;

import java.io.*;
import com.ibm.mq.*;
import java.util.*;

public class QScript {

    private MqQueueManager qm = null;

    private String ms_fileName;

    public QScript(MqQueueManager qm) {
        this.qm = qm;
    }

    public void writeQueues() throws Exception {
        String defn = null;
        MqQueue q = null;
        ArrayList queues = null;
        FileWriter fileOut = new FileWriter(ms_fileName);
        BufferedWriter out = new BufferedWriter(fileOut);
        for (int i = 0; i < queues.size(); i++) {
            q = (MqQueue) queues.get(i);
            if (!q.getQueueName().startsWith("SYSTEM.")) {
                defn = q.getDefinition();
                out.write(defn);
                out.newLine();
                out.newLine();
            }
        }
        out.close();
    }
}
