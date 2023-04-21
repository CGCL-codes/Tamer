package net.sf.rcpforms.experimenting.rcp_base.dnd;

import java.util.Iterator;
import java.util.Set;
import net.sf.rcpforms.experimenting.java.base.ReflectionUtil;

public class ReferenceClassRCPTransferPool extends AbstractClassRCPTransferPool {

    public static IRCPDnDTransferType getRCPTranferType(final Class<?> type) {
        return getInstance().getRCPTranfer(type);
    }

    /**
	 * Lists recursively the type and its super-classes and all super-interfaces
	 * into an Array of <code>Class&lt;?&gt;</code>. Classes in <code>ignoreClasses</code>
	 * are not added and not further recursed.
	 * 
	 * <p>
	 * @see ReflectionUtil#flattenClassHierarchie(Class, boolean, boolean, Class...)
	 */
    public static IRCPDnDTransferType[] getRCPTranferTypeHierarchie(final Class<?> type, final boolean includeInterfaces, final boolean recurseInterfaces, final Class<?>... ignoreClasses) {
        final Set<Class<?>> flattened = ReflectionUtil.flattenClassHierarchie(type, includeInterfaces, recurseInterfaces, ignoreClasses);
        final IRCPDnDTransferType[] result = new IRCPDnDTransferType[flattened.size()];
        int index = 0;
        final Iterator<Class<?>> iterator = flattened.iterator();
        while (iterator.hasNext() && index < result.length) {
            result[index++] = getRCPTranferType(iterator.next());
        }
        return result;
    }

    /**
	 * Gibt die Singleton-Instanz zurueck.
	 * @return die Singleton-Instanz 
	 */
    private static ReferenceClassRCPTransferPool getInstance() {
        if (s_instance == null) {
            s_instance = new ReferenceClassRCPTransferPool();
        }
        return s_instance;
    }

    /** Die Singletoninstanz. */
    private static ReferenceClassRCPTransferPool s_instance = null;

    /**
	 * Konstruktor
	 */
    private ReferenceClassRCPTransferPool() {
        super();
    }

    @Override
    protected IRCPDnDTransferType fabTransfer(final Class<?> type) {
        return new ReferenceRCPTransfer(type);
    }
}
