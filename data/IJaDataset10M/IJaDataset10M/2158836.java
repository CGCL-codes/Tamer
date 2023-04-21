package org.redwood.business.etl.logfile;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;
import java.io.*;
import java.rmi.RemoteException;

public class LogFileBean implements EntityBean, LogFile {

    protected EntityContext entityContext;

    /**
   * CMP managed attributes
   */
    public long rw_id;

    public java.sql.Timestamp rw_dateTransferred;

    public String rw_logFileFormat;

    public String rw_filename;

    public String rw_filepath;

    public long rw_filesize;

    public byte[] rw_file;

    public String rw_processState;

    public String rw_serverAddress;

    public String rw_websiteID;

    public String rw_datasourceID;

    public java.sql.Timestamp rw_beginperiod;

    public java.sql.Timestamp rw_endperiod;

    public java.sql.Timestamp rw_fileDate;

    public String rw_apacheFormatString;

    public String rw_checksum;

    public LogFileBean() {
    }

    /**
   * There must be one ejbCreate() method per create() method on the Home interface,
   * and with the same signature.
   *
   * @param id           primary key id.
   *
   * @return pk primary key set to null
   *
   * @exception RemoteException If the instance could not perform the function
   *            requested by the container
   */
    public LogFilePK ejbCreate(long id, String filename) throws CreateException {
        this.rw_id = id;
        this.rw_filename = filename;
        return new LogFilePK(id);
    }

    /**
   * Each ejbCreate method should have a matching ejbPostCreate method
   */
    public void ejbPostCreate(long id, String filename) throws CreateException {
    }

    /**
   * A container invokes this method when the instance is taken out of the pool
   * of available instances to become associated with a specific EJB object.
   * This method transitions the instance to the ready state.
   *
   * This method executes in an unspecified transaction context.
   *
   * @exception RemoteException If the instance could not perform the function
   *            requested by the container because of an system-level error.
   */
    public void ejbActivate() throws RemoteException {
    }

    /**
   * A container invokes this method to instruct the instance to synchronize
   * its state by loading it state from the underlying database.
   * This method always executes in the proper transaction context.
   *
   * @exception RemoteException  If the instance could not perform the function
   *            requested by the container because of a system-level error.
   *
   */
    public void ejbLoad() throws RemoteException {
    }

    /**
   * A container invokes this method on an instance before the instance becomes
   * disassociated with a specific EJB object. After this method completes, the
   * container will place the instance into the pool of available instances.
   *
   * This method executes in an unspecified transaction context.
   *
   * @exception RemoteException  If the instance could not perform the function
   *            requested by the container because of a system-level error.
   */
    public void ejbPassivate() throws RemoteException {
    }

    /**
   * A container invokes this method before it removes the EJB object
   * that is currently associated with the instance. This method is invoked
   * when a client invokes a remove operation on the enterprise Bean's home
   * interface or the EJB object's remote interface. This method transitions
   * the instance from the ready state to the pool of available instances.
   *
   * This method is called in the transaction context of the remove operation.
   *
   * @exception RemoteException  Thrown if the instance could not perform the function
   *            requested by the container because of a system-level error.
   * @exception RemoveException  The enterprise Bean does not allow destruction
   *            of the object.
   */
    public void ejbRemove() throws RemoteException, RemoveException {
    }

    /**
   * A container invokes this method to instruct the instance to synchronize
   * its state by storing it to the underlying database.
   *
   * This method always executes in the proper transaction context.
   *
   * @exception: RemoteException  Thrown if the instance could not perform the function
   *             requested by the container because of a system-level error.
   */
    public void ejbStore() throws RemoteException {
    }

    /**
   * Sets the associated entity context. The container invokes this method
   * on an instance after the instance has been created.
   *
   * This method is called in an unspecified transaction context.
   *
   * @param ctx - An EntityContext interface for the instance. The instance should
   *              store the reference to the context in an instance variable.
   * @exception RemoteException  Thrown if the instance could not perform the function
   *            requested by the container because of a system-level error.
   */
    public void setEntityContext(EntityContext ctx) throws RemoteException {
        entityContext = ctx;
    }

