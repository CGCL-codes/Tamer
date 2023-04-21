package org.qedeq.kernel.common;

/**
 * This interface provides constants of this package.
 *
 * @version $Revision: 1.1 $
 * @author Michael Meyling
 */
public interface DependencyStateDescriptions {

    /** Undefined state code. */
    public static final int STATE_CODE_UNDEFINED = 0;

    /** Undefined state description. */
    public static final String STATE_STRING_UNDEFINED = "undefined";

    /** Loading required modules code. */
    public static final int STATE_CODE_LOADING_REQUIRED_MODULES = 11;

    /** Loading required modules description. */
    public static final String STATE_STRING_LOADING_REQUIRED_MODULES = "loading required modules";

    /** Loading required modules failed code. */
    public static final int STATE_CODE_LOADING_REQUIRED_MODULES_FAILED = 12;

    /** Loading required modules failed description. */
    public static final String STATE_STRING_LOADING_REQUIRED_MODULES_FAILED = "loading required modules failed";

    /** Loading required modules of required modules code. */
    public static final int STATE_CODE_LOADING_REQUIRED_REQUIREMENTS = 13;

    /** Loading required modules of required modules  failed description. */
    public static final String STATE_STRING_LOADING_REQUIRED_REQUIREMENTS_FAILED = "loading required modules of required modules failed";

    /** Loaded required modules code. */
    public static final int STATE_CODE_LOADED_REQUIRED_MODULES = 15;

    /** Loaded required modules description. */
    public static final String STATE_STRING_LOADED_REQUIRED_MODULES = "loaded required modules";
}
