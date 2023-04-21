package com.g2inc.scap.library.domain.oval;

/**
 * Class represents affected product in oval
 * definition metadata.
 *
 * @author ssill2
 * @see com.g2inc.scap.library.domain.oval.Metadata
 * @see com.g2inc.scap.library.domain.oval.AffectedItemContainer
 */
public abstract class AffectedProduct extends AffectedItem {

    public AffectedProduct(OvalDefinitionsDocument parentDocument) {
        super(parentDocument);
        setType(AffectedItemType.PRODUCT);
    }

    @Override
    public String toString() {
        return "Product: " + getValue();
    }
}
