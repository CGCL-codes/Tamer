package net.openchrom.chromatogram.msd.classifier.result;

public interface IChromatogramClassifierResult {

    /**
	 * Returns the result status of the applied filter.
	 * 
	 * @return {@link ResultStatus}
	 */
    ResultStatus getResultStatus();

    /**
	 * Returns a description of the applied classifier or the failure that has been
	 * occurred.
	 * 
	 * @return String
	 */
    String getDescription();
}
