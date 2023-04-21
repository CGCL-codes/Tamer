package org.usca.workshift.gwt.workshiftapp.client.workshiftmanagement;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.usca.workshift.database.model.Workshift;
import org.usca.workshift.database.model.House;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: danny
 * Date: Mar 27, 2008
 * Time: 4:06:07 AM
 * To change this template use File | Settings | File Templates.
 */
public interface GetWorkshiftInfoAsync {

    void saveWorkshift(Workshift ws, AsyncCallback<Boolean> async);

    void getWorkshift(House house, AsyncCallback<List<Workshift>> async);

    void saveWorkshift(Workshift wsCategory, House h, AsyncCallback<Workshift> async);

    void deleteWorkshift(Workshift wsCategory, AsyncCallback<Boolean> async);
}
