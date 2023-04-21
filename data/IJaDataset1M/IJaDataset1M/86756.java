package net.openchrom.chromatogram.msd.identifier.chromatogram;

import net.openchrom.chromatogram.msd.identifier.exceptions.ValueMustNotBeNullException;
import net.openchrom.chromatogram.msd.identifier.settings.IIdentifierSettings;
import net.openchrom.chromatogram.msd.model.core.support.IChromatogramSelection;

public abstract class AbstractChromatogramIdentifier implements IChromatogramIdentifier {

    /**
	 * Tests if the chromatogram selection is a valid instance.
	 * 
	 * @param chromatogramSelection
	 * @throws ValueMustNotBeNullException
	 */
    public void validateChromatogramSelection(IChromatogramSelection chromatogramSelection) throws ValueMustNotBeNullException {
        if (chromatogramSelection == null) {
            throw new ValueMustNotBeNullException("The chromatogram selection must not be null.");
        }
        if (chromatogramSelection.getChromatogram() == null) {
            throw new ValueMustNotBeNullException("The chromatogram must not be null.");
        }
    }

    /**
	 * Throws an exception if the settings are null.
	 * 
	 * @param identifierSettings
	 * @throws ValueMustNotBeNullException
	 */
    public void validateSettings(IIdentifierSettings identifierSettings) throws ValueMustNotBeNullException {
        if (identifierSettings == null) {
            throw new ValueMustNotBeNullException("The identifier settings must not be null.");
        }
    }
}
