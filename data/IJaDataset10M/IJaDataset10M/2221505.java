package be.vds.jtbdive.client.core.businessdelegate;

import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DocumentFormat;
import be.vds.jtbdive.core.core.LogBookMeta;
import java.util.List;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.integration.businessdelegate.interfaces.LogBookBusinessDelegate;
import be.vds.jtbdive.core.util.ObjectSerializer;
import be.vds.jtbdive.persistence.core.dao.interfaces.DaoFactory;

public class DirectLogBookBusinessDelegate extends LogBookBusinessDelegate {

    public DirectLogBookBusinessDelegate() {
    }

    @Override
    public LogBook findLogBook(long id) throws DataStoreException {
        return DaoFactory.getFactory().createLogBookDAO().findLogBook(id);
    }

    @Override
    public List<LogBook> findLogBookNames() throws DataStoreException {
        return DaoFactory.getFactory().createLogBookDAO().findLogBookNames();
    }

    @Override
    public void initialize() {
    }

    @Override
    public LogBook saveLogBook(LogBook logBook) throws DataStoreException {
        LogBook lb = (LogBook) ObjectSerializer.cloneObject(logBook);
        return DaoFactory.getFactory().createLogBookDAO().saveLogBook(lb);
    }

    @Override
    public Dive saveDive(Dive dive, long logbookId) throws DataStoreException {
        Dive lb = (Dive) ObjectSerializer.cloneObject(dive);
        return DaoFactory.getFactory().createLogBookDAO().saveDive(lb, logbookId);
    }

    @Override
    public Dive reloadDive(long diveId, long logbookId) throws DataStoreException {
        Dive lb = DaoFactory.getFactory().createLogBookDAO().reloadDive(diveId, logbookId);
        return (Dive) ObjectSerializer.cloneObject(lb);
    }

    @Override
    public LogBookMeta saveLogBookMeta(LogBookMeta lb) throws DataStoreException {
        LogBookMeta m = (LogBookMeta) ObjectSerializer.cloneObject(lb);
        return DaoFactory.getFactory().createLogBookDAO().saveLogBookMeta(m);
    }

    @Override
    public Dive deleteDive(Dive dive, long logbookId) throws DataStoreException {
        Dive lb = (Dive) ObjectSerializer.cloneObject(dive);
        return DaoFactory.getFactory().createLogBookDAO().deleteDive(lb, logbookId);
    }

    @Override
    public List<Dive> saveDives(List<Dive> dives, long logbookId) throws DataStoreException {
        List<Dive> lb = (List<Dive>) ObjectSerializer.cloneObject(dives);
        return DaoFactory.getFactory().createLogBookDAO().saveDives(lb, logbookId);
    }

    public byte[] loadDocumentContent(long documentId, DocumentFormat format) throws DataStoreException {
        return DaoFactory.getFactory().createLogBookDAO().loadDocumentContent(documentId, format);
    }
}
