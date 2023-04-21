package pool.bean;

import java.util.Hashtable;
import java.util.Vector;
import pool.tablename.Td_property_details;
import timer.ExecutionTimer;
import timer.TimerRecordFile;
import utility.ConvertToLower;

public class Td_property_details_Bean extends SubBean {

    private Vector<Object> TdPropertyVector = new Vector<Object>();

    ;

    private String tableName = "td_property_details";

    private Hashtable<Object, Object> newTransactionHash;

    private Hashtable<Object, Object> oldTransactionHash;

    private String tdid;

    private Td_property_details objTdpl;

    private Vector<Object> OldTdPropertyList = new Vector<Object>();

    private Vector<Object> NewTdPropertyList = new Vector<Object>();

    private Hashtable<Object, Object> ViewHashtable = new Hashtable<Object, Object>();

    public Td_property_details_Bean() {
    }

    public void setId(String id) {
        this.tdid = id;
    }

    public void setTdid(String tdid) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        this.tdid = tdid;
        t.end();
        @SuppressWarnings("unused") TimerRecordFile timerFile = new TimerRecordFile("pool.bean", "td_property_details_Bean", "setProcess", t.duration());
    }

    /**
	 * this function converts properties in vector in Td_property_details class format to insert
	 * @param PropertyHash contains property of transaction_details
	 * TdPropertyVector vector of properties of Td_property_details
	 */
    public void setPropertyVector(Hashtable<Object, Object> PropertyHash) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        Hashtable<Object, Object> propertyHash = new Hashtable<Object, Object>();
        propertyHash.putAll(PropertyHash);
        propertyHash = gin.ConvertPVtoVT(propertyHash);
        propertyHash = gin.convertMasterId(propertyHash, tableName, process);
        TdPropertyVector.add(propertyHash);
        t.end();
        @SuppressWarnings("unused") TimerRecordFile timerFile = new TimerRecordFile("pool.bean", "td_property_details_Bean", "setPropertyVector", t.duration());
    }

    /**
	 * this function converts properties in vector in Td_property_details class format to insert
	 * @param vector containing properties
	 */
    @SuppressWarnings("unchecked")
    public void setPropertyVector(Vector<Object> vector) {
        for (Object object : vector) {
            Hashtable<Object, Object> propertyHash = new Hashtable<Object, Object>();
            propertyHash.putAll((Hashtable<Object, Object>) object);
            propertyHash = gin.ConvertPVtoVT(propertyHash);
            propertyHash = gin.convertMasterId(propertyHash, tableName, process);
            Td_property_details pl = new Td_property_details(sqlDB);
            pl.PropertyMap.putAll(propertyHash);
            TdPropertyVector.add(pl);
        }
    }

    /**
	 * 
	 *insert into Td_property_details. 
	 */
    @SuppressWarnings("unchecked")
    public StringBuffer insert() {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        StringBuffer sbrDTB = new StringBuffer();
        sqlDB.setBeforeCommit();
        objTdpl = new Td_property_details(sqlDB);
        objTdpl.setDatabase(sqlDB);
        objTdpl.setRequestParameter(reqParam);
        objTdpl.setTdid(tdid);
        objTdpl.setPropertyVector(TdPropertyVector);
        sbrDTB.append(objTdpl.getInsert());
        boolean flag = objTdpl.getResult();
        sqlDB.setAfterCommit(flag);
        System.out.println(sbrDTB.toString());
        t.end();
        @SuppressWarnings("unused") TimerRecordFile timerFile = new TimerRecordFile("pool.bean", "td_property_details_Bean", "insertTd_property_details", t.duration());
        return sbrDTB;
    }

    /**
	 * 
	 * @param oldPropertyHash  is Hashtable of old property of Td_property_details.  
	 */
    public void setOldPropertyHash(Hashtable<Object, Object> oldPropertyHash) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        objTdpl = new Td_property_details(sqlDB);
        objTdpl.setRequestParameter(reqParam);
        oldTransactionHash = new Hashtable<Object, Object>();
        oldTransactionHash.putAll(oldPropertyHash);
        oldTransactionHash = ConvertToLower.convertHashKey(oldTransactionHash);
        oldTransactionHash = gin.ConvertPVtoVT(oldTransactionHash);
        oldTransactionHash = gin.convertMasterId(oldTransactionHash, tableName, process);
        Td_property_details tdpd = new Td_property_details();
        tdpd.setDatabase(sqlDB);
        tdpd.PropertyMap = new Hashtable<Object, Object>();
        tdpd.PropertyMap.putAll(oldTransactionHash);
        tdid = oldTransactionHash.get("tdid").toString();
        OldTdPropertyList.add(tdpd);
        t.end();
        @SuppressWarnings("unused") TimerRecordFile timerFile = new TimerRecordFile("pool.bean", "td_property_details_Bean", "setOldPropertyHash", t.duration());
    }

    /**
	 * 
	 * @param newPropertyHash is Hashtable of new property of Td_property_details to update.  
	 */
    public void setNewPropertyHash(Hashtable<Object, Object> newPropertyHash) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        objTdpl = new Td_property_details(sqlDB);
        objTdpl.setRequestParameter(reqParam);
        newTransactionHash = new Hashtable<Object, Object>();
        newTransactionHash.putAll(newPropertyHash);
        newTransactionHash = ConvertToLower.convertHashKey(newTransactionHash);
        newTransactionHash = gin.ConvertPVtoVT(newTransactionHash);
        newTransactionHash = gin.convertMasterId(newTransactionHash, tableName, process);
        Td_property_details tdpd = new Td_property_details();
        tdpd.setDatabase(sqlDB);
        tdpd.PropertyMap = new Hashtable<Object, Object>();
        tdpd.PropertyMap.putAll(newTransactionHash);
        NewTdPropertyList.add(tdpd);
        t.end();
        @SuppressWarnings("unused") TimerRecordFile timerFile = new TimerRecordFile("pool.bean", "td_property_details_Bean", "setNewPropertyHash", t.duration());
    }

    /**
	 * this function converts old properties of vector in Td_property_details class format to update
	 * @param vector containing old properties
	 */
    @SuppressWarnings("unchecked")
    public void setOldPropertyVector(Vector<Object> vector) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        for (Object object : vector) {
            Hashtable<Object, Object> hasht = (Hashtable<Object, Object>) object;
            setOldPropertyHash(hasht);
        }
        t.end();
        @SuppressWarnings("unused") TimerRecordFile timerFile = new TimerRecordFile("pool.bean", "property_detailsBean", "setProcess", t.duration());
    }

    /**
	 * this function converts new properties of vector in Td_property_details class format to update
	 * @param vector containing new properties
	 */
    @SuppressWarnings("unchecked")
    public void setNewPropertyVector(Vector<Object> vector) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        for (Object object : vector) {
            Hashtable<Object, Object> hasht = (Hashtable<Object, Object>) object;
            setNewPropertyHash(hasht);
        }
        t.end();
        @SuppressWarnings("unused") TimerRecordFile timerFile = new TimerRecordFile("pool.bean", "property_detailsBean", "setProcess", t.duration());
    }

    /**
	 * Update  Td_property_details
	 * @return stringbuffer containing details of updation
	 */
    public StringBuffer update() {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        StringBuffer sbrDTB = new StringBuffer();
        sqlDB.setBeforeCommit();
        objTdpl = new Td_property_details(sqlDB);
        objTdpl.setRequestParameter(reqParam);
        if (!OldTdPropertyList.isEmpty()) objTdpl.setOldPropertyVector(OldTdPropertyList);
        if (!NewTdPropertyList.isEmpty()) objTdpl.setNewPropertyVector(NewTdPropertyList);
        sbrDTB.append(objTdpl.getUpdate());
        boolean flag = objTdpl.getResult();
        sqlDB.setAfterCommit(flag);
        System.out.println(sbrDTB.toString());
        t.end();
        @SuppressWarnings({ "unused", "unused" }) TimerRecordFile timerFile = new TimerRecordFile("pool.bean", "td_property_details_Bean", "updateTd_property_details", t.duration());
        return sbrDTB;
    }

    /**
	 * this function converts hashtable to Td_property_details class format for view
	 * @param hashtable contains property
	 */
    public void setViewHashtable(Hashtable<Object, Object> hashtable) {
        Hashtable<Object, Object> PropertyHash = new Hashtable<Object, Object>();
        PropertyHash.putAll(hashtable);
        PropertyHash = gin.ConvertPVtoVT(PropertyHash);
        PropertyHash.putAll(gin.getHashMapOfValues(PropertyHash));
        PropertyHash.putAll(ConvertToLower.convertHashKey(PropertyHash));
        PropertyHash = gin.convertMasterId(PropertyHash, tableName, process);
        PropertyHash.putAll(gin.getHashMapOfValues(PropertyHash));
        ViewHashtable.putAll(PropertyHash);
    }

    /**
	 * view() calls getView() of Td_property_details class
	 * @return stringbuffer containing details of Td_property_details in message format
	 */
    public StringBuffer view() {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        StringBuffer sbrDTB = new StringBuffer();
        objTdpl = new Td_property_details(sqlDB);
        objTdpl.setRequestParameter(reqParam);
        objTdpl.setViewHashtable(ViewHashtable);
        sbrDTB.append(objTdpl.getView());
        t.end();
        @SuppressWarnings("unused") TimerRecordFile timerFile = new TimerRecordFile("pool.bean", "property_detailsBean", "updateProperty", t.duration());
        return sbrDTB;
    }

    @Override
    public StringBuffer delete() {
        return null;
    }
}
