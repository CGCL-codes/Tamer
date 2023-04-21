package com.globalretailtech.pos.events;

import java.util.Vector;
import java.util.Hashtable;
import com.globalretailtech.util.Application;
import com.globalretailtech.data.Promotion;
import com.globalretailtech.pos.operators.*;
import com.globalretailtech.pos.ej.*;

public class ItemMarkdown extends PosEvent {

    private PosItemModifier modifier;

    /** The item modifier (promotion) for this event */
    public PosItemModifier modifier() {
        return modifier;
    }

    /** Simple constructor for dynamic load */
    public ItemMarkdown() {
        initTransition();
    }

    /**
     * Load the promotion, engage it and add it to
     * the EJ.
     */
    public void engage(int value) {
        String fetchSpec = Promotion.getByNo(value);
        Vector v = Application.dbConnection().fetch(new Promotion(), fetchSpec);
        if (v.size() == 1) {
            Promotion promotion = (Promotion) v.elementAt(0);
            EjPromotion eji = new EjPromotion(context(), promotion);
            eji.engage(0);
        }
        context().eventStack().nextEvent();
    }

    /**
     * Check profile on this one.
     */
    public boolean checkProfile() {
        return true;
    }

    /** Validate transistions state */
    private Hashtable transistions;

    private void initTransition() {
        transistions = new Hashtable();
        transistions.put(FirstItem.eventName(), new Boolean(true));
        transistions.put(PosError.eventName(), new Boolean(true));
    }

    public boolean validTransition(String event) {
        if (transistions.get(event) != null) return true; else return false;
    }

    /** Clear impementation for clear, do nothing. */
    public void clear() {
    }

    /** Always return true. */
    public boolean checkProfile(int event) {
        return true;
    }

    private static String eventname = "ItemMarkdown";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}
