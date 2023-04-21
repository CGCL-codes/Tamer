package com.google.code.ibear.select.fields;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface ISelectFieldsBuilder {

    /**
	 * if selectedFields is not blank return selectedFields. 
	 * If returnTypeBean is not blank,check beans fields and table fields to generate select part.
	 * else return all tables select part.
	 * @return
	 * @throws LinkageError 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IOException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 */
    public abstract String getSelectFieldsSQL(String sqlID) throws InstantiationException, IllegalAccessException, ClassNotFoundException, LinkageError, InvocationTargetException, NoSuchMethodException, IOException;
}