    /**
   * Unsets the associated entity context. The container calls this method
   * before removing the instance. This is the last method that the container
   * invokes on the instance. The Java garbage collector will eventually invoke
   * the finalize() method on the instance.
   *
   * This method is called in an unspecified transaction context.
   *
   * @exception RemoteException  Thrown if the instance could not perform the function
   *            requested by the container because of a system-level error.
   */
    public void unsetEntityContext() {
        entityContext = null;
    }

    public long getRw_id() throws RemoteException {
        return this.rw_id;
    }

    public void setRw_logFileFormat(String logFileFormat) throws RemoteException {
        this.rw_logFileFormat = logFileFormat;
    }

    public void setRw_dateTransferred(java.util.Date dateTransferred) throws RemoteException {
        if (dateTransferred != null) {
            this.rw_dateTransferred = new java.sql.Timestamp(dateTransferred.getTime());
        }
    }

    public java.util.Date getRw_dateTransferred() throws RemoteException {
        return this.rw_dateTransferred;
    }

    public java.util.Date getRw_fileDate() throws RemoteException {
        return this.rw_fileDate;
    }

    public long getRw_filesize() throws RemoteException {
        return this.rw_filesize;
    }

    public String getRw_filename() throws RemoteException {
        return this.rw_filename;
    }

    public String getRw_logFileFormat() throws RemoteException {
        return this.rw_logFileFormat;
    }

    public void setRw_filename(String filename) throws RemoteException {
        this.rw_filename = filename;
    }

    public void setRw_apacheFormatString(String apacheFormatString) throws RemoteException {
        this.rw_apacheFormatString = apacheFormatString;
    }

    public String getRw_apacheFormatString() throws RemoteException {
        return this.rw_apacheFormatString;
    }

    public String getRw_serverAddress() throws RemoteException {
        return this.rw_serverAddress;
    }

    public void setRw_filesize(long filesize) throws RemoteException {
        this.rw_filesize = filesize;
    }

    public String getRw_processState() throws RemoteException {
        return this.rw_processState;
    }

    public void setRw_serverAddress(String serverAddress) throws RemoteException {
        this.rw_serverAddress = serverAddress;
    }

    public void setRw_processState(String processState) throws RemoteException {
        this.rw_processState = processState;
    }

    public void setRw_fileDate(java.util.Date fileDate) throws RemoteException {
        if (fileDate != null) {
            this.rw_fileDate = new java.sql.Timestamp(fileDate.getTime());
        }
    }

    public String getRw_websiteID() throws RemoteException {
        return this.rw_websiteID;
    }

    public void setRw_websiteID(String websiteID) throws RemoteException {
        this.rw_websiteID = websiteID;
    }

    public String getRw_filepath() throws RemoteException {
        return this.rw_filepath;
    }

    public void setRw_filepath(String filepath) throws RemoteException {
        this.rw_filepath = filepath;
    }

    public String getRw_datasourceID() throws RemoteException {
        return this.rw_datasourceID;
    }

    public void setRw_datasourceID(String datasourceID) throws RemoteException {
        this.rw_datasourceID = datasourceID;
    }

    public void setRw_beginperiod(java.util.Date beginperiod) throws RemoteException {
        if (beginperiod != null) {
            this.rw_beginperiod = new java.sql.Timestamp(beginperiod.getTime());
        }
    }

    public java.util.Date getRw_beginperiod() throws RemoteException {
        return this.rw_beginperiod;
    }

    public void setRw_endperiod(java.util.Date endperiod) throws RemoteException {
        if (endperiod != null) {
            this.rw_endperiod = new java.sql.Timestamp(endperiod.getTime());
        }
    }

    public java.util.Date getRw_endperiod() throws RemoteException {
        return this.rw_endperiod;
    }

    /**
   * Gets an uncompressed ByteArray from the compressed one in the database.
   *
   * @return byte[] with the uncompressed logfile.
   * @exception RemoteException
   */
    public byte[] getRw_file() throws RemoteException {
        return rw_file;
    }

    /**
   * Sets the logfile. The file is compressed by java.util.zip and then
   * stored in the database.
   *
   * @exception RemoteException
   */
    public void setRw_file(byte[] file) throws RemoteException {
        this.rw_file = file;
    }

    public void setRw_checksum(String checksum) throws RemoteException {
        this.rw_checksum = checksum;
    }

    public String getRw_checksum() throws RemoteException {
        return this.rw_checksum;
    }
}
