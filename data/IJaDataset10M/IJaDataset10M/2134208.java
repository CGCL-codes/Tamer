package ces.coffice.webmail.mailmodel.mail;

public class MailManagerException extends Exception {

    private String errorMessage;

    public MailManagerException(String err) {
        this.errorMessage = err;
    }

    public String getMessage() {
        return errorMessage;
    }
}
