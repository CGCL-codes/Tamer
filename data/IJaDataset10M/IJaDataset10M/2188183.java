package pattern.part5.chapter15.bytecode;

import java.util.concurrent.TimeUnit;

/**
 * Date: 2009-11-18
 * Time: 0:23:43
 */
public class PassengerByAir extends HappyPeople {

    @Override
    protected void travel() {
        System.out.println("Travelling by Air...");
        try {
            TimeUnit.MILLISECONDS.sleep(200l);
        } catch (InterruptedException e) {
        }
    }
}
