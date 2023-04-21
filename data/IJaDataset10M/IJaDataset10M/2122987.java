package oojmerge.barcoder;

/**
 * BarcoderExtendedCode39
 * A bit of overriding here to cover the fact that we can
 * barcode an exteneded character set, but the code 3 of 9 
 * doesn't actually know that. This is done by translating 
 * the exteneded character to two characters.
 * <p>
 * Copyright &copy; medge 2010
 * Version 1.0
 * @author medge
 */
public class BarcoderExtendedCode39 extends BarcoderCode39 {

    /**
     * Overrides the parent to avoid exceptions when we find 
     * a character that is allowed but only as an extended
     * character.
     * @param value The value to be barcoded.
     * @throws BarcodeException if it we have a problem.
     */
    @Override
    public void setValue(String value) throws BarcodeException {
        for (char c : value.toCharArray()) {
            BarcoderChar bc = null;
            try {
                bc = getBarcoderChar(c);
            } catch (BarcodeException ex) {
                bc = null;
            }
            if (bc == null) bc = getExtendedBarcoderChar(c);
            if (!bc.isValidInValue()) throw new BarcodeException("Invalid character (" + c + ") for chosen barcode type " + this + ".");
        }
        setValueDirect(value);
    }

    /**
     * Returns the list of valid extended barcode characters.
     * @return characters that can be barcoded.
     */
    @Override
    public BarcoderChar[] getExtendedBarcoderChars() {
        return Extended39CharList.getAsBarcoderChars();
    }

    /**
     * Returns the string that will make up the barcode.
     * @return The barcodable string.
     * @throws BarcodeException if it we have a problem.
     */
    @Override
    public String getBarcodeValue() throws BarcodeException {
        return getExtendedBarcodeValue();
    }
}
