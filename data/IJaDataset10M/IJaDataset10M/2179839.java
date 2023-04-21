package timelogger.exception;

public class ConfigurationParsingError extends Exception {

    private String property;

    private String element;

    public ConfigurationParsingError(String property, String element) {
        this.property = property;
        this.element = element;
    }

    @Override
    public String getLocalizedMessage() {
        return "error while parsing property: " + property + ", element " + element + " not allowed";
    }
}
