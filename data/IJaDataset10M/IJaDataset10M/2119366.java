package coyousoft.javaee.el;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Customer {

    private String name;

    private int age;

    private Address[] address;

    private String[] myArray;

    private List<String> myList;

    private Map<String, Integer> myMap;

    public Customer() {
        age = 35;
        myArray = new String[] { "浮沉", "丑陋的中国人", "货币战争" };
        myList = new ArrayList<String>();
        myList.add("牛奶");
        myList.add("咖啡");
        myMap = new HashMap<String, Integer>();
        myMap.put("one", 1000);
        myMap.put("two", 2000);
    }

    public Address[] getAddress() {
        return address;
    }

    public void setAddress(Address[] address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Map<String, Integer> getMyMap() {
        return myMap;
    }

    public void setMyMap(Map<String, Integer> myMap) {
        this.myMap = myMap;
    }

    public List<String> getMyList() {
        return myList;
    }

    public void setMyList(List<String> myList) {
        this.myList = myList;
    }

    public String[] getMyArray() {
        return myArray;
    }

    public void setMyArray(String[] myArray) {
        this.myArray = myArray;
    }
}
