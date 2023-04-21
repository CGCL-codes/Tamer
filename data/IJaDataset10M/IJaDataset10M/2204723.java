package net.sf.resultsetmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Provide a service to map the content of a ResultSet to any set of Target
 * Classes.
 * 
 * @author Warren Mayocchi
 */
public interface ResultSetMapper<T> {

    /**
   * Map the incoming result set row to an object.
   * 
   * @param resultSet
   *          The Result set to map.
   * @return A target class object.
   * @throws SQLException
   */
    public T mapRow(ResultSet resultSet) throws SQLException;

    /**
   * This provides a full result set reader that will construct all valid beans.
   * 
   * @param resultSet
   *          The result set to map.
   * @return A list of target class objects.
   * @throws SQLException
   */
    public List<T> mapTable(ResultSet resultSet) throws SQLException;

    /**
   * Set the <code>NameMatcher</code> for the result set mapper.
   * 
   * @param nameMatcher
   *          The nameMatcher to set.
   */
    public void setNameMatcher(NameMatcher nameMatcher);

    /**
   * Set the <code>ObjectValidator</code> for the result set mapper.
   * 
   * @param objectValidator
   *          The objectValidator to set.
   */
    public void setObjectValidator(ObjectValidator objectValidator);

    /**
   * Set whether the MapToData annotation is required for mapping to occur. By
   * default it is required. If required is set to false, an attempt is made to
   * match every field in the object.
   * 
   * @param required
   *          true = annotation is required; false = annotation is not required.
   */
    public void setAnnotationRequired(boolean required);

    /**
   * Indicate whether the MapToData annotation is required for mapping to occur.
   * 
   * @return Whether annotation is required.
   */
    public boolean isAnnotationRequired();
}
