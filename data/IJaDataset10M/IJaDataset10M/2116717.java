package com.mockturtlesolutions.snifflib.statmodeltools.database;

import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryStorage;
import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryMaintenance;
import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryStorageNameQuery;
import com.mockturtlesolutions.snifflib.reposconfig.database.ReposConfig;
import com.mockturtlesolutions.snifflib.statmodeltools.database.StatisticalModelStorage;
import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryElement;
import com.mockturtlesolutions.snifflib.datatypes.DblParamSet;
import java.util.List;
import java.util.Vector;

public interface ParameterSetStorage extends RepositoryStorage {

    public Vector parameterNames();

    public void addParam(String name);

    public void removeParam(String name);

    public String getParam(String name);

    public void setParam(String name, String value);

    public boolean hasParameter(String s);

    public void clearParameters();

    public DblParamSet getDblParamSet();

    public void setUsingDblParamSet(DblParamSet X);

    public void copyParameters(RepositoryStorage that);
}
