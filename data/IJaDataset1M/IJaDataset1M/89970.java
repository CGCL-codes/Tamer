package checkers.statetrack;

public interface Translator<From, To, ExceptionType extends Throwable> {

    To translate(From value) throws ExceptionType;
}
