package org.melati.poem.test.generated;

import org.melati.poem.AccessPoemException;
import org.melati.poem.Column;
import org.melati.poem.Field;
import org.melati.poem.Persistent;
import org.melati.poem.ValidationPoemException;
import org.melati.poem.test.AthingTable;
import org.melati.poem.test.TestDatabaseTables;

/**
 * Melati POEM generated abstract base class for a <code>Persistent</code> 
 * <code>Athing</code> Object.
 *
 * @generator org.melati.poem.prepro.TableDef#generateBaseJava 
 */
public abstract class AthingBase extends Persistent {

    /**
  * Retrieves the Database object.
  * 
  * @generator org.melati.poem.prepro.TableDef#generateBaseJava 
  * @return the database
  */
    public TestDatabaseTables getTestDatabaseTables() {
        return (TestDatabaseTables) getDatabase();
    }

    /**
  * Retrieves the  <code>AthingTable</code> table 
  * which this <code>Persistent</code> is from.
  * 
  * @generator org.melati.poem.prepro.TableDef#generateBaseJava 
  * @return the AthingTable
  */
    public AthingTable getAthingTable() {
        return (AthingTable) getTable();
    }

    private AthingTable _getAthingTable() {
        return (AthingTable) getTable();
    }

    /**
  * Id 
  */
    protected Integer id;

    /**
  * Binaryfield 
  */
    protected byte[] binaryfield;

    /**
  * Retrieves the <code>Id</code> value, without locking, 
  * for this <code>Athing</code> <code>Persistent</code>.
  *
  * @generator org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @return the Integer id
  */
    public Integer getId_unsafe() {
        return id;
    }

    /**
  * Sets the <code>Id</code> value directly, without checking, 
  * for this Athing <code>Persistent</code>.
  * 
  * @generator org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @param cooked  the pre-validated value to set
  */
    public void setId_unsafe(Integer cooked) {
        id = cooked;
    }

    /**
  * Retrieves the Id value, with locking, for this 
  * <code>Athing</code> <code>Persistent</code>.
  * 
  * @generator org.melati.poem.prepro.AtomFieldDef#generateBaseMethods 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights 
  * @return the value of the field <code>Id</code> for this 
  *         <code>Athing</code> <code>Persistent</code>  
  */
    public Integer getId() throws AccessPoemException {
        readLock();
        return getId_unsafe();
    }

    /**
  * Sets the <code>Id</code> value, with checking, for this 
  * <code>Athing</code> <code>Persistent</code>.
  * 
  * @generator org.melati.poem.prepro.AtomFieldDef#generateBaseMethods  
  * @param cooked  a validated <code>int</code> 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @throws ValidationPoemException 
  *         if the value is not valid
  */
    public void setId(Integer cooked) throws AccessPoemException, ValidationPoemException {
        _getAthingTable().getIdColumn().getType().assertValidCooked(cooked);
        writeLock();
        setId_unsafe(cooked);
    }

    /**
  * Sets the <code>Id</code> value, with checking, for this 
  * <code>Athing</code> <code>Persistent</code>.
  * 
  * @generator org.melati.poem.prepro.IntegerFieldDef#generateBaseMethods 
  * @param cooked  a validated <code>int</code>
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @throws ValidationPoemException 
  *         if the value is not valid
  */
    public final void setId(int cooked) throws AccessPoemException, ValidationPoemException {
        setId(new Integer(cooked));
    }

    /**
  * Retrieves the <code>Id</code> value as a <code>Field</code>
  * from this <code>Athing</code> <code>Persistent</code>.
  * 
  * @generator org.melati.poem.prepro.FieldDef#generateFieldCreator 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @return the Integer id
  */
    public Field getIdField() throws AccessPoemException {
        Column c = _getAthingTable().getIdColumn();
        return new Field(c.getRaw(this), c);
    }

    /**
  * Retrieves the <code>Binaryfield</code> value, without locking, 
  * for this <code>Athing</code> <code>Persistent</code>.
  *
  * @generator org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @return the byte[] binaryfield
  */
    public byte[] getBinaryfield_unsafe() {
        return binaryfield;
    }

    /**
  * Sets the <code>Binaryfield</code> value directly, without checking, 
  * for this Athing <code>Persistent</code>.
  * 
  * @generator org.melati.poem.prepro.FieldDef#generateBaseMethods 
  * @param cooked  the pre-validated value to set
  */
    public void setBinaryfield_unsafe(byte[] cooked) {
        binaryfield = cooked;
    }

    /**
  * Retrieves the Binaryfield value, with locking, for this 
  * <code>Athing</code> <code>Persistent</code>.
  * 
  * @generator org.melati.poem.prepro.AtomFieldDef#generateBaseMethods 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights 
  * @return the value of the field <code>Binaryfield</code> for this 
  *         <code>Athing</code> <code>Persistent</code>  
  */
    public byte[] getBinaryfield() throws AccessPoemException {
        readLock();
        return getBinaryfield_unsafe();
    }

    /**
  * Sets the <code>Binaryfield</code> value, with checking, for this 
  * <code>Athing</code> <code>Persistent</code>.
  * 
  * @generator org.melati.poem.prepro.AtomFieldDef#generateBaseMethods  
  * @param cooked  a validated <code>int</code> 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @throws ValidationPoemException 
  *         if the value is not valid
  */
    public void setBinaryfield(byte[] cooked) throws AccessPoemException, ValidationPoemException {
        _getAthingTable().getBinaryfieldColumn().getType().assertValidCooked(cooked);
        writeLock();
        setBinaryfield_unsafe(cooked);
    }

    /**
  * Retrieves the <code>Binaryfield</code> value as a <code>Field</code>
  * from this <code>Athing</code> <code>Persistent</code>.
  * 
  * @generator org.melati.poem.prepro.FieldDef#generateFieldCreator 
  * @throws AccessPoemException 
  *         if the current <code>AccessToken</code> 
  *         does not confer write access rights
  * @return the byte[] binaryfield
  */
    public Field getBinaryfieldField() throws AccessPoemException {
        Column c = _getAthingTable().getBinaryfieldColumn();
        return new Field(c.getRaw(this), c);
    }
}
