package sun.security.jca;

import java.util.*;
import java.security.Provider;
import java.security.Security;

/**
 * Collection of methods to get and set provider list. Also includes
 * special code for the provider list during JAR verification.
 *
 * @author  Andreas Sterbenz
 * @since   1.5
 */
public class Providers {

    private static final ThreadLocal<ProviderList> threadLists = new InheritableThreadLocal<ProviderList>();

    private static volatile int threadListsUsed;

    private static volatile ProviderList providerList;

    static {
        providerList = ProviderList.EMPTY;
        providerList = ProviderList.fromSecurityProperties();
    }

    private Providers() {
    }

    private static final String BACKUP_PROVIDER_CLASSNAME = "sun.security.provider.VerificationProvider";

    private static final String[] jarVerificationProviders = { "sun.security.provider.Sun", "sun.security.rsa.SunRsaSign", "sun.security.ec.SunEC", BACKUP_PROVIDER_CLASSNAME };

    public static Provider getSunProvider() {
        try {
            Class clazz = Class.forName(jarVerificationProviders[0]);
            return (Provider) clazz.newInstance();
        } catch (Exception e) {
            try {
                Class clazz = Class.forName(BACKUP_PROVIDER_CLASSNAME);
                return (Provider) clazz.newInstance();
            } catch (Exception ee) {
                throw new RuntimeException("Sun provider not found", e);
            }
        }
    }

    /**
     * Start JAR verification. This sets a special provider list for
     * the current thread. You MUST save the return value from this
     * method and you MUST call stopJarVerification() with that object
     * once you are done.
     */
    public static Object startJarVerification() {
        ProviderList currentList = getProviderList();
        ProviderList jarList = currentList.getJarList(jarVerificationProviders);
        return beginThreadProviderList(jarList);
    }

    /**
     * Stop JAR verification. Call once you have completed JAR verification.
     */
    public static void stopJarVerification(Object obj) {
        endThreadProviderList((ProviderList) obj);
    }

    /**
     * Return the current ProviderList. If the thread-local list is set,
     * it is returned. Otherwise, the system wide list is returned.
     */
    public static ProviderList getProviderList() {
        ProviderList list = getThreadProviderList();
        if (list == null) {
            list = getSystemProviderList();
        }
        return list;
    }

    /**
     * Set the current ProviderList. Affects the thread-local list if set,
     * otherwise the system wide list.
     */
    public static void setProviderList(ProviderList newList) {
        if (getThreadProviderList() == null) {
            setSystemProviderList(newList);
        } else {
            changeThreadProviderList(newList);
        }
    }

    /**
     * Get the full provider list with invalid providers (those that
     * could not be loaded) removed. This is the list we need to
     * present to applications.
     */
    public static synchronized ProviderList getFullProviderList() {
        ProviderList list = getThreadProviderList();
        if (list != null) {
            ProviderList newList = list.removeInvalid();
            if (newList != list) {
                changeThreadProviderList(newList);
                list = newList;
            }
            return list;
        }
        list = getSystemProviderList();
        ProviderList newList = list.removeInvalid();
        if (newList != list) {
            setSystemProviderList(newList);
            list = newList;
        }
        return list;
    }

    private static ProviderList getSystemProviderList() {
        return providerList;
    }

    private static void setSystemProviderList(ProviderList list) {
        providerList = list;
    }

    public static ProviderList getThreadProviderList() {
        if (threadListsUsed == 0) {
            return null;
        }
        return threadLists.get();
    }

    private static void changeThreadProviderList(ProviderList list) {
        threadLists.set(list);
    }

    /**
     * Methods to manipulate the thread local provider list. It is for use by
     * JAR verification (see above) and the SunJSSE FIPS mode only.
     *
     * It should be used as follows:
     *
     *   ProviderList list = ...;
     *   ProviderList oldList = Providers.beginThreadProviderList(list);
     *   try {
     *     // code that needs thread local provider list
     *   } finally {
     *     Providers.endThreadProviderList(oldList);
     *   }
     *
     */
    public static synchronized ProviderList beginThreadProviderList(ProviderList list) {
        if (ProviderList.debug != null) {
            ProviderList.debug.println("ThreadLocal providers: " + list);
        }
        ProviderList oldList = threadLists.get();
        threadListsUsed++;
        threadLists.set(list);
        return oldList;
    }

    public static synchronized void endThreadProviderList(ProviderList list) {
        if (list == null) {
            if (ProviderList.debug != null) {
                ProviderList.debug.println("Disabling ThreadLocal providers");
            }
            threadLists.remove();
        } else {
            if (ProviderList.debug != null) {
                ProviderList.debug.println("Restoring previous ThreadLocal providers: " + list);
            }
            threadLists.set(list);
        }
        threadListsUsed--;
    }
}
