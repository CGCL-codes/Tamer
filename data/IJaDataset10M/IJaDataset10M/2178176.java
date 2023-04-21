package nts.math;

import nts.noad.Noad;
import nts.noad.RelNoad;
import nts.noad.Field;

public class MathRelPrim extends MathCompPrim {

    public MathRelPrim(String name) {
        super(name);
    }

    public Noad makeNoad(Field field) {
        return new RelNoad(field);
    }
}
