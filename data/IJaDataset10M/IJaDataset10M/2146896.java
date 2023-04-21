package BotEnvironment.SearchBot;

public class NoGoalLocationException extends Exception {

    public NoGoalLocationException() {
        super();
    }

    public String getMessage() {
        return "The map must have a goal location.";
    }
}
