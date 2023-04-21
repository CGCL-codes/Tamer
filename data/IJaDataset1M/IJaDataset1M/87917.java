package org.wijiscommons.cdcl.gatepoint.integration.rulesmanagement;

import java.util.Date;
import java.util.List;
import org.wijiscommons.cdcl.Custodian;
import org.wijiscommons.cdcl.rules.RuleSheetDeck;
import org.wijiscommons.semantic.SemanticEntry;

/**
 * TODO: Add Java Doc
 *
 * @author Pattabi Doraiswamy (http://pattabidoraiswamy.com)
 * @since Jan 22, 2009
 */
class RulesManagementSystemGatewayImpl implements RulesManagementSystemGateway {

    /**
     * {@inheritDoc}
     */
    public RuleSheetDeck getRuleSheetDeck(Date rulesEffectiveTimeStamp, SemanticEntry problemSpaceIdentifier, Custodian primaryCustodian, List<Custodian> stakeHolders) throws RulesManagementSystemException, RulesManagementSystemRuntimeException {
        throw new UnsupportedOperationException();
    }
}
