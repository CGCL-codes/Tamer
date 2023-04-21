package com.jcorporate.expresso.core.misc;

import com.jcorporate.expresso.core.controller.ControllerException;
import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.dataobjects.DataObject;
import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.dbobj.MultiDBObject;
import com.jcorporate.expresso.core.dbobj.SecuredDBObject;
import com.jcorporate.expresso.services.dbobj.DBObjLimit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Copyright: Copyright (c) 2001-2002 JCorporate Ltd.<p>
 * <p>This class takes care of the low-level logic for when dealing
 * with &quote;pages&quote; of data.  It is used in the Download Controller
 * to allow paging through masses of download files, as well as in <code>DBMaint</code> for
 * paging through the data records.</p>
 * @author Michael Rimov
 * @version $Revision: 3 $ $Date: 2006-03-01 06:17:08 -0500 (Wed, 01 Mar 2006) $
 */
public class RecordPaginator implements Serializable {

    /**
     *  Are there more records that can be retrieved
     */
    private boolean moreRecords;

    /**
     *  Are there previous records that can be retrieved
     */
    private boolean previousRecords;

    /**
     * What is the page number of the current record set.
     */
    private int pageNumber = 1;

    private int pageLimit;

    /**
     * What is the start record number of the current set
     */
    private int startRecordNumber;

    /**
     * What is the end record number of the current set.
     */
    private int endRecordNumber;

    /**
     * Boolean that states whether a DBObject.count() should be issued before
     * issuing the search and retrieve call.
     */
    private boolean countRecords;

    /**
     * Total Number of records retrieved
     */
    private int totalRecordCount;

    /**
     * Total number of pages available
     */
    private int totalPageCount;

