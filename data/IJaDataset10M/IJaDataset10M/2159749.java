package twisun.study.spring;

public class UpperAction implements Action {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String string) {
        message = string;
    }

    public String execute(String str) {
        return (getMessage() + str).toUpperCase();
    }
}
