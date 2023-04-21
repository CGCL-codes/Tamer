package com.phloc.types.dyntypes.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.lang.ClassHelper;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.lang.ServiceLoaderBackport;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.state.EChange;
import com.phloc.types.dyntypes.IDynamicTypeRegistrarSPI;
import com.phloc.types.dyntypes.IDynamicValue;
import com.phloc.types.dyntypes.base.DynamicValueBigDecimal;
import com.phloc.types.dyntypes.base.DynamicValueBigInteger;
import com.phloc.types.dyntypes.base.DynamicValueBoolean;
import com.phloc.types.dyntypes.base.DynamicValueByte;
import com.phloc.types.dyntypes.base.DynamicValueCharacter;
import com.phloc.types.dyntypes.base.DynamicValueDouble;
import com.phloc.types.dyntypes.base.DynamicValueFloat;
import com.phloc.types.dyntypes.base.DynamicValueInteger;
import com.phloc.types.dyntypes.base.DynamicValueLocale;
import com.phloc.types.dyntypes.base.DynamicValueLong;
import com.phloc.types.dyntypes.base.DynamicValueShort;
import com.phloc.types.dyntypes.base.DynamicValueString;
import com.phloc.types.dyntypes.datetime.DynamicValueDateTime;
import com.phloc.types.dyntypes.datetime.DynamicValueLocalDate;
import com.phloc.types.dyntypes.datetime.DynamicValueLocalDateTime;
import com.phloc.types.dyntypes.datetime.DynamicValueLocalTime;
import com.phloc.types.dyntypes.phloc.DynamicValueMicroNode;

/**
 * This class manages the available dynamic types.
 * 
 * @author philip
 */
@ThreadSafe
public final class DynamicTypeRegistry {

    private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock();

    private static final Map<Class<?>, Class<? extends IDynamicValue>> s_aMap = new WeakHashMap<Class<?>, Class<? extends IDynamicValue>>();

    static {
        registerDynamicType(boolean.class, DynamicValueBoolean.class);
        registerDynamicType(Boolean.class, DynamicValueBoolean.class);
        registerDynamicType(byte.class, DynamicValueByte.class);
        registerDynamicType(Byte.class, DynamicValueByte.class);
        registerDynamicType(char.class, DynamicValueCharacter.class);
        registerDynamicType(Character.class, DynamicValueCharacter.class);
        registerDynamicType(double.class, DynamicValueDouble.class);
        registerDynamicType(Double.class, DynamicValueDouble.class);
        registerDynamicType(float.class, DynamicValueFloat.class);
        registerDynamicType(Float.class, DynamicValueFloat.class);
        registerDynamicType(int.class, DynamicValueInteger.class);
        registerDynamicType(Integer.class, DynamicValueInteger.class);
        registerDynamicType(long.class, DynamicValueLong.class);
        registerDynamicType(Long.class, DynamicValueLong.class);
        registerDynamicType(short.class, DynamicValueShort.class);
        registerDynamicType(Short.class, DynamicValueShort.class);
        registerDynamicType(String.class, DynamicValueString.class);
        registerDynamicType(BigDecimal.class, DynamicValueBigDecimal.class);
        registerDynamicType(BigInteger.class, DynamicValueBigInteger.class);
        registerDynamicType(IMicroNode.class, DynamicValueMicroNode.class);
        registerDynamicType(Locale.class, DynamicValueLocale.class);
        registerDynamicType(LocalDate.class, DynamicValueLocalDate.class);
        registerDynamicType(LocalTime.class, DynamicValueLocalTime.class);
        registerDynamicType(LocalDateTime.class, DynamicValueLocalDateTime.class);
        registerDynamicType(DateTime.class, DynamicValueDateTime.class);
        for (final IDynamicTypeRegistrarSPI aSPI : ServiceLoaderBackport.load(IDynamicTypeRegistrarSPI.class)) aSPI.registerDynamicTypes();
    }

    @PresentForCodeCoverage
    @SuppressWarnings("unused")
    private static final DynamicTypeRegistry s_aInstance = new DynamicTypeRegistry();

    private DynamicTypeRegistry() {
    }

    private static void _registerDynamicType(@Nonnull final Class<?> aClass, @Nonnull final Class<? extends IDynamicValue> aDynamicValueClass, final boolean bAllowOverwrite) {
        if (aClass == null) throw new NullPointerException("class");
        if (aDynamicValueClass == null) throw new NullPointerException("dynamicTypeClass");
        if (!ClassHelper.isInstancableClass(aDynamicValueClass)) throw new IllegalArgumentException("The passed dynamic type class must be public, instancable and needs a no-argument constructor: " + aDynamicValueClass);
        s_aRWLock.writeLock().lock();
        try {
            if (!bAllowOverwrite && s_aMap.containsKey(aClass)) throw new IllegalArgumentException("A dynamic value class is already registered for " + aClass);
            if (GlobalDebug.isDebugMode()) {
                final Class<?> aNativeClass = GenericReflection.newInstance(aDynamicValueClass).getNativeClass();
                if (!aClass.equals(aNativeClass) && !ClassHelper.getPrimitiveWrapperClass(aClass).equals(aNativeClass)) throw new IllegalArgumentException(aClass + " is different from native class " + aNativeClass);
            }
            s_aMap.put(aClass, aDynamicValueClass);
        } finally {
            s_aRWLock.writeLock().unlock();
        }
    }

