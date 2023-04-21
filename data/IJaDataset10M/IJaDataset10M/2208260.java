package org.openoffice.embed.oochem;

import com.sun.star.uno.XComponentContext;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.lib.uno.helper.WeakBase;

/**
 *
 * @author kostya
 */
public final class JmolEmbeddedObjectFactory extends WeakBase implements com.sun.star.lang.XServiceInfo, com.sun.star.embed.XEmbedObjectFactory {

    private final XComponentContext m_xContext;

    private static final String m_implementationName = JmolEmbeddedObjectFactory.class.getName();

    private static final String[] m_serviceNames = { JmolConstants.JMOL_EMBEDDED_OBJECT_SERVICE_NAME };

    private static final byte[] m_classID = { 0x37, (byte) 0xD0, (byte) 0x95, 0x6C, 0x0D, (byte) 0xD6, 0x4E, 0x6B, (byte) 0xA6, (byte) 0xC5, 0x33, (byte) 0xEB, 0x7D, 0x3E, 0x31, (byte) 0x9A };

    /**
     *
     * @param context
     */
    public JmolEmbeddedObjectFactory(XComponentContext context) {
        m_xContext = context;
    }

    /**
     *
     * @param sImplementationName
     * @return
     */
    public static XSingleComponentFactory __getComponentFactory(String sImplementationName) {
        XSingleComponentFactory xFactory = null;
        if (sImplementationName.equals(m_implementationName)) {
            xFactory = Factory.createComponentFactory(JmolEmbeddedObjectFactory.class, m_serviceNames);
        }
        return xFactory;
    }

    /**
     *
     * @param xRegistryKey
     * @return
     */
    public static boolean __writeRegistryServiceInfo(XRegistryKey xRegistryKey) {
        return Factory.writeRegistryServiceInfo(m_implementationName, m_serviceNames, xRegistryKey);
    }

    /**
     *
     * @return
     */
    @Override
    public String getImplementationName() {
        return m_implementationName;
    }

    /**
     *
     * @param sService
     * @return
     */
    @Override
    public boolean supportsService(String sService) {
        int len = m_serviceNames.length;
        for (int i = 0; i < len; i++) {
            if (sService.equals(m_serviceNames[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public String[] getSupportedServiceNames() {
        return m_serviceNames;
    }

    /**
     * creates a new object and transport parameters for persistent
     * initialization.
     *
     * This method can be used to have a full control over persistence
     * initialization of a object.
     *
     * If the service implementation does not support XEmbedObjectCreator
     * interface, it must accept the empty aClassID parameter in case of
     * loading from existing entry.
     *
     * @param aClassID              the class id of the new object
     * @param sClassName            the class name of the new object
     * @param xStorage              a parent storage the entry should be created/opened in
     * @param sEntName              a name for the entry
     * @param nEntryConnectionMode  a mode in which the object should be initialized from entry
     *                              can take values from EntryInitModes constant set
     * @param aArgs                 optional parameters for the embedded document persistence initialization,
     *                              see also ::com::sun::star::document::MediaDescriptor
     * @param aObjectArgs           optional parameters for the object persistence initialization,
     *                              see also EmbeddedObjectDescriptor
     * @return
     * @throws com.sun.star.lang.IllegalArgumentException  one of arguments is illegal
     * @throws com.sun.star.io.IOException  in case of io problems during opening\creation
     * @throws com.sun.star.uno.Exception  in case of other problems
     */
    @Override
    public Object createInstanceUserInit(byte[] aClassID, String sClassName, com.sun.star.embed.XStorage xStorage, String sEntName, int nEntryConnectionMode, com.sun.star.beans.PropertyValue[] aArgs, com.sun.star.beans.PropertyValue[] aObjectArgs) throws com.sun.star.lang.IllegalArgumentException, com.sun.star.io.IOException, com.sun.star.uno.Exception {
        if (xStorage == null || sEntName == null || sEntName.length() == 0) {
            throw new com.sun.star.lang.IllegalArgumentException();
        }
        if (nEntryConnectionMode == com.sun.star.embed.EntryInitModes.DEFAULT_INIT) {
            if (aClassID != null && aClassID.length != 0) {
                if (aClassID.length != m_classID.length) {
                    throw new com.sun.star.lang.IllegalArgumentException();
                }
                for (int i = 0; i < aClassID.length; i++) {
                    if (aClassID[i] != m_classID[i]) {
                        throw new com.sun.star.lang.IllegalArgumentException();
                    }
                }
            } else if (!xStorage.hasByName(sEntName)) {
                throw new com.sun.star.lang.IllegalArgumentException();
            }
        } else if (nEntryConnectionMode == com.sun.star.embed.EntryInitModes.TRUNCATE_INIT) {
            if (aClassID.length != m_classID.length) {
                throw new com.sun.star.lang.IllegalArgumentException();
            }
            for (int i = 0; i < m_classID.length; i++) {
                if (aClassID[i] != m_classID[i]) {
                    throw new com.sun.star.lang.IllegalArgumentException();
                }
            }
        }
        JmolEmbeddedObject aObject = new JmolEmbeddedObject(m_xContext, m_classID);
        aObject.setPersistentEntry(xStorage, sEntName, nEntryConnectionMode, aArgs, aObjectArgs);
        return aObject;
    }
}
