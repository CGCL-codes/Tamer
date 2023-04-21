package net.sf.amemailchecker.db.dao.impl;

import net.sf.amemailchecker.db.BaseJdbcDAO;
import net.sf.amemailchecker.db.common.DataBaseNegotiationException;
import net.sf.amemailchecker.db.common.annotation.*;
import net.sf.amemailchecker.db.dao.POP3MarkedCacheDAO;
import net.sf.amemailchecker.mail.model.Account;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@TableDefinition(name = "POP3_MARKED_READ_CACHE", scheme = "AMC", columns = { @ColumnDefinition(name = "ACCOUNT_UID", id = 1), @ColumnDefinition(name = "MESSAGE_UID", id = 2) })
@Parameterizes(parameterizes = { @Parameterized(parameters = { 1 }, name = "deleteByAccount"), @Parameterized(parameters = { 1, 2 }, name = "deleteTarget") })
@Selectors(selectors = { @Selector(name = "contains", parameters = { 1, 2 }, mode = SelectorMode.COUNT, resultSetRoutine = "contains"), @Selector(name = "selectAllByAccount", parameters = { 1 }, mode = SelectorMode.PARTIALLY, selection = { 2 }, resultSetRoutine = "selectAllByAccount") })
public class POP3MarkedCacheDAOImpl extends BaseJdbcDAO implements POP3MarkedCacheDAO {

    @Override
    public void insert(Account account, String... identifiers) throws DataBaseNegotiationException {
        for (String identifier : identifiers) executeQueryInsert(account.getUuid(), identifier);
    }

    @Override
    public void remove(Account account) throws DataBaseNegotiationException {
        executeQueryDelete("deleteByAccount", account.getUuid());
    }

    @Override
    public void remove(Account account, String... identifiers) throws DataBaseNegotiationException {
        try {
            for (String identifier : identifiers) executeQueryDelete("deleteTarget", account.getUuid(), identifier);
        } catch (Exception e) {
            throw new DataBaseNegotiationException(e);
        }
    }

    @Override
    public boolean contains(Account account, String identifier) throws DataBaseNegotiationException {
        return (Boolean) executeSelectQuery("contains", account.getUuid(), identifier);
    }

    private boolean contains(ResultSet resultSet) throws Exception {
        resultSet.first();
        return resultSet.getInt(1) == 1;
    }

    public List<String> selectAllByAccount(Account account) throws DataBaseNegotiationException {
        return (List<String>) executeSelectQuery("selectAllByAccount", account.getUuid());
    }

    private List<String> selectAllByAccount(ResultSet resultSet) throws Exception {
        List<String> result = new ArrayList<String>();
        while (resultSet.next()) {
            result.add(resultSet.getString(1));
        }
        return result;
    }
}
