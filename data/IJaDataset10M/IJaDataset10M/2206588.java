package pattern.part5.chapter15.dynamicproxy;

import java.util.concurrent.TimeUnit;

/**
 * Date: 2009-11-18
 * Time: 0:20:05
 */
public class PassengerByTrain extends HappyPeople {

    @Override
    public void travel() {
        System.out.println("Travelling by Train...");
        try {
            TimeUnit.MILLISECONDS.sleep(400l);
        } catch (InterruptedException e) {
        }
    }
}
