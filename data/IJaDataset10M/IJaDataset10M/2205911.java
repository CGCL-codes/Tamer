package org.streets.database;

import java.sql.ResultSet;
import java.util.Collection;

/**
 * A {@link ResultSet} Record mapping Handler.<br>
 * This handler is supposed to transform a {@link ResultSet} into an object or
 * into a {@link Collection} of the same type as this {@link RecordHandler}
 * type
 * 
 * @param <T> the type of {@link RecordHandler}
 * 
 * @author dzb
 *
 */
public interface RecordHandler<T> {

    /**
	 * Implementations must NOT call {@link ResultSet#next()} on the given
	 * {@link ResultSet} if they intend to return a single object per {@link ResultSet}
	 * 
	 * @param rs
	 *            the {@link ResultSet} to be handled
	 * @return the object made using this {@link ResultSet}
	 * @throws any kind of {@link Exception}
	 */
    T mapping(final ResultSet rs, SQLConnection connection) throws Exception;
}