    /**
   * Register a new dynamic type. If the dynamic type is already registered, an
   * exception is thrown.
   * 
   * @param aClass
   *        The class for which the dynamic type is to be registered. May not be
   *        <code>null</code>.
   * @param aDynamicValueClass
   *        The dynamic value class to be instantiated. May not be
   *        <code>null</code>.
   * @throws IllegalArgumentException
   *         If a dynamic type is already registered.
   */
    public static void registerDynamicType(@Nonnull final Class<?> aClass, @Nonnull final Class<? extends IDynamicValue> aDynamicValueClass) {
        _registerDynamicType(aClass, aDynamicValueClass, false);
    }

    /**
   * Overwrite an existing dynamic type mapping.
   * 
   * @param aClass
   *        The class for which the dynamic type is to be registered. May not be
   *        <code>null</code>.
   * @param aDynamicValueClass
   *        The dynamic value class to be instantiated. May not be
   *        <code>null</code>.
   */
    public static void overwriteDynamicType(@Nonnull final Class<?> aClass, @Nonnull final Class<? extends IDynamicValue> aDynamicValueClass) {
        _registerDynamicType(aClass, aDynamicValueClass, true);
    }

    /**
   * Change all dynamic type mappings that point to the old dynamic value class
   * with ones that point to the new dynamic value class.
   * 
   * @param aOldDynamicValueClass
   *        The old dynamic value class to be replaced. May not be
   *        <code>null</code>.
   * @param aNewDynamicValueClass
   *        The new dynamic value class to be set. May not be <code>null</code>.
   * @return EChange
   */
    @Nonnull
    public static EChange replaceDynamicType(@Nonnull final Class<? extends IDynamicValue> aOldDynamicValueClass, @Nonnull final Class<? extends IDynamicValue> aNewDynamicValueClass) {
        if (aOldDynamicValueClass == null) throw new NullPointerException("oldDynamicValueClass");
        if (aNewDynamicValueClass == null) throw new NullPointerException("newDynamicValueClass");
        if (aOldDynamicValueClass.equals(aNewDynamicValueClass)) return EChange.UNCHANGED;
        boolean bMatchFound = false;
        for (final Map.Entry<Class<?>, Class<? extends IDynamicValue>> aEntry : getAllRegisteredTypes().entrySet()) if (aEntry.getValue().equals(aOldDynamicValueClass)) {
            overwriteDynamicType(aEntry.getKey(), aNewDynamicValueClass);
            bMatchFound = true;
        }
        return EChange.valueOf(bMatchFound);
    }

    /**
   * Create a new dynamic type based on the given class. The initial value of
   * the dynamic type depends on the {@link IDynamicValue} implementation.
   * 
   * @param aClass
   *        The class to resolve. May not be <code>null</code>.
   * @return <code>null</code> if no dynamic type is registered for the passed
   *         class.
   */
    @Nullable
    public static IDynamicValue createNewDynamicValue(@Nonnull final Class<?> aClass) {
        Class<? extends IDynamicValue> aDynamicValueClass = null;
        for (final Class<?> aPossibleClass : ClassHelper.getClassHierarchy(aClass)) {
            s_aRWLock.readLock().lock();
            try {
                aDynamicValueClass = s_aMap.get(aPossibleClass);
                if (aDynamicValueClass != null) break;
            } finally {
                s_aRWLock.readLock().unlock();
            }
        }
        if (aDynamicValueClass == null) {
            return null;
        }
        return GenericReflection.newInstance(aDynamicValueClass);
    }

    /**
   * Create a new dynamic value for the passed value.
   * 
   * @param aValue
   *        The value for which the dynamic type is requested. May not be
   *        <code>null</code> because the class of the object must be
   *        resolvable.
   * @return <code>null</code> if no dynamic type is available for the given
   *         value.
   */
    @Nullable
    public static IDynamicValue createDynamicValue(@Nonnull final Object aValue) {
        if (aValue == null) throw new NullPointerException("value");
        final IDynamicValue aDynamicValue = createNewDynamicValue(aValue.getClass());
        if (aDynamicValue != null) aDynamicValue.setValue(aValue);
        return aDynamicValue;
    }

    @Nonnull
    @ReturnsMutableCopy
    public static Map<Class<?>, Class<? extends IDynamicValue>> getAllRegisteredTypes() {
        s_aRWLock.readLock().lock();
        try {
            return ContainerHelper.newMap(s_aMap);
        } finally {
            s_aRWLock.readLock().unlock();
        }
    }
}
