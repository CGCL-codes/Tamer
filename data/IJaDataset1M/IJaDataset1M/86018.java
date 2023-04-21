package exception;

@SuppressWarnings("serial")
public class ExceptionSalaNaoExiste extends Exception {

    public ExceptionSalaNaoExiste() {
        super("Sala nao existe.");
    }
}
