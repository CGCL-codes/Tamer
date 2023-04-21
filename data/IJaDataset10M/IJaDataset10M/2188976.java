package nts.typo;

import nts.base.Dimen;
import nts.node.BoxSizes;

public class WdPrim extends BoxDimenPrim {

    public WdPrim(String name, SetBoxPrim reg) {
        super(name, reg);
    }

    protected BoxSizes changeSizes(BoxSizes sizes, Dimen dimen) {
        return new BoxSizes(sizes.getHeight(), dimen, sizes.getDepth(), sizes.getLeftX());
    }

    protected Dimen selectSize(BoxSizes sizes) {
        return sizes.getWidth();
    }
}
