package eu.actorsproject.xlim.type;

import eu.actorsproject.xlim.XlimType;

public class TypeConversion {

    protected TypeKind mSourceKind;

    protected TypeKind mTargetKind;

    public TypeConversion(TypeKind sourceKind, TypeKind targetKind) {
        mSourceKind = sourceKind;
        mTargetKind = targetKind;
    }

    public TypeKind getSourceTypeKind() {
        return mSourceKind;
    }

    public TypeKind getTargetTypeKind() {
        return mTargetKind;
    }

    public boolean isApplicable(XlimType t) {
        return mSourceKind.hasPromotionFrom(t);
    }

    public XlimType apply(XlimType sourceT) {
        return mTargetKind.createType();
    }
}
