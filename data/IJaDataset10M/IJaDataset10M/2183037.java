package capaEnlac2.excepciones2;

public class AddressException extends Exception {

    public static final int EXCEPTION_CODE = 03;

    public AddressException() {
    }

    public AddressException(String mensaje) {
        super(mensaje);
    }
}
