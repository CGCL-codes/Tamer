package org.axcommunity.niagara.math;

import javax.baja.sys.*;
import javax.baja.status.*;

/**
 * Subtracts the subtrahend from the minuend on the transition of the trigger from false to true
 * @author Mike Arnott, Kors Engineering
 */
public class BTrSubtract extends BComponent {

    boolean fire = false;

    public void started() {
        try {
            if (getSlot("Out").isProperty()) {
            }
        } catch (Exception ex) {
            add("Out", new BStatusNumeric(0), Flags.SUMMARY);
            add("In$20A", new BStatusNumeric(0), Flags.SUMMARY);
            add("In$20B", new BStatusNumeric(0), Flags.SUMMARY);
        }
    }

    public void changed(Property property, Context context) {
        super.changed(property, context);
        if (!Sys.atSteadyState()) {
            return;
        }
        try {
            if (property == facets) {
                setFacets(getSlot("Out"), getFacets());
                setFacets(getSlot("In$20A"), getFacets());
                setFacets(getSlot("In$20B"), getFacets());
            }
            if (property == trigger) {
                if (getTrigger().getValue() && fire == false) {
                    double a, b, result;
                    a = ((BStatusNumeric) get(getProperty("In$20A"))).getValue();
                    b = ((BStatusNumeric) get(getProperty("In$20B"))).getValue();
                    result = a - b;
                    set(getProperty("Out"), new BStatusNumeric(result));
                    fire = true;
                } else {
                    fire = false;
                }
            }
        } catch (Exception e) {
            System.out.println(BAbsTime.now().toString() + ": Kors Component Error at " + this.getSlotPath().toString() + ":" + e.toString());
        }
    }

    public static final Property facets = newProperty(0, BFacets.makeNumeric(1));

    public static final Property trigger = newProperty(Flags.SUMMARY, new BStatusBoolean(false));

    public BFacets getFacets() {
        return (BFacets) get(facets);
    }

    public BStatusBoolean getTrigger() {
        return (BStatusBoolean) get(trigger);
    }

    public void setFacets(BFacets v) {
        set(facets, v);
    }

    public void setTrigger(BStatusBoolean v) {
        set(trigger, v);
    }

    public BIcon getIcon() {
        return icon;
    }

    private static final BIcon icon = BIcon.make("local:|module://axCommunity/org/axcommunity/niagara/graphics/korsLogo.png");

    public static final Type TYPE = Sys.loadType(BTrSubtract.class);

    public Type getType() {
        return TYPE;
    }
}
