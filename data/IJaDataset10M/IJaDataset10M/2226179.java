package org.enjavers.jethro.api.adapters;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.enjavers.jethro.api.dto.DTODomain;

/**
 * Provides a mapping between the persistence layer data type and 
 * the Java data types.
 * 
 * @author Alessandro Lombardi, Fabrizio Gambelunghe
 */
public interface QLAdapter {

    /**
	 * Extract the object's value from the result set of a query
	 * @param i_rs	the result set
	 * @param i_descriptor the field name or the index of a field
	 * @param i_domain the domain of the related dto property
	 * 
	 * @return the value of the object
	 * @throws SQLException
	 */
    public Object getObject(ResultSet i_rs, String i_descriptor, DTODomain i_domain) throws SQLException;

    /**
	 * Translate the object value into the stringfied sql representation for a specific dbms  
	 * @param value the object value
	 * @param i_domain the domain of the related dto property
	 * @return a stringfied value
	 */
    public String getFieldValue(Object value, DTODomain i_domain);
}
