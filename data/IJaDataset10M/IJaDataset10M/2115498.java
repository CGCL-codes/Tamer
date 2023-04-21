package bill.util.persist;

import java.util.*;
import java.sql.*;

/**
 * This class is used to define an entity and to manipulate the entity
 * in the persistent store. This class works with a RDBMS, in which case an
 * entity represents a row in a table.
 */
public abstract class EntityDataRDBMS extends EntityData
{
   /**
    * Creator based on the name of the entity. Populates and defaults
    * class instance variables. The user must call setEntityName before
    * being able to do much with an object created with this creator.
    */
   public EntityDataRDBMS ()
   {
      super ();
   }

   /**
    * Creator based on the name of the entity. Populates and defaults
    * class instance variables.
    *
    * @param entityName The name of the entity.
    */
   public EntityDataRDBMS (String entityName)
   {
      super (entityName);
   }

   /**
    * Creator based on the name of the entity and the key elements' values.
    * Populates and defaults class instance variables, the populates the
    * entity's element values from the persistent data store.
    *
    * @param entityName The name of the entity.
    * @param keyValues The key element's values. Keyed by the element's logical
    * name, result is the element's value.
    * @throws PersistException If a fatal error occurs that causes us to not
    * be able to perform the retrieval. In our case this would be a SQLException
    * of some sort.
    */
   public EntityDataRDBMS (String entityName, Hashtable keyValues)
          throws PersistException
   {
      super (entityName, keyValues);
   }

   /**
    * Returns the class name of the EntityCollection class associated with
    * this class.
    *
    * @return The full package name of the collection class, in this case
    * bill.util.EntityCollectionRDBMS.
    */
   public String getCollectionClassName ()
   {
      return (new String ("bill.util.persist.EntityCollectionRDBMS"));
   }

   /**
    * Creates the SQL needed to add a new entity to the persistent data
    * store. This is done by looping through all the registered elements
    * and their values and building the SQL string using these values.
    *
    * @return The SQL string to perform the insert.
    */
   protected String createInsertSQL ()
   {
      String      insertSQL = null;
      Enumeration enumerate = null;
      String      elementName = null;
      ElementData element = null;
      long        nextOid = 0;

      insertSQL = "insert into " + getEntityName () + " (";
      for (enumerate = _elements.elements ();enumerate.hasMoreElements ();)
      {
         if (element != null)
            insertSQL += ", ";
         element = (ElementData)enumerate.nextElement ();
         if (element instanceof OIDElement)
         {
            OIDElement oid = (OIDElement)element;
            // Get the next sequence number for the oid
            nextOid = EntityDataFactoryRDBMS.getNextSequence (getEntityName ());
            try
            {
               oid.setValue (nextOid);
            }
            catch (PersistException pEx)
            { // cannot actually happen since we are using a long
            } // vs a string in our setValue, so ignore it
         }
         insertSQL += element.getPhysicalName ();
      }
      insertSQL += ") values (";
      element = null;
      for (enumerate = _elements.elements ();enumerate.hasMoreElements ();)
      {
         if (element != null)
            insertSQL += ", ";
         element = (ElementData)enumerate.nextElement ();
         insertSQL += element.formatValue ();
      }
      insertSQL += ")";
      return insertSQL;
   }

   /**
    * Creates the SQL needed to update an existing entity in the persistent
    * data store. This is done by looping through all the registered elements
    * and their values and building the SQL string using these values.
    *
    * @return The SQL string to perform the update.
    */
   protected String createUpdateSQL () throws PersistException
   {
      String      updateSQL = null;
      Enumeration enumerate = null;
      ElementData element = null;

      updateSQL = "update " + getEntityName () + " set ";
      for (enumerate = _elements.elements ();enumerate.hasMoreElements ();)
      {
         if (element != null)
            updateSQL += ", ";
         element = (ElementData)enumerate.nextElement ();
         updateSQL += element.getPhysicalName () + "=" + element.getValue ();
      }
      updateSQL += buildWhereClause (getKey ().getKeyValues ());
      return updateSQL;
   }

   /**
    * Creates the SQL needed to delete an existing entity from the persistent
    * data store. This is done by looping through all the registered elements
    * and their values and building the SQL string using these values.
    *
    * @return The SQL string to perform the delete.
    */
   protected String createDeleteSQL () throws PersistException
   {
      String      deleteSQL = null;

      deleteSQL = "delete from " + getEntityName ();
      deleteSQL += buildWhereClause (getKey ().getKeyValues ());
      return deleteSQL;
   }

   /**
    * Creates the SQL needed to select some existing entities in the persistent
    * data store.
    *
    * @return The SQL string to perform the select.
    */
   protected String createSelectSQL () throws PersistException
   {
      return createSelectSQL (null);
   }

   /**
    * Creates the SQL needed to select an existing entity (or entities) in the
    * persistent data store. This is done by looping through all the specified
    * elements and their values and building the SQL string using these values.
    *
    * @param keyValues The key element's values. Keyed by the element's logical
    * name, result is the element's value.
    * @return The SQL string to perform the select.
    */
   protected String createSelectSQL (Hashtable keyValues)
             throws PersistException
   {
      String      selectSQL = null;
      Enumeration enumerate = null;
      ElementData element = null;

      selectSQL = "select ";
      for (enumerate = _elements.elements ();enumerate.hasMoreElements ();)
      {
         if (element != null)
            selectSQL += ", ";
         element = (ElementData)enumerate.nextElement ();
         selectSQL += element.getPhysicalName ();
      }
      selectSQL += " from " + getEntityName ();
      selectSQL += buildWhereClause (keyValues);
      return selectSQL;
   }

