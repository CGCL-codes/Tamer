package org.openorb.orb.test.rmi.complex;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.BitSet;

/**
 * This interface provides complex test operations.
 *
 * @author Jerome Daniel
 * @author Michael Rumpf
 */
public interface RemoteComplex extends Remote {

    /**
     * Echo a String.
     *
     * @param val The String to be echoed.
     * @return The String instance.
     * @throws RemoteException When an error occurs.
     */
    String echo_string(String val) throws RemoteException;

    /**
     * Echo a Float.
     *
     * @param val The Float to be echoed.
     * @return The Float instance.
     * @throws RemoteException When an error occurs.
     */
    Float echo_float(Float val) throws RemoteException;

    /**
     * Echo a Double.
     *
     * @param val The Double to be echoed.
     * @return The Double instance.
     * @throws RemoteException When an error occurs.
     */
    Double echo_double(Double val) throws RemoteException;

    /**
     * Echo an Integer.
     *
     * @param val The Integer to be echoed.
     * @return The Integer instance.
     * @throws RemoteException When an error occurs.
     */
    Integer echo_int(Integer val) throws RemoteException;

    /**
     * Echo a Long.
     *
     * @param val The Long to be echoed.
     * @return The Long instance.
     * @throws RemoteException When an error occurs.
     */
    Long echo_long(Long val) throws RemoteException;

    /**
     * Echo a Vector.
     *
     * @param val The Vector to be echoed.
     * @return The Vector instance.
     * @throws RemoteException When an error occurs.
     */
    java.util.Vector echo_vector(java.util.Vector val) throws RemoteException;

    /**
     * Echo an Object.
     *
     * @param val The Object to be echoed.
     * @return The Object instance.
     * @throws RemoteException When an error occurs.
     */
    java.lang.Object echo_object(java.lang.Object val) throws RemoteException;

    /**
     * Return an Object.
     *
     * @return The Object instance.
     * @throws RemoteException When an error occurs.
     */
    java.lang.Object return_object() throws RemoteException;

    /**
     * Echo a Class.
     *
     * @param val The Class to be echoed.
     * @return The Class instance.
     * @throws RemoteException When an error occurs.
     */
    java.lang.Class echo_class0(java.lang.Class val) throws RemoteException;

    /**
     * Echo a lot of different types.
     *
     * @param th The holder of different types.
     * @return The TypeHolder instance.
     * @throws RemoteException When an error occurs.
     */
    TypeHolder echo_typeholder(TypeHolder th) throws RemoteException;

    /**
     * Return the Remote object.
     *
     * @return The Remote instance.
     * @throws RemoteException When an error occurs.
     */
    Remote echo_remote0() throws RemoteException;

    /**
     * Return the RemoteComplex object.
     *
     * @param val A RemoteComplex instance.
     * @return The RemoteComplex instance.
     * @throws RemoteException When an error occurs.
     */
    RemoteComplex echo_remote1(RemoteComplex val) throws RemoteException;

    /**
     * Return the RemoteComplex object.
     *
     * @return The RemoteComplex instance.
     * @throws RemoteException When an error occurs.
     */
    RemoteComplex echo_remote2() throws RemoteException;

    /**
     * Return the RemoteComplex object.
     *
     * @param val A RemoteComplex instance.
     * @throws RemoteException When an error occurs.
     */
    void echo_remote3(RemoteComplex val) throws RemoteException;

    /**
     * The marshalling of more complex objects does not work in RMIoverIIIOP version 1.2.0.
     * When passing an array of strings or a list of strings only the array and the list are
     * passed but not the elements in the array/list. This bug was twofold, first simple array
     * components and complex array components.
     *
     * @param val The array of Remote objects to be echoed.
     * @return The Remote object array.
     * @throws RemoteException When an error occurs.
     */
    Remote[] echo_remotearray0(Remote[] val) throws RemoteException;

    /**
     * Echo an array of more special Remote types.
     *
     * @param val The array of Remote objects to be echoed.
     * @return The RemoteComplex object array.
     * @throws RemoteException When an error occurs.
     */
    RemoteComplex[] echo_remotearray1(RemoteComplex[] val) throws RemoteException;

    /**
     * Return an array of more special Remote types.
     *
     * @return The array of more special Remote types.
     * @throws RemoteException When an error occurs.
     */
    RemoteComplex[] echo_remotearray2() throws RemoteException;

    /**
     * Send an array of more special Remote types.
     *
     * @param val The array of Remote objects send.
     * @throws RemoteException When an error occurs.
     */
    void echo_remotearray3(RemoteComplex[] val) throws RemoteException;

