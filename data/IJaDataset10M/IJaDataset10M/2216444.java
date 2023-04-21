package com.jot.test.tryouts.performance;

import com.jot.system.entities.ImMessage;
import com.jot.system.pjson.Guid;
import com.jot.system.pjson.PjsonParseUtil;
import com.jot.system.pjson.PjsonWriteUtil;
import com.jot.system.utils.JotTime;

public class JsonDecodeRate {

    public static void main(String[] args) throws Exception {
        ImMessage msg = new ImMessage();
        msg.guid = Guid.createHash("JsonDecodeRate");
        String xml = PjsonWriteUtil.toPjsonString(msg);
        xml = PjsonWriteUtil.toPjsonString(msg);
        System.out.println("json is " + xml);
        int lookupcount = 0;
        long starttime = JotTime.get();
        for (int p = 0; p < 10; p++) {
            for (int i = 0; i < 100000; i++) {
                lookupcount++;
                ImMessage msg2 = (ImMessage) PjsonParseUtil.parse(xml);
                if (!msg2.guid.equals(msg.guid)) System.out.println("missed !!!!!!!!");
            }
            long endtime = JotTime.get();
            long elapsed = endtime - starttime;
            double rate = (double) lookupcount / elapsed;
            System.out.println("rate is " + rate * 1000 + " translations per second");
            System.out.println("time is " + 1000 / (rate) + "microseconds per translations");
        }
    }
}
