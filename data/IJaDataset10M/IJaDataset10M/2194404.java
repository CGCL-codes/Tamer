package org.nexopenframework.web.vlh.adapter.jpa.util.setter;

import java.text.ParseException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.nexopenframework.web.vlh.adapter.jpa.util.Setter;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class DefaultSetter implements Setter {

    public void set(final Query query, final String key, final Object value) throws PersistenceException, ParseException {
        query.setParameter(key, value);
    }
}
