package com.phloc.commons.jmx;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.management.JMException;
import javax.management.ObjectName;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.exceptions.LoggedRuntimeException;
import com.phloc.commons.lang.CGStringHelper;
import com.phloc.commons.string.StringHelper;

@Immutable
public final class ObjectNameUtils {

    @SuppressWarnings("unused")
    @PresentForCodeCoverage
    private static final ObjectNameUtils s_aInstance = new ObjectNameUtils();

    private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock();

    private static String s_sDefaultJMXDomain = CJMX.PHLOC_JMX_DOMAIN;

    private ObjectNameUtils() {
    }

    /**
   * Set the default JMX domain
   * 
   * @param sDefaultJMXDomain
   *        The new JMX domain. May neither be <code>null</code> nor empty nor
   *        may it contains ":" or " "
   */
    public static void setDefaultJMXDomain(@Nonnull @Nonempty final String sDefaultJMXDomain) {
        if (StringHelper.hasNoText(sDefaultJMXDomain)) throw new IllegalArgumentException("defaultJMXDomain is empty");
        if (sDefaultJMXDomain.indexOf(':') >= 0 || sDefaultJMXDomain.indexOf(' ') >= 0) throw new IllegalArgumentException("defaultJMXDomain contains invalid chars: " + sDefaultJMXDomain);
        s_aRWLock.writeLock().lock();
        try {
            s_sDefaultJMXDomain = sDefaultJMXDomain;
        } finally {
            s_aRWLock.writeLock().unlock();
        }
    }

    /**
   * @return The default JMX domain to be used for {@link ObjectName} instances
   */
    @Nonnull
    @Nonempty
    public static String getDefaultJMXDomain() {
        s_aRWLock.readLock().lock();
        try {
            return s_sDefaultJMXDomain;
        } finally {
            s_aRWLock.readLock().unlock();
        }
    }

    @Nonnull
    public static ObjectName create(@Nonnull @Nonempty final Hashtable<String, String> aParams) {
        if (ContainerHelper.isEmpty(aParams)) throw new IllegalArgumentException("JMX objectName parameters may not be empty!");
        try {
            return new ObjectName(getDefaultJMXDomain(), aParams);
        } catch (final JMException ex) {
            throw LoggedRuntimeException.newException("Failed to create ObjectName with parameter " + aParams, ex);
        }
    }

    @Nonnull
    public static ObjectName create(@Nonnull @Nonempty final Map<String, String> aParams) {
        if (ContainerHelper.isEmpty(aParams)) throw new IllegalArgumentException("JMX objectName parameters may not be empty!");
        return create(new Hashtable<String, String>(aParams));
    }

    /**
   * Create a clean property value applicable for an {@link ObjectName} property
   * value by replacing the special chars ":" and "," with "." and "//" with
   * "__". If the input value contains a blank, the quotes value is returned.
   * 
   * @param sPropertyValue
   *        The original property value. May not be <code>null</code>.
   * @return The modified property value applicable for {@link ObjectName}.
   * @see ObjectName#quote(String)
   */
    @Nonnull
    public static String getCleanPropertyValue(@Nonnull final String sPropertyValue) {
        if (sPropertyValue.indexOf(' ') != -1) return ObjectName.quote(sPropertyValue);
        return sPropertyValue.replace(':', '.').replace(',', '.').replace("//", "__");
    }

    /**
   * Create a standard {@link ObjectName} using the default domain and only the
   * "type" property. The type property is the class local name of the specified
   * object.
   * 
   * @param aObj
   *        The object from which the name is to be created.
   * @return The non-<code>null</code> {@link ObjectName}.
   */
    @Nonnull
    public static ObjectName createWithDefaultProperties(@Nonnull final Object aObj) {
        if (aObj == null) throw new NullPointerException("object");
        final Hashtable<String, String> aParams = new Hashtable<String, String>();
        aParams.put(CJMX.PROPERTY_TYPE, CGStringHelper.getClassLocalName(aObj));
        return create(aParams);
    }

    /**
   * Create a standard {@link ObjectName} using the default domain and the
   * "type" and "name" properties. The type property is the class local name of
   * the specified object.
   * 
   * @param aObj
   *        The object from which the name is to be created.
   * @param sName
   *        The value of the "name" JMX property
   * @return The non-<code>null</code> {@link ObjectName}.
   */
    @Nonnull
    public static ObjectName createWithDefaultProperties(@Nonnull final Object aObj, @Nonnull final String sName) {
        if (aObj == null) throw new NullPointerException("object");
        if (sName == null) throw new NullPointerException("name");
        final Hashtable<String, String> aParams = new Hashtable<String, String>();
        aParams.put(CJMX.PROPERTY_TYPE, CGStringHelper.getClassLocalName(aObj));
        aParams.put(CJMX.PROPERTY_NAME, getCleanPropertyValue(sName));
        return create(aParams);
    }
}
