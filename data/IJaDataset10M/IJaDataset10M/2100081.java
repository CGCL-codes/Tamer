package csc485.pg4.backgammon.gameFramework;

public class BearoffException extends MoveException {

    public BearoffException() {
        super.message = "Invalid - You must follow the rules for bearing off.";
    }

    @Override
    public String toString() {
        return message;
    }
}
