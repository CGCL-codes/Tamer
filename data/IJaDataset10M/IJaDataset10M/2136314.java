package service;

public class Emails {

    protected service.Email email;

    protected java.lang.Boolean valid;

    public Emails() {
    }

    public Emails(service.Email email, java.lang.Boolean valid) {
        this.email = email;
        this.valid = valid;
    }

    public service.Email getEmail() {
        return email;
    }

    public void setEmail(service.Email email) {
        this.email = email;
    }

    public java.lang.Boolean getValid() {
        return valid;
    }

    public void setValid(java.lang.Boolean valid) {
        this.valid = valid;
    }
}
