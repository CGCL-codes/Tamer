package visad;

import java.rmi.*;

/**
   RemoteDataReferenceImpl is VisAD remote adapter for DataReferenceImpl.<P>
*/
public class RemoteDataReferenceImpl extends RemoteThingReferenceImpl implements RemoteDataReference {

    /**
   * construct a RemoteDataReferenceImpl adapting the given
   * DataReferenceImpl
   * @param ref adpted DataReferenceImpl
   * @throws RemoteException an RMI error occurred
   */
    public RemoteDataReferenceImpl(DataReferenceImpl ref) throws RemoteException {
        super(ref);
    }

    /**
   * set this RemoteDataReferenceImpl to refer to given Data
   * @param d Data to be set (must be RemoteDataImpl)
   * @throws VisADException a VisAD error occurred
   * @throws RemoteException an RMI error occurred
   */
    public synchronized void setData(Data d) throws VisADException, RemoteException {
        if (d == null) {
            throw new ReferenceException("RemoteDataReferenceImpl: data " + "cannot be null");
        }
        if (AdaptedThingReference == null) {
            throw new RemoteVisADException("RemoteDataReferenceImpl.setData: " + "AdaptedThingReference is null");
        }
        if (d instanceof DataImpl) {
            ((DataReferenceImpl) AdaptedThingReference).setData(d);
        } else {
            ((DataReferenceImpl) AdaptedThingReference).adaptedSetData((RemoteData) d, (RemoteDataReference) this);
        }
    }

    /**
   * return referenced Data object, but if Data is a FieldImpl
   * return a RemoteFieldImpl referencing Data to avoid copying
   * entire FieldImpl between JVMs
   * @return referenced Data object, or null if none
   * @throws VisADException a VisAD error occurred
   * @throws RemoteException an RMI error occurred
   */
    public Data getData() throws VisADException, RemoteException {
        if (AdaptedThingReference == null) {
            throw new RemoteVisADException("RemoteDataReferenceImpl.getData: " + "AdaptedThingReference is null");
        }
        Data data = ((DataReferenceImpl) AdaptedThingReference).getData();
        if (data instanceof FieldImpl) {
            boolean return_copy = false;
            if (return_copy) {
                return data;
            } else {
                return new RemoteFieldImpl((FieldImpl) data);
            }
        } else {
            return data;
        }
    }

    /**
   * this is more efficient than getData().getType() for
   * RemoteDataReferences
   * @return the MathType of referenced Data object, or null if none;
   * @throws VisADException a VisAD error occurred
   * @throws RemoteException an RMI error occurred
   */
    public MathType getType() throws VisADException, RemoteException {
        if (AdaptedThingReference == null) {
            throw new RemoteVisADException("RemoteDataReferenceImpl.getType: " + "AdaptedThingReference is null");
        }
        return ((DataReferenceImpl) AdaptedThingReference).getData().getType();
    }
}
