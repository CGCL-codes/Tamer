package net.openchrom.chromatogram.msd.converter.core;

public interface IConverterSupportSetter extends IConverterSupport {

    /**
	 * Adds a ({@link ISupplier}) to the ConverterSupport.
	 * 
	 * @param supplier
	 */
    void add(final ISupplier supplier);
}
