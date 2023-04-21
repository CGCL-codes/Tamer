package de.barmenia.dms.cs.interfaces;

/**
* Remote interface for StatelessContentservice.
*/
public interface StatelessContentservice extends de.barmenia.dms.cs.interfaces.MasterSession {

    public int addiere(int a, int b) throws java.rmi.RemoteException;

    public int subtrahiere(int a, int b) throws java.rmi.RemoteException;

    /**
	* F�r Testzwecke hat es sich bewährt in jede Session Bean eine ping-Methode einzuf�gen.
	*/
    public java.lang.String ping(java.lang.String message) throws java.rmi.RemoteException;
}
