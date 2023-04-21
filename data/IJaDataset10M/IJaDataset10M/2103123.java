package examples.di.impl;

import examples.di.Greeting;
import examples.di.GreetingClient;

public class GreetingClientImpl implements GreetingClient {

    private Greeting greeting;

    public void setGreeting(Greeting greeting) {
        this.greeting = greeting;
    }

    public void execute() {
        System.out.println(greeting.greet());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long start = System.currentTimeMillis();
        for (int i = 3; i < 2000000; i += 2) {
            int j = 1;
            double temp = Math.sqrt(i);
            for (j = 2; j < temp; j++) {
                if (i % j == 0) break;
            }
            if (j == (int) temp + 1) System.out.println(i);
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start) + " milliseconds");
    }
}
