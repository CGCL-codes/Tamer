package com.jot.test.tryouts.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import com.jot.system.utils.Crypto;
import com.jot.system.utils.JotTime;
import com.jot.system.visitors.UserSessionSocket;

public class SessionLookupTree {

    public static void main(String[] args) {
        int count = 50000;
        List<String> sessions = new ArrayList<String>();
        Map<String, UserSessionSocket> map = new TreeMap<String, UserSessionSocket>();
        for (int i = 0; i < count; i++) {
            String guid = Crypto.createSecretKeyString();
            sessions.add(guid);
            UserSessionSocket uss = new UserSessionSocket();
            uss.socket = null;
            map.put(guid, uss);
        }
        int lookupcount = 0;
        long starttime = JotTime.get();
        Random rand = new Random();
        for (int p = 0; p < 10; p++) {
            for (int i = 0; i < 10000000; i++) {
                int index = rand.nextInt(count);
                String key = sessions.get(index);
                UserSessionSocket uss = map.get(key);
                if (uss == null) System.out.println("missed !!!!!!!");
                lookupcount++;
            }
            long endtime = JotTime.get();
            long elapsed = endtime - starttime;
            double rate = (double) lookupcount / elapsed;
            System.out.println("rate is " + rate * 1000 + " lookups per second");
            System.out.println("time is " + 1000 / (rate) + "microseconds per lookup");
        }
    }
}
