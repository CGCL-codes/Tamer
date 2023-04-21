package net.sourceforge.ivi.ui.waveview.format;

/**
 * Validator for changes to an IFormatEntity. This can be used, for example,
 * to ensure that values set by an external configurator (dialog page) are
 * valid.
 */
public interface IFormatEntityEditValidator {

    /**
	 * Validates a change to an entity. If values are out of range or the
	 * tree structure is invalid, an exception should be thrown.
	 */
    public void validateEntityEdit(IFormatEntity entity) throws FormatTreeException;
}
