package net.sf.amemailchecker.db.dao.impl;

import net.sf.amemailchecker.db.BaseJdbcObjectAwareDAO;
import net.sf.amemailchecker.db.common.DataBaseNegotiationException;
import net.sf.amemailchecker.db.common.annotation.*;
import net.sf.amemailchecker.db.dao.LetterDAO;
import net.sf.amemailchecker.mail.impl.letter.LetterImpl;
import net.sf.amemailchecker.mail.impl.letter.RawMessagePart;
import net.sf.amemailchecker.mail.impl.mailbox.LocalFolder;
import net.sf.amemailchecker.mail.model.Folder;
import net.sf.amemailchecker.mail.model.Letter;
import java.sql.ResultSet;
import java.util.List;
import java.util.logging.Level;

@TableDefinition(name = "LETTER", scheme = "AMC", columns = { @ColumnDefinition(name = "ID", id = 1, generated = true), @ColumnDefinition(name = "FOLDER_UUID", id = 2), @ColumnDefinition(name = "MESSAGE_UID", id = 3), @ColumnDefinition(name = "MESSAGE_ID", id = 4), @ColumnDefinition(name = "SUBJECT", id = 5), @ColumnDefinition(name = "DATE_RECEIVED", id = 6) })
@Selectors(selectors = { @Selector(name = "findByUid", parameters = { 2, 3 }), @Selector(name = "findAllByFolder", parameters = { 2 }), @Selector(name = "count", mode = SelectorMode.COUNT, parameters = { 2 }, resultSetRoutine = "count"), @Selector(name = "contains", mode = SelectorMode.COUNT, parameters = { 2, 3, 4 }, resultSetRoutine = "count") })
@Parameterizes(parameterizes = { @Parameterized(name = "delete", parameters = { 2, 3, 4 }), @Parameterized(name = "deleteByFolder", parameters = { 2 }) })
public class LetterDAOImpl extends BaseJdbcObjectAwareDAO<Letter, Long> implements LetterDAO {

    @Override
    public void move(Letter letter, Folder folder, Folder to) throws DataBaseNegotiationException {
        executeUpdate("UPDATE AMC.LETTER SET FOLDER_UUID = ? WHERE FOLDER_UUID = ? AND MESSAGE_UID = ? AND MESSAGE_ID = ?", ((LocalFolder) to).getUuid(), ((LocalFolder) folder).getUuid(), letter.getUid(), letter.getMessageID());
    }

    public boolean contains(Folder folder, Letter letter) {
        try {
            int result = (Integer) executeSelectQuery("contains", ((LocalFolder) folder).getUuid(), letter.getUid(), letter.getMessageID());
            return result == 1;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    public int count(Folder folder) {
        try {
            return (Integer) executeSelectQuery("count", ((LocalFolder) folder).getUuid());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return 0;
        }
    }

    private int count(ResultSet resultSet) throws Exception {
        resultSet.next();
        return resultSet.getInt(1);
    }

    @Override
    public void delete(Letter letter, Folder folder) throws DataBaseNegotiationException {
        executeQueryDelete("delete", ((LocalFolder) folder).getUuid(), letter.getUid(), letter.getMessageID());
    }

    @Override
    public void delete(Folder folder) throws DataBaseNegotiationException {
        executeQueryDelete("deleteByFolder", ((LocalFolder) folder).getUuid());
    }

    @Override
    public long save(Letter letter, Folder folder) throws DataBaseNegotiationException {
        return executeQueryInsert(((LocalFolder) folder).getUuid(), letter.getUid(), letter.getMessageID(), letter.getSubject(), letter.getDate());
    }

    @Override
    public Letter findByUid(String messageUid, Folder folder) throws DataBaseNegotiationException {
        return getSelectionOne("findByUid", ((LocalFolder) folder).getUuid(), messageUid);
    }

    @Override
    public List<Letter> findAll(Folder folder) throws DataBaseNegotiationException {
        return getSelectionList("findAllByFolder", ((LocalFolder) folder).getUuid());
    }

    @Override
    protected Letter processResultSetForObject(ResultSet resultSet) throws Exception {
        LetterImpl result = new LetterImpl(new RawMessagePart());
        result.setUid(resultSet.getString(3));
        result.setMessageID(resultSet.getString(4));
        result.setSubject(resultSet.getString(5));
        result.setDate(resultSet.getDate(6));
        return result;
    }
}
