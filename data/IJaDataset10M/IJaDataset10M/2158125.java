package ch.skyguide.tools.requirement.io;

@SuppressWarnings("serial")
public class RequirementDocumentFormatException extends Exception {

    public RequirementDocumentFormatException(String _string) {
        super(_string);
    }

    public RequirementDocumentFormatException(String _string, Exception _e) {
        super(_string, _e);
    }

    public RequirementDocumentFormatException(Exception _e) {
        super(_e);
    }
}