   /**
    * Builds a SQL where clause based on the element values supplied by
    * the caller. Note that if the supplied values do not uniquely identify
    * a row in the database, than multiple rows will be affected by using
    * the resulting where clause.
    *
    * @param whereValues The list of elements to use in creating the where
    * @return The generated where clause. Note that if the whereValues
    * parameter is <code>null</code>, than an empty string is returned.
    */
   private String buildWhereClause (Hashtable whereValues)
           throws PersistException
   {
      String where = " where ";
      int    i = 0;

      if (whereValues == null)
         return "";
      for (Enumeration enum = whereValues.keys ();
           enum.hasMoreElements ();
           i++)
      {
         String key = (String)enum.nextElement ();
         ElementData element = (ElementData)_elements.get (key);
         if (element == null)
            throw new PersistException (
               "Error building SQL where clause, element \"" + key +
               "\" is not an element of entity \"" + getEntityName () + "\"");
         if (i > 0)
            where += " and ";
         String value = (String)whereValues.get (key);
         // Check if the last character of the value is a %. In this case we are
         // a 'like', not an equals.
         // WGH - although this works for InstantDB, not sure if it works for
         // other DBs, as they may require strings to be in quotes.
         if (value.indexOf ('%') == value.length () - 1)
         {
            where += element.getPhysicalName () + " like " + value;
         }
         else
         {
            where += element.getPhysicalName () + " = " + value;
         }
      }
      return where;
   }

   /**
    * Controls the updating of the persistent store with the entity's
    * information. Determines the type of action to perform against
    * the store (insert, update, or delete) and calls the appropriate
    * method to build the SQL statement to perform the action, then
    * executes the SQL statement.
    *
    * @return The number of rows affected by the persistent store update.
    * @throws PersistException If a fatal error occurs that causes us to not
    * be able to perform the action. In our case this would be a SQLException
    * of some sort or a validation error.
    */
   public int save () throws PersistException
   {
      String    sql = null;
      Statement statement = null;
      int       rowsAffected = 0;

      super.save ();
      if (isNew () && isDeleted ())
         return 1;
      if (isNew ())
         sql = createInsertSQL ();
      else if (isDeleted ())
         sql = createDeleteSQL ();
      else if (isModified ())
         sql = createUpdateSQL ();
//System.out.println ("SQL: " + sql);
      if (sql == null)
         return 0;
      try
      {
         statement =
            EntityDataFactoryRDBMS.getDBConnection ().createStatement ();
      }
      catch (SQLException SQLEx)
      {
         throw new PersistException (
            "EntityDataRDBMS:save => Could not create a new SQL statement: " +
            SQLEx.toString ());
      }
      try
      {
         rowsAffected = statement.executeUpdate (sql);
      }
      catch (SQLException SQLEx2)
      {
         throw new PersistException (
            "EntityDataRDBMS:save => Could not execute SQL statement: " + sql +
            SQLEx2.toString ());
      }
      finally
      {
         try
         {
            statement.close ();
         }
         catch (SQLException SQLEx3)
         {
         }
      }
      // If the entity was not deleted, clear its persistence state. If it
      // was deleted we leave it so that the caller knows and can take the
      // appropriate action with it.
      if (!isDeleted ())
         setClean ();
      return rowsAffected;
   }

   /**
    * Retrieves an entity from the database that is keyed by the
    * given key values.
    *
    * @param keyValues The key element's values. Keyed by the element's logical
    * name, result is the element's value.
    * @throws PersistException If a fatal error occurs that causes us to not
    * be able to perform the retrieval. In our case this would be a SQLException
    * of some sort.
    */
   public void retrieve (Hashtable keyValues) throws PersistException
   {
      String      sql = null,
                  className = null;
      Enumeration enumerate = null;
      ElementData element = null;
      Statement   statement = null;
      ResultSet   results = null;

      sql = createSelectSQL (keyValues);
//      System.out.println ("SQL: " + sql);
      try
      {
         statement =
            EntityDataFactoryRDBMS.getDBConnection ().createStatement ();
      }
      catch (SQLException SQLEx)
      {
         throw new PersistException (
            "EntityDataRDBMS:retrieve => Could not create a new SQL " +
            "statement: " + SQLEx.toString ());
      }
      try
      {
         results = statement.executeQuery (sql);
         if (results.next ())
         {
            for (enumerate = _elements.elements ();
                 enumerate.hasMoreElements ();)
            {
               element = (ElementData)enumerate.nextElement ();
               className = element.getClass ().getName ();
               if (className.equals (ET_STRING))
                  element.setValue (
                     results.getString (element.getPhysicalName ()).trim ());
               else if (className.equals (ET_INTEGER))
                  ((IntegerElement)element).setValue (
                     results.getInt (element.getPhysicalName ()));
               else if (className.equals (ET_LONG))
                  ((LongElement)element).setValue (
                     results.getLong (element.getPhysicalName ()));
               else if (className.equals (ET_OID))
                  ((OIDElement)element).setValue (
                     results.getLong (element.getPhysicalName ()));
            }
         }
      }
      catch (SQLException SQLEx2)
      {
         throw new PersistException (
            "EntityDataRDBMS:retrieve => Could not execute " +
            "SQL statement (" + sql + ") : " + SQLEx2.toString ());
      }
      catch (PersistException pEx)
      {
         throw new PersistException (
            "EntityDataRDBMS:retrieve => Could process SQL " +
            "statement (" + sql + ") : " + pEx.toString ());
      }
      finally
      {
         try
         {
            statement.close ();
         }
         catch (SQLException SQLEx3)
         {
         }
      }
   }
}
