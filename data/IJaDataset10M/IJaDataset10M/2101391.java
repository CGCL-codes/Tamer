package com.dfruits.database;

import org.eclipse.datatools.connectivity.IConnectionProfile;

public interface IDefaultConnectionChooser {

    int getDefaultConnection(IConnectionProfile[] profiles);
}
