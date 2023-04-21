package net.sourceforge.nconfigurations.contrib.mgmtp;

import net.sourceforge.nconfigurations.ConfigurationBuilder;
import net.sourceforge.nconfigurations.Configuration;
import net.sourceforge.nconfigurations.BuilderException;
import net.sourceforge.nconfigurations.ImmNodeTreeBuilder;
import net.sourceforge.nconfigurations.KeyFactory;
import net.sourceforge.nconfigurations.EmptyConfiguration;
import net.sourceforge.nconfigurations.convert.ConvertException;
import net.sourceforge.nconfigurations.convert.ConvertStrategy;
import java.util.Map;
import java.util.Iterator;

/**
 * A base class supporting the creation of configuration builders immitating the
 * behaviour of (the old) {@code com.mgmtp.config.Configuration}. This base
 * class implements variable substitution, the conversion of raw string values
 * to java objects, the creation of the configuration (tree) object and
 * delegates the task of loading the configuration data set (as key-type-value
 * tripples) to clients to subclasses.
 *
 * <p>Extending this class is a matter of implementing the method
 * {@link #loadConfigurationRecords(String)} which has to return a newly
 * allocated, unshared, and modifiable map of all the configuration settings to
 * be presented to the client as a configuration (tree) object. The conditions
 * to be met by the implementation are fully described in the javadoc of that
 * method.
 *
 * <p>The raw string values contained in the data set obtained by a call to
 * {@code loadConfigurationRecords} will underly a variable interpolation
 * process which replaces substrings in the form of "${some.variable.reference}"
 * with the value of the configuration datum referenced by
 * "some.variable.reference" in the same data set. This process is recursive but
 * will fail in case of cyclic references. Variable references that could not be
 * resolved are left in the raw string values as they are. This process is
 * applied to <i>all</i> raw string values in the loaded configuration records.
 *
 * <p>After the the variable substitution process the raw string values will be
 * converted according to their type using the convert strategy obtained via
 * {@link #getConvertStrategy()} which defaults to
 * {@link ComMgmtpConfigConfigurationConverters#NEARLY_EXACT}.
 * Any raw string value that is converted to {@code null} will be skipped and
 * effectively removed from the configuration data set.
 *
 * <p>After the string-to-java-object conversion on the retrieved configuration
 * data set the configuration (tree) object is created. This base class uses the
 * key factory obtained by {@link #getKeyFactory()} to split up keys into path
 * tokens which will determine the path to a configuration datum in the created
 * configuration. The key factory returned by {@link #getKeyFactory()} defauls
 * to the dot ('.') based instance.
 *
 * <p>Provided that the convert strategy returned by {@link #getConvertStrategy()}
 * converts raw value string exclusively to immutable java objects, a
 * configuration object created with a call to {@link #buildConfiguration(String)}
 * will be immutable and can be freely shared and accessed concurrently without
 * the need for explicit synchronization. A configuration created with this class
 * (or a subclass of it) will <i>not</i> contain {@code null}s as values.
 *
 * <p>This base class itself has no mutable state and enables the creation of
 * thread-safe configuration builders without the need for explicit
 * synchronization, however, this does <i>not</i> automatically apply to
 * subclasses. Unless subclasses provide this guarantee explicitely, clients
 * should assume thread-unsafety.
 *
 * @author Petr Novotník
 * @since 1.0
 */
public abstract class AbstractMgmConfigurationBuilder implements ConfigurationBuilder {

    /**
     * The application name of the globaly-defined configuration settings.
     * These settings are automaticaly inherited by all applications (unless
     * these settings are explicitly provided in the configuration set for a
     * particular application).
     */
    public static final String DEFAULT_APPNAME = "global";

    /**
     * Empty class constructor.
     */
    protected AbstractMgmConfigurationBuilder() {
    }

    /**
     * Factory method for retrieving the key factory to be used to break up
     * configuration setting key strings (as retrieved from the underlying data
     * source) to key paths identifying the configuration settings within a
     * configuration object built by {@link #buildConfiguration(String)}. The
     * default implementation returns a dot ('.') based key factory instance.
     *
     * @return the key factory to be used by {@code buildConfiguration}
     *          to interpret key strings; must not return {@code null} otherwise
     *          {@code buildConfiguration} will fail with an
     *          {@code IllegalStateException}
     */
    public KeyFactory getKeyFactory() {
        return KeyFactory.getInstance('.');
    }

    /**
     * Factory method for retrieving the convert strategy to be applied when
     * converting raw string values to java objects after variable substition
     * during a {@link #buildConfiguration(String) building a configuration}
     * object. The default implementation returns the
     * {@link ComMgmtpConfigConfigurationConverters#NEARLY_EXACT}.
     *
     * @return the convert strategy to be used by {@link #buildConfiguration(String)}
     *          to convert raw string values to java objects; must not return
     *          {@code null} otherwise {@code buildConfiguration} will fail
     *          with an {@code IllegalStateException}
     */
    public ConvertStrategy getConvertStrategy() {
        return ComMgmtpConfigConfigurationConverters.NEARLY_EXACT;
    }

