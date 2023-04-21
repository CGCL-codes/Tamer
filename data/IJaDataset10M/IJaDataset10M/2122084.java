package org.in4ama.documentautomator.documents.email;

import org.in4ama.documentautomator.documents.Document;

/** Represents an email in HTML and plain text form */
public class EmailDocument extends Document {

    public static final String TYPE = "email";

    public static final String DISP_TYPE = "TYPE_EMAIL";

    public static final String HTML_FILE_EXT = ".xhtml";

    public static final String TEXT_FILE_EXT = ".txt";

    private String htmlContent;

    private String textContent;

    /** Creates a new instance of EmailDocument. */
    public EmailDocument(String name) {
        this(name, null, null);
    }

    /** Creates a new instance of EmailDocument. */
    public EmailDocument(String name, String htmlContent, String textContent) {
        this.name = name;
        this.htmlContent = htmlContent;
        this.textContent = textContent;
    }

    /** Gets an input stream from where the html version of email
	 * can be read */
    public String getHtmlContent() {
        return htmlContent;
    }

    /** Gets an input stream from where the text version of email
	 * can be read */
    public String getTextContent() {
        return textContent;
    }

    /** Sets the plain text of this email */
    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    /** Sets the html content of this email */
    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    @Override
    public Document getEvalInstance() {
        EmailDocument evalInstance = new EmailDocument(name);
        evalInstance.textContent = getTextContent();
        evalInstance.htmlContent = getHtmlContent();
        return evalInstance;
    }

    /** Gets the type of this document */
    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean isEvaluated() {
        return false;
    }
}
