package net.sourceforge.myvd.protocol.ldap.mina.ldap.codec.actions;

import net.sourceforge.myvd.protocol.ldap.mina.asn1.ber.IAsn1Container;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.ber.grammar.GrammarAction;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.codec.DecoderException;
import net.sourceforge.myvd.protocol.ldap.mina.ldap.codec.LdapMessageContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The action used to initialize the AttributeDesc list
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class InitAttributeDescListAction extends GrammarAction {

    /** The logger */
    private static final Logger log = LoggerFactory.getLogger(InitAttributeDescListAction.class);

    /** Speedup for logs */
    private static final boolean IS_DEBUG = log.isDebugEnabled();

    public InitAttributeDescListAction() {
        super("Initialize AttributeDesc list");
    }

    /**
     * The initialization action
     */
    public void action(IAsn1Container container) throws DecoderException {
        LdapMessageContainer ldapMessageContainer = (LdapMessageContainer) container;
        ldapMessageContainer.grammarEndAllowed(true);
        if (IS_DEBUG) {
            log.debug("Initialize AttributeDesc list");
        }
    }
}
