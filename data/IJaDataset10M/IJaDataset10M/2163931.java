package org.jsofa.process.engine;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsofa.process.Process;
import org.jsofa.process.*;
import org.jsofa.process.Activity.ACTIVITY_TYPE;
import org.jsofa.process.Activity.SPLT_TYPE;
import org.jsofa.task.*;

/**
 * handle the process message
 * @author Administrator
 */
public class MessageHandler implements Runnable {

    Message msg = null;

    public MessageHandler(Message message) {
        this.msg = message;
    }

    public void run() {
        if (msg.getMessageType() == Message.MSG_TYPE.RUN_PROCESS) {
            startProcess();
        } else if (msg.getMessageType() == Message.MSG_TYPE.RUN_ACTIVITY) {
            runActivity();
        } else {
        }
    }

    private void startProcess() {
        ProcessInstance processInstance = msg.getProcessInstance();
        Process process = processInstance.getProcess();
        Activity startActivity = process.getStartActivity();
        startActivity.doIt();
        List<Transition> trans = startActivity.getAfferentTransition();
        SPLT_TYPE splitType = startActivity.getSpltType();
        if (splitType == SPLT_TYPE.AND) {
            Activity target = null;
            for (int i = 0; i < trans.size(); i++) {
                target = trans.get(i).getToActivity();
                RunActivityMessage runActivityMessage = new RunActivityMessage(processInstance, processInstance.getActivityInstance(target));
                ProcessEngine.getProcessEngine().addMessage(runActivityMessage);
            }
        } else {
        }
    }

    private void runActivity() {
        RunActivityMessage runActivityMessage = (RunActivityMessage) msg;
        ActivityInstance activityInstance = runActivityMessage.getActivityInstance();
        activityInstance.addInCommingNum();
        if (!activityInstance.isReadyRun()) {
            return;
        }
        Activity activity = activityInstance.getActivity();
        ProcessInstance processInstance = msg.getProcessInstance();
        if (activity.getType() == ACTIVITY_TYPE.TIME) {
            try {
                TimeActivity ta = (TimeActivity) activity;
                long time = ta.getDelayTime();
                Thread.sleep(time);
            } catch (InterruptedException ex) {
                Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        } else if (activity.getType() == ACTIVITY_TYPE.TASK) {
            TaskActivity target = (TaskActivity) activity;
            target.getTask().run();
        } else {
            return;
        }
        List<Transition> trans = activity.getAfferentTransition();
        Activity target = null;
        for (int i = 0; i < trans.size(); i++) {
            target = trans.get(i).getToActivity();
            RunActivityMessage newRunActivityMessage = new RunActivityMessage(processInstance, processInstance.getActivityInstance(target));
            ProcessEngine.getProcessEngine().addMessage(runActivityMessage);
        }
    }
}
