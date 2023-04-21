package com.centraview.support.knowledgebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.naming.CommunicationException;
import com.centraview.common.CVUtility;
import com.centraview.common.DisplayList;
import com.centraview.common.EntityListElement;
import com.centraview.support.common.SupportConstantKeys;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;

public class KnowledgebaseList extends DisplayList {

    /**
	 *
	 * Constructor
	 */
    public KnowledgebaseList() {
        columnMap = new HashMap();
        columnMap.put("Name", new Integer(5));
        columnMap.put("DateCreated", new Integer(10));
        columnMap.put("DateUpdated", new Integer(45));
        setPrimaryMember("Name");
    }

    /**
	 * gets the dirty flag
	 *
	 * @return    boolean
	 */
    public boolean getDirtyFlag() {
        return dirtyFlag;
    }

    /**
	 * sets the Dirty Flag
	 *
	 * @param   flag
	 */
    public void setDirtyFlag(boolean flag) {
        dirtyFlag = flag;
    }

    /**
	 * gets the StartAt
	 *
	 * @return     int
	 */
    public int getStartAT() {
        return startAT;
    }

    /**
	 * gets the End At
	 *
	 * @return  int
	 */
    public int getEndAT() {
        return endAT;
    }

    /**
	 * gets The Begin Index
	 *
	 * @return     int
	 */
    public int getBeginIndex() {
        return beginIndex;
    }

    /**
	 *
	 * gets the End Index
	 * @return   int
	 */
    public int getEndIndex() {
        return endIndex;
    }

    /**
	 * sets the RecordsPerpage
	 *
	 * @param   i
	 */
    public void setRecordsPerPage(int i) {
        recordsPerPage = i;
    }

    /**
	 * sets the TotalNoOfrecords
	 *
	 * @param   i
	 */
    public void setTotalNoOfRecords(int i) {
        totalNoOfRecords = i;
    }

    /**
	 * sets the List Id
	 *
	 * @param   l
	 */
    public void setListID(long l) {
        super.ListID = l;
    }

    /**
	 * gets the ColumnMap
	 *
	 * @return   HashMap
	 */
    public HashMap getColumnMap() {
        return columnMap;
    }

    /**
	 * sets the sort member
	 *
	 * @param   s
	 */
    public void setSortMember(String s) {
        sortMember = s;
    }

    /**
	 * sets the list type
	 *
	 * @param   s
	 */
    public void setListType(String s) {
        super.listType = s;
    }

    /**
	 * sets the Search String
	 *
	 * @return    String
	 */
    public String getSearchString() {
        return searchString;
    }

    /**
	 * sets the Search String
	 *
	 * @param   s
	 */
    public void setSearchString(String s) {
        searchString = s;
    }

    /**
	 * gets the getListType
	 *
	 * @return   String
	 */
    public String getListType() {
        return listType;
    }

    /**
	 * gets the PrimaryMember type
	 *
	 * @return     String
	 */
    public String getPrimaryMemberType() {
        return PrimaryMemberType;
    }

    /**
	 * sets the Primary Member Type
	 *
	 * @param   s
	 */
    public void setPrimaryMemberType(String s) {
        PrimaryMemberType = s;
    }

    /**
	 * gets the Total No of Records
	 *
	 * @return     int
	 */
    public int getTotalNoofRecords() {
        return totalNoofRecords;
    }

    /**
	 * gets Primary Table
	 *
	 * @return    String
	 */
    public String getPrimaryTable() {
        return primaryTable;
    }

    /**
	 * gets the Sort Member
	 *
	 * @return   String
	 */
    public String getSortMember() {
        return sortMember;
    }

    /**
	 * deletes Element
	 *
	 * @param   key
	 */
    public void deleteElement(int indvID, String key) {
    }

    public ArrayList deleteElement(int indvID, String rowId[]) {
        ArrayList resultDeleteLog = new ArrayList();
        return resultDeleteLog;
    }

    /**
	 * Duplicate Element
	 *
	 */
    public void duplicateElement() {
    }

    /**
	 * sets Primary Member
	 *
	 * @param   s
	 */
    public void setPrimaryMember(String s) {
        primaryMember = s;
    }

    /**
	 * sets the Primary Table
	 *
	 * @param   s
	 */
    public void setPrimaryTable(String s) {
        primaryTable = s;
    }

    /**
	 * adds an Entity Element
	 *
	 * @param   entitylistelement
	 */
    public void addEntityElement(EntityListElement entitylistelement) {
    }

    /**
	 * gets the EntityElement
	 *
	 * @param   s
	 */
    public void getEntityElement(String s) {
    }

    /**
	 * sets the Start At
	 *
	 * @param   i
	 */
    public void setStartAT(int i) {
        startAT = i;
    }

    /**
	 * sets the End AT
	 *
	 * @param   i
	 */
    public void setEndAT(int i) {
        endAT = i;
    }

    /**
	 * sets the Begin Index
	 *
	 * @param   i
	 */
    public void setBeginIndex(int i) {
        beginIndex = i;
    }

    /**
	 * sets the End Index
	 *
	 * @param   i
	 */
    public void setEndIndex(int i) {
        endIndex = i;
    }

    /**
	 * gets the Primary Member
	 *
	 * @return     String
	 */
    public String getPrimaryMember() {
        return primaryMember;
    }

    /**
	 * sets the vecCategory vector
	 *
	 * @param   vec
	 */
    public void setCategoryStructure(Vector vec) {
        this.vecCategory = vec;
    }

    /**
	 * gets the Category Structure
	 *
	 * @return   Vector
	 */
    public Vector getCategoryStructure() {
        return vecCategory;
    }

    public void setCurrentCategoryID(int curCategoryID) {
        this.curCategoryID = curCategoryID;
    }

    public int getCurrentCategoryID() {
        return this.curCategoryID;
    }

    public void deleteElement(int userID, String key, String typeOfDoc, int currCatID) throws CommunicationException {
        int elementID = Integer.parseInt(key);
        try {
            SupportFacadeHome supportFacade = (SupportFacadeHome) CVUtility.getHomeObject("com.centraview.support.supportfacade.SupportFacadeHome", "SupportFacade");
            SupportFacade remote = (SupportFacade) supportFacade.create();
            remote.setDataSource(this.dataSource);
            if (typeOfDoc.equals(SupportConstantKeys.KBELEMENT)) {
                remote.deleteKB(userID, elementID);
            } else if (typeOfDoc.equals(SupportConstantKeys.CATEGORY)) {
                remote.deleteCategory(userID, elementID);
            }
        } catch (Exception e) {
            System.out.println("[Exception][KnowledgebaseList.deleteElement] Exception Thrown: " + e);
        }
        this.setDirtyFlag(true);
    }

    private String sortMember;

    private String primaryMember;

    private String primaryTable = "knowledgebase";

    private String PrimaryMemberType = "kbid";

    private int totalNoofRecords;

    private int beginIndex;

    private int endIndex;

    private int startAT;

    private int endAT;

    protected static boolean dirtyFlag = false;

    private Vector vecCategory = new Vector();

    private int curCategoryID;
}
