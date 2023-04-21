package br.gov.component.demoiselle.mail;

public interface IJNDIMailLookup {

    public static final String DEFAULT_MAIL_NAME = "java:/Mail";

    public String getMailName();
}
