package ParadigmExceptions;

public class DBCannotConnect extends Exception {

    DBCannotConnect(String reason) {
        super(reason);
    }
}
