package org.mobicents.slee.resource.diameter.s6a.events.avp;

import net.java.slee.resource.diameter.s6a.events.avp.AllocationRetentionPriorityAvp;
import net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes;
import net.java.slee.resource.diameter.s6a.events.avp.PreEmptionCapability;
import net.java.slee.resource.diameter.s6a.events.avp.PreEmptionVulnerability;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation for {@link AllocationRetentionPriorityAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class AllocationRetentionPriorityAvpImpl extends GroupedAvpImpl implements AllocationRetentionPriorityAvp {

    public AllocationRetentionPriorityAvpImpl() {
        super();
    }

    public AllocationRetentionPriorityAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
        super(code, vendorId, mnd, prt, value);
    }

    public boolean hasPriorityLevel() {
        return hasAvp(DiameterS6aAvpCodes.PRIORITY_LEVEL, DiameterS6aAvpCodes.S6A_VENDOR_ID);
    }

    public void setPriorityLevel(long pl) {
        addAvp(DiameterS6aAvpCodes.PRIORITY_LEVEL, DiameterS6aAvpCodes.S6A_VENDOR_ID, pl);
    }

    public long getPriorityLevel() {
        return getAvpAsUnsigned32(DiameterS6aAvpCodes.PRIORITY_LEVEL, DiameterS6aAvpCodes.S6A_VENDOR_ID);
    }

    public boolean hasPreEmptionCapability() {
        return hasAvp(DiameterS6aAvpCodes.PRE_EMPTION_CAPABILITY, DiameterS6aAvpCodes.S6A_VENDOR_ID);
    }

    public PreEmptionCapability getPreEmptionCapability() {
        return (PreEmptionCapability) getAvpAsEnumerated(DiameterS6aAvpCodes.PRE_EMPTION_CAPABILITY, DiameterS6aAvpCodes.S6A_VENDOR_ID, PreEmptionCapability.class);
    }

    public void setPreEmptionCapability(PreEmptionCapability pec) {
        addAvp(DiameterS6aAvpCodes.PRE_EMPTION_CAPABILITY, DiameterS6aAvpCodes.S6A_VENDOR_ID, pec.getValue());
    }

    public boolean hasPreEmptionVulnerability() {
        return hasAvp(DiameterS6aAvpCodes.PRE_EMPTION_VULNERABILITY, DiameterS6aAvpCodes.S6A_VENDOR_ID);
    }

    public PreEmptionVulnerability getPreEmptionVulnerability() {
        return (PreEmptionVulnerability) getAvpAsEnumerated(DiameterS6aAvpCodes.PRE_EMPTION_VULNERABILITY, DiameterS6aAvpCodes.S6A_VENDOR_ID, PreEmptionVulnerability.class);
    }

    public void setPreEmptionVulnerability(PreEmptionVulnerability pev) {
        addAvp(DiameterS6aAvpCodes.PRE_EMPTION_VULNERABILITY, DiameterS6aAvpCodes.S6A_VENDOR_ID, pev.getValue());
    }
}
