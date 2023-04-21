package oojmerge.barcoder;

/**
 * BarcoderExtendedCode39Thick
 * Thickened barcodes for the extended code 3 of 9.
 * <p>
 * Copyright &copy; medge 2010
 * Version 1.0
 * @author medge
 */
public class BarcoderExtendedCode39Thick extends BarcoderExtendedCode39 {

    /**
     * Returns the list of valid barcode characters.
     * @return characters that can be barcoded.
     */
    @Override
    public BarcoderChar[] getBarcoderChars() {
        return BarcoderCode39Thick.barcoderChars;
    }
}
