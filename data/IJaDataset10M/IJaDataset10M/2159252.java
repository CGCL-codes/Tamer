package de.sicari.starter.parameters;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This is a wrapper class for a list of script parameters.
 *
 * @author Matthias Pressfreund
 * @version "$Id: SicariScriptParameters.java 306 2007-08-29 22:35:03Z jpeters $"
 */
public class SicariScriptParameters implements Parameter {

    /**
     * The name of the <i>script parameters</i> property
     */
    public static final String PROPERTY = "sicari.script.parameters";

    /**
     * The name of the default file
     */
    public static final String DEFAULT_NAME = "sicari1.conf";

    /**
     * The delimiter that separates the parameters
     */
    public static final String DELIMITER = ",";

    /**
     * The parameter list
     */
    protected List<String> params_;

    /**
     * The <code>String</code> represenation
     */
    protected String abbr_;

    /**
     * The <code>String</code> representation without abbreviations
     */
    protected String full_;

    /**
     * Hidden construction. Usage of {@link #create} is
     * intended for object creation.
     *
     * @param params The list of parameters
     */
    protected SicariScriptParameters(List<String> params) {
        params_ = params;
    }

    /**
     * Check if this is a valid <code>SicariScriptParameters</code>
     * object. Create the <code>String</code> representations with and
     * without abbreviations.
     *
     * @throws ParameterInitException
     *   if the parameter list cannot be used to create a valid
     *   <code>SicariScriptParameters</code> object
     */
    protected void init() throws ParameterInitException {
        if (params_ == null) {
            throw new ParameterInitException("No valid script parameters", this);
        }
        abbr_ = toString(false);
        full_ = toString(true);
    }

    /**
     * Create a valid <code>SicariScriptParameters</code> instance.
     *
     * @param params The parameters in a row, separated by the delimiter
     * @return A valid <code>SicariScriptParameters</code> object
     * @throws ParameterInitException
     *   in case of an initialization failure
     */
    public static SicariScriptParameters create(String params) throws ParameterInitException {
        SicariScriptParameters ssp;
        StringTokenizer toki;
        List<String> list;
        if (params != null) {
            list = new ArrayList<String>();
            toki = new StringTokenizer(params, DELIMITER);
            while (toki.hasMoreTokens()) {
                list.add(FileParameter.toFull(toki.nextToken().trim()));
            }
        } else {
            list = null;
        }
        ssp = new SicariScriptParameters(list);
        ssp.init();
        return ssp;
    }

    /**
     * Find the default script parameters.
     *
     * @return The default script parameters or <code>null</code> if it could
     *   not be found
     */
    public static SicariScriptParameters findDefault(SicariBaseDirectory sbase) {
        if (sbase == null) {
            return null;
        }
        try {
            return create(new File(sbase, "etc/" + DEFAULT_NAME).toString());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return The list of parameters
     */
    public List<String> getList() {
        return params_;
    }

    /**
     * Get a <code>String</code> representation of this
     * <code>SicariScriptParameters</code> object. If the parameters
     * contain <code>File</code> objects use either their full or
     * abbreviated form.
     *
     * @param full <code>true</code> will force full file names,
     *   <code>false</code> will force abbreviated file names
     * @return A <code>String</code> representation of this
     *   <code>SicariScriptParameters</code> object
     */
    protected String toString(boolean full) {
        Iterator<String> itr;
        StringBuffer strbuf;
        String next;
        strbuf = new StringBuffer();
        for (itr = params_.iterator(); itr.hasNext(); ) {
            next = itr.next();
            strbuf.append(full ? next : FileParameter.toShort(next));
            if (itr.hasNext()) {
                strbuf.append(DELIMITER);
            }
        }
        return strbuf.toString();
    }

    public boolean isFlawed() {
        return false;
    }

    public String toFullString() {
        return full_;
    }

    public String toShortString() {
        return abbr_;
    }

    /**
     * Get a <code>String</code> representation of this
     * <code>SicariScriptParameters</code> object using
     * abbreviated file names.
     *
     * @return A <code>String</code> representation of this
     *   <code>SicariScriptParameters</code> object
     */
    @Override
    public String toString() {
        return abbr_;
    }

    @Override
    public int hashCode() {
        return (params_.hashCode() + 31);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof SicariScriptParameters) {
            SicariScriptParameters ssp = (SicariScriptParameters) obj;
            return params_.equals(ssp.params_);
        }
        return false;
    }
}
