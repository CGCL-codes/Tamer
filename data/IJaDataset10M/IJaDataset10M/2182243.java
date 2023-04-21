package amibasearch;

import java.rmi.*;

public interface SearchUnion extends Remote {

    public SearchResult[] search(String lang, String dincl, String dexcl, String region, WordFilter wf[], DateRange range, PageSize size, FileFormat format, String pc, String subject, String source, String blockoffensive, boolean sitecollapse) throws SearchException, RemoteException;

    public SearchResult[] similar(String url) throws SearchException, RemoteException;

    public SearchResult[] linkto(String url) throws SearchException, RemoteException;
}
