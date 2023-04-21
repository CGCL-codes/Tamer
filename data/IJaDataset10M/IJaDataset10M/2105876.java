package capaEnlac2.excepciones2;

public class FunctionCodeException extends Exception {

    public static final int EXCEPTION_CODE = 01;

    public FunctionCodeException() {
    }

    public FunctionCodeException(String mensaje) {
        super(mensaje);
    }
}
