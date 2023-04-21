package net.openchrom.chromatogram.msd.converter.supplier.cdf.io.support;

import net.openchrom.chromatogram.msd.converter.supplier.cdf.exceptions.NoSuchScanStored;
import net.openchrom.chromatogram.msd.converter.supplier.cdf.model.CDFMassSpectrum;

public interface ICDFChromatogramArrayReader extends IAbstractCDFChromatogramArrayReader {

    /**
	 * Returns a valid mass spectrum of the given scan.
	 * 
	 * @param scan
	 * @return CDFMassSpectrum
	 * @throws NoSuchScanStored
	 */
    public CDFMassSpectrum getMassSpectrum(int scan) throws NoSuchScanStored;
}
