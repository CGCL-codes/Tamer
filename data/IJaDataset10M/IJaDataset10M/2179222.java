package com.ec.service;

public interface _ProductServiceOperations {

    DateTime[] getTimes(Ice.Current __current);

    DateTime getCurrentDate(Ice.Current __current);

    void printCurrentDate(DateTime currentDate, Ice.Current __current);

    void printString(String s, Ice.Current __current);

    String[] queryBySql(String sql, Ice.Current __current);

    String[] queryBySqlNotType(String sql, Ice.Current __current);

    boolean addDirectory(int parentid, float tax, String dirName, Ice.Current __current);

    boolean updateDirectory(int id, String dirName, float tax, Ice.Current __current);
}
