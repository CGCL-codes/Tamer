package net.cygeek.tech.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import net.cygeek.tech.client.data.Payperiod;
import java.util.Date;

public interface PayperiodServiceAsync {

    void getPayperiods(AsyncCallback async);

    void addPayperiod(Payperiod mPayperiod, boolean isNew, AsyncCallback async);

    void deletePayperiod(String payperiodCode, AsyncCallback async);

    void getPayperiod(String payperiodCode, AsyncCallback async);
}
