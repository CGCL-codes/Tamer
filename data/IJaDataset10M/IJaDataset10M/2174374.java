package net.infopeers.restlant.commons.populate;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class TestClass1 {

    private String name;

    private int intValue;

    private Long longValue;

    private Date dateValue;

    private Long canToStr;

    private long primitiveToStr;

    private ArrayList longCollectionToNumberArray;

    private Long[] longArrayToNumberCollection;

    private LinkedList linkedListToArrayList;

    private TestClass1 testObject;

    private TestClass1[] testObjectArray2linkedList;

    /**
	 * @return the testClass1
	 */
    public TestClass1 getTestObject() {
        return testObject;
    }

    /**
	 * @param testClass1 the testClass1 to set
	 */
    public void setTestObject(TestClass1 testClass1) {
        this.testObject = testClass1;
    }

    /**
	 * @return the collection
	 */
    public ArrayList getLongCollectionToNumberArray() {
        return longCollectionToNumberArray;
    }

    /**
	 * @param longCollectionToNumberArray the longCollectionToNumberArray to set
	 */
    public void setLongCollectionToNumberArray(ArrayList longCollectionToNumberArray) {
        this.longCollectionToNumberArray = longCollectionToNumberArray;
    }

    /**
	 * @return the canToStr2
	 */
    public long getPrimitiveToStr() {
        return primitiveToStr;
    }

    /**
	 * @param canToStr2 the canToStr2 to set
	 */
    public void setPrimitiveToStr(long catToStr2) {
        this.primitiveToStr = catToStr2;
    }

    public String getName() {
        return name;
    }

    /**
	 * @return the dateValue
	 */
    public Date getDateValue() {
        return dateValue;
    }

    /**
	 * @param dateValue the dateValue to set
	 */
    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    /**
	 * @return the intValue
	 */
    public int getIntValue() {
        return intValue;
    }

    /**
	 * @param intValue the intValue to set
	 */
    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    /**
	 * @return the longValue
	 */
    public Long getLongValue() {
        return longValue;
    }

    /**
	 * @param longValue the longValue to set
	 */
    public void setLongValue(Long longValue) {
        this.longValue = longValue;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the canToStr
	 */
    public Long getCanToStr() {
        return canToStr;
    }

    /**
	 * @param canToStr the canToStr to set
	 */
    public void setCanToStr(Long canToStr) {
        this.canToStr = canToStr;
    }

    /**
	 * @return the longArrayToNumberCollection
	 */
    public Long[] getLongArrayToNumberCollection() {
        return longArrayToNumberCollection;
    }

    /**
	 * @param longArrayToNumberCollection the longArrayToNumberCollection to set
	 */
    public void setLongArrayToNumberCollection(Long[] longArrayToNumberCollection) {
        this.longArrayToNumberCollection = longArrayToNumberCollection;
    }

    /**
	 * @return the linkedListToArrayList
	 */
    public LinkedList getLinkedListToArrayList() {
        return linkedListToArrayList;
    }

    /**
	 * @param linkedListToArrayList the linkedListToArrayList to set
	 */
    public void setLinkedListToArrayList(LinkedList linkedListToArrayList) {
        this.linkedListToArrayList = linkedListToArrayList;
    }

    /**
	 * @return the testObjectArray2linkedList
	 */
    public TestClass1[] getTestObjectArray2linkedList() {
        return testObjectArray2linkedList;
    }

    /**
	 * @param testObjectArray2linkedList the testObjectArray2linkedList to set
	 */
    public void setTestObjectArray2linkedList(TestClass1[] testObjectArray2linkedList) {
        this.testObjectArray2linkedList = testObjectArray2linkedList;
    }
}
