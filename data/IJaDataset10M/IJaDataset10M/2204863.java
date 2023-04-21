package basic;

import alice.cartago.*;
import alice.cartago.tools.*;

public class TestCafeteria {

    public static void main(String[] args) throws Exception {
        CartagoService.installStandaloneNode();
        new CoffeeMachineUser("user").start();
    }
}
