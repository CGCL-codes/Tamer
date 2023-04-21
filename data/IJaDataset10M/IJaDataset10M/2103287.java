package plugins.examples.helloworld;

public class HelloWorldModel {

    private String name = "noname";

    private String errorMessage = "";

    public String getHelloWorld() {
        return "Hello " + this.getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String message) {
        this.errorMessage = message;
    }
}
