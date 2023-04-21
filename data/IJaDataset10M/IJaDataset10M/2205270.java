package com.calipso.reportgenerator.reportcalculator;

import com.calipso.reportgenerator.common.exception.InfoException;
import java.util.*;

/**
 * Estructura intermedia en la que se almacenan los datos provenientes del IDataSource
 * En el futuro esta estructura puede ser directamente reemplazada por el IDataSource
 */
public interface Matrix extends IDataSource {

    public void add(Object[] row) throws InfoException;

    public Iterator iterator() throws InfoException;

    public boolean isEmpty() throws InfoException;

    public int size() throws InfoException;

    public void setColumNames(Vector columnNames);

    public void addAll(Matrix sourceMatrix) throws InfoException;
}