    /**
     * {@inheritDoc}
     *
     * <p>This implementation performs the following steps in the given order:
     * <ol>
     *  <li>Call the factory methods {@link #getConvertStrategy()} and
     *      {@link #getKeyFactory()}</li>
     *  <li>Call {@link #loadConfigurationRecords(String)} to obtain the data
     *      set from which to create a configuration object.</li>
     *  <li>Perform variable substitution on the obtained data set.</li>
     *  <li>Convert the data set values to java objects using the previously
     *      obtained convert strategy.</li>
     *  <li>Construct a {@code Configuration} object using
     *      {@link net.sourceforge.nconfigurations.ImmNodeTreeBuilder#buildTree(String, net.sourceforge.nconfigurations.KeyFactory, java.util.Iterator)}
     *      and supplying the previously obtained key factory. This constructed
     *      configuration is then returned.</li>
     * </ol>
     *
     * @throws NullPointerException if {@code appname} is {@code null}
     * @throws BuilderException {@inheritDoc}
     */
    public final Configuration buildConfiguration(final String appname) throws BuilderException {
        if (appname == null) {
            throw new NullPointerException();
        }
        final ConvertStrategy converter = getConvertStrategy();
        if (converter == null) {
            throw new IllegalStateException("null convert strategy!");
        }
        final KeyFactory keyFactory = getKeyFactory();
        if (keyFactory == null) {
            throw new IllegalStateException("null key factory!");
        }
        final Map<String, TypeValueRecord> records = loadConfigurationRecords(appname);
        if (records == null) {
            throw new IllegalStateException("null configuration records!");
        }
        if (records.isEmpty()) {
            return EmptyConfiguration.getInstance();
        }
        final ObjectLookup<String, Object> configContext = new ObjectLookup<String, Object>() {

            public Object lookup(final String key) {
                final TypeValueRecord o = records.get(key);
                return o == null ? null : o.getValue();
            }

            public Object lookup(final String key, final Object _default) {
                final TypeValueRecord o = records.get(key);
                return o == null ? _default : o.getValue();
            }
        };
        final StringInterpolator si = new StringInterpolator(configContext);
        for (final Map.Entry<String, TypeValueRecord> e : records.entrySet()) {
            final TypeValueRecord rec = e.getValue();
            if (rec == null) {
                throw new IllegalStateException("null record for key '" + e.getKey() + '\'');
            }
            try {
                final String val = rec.getValue();
                if (val != null) {
                    rec.setValue(si.interpolate(val));
                }
            } catch (final StringInterpolator.CyclicReferenceException ex) {
                throw new BuilderException("Cyclic reference detected starting " + "at record with key description '" + rec.getKeyDescription() + '\'', ex);
            }
        }
        final Iterator<Map.Entry<String, TypeValueRecord>> recordsIter = records.entrySet().iterator();
        final Iterator<ImmNodeTreeBuilder.KeyValueAssoc> it = new Iterator<ImmNodeTreeBuilder.KeyValueAssoc>() {

            public boolean hasNext() {
                return recordsIter.hasNext();
            }

            public ImmNodeTreeBuilder.KeyValueAssoc next() {
                final Map.Entry<String, TypeValueRecord> e = recordsIter.next();
                assert e != null;
                final String key = e.getKey();
                final TypeValueRecord rec = e.getValue();
                try {
                    final Object o = converter.toObject(key, rec.getValue(), rec.getType());
                    return o == null ? null : new ImmNodeTreeBuilder.KeyValueAssoc(key, o);
                } catch (final ConvertException ex) {
                    throw new SneakingException(ex);
                }
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        try {
            return ImmNodeTreeBuilder.buildTree(appname, keyFactory, it);
        } catch (final SneakingException e) {
            throw new BuilderException(e.getCause());
        }
    }

    /**
     * Retrieves all the configuration settings out of which a configuration
     * (tree) is to be created.
     *
     * <p>This method is invoked by {@link #buildConfiguration(String)} to
     * obtain the configuration data set on which it will operate. Unless the
     * following conditions are met for the returned object the
     * {@code buildConfiguration} method is likely to fail in a runtime exception
     * or corrupt the configuration build procedure:
     * <ul>
     *  <li>The returned map must be unshared, modifiable and must not contain
     *      {@code null} keys nor {@code null} values. The returned map is not
     *      required to be thread-safe. A newly allocated {@code HashMap} is
     *      perfectly valid for example.</li>
     *  <li>The values contained in the returned map must be modifiable and
     *      must not be shared.</li>
     * </ul>
     *
     * The keys in the hash-map are not necessarily forced to correspond exactly
     * to the keys description obtained by
     * {@link TypeValueRecord#getKeyDescription()} of the corresponding value
     * objects in the returned map. {@code buildConfiguration} uses the keys
     * from the map to associate it with the corresponding object's
     * {@link TypeValueRecord#getType() type} and
     * {@link TypeValueRecord#getValue() raw string value}. The key description
     * obtained by {@link TypeValueRecord#getKeyDescription()} is exclusively
     * used for error reporting during the configuration build process.
     * Typically, the keys in the map are shortened by the specified
     * {@code appname} while the keys obtained through the value objects in the
     * map are containing the full key. The variable substitution process for
     * example is performed using the keys in the map &ndash; not the key
     * descriptions of the value objects in the map. It's the keys in the map
     * that will be broken up into path tokens which will identify values in
     * the configuration under construction &ndash; not the key descriptions of
     * the value objects in the map.
     *
     * @param appname the name of the application for which the configuration
     *         settings are to be retrieved from the underlying data source
     *
     * @return a newly allocated map representing all configuration settings
     *          out of which a new configuration object is to be created;
     *          must not return {@code null} otherwise
     *          {@code buildConfiguration} will fail with an
     *          {@code IllegalStateException}; must not contain {@code null}
     *          key nor {@code null} values
     *
     * @throws NullPointerException if {@code appname} is {@code null}
     * @throws BuilderException if loading/retrieving the requested
     *          configuration data set fails for some reason
     */
    protected abstract Map<String, TypeValueRecord> loadConfigurationRecords(final String appname) throws BuilderException;

    private static class SneakingException extends RuntimeException {

        SneakingException(final Throwable t) {
            super(t);
        }
    }
}
