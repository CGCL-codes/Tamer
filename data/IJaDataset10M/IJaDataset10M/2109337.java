package com.mockturtlesolutions.snifflib.datatypes.database;

import java.util.Vector;
import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryStorage;
import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryMaintenance;
import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryStorageNameQuery;
import com.mockturtlesolutions.snifflib.reposconfig.database.ReposConfig;
import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryElement;

public interface DblMatrixStorage extends RepositoryStorage {

    public void addEntry();

    public void setEmptyValue(String cls);

    public String getEmptyValue();

    public int[] getMatrixSize();

    public void setMatrixSize(int[] siz);

    public void clearMatrixSize();

    public void set(Object N, int element);

    public Object get(int element);

    public void clearContents();

    public void setContents(Vector in);

    public Vector getContents();

    /**
        Return a vector containing the non-empty elements.
        This method should return null if all the elements are nonempty. 
	*/
    public Vector getNonEmptyIndices();

    /**
        Return the number of non-empty elements
        */
    public int numberNonEmpty();

    /**
        Returns true if the value at the given index is non-empty.
        */
    public boolean isNonEmptyAt(int index);

    /**
	Return the index of the maximum element.
	*/
    public int findMax();

    /**
	Return the maximum element.
	*/
    public Object Max();

    /**
	Return the index of the minimum element.
	*/
    public int findMin();

    /**
	Return the maximum element.
	*/
    public Object Min();
}
