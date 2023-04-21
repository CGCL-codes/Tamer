package org.emergent.antbite.savant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;

/**
 * <p>
 * This class is the abstract base class for all implementors
 * of the {@link URLBuilder} interface.
 * </p>
 *
 * @author Brian Pontarelli
 */
public abstract class AbstractURLBuilder implements URLBuilder {

    /**
     * Builds either the base URL specification for the given artifact by figuring
     * out the domain (either default or something from the mapping).
     *
     * @param   defaultDomain The default domain URL specification to use as the
     *          base URL
     * @param   mapping (optional) A file that contains group to URL mappings that
     *          can be used to override the defaultDomain base URL
     * @param   artifact The artifact to fetch the group from if mapping is not
     *          null
     * @return  The base URL
     */
    protected String makeBaseURLSpec(String defaultDomain, File mapping, Artifact artifact) throws SavantException {
        String domain = defaultDomain;
        if (mapping != null && mapping.exists() && mapping.isFile()) {
            try {
                FileInputStream fis = new FileInputStream(mapping);
                PropertyResourceBundle bundle = new PropertyResourceBundle(fis);
                domain = bundle.getString(artifact.getGroup());
            } catch (IOException ioe) {
                throw new SavantException(ioe);
            } catch (MissingResourceException mre) {
            }
        } else if (mapping != null) {
            throw new SavantException("Unable to locate mapping file [" + mapping.getAbsolutePath() + "]");
        }
        return domain;
    }
}
