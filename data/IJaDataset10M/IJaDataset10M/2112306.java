package nts.noad;

public class RelNoad extends WordPartNoad {

    public RelNoad(Field nucleus) {
        super(nucleus);
    }

    protected String getDesc() {
        return "mathrel";
    }

    public boolean canPrecedeBin() {
        return false;
    }

    public boolean canFollowBin() {
        return false;
    }

    protected byte spacingType() {
        return SPACING_TYPE_REL;
    }
}
