package org.slasoi.isslam.pac.mockups;

import org.slasoi.gslam.core.negotiation.SLARegistry;
import org.slasoi.slamodel.primitives.UUID;
import org.slasoi.slamodel.sla.SLA;

public class SLARegistryMockup implements SLARegistry, SLARegistry.IRegister, SLARegistry.IQuery, SLARegistry.IAdminUtils {

    private SLA sla;

    public IQuery getIQuery() {
        return this;
    }

    public IRegister getIRegister() {
        return this;
    }

    public IAdminUtils getIAdmin() {
        return null;
    }

    public UUID register(SLA sla, UUID[] uuids, SLAState arg2) throws RegistrationFailureException {
        this.sla = sla;
        return null;
    }

    public UUID sign(UUID uuid) throws UpdateFailureException {
        return uuid;
    }

    public UUID update(UUID uuid, SLA sla, UUID[] arg2, SLAState arg3) throws UpdateFailureException {
        this.sla = sla;
        return uuid;
    }

    public SLA[] getSLA(UUID[] arg0) throws InvalidUUIDException {
        SLA[] slas = new SLA[1];
        slas[0] = sla;
        return slas;
    }

    public UUID[] getDependencies(UUID arg0) throws InvalidUUIDException {
        return null;
    }

    public SLAMinimumInfo[] getMinimumSLAInfo(UUID[] arg0) throws InvalidUUIDException {
        return null;
    }

    public UUID[] getSLAsByState(SLAState[] arg0, boolean arg1) throws InvalidStateException {
        return null;
    }

    public SLAStateInfo[] getStateHistory(UUID arg0, boolean arg1) throws InvalidUUIDException, InvalidStateException {
        return null;
    }

    public UUID[] getUpwardDependencies(UUID arg0) throws InvalidUUIDException {
        return null;
    }

    public SLA[] getSLAsByParty(UUID arg0) throws InvalidUUIDException {
        return null;
    }

    public SLA[] getSLAsByTemplateId(UUID arg0) throws InvalidUUIDException {
        return null;
    }

    public void clear(UUID[] ids) {
    }

    public void clearAll() {
    }
}
