package pub.db.command;

public class IllegalParameterValueException extends Exception {

    private String parameter;

    private String value;

    public IllegalParameterValueException(String parameter, String value) {
        this.parameter = parameter;
        this.value = value;
    }

    public String toString() {
        return "Illegal parameter '" + parameter + "' with value '" + value + "'.";
    }
}
