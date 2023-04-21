package command;

public class Variable {

    /** used to refer to the variable */
    private String name;

    /** used in the frontend to label the variable's input field. */
    private String label;

    /** contents */
    private String value;

    public Variable(String name, String label) {
        this.name = name;
        this.label = label;
        this.value = "";
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String get() {
        return value;
    }

    public void set(String obj) {
        value = obj;
    }
}
