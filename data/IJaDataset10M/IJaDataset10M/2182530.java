package beans.html;

public interface ValidatedElement {

    boolean validate();

    String getValidationError();
}
