package com.mockturtlesolutions.snifflib.statmodeltools.database;

import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryConnectivity;
import com.mockturtlesolutions.snifflib.invprobs.DeclaredParameters;
import com.mockturtlesolutions.snifflib.invprobs.DeclaredVariables;
import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryStorage;
import com.mockturtlesolutions.snifflib.invprobs.StatisticalModel;

public interface StatisticalModelStorage extends RepositoryStorage, DeclaredParameters, DeclaredVariables {

    public StatisticalModel getStatisticalModel();

    /**
	The default StatisticalModel class for analysis of this storage.
	*/
    public String getStatisticalModelClassName();

    public void setStatisticalModelClassName(String clsname);
}
