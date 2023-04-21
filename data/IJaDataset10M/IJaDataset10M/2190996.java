package com.ravi.taskman;

import java.util.ArrayList;
import java.util.Date;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.ScheduledPollConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TMConsumer extends ScheduledPollConsumer {

    private Logger log = LoggerFactory.getLogger(TMConsumer.class);

    private final TMEndpoint endpoint;

    ProducerTemplate pt;

    ConsumerTemplate ct;

    int cnt = 0;

    TaskRegister register = TaskRegister.createInstance();

    public TMConsumer(TMEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        ct = this.getEndpoint().getCamelContext().createConsumerTemplate();
        pt = this.getEndpoint().getCamelContext().createProducerTemplate();
    }

    @Override
    protected int poll() throws Exception {
        Exchange exchange = ct.receive("activemq:queue:taskstatus");
        TTTask task = exchange.getIn().getBody(TTTask.class);
        log.debug("task info:" + task.getUniqueId() + ";" + task.getExeResult() + ";" + task.getTaskClass());
        switch(task.getExeResult()) {
            case NEW:
            case RUNNING:
                register.taskRunning(task);
                break;
            case SUCCESS:
            case FAIL:
            case UNAVAILABLE:
                TaskProcessInfo info = register.taskFinished(task);
                if (info.isRunOnce()) {
                    register.removeTask(task);
                    task.exeDuration = new Date().getTime() - task.exeDuration;
                    pt.sendBody("activemq:queue:resultqueue", task);
                } else if (info.isAllDone()) {
                    ArrayList<TTTask> allResult = register.removeTask(task).getClientRunResult();
                    task.exeDuration = new Date().getTime() - allResult.get(0).exeDuration;
                    for (int i = 0; i < allResult.size() - 1; i++) {
                        TTTask tmp = allResult.get(i);
                        if (tmp.workerClient != null && tmp.workerClient.length() > 0) {
                            task.workerClient += "\n\t" + tmp.workerClient;
                            task.resultDesc += "\n\n\t" + tmp.resultDesc;
                        }
                    }
                    pt.sendBody("activemq:queue:resultqueue", task);
                }
                break;
            default:
                break;
        }
        log.debug("task pool size=" + register.size());
        return cnt++;
    }
}
