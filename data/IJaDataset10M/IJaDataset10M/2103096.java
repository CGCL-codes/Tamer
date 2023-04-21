package de.intarsys.pdf.pd;

import de.intarsys.pdf.cos.COSObject;

/**
 * Use tensor product shading when filling the shape.
 * 
 */
public class PDTensorProductShading extends PDShading {

    /**
	 * The meta class implementation
	 */
    public static class MetaClass extends PDShading.MetaClass {

        protected MetaClass(Class paramInstanceClass) {
            super(paramInstanceClass);
        }
    }

    /** The meta class instance */
    public static final MetaClass META = new MetaClass(MetaClass.class.getDeclaringClass());

    protected PDTensorProductShading(COSObject object) {
        super(object);
    }

    public int getShadingType() {
        return SHADING_TYPE_TENSORPRODUCT;
    }
}
