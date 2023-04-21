package org.subrecord.repo.dao;

import static org.subrecord.Constants.PROPERTY_NAME_DOMAIN;
import static org.subrecord.Constants.PROPERTY_NAME_USER;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.subrecord.model.Record;
import org.subrecord.repo.exception.RepoException;
import org.subrecord.repo.index.FieldConstraint;
import org.subrecord.repo.model.Result;
import org.subrecord.repo.model.ScanResult;
import org.subrecord.repo.storage.Storage;

/**
 * Provides low-level access to general (non-system) data records.
 */
@Component
public class RecordDao extends AbstractDao {

    /** An instance of the logger */
    protected final Log LOG = LogFactory.getLog(getClass());

    /**
	 * Find all general data records owned by the supplied user.
	 * 
	 * @param user
	 *            Identifies the user whose records we want to list.
	 * @return A List of general data records.
	 * @throws RepoException
	 */
    public Result find(String user) throws RepoException {
        return repo.getStorage().query(getTable(), getClazz(), FieldConstraint.list(new FieldConstraint(PROPERTY_NAME_USER, user)));
    }

    public Result find(String user, String domain, Map<String, String> contraints) throws RepoException {
        return repo.getStorage().query(getTable(), getClazz(), FieldConstraint.list(contraints, new FieldConstraint(PROPERTY_NAME_USER, user), new FieldConstraint(PROPERTY_NAME_DOMAIN, domain)));
    }

    public Record get(String rowId) throws RepoException {
        return (Record) repo.getStorage().get(getTable(), rowId, getClazz());
    }

    public Record get(String rowId, String domain) throws RepoException {
        Record record = (Record) repo.getStorage().get(getTable(), rowId, getClazz());
        if (domain.equals(record.getDomain())) {
            return record;
        } else {
            throw new RepoException("No record in specified domain");
        }
    }

    public void update(String id, Map properties) throws RepoException {
        repo.getStorage().put(id, getTable(), properties);
    }

    @Override
    public String getTable() {
        return Storage.TABLE_NAME_ANY;
    }

    @Override
    public Class getClazz() {
        return Record.class;
    }

    public ScanResult scan(String domain, String expression) throws RepoException {
        return repo.getStorage().scan(getTable(), domain, expression, Record.class);
    }
}
