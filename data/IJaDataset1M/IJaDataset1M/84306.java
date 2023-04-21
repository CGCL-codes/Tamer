package com.tdcs.lords.store;

import com.tdcs.appt.store.AppointmentServer;
import com.tdcs.docs.common.DocServer;
import com.tdcs.lords.store.data.MedHxServer;
import com.tdcs.lords.store.data.PatientServer;
import com.tdcs.lords.store.data.RxServer;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.security.auth.Subject;

/**
 *
 * @author david
 */
public interface LordsServer extends Remote {

    public Subject login(String name, char[] pass) throws RemoteException;

    public PatientServer getPatientServer(Subject subject) throws RemoteException;

    public DocServer getDocServer(Subject subject) throws RemoteException;

    public MedHxServer getMedHxServer(Subject subject) throws RemoteException;

    public RxServer getRxServer(Subject subject) throws RemoteException;

    public AppointmentServer getAppointmentServer(Subject subject) throws RemoteException;

    public void logout(Subject subject) throws RemoteException;
}
