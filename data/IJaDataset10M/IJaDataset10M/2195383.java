package org.santeplanning.state;

import org.santeplanning.state.EditServiceState.AssociateCycleProgrammeTravailAndDay;
import org.stateengine.storage.IDBConnection;

public interface RowCounter {

    public void count(IDBConnection db, AssociateCycleProgrammeTravailAndDay associate);

    public String getResume();
}
