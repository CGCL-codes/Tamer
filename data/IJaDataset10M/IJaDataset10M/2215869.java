package com.fisoft.phucsinh.phucsinhsrv.service.autoaccounting;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import javax.ejb.Local;

/**
 *
 * @author truong ha
 */
@Local
public interface ITypeEnumManager {

    /**
     * Get the map of value and label from an enum full qualified name
     * @param fullQualifiedName
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public Map<Integer, String> getValues(String fullQualifiedName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException;

    /**
     * Get the map of value and label from a enum
     * @param c
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public Map<Integer, String> getValues(Class c) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;
}
