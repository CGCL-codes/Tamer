package com.schwidder.nucleus.refinement;

import java.util.ArrayList;
import com.schwidder.nucleus.exception.NucleusException;
import com.schwidder.nucleus.objects.SlotImpl;
import com.schwidder.nucleus.objects.interfaces.IArc;
import com.schwidder.nucleus.refinement.interfaces.IPortSlot;

/**
 * @author kai@schwidder.com
 * @version 1.0
 */
public class PortSlotImpl extends SlotImpl implements IPortSlot {

    protected ArrayList<IArc> portConnectors;

    protected int portCount;

    public PortSlotImpl() {
        super(1);
        this.portCount = 1;
        this.portConnectors = new ArrayList<IArc>();
    }

    public PortSlotImpl(int count) {
        super(count);
        this.portCount = count;
        this.portConnectors = new ArrayList<IArc>();
    }

    public IArc getArc(int slot) {
        return connectors.get(slot);
    }

    public void deleteAllPorts() {
        if (portCount == 0) return;
        while (!portConnectors.isEmpty()) portConnectors.remove(0);
    }

    public IArc addToPortSlot(IArc aArc) throws NucleusException {
        portConnectors.add(0, aArc);
        return portConnectors.get(0);
    }

    public boolean checkTokenAvailability() throws NucleusException {
        for (int i = 0; i < portCount; i++) {
            if (!portConnectors.get(i).equals(null)) {
                if (!(portConnectors.get(i).getActiveObj()).processTokenAvailable()) return false;
            }
        }
        return true;
    }

    public void signalTokenEvent() throws NucleusException {
        for (int i = 0; i < count; i++) {
            portConnectors.get(i).getActiveObj().signalTokenEvent();
        }
    }
}
