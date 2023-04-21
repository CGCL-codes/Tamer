package capaEnlac2.excepciones2;

public class QuantityRegistersException extends Exception {

    public static final int EXCEPTION_CODE = 02;

    public QuantityRegistersException() {
    }

    public QuantityRegistersException(String mensaje) {
        super(mensaje);
    }
}
