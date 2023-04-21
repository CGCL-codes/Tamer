package edu.hawaii.server;

import java.util.List;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Creates an Java Object for each record in the database.  
 * 
 * @author Kiet Huynh
 * @author brandon lee
 */
public class ObjectFactory {

    /**
   * Loops through the result set and create an object for each record 
   * in the database.
   * @param rs The ResultSet which is the output returned by the database 
   *        after each query or each call to a stored procedure.
   * @param cls The java class that the records in the ResultSet will be 
   *        converted to.
   * @return A list of converted objects
   */
    @SuppressWarnings("unchecked")
    public static List convertsToObjects(ResultSet rs, Class cls) {
        List result = new ArrayList();
        try {
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object item = cls.newInstance();
                for (int i = 1; i <= columnCount; i += 1) {
                    String columnName = rs.getMetaData().getColumnName(i);
                    Object value = rs.getObject(i);
                    PropertyDescriptor pd = new PropertyDescriptor(columnName, cls);
                    Method method = pd.getWriteMethod();
                    method.invoke(item, new Object[] { value });
                }
                result.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    /**
   * Exactly like convertsToObjects but catored parsing for class AvailableSeatsBySection using the
   * class TimeLocationParse to parse info as well as create new collumns for each.
   * 
   * @param rs The ResultSet which is the output returned by the database 
   *        after each query or each call to a stored procedure.
   * @param cls The java class that the records in the ResultSet will be 
   *        converted to.
   * @return A list of converted objects
   */
    @SuppressWarnings("unchecked")
    public static List convertsToObjectsASBS(ResultSet rs, Class cls) {
        List result = new ArrayList();
        try {
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object item = cls.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rs.getMetaData().getColumnName(i);
                    Object value = rs.getObject(i);
                    if (columnName.equalsIgnoreCase("TIMELOCATION")) {
                        PropertyDescriptor pd = new PropertyDescriptor(columnName, cls);
                        Method method = pd.getWriteMethod();
                        method.invoke(item, new Object[] { value });
                        pd = new PropertyDescriptor("LOCATION", cls);
                        TimeLocationParse tlp = new TimeLocationParse(value.toString());
                        method = pd.getWriteMethod();
                        method.invoke(item, new Object[] { new String(tlp.getLocation()) });
                        pd = new PropertyDescriptor("INSTRUCTOR", cls);
                        tlp = new TimeLocationParse(value.toString());
                        method = pd.getWriteMethod();
                        method.invoke(item, new Object[] { new String(tlp.getInstructor()) });
                        pd = new PropertyDescriptor("TIME", cls);
                        tlp = new TimeLocationParse(value.toString());
                        method = pd.getWriteMethod();
                        method.invoke(item, new Object[] { new String(tlp.getTime()) });
                        pd = new PropertyDescriptor("DAYS", cls);
                        tlp = new TimeLocationParse(value.toString());
                        method = pd.getWriteMethod();
                        method.invoke(item, new Object[] { new String(tlp.getDay()) });
                    } else {
                        PropertyDescriptor pd = new PropertyDescriptor(columnName, cls);
                        Method method = pd.getWriteMethod();
                        method.invoke(item, new Object[] { value });
                    }
                }
                result.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
}
