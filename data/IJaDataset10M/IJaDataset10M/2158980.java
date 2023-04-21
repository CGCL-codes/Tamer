package com.schwidder.petrinet.refinement;

import com.schwidder.nucleus.exception.NucleusException;
import com.schwidder.nucleus.objects.ArcMode;
import com.schwidder.nucleus.objects.TokenImpl;
import com.schwidder.nucleus.objects.interfaces.IObjectPassive;
import com.schwidder.nucleus.objects.interfaces.IToken;
import com.schwidder.nucleus.refinement.PortSlotImpl;
import com.schwidder.petrinet.objects.Anonym;
import com.schwidder.petrinet.objects.interfaces.IPTArc;
import com.schwidder.petrinet.objects.interfaces.IPTPlace;
import com.schwidder.petrinet.refinement.interfaces.IPTPortSlot;

/**
 * PTPortSlotImpl implements the P/T specifics for refined nodes
 * 
 * @author kai@schwidder.com
 * @version 1.0
 */
public class PTPortSlotImpl extends PortSlotImpl implements IPTPortSlot {

    public void setCapacity(int capacity) {
        ((IPTArc) portConnectors.get(0)).setCapacity(capacity);
    }

    public boolean checkCapacity() throws NucleusException {
        IPTArc aArc = (IPTArc) connectors.get(0);
        IPTPlace aPlace = (IPTPlace) aArc.getPassiveObj();
        if (aArc.getArcMode() == ArcMode.INPUTARC) return (aPlace.getFilling() - aArc.getCapacity() >= 0) ? false : true; else if (aArc.getArcMode() == ArcMode.OUTPUTARC) {
            if (aPlace.getCapacity() == 0) {
                return false;
            }
            return (aArc.getCapacity() + aPlace.getFilling() <= aPlace.getCapacity()) ? false : true;
        }
        return true;
    }

    public boolean putToken(Object aToken) throws NucleusException {
        int count = ((IPTArc) portConnectors.get(0)).getCapacity();
        IObjectPassive aPlace = portConnectors.get(0).getPassiveObj();
        for (int i = 0; i < count; i++) aPlace.putToken(new TokenImpl(new Anonym()));
        return true;
    }

    public IToken getToken() throws NucleusException {
        int count = ((IPTArc) portConnectors.get(0)).getCapacity();
        for (int i = 0; i < count; i++) {
            portConnectors.get(0).getToken();
        }
        return null;
    }

    public PTPortSlotImpl() {
        super(1);
    }

    public PTPortSlotImpl(int connections) {
        super(connections);
    }
}
