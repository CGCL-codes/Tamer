package net.teqlo.db.remote;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import net.teqlo.TeqloException;
import net.teqlo.bus.messages.userdocument.GetAllKeys;
import net.teqlo.bus.messages.userdocument.GetAllRecords;
import net.teqlo.bus.messages.userdocument.GetContentAsString;
import net.teqlo.bus.messages.userdocument.GetRecord;
import net.teqlo.bus.messages.userdocument.GetSubjectRecords;
import net.teqlo.bus.messages.userdocument.InsertRecord;
import net.teqlo.bus.messages.userdocument.RemoveRecord;
import net.teqlo.bus.messages.userdocument.SetContent;
import net.teqlo.bus.messages.userdocument.UpdateRecord;
import net.teqlo.bus.messages.userdocument.XQuery;
import net.teqlo.db.User;
import net.teqlo.db.impl.UserDocumentImpl;
import net.teqlo.xml.TeqloXmlValue;

public class RemoteUserDocument extends UserDocumentImpl {

    private RemoteXmlDatabase database;

    private String userFqn;

    private String content = null;

    public RemoteUserDocument(RemoteXmlDatabase database, String fqn, User user) {
        super(fqn);
        this.database = database;
        this.userFqn = user.getUserFqn();
    }

    public String getRecord(UUID key) throws TeqloException {
        return (String) database.requestReply(new GetRecord(userFqn, getDocumentFqn(), key));
    }

    public synchronized UUID[] insertRecords(String parentXPath, String[] records) throws TeqloException {
        content = null;
        return (UUID[]) database.requestReply(new InsertRecord(userFqn, getDocumentFqn(), parentXPath, records));
    }

    /**
	 * 
	 */
    public UUID[] getAllKeys() throws TeqloException {
        return (UUID[]) database.requestReply(new GetAllKeys(userFqn, getDocumentFqn()));
    }

    /**
	 * This implementation is a single fetch and iterates over the entire document.
	 * I really want to use generics to make these bus methods typesafe...there are nasty casts all over it!
	 * TODO Make this more effective for large documents by buffering groups of records
	 */
    @SuppressWarnings("unchecked")
    public Collection<String> getAllRecords() throws TeqloException {
        Collection<String> allRecords = (Vector<String>) database.requestReply(new GetAllRecords(userFqn, getDocumentFqn()));
        return allRecords;
    }

    @SuppressWarnings("unchecked")
    public Collection<String> getSubjectRecords(String linkPredicate, UUID objectRecord, boolean deep) throws TeqloException {
        Collection<String> subjectRecords = (Vector<String>) database.requestReply(new GetSubjectRecords(userFqn, getDocumentFqn(), linkPredicate, objectRecord, deep));
        return subjectRecords;
    }

    public synchronized void removeRecords(UUID[] keys) throws TeqloException {
        content = null;
        database.requestReply(new RemoveRecord(userFqn, getDocumentFqn(), keys));
    }

    public synchronized void setContent(String content) throws TeqloException {
        this.content = content;
        database.requestReply(new SetContent(userFqn, getDocumentFqn(), content));
    }

    public synchronized void updateRecords(UUID[] keys, String[] records) throws TeqloException {
        content = null;
        database.requestReply(new UpdateRecord(userFqn, getDocumentFqn(), keys, records));
    }

    public synchronized String getContentAsString() throws TeqloException {
        if (content == null) content = (String) database.requestReply(new GetContentAsString(userFqn, getDocumentFqn()));
        return content;
    }

    @SuppressWarnings("unchecked")
    public Vector<TeqloXmlValue> xquery(String xquery, Map<String, TeqloXmlValue> xqueryVars) throws TeqloException {
        return (Vector<TeqloXmlValue>) database.requestReply(new XQuery(userFqn, getDocumentFqn(), xquery, xqueryVars));
    }

    public synchronized void flushContent() {
        content = null;
    }
}
