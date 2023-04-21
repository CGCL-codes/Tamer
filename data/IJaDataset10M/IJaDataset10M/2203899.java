package lu.fisch.structorizer.elements;

public class DetectedError {

    private String error = new String();

    private Element element = null;

    public DetectedError(String _error, Element _ele) {
        error = _error;
        element = _ele;
    }

    public String getError() {
        return error;
    }

    public Element getElement() {
        return element;
    }

    public String toString() {
        if (element != null) {
            error = error.replaceAll("«", "«");
            error = error.replaceAll("»", "»");
            return error;
        } else {
            return "No error?";
        }
    }

    public boolean equals(DetectedError _error) {
        return (element == _error.getElement()) && (error.equals(_error.getError()));
    }
}
