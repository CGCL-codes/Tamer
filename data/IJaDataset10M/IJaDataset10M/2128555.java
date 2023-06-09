package org.springframework.jdbc.support;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

/** 
 * An implementation of the KeyHolder interface, to be used for holding
 * auto-generated keys as potentially returned by JDBC insert statements.
 *
 * <p>Create an instance of this class for each insert operation, and pass
 * it to the corresponding JdbcTemplate or SqlUpdate methods.
 *
 * @author Thomas Risberg
 * @since 1.1
 * @see org.springframework.jdbc.core.JdbcTemplate
 * @see org.springframework.jdbc.object.SqlUpdate
 */
public class GeneratedKeyHolder implements KeyHolder {

    private final List keyList;

    /**
	 * Create a new GeneratedKeyHolder with a default list.
	 */
    public GeneratedKeyHolder() {
        this.keyList = new LinkedList();
    }

    /**
	 * Create a new GeneratedKeyHolder with a given list.
	 * @param keyList a list to hold maps of keys
	 */
    public GeneratedKeyHolder(List keyList) {
        this.keyList = keyList;
    }

    public Number getKey() throws InvalidDataAccessApiUsageException, DataRetrievalFailureException {
        if (this.keyList.size() == 0) {
            return null;
        }
        if (this.keyList.size() > 1 || ((Map) this.keyList.get(0)).size() > 1) {
            throw new InvalidDataAccessApiUsageException("The getKey method should only be used when a single key is returned.  " + "The current key entry contains multiple keys: " + this.keyList);
        }
        Iterator keyIter = ((Map) this.keyList.get(0)).values().iterator();
        if (keyIter.hasNext()) {
            Object key = keyIter.next();
            if (!(key instanceof Number)) {
                throw new DataRetrievalFailureException("The generated key is not of a supported numeric type. " + "Unable to cast [" + key.getClass().getName() + "] to [" + Number.class.getName() + "]");
            }
            return (Number) key;
        } else {
            throw new DataRetrievalFailureException("Unable to retrieve the generated key. " + "Check that the table has an identity column enabled.");
        }
    }

    public Map getKeys() throws InvalidDataAccessApiUsageException {
        if (this.keyList.size() == 0) {
            return null;
        }
        if (this.keyList.size() > 1) throw new InvalidDataAccessApiUsageException("The getKeys method should only be used when keys for a single row are returned.  " + "The current key list contains keys for multiple rows: " + this.keyList);
        return (Map) this.keyList.get(0);
    }

    public List getKeyList() {
        return keyList;
    }
}
