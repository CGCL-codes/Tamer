package edu.java.lectures.lec10.exceptions.example.cafe.exceptions;

public class TooColdException extends TemperatureException {

    public TooColdException(double temperature) {
        super(temperature);
    }

    public TooColdException(String message, double temperature) {
        super(message, temperature);
    }

    public TooColdException(String message, Throwable throwable, double temperature) {
        super(message, throwable, temperature);
    }
}
