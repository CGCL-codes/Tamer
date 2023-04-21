package br.com.unitri.blog.server;

import java.util.Random;
import br.com.unitri.blog.shared.CounterService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class CounterServiceImpl extends RemoteServiceServlet implements CounterService {

    static Integer value = 0;

    static final Random rand = new Random();

    static {
        new Thread() {

            public void run() {
                while (true) {
                    value += 1;
                    try {
                        Thread.sleep((rand.nextInt(10) + 1) * 500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    public Integer getCurrentValue() {
        return value;
    }
}
