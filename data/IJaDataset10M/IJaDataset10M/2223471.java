package org.tonguetied.datatransfer.importing;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.tonguetied.datatransfer.common.ImportParameters;
import org.tonguetied.datatransfer.importing.ImportException.ImportErrorCode;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.Keyword;
import org.tonguetied.keywordmanagement.KeywordService;
import org.tonguetied.keywordmanagement.Translation;
import org.tonguetied.keywordmanagement.Translation.TranslationState;

/**
 * This is an abstraction on top the underlying import method. The purpose is to
 * hide away the implementation details such that multiple import types can be
 * handled.
 * 
 * @author bsion
 * 
 */
public abstract class Importer {

    private KeywordService keywordService;

    protected static final Logger logger = Logger.getLogger(Importer.class);

    /**
     * Create a new instance of Importer.
     * 
     * @param keywordService the interface to keyword functions
     */
    protected Importer(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    /**
     * Import {@link Keyword}/{@link Translation}s into the system. If the
     * {@link ImportParameters#getTranslationState()} is <code>null</code>
     * then the <code>TranslationState<code> is set to
     * {@link TranslationState#UNVERIFIED} for all imported 
     * {@link Translation}s
     *  
     * @param parameters the parameters used to process import data
     * @throws ImportException if an error occurs during the whole import 
     * process. The {@link ImportException} may contain additional information 
     * as to why the import failed
     */
    public final void importData(ImportParameters parameters) throws ImportException {
        validate(parameters);
        if (parameters.getTranslationState() == null) parameters.setTranslationState(TranslationState.UNVERIFIED);
        doImport(parameters.getData(), parameters.getTranslationState());
    }

    /**
     * The delegate method to which {@link Keyword}/{@link Translation}s are
     * imported into the system. This is the method subclasses should implement
     * to get the desired features needed for the import type.
     * 
     * Implementing methods can determine if they will overwrite existing data
     * or not.
     * 
     * @param input raw data to import. This data can be in text or binary
     *        format. This will be specific to the importer, as data can be
     *        imported from different sources.
     * @param state the value of the {@link TranslationState} to set on imported
     *        {@link Translation}s. This should never be <code>null</code> as
     *        {@link #importData(ImportParameters)} should set the value of
     *        <code>state</code> if it is not already set
     * @throws ImportException if an error occurs during the data import
     *         process. The {@link ImportException} may contain additional
     *         information as to why the import failed
     */
    protected abstract void doImport(final byte[] input, final TranslationState state) throws ImportException;

    /**
     * Validates the {@link ImportParameters} to make sure that all criteria are
     * met to avoid errors during the import. Can be overridden by implementing
     * classes to add extra validation, or can override specific parameter
     * validation.
     * 
     * @param parameters the {@link ImportParameters} to validate
     * @throws ImportException if an error occurs during the validation process.
     *         The {@link ImportException} may contain additional information as
     *         to why the import failed
     * @see #validate(byte[], List)
     */
    protected void validate(ImportParameters parameters) throws ImportException {
        List<ImportErrorCode> errorCodes = new ArrayList<ImportErrorCode>();
        validate(parameters.getData(), errorCodes);
        validate(parameters.getFileName(), parameters.getBundle(), errorCodes);
        if (!errorCodes.isEmpty()) {
            logger.warn("failed to import files. errorCodes (" + errorCodes + ")");
            throw new ImportException(errorCodes);
        }
    }

    /**
     * Validates the data to make sure that certain criteria are met to avoid
     * errors during the import. Can be overridden by implementing classes to
     * add extra validation.
     * 
     * @param input raw data to import. This data can be in text or binary
     *        format. This will be specific to the importer, as data can be
     *        imported from different sources.
     * @param errorCodes the list of existing {@link ImportErrorCode}s. Cannot
     *        be <code>null</code>
     * @throws ImportException if an error occurs during the validation process.
     *         The {@link ImportException} may contain additional information as
     *         to why the import failed
     */
    protected void validate(final byte[] input, List<ImportErrorCode> errorCodes) throws ImportException {
        if (input == null || input.length == 0) errorCodes.add(ImportErrorCode.emptyData);
    }

    /**
     * Override this method to perform validation specific to the input data
     * file name. The default implementation does nothing.
     * 
     * @param fileName the fileName corresponding to the input file
     * @param bundle TODO
     * @param errorCodes the list of existing {@link ImportErrorCode}s. Cannot
     *        be <code>null</code>
     * @throws ImportException if an error occurs during the validation process.
     *         The {@link ImportException} may contain additional information as
     *         to why the import failed
     */
    protected void validate(final String fileName, final Bundle bundle, List<ImportErrorCode> errorCodes) throws ImportException {
    }

    /**
     * @return the keywordService
     */
    protected KeywordService getKeywordService() {
        return keywordService;
    }
}
