package net.sf.jttslite.support.help;

import java.util.Properties;

/**
 * Risolve gli identificatori delle risorse di help con dei target validi. Si
 * appoggia ai file di configurazione.
 * 
 * @author davide
 */
public class HelpResourcesResolver {

    /**
	 * La configurazione.
	 */
    private final Properties _properties;

    /**
	 * Costruttore.
	 * 
	 * @param p
	 */
    public HelpResourcesResolver(Properties p) {
        this._properties = p;
    }

    /**
	 * Risolve il valore della risorsa di help specificata. Cerca nel file di
	 * mappatura, altrimenti ritorna direttamente il valore della risorsa.
	 * 
	 * @param helpResource
	 *            la risorsa.
	 * @return il valore della risorsa di help specificata..
	 */
    public String resolveHelpID(HelpResource helpResource) {
        final String resourceValue = helpResource.getValue();
        return this._properties.getProperty(resourceValue, resourceValue);
    }
}
