package com.mysite.datastore;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import com.jot.httputils.HttpRequestUtil;
import com.jot.system.callbacks.HttpCompletionHandler;
import com.jot.system.pjson.Guid;
import com.jot.system.utils.EndTimer;
import com.jot.user.HttpHeaderInfo;

public class TestStore {

    Guid guid;

    byte[] data;

    public TestStore(int num) {
        guid = Guid.SHA1("" + num + "kRYVKu5K4Sd8jBgt2AmJqZKsSyu");
        data = ("hello from #" + num).getBytes();
    }

    public HttpCompletionHandler onPost = new HttpCompletionHandler() {

        @Override
        public void run(HttpHeaderInfo info) throws Exception {
        }
    };

    public HttpCompletionHandler onGet = new HttpCompletionHandler() {

        @Override
        public void run(HttpHeaderInfo info) throws Exception {
        }
    };

    static Random rand = new Random();

    static String[] texas = new String[] { "74.86.60.47", "74.86.20.201" };

    public static String getHost() {
        if (1 == 0) {
            return texas[rand.nextInt(texas.length)];
        } else if (1 == 0) {
            return "192.168.1.110";
        } else if (1 == 0) {
            return "192.168.1.55";
        } else return "localhost";
    }

    static int size = 1000;

    static int passes = 5;

    static int threadCount = 20;

    static String command = "datastore";

    static class Runner implements Runnable {

        int base = 0;

        public Runner(int base) {
            this.base = base;
        }

        @Override
        public void run() {
            for (int pass = 0; pass < passes; pass++) {
                for (int i = 0; i < size; i++) {
                    TestStore test = new TestStore(i + base);
                    HttpRequestUtil.post(null, getHost(), 8081, command + "/data.put?id=" + test.guid, test.data, test.onPost);
                    if (i % 20 == 0) System.out.print(".");
                }
                System.out.println();
                for (int i = 0; i < size; i++) {
                    TestStore test = new TestStore(i + base);
                    HttpRequestUtil.get(null, getHost(), 8081, command + "/data.get?id=" + test.guid, test.onGet);
                    if (i % 20 == 0) System.out.print(".");
                }
                System.out.println();
                for (int i = 0; i < size; i++) {
                    TestStore test = new TestStore(i + base);
                    test.data = (new String(test.data) + new Date()).getBytes();
                    HttpRequestUtil.post(null, getHost(), 8081, command + "/data.put?id=" + test.guid, test.data, test.onPost);
                    if (i % 20 == 0) System.out.print(".");
                }
                System.out.println();
                for (int i = 0; i < size; i++) {
                    TestStore test = new TestStore(i + base);
                    HttpRequestUtil.get(null, getHost(), 8081, command + "/data.get?id=" + test.guid, test.onGet);
                    if (i % 20 == 0) System.out.print(".");
                }
                System.out.println();
            }
            System.out.println("finished " + base);
        }
    }

    public static void main(String[] args) throws UnknownHostException, IOException, Exception {
        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < threadCount; i++) {
            Thread th = new Thread(new Runner(i * size));
            threads.add(th);
        }
        System.out.println("Server is " + getHost());
        EndTimer timer = new EndTimer();
        for (int i = 0; i < threadCount; i++) {
            threads.get(i).start();
        }
        boolean running = true;
        while (running) {
            timer.sleep(100);
            running = false;
            for (int i = 0; i < threadCount; i++) {
                if (threads.get(i).getState() != Thread.State.TERMINATED) running = true;
            }
        }
        int opCount = 4 * size * passes * threadCount;
        System.out.println("total time = " + timer.elapsed() + " for " + opCount + " operations = " + (1000 * opCount / timer.elapsed()) + "/sec");
    }
}
