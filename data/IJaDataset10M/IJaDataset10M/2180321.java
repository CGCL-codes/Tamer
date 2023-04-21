package com.fddtool.si.export;

import com.fddtool.resource.MessageKey;
import com.fddtool.resource.MessageProviderImpl;

/**
 * This is an enumeration of possible levels of severity for the notes that may
 * be generated during project import.
 * 
 * @author SKhramtc
 */
public class ImportNoteSeverity {

    /**
     * The level "Information"
     */
    public static final ImportNoteSeverity INFO = new ImportNoteSeverity("info", MessageKey.LBL_INFORMATION);

    /**
     * The level "Warning"
     */
    public static final ImportNoteSeverity WARNING = new ImportNoteSeverity("warning", MessageKey.LBL_WARNING);

    /**
     * The level "Error"
     */
    public static final ImportNoteSeverity ERROR = new ImportNoteSeverity("error", MessageKey.LBL_ERROR);

    /**
     * The code for the severity.
     */
    private String code;

    /**
     * The message key to be used to retrieve the locale-specific description of
     * severity.
     */
    private MessageKey labelKey;

    private ImportNoteSeverity(String code, MessageKey labelKey) {
        this.code = code;
        this.labelKey = labelKey;
    }

    /**
     * Indicates if this severity represents an error.
     * 
     * @return boolean <code>true</code> if this severity represents an error
     *         and <code>false</code> otherwise.
     */
    public boolean isError() {
        return this == ERROR;
    }

    /**
     * Returns a unique code for the severity.
     * 
     * @return non-empty String with severity code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the lavel fpr the severity. The label may be returned in 
     * local language.
     * 
     * @return String with label for the severty. 
     */
    public String getLabel() {
        return MessageProviderImpl.getProvider().getMessage(labelKey);
    }
}
