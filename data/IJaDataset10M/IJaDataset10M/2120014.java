package net.sf.epfe.core.model;

import java.util.Locale;

public interface II18NBundleDescriptor {

    /**
	 * TODO comment
	 * 
	 * @return
	 */
    public abstract String getBundleFileDescription();

    /**
	 * The file extension of the bundle resource. (usagely ".properties")
	 * 
	 * @return
	 */
    public abstract String getExtension();

    /**
	 * The locale of the bundle resource.
	 * 
	 * @return <code>null</code>, if there was no locales data, the {@link Locale}-object otherwise.
	 *         <p>
	 *         The default Locale is equal to <code>Locale("")</code>.
	 */
    public abstract Locale getLocale();

    /**
	 * The base bundle name that can be used for grouping bundles to a set.
	 * 
	 * @return
	 */
    public abstract String getName();

    /**
	 * TODO comment
	 * 
	 * @param aExtension
	 */
    public abstract void setExtension(String aExtension);

    /**
	 * TODO comment
	 * 
	 * @param aLocale
	 */
    public abstract void setLocale(Locale aLocale);

    /**
	 * TODO comment
	 * 
	 * @param aName
	 */
    public abstract void setName(String aName);
}
