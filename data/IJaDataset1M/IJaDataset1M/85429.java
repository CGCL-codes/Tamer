package objectdesign;

public class EchoReporter extends Reporter {

    @Override
    public String report(String message) {
        return message + message;
    }
}
