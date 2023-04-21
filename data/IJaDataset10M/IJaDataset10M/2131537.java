package org.in4ama.datasourcemanager;

import java.util.ArrayList;
import org.in4ama.datasourcemanager.exception.DataSourceException;

/** This interface exposes informations about the structure of data sets. */
public interface DataSetTemplate {

    public static final int SIMPLE_TYPE = 0;

    public static final int COMPLEX_TYPE = 1;

    public static final int COLLECTION_TYPE = 2;

    public static final int ATTRIBUTE_TYPE = 3;

    /** Returns all children data sets. */
    ArrayList<DataSetTemplate> getChildren() throws DataSourceException;

    /** Returns the number of sub data sets. */
    int getChildCount() throws DataSourceException;

    /** Returns the name of this data set. */
    String getLongName();

    /** Returns the shortest version of the name. */
    String getName();

    /** Returns the full path to this data set. */
    String getLongPath();

    /** Returns the short path to this data set. */
    String getPath();

    /** Returns the value of this data set, 
	 * 'null' means that the value isn't available.*/
    Object getValue() throws DataSourceException;

    /** Returns the type of this data set. */
    int getType();

    /** Interface used to build child data set templates. */
    public static interface Builder {

        DataSetTemplate build(DataSet dataSet, DataSetTemplate parent);
    }
}
