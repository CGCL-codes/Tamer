package basic;

import alice.cartago.*;
import alice.cartago.util.Trigger;

public class TestTrigger {

    public static void main(String[] args) throws Exception {
        CartagoService.installStandaloneNode();
        new TriggerUser("user").start();
        new TriggerNotifier("notifier-1").start();
        new TriggerNotifier("notifier-2").start();
        new TriggerNotifier("notifier-3").start();
    }
}
