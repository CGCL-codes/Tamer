package org.eclipse.dltk.dbgp.debugger.packet.sender.response;

import org.eclipse.dltk.dbgp.DbgpRequest;

/**
 * DBGp XML Set Feature Packet. Example :
 * 
 * <pre>
 * <response command="feature_set"
 *           feature="feature_name"
 *           success="0|1"
 *           transaction_id="transaction_id"/>
 * </pre>
 * 
 * @see specification at http://xdebug.org/docs-dbgp.php#feature-set
 */
public class FeatureSetPacket extends BaseFeaturePacket {

    private static final String FEATURE_ATTR = "feature";

    public FeatureSetPacket(DbgpRequest command) {
        super(command);
        String featureName = super.getFeatureName();
        addAttribute(FEATURE_ATTR, featureName);
        addAttribute(SUCCESS_ATTR, ONE);
    }
}
