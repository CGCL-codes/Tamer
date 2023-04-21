package com.bagaturchess.ucitournament.rating;

public class RatingLog {

    public void log(String message) {
        System.out.println(message);
    }

    public void log(Throwable t) {
        t.printStackTrace(System.out);
    }
}