    public RecordPaginator() {
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    public void setMoreRecords(boolean newMoreRecords) {
        moreRecords = newMoreRecords;
    }

    public boolean isMoreRecords() {
        return moreRecords;
    }

    public void setPreviousRecords(boolean newPreviousRecords) {
        previousRecords = newPreviousRecords;
    }

    public boolean isPreviousRecords() {
        return previousRecords;
    }

    public void setPageNumber(int newPageNumber) {
        pageNumber = newPageNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setStartRecordNumber(int newStartRecordNumber) {
        startRecordNumber = newStartRecordNumber;
    }

    /**
     * Modified code based upon xin lee.
     * @return the Start Record Number
     */
    public int getStartRecordNumber() {
        if (endRecordNumber > startRecordNumber) {
            return startRecordNumber;
        } else {
            return endRecordNumber;
        }
    }

    /**
     * Sets the ending record number
     * @param newEndRecordNumber int for the new end record number
     */
    public void setEndRecordNumber(int newEndRecordNumber) {
        endRecordNumber = newEndRecordNumber;
    }

    /**
     * Returns the end record number for a particular page.
     * @return integer &gt;= 0
     */
    public int getEndRecordNumber() {
        return endRecordNumber;
    }

    /**
     * Sets whether or not the total record count should be used or not.
     * @param newCountRecords true if you wish for the recordset to retrieve
     * a total record count.
     */
    public void setCountRecords(boolean newCountRecords) {
        countRecords = newCountRecords;
    }

    /**
     * Returns true if the system is expected to get the total record count.
     * @return true if the record count will be active.
     */
    public boolean isCountRecords() {
        return countRecords;
    }

    /**
     * Returns the total record count retrieved for this database search
     * @return total record count as long.
     */
    public long getTotalRecordCount() {
        return totalRecordCount;
    }

    /**
     * Get the number of pages [using recorg pagination] that exist for this record.
     * @return
     */
    public int getPageCount() {
        if (pageLimit == 0) {
            return 1;
        } else {
            return (int) (this.getTotalRecordCount() / pageLimit) + 1;
        }
    }

    /**
     * Returns a search and retrieve list from the DBObject who's criteria
     * you have set.  Also sets its internal attributes such as start record number
     * etc.
     * @param searchCriteria The DBObject to search against
     * @param sortKey the field to search against.
     * @throws DBException
     * @return an ArrayList of DBObjects retrieved by the searchCriteria
     */
    public ArrayList searchAndRetrieve(DataObject searchCriteria, String sortKey) throws DBException {
        if (isCountRecords()) {
            totalRecordCount = searchCriteria.count();
        }
        if (getPageNumber() > 1) {
            setPreviousRecords(true);
        }
        Integer pageLimitObj = (Integer) searchCriteria.getAttribute("pageLimit");
        if (pageLimitObj == null) {
            this.setPageLimitAttribute(searchCriteria);
            pageLimitObj = (Integer) searchCriteria.getAttribute("pageLimit");
        }
        if (pageLimitObj != null) {
            pageLimit = pageLimitObj.intValue();
        } else {
            pageLimit = 0;
        }
        searchCriteria.setMaxRecords(pageLimit);
        searchCriteria.setOffsetRecord(pageLimit * (getPageNumber() - 1));
        ArrayList allRecords = (ArrayList) searchCriteria.searchAndRetrieveList(sortKey);
        if (!isCountRecords()) {
            totalRecordCount = allRecords.size();
        }
        startRecordNumber = (pageLimit * (getPageNumber() - 1)) + 1;
        endRecordNumber = startRecordNumber + allRecords.size() - 1;
        if (endRecordNumber >= totalRecordCount) {
            setMoreRecords(false);
        } else {
            setMoreRecords(true);
        }
        return allRecords;
    }

    /**
     * Returns a search and retrieve list from the DBObject who's criteria
     * you have set.  Also sets its internal attributes such as start record number
     * etc.
     * @param searchCriteria The DBObject to search against
     * @param sortKey the field to search against.
     * @throws DBException
     * @return an ArrayList of DBObjects retrieved by the searchCriteria
     */
    public List searchAndRetrieve(MultiDBObject searchCriteria, String sortKey) throws DBException {
        if (isCountRecords()) {
            totalRecordCount = searchCriteria.count();
        }
        if (getPageNumber() > 1) {
            setPreviousRecords(true);
        }
        Integer pageLimitObj = (Integer) searchCriteria.getAttribute("pageLimit");
        if (pageLimitObj == null) {
            this.setPageLimitAttribute(searchCriteria);
            pageLimitObj = (Integer) searchCriteria.getAttribute("pageLimit");
        }
        int pageLimit;
        if (pageLimitObj != null) {
            pageLimit = pageLimitObj.intValue();
        } else {
            pageLimit = 0;
        }
        searchCriteria.setMaxRecords(pageLimit);
        searchCriteria.setOffsetRecord(pageLimit * (getPageNumber() - 1));
        List allRecords = searchCriteria.searchAndRetrieveList(sortKey);
        if (!isCountRecords()) {
            totalRecordCount = allRecords.size();
        }
        startRecordNumber = (pageLimit * (getPageNumber() - 1)) + 1;
        endRecordNumber = startRecordNumber + allRecords.size() - 1;
        if (endRecordNumber >= totalRecordCount) {
            setMoreRecords(false);
        } else {
            setMoreRecords(true);
        }
        return allRecords;
    }

    /**
     * Sets the size of the page limit.  It uses the DBObject Limit to define
     * how many records per page are displayed
     * @param dbObj The dbobject to set for.
     * @throws DBException
     */
    protected void setPageLimitAttribute(DataObject dbObj) throws DBException {
        DBObjLimit dl = new DBObjLimit(SecuredDBObject.SYSTEM_ACCOUNT);
        dl.setDataContext(dbObj.getDataContext());
        dl.setField("DBObjectName", ((Object) dbObj).getClass().getName());
        int pageLimit = 0;
        if (dl.find()) {
            try {
                pageLimit = new Integer(dl.getField("PageLimit")).intValue();
            } catch (NumberFormatException ne) {
                throw new DBException("Can't use limit of '" + dl.getField("PageLimit") + "' for " + ((Object) dbObj).getClass().getName());
            }
            dbObj.setMaxRecords((getPageNumber() * pageLimit) + 1);
            dbObj.setAttribute("pageLimit", new Integer(pageLimit));
        } else {
            dl.setField("DBObjectName", "com.jcorporate.expresso.services.dbobj.ControllerDefault");
            if (dl.find()) {
                try {
                    pageLimit = new Integer(dl.getField("PageLimit")).intValue();
                } catch (NumberFormatException ne) {
                    throw new DBException("Can't use limit of '" + dl.getField("PageLimit") + "' for " + ((Object) dbObj).getClass().getName());
                }
                dbObj.setMaxRecords((getPageNumber() * pageLimit) + 1);
                dbObj.setAttribute("pageLimit", new Integer(pageLimit));
            } else {
                pageLimit = 0;
            }
        }
    }

    /**
     * Sets the size of the page limit.  It uses the DBObject Limit to define
     * how many records per page are displayed
     * @param dbObj The dbobject to set for.
     * @throws DBException
     */
    protected void setPageLimitAttribute(MultiDBObject dbObj) throws DBException {
        DBObjLimit dl = new DBObjLimit(SecuredDBObject.SYSTEM_ACCOUNT);
        dl.setDataContext(dbObj.getDBName());
        dl.setField("DBObjectName", ((Object) dbObj).getClass().getName());
        int pageLimit = 0;
        if (dl.find()) {
            try {
                pageLimit = new Integer(dl.getField("PageLimit")).intValue();
            } catch (NumberFormatException ne) {
                throw new DBException("Can't use limit of '" + dl.getField("PageLimit") + "' for " + ((Object) dbObj).getClass().getName());
            }
            dbObj.setMaxRecords((getPageNumber() * pageLimit) + 1);
            dbObj.setAttribute("pageLimit", new Integer(pageLimit));
        } else {
            dl.setField("DBObjectName", "com.jcorporate.expresso.services.dbobj.ControllerDefault");
            if (dl.find()) {
                try {
                    pageLimit = new Integer(dl.getField("PageLimit")).intValue();
                } catch (NumberFormatException ne) {
                    throw new DBException("Can't use limit of '" + dl.getField("PageLimit") + "' for " + ((Object) dbObj).getClass().getName());
                }
                dbObj.setMaxRecords((getPageNumber() * pageLimit) + 1);
                dbObj.setAttribute("pageLimit", new Integer(pageLimit));
            } else {
                pageLimit = 0;
            }
        }
    }

    /**
     * Sets the page number based upon the controller request object.
     * If the page parameters is not included with the request, then page
     * is set to zero.
     *
     * @param request The controller request fed to the controller from which the
     * function can extract the page= controller parameter.
     * @throws ControllerException if page= is not a number.
     */
    public void setPageNumber(ControllerRequest request) throws ControllerException {
        String pg = request.getParameter("page");
        if (pg != null) {
            try {
                this.setPageNumber(Integer.parseInt(pg));
            } catch (NumberFormatException nfe) {
                throw new ControllerException("Page number parameter is not an integer");
            }
        } else {
            setPageNumber(1);
        }
    }
}
