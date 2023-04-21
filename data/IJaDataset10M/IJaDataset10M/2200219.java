package jEcoSim.Server.Accountancy;

public class CalcException extends Exception {

    public CalcException() {
    }

    public CalcException(String message) {
        super(message);
    }

    public CalcException(Throwable cause) {
        super(cause);
    }

    public CalcException(String message, Throwable cause) {
        super(message, cause);
    }
}
