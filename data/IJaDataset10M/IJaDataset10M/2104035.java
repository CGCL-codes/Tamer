package net.sf.jeckit.verifying;

import java.util.Locale;
import java.util.Set;

/**
 * @author julian
 *
 */
public interface NamedEntitiesGuesserInterface {

    public Set recognizeNamedEntities(String string, Locale locale);
}
