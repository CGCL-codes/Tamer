package org.databene.jdbacl.identity.xml;

import org.databene.jdbacl.identity.IdentityProvider;
import org.databene.webdecs.xml.ParseContext;
import org.databene.webdecs.xml.XMLElementParserFactory;

/**
 * {@link ParseContext} implementation for identity definition files.<br/><br/>
 * Created: 07.12.2011 15:45:13
 * @since 0.7.1
 * @author Volker Bergmann
 */
public class IdentityParseContext extends ParseContext<Object> {

    protected IdentityProvider identityProvider;

    public IdentityParseContext() {
        this(new IdentityProvider());
    }

    public IdentityParseContext(IdentityProvider identityProvider) {
        super(Object.class, new XMLElementParserFactory<Object>());
        this.identityProvider = identityProvider;
        createParsers();
    }

    private void createParsers() {
        addParser(new IdentityParser());
    }

    public IdentityProvider getIdentityProvider() {
        return identityProvider;
    }
}
