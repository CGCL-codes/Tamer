package com.xerox.amazonws.ec2;

/**
 * Attribute class for the productCodes attribute type.
 */
public class ProductCodesAttribute extends ImageListAttribute {

    public ProductCodesAttribute() {
        super(ImageAttributeType.productCodes);
    }

    public boolean itemTypeCompatible(ImageListAttributeItemType type) {
        return type == ImageListAttributeItemType.productCode;
    }
}