    /**
     * The following method signature created a stack overflow exception for
     * Java2Idl in version 1.2.0:
     *   org.omg.CORBA.Object getObject() throws RemoteException;
     * The Java2IDL spec says that org.omg.CORBA.Object should be mapped to
     * the IDL type Object.
     *
     * @param obj The CORBA object to be echoed.
     * @return The CORBA object instance.
     * @throws RemoteException When an error occurs.
     */
    org.omg.CORBA.Object echo_corbaobject(org.omg.CORBA.Object obj) throws RemoteException;

    /**
     * When passing an instance of SubArrayList to the method getSizeOfArrayList() a
     * CORBA MARSHAL exception was received. The reason was that UtilDelegateImpl tried
     * the different class loaders available and in one case (RMIClassloader) it did not
     * return the class when the loading was successful. Instead it run to the end of
     * the method where a ClassNotFoundException was thrown.
     *
     * @param val An Arraylist to be echoed.
     * @return The ArrayList instance.
     * @throws RemoteException When an error occurs.
     */
    java.util.ArrayList echo_collection(java.util.ArrayList val) throws RemoteException;

    /**
     * JavaToIdl failed passing objects derived from IDLEntity.
     *
     * @param any The CORBA Any type to be echoed.
     * @return The Any instance.
     * @throws RemoteException When an error occurs.
     */
    org.omg.CORBA.Any[] echo_any(org.omg.CORBA.Any[] any) throws RemoteException;

    org.omg.CORBA.Any echo_any(org.omg.CORBA.Any any) throws RemoteException;

    /**
     * JavaToIdl failed passing objects derived from IDLEntity.
     *
     * @param tc The CORBA TypeCode type to be echoed.
     * @return The TypeCode instance.
     * @throws RemoteException When an error occurs.
     */
    org.omg.CORBA.TypeCode echo_typecode(org.omg.CORBA.TypeCode tc) throws RemoteException;

    org.omg.CORBA.TypeCode[] echo_typecode(org.omg.CORBA.TypeCode[] tc) throws RemoteException;

    /**
     * JavaToIdl failed passing objects derived from IDLEntity other
     * than org.omg.CORBA.Any and org.omg.CORBA.TypeCode.
     *
     * @param ent The CORBA IDLEntity type to be echoed.
     * @return The IDLEntity instance.
     * @throws RemoteException When an error occurs.
     */
    IDLStruct echo_entity(IDLStruct ent) throws RemoteException;

    IDLStruct[] echo_entity(IDLStruct[] ents) throws RemoteException;

    /**
     * Test various serialization mechanisms.
     *
     * @param pft A class using the PutField mechanism.
     * @return The class using the PutField mechanism.
     * @throws RemoteException When an error occurs.
     */
    PutFieldTest echo_pft(PutFieldTest pft) throws RemoteException;

    /**
     * Test various serialization mechanisms.
     *
     * @param spft A class using the SerialPersistentField mechanism.
     * @return The class using the SerialPersistentField mechanism.
     * @throws RemoteException When an error occurs.
     */
    SerialPersistentFieldsTest echo_spft(SerialPersistentFieldsTest spft) throws RemoteException;

    /**
     * The Throwable was causing problems with the RMI-IIOP serialization engine
     * starting from JDK1.4 because the new implementation class uses its own
     * serialization mechanism. Therefore custom read/write methods guarantee
     * some backward compatibility to the serial stream format of eralier JDKs.
     * The problem here was that the class Throwable has only a writeObject()
     * method, but the RMIoverIIOP serialization engine needs both methods
     * readObject and writeObject.
     */
    Throwable echo_throwable(Throwable th) throws RemoteException;

    /**
     * The Throwable was causing problems with the RMI-IIOP serialization engine
     * starting from JDK1.4 because the new implementation class uses some
     * backward compatibility mode.
     * Internally a BigInteger is used. The BigInteger's implementation has changed
     * between JDK 1.3 and 1.4. In order to keep the serialized format equal the
     * JDK1.4 read/writeObject() methods do a conversion of the new internal member
     * to keep the serialized stream format equal to former JDKs.
     * This was causing problems with the RMIoverIIOP serialization engine.
     */
    java.math.BigDecimal echo_bigdecimal(java.math.BigDecimal bd) throws java.rmi.RemoteException;

    /**
     * This test case is to check whether the RMIoverIIOP serialization engine
     * does not stumble over a java.lang.Object array with remote references.
     * The problem is that these object references are untyped, which results
     * in a special handling when marshaling/demarshaling.
     */
    java.lang.Object[] echo_remoteobjectarray(Object[] val) throws RemoteException;

    /**
     * Test whether we can marshal a Serializable interface.
     */
    SerialItf echo_serialitf(SerialItf si) throws RemoteException;

    /**
     * Test whether BitSet can be marshaled correctly.
     */
    BitSet echo_bitset(BitSet bs) throws RemoteException;

    /**
     * Test whether Timestamp can be marshaled correctly.
     */
    Timestamp echo_timestamp(Timestamp ts) throws RemoteException;
}
