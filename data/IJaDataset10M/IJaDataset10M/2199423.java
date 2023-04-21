package com.obdobion.algebrain;

import java.util.*;

public interface EquationSupport {

    Hashtable resolveRate(String tableName, java.sql.Date baseDate, double tableKey) throws EquException;

    double resolveRate(String tableName, java.sql.Date baseDate, String key1, String key2, String key3, String key4, String key5) throws EquException;

    Object resolveVariable(String variableName, java.sql.Date baseDate) throws EquException;
}
