package edu.java.lectures.lec10.exceptions.example.cafe;

import edu.java.lectures.lec10.exceptions.example.cafe.exceptions.TooColdException;
import edu.java.lectures.lec10.exceptions.example.cafe.exceptions.TooHotException;

public class Waitress {

    private static final String LOG_PREFIX = "[Waitress]\t";

    public Waitress() {
    }

    public void serveCoffee(Client client, Coffee cupOfCoffee) {
        if (client == null) {
            throw new IllegalArgumentException("Invalid argument - client: null.");
        }
        if (cupOfCoffee == null) {
            throw new IllegalArgumentException("INvalid argument - cupOfCoffee: null.");
        }
        System.out.println(LOG_PREFIX + "Serving coffee with temperature " + cupOfCoffee.getTemperature());
        try {
            client.drinkCoffee(cupOfCoffee);
        } catch (TooHotException e) {
            System.out.println(LOG_PREFIX + "The client complains: " + e.getMessage());
            System.out.println(LOG_PREFIX + "Ask the client to blow the coffee or wait " + "little bit the coffee to get cold.");
        } catch (TooColdException e) {
            System.out.println(LOG_PREFIX + "The client complains: " + e.getMessage());
            System.out.println(LOG_PREFIX + "Put the coffee in the microwave.");
        }
    }
}
