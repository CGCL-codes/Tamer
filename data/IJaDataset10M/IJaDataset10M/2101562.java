package net.openchrom.chromatogram.msd.integrator.core.peaks;

/**
 * @author eselmeister
 */
public interface IPeakIntegratorSupplier {

    /**
	 * The id of the extension point: e.g.
	 * (net.openchrom.chromatogram.msd.integrator.supplier.chemstation)
	 * 
	 * @return String
	 */
    String getId();

    /**
	 * A short description of the functionality of the extension point.
	 * 
	 * @return String
	 */
    String getDescription();

    /**
	 * The integrator name that can be shown in a list box dialogue.
	 * 
	 * @return String
	 */
    String getIntegratorName();
}
