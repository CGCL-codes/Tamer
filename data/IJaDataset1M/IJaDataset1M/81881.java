package BusinessLogic.Exceptions;

public class DataAlreadyExistException extends Exception {

    public DataAlreadyExistException(String moviename) {
        super("Movie " + moviename + " already exist");
    }
}
