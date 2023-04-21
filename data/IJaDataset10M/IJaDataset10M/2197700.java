package plecotus.models.bwin.extension;

import plecotus.GenericStand;
import plecotus.exception.WrongElementTypeException;
import plecotus.extension.GenericStandExtension;
import plecotus.models.bwin.visitor.GrowthInducer;
import plecotus.unit.Time;

public class BwinStand extends GenericStandExtension {

    public BwinStand(GenericStand stand, String name) {
        super(stand, name);
        try {
            stand.addExtension(this);
        } catch (WrongElementTypeException e) {
            e.printStackTrace();
        }
    }

    public void model(Time duration) {
        GrowthInducer gi = new GrowthInducer(duration);
        stand.accept(gi);
    }
}
