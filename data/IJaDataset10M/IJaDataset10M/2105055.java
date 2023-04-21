package com.dukesoftware.utils.thread.control;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import junit.framework.TestCase;
import com.dukesoftware.utils.thread.control.Manager;

public class Tester extends TestCase {

    public void testExecution() throws InterruptedException {
        String[] keyArray = new String[10];
        for (int i = 0; i < keyArray.length; i++) {
            keyArray[i] = String.valueOf(i);
        }
        List<String> keys = Collections.unmodifiableList(Arrays.asList(keyArray));
        Manager<String, String> box = new Manager<String, String>();
        Thread t = box.startTaskOnOtherThread(keys, new TimeProvider(), 60000, new PrintFinalPhaseTask<String, String>());
        t.join();
    }
}
