package org.datanucleus.jdo;

import javax.jdo.PersistenceManagerFactory;
import javax.jdo.identity.ByteIdentity;
import javax.jdo.identity.CharIdentity;
import javax.jdo.identity.IntIdentity;
import javax.jdo.identity.LongIdentity;
import javax.jdo.identity.ObjectIdentity;
import javax.jdo.identity.ShortIdentity;
import javax.jdo.identity.StringIdentity;
import javax.jdo.spi.PersistenceCapable;

/**
 * Class providing names of common JDO classes to aid performance.
 */
public class JDOClassNameConstants {

    /** javax.jdo.identity.LongIdentity **/
    public static final String JAVAX_JDO_IDENTITY_LONG_IDENTITY = LongIdentity.class.getName();

    /** javax.jdo.identity.IntIdentity **/
    public static final String JAVAX_JDO_IDENTITY_INT_IDENTITY = IntIdentity.class.getName();

    /** javax.jdo.identity.StringIdentity **/
    public static final String JAVAX_JDO_IDENTITY_STRING_IDENTITY = StringIdentity.class.getName();

    /** javax.jdo.identity.CharIdentity **/
    public static final String JAVAX_JDO_IDENTITY_CHAR_IDENTITY = CharIdentity.class.getName();

    /** javax.jdo.identity.ByteIdentity **/
    public static final String JAVAX_JDO_IDENTITY_BYTE_IDENTITY = ByteIdentity.class.getName();

    /** javax.jdo.identity.ObjectIdentity **/
    public static final String JAVAX_JDO_IDENTITY_OBJECT_IDENTITY = ObjectIdentity.class.getName();

    /** javax.jdo.identity.ShortIdentity **/
    public static final String JAVAX_JDO_IDENTITY_SHORT_IDENTITY = ShortIdentity.class.getName();

    /** javax.jdo.PersistenceManagerFactory **/
    public static final String JAVAX_JDO_PersistenceManagerFactory = PersistenceManagerFactory.class.getName();

    /** javax.jdo.spi.PersistenceCapable **/
    public static final String JAVAX_JDO_SPI_PERSISTENCE_CAPABLE = PersistenceCapable.class.getName();

    /** org.datanucleus.jdo.JDOPersistenceManagerFactory **/
    public static final String JDOPersistenceManagerFactory = JDOPersistenceManagerFactory.class.getName();
}
