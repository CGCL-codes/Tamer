package Adler;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DataInserter extends Remote {

    int Send(String project, String semanticData, String baseURI, String dataLanguage) throws RemoteException;

    String getOntology() throws RemoteException;

    static final int SEND_OK = 0;

    static final int ERR_PROJECT_BUSY = -1;

    static final int ERR_FORMAT_INVALID = -2;

    static final int ERR_PROJECT_DOESNT_EXIST = -3;
}
