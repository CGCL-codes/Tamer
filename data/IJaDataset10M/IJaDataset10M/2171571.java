package nl.ow.dilemma.exist.module.mail;

import org.exist.xquery.AbstractInternalModule;
import org.exist.xquery.FunctionDef;

/**
 * eXist Mail Module Extension
 *
 * An extension module for the eXist Native XML Database that allows email to
 * be sent from XQuery using the JavaMail API.
 *
 * @author Dannes Wessels
 * @author Adam Retter (original code)
 *
 * @see org.exist.xquery.AbstractInternalModule#AbstractInternalModule(org.exist.xquery.FunctionDef[])
 */
public class MailModule extends AbstractInternalModule {

    public static final String NAMESPACE_URI = "http://dilemma.ow.nl/exist/mail";

    public static final String PREFIX = "mail";

    private static final FunctionDef[] functions = { new FunctionDef(SendEmailFunction.signature, SendEmailFunction.class) };

    public MailModule() {
        super(functions);
    }

    public String getNamespaceURI() {
        return NAMESPACE_URI;
    }

    public String getDefaultPrefix() {
        return PREFIX;
    }

    public String getDescription() {
        return "A module for sending e-mail.";
    }
}
