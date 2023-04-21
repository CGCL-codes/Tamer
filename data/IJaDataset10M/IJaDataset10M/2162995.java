package be.xios.mobile.project.webservice;

public interface GoogleWebservice extends java.rmi.Remote {

    public java.lang.Boolean login(java.lang.String login, java.lang.String passwd) throws java.rmi.RemoteException;

    public be.xios.mobile.project.webservice.Afspraak[] getEventById(java.lang.String calendarId) throws java.rmi.RemoteException, webservice.IOException;

    public be.xios.mobile.project.webservice.Afspraak[] getEventByDateId(java.lang.String calendarId, java.util.Calendar date) throws java.rmi.RemoteException, webservice.IOException;

    public be.xios.mobile.project.webservice.Coordinates getCoordinates(java.lang.String address) throws java.rmi.RemoteException;
}
