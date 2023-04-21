package org.mobicents.slee.resource.diameter.cca.events.avp;

import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.IPFilterRule;
import net.java.slee.resource.diameter.cca.events.avp.CreditControlAVPCodes;
import net.java.slee.resource.diameter.cca.events.avp.FinalUnitActionType;
import net.java.slee.resource.diameter.cca.events.avp.FinalUnitIndicationAvp;
import net.java.slee.resource.diameter.cca.events.avp.RedirectServerAvp;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Start time:13:51:00 2008-11-10<br>
 * Project: mobicents-diameter-parent<br>
 * Implementation of AVP: {@link FinalUnitIndicationAvp}
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class FinalUnitIndicationAvpImpl extends GroupedAvpImpl implements FinalUnitIndicationAvp {

    public FinalUnitIndicationAvpImpl() {
        super();
    }

    public FinalUnitIndicationAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
        super(code, vendorId, mnd, prt, value);
    }

    public String[] getFilterIds() {
        return getAvpsAsUTF8String(DiameterAvpCodes.FILTER_ID);
    }

    public FinalUnitActionType getFinalUnitAction() {
        return (FinalUnitActionType) getAvpAsEnumerated(CreditControlAVPCodes.Final_Unit_Action, FinalUnitActionType.class);
    }

    public RedirectServerAvp getRedirectServer() {
        return (RedirectServerAvp) getAvpAsCustom(CreditControlAVPCodes.Redirect_Server, RedirectServerAvpImpl.class);
    }

    public IPFilterRule[] getRestrictionFilterRules() {
        return (IPFilterRule[]) getAvpsAsIPFilterRule(CreditControlAVPCodes.Restriction_Filter_Rule);
    }

    public boolean hasFinalUnitAction() {
        return hasAvp(CreditControlAVPCodes.Final_Unit_Action);
    }

    public boolean hasRedirectServer() {
        return hasAvp(CreditControlAVPCodes.Redirect_Server);
    }

    public void setFilterId(String filterId) {
        addAvp(DiameterAvpCodes.FILTER_ID, filterId);
    }

    public void setFilterIds(String[] filterIds) {
        for (String filterId : filterIds) {
            setFilterId(filterId);
        }
    }

    public void setFinalUnitAction(FinalUnitActionType finalUnitAction) {
        addAvp(CreditControlAVPCodes.Final_Unit_Action, finalUnitAction.getValue());
    }

    public void setRedirectServer(RedirectServerAvp redirectServer) {
        addAvp(CreditControlAVPCodes.Redirect_Server, redirectServer.byteArrayValue());
    }

    public void setRestrictionFilterRule(IPFilterRule restrictionFilterRule) {
        addAvp(CreditControlAVPCodes.Restriction_Filter_Rule, restrictionFilterRule.getRuleString());
    }

    public void setRestrictionFilterRules(IPFilterRule[] restrictionFilterRules) {
        for (IPFilterRule restrictionFilterRule : (IPFilterRule[]) restrictionFilterRules) {
            setRestrictionFilterRule(restrictionFilterRule);
        }
    }
}
