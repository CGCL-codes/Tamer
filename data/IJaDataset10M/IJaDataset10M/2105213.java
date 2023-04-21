package com.schwidder.nucleus.helper;

import com.schwidder.nucleus.exception.NucleusException;
import com.schwidder.nucleus.helper.interfaces.IFactory;
import com.schwidder.nucleus.objects.ArcMode;
import com.schwidder.nucleus.objects.SlotMode;
import com.schwidder.nucleus.objects.interfaces.IArc;
import com.schwidder.nucleus.objects.interfaces.IObjectActive;
import com.schwidder.nucleus.objects.interfaces.IObjectPassive;
import com.schwidder.nucleus.objects.interfaces.IRule;
import com.schwidder.nucleus.refinement.interfaces.IPortSlot;

/**
 * @author kai@schwidder.com
 * @version 1.0
 */
public class FactoryImpl implements IFactory {

    public FactoryImpl() {
    }

    public IArc data_arc(IObjectPassive place, IObjectActive transition) throws NucleusException {
        return data_arc(place, transition, null);
    }

    public IArc data_arc(IObjectPassive place, IObjectActive transition, IRule rule) throws NucleusException {
        return data_arc(place, transition, -1, rule);
    }

    public IArc data_arc(IObjectPassive place, IObjectActive transition, int slot) throws NucleusException {
        return data_arc(place, transition, slot, null);
    }

    public IArc data_arc(IObjectPassive place, IObjectActive transition, int slot, IRule aRule) throws NucleusException {
        IArc resultArc = createArc(place, transition, ArcMode.INPUTARC, aRule);
        transition.addArc(resultArc, slot, SlotMode.INPUTSLOT);
        place.addArc(SlotMode.OUTPUTSLOT, resultArc);
        return resultArc;
    }

    public IArc data_arc(IObjectActive transition, IObjectPassive place) throws NucleusException {
        return data_arc(transition, place, null);
    }

    public IArc data_arc(IObjectActive transition, IObjectPassive place, IRule rule) throws NucleusException {
        return data_arc(transition, place, -1, rule);
    }

    public IArc data_arc(IObjectActive transition, IObjectPassive place, int slot) throws NucleusException {
        return data_arc(transition, place, slot, null);
    }

    public IArc data_arc(IObjectActive transition, IObjectPassive place, int slot, IRule aRule) throws NucleusException {
        IArc resultArc = createArc(place, transition, ArcMode.OUTPUTARC, aRule);
        transition.addArc(resultArc, slot, (int) SlotMode.OUTPUTSLOT);
        place.addArc(SlotMode.INPUTSLOT, resultArc);
        return resultArc;
    }

    public IArc data_arc(IPortSlot port, IObjectActive transition) throws NucleusException {
        return data_arc(port, transition, null);
    }

    public IArc data_arc(IPortSlot port, IObjectActive transition, IRule aRule) throws NucleusException {
        IObjectPassive aPlace = port.getArc(0).getPassiveObj();
        IArc resultArc = createArc(aPlace, transition, ArcMode.INPUTARC, aRule);
        port.addToPortSlot(resultArc);
        return data_arc(aPlace, transition, aRule);
    }

    public IArc data_arc(IObjectActive transition, IPortSlot port) throws NucleusException {
        return data_arc(transition, port, null);
    }

    public IArc data_arc(IObjectActive transition, IPortSlot port, IRule aRule) throws NucleusException {
        IObjectPassive aPlace = port.getArc(0).getPassiveObj();
        IArc resultArc = createArc(aPlace, transition, ArcMode.OUTPUTARC, aRule);
        port.addToPortSlot(resultArc);
        return data_arc(transition, (IObjectPassive) aPlace, aRule);
    }

    public IArc createArc(IObjectPassive aPlace, IObjectActive aTransition, int arcMode, Object args) throws NucleusException {
        return null;
    }
}
