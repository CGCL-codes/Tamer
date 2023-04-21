package net.sourceforge.myvd.protocol.ldap.mina.ldap.codec.actions;

import net.sourceforge.myvd.protocol.ldap.mina.asn1.ber.IAsn1Container;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.ber.grammar.GrammarAction;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.ber.tlv.TLV;
import net.sourceforge.myvd.types.FilterNode;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.codec.DecoderException;
import net.sourceforge.myvd.protocol.ldap.mina.ldap.codec.LdapMessage;
import net.sourceforge.myvd.protocol.ldap.mina.ldap.codec.LdapMessageContainer;
import net.sourceforge.myvd.protocol.ldap.mina.ldap.codec.search.SearchRequest;
import net.sourceforge.myvd.protocol.ldap.mina.ldap.codec.search.SubstringFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The action used to store a final value into a substring filter
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class StoreFinalAction extends GrammarAction {

    /** The logger */
    private static final Logger log = LoggerFactory.getLogger(StoreFinalAction.class);

    /** Speedup for logs */
    private static final boolean IS_DEBUG = log.isDebugEnabled();

    public StoreFinalAction() {
        super("Store a final value");
    }

    /**
     * The initialization action
     */
    public void action(IAsn1Container container) throws DecoderException {
        LdapMessageContainer ldapMessageContainer = (LdapMessageContainer) container;
        LdapMessage ldapMessage = ldapMessageContainer.getLdapMessage();
        SearchRequest searchRequest = ldapMessage.getSearchRequest();
        TLV tlv = ldapMessageContainer.getCurrentTLV();
        FilterNode node = searchRequest.getTerminalFilter();
        if (tlv.getLength() == 0) {
            log.error("The substring final filter is empty");
            throw new DecoderException("The substring final filter is empty");
        }
        String finalValue = new String(tlv.getValue().getData());
        if (node.getValue().length() > 0 && node.getValue().charAt(node.getValue().length() - 1) == '*') {
            node.setValue(node.getValue() + finalValue);
        } else {
            node.setValue(node.getValue() + '*' + finalValue);
        }
        searchRequest.unstackFilters(container);
        if (IS_DEBUG) {
            log.debug("Stored a any substring : {}", finalValue);
        }
    }
}
