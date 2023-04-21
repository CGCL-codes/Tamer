package org.dozer.vo;

import java.util.List;
import java.util.Set;

/**
 * @author garsombke.franz
 * @author sullins.ben
 * @author tierney.matt
 * 
 */
public class TestReferenceObject extends BaseTestObject {

    List listA;

    private Object[] arrayToArrayCumulative;

    private Object[] arrayToArrayNoncumulative;

    private List listToArray;

    private int[] primitiveArray;

    private Integer[] primitiveArrayWrapper;

    private Set setToSet;

    private Car[] cars;

    private List vehicles;

    public Car[] getCars() {
        return cars;
    }

    public void setCars(Car[] cars) {
        this.cars = cars;
    }

    public Set getSetToSet() {
        return setToSet;
    }

    public void setSetToSet(Set setToSet) {
        this.setToSet = setToSet;
    }

    public Integer[] getPrimitiveArrayWrapper() {
        return primitiveArrayWrapper;
    }

    public void setPrimitiveArrayWrapper(Integer[] primitiveArrayWrapper) {
        this.primitiveArrayWrapper = primitiveArrayWrapper;
    }

    public int[] getPrimitiveArray() {
        return primitiveArray;
    }

    public void setPrimitiveArray(int[] primitiveArray) {
        this.primitiveArray = primitiveArray;
    }

    public List getListA() {
        return listA;
    }

    public void setListA(List listA) {
        this.listA = listA;
    }

    public Object[] getArrayToArrayCumulative() {
        return arrayToArrayCumulative;
    }

    public void setArrayToArrayCumulative(Object[] arrayToArray) {
        this.arrayToArrayCumulative = arrayToArray;
    }

    public List getListToArray() {
        return listToArray;
    }

    public void setListToArray(List listToArray) {
        this.listToArray = listToArray;
    }

    public Object[] getArrayToArrayNoncumulative() {
        return arrayToArrayNoncumulative;
    }

    public void setArrayToArrayNoncumulative(Object[] arrayToArrayNoncumulative) {
        this.arrayToArrayNoncumulative = arrayToArrayNoncumulative;
    }

    public List getVehicles() {
        return vehicles;
    }

    public void setVehicles(List vehicles) {
        this.vehicles = vehicles;
    }
}
