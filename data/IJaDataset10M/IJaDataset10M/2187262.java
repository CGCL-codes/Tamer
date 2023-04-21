package de.intarsys.pdf.font;

import de.intarsys.pdf.cds.CDSMatrix;
import de.intarsys.pdf.cos.COSBasedObject;
import de.intarsys.pdf.cos.COSDictionary;
import de.intarsys.pdf.cos.COSName;
import de.intarsys.pdf.cos.COSObject;

/**
 * 
 */
public class PDFontType3 extends PDSingleByteFont {

    /**
	 * The meta class implementation
	 */
    public static class MetaClass extends PDFont.MetaClass {

        protected MetaClass(Class instanceClass) {
            super(instanceClass);
        }

        @Override
        protected COSBasedObject doCreateCOSBasedObject(COSObject object) {
            return new PDFontType3(object);
        }
    }

    public static final COSName DK_FontBBox = COSName.constant("FontBBox");

    public static final COSName DK_FontMatrix = COSName.constant("FontMatrix");

    public static final COSName DK_CharProcs = COSName.constant("CharProcs");

    public static final COSName DK_Resources = COSName.constant("Resources");

    public static final COSName DK_ToUnicode = COSName.constant("ToUnicode");

    /** The meta class instance */
    public static final MetaClass META = new MetaClass(MetaClass.class.getDeclaringClass());

    /**
	 * @param object
	 */
    public PDFontType3(COSObject object) {
        super(object);
    }

    public COSDictionary cosGetCharProcs() {
        return cosGetField(DK_CharProcs).asDictionary();
    }

    @Override
    protected COSName cosGetExpectedSubtype() {
        return CN_Subtype_Type3;
    }

    public COSDictionary cosSetCharProcs(COSDictionary newDict) {
        return cosSetField(DK_CharProcs, newDict).asDictionary();
    }

    @Override
    protected PDFontDescriptor createBuiltinFontDescriptor() {
        return null;
    }

    @Override
    public String getFontFamilyName() {
        return "Helvetica";
    }

    public CDSMatrix getFontMatrix() {
        return CDSMatrix.createFromCOS(cosGetField(DK_FontMatrix).asArray());
    }

    @Override
    public String getFontName() {
        return "Helvetica";
    }

    @Override
    public PDFontStyle getFontStyle() {
        return PDFontStyle.REGULAR;
    }

    @Override
    public String getFontType() {
        return "Type3";
    }

    @Override
    public int getGlyphWidthEncoded(int codePoint) {
        int width = super.getGlyphWidthEncoded(codePoint);
        float[] vector = new float[] { width, 0 };
        vector = getFontMatrix().transform(vector);
        return (int) (vector[0] * 1000f);
    }

    @Override
    public boolean isEmbedded() {
        return true;
    }

    @Override
    public boolean isSubset() {
        return false;
    }

    public void setMatrix(CDSMatrix fontMatrix) {
        setFieldObject(DK_FontMatrix, fontMatrix);
    }

    @Override
    public String toString() {
        return cosGetSubtype() + "-Font ";
    }
}
