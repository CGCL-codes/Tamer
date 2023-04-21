package net.sourceforge.myvd.protocol.ldap.mina.ldap.codec.search.controls;

import javax.naming.InvalidNameException;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.ber.IAsn1Container;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.ber.grammar.AbstractGrammar;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.ber.grammar.GrammarAction;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.ber.grammar.GrammarTransition;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.ber.grammar.IGrammar;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.ber.tlv.UniversalTag;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.ber.tlv.Value;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.codec.DecoderException;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.util.IntegerDecoder;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.util.IntegerDecoderException;
import net.sourceforge.myvd.protocol.ldap.mina.ldap.name.LdapDN;
import net.sourceforge.myvd.protocol.ldap.mina.ldap.util.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the EntryChangeControl. All the actions are declared in
 * this class. As it is a singleton, these declaration are only done once.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class EntryChangeControlGrammar extends AbstractGrammar implements IGrammar {

    /** The logger */
    static final Logger log = LoggerFactory.getLogger(EntryChangeControlGrammar.class);

    /** Speedup for logs */
    static final boolean IS_DEBUG = log.isDebugEnabled();

    /** The instance of grammar. EntryChangeControlGrammar is a singleton */
    private static IGrammar instance = new EntryChangeControlGrammar();

    /**
     * Creates a new EntryChangeControlGrammar object.
     */
    private EntryChangeControlGrammar() {
        name = EntryChangeControlGrammar.class.getName();
        statesEnum = EntryChangeControlStatesEnum.getInstance();
        super.transitions = new GrammarTransition[EntryChangeControlStatesEnum.LAST_EC_STATE][256];
        super.transitions[EntryChangeControlStatesEnum.START_STATE][UniversalTag.SEQUENCE_TAG] = new GrammarTransition(EntryChangeControlStatesEnum.START_STATE, EntryChangeControlStatesEnum.EC_SEQUENCE_STATE, UniversalTag.SEQUENCE_TAG, new GrammarAction("Init EntryChangeControl") {

            public void action(IAsn1Container container) {
                EntryChangeControlContainer entryChangeContainer = (EntryChangeControlContainer) container;
                EntryChangeControl control = new EntryChangeControl();
                entryChangeContainer.setEntryChangeControl(control);
            }
        });
        super.transitions[EntryChangeControlStatesEnum.EC_SEQUENCE_STATE][UniversalTag.ENUMERATED_TAG] = new GrammarTransition(EntryChangeControlStatesEnum.EC_SEQUENCE_STATE, EntryChangeControlStatesEnum.CHANGE_TYPE_STATE, UniversalTag.ENUMERATED_TAG, new GrammarAction("Set EntryChangeControl changeType") {

            public void action(IAsn1Container container) throws DecoderException {
                EntryChangeControlContainer entryChangeContainer = (EntryChangeControlContainer) container;
                Value value = entryChangeContainer.getCurrentTLV().getValue();
                try {
                    int change = IntegerDecoder.parse(value, 1, 8);
                    switch(change) {
                        case ChangeType.ADD_VALUE:
                        case ChangeType.DELETE_VALUE:
                        case ChangeType.MODDN_VALUE:
                        case ChangeType.MODIFY_VALUE:
                            ChangeType changeType = ChangeType.getChangeType(change);
                            if (IS_DEBUG) {
                                log.debug("changeType = " + changeType);
                            }
                            entryChangeContainer.getEntryChangeControl().setChangeType(changeType);
                            break;
                        default:
                            String msg = "failed to decode the changeType for EntryChangeControl";
                            log.error(msg);
                            throw new DecoderException(msg);
                    }
                    entryChangeContainer.grammarEndAllowed(true);
                } catch (IntegerDecoderException e) {
                    String msg = "failed to decode the changeType for EntryChangeControl";
                    log.error(msg, e);
                    throw new DecoderException(msg);
                }
            }
        });
        super.transitions[EntryChangeControlStatesEnum.CHANGE_TYPE_STATE][UniversalTag.OCTET_STRING_TAG] = new GrammarTransition(EntryChangeControlStatesEnum.CHANGE_TYPE_STATE, EntryChangeControlStatesEnum.PREVIOUS_DN_STATE, UniversalTag.OCTET_STRING_TAG, new GrammarAction("Set EntryChangeControl previousDN") {

            public void action(IAsn1Container container) throws DecoderException {
                EntryChangeControlContainer entryChangeContainer = (EntryChangeControlContainer) container;
                ChangeType changeType = entryChangeContainer.getEntryChangeControl().getChangeType();
                if (changeType != ChangeType.MODDN) {
                    log.error("The previousDN field should not contain anything if the changeType is not MODDN");
                    throw new DecoderException("Previous DN is not allowed for this change type");
                } else {
                    Value value = entryChangeContainer.getCurrentTLV().getValue();
                    LdapDN previousDn = null;
                    try {
                        previousDn = new LdapDN(StringTools.utf8ToString(value.getData()));
                    } catch (InvalidNameException ine) {
                        log.error("Bad Previous DN : '" + StringTools.dumpBytes(value.getData()));
                        throw new DecoderException("failed to decode the previous DN");
                    }
                    if (IS_DEBUG) {
                        log.debug("previousDN = " + previousDn);
                    }
                    entryChangeContainer.getEntryChangeControl().setPreviousDn(previousDn);
                    entryChangeContainer.grammarEndAllowed(true);
                }
            }
        });
        GrammarAction setChangeNumberAction = new GrammarAction("Set EntryChangeControl changeNumber") {

            public void action(IAsn1Container container) throws DecoderException {
                EntryChangeControlContainer entryChangeContainer = (EntryChangeControlContainer) container;
                Value value = entryChangeContainer.getCurrentTLV().getValue();
                try {
                    int changeNumber = IntegerDecoder.parse(value);
                    if (IS_DEBUG) {
                        log.debug("changeNumber = " + changeNumber);
                    }
                    entryChangeContainer.getEntryChangeControl().setChangeNumber(changeNumber);
                    entryChangeContainer.grammarEndAllowed(true);
                } catch (IntegerDecoderException e) {
                    String msg = "failed to decode the changeNumber for EntryChangeControl";
                    log.error(msg, e);
                    throw new DecoderException(msg);
                }
            }
        };
        super.transitions[EntryChangeControlStatesEnum.PREVIOUS_DN_STATE][UniversalTag.INTEGER_TAG] = new GrammarTransition(EntryChangeControlStatesEnum.PREVIOUS_DN_STATE, EntryChangeControlStatesEnum.CHANGE_NUMBER_STATE, UniversalTag.INTEGER_TAG, setChangeNumberAction);
        super.transitions[EntryChangeControlStatesEnum.CHANGE_TYPE_STATE][UniversalTag.INTEGER_TAG] = new GrammarTransition(EntryChangeControlStatesEnum.CHANGE_TYPE_STATE, EntryChangeControlStatesEnum.CHANGE_NUMBER_STATE, UniversalTag.INTEGER_TAG, setChangeNumberAction);
    }

    /**
     * This class is a singleton.
     * 
     * @return An instance on this grammar
     */
    public static IGrammar getInstance() {
        return instance;
    }
}
